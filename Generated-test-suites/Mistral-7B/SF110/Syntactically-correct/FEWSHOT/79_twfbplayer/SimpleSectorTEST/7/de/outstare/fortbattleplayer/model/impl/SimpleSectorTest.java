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
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        assertEquals(5, sector.getHeight());
        assertEquals(true, sector.isDefenderSpawn());
        assertEquals(false, sector.isAttackerSpawn());
        assertEquals(10, sector.getAttackBonus(CharacterClass.ARcher));
        assertEquals(20, sector.getDefenseBonus(CharacterClass.ARcher));
        assertEquals(true, sector.isFlag());
        assertEquals(5, sector.getClassBonus());
        assertEquals(CharacterClass.ARcher, sector.getClassType());
    }

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        Combatant combatant = new Combatant(CharacterClass.ARcher, "Combatant");
        Area area = new SimpleArea(new Coordinate(1, 1));
        area.setOccupier(combatant);
        sector._addArea(area);
        assertEquals(15, sector.getBonus(CharacterClass.ARcher).getAttackBonus());
    }

    @Test
    public void testGetAttackBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        assertEquals(10, sector.getAttackBonus(CharacterClass.ARcher));
    }

    @Test
    public void testGetDefenseBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        assertEquals(20, sector.getDefenseBonus(CharacterClass.ARcher));
    }

    @Test
    public void testClassBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        assertEquals(5, sector.classBonus(CharacterClass.ARcher));
    }

    @Test
    public void testAddArea() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        Area area = new SimpleArea(new Coordinate(1, 1));
        sector._addArea(area);
        Set<Area> areas = sector.getAreas();
        assertTrue(areas.contains(area));
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        Set<Area> areas1 = new HashSet<Area>(Arrays.asList(new SimpleArea(new Coordinate(1, 1))));
        Set<Area> areas2 = new HashSet<Area>(Arrays.asList(new SimpleArea(new Coordinate(1, 1))));
        assertEquals(sector1.hashCode(), sector1.hashCode());
        assertEquals(sector1.hashCode(), sector2.hashCode());
        Set<Area> areas3 = new HashSet<Area>(Arrays.asList(new SimpleArea(new Coordinate(1, 2))));
        SimpleSector sector3 = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        sector3._addArea(new SimpleArea(new Coordinate(1, 1)));
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        Set<Area> areas1 = new HashSet<Area>(Arrays.asList(new SimpleArea(new Coordinate(1, 1))));
        Set<Area> areas2 = new HashSet<Area>(Arrays.asList(new SimpleArea(new Coordinate(1, 1))));
        assertTrue(sector1.equals(sector1));
        assertTrue(sector1.equals(sector2));
        SimpleSector sector3 = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        sector3._addArea(new SimpleArea(new Coordinate(1, 1)));
        assertFalse(sector1.equals(sector3));
    }

    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        SectorObserver observer = new SectorObserver() {};
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 20, true, 5, CharacterClass.ARcher);
        SectorObserver observer = new SectorObserver() {};
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }
}