package thesimpleton.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import com.megacrit.cardcrawl.vfx.combat.*;

public class HaymakerSplashAction extends AbstractGameAction {

  private final int VULNERABLE_AMOUNT;
  private final AbstractPlayer p;
  private DamageInfo info;

  public HaymakerSplashAction(AbstractPlayer p, AbstractCreature target, DamageInfo info, int vulnerableAmount) {
    this.setValues(target, info);
    this.actionType = ActionType.DEBUFF;
    this.attackEffect = AttackEffect.BLUNT_HEAVY;
    this.duration = 0.1F;
    this.info = info;
    this.p = p;
    this.VULNERABLE_AMOUNT = vulnerableAmount;
  }

  @Override
  public void update() {

    if (this.duration == 0.1F && this.target != null) {
      this.target.damage(this.info);

      AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, this.attackEffect));
      AbstractDungeon.effectList.add(new DamageImpactCurvyEffect(this.target.hb.cX, this.target.hb.cY, Color.GOLDENROD, false));

      System.out.println("Action.HaymakerSplashAction.update: checking if target was killed");

      if ((((AbstractMonster) this.target).isDying || this.target.currentHealth <= 0)
          && !this.target.halfDead && !this.target.hasPower("Minion")) {

        System.out.println("Action.HaymakerSplashAction.update: monster killed. Applying debuffs");
        AbstractDungeon.getMonsters().monsters.stream()
          .filter(mo -> !mo.isDeadOrEscaped())
          .forEach(mo -> {
              AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(
                  mo, this.p, new VulnerablePower(mo, this.VULNERABLE_AMOUNT, false),
                  this.VULNERABLE_AMOUNT, true, AbstractGameAction.AttackEffect.NONE));
          });
        } else {
            System.out.println("Action.HaymakerSplashAction.update: monster not killed. Debuffs not applied.");
        }

      if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
        AbstractDungeon.actionManager.clearPostCombatActions();
      }
    } else {
      System.out.println("Action.HaymakerSplashAction.update: no target or no duration.");
    }


    this.tickDuration();
  }

}