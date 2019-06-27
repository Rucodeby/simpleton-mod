package thesimpleton.orbs;

import basemod.abstracts.CustomOrb;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import thesimpleton.TheSimpletonMod;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.crops.Crop;
import thesimpleton.effects.orb.CropAnimationEffect;
import thesimpleton.effects.orb.GainCropSoundEffect;
import thesimpleton.effects.orb.HarvestCropSoundEffect;
import thesimpleton.effects.orb.StackCropSoundEffect;
import thesimpleton.orbs.utilities.CropOrbHelper;
import thesimpleton.ui.SettingsHelper;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractCropOrb extends CustomOrb {
  private static final String ORB_DESCRIPTION_ID = "TheSimpletonMod:AbstractCropOrb";
  private final static String[] GENERIC_DESCRIPTION;
  private final static OrbStrings orbStrings;

  private final static Color MATURE_CROP_HALO_COLOR = Color.WHITE;
//  private final static Color MATURE_CROP_HALO_COLOR = new Color(237.0F, 254.0F, 53.0F, 0.2F); //Color.YELLOW;

  private final static Color CROP_STACK_COUNT_FONT_COLOR = Color.WHITE;
  private final static Color MATURE_CROP_STACK_COUNT_FONT_COLOR = Color.YELLOW; // new Color(250.0F, 255.0F, 190.0F, 1.0F); //Color.YELLOW;
  private static final float TOOLTIP_X_OFFSET = 80.0F;
  private static final float TOOLTIP_Y_OFFSET = -48.0F;

  private final Crop crop;

  private Texture haloImage;
  private String haloImageFilename;

  // TODO: separate CropOrbType (which has e.g. harvest info and description data) from CropOrb (which has stack count)

  public AbstractCropOrb(Crop crop, String ID, String NAME, int amount, int maturityThreshold, String description, String imgPath, String haloImgFilename) {
    super(ID, NAME, amount, maturityThreshold, description, "", TheSimpletonMod.getResourcePath(getUiPath(imgPath)));
    this.crop = crop;
    this.basePassiveAmount = this.passiveAmount = amount;
    this.haloImageFilename = haloImgFilename;
  }

  private Texture getHaloImage() {
    if (this.haloImage == null) {
      haloImage = TheSimpletonMod.loadTexture(TheSimpletonMod.getResourcePath(getUiPath(haloImageFilename)));
    }
    return haloImage;
  }

  abstract public AbstractCropOrb makeCopy(int amount);

  @Override
  public void onEvoke() {
    this.getCrop().harvestAll();
  }

  public AbstractCrop getCrop() { return this.crop.getCropInfo().getCrop(); }

  public int getAmount() {
    AbstractCropOrb orb = getCropOrb();

    return orb != null ? orb.passiveAmount : 0;
//    return hasCropOrb() ? getCropOrb().passiveAmount : 0;
  }

  public static String getGenericDescription(int maturityThreshold) {
    return GENERIC_DESCRIPTION[0] + maturityThreshold + GENERIC_DESCRIPTION[1];
  }

  public boolean isMature(boolean thisIsActiveOrb) {
//    TheSimpletonMod.logger.debug("AbstractCropOrb::isMature amount: " + this.getAmount() + " maturityThreshold: " + getCrop().getMaturityThreshold());
    if (thisIsActiveOrb) {
      return this.passiveAmount >= this.getCrop().getMaturityThreshold();
    } else {
      return this.getAmount() >= getCrop().getMaturityThreshold();
    }
  }

  public static boolean isMature(Crop crop) {
    AbstractCropOrb orb = AbstractCropOrb.getCropOrb(crop);
    return orb != null && orb.isMature(false);
//    return hasCropOrb(crop) && getCropOrb(crop).isMature();
  }

  public boolean hasCropOrb() {
    return hasCropOrb(this.ID);
  }

  public static boolean hasCropOrb(Crop crop) {
    return hasCropOrb(crop.getCropInfo().orbId);
  }

  public AbstractCropOrb getCropOrb() {
    return getCropOrb(this.ID);
  }

  //TODO: move to util
  public static boolean hasCropOrb(AbstractCropOrb orbType) {
    return hasCropOrb(orbType.ID);
  }

  //TODO: move to util
  public static List<AbstractCropOrb> getActiveCropOrbs() {
    return AbstractDungeon.player.orbs.stream()
        .filter(orb -> orb instanceof AbstractCropOrb)
        .map(orb -> (AbstractCropOrb)orb)
        .collect((Collectors.toList()));
  }

  public static boolean hasCropOrb(String orbId) {
//    TheSimpletonMod.logger.debug("AbstractCropOrb::hasCropOrb : Player has orbs:"
//        + AbstractDungeon.player.orbs.stream().map(orb -> orb.name).collect(Collectors.joining(", ")));

    Optional<AbstractOrb> cropOrb = AbstractDungeon.player.orbs.stream()
        .filter(orb -> orb.ID == orbId)
        .findFirst();

    return cropOrb.isPresent();
  }

  public static int getNumberActiveCropOrbs() {
    return getActiveCropOrbs().size();
  }

  public void gainCropEffectBefore() {
    AbstractDungeon.effectList.add(new GainCropSoundEffect(-1.0F, -1.0F));
  }

  public void gainCropEffectAfter() {
    AbstractDungeon.effectList.add(new CropAnimationEffect(this.hb.cX, this.hb.y));
  }

  public void stackCropEffect() {
    AbstractDungeon.effectList.add(new StackCropSoundEffect(this.hb.cX, this.hb.y));
    AbstractDungeon.effectList.add(new CropAnimationEffect(this.hb.cX, this.hb.y));
  }

  public void harvestCropEffect() {
    //TODO: use an animation that's visually distinct from gain or stack animations
    AbstractDungeon.effectList.add(new HarvestCropSoundEffect(this.hb.cX, this.hb.y));
    AbstractDungeon.effectList.add(new CropAnimationEffect(this.hb.cX, this.hb.y, Settings.ACTION_DUR_MED));
  }

  public static boolean playerHasAnyCropOrbs() {
    return getActiveCropOrbs().size() > 0;
  }

  public static AbstractCropOrb getOldestCropOrb() {
    List<AbstractCropOrb> activeCropOrbs = getActiveCropOrbs();
    if (!playerHasAnyCropOrbs()) {
      return null;
    }
    return activeCropOrbs.get(0);
  }

  public static AbstractCropOrb getNewestCropOrb() {
    List<AbstractCropOrb> activeCropOrbs = getActiveCropOrbs();
    if (!playerHasAnyCropOrbs()) {
      return null;
    }
    return activeCropOrbs.get(activeCropOrbs.size() - 1);
  }
  //TODO: move to util
  public static AbstractCropOrb getCropOrb(AbstractCropOrb orbType) {
    return getCropOrb(orbType.ID);
  }

  public static AbstractCropOrb getCropOrb(Crop crop) {
    return getCropOrb(crop.getCropInfo().orbId);
  }

  public void stackOrb(int amount) {

  }

  public static AbstractCropOrb getCropOrb(String orbId) {

//    TheSimpletonMod.logger.debug("AbstractCropOrb::getCropOrb : getting crop orb: " + orbId);
//
//    TheSimpletonMod.logger.debug("AbstractCropOrb::getCropOrb : Player has orbs:"
//        + AbstractDungeon.player.orbs.stream().map(orb -> orb.name).collect(Collectors.joining(", ")));
//
//    TheSimpletonMod.logger.debug("AbstractCropOrb::getCropOrb : Player has active orbs:"
//        + AbstractDungeon.player.orbs.stream()
//            .filter(orb -> orb.passiveAmount > 0).map(orb -> orb.name).collect(Collectors.joining(", ")));
//
//    TheSimpletonMod.logger.debug("AbstractCropOrb::getCropOrb : Player active orb counts:"
//        + AbstractDungeon.player.orbs.stream()
//        .filter(orb -> orb.passiveAmount > 0).map(orb -> orb.name + ": " + orb.passiveAmount).collect(Collectors.joining(", ")));

    Optional<AbstractOrb> cropOrb = AbstractDungeon.player.orbs.stream()
        .filter(orb -> orb instanceof AbstractCropOrb && orb.ID == orbId)
        .findFirst();

    if (!cropOrb.isPresent()) {
      return null;
    }

    TheSimpletonMod.logger.debug("AbstractCropOrb::getCropOrb : returning orb " + ((AbstractCropOrb)cropOrb.get()).name  + " with " + cropOrb.get().passiveAmount + " passive stacks");

    return (AbstractCropOrb)cropOrb.get();
  }

  @Override
  public void onStartOfTurn() {
    getCrop().atStartOfTurn();
  }

  private static String getUiPath(String id) {
    return "orbs/" + id + ".png";
  }

  @Override
  public void render(SpriteBatch sb) {
    super.render(sb);

    Color filterColor = MATURE_CROP_HALO_COLOR;
    Color textColor = MATURE_CROP_STACK_COUNT_FONT_COLOR;

    if (this.isMature(true)) {
      // TODO: when stacks > maturity level, replace with flash image + add sound effect for those few frames
      //      final Color overplantFilterColor = Color.LIME;
      //      final Color overplantTextColor = Color.LIME;
      //      if (this.getAmount() >  this.getCrop().getMaturityThreshold()) {
      //        filterColor = overplantFilterColor;
      //        textColor = overplantTextColor;
      //      }

      // TODO: Highlight targeted crop on dynamic card hover (e.g. Aerate) with different-colored halo

      sb.draw(this.getHaloImage(), this.cX - 48.0F + this.bobEffect.y / 4.0F, this.cY - 48.0F + this.bobEffect.y / 4.0F, 48.0F, 48.0F, 96.0F, 96.0F, this.scale, this.scale, 0.0F, 0, 0, 96, 96, false, false);
      this.c = textColor;
      renderText(sb);
      this.c = filterColor;
    } else {
      this.c = CROP_STACK_COUNT_FONT_COLOR;
    }

    Color highlightFilterColor = Color.GOLD;
    if (CropOrbHelper.hasHighlightedOrb()) {
      if (CropOrbHelper.getHighlightedOrb().name == this.name) {
        this.c = highlightFilterColor;
      }
    } else {
      this.c = filterColor;
    }
  }

  @Override
  public void update() {
//    super.update();
//    if (this.hb.hovered) {
//      TipHelper.queuePowerTips(
//          hb.x + TOOLTIP_X_OFFSET * SettingsHelper.getScaleX(),
//          hb.y - TOOLTIP_Y_OFFSET * SettingsHelper.getScaleY(), getPowerTips());
//    } else {
//      hb.x = hb.x;
//    }

    this.hb.update();

    if (this.hb.hovered) {
      TipHelper.queuePowerTips(
          hb.x + TOOLTIP_X_OFFSET * SettingsHelper.getScaleX(),
          hb.y - TOOLTIP_Y_OFFSET * SettingsHelper.getScaleY(),
          getPowerTips());

      this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7F);
    }
  }

  protected List<Keyword> getBaseGameKeywords() {
    return new ArrayList<>();
  }

  protected List<String> getCustomKeywords() {
    return new ArrayList<>();
  }

  private ArrayList<PowerTip> keywordPowerTips;

  private ArrayList<PowerTip> getPowerTips() {
      if (keywordPowerTips == null) {
        keywordPowerTips = new ArrayList<>();

        // main tooltip
        keywordPowerTips.add(new PowerTip(this.name, this.description));
        // crop keyword tooltips
        for (Map.Entry<String,String> entry : getKeywords().entrySet()) {
          keywordPowerTips.add(new PowerTip(TipHelper.capitalize(entry.getKey()), entry.getValue()));
        }
      }
      return keywordPowerTips;
  }



  private Map<String, String> getKeywords() {
    Map<String, String> keywords = getCustomKeywords().stream()
        .map(k -> TheSimpletonMod.getKeyword(k))
        .filter(kw -> kw != null)
        .collect(Collectors.toMap(kw -> kw.PROPER_NAME, kw -> kw.DESCRIPTION));

    keywords.putAll(getBaseGameKeywords().stream()
        .collect(Collectors.toMap(kw -> kw.NAMES[0], kw -> kw.DESCRIPTION)));

    com.evacipated.cardcrawl.mod.stslib.Keyword maturityKeyword
        = TheSimpletonMod.getKeyword("TheSimpletonMod:MaturityKeyword");
    keywords.put(maturityKeyword.PROPER_NAME, maturityKeyword.DESCRIPTION);

    return keywords;
  }

  static {
    orbStrings = CardCrawlGame.languagePack.getOrbString(ORB_DESCRIPTION_ID);
    GENERIC_DESCRIPTION = orbStrings.DESCRIPTION;
  }
}
