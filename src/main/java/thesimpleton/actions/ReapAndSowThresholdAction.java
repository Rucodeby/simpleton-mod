package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.cards.skill.Harvest;
import thesimpleton.powers.AbstractCropPower;

public class ReapAndSowThresholdAction extends AbstractGameAction {
  private final int harvestThreshold;
  private final boolean upgraded;

  public ReapAndSowThresholdAction(int harvestThreshold, boolean upgraded) {
    this.actionType = ActionType.SPECIAL;
    this.harvestThreshold = harvestThreshold;
    this.upgraded = upgraded;
  }

  @Override
  public void update() {
    if (AbstractCropPower.getActiveCropPowers().stream().anyMatch(pow -> pow.amount >= harvestThreshold)) {
      AbstractCard card = new Harvest();
      if (this.upgraded) {
        card.upgrade();
      }
      AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(card, 1));
    }
    this.isDone = true;
  }
}