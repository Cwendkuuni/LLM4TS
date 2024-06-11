package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.BonusType;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SectorObserver;
import de.outstare.fortbattleplayer.model.impl.Weapon;

public class SimpleSectorTest {

    @Test
    public void testSimpleSectorConstruction() {
        SimpleSector sector = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertEquals(1, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(2, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertFalse(sector.isFlag());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
        assertNotNull(sector.getAreas());
        assertEquals(0, sector.hashCode());
    }

    @Test
    public void testFreeMethod() {
        SimpleSector sector = createSectorWithOneCombatant();
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }

    @Test(expected = AssertionError.class)
    public void testFreeMethodNegativeOccupierCount() {
        SimpleSector sector = createSectorWithOneCombatant();
        sector.free();
        sector.free(); //Should free the last combatant, but we will force an exception
        fail("Expected an AssertionError when trying to free a sector with a negative occupier count");
    }

    @Test
    public void testGainControlMethod() {
        SimpleSector sector = createSectorWithOneCombatant();
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testGainControlMethodSameSide() {
        SimpleSector sector = createSectorWithOneCombatant();
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testGetOccupierMethod() {
        SimpleSector sector = createSectorWithOneCombatant();
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testIsOccupiedMethod() {
        SimpleSector sector = createSectorWithOneCombatant();
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testIsDefenderSpawnMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawnMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
        assertTrue(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlagMethod() {
        SimpleSector sector = new SimpleSector(5, false, false, 1, 2, true, 3, CharacterClass.ARCHER);
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonusMethod() {
        SimpleSector sector = createSectorWithWeapon();
        SectorBonus expectedBonus = new SectorBonus(5, 5, 5);
        assertEquals(expectedBonus, sector.getBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetAttackBonusMethod() {
        SimpleSector sector = createSectorWithWeapon();
        int expectedAttackBonus = 5 + 3;
        assertEquals(expectedAttackBonus, sector.getAttackBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetDefenseBonusMethod() {
        SimpleSector sector = createSectorWithWeapon();
        int expectedDefenseBonus = 5 + 3;
        assertEquals(expectedDefenseBonus, sector.getDefenseBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetDamageBonusMethod() {
        SimpleSector sector = createSectorWithWeapon();
        int expectedDamageBonus = 5;
        assertEquals(expectedDamageBonus, sector.getDamageBonus());
    }

    @Test
    public void testClassBonusMethod() {
        SimpleSector sector = new SimpleSector(5, false, false, 1, 2, true, 3, CharacterClass.ARCHER);
        assertEquals(3, sector.classBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetAreasMethod() {
        SimpleSector sector = new SimpleSector(5, false, false, 1, 2, true, 3, CharacterClass.ARCHER);
        assertNotNull(sector.getAreas());
    }

    @Test
    public void testHashCodeMethod() {
        SimpleSector sector1 = new SimpleSector(5, false, false, 1, 2, true, 3, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, false, false, 1, 2, true, 3, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(6, false, false, 1, 2, true, 3, CharacterClass.ARCHER);
        assertEquals(sector1.hashCode(), sector1.hashCode()); //hashCode should be the same for equal objects
        assertNotEquals(sector1.hashCode(), sector2.hashCode()); //hashCode should be different for not equal objects
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
    }

    @Test
    public void testEqualsMethod() {
        SimpleSector sector1 = new SimpleSector(5, false, false, 1, 2, true, 3, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, false, false, 1, 2, true, 3, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(6, false, false, 1, 2, true, 3, CharacterClass.ARCHER);
        assertTrue(sector1.equals(sector1)); //equals should return true for identical objects
        assertTrue(sector1.equals(sector2)); //equals should return true for identical objects
        assertFalse(sector1.equals(sector3)); //equals should return false for not identical objects
    }

    @Test
    public void testAddObserverMethod() {
        SimpleSector sector = new SimpleSector(5, false, false, 1, 2, true, 3, CharacterClass.ARCHER);
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));
    }

    @Test
    public void testRemoveObserverMethod() {
        SimpleSector sector = new SimpleSector(5, false, false, 1, 2, true, 3, CharacterClass.ARCHER);
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.observers.contains(observer));
    }

    @Test
    public void testOccupierChangedMethod() {
        SimpleSector sector = new SimpleSector(5, false, false, 1, 2, true, 3, CharacterClass.ARCHER);
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        sector.gainControl(CombatantSide.DEFENDER);
        verify(observer).occupierChanged();
    }

    private SimpleSector createSectorWithOneCombatant() {
        Area area = new Area();
        Combatant combatant = new Combatant();
        Area.setOccupier(area, combatant);
        SimpleSector sector = new SimpleSector(5, false, false, 1, 2, true, 3, CharacterClass.ARCHER);
        sector._addArea(area);
        sector.gainControl(CombatantSide.ATTACKER);
        return sector;
    }

    private SimpleSector createSectorWithWeapon() {
        Area area = new Area();
        Combatant combatant = new Combatant();
        Weapon weapon = new Weapon();
        Area.setOccupier(area, combatant);
        SimpleSector sector = new SimpleSector(5, false, false, 1, 2, true, 3, CharacterClass.ARCHER);
        sector._addArea(area);
        weapon.setSectorBonus(BonusType.ATTACK, 2);
        combatant.setWeapon(weapon);
        return sector;
    }

}