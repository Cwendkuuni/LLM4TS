package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Test
 public void testSimpleSectorConstruction() {
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertFalse(sector.isAttackerSpawn());
 assertEquals(4, sector.getClassBonus());
 assertEquals(CharacterClass.ARCHER, sector.getClassType());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEmpty(sector.getAreas());
 }

 @Test
 public void testFreeMethod() {
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 }

 @Test(expected = AssertionError.class)
 public void testFreeMethodWithNegativeOccupierCount() {
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 sector.free(); // This should throw an AssertionError
 }

 @Test
 public void testGainControlMethod() {
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.getOccupier() == CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testGainControlMethodWithSameSide() {
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.getOccupier() == CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testGetOccupierMethod() {
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.getOccupier() == CombatantSide.DEFENDER);
 }

 @Test
 public void testIsOccupiedMethod() {
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testGetBonusMethod() {
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 CharacterClass archer = CharacterClass.ARCHER;
 SectorBonus expectedBonus = new SectorBonus(7, 5, 2);
 SectorBonus bonus = sector.getBonus(archer);
 assertEquals(expectedBonus.getAttackBonus(), bonus.getAttackBonus());
 assertEquals(expectedBonus.getDefenseBonus(), bonus.getDefenseBonus());
 assertEquals(expectedBonus.getDamageBonus(), bonus.getDamageBonus());
 }

 @Test
 public void testGetAttackBonusMethod() {
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 CharacterClass archer = CharacterClass.ARCHER;
 int expectedAttackBonus = 6;
 int attackBonus = sector.getAttackBonus(archer);
 assertEquals(expectedAttackBonus, attackBonus);
 }

 @Test
 public void testGetDefenseBonusMethod() {
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 CharacterClass archer = CharacterClass.ARCHER;
 int expectedDefenseBonus = 5;
 int defenseBonus = sector.getDefenseBonus(archer);
 assertEquals(expectedDefenseBonus, defenseBonus);
 }

 @Test
 public void testClassBonusMethod() {
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 int expectedArchersBonus = 4;
 int expectedKnightsBonus = 0;
 int archerBonus = sector.classBonus(archer);
 int knightBonus = sector.classBonus(knight);
 assertEquals(expectedArchersBonus, archerBonus);
 assertEquals(expectedKnightsBonus, knightBonus);
 }

 @Test
 public void testGetAreasMethod() {
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 Set<Area> expectedAreas = new HashSet<>(Arrays.asList(new Area("testArea1"), new Area("testArea2")));
 Set<Area> areas = sector.getAreas();
 assertEquals(expectedAreas, areas);
 }

 @Test
 public void testAddAreaMethod() {
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 Area area = new Area("testArea");
 sector.addArea(area);
 Set<Area> expectedAreas = new HashSet<>(Arrays.asList(new Area("testArea")));
 Set<Area> areas = sector.getAreas();
 assertEquals(expectedAreas, areas);
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullAreaMethod() {
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 try {
 sector.addArea(null);
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test
 public void testHashCodeMethod() {
 Set<Area> areaSet = new HashSet<>();
 areaSet.add(new Area("testArea"));
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 Set<Area> sectorsAreas = Arrays.asList(new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER), sector).hashCode();
 assertEquals(sectorsAreas.hashCode(), sector.hashCode());
 }

 @Test
 public void testEqualsMethod() {
 SimpleSector sector1 = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, false, 3, 3, false, 4, CharacterClass.ARCHER);
 assertTrue(sector1.equals(sector1));
 assertTrue(sector1.equals(sector2));
 assertFalse(sector1.equals(sector3));
 }

 @Test
 public void testAddObserverMethod() {
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 SectorObserver observer = mock(SectorObserver.class);
 sector.addObserver(observer.mock());
 assertTrue(sector.observers.contains(observer));
 }

 @Test(expected = NullPointerException.class)
 public void testAddNullObserverMethod() {
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 try {
 sector.addObserver(null);
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 @Test
 public void testRemoveObserverMethod() {
 sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
 SectorObserver observer = mock(SectorObserver.class);
 sector.addObserver(observer.mock());
 sector.removeObserver(observer);
 assertFalse(sector.observers.contains(observer));
 }
}