package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(5, true, true, 10, 5, false, 2, CharacterClass.ARCHER);
    }

    @Test
    public void testConstructor() {
        assertNotNull(sector);
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertEquals(10, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(5, sector.getDefendBonus(CharacterClass.ARCHER));
        assertFalse(sector.isFlag());
        assertEquals(CharacterClass.ARCHER, sector.getClassType());
        assertTrue(sector.isOccupied());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
        assertFalse(sector.isOccupiedBy(CharacterClass.ARCHER));
        assertTrue(sector.isEmpty());
        assertEquals(0, sector.getAreas().size());
        assertEquals(0, sector.getObservers().size());
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertTrue(sector.isEmpty());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testOccupierChanged() {
        Set<SectorObserver> observers = new HashSet<>();
        sector = new SimpleSector(5, true, true, 10, 5, false, 2, CharacterClass.ARCHER) {
            @Override
            protected void occupierChanged() {
                super.occupierChanged();
                observers.forEach(SectorObserver::occupierChanged);
            }
        };

        sector.addObserver(new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do nothing
            }
        });

        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, observers.size());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertEquals(1, sector.getOccupierCount());

        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertEquals(2, sector.getOccupierCount());

        sector.free();
        assertFalse(sector.isOccupied());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }

    @Test
    public void testGetOccupier() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());

        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGetBonus() {
        Area area1 = new SimpleArea(new SimpleCombatant(CharacterClass.ARCHER, 100, 100), 1);
        Area area2 = new SimpleArea(new SimpleCombatant(CharacterClass.ARCHER, 150, 150), 2);
        sector.addArea(area1);
        sector.addArea(area2);

        assertEquals(12, sector.getDamageBonus());
        assertEquals(15, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(12, sector.getDefenseBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testClassBonus() {
        assertEquals(2, sector.classBonus(CharacterClass.ARCHER));
        assertEquals(0, sector.classBonus(CharacterClass.KNIGHT));
    }

    @Test
    public void testAddArea() {
        sector.addArea(new SimpleArea(new SimpleCombatant(CharacterClass.ARCHER, 100, 100), 1));
        assertEquals(1, sector.getAreas().size());
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 2, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 2, CharacterClass.ARCHER);

        Set<Area> areas1 = new HashSet<>(Arrays.asList(new SimpleArea(new SimpleCombatant(CharacterClass.ARCHER, 100, 100), 1)));
        Set<Area> areas2 = new HashSet<>(Arrays.asList(new SimpleArea(new SimpleCombatant(CharacterClass.ARCHER, 100, 100), 1)));

        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertEquals(sector1.hashCode(), areas1.hashCode());
        assertEquals(sector1.hashCode(), areas2.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 2, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 2, CharacterClass.ARCHER);

        Set<Area> areas1 = new HashSet<>(Arrays.asList(new SimpleArea(new SimpleCombatant(CharacterClass.ARCHER, 100, 100), 1)));
        Set<Area> areas2 = new HashSet<>(Arrays.asList(new SimpleArea(new SimpleCombatant(CharacterClass.ARCHER, 100, 100), 1)));

        assertTrue(sector1.equals(sector1));
        assertTrue(sector1.equals(sector2));
        assertTrue(sector1.equals(areas1));
        assertTrue(sector1.equals(areas2));
    }

    @Test
    public void testAddObserver() {
        sector.addObserver(new SectorObserver() {
            @Override
            public void occupierChanged() {
                fail();
            }
        });

        sector.gainControl(CombatantSide.DEFENDER);
        assertFalse(sector.isOccupied());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullObserver() {
        sector.addObserver(null);
    }

    @Test
    public void testRemoveObserver() {
        sector.addObserver(new SectorObserver() {
            @Override
            public void occupierChanged() {
                fail();
            }
        });

        sector.removeObserver(new SectorObserver() {
            @Override
            public void occupierChanged() {
                fail();
            }
        });

        sector.gainControl(CombatantSide.DEFENDER);
        assertFalse(sector.isOccupied());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }

    @After
    public void tearDown() {
        sector = null;
    }
}