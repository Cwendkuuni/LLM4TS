package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.observer.SectorObserver;

public class SimpleSectorTest {

    @Test
    public void testSimpleSectorConstructor() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertEquals(10, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(15, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertFalse(sector.isFlag());
        assertEquals(5, sector.getClassBonus());
        assertEquals(CharacterClass.ARCHER, sector.getClassType());
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
        assertTrue(sector.getAreas().isEmpty());
        assertFalse(sector.hasObservers());
    }

    @Test
    public void testFreeMethod() {
        SimpleSector sector = createAndInitializeSector();
        assertEquals(0, sector.getOccupierCount());
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGainControlMethod() {
        SimpleSector sector = createAndInitializeSector();
        Combatant combatant = new Character("Test Combatant", CharacterClass.ARCHER, 100, 100);
        Area area = new Area(5, 5, sector, null, null, null);
        area.setOccupier(combatant);
        sector.addArea(area);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertEquals(1, sector.getOccupierCount());
        assertFalse(sector.isOccupied());
    }

    @Test(expected = AssertionError.class)
    public void testGainControlNegativeOccupierCount() {
        SimpleSector sector = createAndInitializeSector();
        sector.free();
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER); // this should throw an AssertionError
    }

    @Test
    public void testGetOccupierMethod() {
        SimpleSector sector = createAndInitializeSector();
        Combatant combatant = new Character("Test Combatant", CharacterClass.ARCHER, 100, 100);
        Area area = new Area(5, 5, sector, null, null, null);
        area.setOccupier(combatant);
        sector.addArea(area);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testIsOccupiedMethod() {
        SimpleSector sector = createAndInitializeSector();
        assertFalse(sector.isOccupied());
        Combatant combatant = new Character("Test Combatant", CharacterClass.ARCHER, 100, 100);
        Area area = new Area(5, 5, sector, null, null, null);
        area.setOccupier(combatant);
        sector.addArea(area);
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetHeightMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawnMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawnMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        assertTrue(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlagMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        SectorBonus expectedBonus = new SectorBonus(15, 20, 5);
        SectorBonus actualBonus = sector.getBonus(CharacterClass.ARCHER);
        assertEquals(expectedBonus.getAttackBonus(), actualBonus.getAttackBonus());
        assertEquals(expectedBonus.getDefenseBonus(), actualBonus.getDefenseBonus());
        assertEquals(expectedBonus.getDamageBonus(), actualBonus.getDamageBonus());
    }

    @Test
    public void testGetDamageBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        Area area = new Area(5, 5, sector, null, null, null);
        Combatant combatant = new Character("Test Combatant", CharacterClass.ARCHER, 100, 100);
        area.setOccupier(combatant);
        sector.addArea(area);
        sector.gainControl(CombatantSide.DEFENDER);
        int expectedDamageBonus = 5;
        int actualDamageBonus = sector.getDamageBonus();
        assertEquals(expectedDamageBonus, actualDamageBonus);
    }

    @Test
    public void testGetAttackBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        int expectedAttackBonus = 15;
        int actualAttackBonus = sector.getAttackBonus(CharacterClass.ARCHER);
        assertEquals(expectedAttackBonus, actualAttackBonus);
    }

    @Test
    public void testGetDefenseBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        int expectedDefenseBonus = 20;
        int actualDefenseBonus = sector.getDefenseBonus(CharacterClass.ARCHER);
        assertEquals(expectedDefenseBonus, actualDefenseBonus);
    }

    @Test
    public void testClassBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        int expectedClassBonus = 5;
        int actualClassBonus = sector.classBonus(CharacterClass.ARCHER);
        assertEquals(expectedClassBonus, actualClassBonus);
    }

    @Test
    public void testGetAreasMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        Set<Area> expectedAreas = new HashSet<>();
        Set<Area> actualAreas = sector.getAreas();
        assertEquals(expectedAreas, actualAreas);
    }

    @Test
    public void testAddAreaMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        Area area = new Area(5, 5, sector, null, null, null);
        sector.addArea(area);
        Set<Area> expectedAreas = new HashSet<>(Arrays.asList(area));
        Set<Area> actualAreas = sector.getAreas();
        assertEquals(expectedAreas, actualAreas);
    }

    @Test
    public void testHashCodeMethod() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, false, true, 10, 15, false, 5, CharacterClass.ARCHER);
        assertEquals(sector1.hashCode(), sector1.hashCode());
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
    }

    @Test
    public void testEqualsMethod() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, false, true, 10, 15, false, 5, CharacterClass.ARCHER);
        assertTrue(sector1.equals(sector1));
        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
    }

    @Test
    public void testAddObserverMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        assertTrue(sector.hasObservers());
        sector.removeObserver(observer);
        assertFalse(sector.hasObservers());
    }

    @Test
    public void testOccupierChangedMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        Combatant combatant = new Character("Test Combatant", CharacterClass.ARCHER, 100, 100);
        Area area = new Area(5, 5, sector, null, null, null);
        area.setOccupier(combatant);
        sector.addArea(area);
        sector.gainControl(CombatantSide.DEFENDER);
        verify(observer).occupierChanged();
    }

    private SimpleSector createAndInitializeSector() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        for (int i = 0; i < 10; i++) {
            Area area = new Area(5, 5, sector, null, null, null);
            sector.addArea(area);
        }
        return sector;
    }
}