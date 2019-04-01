package thesimpleton.relics.unused;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thesimpleton.TheSimpletonMod;

public class BloodyHarpoon extends CustomRelic {
    public static final String ID = "TheSimpletonMod:BloodyHarpoon";
    public static final String IMG_PATH = "relics/bloodyharpoon.png";
    public static final String IMG_PATH_LARGE = "relics/bloodyharpoon_large.png";
    public static final String OUTLINE_IMG_PATH = "relics/bloodyharpoon_outline.png";

    public static final int BLEED_ADD_AMOUNT = 1;

    private static final RelicTier TIER = RelicTier.UNCOMMON;
    private static final LandingSound SOUND = LandingSound.FLAT;


    public BloodyHarpoon() {
        super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
                new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
        this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0] + BLEED_ADD_AMOUNT + this.DESCRIPTIONS[1];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new BloodyHarpoon();
    }

}
