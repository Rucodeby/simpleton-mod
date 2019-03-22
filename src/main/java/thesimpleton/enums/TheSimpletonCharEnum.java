package thesimpleton.enums;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Finesse;
import com.megacrit.cardcrawl.cards.colorless.FlashOfSteel;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import thesimpleton.cards.attack.*;
import thesimpleton.cards.power.*;
import thesimpleton.cards.skill.*;
import thesimpleton.relics.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TheSimpletonCharEnum {
    @SpireEnum
    public static AbstractPlayer.PlayerClass THE_SIMPLETON;

    public enum Theme {
        ENDLESS_STRIKE("TheSimpletonMod:EndlessStrikeTheme"),
        HEAVY_MACHINE_GUN("TheSimpletonMod:HeavyMachineGunTheme"),
        HELLFIRE("TheSimpletonMod:HellfireTheme"),
        LETS_STRETCHING("TheSimpletonMod:LetsStretchingTheme"),
        MANGLING("TheSimpletonMod:ManglingTheme"),
        MOON_CRYSTAL_POWER("TheSimpletonMod:MoonCrystalPowerTheme"),
        PAINFUL_BOOMERANG("TheSimpletonMod:PainfulBoomerangTheme"),
        SOUL_REAPER("TheSimpletonMod:SoulReaperTheme"),
        STRENGTH_MACHINE("TheSimpletonMod:StrengthMachineTheme"),
        VOID_LORD("TheSimpletonMod:VoidLordTheme");

        private final String themeId;

        Theme(String themeId) {
            this.themeId = themeId;
        }

        // Construct each cards here instead of having them in each enum's constructor, due to performance.
        public List<AbstractCard> getStartingDeck() throws Exception {
            List<AbstractCard> cards = new ArrayList<>();
            switch (this) {
                case ENDLESS_STRIKE:
                    cards.add(new Strike_TheSimpleton());
                    cards.add(new CleanUpWorkshop());
                    cards.add(new SpitefulStrike());
                    cards.add(new TacticalStrike());
                    cards.add(new VitalStrike());
                    cards.add(new FuryStrike());
                    cards.add(new FuryStrike());
                    cards.add(new CircleOfWindfury());
                    cards.add(new CursedSpellbook());
                    cards.add(new StunningStrike());
                    break;
                case HEAVY_MACHINE_GUN:
                    cards.add(new Haymaker());
                    cards.add(new CleanUpWorkshop());
                    cards.add(new CursedShiv());
                    cards.add(new Wield());
                    cards.add(new ManaBullet());
                    cards.add(new CircleOfFlame());
                    cards.add(new CircleOfWindfury());
                    cards.add(new CircleOfCorruption());
                    cards.add(new CircleOfAbyss());
                    cards.add(new Finesse());
                    break;
                case HELLFIRE:
                    cards.add(new CleanUpWorkshop());
                    cards.add(new CursedAmulet());
                    cards.add(new HellfireBreathing());
                    cards.add(new HellfireBreathing());
                    cards.add(new SpitefulPlan());
                    cards.add(new LuckyCharm());
                    cards.add(new CursedSpellbook());
                    cards.add(new NeowsMight());
                    cards.add(new ParallelWorld());
                    cards.add(new CircleOfAmplification());
                    break;
                case LETS_STRETCHING:
                    cards.add(new Haymaker());
                    cards.add(new CleanUpWorkshop());
                    cards.add(new Wield());
                    cards.add(new Finesse());
                    cards.add(new Stretching());
                    cards.add(new Stretching());
                    cards.add(new CircleOfCorruption());
                    cards.add(new CircleOfCorruption());
                    cards.add(new CircleOfWindfury());
                    cards.add(new CircleOfAbyss());
                    break;
                case MANGLING:
                    cards.add(new Haymaker());
                    cards.add(new Stigma());
                    cards.add(new CircleOfFlame());
                    cards.add(new Mangle());
                    cards.add(new Mangle());
                    cards.add(new Mangle());
                    cards.add(new AshesToAshes());
                    cards.add(new NeowsMight());
                    cards.add(new ParallelWorld());
                    cards.add(new FlashOfSteel());
                    break;
                case MOON_CRYSTAL_POWER:
                    cards.add(new Haymaker());
                    cards.add(new CleanUpWorkshop());
                    cards.add(new CursedAmulet());
                    cards.add(new RapidMovement());
                    cards.add(new Grudge());
                    cards.add(new AshesToAshes());
                    cards.add(new DemonicPact());
                    cards.add(new LuckyCharm());
                    cards.add(new SpitefulPlan());
                    cards.add(new CollectTribute());
                    break;
                case PAINFUL_BOOMERANG:
                    cards.add(new CleanUpWorkshop());
                    cards.add(new Wield());
                    cards.add(new CursedSpellbook());
                    cards.add(new CursedBoomerang());
                    cards.add(new BlessingInDisguise());
                    cards.add(new SpitefulPlan());
                    cards.add(new CircleOfAmplification());
                    cards.add(new CircleOfAbyss());
                    cards.add(new OutForBlood());
                    cards.add(new UltimatePain());
                    break;
                case SOUL_REAPER:
                    cards.add(new CleanUpWorkshop());
                    cards.add(new CursedSpellbook());
                    cards.add(new AbyssShockwave());
                    cards.add(new SoulReap());
                    cards.add(new SoulCrush());
                    cards.add(new LuckyCharm());
                    cards.add(new TroublousMinions());
                    cards.add(new TroublousMinions());
                    cards.add(new CircleOfBlood());
                    cards.add(new CollectTribute());
                    break;
                case STRENGTH_MACHINE:
                    cards.add(new CleanUpWorkshop());
                    cards.add(new CursedBoomerang());
                    cards.add(new RavingStaff());
                    cards.add(new SpitefulStrike());
                    cards.add(new CursedSpellbook());
                    cards.add(new DoubleShield());
                    cards.add(new EmpowerCircles());
                    cards.add(new CircleOfFocus());
                    cards.add(new CircleOfWindfury());
                    cards.add(new CircleOfAbyss());
                    break;
                case VOID_LORD:
                    cards.add(new CleanUpWorkshop());
                    cards.add(new CursedSpellbook());
                    cards.add(new CursedArmor());
                    cards.add(new SpitefulPlan());
                    cards.add(new LuckyCharm());
                    cards.add(new ShockAndAwe());
                    cards.add(new CircleOfBlood());
                    cards.add(new CircleOfFocus());
                    cards.add(new CircleOfAmplification());
                    cards.add(new VoidForm());
                    break;
                default:
                    throw new Exception("Unknown Theme enum");
            }
            return cards;
        }

        public String getStartingRelicId() throws Exception {
            switch (this) {
                case ENDLESS_STRIKE:
                    return FourLeafCloverCharm.ID;
                case HEAVY_MACHINE_GUN:
                    return MagicCandle.ID;
                case HELLFIRE:
                    return BlackMagicAdvanced.ID;
                case LETS_STRETCHING:
                    return PinkPellets.ID;
                case MANGLING:
                    return Tack.ID;
                case MOON_CRYSTAL_POWER:
                    return CrystalBall.ID;
                case PAINFUL_BOOMERANG:
                    return BloodyHarpoon.ID;
                case SOUL_REAPER:
                    return SoulVessel.ID;
                case STRENGTH_MACHINE:
                    return DemonicMark.ID;
                case VOID_LORD:
                    return OminousMark.ID;
                default:
                    throw new Exception("Unknown Theme enum");
            }
        }

        @Override
        public String toString() {
            return themeId;
        }

        public static Theme fromString(String id) {
            return Arrays.stream(Theme.values())
                    .filter(theme -> theme.themeId == id)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown theme ID: " + id));
        }
    }
}