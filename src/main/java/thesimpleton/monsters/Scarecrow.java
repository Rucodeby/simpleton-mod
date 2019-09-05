package thesimpleton.monsters;

import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;

public class Scarecrow extends AbstractMonster {
  public static final String ID = TheSimpletonMod.makeID("ScarecrowMonster");
  private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
  public static final String NAME = monsterStrings.NAME;
  public static final String[] DIALOG = monsterStrings.DIALOG;
  public static final String[] MOVES = monsterStrings.MOVES;
  public static final String IMG_PATH = "monsters/scarecrow.png";

  private static final int HP_MIN = 90;
  private static final int HP_MAX = 100;
  private static final int A_8_HP_MIN = 140;
  private static final int A_8_HP_MAX = 155;
  private static final float HB_X = 0.0F;
  private static final float HB_Y = 0.0F;
  private static final float HB_W = 280.0F;
  private static final float HB_H = 298.0F;

  private static final int REGEN_AMOUNT = 8;
  private static final int HEAL_AMOUNT = 10;
  private static final int HEAL_AMOUNT_DELTA = 2;

  private static final int A_18_REGEN_AMOUNT = 16;
  private static final int A_18_HEAL_AMOUNT = 8;
  private static final int A_18_HEAL_AMOUNT_DELTA = 4;

  private static final int ARTIFACT_AMOUNT = 1;
  private static final int FRAIL_AMOUNT = 2;
  private static final int WEAK_AMOUNT = 2;

  private static final int FLEX_AMOUNT = 2;

  private final int regenAmount;
  private final int healAmount;
  private final int healAmountDelta;

  private boolean lastDebuffWeak;
  private AbstractMonster lastMonsterHealed;
  private AbstractMonster lastMonsterBuffed;

  public Scarecrow(float x, float y) {
    super(NAME, ID, 25, HB_X, HB_Y, HB_W, HB_H, TheSimpletonMod.getResourcePath(IMG_PATH), x, y);

    if (AbstractDungeon.ascensionLevel >= 8) {
      this.setHp(A_8_HP_MIN, A_8_HP_MAX);

    } else {
      this.setHp(HP_MIN, HP_MAX);
    }


    if (AbstractDungeon.ascensionLevel >= 18) {
      this.regenAmount = A_18_REGEN_AMOUNT;
      this.healAmount = A_18_HEAL_AMOUNT;
      this.healAmountDelta = A_18_HEAL_AMOUNT_DELTA;
    } else {
      this.regenAmount = REGEN_AMOUNT;
      this.healAmount = HEAL_AMOUNT;
      this.healAmountDelta = HEAL_AMOUNT_DELTA;
    }

    lastMonsterHealed = null;
    lastDebuffWeak = AbstractDungeon.aiRng.randomBoolean();
  }

  public void usePreBattleAction() {
    // TODO: apply flammable power
//
//    AbstractDungeon.actionManager.addToBottom(
//        new ApplyPowerAction(this, this,
//            new RegenerateMonsterPower(this, this.regenAmount), this.regenAmount));
  }

  @Override
  public void takeTurn() {
    switch (this.nextMove) {
      case 1:
        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 0.5F, 2.0F));
        break;
      case 2:
        this.flashIntent();

        if (!MonsterUtil.getDamagedMonsters().isEmpty()) {
          MonsterUtil.allOtherMonsters(this).forEach(m -> AbstractDungeon.actionManager.addToBottom(
              new HealAction(m, this, this.healAmount)));
        } else if (MonsterUtil.allOtherMonsters(this).stream()
            .anyMatch(m -> !m.hasPower(ArtifactPower.POWER_ID))) {
          MonsterUtil.allOtherMonsters(this).stream()
              .filter(m -> !m.hasPower(ArtifactPower.POWER_ID))
              .forEach(m -> AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m , this,
                  new ArtifactPower(lastMonsterBuffed, ARTIFACT_AMOUNT), ARTIFACT_AMOUNT)));
        } else {
          MonsterUtil.allOtherMonsters(this).forEach(m -> {
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(m,this, new StrengthPower(m, FLEX_AMOUNT), FLEX_AMOUNT));
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(m,this, new LoseStrengthPower(m, FLEX_AMOUNT), FLEX_AMOUNT));
          });
        }

        break;
      case 3:
        this.flashIntent();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this,
            new FrailPower(AbstractDungeon.player, FRAIL_AMOUNT, true), FRAIL_AMOUNT));
        break;
      case 4:
        this.flashIntent();
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this,
            new WeakPower(AbstractDungeon.player, WEAK_AMOUNT, true), WEAK_AMOUNT));
        break;
    }
    AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
  }

  @Override
  public void deathReact() {
    TheSimpletonMod.logger.info("Scarecrow::deathReact called.");

    if ((!this.isDead) && (!this.isDying)) {
      if (MonsterUtil.otherMonstersAreAllDead(this)) {
        AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(this));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExplosionSmallEffect(this.hb.cX, this.hb.cY), 0.1F));
        AbstractDungeon.actionManager.addToBottom(new SuicideAction(this));
      }
    }
  }

  @Override
  protected void getMove(int num) {
    Logger logger = TheSimpletonMod.logger;
    if (!this.moveHistory.isEmpty()) {
       logger.info("Scarecrow::getMove last move:" + (int) this.moveHistory.get(this.moveHistory.size() - 1));
    } else {
      logger.info("Scarecrow::getMove no last move");
    }

    if (!moveHistory.isEmpty() && !lastMove((byte) 2)) {
      logger.info("Scarecrow::getMove next move: HEAL/BUFF");
      this.setMove((byte) 2, Intent.BUFF);
    } else if (!lastMove((byte) 3) && !lastMove((byte) 4)) {
      logger.info("Scarecrow::getMove last move FRAIL? " + lastMove((byte) 3) + " ; last move WEAK? "
        + lastMove((byte) 4));
      this.setMove((byte) (this.lastDebuffWeak ? 3 : 4), Intent.DEBUFF);

      logger.info("Scarecrow::getMove next move: " + (this.lastDebuffWeak ? "FRAIL" : "WEAK"));

      this.lastDebuffWeak = !this.lastDebuffWeak;
    } else {
      logger.info("Scarecrow::getMove next move: TALK");
      // TODO: do something scary
      this.setMove((byte) 1, Intent.NONE);
    }
  }
}