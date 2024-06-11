package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;

public class SimpleSectorTest {

    @Test
    public void testSimpleSectorConstruction() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertEquals(10, sector.getAttackBonus(CharacterClass.Archer));
        assertEquals(5, sector.getDefenseBonus(CharacterClass.Archer));
        assertTrue(sector.isFlag());
        assertEquals(2, sector.getClassBonus());
        assertEquals(CharacterClass.Archer, sector.getClassType());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
        assertTrue(sector.isEmpty());
        assertTrue(sector.isControllableBy(CharacterClass.Archer));
    }

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        sector.gainControl(CombatantSide.Attacker);
        sector.free();
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
        assertTrue(sector.isEmpty());
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        sector.gainControl(CombatantSide.Attacker);
        assertTrue(sector.getOccupier() instanceof Combatant);
        assertEquals(CombatantSide.Attacker, sector.getOccupier().getSide());
        assertEquals(1, sector.getOccupierCount());
        assertFalse(sector.isEmpty());
        assertFalse(sector.isControllableBy(CharacterClass.ArchersApprentice));
    }

    @Test
    public void testGetOccupier() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        Combatant combatant = new Combatant(CharacterClass.ArchersApprentice, new Weapon(CharacterClass.Bow, 0, 10, 5, 5, 5));
        sector.gainControl(CombatantSide.Defender, combatant);
        assertEquals(combatant, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.Attacker);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        Set<Area> areas = new HashSet<Area>(Arrays.asList(new Area(1, 1), new Area(2, 2)));
        sector._addArea(new Area(1, 1));
        sector._addArea(new Area(2, 2));
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        assertEquals(new SectorBonus(12, 7, 3), sector.getBonus(CharacterClass.Archer));
    }

    @Test
    public void testGetDamageBonus() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        assertEquals(3, sector.getDamageBonus());
    }

    @Test
    public void testGetAttackBonus() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        assertEquals(13, sector.getAttackBonus(CharacterClass.Archer));
    }

    @Test
    public void testGetDefenseBonus() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        assertEquals(8, sector.getDefenseBonus(CharacterClass.Archer));
    }

    @Test
    public void testClassBonus() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        assertEquals(2, sector.classBonus(CharacterClass.Archer));
    }

    @Test
    public void testIsControllableBy() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        assertTrue(sector.isControllableBy(CharacterClass.Archer));
        assertFalse(sector.isControllableBy(CharacterClass.ArchersApprentice));
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        SimpleSector sector3 = new SimpleSector(6, true, true, 10, 5, true, 2, CharacterClass.Archer);
        SimpleSector sector4 = new SimpleSector(5, false, true, 10, 5, true, 2, CharacterClass.Archer);
        Set<Area> areas1 = new HashSet<Area>(Arrays.asList(new Area(1, 1), new Area(2, 2)));
        Set<Area> areas2 = new HashSet<Area>(Arrays.asList(new Area(1, 1), new Area(3, 3)));
        sector1._addArea(new Area(1, 1));
        sector1._addArea(new Area(2, 2));
        sector2._addArea(new Area(1, 1));
        sector2._addArea(new Area(2, 2));
        sector3._addArea(new Area(1, 1));
        sector3._addArea(new Area(2, 2));
        sector4._addArea(new Area(1, 1));
        sector4._addArea(new Area(2, 2));
        assertEquals(sector1.hashCode(), sector1.hashCode());
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
        assertNotEquals(sector1.hashCode(), sector4.hashCode());
        assertNotEquals(sector1.hashCode(), Arrays.asList(new Area(1, 1), new Area(2, 2)).hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        SimpleSector sector3 = new SimpleSector(6, true, true, 10, 5, true, 2, CharacterClass.Archer);
        SimpleSector sector4 = new SimpleSector(5, false, true, 10, 5, true, 2, CharacterClass.Archer);
        Set<Area> areas1 = new HashSet<Area>(Arrays.asList(new Area(1, 1), new Area(2, 2)));
        Set<Area> areas2 = new HashSet<Area>(Arrays.asList(new Area(1, 1), new Area(3, 3)));
        sector1._addArea(new Area(1, 1));
        sector1._addArea(new Area(2, 2));
        sector2._addArea(new Area(1, 1));
        sector2._addArea(new Area(2, 2));
        sector3._addArea(new Area(1, 1));
        sector3._addArea(new Area(2, 2));
        sector4._addArea(new Area(1, 1));
        sector4._addArea(new Area(2, 2));
        assertTrue(sector1.equals(sector1));
        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
        assertFalse(sector1.equals(sector4));
        assertFalse(sector1.equals(Arrays.asList(new Area(1, 1), new Area(2, 2))));
    }

    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        verify(observer).occupierChanged();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullObserver() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        sector.addObserver(null);
    }

    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 2, CharacterClass.Archer);
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        sector.removeObserver(observer);
        verifyNoMoreInteractions(observer);
    }
}