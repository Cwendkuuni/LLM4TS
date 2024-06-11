package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
    }

    @Test
    public void testConstructor() {
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertEquals(1, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(2, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertFalse(sector.isFlag());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
        assertFalse(sector.isOccupied());
        assertTrue(sector.areas.isEmpty());
        assertTrue(sector.observers.isEmpty());
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }

    @Test
    public void testOccupierChanged() {
        Set<SectorObserver> observers = new HashSet<>();
        SimpleSector sector = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER) {
            @Override
            void occupierChanged() {
                observers.forEach(obs -> obs.occupierChanged());
            }
        };
        sector.addObserver(mock(SectorObserver.class));
        sector.gainControl(CombatantSide.DEFENDER);
        verify(mock(SectorObserver.class)).occupierChanged();
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertEquals(1, sector.getOccupierCount());
        assertTrue(sector.isOccupied());
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
        assertEquals(new SectorBonus(4, 5, 6), sector.getBonus(CharacterClass.ARCHER));
        assertEquals(new SectorBonus(4, 5, 7), sector.getBonus(CharacterClass.KNIGHT));
    }

    @Test
    public void testGetAttackBonus() {
        assertEquals(4, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(5, sector.getAttackBonus(CharacterClass.KNIGHT));
    }

    @Test
    public void testGetDefenseBonus() {
        assertEquals(5, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertEquals(4, sector.getDefenseBonus(CharacterClass.KNIGHT));
    }

    @Test
    public void testClassBonus() {
        assertEquals(3, sector.classBonus(CharacterClass.ARCHER));
        assertEquals(3, sector.classBonus(CharacterClass.KNIGHT));
        assertEquals(0, sector.classBonus(CharacterClass.MAGE));
    }

    @Test
    public void testGetAreas() {
        assertEquals(Collections.emptySet(), sector.getAreas());
    }

    @Test
    public void testAddArea() {
        SimpleSector sector = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        Area area = mock(Area.class);
        sector._addArea(area);
        assertEquals(1, sector.areas.size());
        assertEquals(area, sector.areas.toArray()[0]);
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, false, false, 1, 2, false, 3, CharacterClass.ARCHER);
        assertEquals(sector1.hashCode(), sector1.hashCode());
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, false, false, 1, 2, false, 3, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, true, true, 1, 3, false, 3, CharacterClass.ARCHER);

        assertTrue(sector1.equals(sector1));
        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
        assertFalse(sector1.equals(sector4));
    }

    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.observers.contains(observer));
    }
}