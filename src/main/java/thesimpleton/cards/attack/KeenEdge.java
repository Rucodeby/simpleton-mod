package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import thesimpleton.TheSimpletonMod;
import thesimpleton.enums.AbstractCardEnum;

public class KeenEdge extends CustomCard {
  public static final String ID = "TheSimpletonMod:KeenEdge";
  public static final String NAME;
  public static final String DESCRIPTION;
  public static final String UPGRADE_DESCRIPTION;
  public static final String[] EXTENDED_DESCRIPTION;
  public static final String IMG_PATH = "cards/keenedge.png";

  private static final CardStrings cardStrings;

  private static final CardType TYPE = CardType.ATTACK;
  private static final CardRarity RARITY = CardRarity.COMMON;
  private static final CardTarget TARGET = CardTarget.ENEMY;

  private static final int COST = 1;
  private static final int DAMAGE = 9;
  private static final int COST_INCREASE = 1;
  private static final int DAMAGE_INCREASE = 9;

  public KeenEdge() {
    super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, getDescription(false), TYPE,
        AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
    this.baseDamage = this.damage = DAMAGE;
    this.baseMagicNumber = this.magicNumber = DAMAGE_INCREASE;
    this.isEthereal = true;
  }

  public void use(AbstractPlayer p, AbstractMonster m) {

    AbstractGameEffect effect = new FlashAtkImgEffect(m.hb.cX, m.hb.cY,
        AbstractGameAction.AttackEffect.SLASH_DIAGONAL, true);
    AbstractDungeon.effectList.add(effect);
    AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_SCIMITAR_1"));

    AbstractDungeon.actionManager.addToBottom(
        new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));

    this.updateOnUse();
  }

  private void updateOnUse() {
    this.upgradeDamage(DAMAGE_INCREASE);
    updateCost(COST_INCREASE);
  }

  @Override
  public AbstractCard makeCopy() {
    return new KeenEdge();
  }

  @Override
  public void upgrade() {
    if (!this.upgraded) {
      this.upgradeName();
      this.isEthereal = false;
      this.rawDescription = getDescription(true);
      initializeDescription();
    }
  }

  private static String getDescription(boolean isUpgraded) {
    return (isUpgraded ? UPGRADE_DESCRIPTION : DESCRIPTION) + COST_INCREASE  + EXTENDED_DESCRIPTION[0];
  }

  static {
    cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    NAME = cardStrings.NAME;
    DESCRIPTION = cardStrings.DESCRIPTION;
    UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    EXTENDED_DESCRIPTION = cardStrings.EXTENDED_DESCRIPTION;
  }
}