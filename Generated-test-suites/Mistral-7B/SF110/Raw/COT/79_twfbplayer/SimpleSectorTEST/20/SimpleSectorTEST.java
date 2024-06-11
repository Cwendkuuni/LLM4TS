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
 sector = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
 }

 @Test
 public void testConstructor() {
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertFalse(sector.isOccupied());
 assertFalse(sector.isFlag());
 assertEquals(3, sector.getClassBonus());
 assertEquals(CharacterClass.ARCHER, sector.getClassType());
 assertNull(sector.getOccupier());
 assertEquals(0, sector.getOccupierCount());
 assertNotNull(sector.getAreas());
 assertFalse(sector.areas.isEmpty());
 }

 @Test
 public void testFree() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 }

 @Test(expected = AssertionError.class)
 public void testFreeWithNegativeOccupierCount() {
 sector.free();
 sector.free();
 sector.free();
 sector.free();
 sector.free();
 }

 @Test
 public void testGainControl() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 assertEquals(3, sector.getOccupierCount());
 }

 @Test
 public void testIsOccupied() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testGetHeight() {
 assertEquals(5, sector.getHeight());
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
 assertFalse(sector.isFlag());
 }

 @Test
 public void testGetBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 SectorBonus expectedBonusArchers = new SectorBonus(3, 2, 1);
 SectorBonus expectedBonusKnights = new SectorBonus(1, 3, 2);
 assertEquals(expectedBonusArchers, sector.getBonus(archer));
 assertEquals(expectedBonusKnights, sector.getBonus(knight));
 }

 @Test
 public void testGetAttackBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 int expectedBonusArchers = 4;
 int expectedBonusKnights = 2;
 assertEquals(expectedBonusArchers, sector.getAttackBonus(archer));
 assertEquals(expectedBonusKnights, sector.getAttackBonus(knight));
 }

 @Test
 public void testGetDefenseBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 int expectedBonusArchers = 2;
 int expectedBonusKnights = 4;
 assertEquals(expectedBonusArchers, sector.getDefenseBonus(archer));
 assertEquals(expectedBonusKnights, sector.getDefenseBonus(knight));
 }

 @Test
 public void testClassBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 assertEquals(3, sector.classBonus(archer));
 assertEquals(3, sector.classBonus(knight));
 }

 @Test
 public void testGetAreas() {
 Set<Area> areas = new HashSet<Area>(Arrays.asList(new Area(), new Area()));
 sector._addArea(new Area());
 sector._addArea(new Area());
 assertEquals(areas, sector.getAreas());
 }

 @Test
 public void testHashCodeAndEquals() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 1, 3, true, 3, CharacterClass.ARCHER);
 assertEquals(sector1, sector1);
 assertEquals(sector1, sector2);
 assertNotEquals(sector1, sector3);
 }

 @Test
 public void testAddObserver() {
 SectorObserver observer = mock(SectorObserver.class);
 sector.addObserver(observer.mock());
 assertTrue(sector.observers.contains(observer));
 }

 @Test
 public void testRemoveObserver() {
 SectorObserver observer = mock(SectorObserver.class);
 sector.addObserver(observer.mock());
 sector.removeObserver(observer);
 assertFalse(sector.observers.contains(observer));
 }
}