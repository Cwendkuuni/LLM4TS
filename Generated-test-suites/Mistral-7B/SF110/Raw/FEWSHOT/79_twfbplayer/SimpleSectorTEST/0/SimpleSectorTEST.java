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
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertFalse(sector.isAttackerSpawn());
        assertEquals(10, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(5, sector.getDefendBonus(CharacterClass.ARCHER));
        assertFalse(sector.isFlag());
        assertEquals(2, sector.getClassBonus());
        assertSame(CharacterClass.ARCHER, sector.getClassType());
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
        assertTrue(sector.isOccupied());
        assertEquals(new HashSet<Area>(), sector.getAreas());
        assertEquals(new HashSet<SectorObserver>(), sector.getObservers());
    }

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertFalse(sector.isOccupied());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }

    @Test
    public void testOccupierChanged() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        Set<SectorObserver> observers = new HashSet<SectorObserver>(Arrays.asList(new SectorObserver() {
            @Override
            public void occupierChanged() {
                fail();
            }
        }, new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do nothing
            }
        }));
        sector.addObserver(observers.toArray(new SectorObserver[0]));
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(observers.contains(sector.getObserver(0)));
        sector.free();
        assertFalse(observers.contains(sector.getObserver(0)));
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertEquals(2, sector.getOccupierCount());
        assertFalse(sector.isOccupied());
        sector.free();
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }

    @Test
    public void testGetOccupier() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        sector.free();
        assertNull(sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        assertEquals(new SectorBonus(12, 12, 0), sector.getBonus(CharacterClass.ARCHER));
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.WARRIOR);
        assertEquals(new SectorBonus(10, 10, 0), sector2.getBonus(CharacterClass.WARRIOR));
    }

    @Test
    public void testGetDamageBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        assertEquals(0, sector.getDamageBonus());
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.WARRIOR);
        assertEquals(2, sector2.getDamageBonus());
    }

    @Test
    public void testGetAttackBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        assertEquals(12, sector.getAttackBonus(CharacterClass.ARCHER));
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.WARRIOR);
        assertEquals(8, sector2.getAttackBonus(CharacterClass.WARRIOR));
    }

    @Test
    public void testGetDefenseBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        assertEquals(12, sector.getDefenseBonus(CharacterClass.ARCHER));
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.WARRIOR);
        assertEquals(14, sector2.getDefenseBonus(CharacterClass.WARRIOR));
    }

    @Test
    public void testClassBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        assertEquals(2, sector.classBonus(CharacterClass.ARCHER));
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.WARRIOR);
        assertEquals(0, sector2.classBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        Set<Area> areas = new HashSet<Area>();
        sector.addObserver(new SectorObserver() {
            @Override
            public void areasChanged() {
                assertEquals(areas, sector.getAreas());
            }
        });
        sector._addArea(new Area(new Location(1, 1), CharacterClass.ARCHER));
        sector._addArea(new Area(new Location(2, 2), CharacterClass.WARRIOR));
        assertTrue(sector.getAreas().size() == 2);
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, false, 11, 5, false, 2, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.WARRIOR);
        assertEquals(sector1.hashCode(), sector1.hashCode());
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
        assertNotEquals(sector1.hashCode(), sector4.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, false, 11, 5, false, 2, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.WARRIOR);
        assertTrue(sector1.equals(sector1));
        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
        assertFalse(sector1.equals(sector4));
    }
}