package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.BonusType;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.Weapon;

public class SimpleSectorTest {

    @Test
    public void testSimpleSectorConstruction() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 1, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertEquals(2, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(3, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertTrue(sector.isFlag());
        assertNull(sector.getOccupier());
        assertEquals(new HashSet<>(), sector.getAreas());
        assertEquals(new HashSet<>(), sector.getObservers());
    }

    @Test
    public void testFree() {
        SimpleSector sector = createSectorWithOccupant();
        sector.free();
        assertNull(sector.getOccupier());
    }

    @Test(expected = AssertionError.class)
    public void testFreeNegativeOccupantCount() {
        SimpleSector sector = createSectorWithOccupant();
        sector.free();
        sector.free(); // this should free the last combatant
        sector.free(); // this should throw an AssertionError
    }

    @Test(expected = AssertionError.class)
    public void testFreeNullOccupant() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 1, CharacterClass.ARCHER);
        sector.free();
        sector.free();
        sector.occupierChanged(); // this should set occupier to null
        sector.free(); // this should throw an AssertionError
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = createSector();
        Combatant combatant = createCombatant(CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDERS);
        assertEquals(CombatantSide.DEFENDERS, sector.getOccupier());
        combatant.setSector(null);
        sector.gainControl(CombatantSide.ATTACKERS);
        assertEquals(CombatantSide.ATTACKERS, sector.getOccupier());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGainControlNullOccupier() {
        SimpleSector sector = createSector();
        sector.gainControl(null);
    }

    @Test
    public void testOccupierChanged() {
        SimpleSector sector = createSector();
        Set<SectorObserver> observers = new HashSet<>();
        sector.addObserver(observers);
        sector.gainControl(CombatantSide.DEFENDERS);
        assertTrue(observers.size() > 0);
        observers.clear();
        sector.gainControl(CombatantSide.ATTACKERS);
        assertTrue(observers.size() > 0);
    }

    @Test
    public void testGetOccupier() {
        SimpleSector sector = createSectorWithOccupant();
        assertEquals(CombatantSide.DEFENDERS, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        SimpleSector sector = createSector();
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDERS);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        SimpleSector sector = createSector();
        assertEquals(5, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = createSector();
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = createSector();
        assertTrue(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        SimpleSector sector = createSector();
        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = createSector(1, 2, 3, 4, 5, true, 1, CharacterClass.ARCHER);
        SectorBonus bonus = sector.getBonus(CharacterClass.ARCHER);
        assertEquals(4, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(3, bonus.getDamageBonus());
    }

    @Test
    public void testGetDamageBonus() {
        SimpleSector sector = createSector();
        int damageBonus = sector.getDamageBonus();
        assertEquals(0, damageBonus);
    }

    @Test
    public void testGetAttackBonus() {
        SimpleSector sector = createSector(1, 2, 3, 4, 5, true, 1, CharacterClass.ARCHER);
        int attackBonus = sector.getAttackBonus(CharacterClass.ARCHER);
        assertEquals(2, attackBonus);
    }

    @Test
    public void testGetDefenseBonus() {
        SimpleSector sector = createSector(1, 2, 3, 4, 5, true, 1, CharacterClass.ARCHER);
        int defenseBonus = sector.getDefenseBonus(CharacterClass.ARCHER);
        assertEquals(3, defenseBonus);
    }

    @Test
    public void testClassBonus() {
        SimpleSector sector = createSector(1, 2, 3, 4, 5, true, 1, CharacterClass.ARCHER);
        int classBonus = sector.classBonus(CharacterClass.ARCHER);
        assertEquals(1, classBonus);
    }

    @Test
    public void testGetAreas() {
        SimpleSector sector = createSector();
        assertEquals(new HashSet<>(), sector.getAreas());
    }

    @Test
    public void testAddArea() {
        SimpleSector sector = createSector();
        Area area = createArea();
        sector._addArea(area);
        assertEquals(Arrays.asList(area), new ArrayList<>(sector.getAreas()));
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullArea() {
        SimpleSector sector = createSector();
        sector._addArea(null);
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = createSector();
        SimpleSector sector2 = createSector();
        SimpleSector sector3 = new SimpleSector(5, true, true, 2, 3, true, 1, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, true, true, 2, 3, true, 1, CharacterClass.ARCHER);
        assertEquals(sector1.hashCode(), sector1.hashCode());
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
        assertNotEquals(sector1.hashCode(), sector4.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = createSector();
        SimpleSector sector2 = createSector();
        SimpleSector sector3 = new SimpleSector(5, true, true, 2, 3, true, 1, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, true, true, 2, 3, true, 1, CharacterClass.ARCHER);
        assertTrue(sector1.equals(sector1));
        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
        assertFalse(sector1.equals(sector4));
    }

    @Test
    public void testAddObserver() {
        SimpleSector sector = createSector();
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullObserver() {
        SimpleSector sector = createSector();
        sector.addObserver(null);
    }

    @Test
    public void testRemoveObserver() {
        SimpleSector sector = createSector();
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }

    private SimpleSector createSector() {
        return new SimpleSector(5, true, true, 2, 3, true, 1, CharacterClass.ARCHER);
    }

    private SimpleSector createSectorWithOccupant() {
        SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 1, CharacterClass.ARCHER);
        Combatant combatant = createCombatant(CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDERS);
        sector.setOccupier(combatant);
        return sector;
    }

    private Combatant createCombatant(CharacterClass charClass) {
        Weapon weapon = new Weapon("Test Weapon", "Test Weapon Description", null, SectorBonus.ZERO);
        Combatant combatant = new Combatant("Test Name", charClass, new ArrayList<>());
        combatant.setWeapon(weapon);
        return combatant;
    }

    private Area createArea() {
        return new Area("Test Area");
    }
}