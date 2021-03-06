package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.cards.attack.GiantTurnip;

public class TurnipCrop extends AbstractCrop {
  public static final Crop CROP_ENUM = Crop.TURNIPS;

  public TurnipCrop() {
    super(CROP_ENUM);
  }

  protected int calculateHarvestAmount(int amount, int maxAmount, boolean harvestAll) { return this.getAmount();  }

  protected int harvestAction(int harvestAmount) {
    AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(new GiantTurnip(harvestAmount), 1));
    return harvestAmount;
  }
}