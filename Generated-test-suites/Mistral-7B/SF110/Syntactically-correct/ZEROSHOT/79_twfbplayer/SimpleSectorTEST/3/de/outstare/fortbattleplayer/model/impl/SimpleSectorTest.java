package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.TestCharacterClass;
import de.outstare.fortbattleplayer.model.impl.TestWeapon;

public class SimpleSectorTest
{
    private SimpleSector sector;
    private Set<Area> areas;
    private Area area1;
    private Area area2;
    private Combatant attacker;
    private Combatant defender;
    private SectorObserver observer;
    
    @Before
    public void setUp()
    {
        this.sector = new SimpleSector(10, true, true, 5, 5, false, 3, CharacterClass.ARCHER);
        this.areas = new HashSet<Area>();
        this.area1 = new TestArea();
        this.area2 = new TestArea();
        this.areas.add(this.area1);
        this.areas.add(this.area2);
        this.attacker = new TestCombatant(CharacterClass.ARCHER);
        this.defender = new TestCombatant(CharacterClass.KNIGHT);
        this.observer = new TestSectorObserver();
        this.sector.addObserver(this.observer);
    }

    @Test
    public void testConstructor()
    {
        assertEquals(10, this.sector.getHeight());
        assertTrue(this.sector.isDefenderSpawn());
        assertTrue(this.sector.isAttackerSpawn());
        assertFalse(this.sector.isFlag());
        assertEquals(3, this.sector.getClassBonus());
        assertEquals(CharacterClass.ARCHER, this.sector.getClassType());
        assertNull(this.sector.getOccupier());
        assertFalse(this.sector.isOccupied());
        assertTrue(this.sector.getAreas().isEmpty());
    }

    @Test
    public void testFree()
    {
        this.sector.gainControl(CombatantSide.ATTACKER);
        this.sector.gainControl(CombatantSide.DEFENDER);
        this.sector.free();
        assertNull(this.sector.getOccupier());
        assertFalse(this.sector.isOccupied());
        this.sector.free();
        assertEquals(0, this.sector.getOccupierCount());
    }

    @Test
    public void testGainControl()
    {
        this.sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, this.sector.getOccupierCount());
        assertEquals(CombatantSide.DEFENDER, this.sector.getOccupier());
        this.sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(2, this.sector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, this.sector.getOccupier());
    }

    @Test
    public void testGetOccupier()
    {
        this.sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, this.sector.getOccupier());
        this.sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, this.sector.getOccupier());
    }

    @Test
    public void testIsOccupied()
    {
        this.sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(this.sector.isOccupied());
        this.sector.free();
        assertFalse(this.sector.isOccupied());
    }

    @Test
    public void testGetHeight()
    {
        assertEquals(10, this.sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn()
    {
        assertTrue(this.sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn()
    {
        assertTrue(this.sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag()
    {
        assertFalse(this.sector.isFlag());
    }

    @Test
    public void testGetBonus()
    {
        int archerBonus = this.sector.getBonus(CharacterClass.ARCHER).getAttackBonus();
        int knightBonus = this.sector.getBonus(CharacterClass.KNIGHT).getAttackBonus();
        assertEquals(5, archerBonus);
        assertEquals(5, knightBonus);

        int archerDefenseBonus = this.sector.getBonus(CharacterClass.ARCHER).getDefenseBonus();
        int knightDefenseBonus = this.sector.getBonus(CharacterClass.KNIGHT).getDefenseBonus();
        assertEquals(5, archerDefenseBonus);
        assertEquals(5, knightDefenseBonus);

        int damageBonus = this.sector.getDamageBonus();
        assertEquals(0, damageBonus);
    }

    @Test
    public void testGetDamageBonus()
    {
        int damageBonus = this.sector.getDamageBonus();
        assertEquals(0, damageBonus);

        this.area1.setOccupier(this.attacker);
        damageBonus = this.sector.getDamageBonus();
        TestWeapon weapon = this.attacker.getWeapon();
        assertEquals(weapon.getSectorBonus(SectorBonus.BonusType.DAMAGE), damageBonus);

        this.area1.setOccupier(null);
        this.area2.setOccupier(this.defender);
        damageBonus = this.sector.getDamageBonus();
        weapon = this.defender.getWeapon();
        assertEquals(weapon.getSectorBonus(SectorBonus.BonusType.DAMAGE), damageBonus);
    }

    @Test
    public void testGetAttackBonus()
    {
        int archerBonus = this.sector.getAttackBonus(CharacterClass.ARCHER);
        int knightBonus = this.sector.getAttackBonus(CharacterClass.KNIGHT);
        assertEquals(5 + 3, archerBonus);
        assertEquals(5 + 3, knightBonus);

        this.area1.setOccupier(this.attacker);
        archerBonus = this.sector.getAttackBonus(CharacterClass.ARCHER);
        TestWeapon weapon = this.attacker.getWeapon();
        assertEquals(weapon.getSectorBonus(SectorBonus.BonusType.ATTACK) + 3, archerBonus);

        this.area1.setOccupier(null);
        this.area2.setOccupier(this.defender);
        knightBonus = this.sector.getAttackBonus(CharacterClass.KNIGHT);
        weapon = this.defender.getWeapon();
        assertEquals(weapon.getSectorBonus(SectorBonus.BonusType.ATTACK) + 3, knightBonus);
    }

    @Test
    public void testGetDefenseBonus()
    {
        int archerBonus = this.sector.getDefenseBonus(CharacterClass.ARCHER);
        int knightBonus = this.sector.getDefenseBonus(CharacterClass.KNIGHT);
        assertEquals(5 + 3, archerBonus);
        assertEquals(5 + 3, knightBonus);

        this.area1.setOccupier(this.attacker);
        archerBonus = this.sector.getDefenseBonus(CharacterClass.ARCHER);
        TestWeapon weapon = this.attacker.getWeapon();
        assertEquals(weapon.getSectorBonus(SectorBonus.BonusType.DEFENSE) + 3, archerBonus);

        this.area1.setOccupier(null);
        this.area2.setOccupier(this.defender);
        knightBonus = this.sector.getDefenseBonus(CharacterClass.KNIGHT);
        weapon = this.defender.getWeapon();
        assertEquals(weapon.getSectorBonus(SectorBonus.BonusType.DEFENSE) + 3, knightBonus);
    }

    @Test
    public void testClassBonus()
    {
        int archerBonus = this.sector.classBonus(CharacterClass.ARCHER);
        int knightBonus = this.sector.classBonus(CharacterClass.KNIGHT);
        assertEquals(3, archerBonus);
        assertEquals(3, knightBonus);
    }

    @Test
    public void testGetAreas()
    {
        assertEquals(this.areas, this.sector.getAreas());
    }

    @Test
    public void testAddArea()
    {
        this.sector._addArea(new TestArea());
        assertEquals(3, this.sector.getAreas().size());
    }

    @Test
    public void testHashCode()
    {
        SimpleSector sector1 = new SimpleSector(10, true, true, 5, 5, false, 3, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(10, true, true, 5, 5, false, 3, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(11, true, true, 5, 5, false, 3, CharacterClass.ARCHER);

        int hashCode1 = sector1.hashCode();
        int hashCode2 = sector2.hashCode();
        int hashCode3 = sector3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testEquals()
    {
        SimpleSector sector1 = new SimpleSector(10, true, true, 5, 5, false, 3, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(10, true, true, 5, 5, false, 3, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(11, true, true, 5, 5, false, 3, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(10, true, true, 5, 5, false, 3, CharacterClass.ARCHER);

        assertTrue(sector1.equals(sector1));
        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
        assertFalse(sector1.equals(sector4));
    }

    @Test
    public void testAddObserver()
    {
        this.sector.addObserver(this.observer);
        assertTrue(this.observer.isRegistered());
    }

    @Test
    public void testRemoveObserver()
    {
        this.sector.addObserver(this.observer);
        assertTrue(this.observer.isRegistered());
        this.sector.removeObserver(this.observer);
        assertFalse(this.observer.isRegistered());
    }

    private static class TestCharacterClass implements CharacterClass
    {
        public TestCharacterClass(final String name)
        {
            super();
            setName(name);
        }

        @Override
        public String getName()
        {
            return this.name;
        }
    }

    private static class TestCombatant implements Combatant
    {
        private CharacterClass characterClass;
        private Weapon weapon;

        public TestCombatant(final CharacterClass charClass)
        {
            this.characterClass = charClass;
            this.weapon = new TestWeapon();
        }

        @Override
        public CharacterClass getCharacterClass()
        {
            return this.characterClass;
        }

        @Override
        public Weapon getWeapon()
        {
            return this.weapon;
        }
    }

    private static class TestWeapon implements Weapon
    {
        private int sectorBonusAttack;
        private int sectorBonusDefense;

        @Override
        public int getSectorBonus(final SectorBonus.BonusType bonusType)
        {
            switch (bonusType)
            {
                case ATTACK:
                    return this.sectorBonusAttack;
                case DEFENSE:
                    return this.sectorBonusDefense;
                default:
                    throw new IllegalArgumentException("Unknown bonus type: " + bonusType);
            }
        }

        @Override
        public void setSectorBonus(final SectorBonus.BonusType bonusType, final int bonusInt)
        {
            switch (bonusType)
            {
                case ATTACK:
                    this.sectorBonusAttack = bonusInt;
                    break;
                case DEFENSE:
                    this.sectorBonusDefense = bonusInt;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown bonus type: " + bonusType);
            }
        }
    }

    private static class TestArea implements Area
    {
        private Combatant occupier;

        @Override
        public boolean isOccupied()
        {
            return this.occupier != null;
        }

        @Override
        public Combatant getOccupier()
        {
            return this.occupier;
        }

        @Override
        public void setOccupier(final Combatant combatant)
        {
            this.occupier = combatant;
        }
    }

    private static class TestSectorObserver implements SectorObserver
    {
        private boolean registered = false;

        @Override
        public void occupierChanged()
        {
        }

        public boolean isRegistered()
        {
            return this.registered;
        }
    }
}