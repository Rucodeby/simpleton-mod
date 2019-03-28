package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.cards.attack.GiantTurnip;
import thesimpleton.cards.skill.AbstractHarvestCard;

public class PlantTurnipPower extends AbstractCropPower {

  public static final String POWER_ID = "TheSimpletonMod:PlantTurnipPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static AbstractPower.PowerType POWER_TYPE = AbstractPower.PowerType.BUFF;
  public static final String IMG = "plantturnip.png";
  private static final boolean IS_HARVEST_ALL = true;

  public PlantTurnipPower(AbstractCreature owner, int amount) {
    super(IMG, owner, amount, IS_HARVEST_ALL);

    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;

    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0];
  }
  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }

  public void onUseCard(AbstractCard card, UseCardAction action) {
    if (card.hasTag(TheSimpletonCardTags.HARVEST) && card instanceof AbstractHarvestCard && ((AbstractHarvestCard)card).autoHarvest) {
      harvest(true, this.amount);
    }
  }

  @Override
  public void harvest(boolean harvestAll, int maxHarvestAmount) {
    if  (amount > 0) {
      final int harvestAmount = this.amount;

      this.flash();
      AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new GiantTurnip(harvestAmount), 1));
      amount -= harvestAmount;

      if (amount == 0) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
      }
    }
  }
}