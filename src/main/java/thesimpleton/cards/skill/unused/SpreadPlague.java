package thesimpleton.cards.skill.unused;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import thesimpleton.TheSimpletonMod;
import thesimpleton.enums.AbstractCardEnum;
import thesimpleton.powers.unused.BleedPower;

public class SpreadPlague extends CustomCard {
    public static final String ID = "TheSimpletonMod:SpreadPlague";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/spreadplague.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.SKILL;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 2;
    private static final int BLEED = 3;
    private static final int BLEED_UPGRADE_BONUS = 3;
    private static final int VULNERABLE_DURATION = 3;


    public SpreadPlague() {
        super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);

        this.baseMagicNumber = this.magicNumber = BLEED;
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.getMonsters().monsters.stream()
                .filter(mo -> !mo.isDeadOrEscaped())
                .forEach(mo -> {
                    AbstractDungeon.actionManager.addToBottom(
                            new ApplyPowerAction(
                                    mo, p, new BleedPower(mo, p, this.magicNumber), this.magicNumber, true,
                                    AttackEffect.NONE));
                    AbstractDungeon.actionManager.addToBottom(
                            new ApplyPowerAction(
                                    mo, p, new VulnerablePower(mo, VULNERABLE_DURATION, false),
                                    VULNERABLE_DURATION, true, AttackEffect.NONE));
                });
    }

    @Override
    public AbstractCard makeCopy() {
        return new SpreadPlague();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(BLEED_UPGRADE_BONUS);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}