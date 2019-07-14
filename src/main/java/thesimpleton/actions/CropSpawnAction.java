package thesimpleton.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.apache.logging.log4j.Logger;
import thesimpleton.TheSimpletonMod;
import thesimpleton.cards.SimpletonUtil;
import thesimpleton.crops.AbstractCrop;
import thesimpleton.orbs.AbstractCropOrb;
import thesimpleton.powers.AbundancePower;


public class CropSpawnAction extends AbstractGameAction {
    private static final ActionType ACTION_TYPE = ActionType.SPECIAL;
    private static final float ACTION_DURATION = Settings.ACTION_DUR_XFAST;
    private final boolean isFromCard;
    private final int amount;
    private AbstractCropOrb cropOrb;

    private boolean secondTick = false;

    // TODO: increment existing cropOrb instead if (?) it
    public CropSpawnAction(AbstractCropOrb cropOrb, boolean isFromCard) {
        this(cropOrb, -1, isFromCard);
    }

    public CropSpawnAction(AbstractCropOrb cropOrb, int stacks, boolean isFromCard) {
//        TheSimpletonMod.logger.info("============> CropSpawnAction::constructor =====");

        final int rawAmount = stacks >= 0 ? stacks : cropOrb.passiveAmount;

        this.duration = ACTION_DURATION;
        this.actionType = ACTION_TYPE;
        this.isFromCard = isFromCard;
        this.amount = calculateCropStacks(rawAmount, this.isFromCard);
        this.cropOrb = cropOrb;

        Logger logger = TheSimpletonMod.logger;
//        logger.info("CropSpawnAction() constructor: " + cropOrb.getClass().getSimpleName() + "; rawAmount: " + rawAmount + " calculated amount: " + this.amount + " cropOrb.amount " + cropOrb.getAmount() + " cropOrb.passiveAmount " + cropOrb.passiveAmount);
    }

    public void update() {
//        TheSimpletonMod.logger.info("CropSpawnAction::update duration: " + this.duration);

        Logger logger = TheSimpletonMod.logger;

        if (AbstractDungeon.player.maxOrbs <= 0) {
            this.isDone = true;
            return;
        } else {
            if (secondTick) {
                if (this.duration != ACTION_DURATION) {
                    AbstractCropOrb cropOrb = AbstractCropOrb.getCropOrb(this.cropOrb);
                    if (cropOrb != null) {
                        int newAmount = cropOrb.passiveAmount;
//                        logger.info("CropSpawnAction::update secondTick. newAmount: " + newAmount);

                        AbstractCrop crop = cropOrb.getCrop();
                        if (newAmount > crop.getMaturityThreshold()) {
                            crop.flash();
//                            TheSimpletonMod.logger.info("============> CropSpawnAction::update calling  crop.harvest()=====");

                            crop.harvest(false, newAmount - crop.getMaturityThreshold());
                        }
                    } else {
//                        logger.info("CropSpawnAction::update secondTick. player does not have expected orb: " + this.cropOrb.name + ". terminating action.");
                    }
                    this.isDone = true;
                    return;
                }
                tickDuration();
            } else {
//                logger.info("CropSpawnAction::update : Player can have orbs: " + AbstractDungeon.player.maxOrbs);
                AbstractCropOrb orb = AbstractCropOrb.getCropOrb(this.cropOrb);
                if (orb != null) {
//                    logger.info("CropSpawnAction::update : Player has cropOrb " + this.cropOrb.name);
                    orb.passiveAmount += this.amount;
                    orb.stackCropEffect();
                    orb.update();
                    // (trigger cropOrb crap}
                } else {
//                    logger.info("CropSpawnAction::update : Player doesn't have cropOrb "
//                        + this.cropOrb.getClass().getSimpleName() + ". Adding " + this.amount);

                    if (SimpletonUtil.getActiveOrbs().size() >= AbstractDungeon.player.maxOrbs) {
//                        logger.info("CropSpawnAction::update player has no free orb slots. Queueing CropOrbCycleAction with " + this.cropOrb.name + " for " + this.cropOrb.passiveAmount + " stacks");
                        AbstractDungeon.actionManager.addToBottom(new CropOrbCycleAction(this.cropOrb, this.amount, this.isFromCard));
                    }  else {
//                        logger.info("CropSpawnAction::update player has " +  (AbstractDungeon.player.maxOrbs - SimpletonUtil.getActiveOrbs().size()) + " free orb slots");
//                        logger.info("CropSpawnAction::update # of " + this.cropOrb.name + " before: " + this.cropOrb.passiveAmount);

                        this.cropOrb.gainCropEffectBefore();
                        this.cropOrb.passiveAmount = this.amount;

                        logger.info("CropSpawnAction::update # of " + this.cropOrb.name + " passiveAmount after (1): " + this.cropOrb.passiveAmount + "; this.amount: " + this.amount);

                        AbstractCropOrb newOrb = this.cropOrb.makeCopy(this.amount);
                        AbstractDungeon.player.channelOrb(newOrb);
                        newOrb.update();
//                        logger.info("CropSpawnAction::update # of " + newOrb.name + " passiveAmount after (2): " + newOrb.passiveAmount + " getAmount amount after: " + AbstractCropOrb.getCropOrb(newOrb).getAmount());
                        AbstractCropOrb.getCropOrb(newOrb).gainCropEffectAfter();
                    }
                }
                this.secondTick = true;
            }
            tickDuration();
        }
    }

    public static int calculateCropStacks(int amount, boolean isFromCard) {
        AbstractPlayer player = AbstractDungeon.player;

        Logger logger = TheSimpletonMod.logger;

//        logger.info("ApplyCropAction:calculateCropStacks");

        int adjustedAmount = amount;
        if (player.hasPower(AbundancePower.POWER_ID) && isFromCard) {
            AbstractPower power = player.getPower(AbundancePower.POWER_ID);
            if (power.amount > 0) {
                power.flashWithoutSound();
                adjustedAmount += power.amount;
            }
        }
        return adjustedAmount;
    }
}



