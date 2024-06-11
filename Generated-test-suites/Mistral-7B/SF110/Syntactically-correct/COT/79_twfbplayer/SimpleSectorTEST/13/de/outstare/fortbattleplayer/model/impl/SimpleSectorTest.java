package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.test.util.TestCombatant;
import de.outstare.fortbattleplayer.model.impl.test.util.TestSectorObserver;
import de.outstare.fortbattleplayer.model.impl.test.util.TestWeapon;
import de.outstare.fortbattleplayer.model.impl.test.util.TestArea;

public class SimpleSectorTest {

    @Test
    public void testSimpleSectorConstructor() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertEquals(10, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(15, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertTrue(sector.isFlag());
        assertEquals(20, sector.getClassBonus());
        assertEquals(CharacterClass.ARCHER, sector.getClassType());
        assertTrue(sector.isOccupied());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testFreeMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        TestCombatant combatant = new TestCombatant("TestCombatant", CharacterClass.ARCHER);
        TestArea area = new TestArea(new TestCombatant("TestCombatant2", CharacterClass.ARCHER), 5, 5);
        sector._addArea(area);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertEquals(2, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }

    @Test(expected = AssertionError.class)
    public void testFreeMethodWithNegativeOccupierCount() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        TestCombatant combatant = new TestCombatant("TestCombatant", CharacterClass.ARCHER);
        TestArea area = new TestArea(new TestCombatant("TestCombatant2", CharacterClass.ARCHER), 5, 5);
        sector._addArea(area);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        sector.free();
    }

    @Test
    public void testGainControlMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        TestCombatant combatant = new TestCombatant("TestCombatant", CharacterClass.ARCHER);
        TestArea area = new TestArea(combatant, 5, 5);
        sector._addArea(area);
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testGetOccupierMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        TestCombatant combatant = new TestCombatant("TestCombatant", CharacterClass.ARCHER);
        TestArea area = new TestArea(combatant, 5, 5);
        sector._addArea(area);
        sector.gainControl(CombatantSide.DEFENDER);
        TestCombatant combatant2 = new TestCombatant("TestCombatant2", CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.ATTACKER, combatant2);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testIsOccupiedMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        TestCombatant combatant = new TestCombatant("TestCombatant", CharacterClass.ARCHER);
        TestArea area = new TestArea(combatant, 5, 5);
        sector._addArea(area);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGetHeightMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawnMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawnMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        assertTrue(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlagMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        TestWeapon weapon = new TestWeapon("TestWeapon", 5, 10, 15);
        TestCombatant combatant = new TestCombatant("TestCombatant", CharacterClass.ARCHER);
        combatant.setWeapon(weapon);
        TestArea area = new TestArea(combatant, 5, 5);
        sector._addArea(area);
        TestCombatant combatant2 = new TestCombatant("TestCombatant2", CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.ATTACKER, combatant2);
        assertEquals(new SectorBonus(15, 15, 5), sector.getBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetDamageBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        TestWeapon weapon = new TestWeapon("TestWeapon", 5, 10, 15);
        TestCombatant combatant = new TestCombatant("TestCombatant", CharacterClass.ARCHER);
        combatant.setWeapon(weapon);
        TestArea area = new TestArea(combatant, 5, 5);
        sector._addArea(area);
        TestCombatant combatant2 = new TestCombatant("TestCombatant2", CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.ATTACKER, combatant2);
        assertEquals(5, sector.getDamageBonus());
    }

    @Test
    public void testGetAttackBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        TestWeapon weapon = new TestWeapon("TestWeapon", 5, 10, 15);
        TestCombatant combatant = new TestCombatant("TestCombatant", CharacterClass.ARCHER);
        combatant.setWeapon(weapon);
        TestArea area = new TestArea(combatant, 5, 5);
        sector._addArea(area);
        TestCombatant combatant2 = new TestCombatant("TestCombatant2", CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.ATTACKER, combatant2);
        assertEquals(15, sector.getAttackBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetDefenseBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        TestWeapon weapon = new TestWeapon("TestWeapon", 5, 10, 15);
        TestCombatant combatant = new TestCombatant("TestCombatant", CharacterClass.ARCHER);
        combatant.setWeapon(weapon);
        TestArea area = new TestArea(combatant, 5, 5);
        sector._addArea(area);
        TestCombatant combatant2 = new TestCombatant("TestCombatant2", CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.ATTACKER, combatant2);
        assertEquals(15, sector.getDefenseBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testClassBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        assertEquals(20, sector.classBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetAreasMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        Set<Area> areas = new HashSet<Area>();
        TestArea area = new TestArea(new TestCombatant("TestCombatant", CharacterClass.ARCHER), 5, 5);
        TestArea area2 = new TestArea(new TestCombatant("TestCombatant2", CharacterClass.ARCHER), 5, 5);
        areas.add(area);
        areas.add(area2);
        sector._addArea(area);
        sector._addArea(area2);
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testAddAreaMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        Set<Area> areas = new HashSet<Area>();
        TestArea area = new TestArea(new TestCombatant("TestCombatant", CharacterClass.ARCHER), 5, 5);
        sector._addArea(area);
        areas.add(area);
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testHashCodeMethod() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, false, true, 10, 15, true, 20, CharacterClass.ARCHER);
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
        assertNotEquals(sector1.hashCode(), sector4.hashCode());
    }

    @Test
    public void testEqualsMethod() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, false, true, 10, 15, true, 20, CharacterClass.ARCHER);
        SimpleSector sector5 = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        assertEquals(sector1, sector5);
        assertNotEquals(sector1, sector2);
        assertNotEquals(sector1, sector3);
        assertNotEquals(sector1, sector4);
    }

    @Test
    public void testAddObserverMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        TestSectorObserver observer = new TestSectorObserver();
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));
    }

    @Test
    public void testRemoveObserverMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
        TestSectorObserver observer = new TestSectorObserver();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.observers.contains(observer));
    }

}