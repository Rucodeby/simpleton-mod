package thesimpleton.cards.attack;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import thesimpleton.TheSimpletonMod;
import thesimpleton.actions.RavingStaffAction;
import thesimpleton.enums.AbstractCardEnum;

public class RavingStaff extends CustomCard {
    public static final String ID = "TheSimpletonMod:RavingStaff";
    public static final String NAME;
    public static final String DESCRIPTION;
    public static final String IMG_PATH = "cards/ravingstaff.png";

    private static final CardStrings cardStrings;

    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = -1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_DAMAGE_AMOUNT = 2;

    public RavingStaff() {
        super(ID, NAME, TheSimpletonMod.getResourcePath(IMG_PATH), COST, DESCRIPTION, TYPE, AbstractCardEnum.THE_SIMPLETON_BLUE, RARITY, TARGET);
        this.baseDamage = this.damage = DAMAGE;
        this.isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (this.energyOnUse < EnergyPanel.totalCount) {
            this.energyOnUse = EnergyPanel.totalCount;
        }

        AbstractDungeon.actionManager.addToBottom(
                new RavingStaffAction(p, new DamageInfo(p, this.baseDamage), this.freeToPlayOnce, this.energyOnUse));
    }

    @Override
    public AbstractCard makeCopy() {
        return new RavingStaff();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_DAMAGE_AMOUNT);
        }
    }

    static {
        cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
        NAME = cardStrings.NAME;
        DESCRIPTION = cardStrings.DESCRIPTION;
    }
}
