package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.test.DummyArea;
import de.outstare.fortbattleplayer.model.test.DummyCharacterClass;
import de.outstare.fortbattleplayer.model.test.DummyCombatant;
import de.outstare.fortbattleplayer.model.test.DummySectorObserver;

public class SimpleSectorTest {

    @Test
    public void testSimpleSectorConstructor() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, false, 4, DummyCharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertFalse(sector.isFlag());
        assertEquals(4, sector.getClassBonus());
        assertEquals(DummyCharacterClass.ARCHER, sector.getClassType());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
        assertTrue(sector.areas.isEmpty());
        assertTrue(sector.observers.isEmpty());
    }

    @Test
    public void testFreeMethod() {
        SimpleSector sector = createSectorWithCombatant();
        sector.free();
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }

    @Test(expected = AssertionError.class)
    public void testFreeMethodWithNegativeOccupierCount() {
        SimpleSector sector = createSectorWithCombatant();
        sector.occupierCount = -1;
        sector.free();
    }

    @Test
    public void testGainControlMethod() {
        SimpleSector sector = createSector();
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertEquals(1, sector.getOccupierCount());
    }

    @Test
    public void testGainControlMethodWithSameSide() {
        SimpleSector sector = createSector();
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertEquals(2, sector.getOccupierCount());
    }

    @Test
    public void testGetOccupierMethod() {
        SimpleSector sector = createSectorWithCombatant();
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testIsOccupiedMethod() {
        SimpleSector sector = createSectorWithCombatant();
        assertTrue(sector.isOccupied());
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGetHeightMethod() {
        SimpleSector sector = createSector();
        assertEquals(5, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawnMethod() {
        SimpleSector sector = createSector();
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawnMethod() {
        SimpleSector sector = createSector();
        assertTrue(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlagMethod() {
        SimpleSector sector = createSector();
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonusMethod() {
        SimpleSector sector = createSectorWithBonusArea();
        SectorBonus expectedBonus = new SectorBonus(5, 3, 2);
        SectorBonus actualBonus = sector.getBonus(DummyCharacterClass.ARCHER);
        assertEquals(expectedBonus.getAttackBonus(), actualBonus.getAttackBonus());
        assertEquals(expectedBonus.getDefenseBonus(), actualBonus.getDefenseBonus());
        assertEquals(expectedBonus.getDamageBonus(), actualBonus.getDamageBonus());
    }

    @Test
    public void testGetDamageBonusMethod() {
        SimpleSector sector = createSectorWithBonusArea();
        int expectedBonus = 2;
        int actualBonus = sector.getDamageBonus();
        assertEquals(expectedBonus, actualBonus);
    }

    @Test
    public void testGetAttackBonusMethod() {
        SimpleSector sector = createSectorWithBonusArea();
        int expectedBonus = 7;
        int actualBonus = sector.getAttackBonus(DummyCharacterClass.ARCHER);
        assertEquals(expectedBonus, actualBonus);
    }

    @Test
    public void testGetDefenseBonusMethod() {
        SimpleSector sector = createSectorWithBonusArea();
        int expectedBonus = 4;
        int actualBonus = sector.getDefenseBonus(DummyCharacterClass.ARCHER);
        assertEquals(expectedBonus, actualBonus);
    }

    @Test
    public void testClassBonusMethod() {
        SimpleSector sector = createSector();
        int expectedBonus = 0;
        int actualBonus = sector.classBonus(DummyCharacterClass.ARCHER);
        assertEquals(expectedBonus, actualBonus);

        SimpleSector sector2 = createSector(DummyCharacterClass.ARCHER);
        int expectedBonus2 = 4;
        int actualBonus2 = sector2.classBonus(DummyCharacterClass.ARCHER);
        assertEquals(expectedBonus2, actualBonus2);
    }

    @Test
    public void testGetAreasMethod() {
        SimpleSector sector = createSectorWithAreas();
        Set<Area> expectedAreas = new HashSet<>(Arrays.asList(area1, area2));
        Set<Area> actualAreas = sector.getAreas();
        assertEquals(expectedAreas, actualAreas);
    }

    @Test
    public void testAddAreaMethod() {
        SimpleSector sector = createSector();
        Area area = new DummyArea();
        sector._addArea(area);
        assertTrue(sector.areas.contains(area));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAreaMethodWithUnmodifiableSet() {
        SimpleSector sector = createSector();
        Set<Area> unmodifiableAreas = Collections.unmodifiableSet(sector.areas);
        Area area = new DummyArea();
        sector._addArea(area);
    }

    @Test
    public void testHashCodeAndEqualsMethods() {
        SimpleSector sector1 = createSector();
        SimpleSector sector2 = createSector();
        SimpleSector sector3 = new SimpleSector(5, true, true, 2, 3, false, 4, DummyCharacterClass.ARCHER);

        assertEquals(sector1.hashCode(), sector1.hashCode());
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());

        assertTrue(sector1.equals(sector1));
        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
    }

    @Test
    public void testAddObserverMethod() {
        SimpleSector sector = createSector();
        DummySectorObserver observer = new DummySectorObserver();
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddObserverMethodWithNullObserver() {
        SimpleSector sector = createSector();
        DummySectorObserver nullObserver = null;
        sector.addObserver(nullObserver);
    }

    @Test
    public void testRemoveObserverMethod() {
        SimpleSector sector = createSectorWithObserver();
        DummySectorObserver observer = sector.observers.iterator().next();
        sector.removeObserver(observer);
        assertFalse(sector.observers.contains(observer));
    }

    private SimpleSector createSector() {
        return new SimpleSector(5, true, true, 2, 3, false, 4, DummyCharacterClass.ARCHER);
    }

    private SimpleSector createSectorWithCombatant() {
        SimpleSector sector = createSector();
        sector.gainControl(CombatantSide.ATTACKER);
        DummyCombatant combatant = new DummyCombatant(CombatantSide.ATTACKER, new DummyWeapon());
        sector.areas.add(new DummyArea(combatant));
        return sector;
    }

    private SimpleSector createSectorWithBonusArea() {
        SimpleSector sector = createSector();
        sector.areas.add(new DummyAreaWithBonus(5, 3, 2));
        return sector;
    }

    private SimpleSector createSectorWithAreas() {
        SimpleSector sector = createSector();
        sector.areas.addAll(Arrays.asList(new DummyArea(), new DummyArea()));
        return sector;
    }

    private SimpleSector createSectorWithObserver() {
        SimpleSector sector = createSector();
        DummySectorObserver dummyObserver = new DummySectorObserver();
        sector.addObserver(dummyObserver);
        return sector;
    }

    private Area area1 = new DummyArea();
    private Area area2 = new DummyArea();

}