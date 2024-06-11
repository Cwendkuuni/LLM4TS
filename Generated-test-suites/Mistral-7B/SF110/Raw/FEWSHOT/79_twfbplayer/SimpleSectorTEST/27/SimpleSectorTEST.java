package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.Weapon;

public class SimpleSectorTest {

    @Test
    public void testSimpleSectorCreation() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        assertEquals(10, sector.getHeight());
        assertEquals(true, sector.isDefenderSpawn());
        assertEquals(false, sector.isAttackerSpawn());
        assertEquals(5, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(3, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertNull(sector.getOccupier());
        assertEquals(0, sector.isOccupied() ? 0 : 1);
        assertFalse(sector.isFlag());
        assertEquals(CharacterClass.ARCHER, sector.getClassType());
        assertTrue(sector.getAreas().isEmpty());
        assertEquals(0, sector.hashCode());
        assertFalse(sector.equals(null));
        assertFalse(sector.equals("not a SimpleSector"));
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertTrue(sector.isOccupied());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertTrue(sector.isOccupied());
        sector.free();
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        Area area1 = new Area(new HashSet<>(), null);
        area1.gainControl(new Combatant(CharacterClass.ARCHER, null, new Weapon(CharacterClass.ARCHER, 0, new SectorBonus(1, 1, 1))));
        sector._addArea(area1);
        assertEquals(2, sector.getAttackBonus(CharacterClass.ARCHER));
        sector = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        Area area2 = new Area(new HashSet<>(), null);
        area2.gainControl(new Combatant(CharacterClass.ARCHER, null, new Weapon(CharacterClass.ARCHER, 0, new SectorBonus(2, 2, 2))));
        sector._addArea(area2);
        assertEquals(7, sector.getAttackBonus(CharacterClass.ARCHER));
        sector = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        Area area3 = new Area(new HashSet<>(), null);
        area3.gainControl(new Combatant(CharacterClass.ARCHER, null, new Weapon(CharacterClass.ARCHER, 0, new SectorBonus(3, 3, 3))));
        sector._addArea(area3);
        assertEquals(12, sector.getAttackBonus(CharacterClass.ARCHER));
        sector = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        Area area4 = new Area(new HashSet<>(), null);
        area4.gainControl(new Combatant(CharacterClass.ARCHER, null, new Weapon(CharacterClass.ARCHER, 0, new SectorBonus(4, 4, 4))));
        sector._addArea(area4);
        assertEquals(17, sector.getAttackBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testBonusDefense() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        Area area1 = new Area(new HashSet<>(), null);
        area1.gainControl(new Combatant(CharacterClass.ARCHER, null, new Weapon(CharacterClass.ARCHER, 0, new SectorBonus(1, 1, 1))));
        sector._addArea(area1);
        assertEquals(6, sector.getDefenseBonus(CharacterClass.ARCHER));
        sector = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        Area area2 = new Area(new HashSet<>(), null);
        area2.gainControl(new Combatant(CharacterClass.ARCHER, null, new Weapon(CharacterClass.ARCHER, 0, new SectorBonus(2, 2, 2))));
        sector._addArea(area2);
        assertEquals(11, sector.getDefenseBonus(CharacterClass.ARCHER));
        sector = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        Area area3 = new Area(new HashSet<>(), null);
        area3.gainControl(new Combatant(CharacterClass.ARCHER, null, new Weapon(CharacterClass.ARCHER, 0, new SectorBonus(3, 3, 3))));
        sector._addArea(area3);
        assertEquals(16, sector.getDefenseBonus(CharacterClass.ARCHER));
        sector = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        Area area4 = new Area(new HashSet<>(), null);
        area4.gainControl(new Combatant(CharacterClass.ARCHER, null, new Weapon(CharacterClass.ARCHER, 0, new SectorBonus(4, 4, 4))));
        sector._addArea(area4);
        assertEquals(21, sector.getDefenseBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testBonusDamage() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        Area area1 = new Area(new HashSet<>(), null);
        area1.gainControl(new Combatant(CharacterClass.ARCHER, null, new Weapon(CharacterClass.ARCHER, 0, new SectorBonus(1, 1, 1))));
        sector._addArea(area1);
        assertEquals(1, sector.getDamageBonus());
        sector = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        Area area2 = new Area(new HashSet<>(), null);
        area2.gainControl(new Combatant(CharacterClass.ARCHER, null, new Weapon(CharacterClass.ARCHER, 0, new SectorBonus(2, 2, 2))));
        sector._addArea(area2);
        assertEquals(3, sector.getDamageBonus());
        sector = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        Area area3 = new Area(new HashSet<>(), null);
        area3.gainControl(new Combatant(CharacterClass.ARCHER, null, new Weapon(CharacterClass.ARCHER, 0, new SectorBonus(3, 3, 3))));
        sector._addArea(area3);
        assertEquals(5, sector.getDamageBonus());
        sector = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        Area area4 = new Area(new HashSet<>(), null);
        area4.gainControl(new Combatant(CharacterClass.ARCHER, null, new Weapon(CharacterClass.ARCHER, 0, new SectorBonus(4, 4, 4))));
        sector._addArea(area4);
        assertEquals(7, sector.getDamageBonus());
    }

    @Test
    public void testHashCodeEquals() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);

        Set<Area> areas1 = new HashSet<>();
        Area area1 = new Area(Arrays.asList(sector1), null);
        areas1.add(area1);
        sector1._addArea(area1);

        Set<Area> areas2 = new HashSet<>();
        Area area2 = new Area(Arrays.asList(sector2), null);
        areas2.add(area2);
        sector2._addArea(area2);

        assertEquals(sector1.hashCode(), sector1.hashCode());
        assertEquals(sector1.hashCode(), sector1.hashCode());
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertEquals(sector1, sector1);
        assertEquals(sector1, sector1);
        assertEquals(sector1, sector2);
    }

    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        SectorObserver observer1 = mock(SectorObserver.class);
        SectorObserver observer2 = mock(SectorObserver.class);
        sector.addObserver(observer1);
        sector.addObserver(observer2);
        assertTrue(sector.observers.contains(observer1));
        assertTrue(sector.observers.contains(observer2));
    }

    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 1, CharacterClass.ARCHER);
        SectorObserver observer1 = mock(SectorObserver.class);
        SectorObserver observer2 = mock(SectorObserver.class);
        sector.addObserver(observer1);
        sector.addObserver(observer2);
        assertTrue(sector.observers.contains(observer1));
        assertTrue(sector.observers.contains(observer2));
        sector.removeObserver(observer1);
        assertFalse(sector.observers.contains(observer1));
        assertTrue(sector.observers.contains(observer2));
        sector.removeObserver(observer2);
        assertFalse(sector.observers.contains(observer1));
        assertFalse(sector.observers.contains(observer2));
    }
}