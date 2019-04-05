package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.ApplyBurningAction;

public class HotPotato extends CustomRelic {
  public static final String ID = "TheSimpletonMod:HotPotato";
  public static final String IMG_PATH = "relics/hotpotato.png";
  public static final String IMG_PATH_LARGE = "relics/hotpotato_large.png";
  public static final String OUTLINE_IMG_PATH = "relics/hotpotato_outline.png";

  private static final RelicTier TIER = RelicTier.COMMON;
  private static final LandingSound SOUND = LandingSound.SOLID;


  public HotPotato() {
    super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
        new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
    this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
  }

  @Override
  public String getUpdatedDescription() {
    return this.DESCRIPTIONS[0];
  }

  public void activate(AbstractCreature source, AbstractCreature target, int amount) {
    flash();
    AbstractDungeon.actionManager.addToBottom(new ApplyBurningAction(target, source, amount));
  }

  @Override
  public AbstractRelic makeCopy() {
    return new HotPotato();
  }

}
