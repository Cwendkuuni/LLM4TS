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
    public void testSimpleSectorConstruction() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        assertNotNull(sector);
        assertEquals(10, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertEquals(5, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(3, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertNull(sector.getOccupier());
        assertTrue(sector.isOccupied());
        assertFalse(sector.isFlag());
        assertEquals(0, sector.getBonusFromWeapons(SectorBonus.BonusType.ATTACK));
        assertEquals(0, sector.getBonusFromWeapons(SectorBonus.BonusType.DEFENSE));
        assertNull(sector.classType);
        assertNotNull(sector.areas);
        assertNotNull(sector.observers);
    }

    @Test
    public void testFreeMethod() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        assertNull(sector.getOccupier());
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        sector.free();
        assertNull(sector.getOccupier());
    }

    @Test
    public void testGainControlMethod() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        assertNull(sector.getOccupier());
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testGetOccupierMethod() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        assertNull(sector.getOccupier());
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testIsOccupiedMethod() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetHeightMethod() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        assertEquals(10, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawnMethod() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawnMethod() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        assertTrue(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlagMethod() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonusMethod() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        assertEquals(new SectorBonus(5, 3, 0), sector.getBonus(CharacterClass.ARCHER));

        Weapon weapon = new Weapon("Bow", "Wooden Bow", 1, 5, 1, 1, new HashSet<SectorBonus.BonusType>(), CharacterClass.ARCHER);
        Area area = new Area(new Position(1, 1), new CombatantSide(CombatantSide.DEFENDER), new HashSet<Area>(), new HashSet<SectorObserver>(), new Weapon(weapon));
        sector._addArea(area);
        assertEquals(new SectorBonus(5, 3, 5), sector.getBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetDamageBonusMethod() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        assertEquals(0, sector.getDamageBonus());

        Weapon weapon = new Weapon("Bow", "Wooden Bow", 1, 5, 1, 1, new HashSet<SectorBonus.BonusType>(), CharacterClass.ARCHER);
        Area area = new Area(new Position(1, 1), new CombatantSide(CombatantSide.DEFENDER), new HashSet<Area>(), new HashSet<SectorObserver>(), new Weapon(weapon));
        sector._addArea(area);
        assertEquals(5, sector.getDamageBonus());
    }

    @Test
    public void testGetAttackBonusMethod() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        assertEquals(5, sector.getAttackBonus(CharacterClass.ARCHER));

        Weapon weapon = new Weapon("Bow", "Wooden Bow", 1, 5, 1, 1, new HashSet<SectorBonus.BonusType>(), CharacterClass.ARCHER);
        Area area = new Area(new Position(1, 1), new CombatantSide(CombatantSide.DEFENDER), new HashSet<Area>(), new HashSet<SectorObserver>(), new Weapon(weapon));
        sector._addArea(area);
        assertEquals(10, sector.getAttackBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetDefenseBonusMethod() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        assertEquals(3, sector.getDefenseBonus(CharacterClass.ARCHER));

        Weapon weapon = new Weapon("Bow", "Wooden Bow", 1, 5, 1, 1, new HashSet<SectorBonus.BonusType>(), CharacterClass.ARCHER);
        Area area = new Area(new Position(1, 1), new CombatantSide(CombatantSide.DEFENDER), new HashSet<Area>(), new HashSet<SectorObserver>(), new Weapon(weapon));
        sector._addArea(area);
        assertEquals(8, sector.getDefenseBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testClassBonusMethod() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        assertEquals(2, sector.classBonus(CharacterClass.ARCHER));

        SimpleSector sector2 = new SimpleSector(10, true, true, 5, 3, true, 5, CharacterClass.ARCHER);
        assertEquals(5, sector2.classBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testAddAreaMethod() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        Set<Area> areas = sector.getAreas();
        assertEquals(0, areas.size());

        Area area = new Area(new Position(1, 1), new CombatantSide(CombatantSide.DEFENDER), new HashSet<Area>(), new HashSet<SectorObserver>(), null);
        sector._addArea(area);
        areas = sector.getAreas();
        assertEquals(1, areas.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddAreaMethodWithOccupiedArea() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        Area area = new Area(new Position(1, 1), new CombatantSide(CombatantSide.DEFENDER), new HashSet<Area>(), new HashSet<SectorObserver>(), new Weapon(new Weapon("Bow", "Wooden Bow", 1, 5, 1, 1, new HashSet<SectorBonus.BonusType>(), CharacterClass.ARCHER)));
        sector.gainControl(CombatantSide.DEFENDER);
        sector._addArea(area);
    }

    @Test
    public void testHashCodeMethod() {
        SimpleSector sector1 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(10, true, true, 5, 3, true, 3, CharacterClass.ARCHER);

        Set<Area> areas1 = new HashSet<>();
        Set<Area> areas2 = new HashSet<>();

        Area area1 = new Area(new Position(1, 1), new CombatantSide(CombatantSide.DEFENDER), areas1, new HashSet<SectorObserver>(), null);
        Area area2 = new Area(new Position(1, 1), new CombatantSide(CombatantSide.DEFENDER), areas2, new HashSet<SectorObserver>(), null);

        sector1._addArea(area1);
        sector2._addArea(area2);

        assertEquals(sector1.hashCode(), sector1.hashCode());
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
    }

    @Test
    public void testEqualsMethod() {
        SimpleSector sector1 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(10, true, true, 5, 3, true, 3, CharacterClass.ARCHER);

        Set<Area> areas1 = new HashSet<>();
        Set<Area> areas2 = new HashSet<>();

        Area area1 = new Area(new Position(1, 1), new CombatantSide(CombatantSide.DEFENDER), areas1, new HashSet<SectorObserver>(), null);
        Area area2 = new Area(new Position(1, 1), new CombatantSide(CombatantSide.DEFENDER), areas2, new HashSet<SectorObserver>(), null);

        sector1._addArea(area1);
        sector2._addArea(area2);

        assertTrue(sector1.equals(sector1));
        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
    }

    @Test
    public void testAddObserverMethod() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));
    }

    @Test
    public void testRemoveObserverMethod() {
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));
        sector.removeObserver(observer);
        assertFalse(sector.observers.contains(observer));
    }

}