package thesimpleton.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import thesimpleton.cards.power.crop.AbstractCropPowerCard;
import thesimpleton.characters.TheSimpletonCharacter;
import thesimpleton.relics.TheHarvester;
import thesimpleton.utilities.Trigger;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

// TODO: create separate enum for CropRarity
public abstract class AbstractCropPower extends AbstractTheSimpletonPower {
  public static Map<CardRarity, Integer> CROP_RARITY_DISTRIBUTION;

  private static boolean IS_HARVEST_ALL = false;
  private static int AUTO_HARVEST_THRESHOLD = 5;
  private static int CROP_POWER_ID_COUNTER = 0;
  private static int STACKS_PER_TRIGGER = 1;
  private static boolean hasHarvestedThisTurn = false;

  private static final String POWER_DESCRIPTION_ID = "TheSimpletonMod:AbstractCropPower";
  private static final PowerStrings powerStrings;
  public final String[] descriptions;
  public static final String[] PASSIVE_DESCRIPTIONS;

  private final int cropPowerId;
  private final boolean isHarvestAll;
  private final AbstractCropPowerCard powerCard;
  private int autoHarvestThreshold;


  public AbstractCard.CardRarity cropRarity;

  AbstractCropPower(String name, String id, PowerType powerType, String[] descriptions, String imgName, AbstractCreature owner, AbstractCard.CardRarity rarity,
                    AbstractCropPowerCard powerCard, int amount) {
    this(name, id, powerType, descriptions, imgName, owner, rarity, powerCard, amount, IS_HARVEST_ALL, AUTO_HARVEST_THRESHOLD);
  }

  AbstractCropPower(String name, String id, PowerType powerType,  String[] descriptions, String imgName, AbstractCreature owner, AbstractCard.CardRarity rarity,
                    AbstractCropPowerCard powerCard, int amount, boolean isHarvestAll) {
    this(name, id, powerType, descriptions, imgName, owner, rarity, powerCard, amount, isHarvestAll, AUTO_HARVEST_THRESHOLD);
  }

  private AbstractCropPower(String name, String id, PowerType powerType, String[] descriptions, String imgName, AbstractCreature owner, AbstractCard.CardRarity rarity,
                            AbstractCropPowerCard powerCard, int amount, boolean isHarvestAll,
                            int autoHarvestThreshold) {
    super(imgName);
    this.ID = id;
    this.type = powerType;
    this.name = name;
    this.descriptions = descriptions;
    this.owner = owner;
    this.amount = amount;
    this.cropRarity = rarity;
    this.isHarvestAll = isHarvestAll;
    this.autoHarvestThreshold = autoHarvestThreshold;
    this.cropPowerId = CROP_POWER_ID_COUNTER++;
    this.powerCard = powerCard;

    getPlayer().getCropUtil().onCropGained(this);

    logger.debug(this.name + ": gained " + amount + " stacks");
  }


  protected String getPassiveDescription() {
    return PASSIVE_DESCRIPTIONS[0] + this.autoHarvestThreshold + PASSIVE_DESCRIPTIONS[1];
  }

  void onHarvest(int amount) {
    hasHarvestedThisTurn = true;
    logger.debug(this.name + ": harvested. Set hasHarvestedThisTurn");

    if (getPlayer().hasPower(ToughSkinPower.POWER_ID)) {
      ((ToughSkinPower) getPlayer().getPower(ToughSkinPower.POWER_ID)).applyPower(getPlayer());
    }
  }

//  @Override
//  public void onGainCharge(int chargeAmount) {
//    super.onGainCharge(chargeAmount);
//    logger.debug(this.name + ": gained " + chargeAmount + " stacks");
//  }
//
//
//  @Override
//  public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
//    super.onApplyPower(power, target, source);
//
//    if (power instanceof AbstractCropPower && ((AbstractCropPower)power).getInstanceId() == this.getInstanceId()) {
//      logger.debug(this.name + ": power applied");
//    }
//  }

  public int getMaturityThreshold() {
    return this.autoHarvestThreshold;
  }

  public void updateMaturityThreshold(int amount) {
    this.autoHarvestThreshold += amount;
    this.updateDescription();
  }

  static TheSimpletonCharacter getPlayer() {
    return (TheSimpletonCharacter) AbstractDungeon.player;
  }

  public void harvest(boolean all, int amount) {
    onHarvest(amount);
  }

  public void atStartOfTurn() {
    logger.debug("Checking for auto-harvest triggers (amount: " + this.amount + "; hasHarvester:  " +  getPlayer().hasRelic(TheHarvester.ID)+ ")");
    if (this.amount >= autoHarvestThreshold &&  getPlayer().hasRelic(TheHarvester.ID)) {
      logger.debug("Triggering Harvester");
      getPlayer().getRelic(TheHarvester.ID).flash();
      this.flash();
      harvest(isHarvestAll, STACKS_PER_TRIGGER);
    } else { logger.debug("Not triggered"); }
  }

  public AbstractCropPowerCard getPowerCard() throws UnsupportedOperationException {
    if (!this.hasPowerCard()) {
      throw new UnsupportedOperationException("No crop power card defined for " + this.getClass().getSimpleName());
    }
    return (AbstractCropPowerCard)this.powerCard.makeCopy();
  }

  public boolean hasPowerCard() {
    return this.powerCard != null;
  }

  public static boolean playerHasAnyActiveCropPowers() {
    return AbstractCropPower.getActiveCropPowers().size() > 0;
  }

  public static AbstractCropPower getOldestPower() {
    final Optional<AbstractPower> oldestPower = AbstractDungeon.player.powers.stream()
        .filter(power -> power instanceof AbstractCropPower)
        .reduce((p1, p2) -> ((AbstractCropPower) p2).cropPowerId < ((AbstractCropPower) p1).cropPowerId ? p2 : p1);

    return oldestPower.isPresent() ? (AbstractCropPower)oldestPower.get() : null;
  }

  public static AbstractCropPower getNewestPower() {
    final  Optional<AbstractPower> newestPower = AbstractDungeon.player.powers.stream()
        .filter(power -> power instanceof AbstractCropPower)
        .reduce((p1, p2) -> ((AbstractCropPower) p2).cropPowerId >= ((AbstractCropPower) p1).cropPowerId ? p2 : p1);

    logger.debug("Newest crop per CropUtil: " + (getPlayer().getCropUtil().playerHasAnyCrops() ? getPlayer().getCropUtil().getNewestCrop() : "None"));
    logger.debug("Newest crop per AbstractCropPower: " + (newestPower.isPresent() ? (AbstractCropPower)newestPower.get() : "None"));

//    return newestPower.isPresent() ? (AbstractCropPower)newestPower.get() : null;
    return getPlayer().getCropUtil().getNewestCrop();
  }

  public static List<AbstractCropPower> getActiveCropPowers() {
    List<AbstractCropPower> cropPowers = new ArrayList<>();
    AbstractDungeon.player.powers.stream()
        .filter(power -> power instanceof AbstractCropPower)
        .forEach(power -> cropPowers.add((AbstractCropPower)power));
    return cropPowers;
  }

  public static AbstractCropPower getRandomCropPower(AbstractPlayer p, int numStacks) {
    return getRandomCropPowers(p, 1, numStacks,false).get(0);
  }

  public static AbstractCropPower getRandomCropPower(AbstractPlayer p, int numStacks, boolean withRarityDistribution) {
    return getRandomCropPowers(p, 1, numStacks, withRarityDistribution).get(0);
  }

  public static List<AbstractCropPower> getRandomCropPowers(
      AbstractPlayer p, int numPowers, int numStacks, boolean withRarityDistribution) {
    return getRandomCropPowers(p, numPowers, numStacks, withRarityDistribution, power -> true);
  }

  public static List<AbstractCropPower> getActiveCropPowers(AbstractPlayer player, boolean shuffle) {
    final ArrayList<AbstractPower> activePowers = new ArrayList<>(player.powers);

    if (shuffle) {
      Collections.shuffle(activePowers);
    }

    List<AbstractCropPower> activeCropPowers = activePowers.stream()
        .filter(pow -> pow instanceof AbstractCropPower && pow.amount > 0 && !((AbstractCropPower) pow).finished)
        .map(pow -> (AbstractCropPower) pow)
        .sorted(Comparator.comparing(AbstractCropPower::getInstanceId))
        .collect(Collectors.toList());

    return activeCropPowers;
  }

  public static boolean hasHarvestedThisTurn() {
    return hasHarvestedThisTurn;
  }

  public int getInstanceId() {
    return this.cropPowerId;
  }

  @Override
  public void stackPower(int amount) {
    super.stackPower(amount);
    logger.debug("Called stackPower for " + this.ID + " amount: " + amount + ". Updated amount: " + this.amount);
    getPlayer().getCropUtil().onCropGained(this);
    if (this.amount > autoHarvestThreshold) {
      this.flash();
      this.harvest(false, this.amount - autoHarvestThreshold);
    }
  }

  @Override
  public void onRemove() {
    super.onRemove();
    getPlayer().getCropUtil().onCropLost(this);
  }

  public static List<AbstractCropPower> getRandomCropPowers(
      AbstractPlayer p, int numPowers, int numStacks, boolean withRarityDistribution,
      Predicate<AbstractCropPower> predicate){
    // TODO: move this logic to a plant power manager class
    final PlantPotatoPower potatoPower = new PlantPotatoPower(p, numStacks);
    final PlantSpinachPower spinachPower = new PlantSpinachPower(p, numStacks);
    final PlantOnionPower onionPower = new PlantOnionPower(p, numStacks);
    final PlantTurnipPower turnipPower = new PlantTurnipPower(p, numStacks);
    final PlantCornPower cornPower = new PlantCornPower(p, numStacks);
    final PlantChiliPower chiliPower = new PlantChiliPower(p, numStacks);

    ArrayList<AbstractCropPower> referencePowers = new ArrayList<>();
    referencePowers.add(potatoPower);
    referencePowers.add(spinachPower);
    referencePowers.add(onionPower);
    referencePowers.add(turnipPower);
    referencePowers.add(cornPower);
    referencePowers.add(chiliPower);

    List<AbstractCropPower> filteredPowers = referencePowers.stream()
        .filter(predicate)
        .collect(Collectors.toList());

    logger.debug(AbstractCropPowerCard.class.getSimpleName()
        + ".getRandomCropPowers :: referencePowers: "
        + referencePowers.stream().map(rp -> rp.name).collect(Collectors.joining(", ")));

    logger.debug(AbstractCropPowerCard.class.getSimpleName()
        + ".getRandomCropPowers :: filteredPowers: "
        + filteredPowers.stream().map(fp -> fp.name).collect(Collectors.joining(", ")));

    ArrayList<AbstractCropPower> resultPowers;

    final int numTotalPowers = filteredPowers.size();
    if (numPowers > numTotalPowers) {
      throw new IndexOutOfBoundsException("Requested " + numPowers + " powers but only " + numTotalPowers
          + " powers are available");
    } else if (numPowers == numTotalPowers) {
      logger.debug("Requested powers size equals total powers. Returning all powers.");
      resultPowers = new ArrayList(filteredPowers);
    } else {
      logger.debug("Choosing powers by probability distribution");

      ArrayList<AbstractCropPower> distributedPowers = new ArrayList<>();
      filteredPowers.forEach(power -> {
        List<AbstractCropPower> copies =
            Collections.nCopies(withRarityDistribution
                    ? AbstractCropPower.CROP_RARITY_DISTRIBUTION.get(power.cropRarity) : 1,
                power);
        distributedPowers.addAll(copies);
      });

      logger.debug(AbstractCropPowerCard.class.getSimpleName()
          + ".getRandomCropPowers :: distributedPowers: "
          + distributedPowers.stream().map(dp -> dp.name).collect(Collectors.joining(", ")));

      Collections.shuffle(distributedPowers);

      logger.debug("Choosing " + numPowers + " powers from distributedPowers");

      resultPowers = new ArrayList<>();
      for (int i = 0; i < numPowers; i++) {
        final AbstractCropPower power = distributedPowers.get(0);
        resultPowers.add(power);
        logger.debug("Selected " + power.name);

        distributedPowers.removeIf(pow -> pow == power);
      }
    }

    logger.debug(AbstractCropPowerCard.class.getSimpleName()
        + ".getRandomCropPowers :: resultPowers: "
        + resultPowers.stream().map(rp -> rp.name).collect(Collectors.joining(", ")));

    Collections.shuffle(resultPowers);

    logger.debug(AbstractCropPowerCard.class.getSimpleName()
        + ".getRandomCropPowers :: resultPowers(shuffled): "
        + resultPowers.stream().map(rp -> rp.name).collect(Collectors.joining(", ")));

    return resultPowers;
  }

  public static int getNumberActiveCropTypes() {
    return getActiveCropPowers().size();
  }

  static {
    powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_DESCRIPTION_ID);
    PASSIVE_DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // define rarity distribution
    Map<CardRarity, Integer> rarityDistribution = new HashMap<>();
    rarityDistribution.put(CardRarity.BASIC, 27);
    rarityDistribution.put(CardRarity.COMMON, 18);
    rarityDistribution.put(CardRarity.UNCOMMON, 12);
    rarityDistribution.put(CardRarity.RARE, 8);
    CROP_RARITY_DISTRIBUTION = Collections.unmodifiableMap(rarityDistribution);

    // reset hasHarvestedThisTurn at start of combat and at end of turn
    Trigger trigger = new Trigger() {
      public void trigger() {
        logger.debug("Resetting hasHarvestedThisTurn");
        hasHarvestedThisTurn = false;
      }
    };
    getPlayer().addStartOfTurnTrigger(trigger);
    getPlayer().addEndOfTurnTrigger(trigger);
  }

  // TODO: move shared functionality(e.g. harvest logic) to here
}