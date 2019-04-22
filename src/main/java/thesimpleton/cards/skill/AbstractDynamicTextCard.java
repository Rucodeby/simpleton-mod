package thesimpleton.cards.skill;

import basemod.abstracts.CustomCard;
import thesimpleton.cards.SimpletonUtil;

abstract public class AbstractDynamicTextCard extends CustomCard {
  public AbstractDynamicTextCard(String id, String name, String imgUrl, int cost,
                                 String rawDescription, CardType type, CardColor color,
                                 CardRarity rarity, CardTarget target) {
    super(id, name, imgUrl, cost, rawDescription, type, color, rarity, target);
  }


  @Override
  public void hover() {
    updateDescription(SimpletonUtil.isPlayerInCombat());
  }

  @Override
  public void unhover() {
    super.unhover();
    updateDescription(false);
  }

  abstract protected void updateDescription(boolean extendedDescription);
}