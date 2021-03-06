package thesimpleton.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.powers.SlowPower;

public class IntoxicatedPower extends AbstractTheSimpletonPower {

  public static final String POWER_ID = "TheSimpletonMod:IntoxicatedPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.DEBUFF;
  public static final String IMG = "intoxicated.png";

  public IntoxicatedPower(AbstractCreature owner) {
    super(IMG);
    this.owner = owner;
    this.amount = -1;

    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;

    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0];
  }

  @Override
  public void onCardDraw(AbstractCard card) {
    if (card.cost >= 0) {
      int newCost = AbstractDungeon.cardRandomRng.random(3);
      if (card.cost != newCost) {
        card.setCostForTurn(newCost);
        card.isCostModifiedForTurn = true;
      }
    }
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}