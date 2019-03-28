package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.cards.skill.AbstractHarvestCard;

public class PlantOnionPower extends AbstractCropPower {

  public static final String POWER_ID = "TheSimpletonMod:PlantOnionPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static AbstractPower.PowerType POWER_TYPE = AbstractPower.PowerType.BUFF;
  public static final String IMG = "plantonion.png";

  private static int BASE_WEAK_PER_STACK = 1;
  private static int weakPerStack = 1;

  public PlantOnionPower(AbstractCreature owner, int amount) {
    super(IMG, owner, amount);

    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;
    this.weakPerStack = BASE_WEAK_PER_STACK;
    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.weakPerStack + DESCRIPTIONS[1];
  }

  //TODO: AbstractCard should be an AbstractHarvestCard, with harvestAmount, harvestEffect, etc.
  public void onUseCard(AbstractCard card, UseCardAction action) {
    if (card.hasTag(TheSimpletonCardTags.HARVEST) && card instanceof AbstractHarvestCard && ((AbstractHarvestCard)card).autoHarvest) {
      harvest(((AbstractHarvestCard) card).isHarvestAll(), ((AbstractHarvestCard) card).getHavestAmount());
    }
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }

  @Override
  public void harvest(boolean harvestAll, int maxHarvestAmount) {

    if  (amount > 0) {
      final int harvestAmount = Math.min(this.amount, harvestAll ? this.amount : maxHarvestAmount);

      if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
        this.flash();

        final long numTargetMonsters = AbstractDungeon.getMonsters().monsters.stream()
            .filter(m -> !m.isDead && !m.isDying)
            .count();

        System.out.println("PlantOnionPower.harvest :: numTargetMonsters: " + numTargetMonsters);

        //TODO: reconcile with getCurrRoom.monsters.monsters version
        AbstractDungeon.getMonsters().monsters.stream()
            .filter(m -> !m.isDead && !m.isDying)
            .forEach(m -> applyWeakPower(m, harvestAmount));
      }

      amount -= harvestAmount;

      if (amount == 0) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
      }
    }
  }

  private void applyWeakPower(AbstractMonster m, int numApplications) {
    final int weakAmount =  this.weakPerStack * numApplications;

    AbstractDungeon.actionManager.addToBottom(
        new ApplyPowerAction(m, this.owner, new WeakPower(m, weakAmount, false),weakAmount));
  }

}