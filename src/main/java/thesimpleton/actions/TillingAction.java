package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;

import java.util.List;
import java.util.stream.Collectors;

public class TillingAction extends AbstractGameAction {
  private AbstractPlayer p;

  private AbstractGameAction.ActionType ACTION_TYPE = ActionType.CARD_MANIPULATION;

  public TillingAction(int numPowers) {
    TheSimpletonMod.logger.debug("TillingAction: constructing with numPowers: " + numPowers);


    this.p = AbstractDungeon.player;
    setValues(this.p,  this.p, numPowers);

    TheSimpletonMod.logger.debug("TillingAction: constructor setValues. this.amount: " + this.amount);

    this.actionType = ACTION_TYPE;
    this.amount = numPowers;
    this.duration = Settings.ACTION_DUR_MED;

    TheSimpletonMod.logger.debug("TillingAction: constructed. this.amount: " + this.amount);

  }

  public void update() {
    if (this.duration != Settings.ACTION_DUR_MED) {
      if (AbstractDungeon.gridSelectScreen.selectedCards.size() != 0) {
        //TODO: clean up: find the one (first) selected card and add it
        for (AbstractCard c : AbstractDungeon.gridSelectScreen.selectedCards) {
          c.unhover();
          c.setCostForTurn(0);
          AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(c, 1));
        }
        this.p.hand.refreshHandLayout();

        this.tickDuration();
        this.isDone = true;
        return;
      }
    }

    final CardGroup cardGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);

    List<AbstractCropPowerCard> cards = AbstractCropPowerCard.getRandomCropPowerCards(this.amount);

    for (final AbstractCropPowerCard c2 : cards) {
      cardGroup.addToRandomSpot(c2);
    }

    if (cardGroup.size() == 0) {
      this.isDone = true;
      return;
    }
    if (cardGroup.size() == 1) {
      final AbstractCard c3 = cardGroup.getTopCard();
      c3.unhover();
      AbstractDungeon.actionManager.addToTop(new MakeTempCardInHandAction(c3, 1));
      this.isDone = true;
      return;
    }
    // TODO: move to localized strings
    AbstractDungeon.gridSelectScreen.open(cardGroup, 1, "Select a card.", false);
    this.tickDuration();
  }
}