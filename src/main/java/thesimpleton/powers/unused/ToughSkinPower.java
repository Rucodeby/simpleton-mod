package thesimpleton.powers.unused;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import thesimpleton.powers.AbstractTheSimpletonPower;


public class ToughSkinPower extends AbstractTheSimpletonPower {
  public static final String POWER_ID = "TheSimpletonMod:ToughSkinPower";
  private static final PowerStrings powerStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public static PowerType POWER_TYPE = PowerType.BUFF;
  public static final String IMG = "toughskin.png";

  private AbstractCreature source;

  public ToughSkinPower(AbstractCreature owner, AbstractCreature source, int amount) {
    super(IMG);
    this.owner = owner;
    this.source = source;
    this.amount = amount;

    this.name = NAME;
    this.ID = POWER_ID;
    this.type = POWER_TYPE;

    updateDescription();
  }

  @Override
  public void updateDescription() {
    this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
  }

  public void applyPower(AbstractPlayer p) {
    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.amount));
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    NAME = powerStrings.NAME;
    DESCRIPTIONS = powerStrings.DESCRIPTIONS;
  }
}