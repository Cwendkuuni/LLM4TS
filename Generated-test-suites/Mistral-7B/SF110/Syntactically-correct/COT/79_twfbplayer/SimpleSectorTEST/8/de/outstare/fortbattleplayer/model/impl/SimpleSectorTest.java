package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.test.TestArea;
import de.outstare.fortbattleplayer.model.impl.test.TestWeapon;
import de.outstare.fortbattleplayer.model.impl.test.TestSectorObserver;
import de.outstare.fortbattleplayer.model.impl.test.TestSectorObserver2;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.Weapon;

public class SimpleSectorTest {

    private SimpleSector sector;
    private Set<Area> areas;
    private Set<SectorObserver> observers;

    @Before
    public void setUp() {
        areas = new HashSet<Area>();
        observers = new HashSet<SectorObserver>();
        sector = new SimpleSector(10, true, true, 5, 5, false, 10, CharacterClass.ARCHER);
    }

    @Test
    public void testSimpleSectorConstructor() {
        assertEquals(10, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertFalse(sector.isFlag());
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
        assertEquals(0, sector.getBonus(CharacterClass.ARCHER).getAttackBonus());
        assertEquals(0, sector.getBonus(CharacterClass.ARCHER).getDefenseBonus());
        assertNull(sector.getBonus(CharacterClass.ARCHER).getDamageBonus());
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testFreeMethod() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertFalse(sector.isOccupied());
        assertNull(sector.getOccupier());
    }

    @Test(expected = AssertionError.class)
    public void testFreeMethod_ErrorCase() {
        sector.free();
    }

    @Test
    public void testGainControlMethod() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertTrue(sector.isOccupied());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertTrue(sector.isOccupied());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGainControlMethod_ErrorCase() {
        sector.gainControl(null);
    }

    @Test
    public void testGetOccupierMethod() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertNull(sector.getOccupier());
    }

    @Test
    public void testIsOccupiedMethod() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGetHeightMethod() {
        assertEquals(10, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawnMethod() {
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawnMethod() {
        assertTrue(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlagMethod() {
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonusMethod() {
        CharacterClass charClass = CharacterClass.ARCHER;
        SectorBonus expectedBonus = new SectorBonus(5 + 10, 5 + 10, sector.getDamageBonus());
        SectorBonus actualBonus = sector.getBonus(charClass);
        assertEquals(expectedBonus.getAttackBonus(), actualBonus.getAttackBonus());
        assertEquals(expectedBonus.getDefenseBonus(), actualBonus.getDefenseBonus());
        assertEquals(expectedBonus.getDamageBonus(), actualBonus.getDamageBonus());
    }

    @Test
    public void testGetDamageBonusMethod() {
        int expectedBonus = 0;
        int actualBonus = sector.getDamageBonus();
        assertEquals(expectedBonus, actualBonus);
    }

    @Test
    public void testGetAttackBonusMethod() {
        CharacterClass charClass = CharacterClass.ARCHER;
        int expectedBonus = 5 + 10 + sector.getBonusFromWeapons(SectorBonus.BonusType.ATTACK);
        int actualBonus = sector.getAttackBonus(charClass);
        assertEquals(expectedBonus, actualBonus);
    }

    @Test
    public void testGetDefenseBonusMethod() {
        CharacterClass charClass = CharacterClass.ARCHER;
        int expectedBonus = 5 + 10 + sector.getBonusFromWeapons(SectorBonus.BonusType.DEFENSE);
        int actualBonus = sector.getDefenseBonus(charClass);
        assertEquals(expectedBonus, actualBonus);
    }

    @Test
    public void testClassBonusMethod() {
        CharacterClass charClass = CharacterClass.ARCHER;
        int expectedBonus = 10;
        int actualBonus = sector.classBonus(charClass);
        assertEquals(expectedBonus, actualBonus);
    }

    @Test
    public void testGetAreasMethod() {
        assertEquals(new HashSet<Area>(), sector.getAreas());
    }

    @Test
    public void testAddAreaMethod() {
        Area area = new TestArea();
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }

    @Test(expected = NullPointerException.class)
    public void testAddAreaMethod_ErrorCase() {
        sector._addArea(null);
    }

    @Test
    public void testHashCodeMethod() {
        SimpleSector sector2 = new SimpleSector(10, true, true, 5, 5, false, 10, CharacterClass.ARCHER);
        assertEquals(sector.hashCode(), sector2.hashCode());
    }

    @Test
    public void testEqualsMethod() {
        SimpleSector sector2 = new SimpleSector(10, true, true, 5, 5, false, 10, CharacterClass.ARCHER);
        assertTrue(sector.equals(sector2));
    }

    @Test
    public void testAddObserverMethod() {
        TestSectorObserver observer = new TestSectorObserver();
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));
    }

    @Test(expected = NullPointerException.class)
    public void testAddObserverMethod_ErrorCase() {
        sector.addObserver(null);
    }

    @Test
    public void testRemoveObserverMethod() {
        TestSectorObserver observer = new TestSectorObserver();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.observers.contains(observer));
    }

    @Test
    public void testOccupierChangedMethod() {
        TestSectorObserver observer = new TestSectorObserver();
        sector.addObserver(observer);
        sector.gainControl(CombatantSide.DEFENDER);
        TestSectorObserver2 observer2 = new TestSectorObserver2();
        sector.addObserver(observer2);
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(observer.occupierChangedCalled);
        assertTrue(observer2.occupierChangedCalled);
    }

    @Test
    public void testGetBonusFromWeaponsMethod() {
        int expectedBonus = 0;
        for (int i = 0; i < 5; ++i) {
            Area area = new TestArea();
            Combatant combatant = new TestCombatant(CharacterClass.ARCHER, new TestWeapon());
            area.setOccupier(combatant);
            sector._addArea(area);
            expectedBonus += combatant.getWeapon().getSectorBonus(SectorBonus.BonusType.ATTACK);
        }
        int actualBonus = sector.getBonusFromWeapons(SectorBonus.BonusType.ATTACK);
        assertEquals(expectedBonus, actualBonus);
    }
}