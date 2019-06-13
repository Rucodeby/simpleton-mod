package thesimpleton.savedata;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import basemod.interfaces.PostInitializeSubscriber;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ExampleCustomSavable implements CustomSavable<List<String>>, PostInitializeSubscriber {
  final private List<AbstractCard> cardpool = new ArrayList<>();
  final Logger logger = TheSimpletonMod.logger;

  public ExampleCustomSavable() {
    if (TheSimpletonMod.isGameInitialized()) {
     registerSaveId();
    }
  }

  public ExampleCustomSavable(List<AbstractCard> cardpool) {
    this();
    logger.debug("ExampleCustomSavable :: instantiated with card pool. size:" + cardpool.size());
    this.cardpool.addAll(cardpool);
  }

  @Override
  public List<String> onSave() {
    logger.debug("ExampleCustomSavable.onSave :: called");

    final List<String> idList = cardpool.stream().map(c -> c.cardID).collect(Collectors.toList());

    logger.debug("ExampleCustomSavable.onSave :: Saving card pool. Cards:");
    int index = 0;
    for(AbstractCard card : cardpool) {
      logger.debug(index++ + ") " + card.name + " [cardId: " + card.cardID + "]");
    }

    return idList;
  }

  // TODO: make abstract
  public String getCustomSaveKey(){
    return AbstractDungeon.player.chosenClass + "." + this.getClass().getSimpleName();
  }

  @Override
  public void onLoad(List<String> ids) {
    logger.debug("ExampleCustomSavable.onLoad :: Loading cards into card pool. Card ids:");
    int index = 0;
    for(String id : ids) {
      logger.debug(index++ + ") " + id);
    }

    if (ids != null && !ids.isEmpty()) {
//
//      for (String uiName : ids) {
//        TheSimpletonMod.addCardToLoadedCardPool(CardLibrary.getCard(uiName));
//      }

      int cardIndex = 0;
      logger.info("ExampleCustomSavable.onLoad :: Loading cards for card pool from save:");
      for (String id : ids) {
        AbstractCard card = CardLibrary.getCard(id);
        this.cardpool.add(card);
        logger.debug(cardIndex++ + ") " + card.name + " [cardId: " + card.cardID + "]");
      }

//      if (!TheSimpletonMod.getSaveCardPool().isEmpty()) {
//        logger.info("CardPoolSavable.onLoad :: Card pool loaded from save with " + ids.size() + " cards. Initializing");
//        CardCrawlGame.dungeon.initializeCardPools();
//      }
    } else {
      logger.info("ExampleCustomSavable.onLoad :: no save data found");
    }
  }

  public List<AbstractCard> getCardpool() {
    return Collections.unmodifiableList(cardpool);
  }

  private void registerSaveId() {
    if (BaseMod.getSaveFields().get(this.getCustomSaveKey()) == null) {
      BaseMod.addSaveField(this.getCustomSaveKey(), this);
    }
  }

  @Override
  public void receivePostInitialize() {
    registerSaveId();
  }
}