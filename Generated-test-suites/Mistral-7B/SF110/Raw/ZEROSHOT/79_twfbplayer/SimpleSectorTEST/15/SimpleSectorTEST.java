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
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.test.mock.MockArea;
import de.outstare.fortbattleplayer.model.impl.test.mock.MockSectorObserver;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
    }

    @Test
    public void testConstructor() {
        assertNotNull(sector);
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertEquals(10, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(5, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertFalse(sector.isFlag());
        assertEquals(5, sector.getClassBonus());
        assertNull(sector.getOccupier());
        assertTrue(sector.isOccupied());
        assertTrue(sector.getAreas().isEmpty());
        assertTrue(sector.getObservers().isEmpty());
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.DEFENDERS);
        sector.free();
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.DEFENDERS);
        assertEquals(CombatantSide.DEFENDERS, sector.getOccupier());
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetOccupier() {
        sector.gainControl(CombatantSide.DEFENDERS);
        assertEquals(CombatantSide.DEFENDERS, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        sector.gainControl(CombatantSide.DEFENDERS);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        assertEquals(5, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        assertTrue(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(CharacterClass.ARCHER);
        assertEquals(15, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(sector.getDamageBonus(), bonus.getDamageBonus());
    }

    @Test
    public void testGetAttackBonus() {
        assertEquals(15, sector.getAttackBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetDefenseBonus() {
        assertEquals(5, sector.getDefenseBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetDamageBonus() {
        assertEquals(0, sector.getDamageBonus());
    }

    @Test
    public void testClassBonus() {
        assertEquals(5, sector.classBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetAreas() {
        Set<Area> areas = new HashSet<>();
        areas.add(new MockArea());
        sector.setAreas(areas);
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testAddArea() {
        Set<Area> areas = new HashSet<>();
        sector.setAreas(areas);
        Area area = new MockArea();
        sector.addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
        int hashCode1 = sector1.hashCode();
        int hashCode2 = sector2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
        assertTrue(sector1.equals(sector2));
    }

    @Test
    public void testAddObserver() {
        Set<SectorObserver> observers = new HashSet<>();
        sector.setObservers(observers);
        MockSectorObserver observer = new MockSectorObserver();
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
        observer.occupierChanged();
        assertTrue(observer.isOccupierChangedCalled());
    }

    @Test
    public void testRemoveObserver() {
        Set<SectorObserver> observers = new HashSet<>();
        sector.setObservers(observers);
        MockSectorObserver observer = new MockSectorObserver();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }
}