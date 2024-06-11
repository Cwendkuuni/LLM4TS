package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {

    @Test
    public void testSimpleSectorConstruction() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertFalse(sector.isFlag());
        assertEquals(4, sector.getClassBonus());
        assertEquals(CharacterClass.ARCHER, sector.getClassType());
        assertNull(sector.getOccupier());
        assertTrue(sector.isOccupied());
        assertFalse(sector.isOccupied(CharacterClass.ARCHER));
        assertNotNull(sector.getBonus(CharacterClass.ARCHER));
        assertEquals(0, sector.getDamageBonus());
        assertTrue(sector.getAreas().isEmpty());
        assertTrue(sector.observers.isEmpty());
    }

    @Test
    public void testFreeMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        sector.gainControl(CharacterClass.ARCHER);
        sector.free();
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
        assertFalse(sector.isOccupied(CharacterClass.ARCHER));
    }

    @Test
    public void testGainControlMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
        sector.gainControl(CharacterClass.ARCHER);
        assertEquals(CharacterClass.ARCHER, sector.getOccupier());
        assertTrue(sector.isOccupied());
        assertTrue(sector.isOccupied(CharacterClass.ARCHER));
    }

    @Test
    public void testOccupierChangedMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        Set<SectorObserver> observers = new HashSet<>();
        sector.addObserver(observers);
        sector.gainControl(CharacterClass.Archers);
        sector.free();
        sector.gainControl(CharacterClass.Spearmen);
        assertEquals(2, observers.size());
        for (SectorObserver observer : observers) {
            observer.occupierChanged();
            assertEquals(CharacterClass.Spearmen, sector.getOccupier());
        }
    }

    @Test
    public void testGetHeightMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawnMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawnMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        assertTrue(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlagMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        SectorBonus bonus = sector.getBonus(CharacterClass.ARCHER);
        assertEquals(2, bonus.getAttackBonus());
        assertEquals(3, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetDamageBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        assertEquals(0, sector.getDamageBonus());
    }

    @Test
    public void testGetAttackBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        assertEquals(4, sector.getClassBonus());
        assertEquals(6, sector.getAttackBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetDefenseBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        assertEquals(3, sector.getDefendBonus());
        assertEquals(6, sector.getDefenseBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testClassBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        assertEquals(4, sector.getClassBonus());
        assertEquals(4, sector.classBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetAreasMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        Set<Area> areas = new HashSet<>(Arrays.asList(new Area(1, 1), new Area(2, 2), new Area(3, 3)));
        sector._addArea(areas.iterator().next());
        sector._addArea(areas.iterator().next());
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testHashCodeMethod() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, true, 2, 3, false, 5, CharacterClass.ARCHER);
        assertEquals(sector1.hashCode(), sector1.hashCode());
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
    }

    @Test
    public void testEqualsMethod() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, true, 2, 3, false, 5, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        assertTrue(sector1.equals(sector1));
        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
        assertFalse(sector1.equals(sector4));
        assertFalse(sector1.equals(null));
        assertFalse(sector1.equals("test"));
    }

    @Test
    public void testAddObserverMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        Set<SectorObserver> observers = new HashSet<>();
        sector.addObserver(observers);
        assertEquals(1, observers.size());
    }

    @Test
    public void testRemoveObserverMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        Set<SectorObserver> observers = new HashSet<>();
        sector.addObserver(observers);
        observers.add(new SectorObserver() {});
        assertEquals(1, observers.size());
        sector.removeObserver(observers.iterator().next());
        assertEquals(0, observers.size());
    }
}