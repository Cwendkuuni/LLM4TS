package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Before
 public void setup() {
 sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
 }

 @Test
 public void testSimpleSectorConstructor() {
 // Check that all instance variables are initialized correctly
 assertEquals(10, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(5, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals(3, sector.getDefenseBonus(CharacterClass.ARCHER));
 assertTrue(sector.isFlag());
 assertEquals(2, sector.getClassBonus());
 assertEquals(CharacterClass.ARCHER, sector.getClassType());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEmpty(sector.getAreas());
 assertEmpty(sector.getObservers());
 }

 @Test
 public void testFree() {
 // Test freeing an empty sector
 sector.free();
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());

 // Test freeing a sector with combatants
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 }

 @Test(expected = AssertionError.class)
 public void testFreeWhenOccupierCountIsLessThanZero() {
 sector.free();
 sector.free();
 sector.free();
 sector.free(); // This should throw an AssertionError
 }

 @Test
 public void testOccupierChanged() {
 Set<SectorObserver> observers = new HashSet<>();
 sector.addObserver(observers);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(observers.size() > 0);
 sector.occupierChanged();
 assertTrue(observers.isEmpty());
 }

 @Test
 public void testGainControl() {
 // Test gaining control of an empty sector
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 assertTrue(sector.isOccupied());

 // Test gaining control of a sector that is already occupied by the same side
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 assertTrue(sector.isOccupied());

 // Test gaining control of a sector that is occupied by a different side
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 assertTrue(sector.isOccupied());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGainControlWithInvalidSide() {
 sector.gainControl(null);
 }

 @Test
 public void testGetOccupier() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(sector.isOccupied());
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGetHeight() {
 assertEquals(10, sector.getHeight());
 }

 @Test
 public void testIsDefenderSpawn() {
 assertTrue(sector.isDefenderSpawn());
 }

 @Test
 public void testIsAttackerSpawn() {
 assertTrue(sector.isAttackerSpawn());
 }

 @Test
 public void testIsFlag() {
 assertTrue(sector.isFlag());
 }

 @Test
 public void testGetBonusWithValidCharClass() {
 SectorBonus expectedBonus = new SectorBonus(7, 8, 3);
 SectorBonus actualBonus = sector.getBonus(CharacterClass.ARCHER);
 assertEquals(expectedBonus, actualBonus);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetBonusWithInvalidCharClass() {
 sector.getBonus(null);
 }

 @Test
 public void testGetDamageBonus() {
 int expectedBonus = 3;
 int actualBonus = sector.getDamageBonus();
 assertEquals(expectedBonus, actualBonus);
 }

 @Test
 public void testGetAttackBonusWithValidCharClass() {
 int expectedBonus = 8;
 int actualBonus = sector.getAttackBonus(CharacterClass.ARCHER);
 assertEquals(expectedBonus, actualBonus);
 }

 @Test
 public void testGetAttackBonusWithInvalidCharClass() {
 assertEquals(0, sector.getAttackBonus(CharacterClass.MAGE));
 }

 @Test
 public void testGetDefenseBonusWithValidCharClass() {
 int expectedBonus = 5;
 int actualBonus = sector.getDefenseBonus(CharacterClass.ARCHER);
 assertEquals(expectedBonus, actualBonus);
 }

 @Test
 public void testGetDefenseBonusWithInvalidCharClass() {
 assertEquals(0, sector.getDefenseBonus(CharacterClass.MAGE));
 }

 @Test
 public void testClassBonusWithValidCharClass() {
 int expectedBonus = 2;
 int actualBonus = sector.classBonus(CharacterClass.ARCHER);
 assertEquals(expectedBonus, actualBonus);
 }

 @Test
 public void testClassBonusWithInvalidCharClass() {
 assertEquals(0, sector.classBonus(CharacterClass.MAGE));
 }

 @Test
 public void testGetAreas() {
 Set<Area> expectedAreas = new HashSet<>(Arrays.asList(new Area()));
 Set<Area> actualAreas = sector.getAreas();
 assertEquals(expectedAreas, actualAreas);
 }

 @Test
 public void testAddArea() {
 Set<Area> areas = new HashSet<>();
 sector.addArea(new Area());
 assertEquals(1, areas.size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullArea() {
 sector.addArea(null);
 }

 @Test
 public void testHashCode() {
 SimpleSector anotherSector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
 int expectedHashCode = 371291883;
 int actualHashCode = sector.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 assertEquals(expectedHashCode, anotherSector.hashCode());
 }

 @Test
 public void testEquals() {
 SimpleSector anotherSector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
 assertTrue(sector.equals(anotherSector));
 }

 @Test
 public void testEqualsWithDifferentAreas() {
 Set<Area> anotherAreas = new HashSet<>(Arrays.asList(new Area(), new Area()));
 SimpleSector anotherSector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
 anotherSector.setAreas(anotherAreas);
 assertFalse(sector.equals(anotherSector));
 }

 @Test
 public void testEqualsWithDifferentAttackBonus() {
 SimpleSector anotherSector = new SimpleSector(10, true, true, 6, 3, true, 2, CharacterClass.ARCHER);
 assertFalse(sector.equals(anotherSector));
 }

 @Test
 public void testEqualsWithDifferentDefendBonus() {
 SimpleSector anotherSector = new SimpleSector(10, true, true, 5, 4, true, 2, CharacterClass.ARCHER);
 assertFalse(sector.equals(anotherSector));
 }

 @Test
 public void testEqualsWithDifferentClassBonus() {
 SimpleSector anotherSector = new SimpleSector(10, true, true, 5, 3, true, 3, CharacterClass.ARCHER);
 assertFalse(sector.equals(anotherSector));
 }

 @Test
 public void testEqualsWithDifferentClassType() {
 SimpleSector anotherSector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.MAGE);
 assertFalse(sector.equals(anotherSector));
 }

 @Test
 public void testEqualsWithDifferentHeight() {
 SimpleSector anotherSector = new SimpleSector(11, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
 assertFalse(sector.equals(anotherSector));
 }

 @Test
 public void testEqualsWithDifferentDefenderSpawn() {
 SimpleSector anotherSector = new SimpleSector(10, false, true, 5, 3, true, 2, CharacterClass.ARCHER);
 assertFalse(sector.equals(anotherSector));
 }

 @Test
 public void testEqualsWithDifferentAttackerSpawn() {
 SimpleSector anotherSector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.ARCHER);
 assertFalse(sector.equals(anotherSector));
 }

 @Test
 public void testEqualsWithDifferentFlag() {
 SimpleSector anotherSector = new SimpleSector(10, true, true, 5, 3, false, 2, CharacterClass.ARCHER);
 assertFalse(sector.equals(anotherSector));
 }

 @Test
 public void testAddObserver() {
 Set<SectorObserver> observers = new HashSet<>();
 sector.addObserver(observers);
 assertTrue(observers.size() > 0);
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullObserver() {
 sector.addObserver(null);
 }

 @Test
 public void testRemoveObserver() {
 Set<SectorObserver> observers = new HashSet<>();
 sector.addObserver(observers);
 assertTrue(observers.size() > 0);
 sector.removeObserver(observers);
 assertTrue(observers.isEmpty());
 }

 @Test
 public void testRemoveNonexistentObserver() {
 Set<SectorObserver> observers = new HashSet<>();
 sector.removeObserver(observers);
 assertTrue(observers.isEmpty());
 }
}