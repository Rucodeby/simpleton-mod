package thesimpleton.orbs;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.SquashCrop;
import thesimpleton.powers.utils.Crop;

public class SquashCropOrb extends AbstractCropOrb {
  public static final Crop CROP_ENUM = Crop.SQUASH;
  public static final String ORB_ID = "TheSimpletonMod:SquashCropOrb";
  public static final String IMG_PATH = "TheSimpletonMod/img/orbs/plantsquash.png";
  public static final int MATURITY_THRESHOLD = 5;
  private static final OrbStrings orbStrings;
  public static final String NAME;
  public static final String[] DESCRIPTIONS;

  public SquashCropOrb() {
    this(0);
  }

  public SquashCropOrb(int amount) {
    super(CROP_ENUM, ORB_ID, NAME, amount, MATURITY_THRESHOLD, DESCRIPTIONS[0], IMG_PATH);
  }

  @Override
  public AbstractOrb makeCopy() {
    return this.makeCopy(0);
  }

  public AbstractCropOrb makeCopy(int amount) {
    return new SquashCropOrb(amount);
  }

  @Override
  public void playChannelSFX() {
    TheSimpletonMod.logger.debug(" ============================================= ADD playChannelSFX for " + this.name + " =============================================");
  }

  private static String getDescription() {
    return getGenericDescription(MATURITY_THRESHOLD)
        + " NL " + DESCRIPTIONS[0] + SquashCrop.BLOCK_PER_STACK + DESCRIPTIONS[1];
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