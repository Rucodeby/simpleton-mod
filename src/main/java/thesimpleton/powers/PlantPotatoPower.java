package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thesimpleton.cards.CurseUtil;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Potatoes;
import thesimpleton.cards.skill.AbstractHarvestCard;

public class PlantPotatoPower extends AbstractCropPower {

  public static final String POWER_ID = "TheSimpletonMod:PlantPotatoPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "plantpotato.png";
  public static final CardRarity cropRarity = CardRarity.COMMON;
  private static final AbstractCropPowerCard powerCard = new Potatoes();

  public PlantPotatoPower(AbstractCreature owner, int amount) {
    super(IMG, owner, cropRarity, powerCard, amount);

    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;

    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0];
  }

  //TODO: AbstractCard should be an AbstractHarvestCard, with harvestAmount, harvestEffect, etc.
  public void onUseCard(AbstractCard card, UseCardAction action) {
    if (card.hasTag(TheSimpletonCardTags.HARVEST) && card instanceof AbstractHarvestCard && ((AbstractHarvestCard)card).autoHarvest) {
       harvest(((AbstractHarvestCard) card).isHarvestAll(), ((AbstractHarvestCard) card).getHarvestAmount());
    }
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }

  @Override
  public void harvest(boolean harvestAll, int maxHarvestAmount) {
    super.harvest(harvestAll, maxHarvestAmount);

    if  (amount > 0) {
      final int harvestAmount = Math.min(this.amount, harvestAll ? this.amount : maxHarvestAmount);

      this.flash();
      AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(CurseUtil.SPUD_MISSILE, harvestAmount));
      amount -= harvestAmount;

      if (amount == 0) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
      }
    }
  }
}
