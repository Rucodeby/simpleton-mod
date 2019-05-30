package thesimpleton.powers.utils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import thesimpleton.crops.*;
import thesimpleton.orbs.*;
import thesimpleton.powers.*;

// TODO: instantiate these reference crops in CropUtil
public enum Crop {
  ARTICHOKES(null),
  ASPARAGUS(new AsparagusCrop()),
  CORN(new CornCrop()),
  CHILIS(new ChilisCrop()),
  SQUASH(new SquashCrop()),
  MUSHROOMS(new MushroomCrop()),
  ONIONS(new OnionCrop()),
  POTATOES(new PotatoCrop()),
  SPINACH(new SpinachCrop()),
  TURNIPS(new TurnipCrop());

  Crop(AbstractCrop crop) {
    this.crop = crop;
  }

  public final AbstractCrop crop;

  public String getName() {
    return this.crop.getName();
  }

  public AbstractCrop getCrop() {
    return this.crop;
  }

  public static AbstractCropOrb getCropOrb(Crop crop, int amount) {
    switch(crop) {
      case ARTICHOKES:
//        return new ArtichokeCropOrb(amount);
      case ASPARAGUS:
        return new AsparagusCropOrb(amount);
      case CORN:
        return new CornCropOrb(amount);
      case CHILIS:
        return new ChiliCropOrb(amount);
      case MUSHROOMS:
        return new MushroomCropOrb(amount);
      case ONIONS:
        return new OnionCropOrb(amount);
      case POTATOES:
        return new PotatoCropOrb(amount);
      case SPINACH:
        return new SpinachCropOrb(amount);
      case SQUASH:
        return new SquashCropOrb(amount);
      case TURNIPS:
        return new TurnipCropOrb(amount);
      default:
        throw new IllegalArgumentException("Crop cannot be null");
    }
  }
}