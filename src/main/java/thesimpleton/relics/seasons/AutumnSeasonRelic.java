package thesimpleton.relics.seasons;

import basemod.abstracts.CustomSavable;
import thesimpleton.seasons.Season;

import java.util.List;

public class AutumnSeasonRelic extends AbstractSeasonRelic implements CustomSavable<List<String>> {
  public static final String ID = "TheSimpletonMod:AutumnSeasonRelic";
  public static final String IMG_PATH = "relics/seasonautumn.png";
  public static final String IMG_PATH_LARGE = "relics/seasonautumn_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/seasonautumn_outline.png";

  private static final Season SEASON = Season.FALL;

  public AutumnSeasonRelic() {
    super(ID, IMG_PATH, IMG_PATH_LARGE, OUTLINE_IMG_PATH ,SEASON);
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0]; // list in-season crops etc.
  }
}