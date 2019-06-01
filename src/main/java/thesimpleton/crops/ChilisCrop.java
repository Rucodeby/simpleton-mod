package thesimpleton.crops;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import thesimpleton.actions.ApplyBurningAction;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.cards.power.crop.Chilis;
import thesimpleton.orbs.ChiliCropOrb;
import thesimpleton.powers.utils.Crop;

public class ChilisCrop extends  AbstractCrop {
  public static final Crop CROP_ENUM = Crop.CHILIS;
  private static final String ORB_ID = ChiliCropOrb.ORB_ID;
  private static final AbstractCropPowerCard POWER_CARD = new Chilis();

  private static final AbstractCard.CardRarity RARITY = AbstractCard.CardRarity.UNCOMMON;

  public static final int MATURITY_THRESHOLD = 5;
  public static int DAMAGE_PER_STACK = 3;

  private int damagePerStack;

  public ChilisCrop() {
    super(CROP_ENUM, ORB_ID, POWER_CARD, RARITY, MATURITY_THRESHOLD);
    this.damagePerStack = DAMAGE_PER_STACK;
  }

  protected int harvestAction(int harvestAmount) {
    logger.debug("ChilisCrop::harvestAction");
    final int damageAmount = harvestAmount * this.damagePerStack;

    if (harvestAmount > 0) {
      // all monsters version
      for (int i = 0; i < harvestAmount; i++) {
        AbstractDungeon.actionManager.addToTop(
            new DamageAllEnemiesAction(getPlayer(), DamageInfo.createDamageMatrix(damageAmount, true),
                DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.FIRE, true));
      }

      AbstractDungeon.getMonsters().monsters.stream()
          .filter(mo -> !mo.isDeadOrEscaped())
          .forEach(mo -> AbstractDungeon.actionManager.addToBottom(new ApplyBurningAction(mo, getPlayer(), damageAmount)));
    }
    return harvestAmount;
  }
}