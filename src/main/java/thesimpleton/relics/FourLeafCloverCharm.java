package thesimpleton.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import thesimpleton.TheSimpletonMod;

public class FourLeafCloverCharm extends CustomRelic {
    public static final String ID = "TheSimpletonMod:FourLeafCloverCharm";
    public static final String IMG_PATH = "relics/fourleafclovercharm.png";
    public static final String IMG_PATH_LARGE = "relics/fourleafclovercharm_large.png";
    public static final String OUTLINE_IMG_PATH = "relics/fourleafclovercharm_outline.png";

    private static final RelicTier TIER = RelicTier.BOSS;
    private static final LandingSound SOUND = LandingSound.CLINK;


    public FourLeafCloverCharm() {
        super(ID, new Texture(TheSimpletonMod.getResourcePath(IMG_PATH)),
                new Texture(TheSimpletonMod.getResourcePath(OUTLINE_IMG_PATH)), TIER, SOUND);
        this.largeImg = ImageMaster.loadImage(TheSimpletonMod.getResourcePath(IMG_PATH_LARGE));
    }

    @Override
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new FourLeafCloverCharm();
    }

}
