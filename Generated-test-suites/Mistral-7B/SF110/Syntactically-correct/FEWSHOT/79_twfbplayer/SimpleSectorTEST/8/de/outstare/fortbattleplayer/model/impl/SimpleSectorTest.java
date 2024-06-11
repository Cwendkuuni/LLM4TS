package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.Weapon;
import de.outstare.fortbattleplayer.model.impl.Area;
import de.outstare.fortbattleplayer.model.impl.CharacterClass;
import de.outstare.fortbattleplayer.model.impl.SectorBonus;

public class SimpleSectorTest {

    @Test
    public void testSimpleSectorConstruction() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 3, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
        assertEquals(true, sector.isDefenderSpawn());
        assertEquals(true, sector.isAttackerSpawn());
        assertEquals(10, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(5, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertEquals(false, sector.isFlag());
        assertEquals(3, sector.getClassBonus());
        assertEquals(CharacterClass.ARCHER, sector.getClassType());
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
        assertEquals(0, sector.getOccupierCount());
        assertTrue(sector.getAreas().isEmpty());
        assertTrue(sector.getObservers().isEmpty());
    }

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 3, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.getOccupierCount());
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 3, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertTrue(sector.isOccupied());

        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(2, sector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertTrue(sector.isOccupied());

        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testOccupierChanged() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 3, CharacterClass.ARCHER);
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do something when occupier changes
            }
        };
        sector.addObserver(observer);

        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(observer.isOccupierChangedCalledOnce());

        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(observer.isOccupierChangedCalledTwice());
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 3, CharacterClass.ARCHER);

        Area area1 = new Area(5, 5, new HashSet<Combatant>(), new HashSet<Weapon>(), CharacterClass.ARCHER);
        area1.gainControl(new Combatant(CharacterClass.ARCHER, new Weapon("bow", 5, 10, SectorBonus.BonusType.ATTACK)));
        sector._addArea(area1);

        Area area2 = new Area(5, 5, new HashSet<Combatant>(), new HashSet<Weapon>(), CharacterClass.ARCHER);
        sector._addArea(area2);

        assertEquals(5, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(5, sector.getDefenseBonus(CharacterClass.ARCHER));

        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(15, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(10, sector.getDefenseBonus(CharacterClass.ARCHER));

        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(18, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(12, sector.getDefenseBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 3, CharacterClass.ARCHER);

        SectorObserver observer1 = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do something when occupier changes
            }
        };
        sector.addObserver(observer1);
        assertTrue(sector.getObservers().contains(observer1));

        SectorObserver observer2 = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do something
            }
        };
        sector.addObserver(observer2);
        assertTrue(sector.getObservers().size() == 2);
    }

    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 3, CharacterClass.ARCHER);

        SectorObserver observer1 = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do something when occupier changes
            }
        };
        sector.addObserver(observer1);
        assertTrue(sector.getObservers().contains(observer1));

        sector.removeObserver(observer1);
        assertFalse(sector.getObservers().contains(observer1));

        SectorObserver observer2 = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do something
            }
        };
        sector.addObserver(observer2);
        assertTrue(sector.getObservers().size() == 1);
    }

    @Test
    public void testHashCodeAndEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 3, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 3, CharacterClass.ARCHER);

        SimpleSector sector3 = new SimpleSector(5, true, true, 10, 5, false, 3, CharacterClass.ARCHER);
        sector3.gainControl(CombatantSide.DEFENDER);

        SimpleSector sector4 = new SimpleSector(5, false, true, 10, 5, false, 3, CharacterClass.ARCHER);

        SimpleSector sector5 = new SimpleSector(6, true, true, 10, 5, false, 3, CharacterClass.ARCHER);

        SimpleSector sector6 = new SimpleSector(5, true, true, 10, 5, false, 4, CharacterClass.ARCHER);

        Set<Area> areas1 = new HashSet<Area>(Arrays.asList(new Area(5, 5, new HashSet<Combatant>(), new HashSet<Weapon>(), CharacterClass.ARCHER)));
        Set<Area> areas2 = new HashSet<Area>(Arrays.asList(new Area(5, 5, new HashSet<Combatant>(), new HashSet<Weapon>(), CharacterClass.ARCHER)));
        Set<SectorObserver> observers1 = new HashSet<SectorObserver>();
        Set<SectorObserver> observers2 = new HashSet<SectorObserver>();

        sector1.setAreas(areas1);
        sector1.setObservers(observers1);

        sector2.setAreas(areas2);
        sector2.setObservers(observers2);

        assertEquals(sector1, sector2);
        assertEquals(sector1.hashCode(), sector2.hashCode());

        assertNotEquals(sector1, sector3);
        assertNotEquals(sector1.hashCode(), sector3.hashCode());

        assertNotEquals(sector1, sector4);
        assertNotEquals(sector1.hashCode(), sector4.hashCode());

        assertNotEquals(sector1, sector5);
        assertNotEquals(sector1.hashCode(), sector5.hashCode());

        assertNotEquals(sector1, sector6);
        assertNotEquals(sector1.hashCode(), sector6.hashCode());
    }
}