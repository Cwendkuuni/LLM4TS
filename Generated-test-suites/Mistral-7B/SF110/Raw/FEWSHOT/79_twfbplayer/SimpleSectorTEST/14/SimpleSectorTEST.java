package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.Weapon;
import de.outstare.fortbattleplayer.model.observer.SectorObserver;

public class SimpleSectorTest {

    @Test
    public void testSimpleSectorConstruction() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertFalse(sector.isAttackerSpawn());
        assertEquals(10, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(5, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertFalse(sector.isFlag());
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
        assertEquals(0, sector.getOccupierCount());
        assertTrue(sector.getAreas().isEmpty());
        assertTrue(sector.getObservers().isEmpty());
    }

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.getOccupierCount());
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        Combatant defender = new Combatant(CharacterClass.ARCHER);
        CombatantSide expectedSide = CombatantSide.DEFENDER;
        sector.gainControl(expectedSide);
        Combatant actualOccupier = sector.getOccupier();
        assertEquals(expectedSide, actualOccupier.getSide());
        assertEquals(1, sector.getOccupierCount());
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetOccupier() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        Combatant defender = new Combatant(CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(defender, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        SectorBonus expectedBonus = new SectorBonus(15, 10, 5);
        assertEquals(expectedBonus, sector.getBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetAttackBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        int expectedBonus = 15;
        assertEquals(expectedBonus, sector.getAttackBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetDefenseBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        int expectedBonus = 10;
        assertEquals(expectedBonus, sector.getDefenseBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testClassBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        int expectedBonus = 5;
        assertEquals(expectedBonus, sector.classBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        Set<Area> expectedAreas = new HashSet<>();
        assertEquals(expectedAreas, sector.getAreas());
    }

    @Test
    public void testAddArea() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        Area area = new Area();
        sector._addArea(area);
        Set<Area> expectedAreas = new HashSet<>(Arrays.asList(area));
        assertEquals(expectedAreas, sector.getAreas());
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(6, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, true, false, 10, 5, false, 6, CharacterClass.ARCHER);
        int expectedHashCode = 525386653;
        int actualHashCode = sector1.hashCode();
        assertEquals(expectedHashCode, actualHashCode);
        assertEquals(expectedHashCode, sector2.hashCode());
        assertNotEquals(expectedHashCode, sector3.hashCode());
        assertNotEquals(expectedHashCode, sector4.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(6, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        assertTrue(sector1.equals(sector2));
        assertTrue(sector1.equals(sector4));
        assertFalse(sector1.equals(sector3));
        assertFalse(sector1.equals(null));
        assertFalse(sector1.equals(new Object()));
    }

    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        SectorObserver sectorObserver = mock(SectorObserver.class);
        sector.addObserver(sectorObserver);
        Set<SectorObserver> expectedObservers = new HashSet<>(Arrays.asList(sectorObserver));
        assertEquals(expectedObservers, sector.getObservers());
    }

    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        SectorObserver sectorObserver = mock(SectorObserver.class);
        sector.addObserver(sectorObserver);
        Set<SectorObserver> expectedObservers = new HashSet<>();
        sector.removeObserver(sectorObserver);
        assertEquals(expectedObservers, sector.getObservers());
    }

    @Test
    public void testOccupierChanged() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        SectorObserver sectorObserver = mock(SectorObserver.class);
        sector.addObserver(sectorObserver);
        Combatant defender = new Combatant(CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.occupierChanged();
        verify(sectorObserver).occupierChanged();
    }
}