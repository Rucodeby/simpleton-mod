package thesimpleton.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.Crop;

public class CoffeeCropOrb extends AbstractCropOrb {
  public static final Crop CROP_ENUM = Crop.COFFEE;
  public static final String ORB_ID = "TheSimpletonMod:CoffeeCropOrb";
  public static final String IMG_PATH = "TheSimpletonMod/img/orbs/plantcoffee.png";
  private static final OrbStrings orbStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public CoffeeCropOrb() {
    this(0);
  }

  public CoffeeCropOrb(int amount) {
    super(CROP_ENUM, ORB_ID, NAME, amount, Crop.COFFEE.getCropInfo().maturityThreshold, DESCRIPTIONS[0], IMG_PATH);
  }

  @Override
  public AbstractOrb makeCopy() {
    return new CoffeeCropOrb();
  }

  public AbstractCropOrb makeCopy(int amount) {
    return new CoffeeCropOrb(amount);
  }

  @Override
  public void playChannelSFX() {
    TheSimpletonMod.logger.debug(" ============================================= ADD playChannelSFX for " + this.name + " =============================================");
  }

  private static String getDescription() {
    return getGenericDescription(Crop.COFFEE.getCropInfo().maturityThreshold)
        + " NL " + DESCRIPTIONS[0];
  }

  @Override
  public void updateDescription() {
    this.description = getDescription();
    this.update();
  }

  static {
    orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    NAME = orbStrings.NAME;
    DESCRIPTIONS = orbStrings.DESCRIPTION;
  }
}
