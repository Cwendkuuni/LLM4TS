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
 sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 }

 @After
 public void tearDown() {
 sector = null;
 }

 @Test
 public void testSimpleSectorConstruction() {
 // Testing constructor with given parameters
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(10, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals(5, sector.getDefenseBonus(CharacterClass.ARCHER));
 assertFalse(sector.isFlag());
 assertEquals(5, sector.getClassBonus());
 assertNull(sector.getOccupier());
 assertEquals(0, sector.getOccupierCount());
 assertEquals(new HashSet<Area>(), sector.getAreas());
 assertEquals(new HashSet<SectorObserver>(), sector.getObservers());
 }

 @Test
 public void testFreeMethod() {
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.free();
 assertTrue(sector.isFree());
 }

 @Test(expected = AssertionError.class)
 public void testFreeMethodWhenOccupiedButNoCombatantsLeft() {
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.free();
 sector.free();
 }

 @Test
 public void testOccupierChangedMethod() {
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.gainControl(CombatantSide.ATTACKERS);
 verify(sectorsObservers).occupierChanged(); // Assuming you have a mock for SectorObserver
 }

 @Test
 public void testGainControlMethod() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDERS);
 assertTrue(sector.isOccupied());
 assertEquals(CombatantSide.DEFENDERS, sector.getOccupier());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGainControlMethodWithInvalidSide() {
 sector.free();
 sector.gainControl(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGainControlMethodWithInvalidSide2() {
 sector.free();
 sector.gainControl(CombatantSide.values()[5]);
 }

 @Test
 public void testGetOccupierMethod() {
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.gainControl(CombatantSide.ATTACKERS);
 assertEquals(CombatantSide.ATTACKERS, sector.getOccupier());
 }

 @Test
 public void testIsOccupiedMethod() {
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.gainControl(CombatantSide.ATTACKERS);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testIsFreeMethod() {
 sector.free();
 assertTrue(sector.isFree());
 }

 @Test
 public void testGetHeightMethod() {
 assertEquals(5, sector.getHeight());
 }

 @Test
 public void testIsDefenderSpawnMethod() {
 assertTrue(sector.isDefenderSpawn());
 }

 @Test
 public void testIsAttackerSpawnMethod() {
 assertTrue(sector.isAttackerSpawn());
 }

 @Test
 public void testIsFlagMethod() {
 assertFalse(sector.isFlag());
 }

 @Test
 public void testGetBonusMethod() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 assertEquals(new SectorBonus(15, 8, 3), sector.getBonus(archer));
 assertEquals(new SectorBonus(12, 10, 4), sector.getBonus(knight));
 }

 @Test
 public void testGetAttackBonusMethod() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 assertEquals(15, sector.getAttackBonus(archer));
 assertEquals(12, sector.getAttackBonus(knight));
 }

 @Test
 public void testGetDefenseBonusMethod() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 assertEquals(8, sector.getDefenseBonus(archer));
 assertEquals(10, sector.getDefenseBonus(knight));
 }

 @Test
 public void testClassBonusMethod() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 assertEquals(5, sector.classBonus(archer));
 assertEquals(5, sector.classBonus(knight));
 }

 @Test
 public void testAddObserveMethod() {
 Set<SectorObserver> observers = new HashSet<SectorObserver>();
 sector.addObserver(observers);
 assertEquals(observers, sector.getObservers());
 }

 @Test
 public void testRemoveObserverMethod() {
 Set<SectorObserver> observers = new HashSet<SectorObserver>(Arrays.asList(new SectorObserver() {}, new SectorObserver() {}));
 sector.addObserver(observers);
 sector.removeObserver(observers.iterator().next());
 assertFalse(sector.getObservers().contains(observers.iterator().next()));
 }

 @Test
 public void testHashCodeMethod() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 assertEquals(sector.hashCode(), sector2.hashCode());
 }

 @Test
 public void testEqualsMethod() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 assertTrue(sector.equals(sector2));
 }

 @Test
 public void testEqualsMethodWithDifferentSector() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 sector2.addArea(new Area());
 sector3.addArea(new Area());
 assertFalse(sector2.equals(sector3));
 }
}