package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
    }

    @Test
    public void testSimpleSectorConstruction() {
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertFalse(sector.isFlag());
        assertEquals(3, sector.getClassBonus());
        assertEquals(CharacterClass.ARCHER, sector.getClassType());
        assertNull(sector.getOccupier());
        assertTrue(sector.isOccupied());
        assertEquals(0, sector.getOccupierCount());
        assertTrue(sector.getAreas().isEmpty());
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
        assertEquals(0, sector.getOccupierCount());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertTrue(sector.isOccupied());
        assertEquals(1, sector.getOccupierCount());

        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertTrue(sector.isOccupied());
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
        assertFalse(sector.isOccupied());
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
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass knight = CharacterClass.KNIGHT;

        assertEquals(new SectorBonus(1, 2, 0), sector.getBonus(archer));
        assertEquals(new SectorBonus(1, 2, 3), sector.getBonus(knight));
    }

    @Test
    public void testGetAttackBonus() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass knight = CharacterClass.KNIGHT;

        assertEquals(4, sector.getAttackBonus(archer));
        assertEquals(6, sector.getAttackBonus(knight));
    }

    @Test
    public void testGetDefenseBonus() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass knight = CharacterClass.KNIGHT;

        assertEquals(2, sector.getDefenseBonus(archer));
        assertEquals(5, sector.getDefenseBonus(knight));
    }

    @Test
    public void testClassBonus() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass knight = CharacterClass.KNIGHT;

        assertEquals(3, sector.classBonus(archer));
        assertEquals(3, sector.classBonus(knight));
    }

    @Test
    public void testGetAreas() {
        Set<Area> areas = new HashSet<Area>(Arrays.asList(new Area(1, 1), new Area(1, 2), new Area(2, 1)));
        sector._addArea(new Area(1, 1));
        sector._addArea(new Area(1, 2));
        sector._addArea(new Area(2, 1));
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, true, 1, 2, false, 4, CharacterClass.ARCHER);

        assertEquals(sector.hashCode(), sector1.hashCode());
        assertNotEquals(sector.hashCode(), sector3.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, true, 1, 2, false, 4, CharacterClass.ARCHER);

        assertTrue(sector.equals(sector1));
        assertFalse(sector.equals(sector3));
    }

    @Test
    public void testAddObserver() {
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // Do nothing
            }
        };
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // Do nothing
            }
        };
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }
}