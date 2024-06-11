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
        SimpleSector sector = new SimpleSector(10, true, true, 5, 5, false, 3, CharacterClass.ARCHER);
        assertEquals(10, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertEquals(5, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(5, sector.getDefendBonus(CharacterClass.ARCHER));
        assertFalse(sector.isFlag());
        assertNull(sector.getOccupier());
        assertTrue(sector.isOccupied());
        assertEquals(0, sector.getOccupierCount());
        assertEmpty(sector.getAreas());
        assertEmpty(sector.getObservers());
    }

    @Test
    public void testSetAndGetOccupier() {
        SimpleSector sector = new SimpleSector(1, false, false, 0, 0, false, 0, CharacterClass.ARCHER);
        assertNull(sector.getOccupier());
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        sector.free();
        assertNull(sector.getOccupier());
        sector.free();
        assertNull(sector.getOccupier());
    }

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(1, true, true, 0, 0, false, 0, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertNull(sector.getOccupier());
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertNull(sector.getOccupier());
        sector.free();
        assertNull(sector.getOccupier());
    }

    @Test
    public void testOccupierChanged() {
        SimpleSector sector = new SimpleSector(1, true, true, 0, 0, false, 0, CharacterClass.ARCHER);
        Set<SectorObserver> observers = new HashSet<>();
        sector.addObserver(observers);
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(observers.size() >= 1);
    }

    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(1, false, false, 0, 0, false, 0, CharacterClass.ARCHER);
        Area area1 = new Area();
        Area area2 = new Area();
        sector._addArea(area1);
        sector._addArea(area2);
        Set<Area> areas = sector.getAreas();
        assertTrue(areas.size() == 2);
        assertTrue(areas.contains(area1));
        assertTrue(areas.contains(area2));
    }

    @Test
    public void testHashCodeAndEquals() {
        SimpleSector sector1 = new SimpleSector(1, true, true, 0, 0, false, 0, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(1, true, true, 0, 0, false, 0, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(1, true, false, 0, 0, false, 0, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(1, true, true, 1, 0, false, 0, CharacterClass.ARCHER);

        Set<Area> areaSet1 = new HashSet<>(Arrays.asList(new Area(), new Area()));
        Set<Area> areaSet2 = new HashSet<>(Arrays.asList(new Area(), new Area()));
        Set<SectorObserver> observerSet1 = new HashSet<>();
        Set<SectorObserver> observerSet2 = new HashSet<>();

        sector1.setAreas(areaSet1);
        sector1.setObservers(observerSet1);
        sector2.setAreas(areaSet2);
        sector2.setObservers(observerSet2);

        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertTrue(sector1.equals(sector2));

        sector1.setAreas(new HashSet<>(Arrays.asList(new Area())));
        assertNotEquals(sector1.hashCode(), sector2.hashCode());
        assertFalse(sector1.equals(sector2));

        sector1.setObservers(observerSet2);
        assertNotEquals(sector1.hashCode(), sector2.hashCode());
        assertFalse(sector1.equals(sector2));
    }

    @Test
    public void testBonusCalculation() {
        SimpleSector sector = new SimpleSector(1, true, true, 0, 0, false, 0, CharacterClass.ARCHER);
        Area area = new Area();
        Combatant combatant = new Combatant(CharacterClass.ARCHER, null);
        Weapon weapon = new Weapon();

        sector._addArea(area);
        area.setOccupier(combatant);
        combatant.setWeapon(weapon);

        assertEquals(0, sector.getDamageBonus());
        assertEquals(0, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(0, sector.getDefendBonus(CharacterClass.ARCHER));

        weapon.setSectorBonus(SectorBonus.BonusType.DAMAGE, 5);
        assertEquals(5, sector.getDamageBonus());

        weapon.setSectorBonus(SectorBonus.BonusType.ATTACK, 3);
        assertEquals(3, sector.getAttackBonus(CharacterClass.ARCHER));

        weapon.setSectorBonus(SectorBonus.BonusType.DEFENSE, 2);
        assertEquals(2, sector.getDefendBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testBonusCalculationWithMultipleWeapons() {
        SimpleSector sector = new SimpleSector(1, true, true, 0, 0, false, 0, CharacterClass.ARCHER);
        Area area1 = new Area();
        Area area2 = new Area();
        sector._addArea(area1);
        sector._addArea(area2);

        Combatant combatant1 = new Combatant(CharacterClass.ARCHER, null);
        Combatant combatant2 = new Combatant(CharacterClass.ARCHER, null);
        Weapon weapon1 = new Weapon();
        Weapon weapon2 = new Weapon();

        area1.setOccupier(combatant1);
        combatant1.setWeapon(weapon1);
        weapon1.setSectorBonus(SectorBonus.BonusType.DAMAGE, 3);
        weapon1.setSectorBonus(SectorBonus.BonusType.ATTACK, 2);

        area2.setOccupier(combatant2);
        combatant2.setWeapon(weapon2);
        weapon2.setSectorBonus(SectorBonus.BonusType.DAMAGE, 5);
        weapon2.setSectorBonus(SectorBonus.BonusType.DEFENSE, 1);

        assertEquals(8, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(3, sector.getDefendBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testBonusCalculationWithDifferentCharacterClasses() {
        SimpleSector sector = new SimpleSector(1, true, true, 0, 0, false, 0, CharacterClass.ARCHER);
        Area area = new Area();
        Combatant combatant = new Combatant(CharacterClass.KNIGHT, null);
        Weapon weapon = new Weapon();

        sector._addArea(area);
        area.setOccupier(combatant);
        combatant.setWeapon(weapon);

        assertEquals(0, sector.getDamageBonus());
        assertEquals(0, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(0, sector.getDefendBonus(CharacterClass.ARCHER));

        weapon.setSectorBonus(SectorBonus.BonusType.DAMAGE, 5);
        assertEquals(5, sector.getDamageBonus());

        assertEquals(0, sector.getAttackBonus(CharacterClass.KNIGHT));
        assertEquals(0, sector.getDefendBonus(CharacterClass.KNIGHT));
    }

    @Test
    public void testBonusCalculationWithEmptyArea() {
        SimpleSector sector = new SimpleSector(1, true, true, 0, 0, false, 0, CharacterClass.ARCHER);
        Area area = new Area();

        sector._addArea(area);

        assertEquals(0, sector.getDamageBonus());
        assertEquals(0, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(0, sector.getDefendBonus(CharacterClass.ARCHER));

        area.setOccupier(null);

        assertEquals(0, sector.getDamageBonus());
        assertEquals(0, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(0, sector.getDefendBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(1, true, true, 0, 0, false, 0, CharacterClass.ARCHER);
        Set<SectorObserver> observers = new HashSet<>();

        sector.addObserver(observers);

        assertEquals(1, observers.size());
        assertTrue(sector.getObservers().contains(observers));
    }

    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(1, true, true, 0, 0, false, 0, CharacterClass.ARCHER);
        Set<SectorObserver> observers = new HashSet<>();

        sector.addObserver(observers);

        assertEquals(1, observers.size());
        assertTrue(sector.getObservers().contains(observers));

        sector.removeObserver(observers);

        assertEquals(0, observers.size());
        assertFalse(sector.getObservers().contains(observers));
    }
}