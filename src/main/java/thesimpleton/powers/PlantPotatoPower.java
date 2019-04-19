package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thesimpleton.cards.HarvestCard;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.cards.TheSimpletonCardTags;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Potatoes;
import thesimpleton.powers.utils.Crop;
import thesimpleton.relics.HotPotato;

public class PlantPotatoPower extends AbstractCropPower {
  public static final Crop enumValue = Crop.POTATOES;

  public static final String POWER_ID = "TheSimpletonMod:PlantPotatoPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "plantpotato.png";
  public static final CardRarity cropRarity = CardRarity.COMMON;
  private static final AbstractCropPowerCard powerCard = new Potatoes();

  public PlantPotatoPower(AbstractCreature owner, int amount) {
    super(enumValue, NAME, POWER_ID, POWER_TYPE, DESCRIPTIONS, IMG, owner, cropRarity, powerCard, amount);
    this.name = NAME;
    updateDescription();
    logger.debug("MAKIN' POTATOES (instantiating plantpotatopower). name: " + NAME);
  }

  @Override
  public void updateDescription() {
    this.description = getPassiveDescription() + " NL " + DESCRIPTIONS[0];
  }

  //TODO: AbstractCard should be an HarvestCard, with harvestAmount, harvestEffect, etc.
  public void onUseCard(AbstractCard card, UseCardAction action) {
    if (card.hasTag(TheSimpletonCardTags.HARVEST) && card instanceof HarvestCard && ((HarvestCard)card).isAutoHarvest()) {
       harvest(((HarvestCard) card).isHarvestAll(), ((HarvestCard) card).getHarvestAmount());
    }
  }

  @Override
  public void harvest(boolean harvestAll, int maxHarvestAmount) {
    super.harvest(harvestAll, maxHarvestAmount);

    if  (amount > 0 && this.owner == SimpletonUtil.getPlayer()) {
      final int harvestAmount = Math.min(this.amount, harvestAll ? this.amount : maxHarvestAmount);

      this.flash();

      if (SimpletonUtil.getPlayer().hasRelic(HotPotato.ID)) {
        SimpletonUtil.getPlayer().getRelic(HotPotato.ID).flash();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(SimpletonUtil.FLAMING_SPUD, harvestAmount));
      } else {
        logger.debug("PlantPotatoPower.harvest : owner does not have Hot Potato. owner: " + owner.name);

        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(SimpletonUtil.SPUD_MISSILE, harvestAmount));
      }

      if (harvestAmount < amount) {
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, POWER_ID, harvestAmount));
//      amount -= harvestAmount;
      } else {
        logger.debug("PlantPotatoPower.harvest : now at zero stacks, removing power");
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
      }
    }
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}