package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SectorBonus;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(5, true, true, 1, 1, false, 2, CharacterClass.ARCHER);
    }

    @Test
    public void testConstructor() {
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertEquals(1, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(1, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertFalse(sector.isFlag());
        assertEquals(2, sector.getClassBonus());
        assertSame(CharacterClass.ARCHER, sector.getClassType());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
        assertTrue(sector.areas.isEmpty());
        assertTrue(sector.observers.isEmpty());
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }

    @Test
    public void testOccupierChanged() {
        sector.gainControl(CombatantSide.DEFENDER);
        Set<SectorObserver> observers = new HashSet<>();
        sector.addObserver(observers);
        sector.free();
        assertTrue(observers.size() > 0);
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertEquals(1, sector.getOccupierCount());

        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertEquals(2, sector.getOccupierCount());
    }

    @Test
    public void testGetOccupier() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());

        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());

        sector.free();
        assertFalse(sector.isOccupied());
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
        assertEquals(2, bonus.getAttackBonus());
        assertEquals(2, bonus.getDefenseBonus());
        assertNull(bonus.getDamageBonus());
    }

    @Test
    public void testGetAttackBonus() {
        assertEquals(3, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(3, sector.getAttackBonus(CharacterClass.KNIGHT));
    }

    @Test
    public void testGetDefenseBonus() {
        assertEquals(3, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertEquals(3, sector.getDefenseBonus(CharacterClass.KNIGHT));
    }

    @Test
    public void testClassBonus() {
        assertEquals(2, sector.classBonus(CharacterClass.ARCHER));
        assertEquals(0, sector.classBonus(CharacterClass.KNIGHT));
    }

    @Test
    public void testGetAreas() {
        assertEquals(0, sector.getAreas().size());
    }

    @Test
    public void testAddArea() {
        Area area = new SimpleArea(1, 1);
        sector.addArea(area);
        assertEquals(1, sector.getAreas().size());
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 1, 1, false, 2, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 1, 1, false, 2, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, true, 1, 2, false, 2, CharacterClass.ARCHER);
        SimpleArea area = new SimpleArea(1, 1);
        Set<Area> areas = new HashSet<>(Arrays.asList(area));

        assertEquals(sector1.hashCode(), sector1.hashCode());
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
        assertNotEquals(sector1.hashCode(), areas.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 1, 1, false, 2, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 1, 1, false, 2, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, true, 1, 2, false, 2, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, true, true, 1, 1, false, 3, CharacterClass.ARCHER);
        SimpleArea area = new SimpleArea(1, 1);
        Set<Area> areas = new HashSet<>(Arrays.asList(area));

        assertTrue(sector1.equals(sector1));
        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
        assertFalse(sector1.equals(sector4));
        assertFalse(sector1.equals(areas));
    }

    @Test
    public void testAddObserver() {
        Set<SectorObserver> observers = new HashSet<>();
        sector.addObserver(observers);
        assertTrue(observers.size() > 0);
    }

    @Test
    public void testRemoveObserver() {
        Set<SectorObserver> observers = new HashSet<>();
        sector.addObserver(observers);
        sector.removeObserver(observers);
        assertTrue(observers.isEmpty());
    }
}