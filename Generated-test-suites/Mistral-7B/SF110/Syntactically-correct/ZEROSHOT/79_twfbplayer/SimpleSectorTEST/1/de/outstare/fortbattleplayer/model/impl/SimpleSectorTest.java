package de.outstare.fortbattleplayer.model.impl;

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
 public void init() {
 sector = new SimpleSector(5, true, true, 10, 5, false, 1, CharacterClass.ARCHER);
 }

 @Test
 public void testSimpleSectorConstruction() {
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(10, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals(5, sector.getDefenseBonus(CharacterClass.ARCHER));
 assertFalse(sector.isFlag());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEmpty(sector.getAreas());
 }

 @Test
 public void testFreeMethod() {
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.free();
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 }

 @Test(expected = AssertionError.class)
 public void testFreeMethodWithNegativeOccupierCount() {
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.free();
 sector.free();
 }

 @Test
 public void testGainControlMethod() {
 sector.free();
 sector.gainControl(CombatantSide.ATTACKERS);
 assertEquals(CombatantSide.ATTACKERS, sector.getOccupier());
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testGainControlMethodWithSameSide() {
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.gainControl(CombatantSide.DEFENDERS);
 assertEquals(CombatantSide.DEFENDERS, sector.getOccupier());
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testGetOccupierMethod() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDERS);
 assertEquals(CombatantSide.DEFENDERS, sector.getOccupier());
 }

 @Test
 public void testIsOccupiedMethod() {
 sector.free();
 assertFalse(sector.isOccupied());
 sector.gainControl(CombatantSide.DEFENDERS);
 assertTrue(sector.isOccupied());
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
 SectorBonus expectedArchersBonus = new SectorBonus(13, 8, 3);
 SectorBonus expectedKnightsBonus = new SectorBonus(12, 9, 2);
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.addArea(new Area(new Position(1, 1), Arrays.asList(CharacterClass.ARCHER), CharacterClass.ARCHER));
 sector.addArea(new Area(new Position(2, 2), Arrays.asList(CharacterClass.KNIGHT), CharacterClass.KNIGHT));
 assertEquals(expectedArchersBonus, sector.getBonus(archer));
 assertEquals(expectedKnightsBonus, sector.getBonus(knight));
 }

 @Test
 public void testGetAttackBonusMethod() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 int archerBaseAttackBonus = 10;
 int knightBaseAttackBonus = 8;
 int archerClassBonus = 3;
 int knightClassBonus = 2;
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.addArea(new Area(new Position(1, 1), Arrays.asList(CharacterClass.ARCHER), CharacterClass.ARCHER));
 sector.addArea(new Area(new Position(2, 2), Arrays.asList(CharacterClass.KNIGHT), CharacterClass.KNIGHT));
 sector.addObserver(new SectorObserver() {
 @Override public void occupierChanged() {
 // do nothing
 }
 });
 assertEquals(archerBaseAttackBonus + archerClassBonus + 3, sector.getAttackBonus(archer));
 assertEquals(knightBaseAttackBonus + knightClassBonus + 2, sector.getAttackBonus(knight));
 }

 @Test
 public void testGetDefenseBonusMethod() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 int archerBaseDefenseBonus = 5;
 int knightBaseDefenseBonus = 7;
 int archerClassBonus = 2;
 int knightClassBonus = 4;
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.addArea(new Area(new Position(1, 1), Arrays.asList(CharacterClass.ARCHER), CharacterClass.ARCHER));
 sector.addArea(new Area(new Position(2, 2), Arrays.asList(CharacterClass.KNIGHT), CharacterClass.KNIGHT));
 sector.addObserver(new SectorObserver() {
 @Override public void occupierChanged() {
 // do nothing
 }
 });
 assertEquals(archerBaseDefenseBonus + archerClassBonus + 2, sector.getDefenseBonus(archer));
 assertEquals(knightBaseDefenseBonus + knightClassBonus + 4, sector.getDefenseBonus(knight));
 }

 @Test
 public void testClassBonusMethod() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 sector.gainControl(CharacterClass.ARCHER.getOppositeSide());
 sector.addObserver(new SectorObserver() {
 @Override public void occupierChanged() {
 // do nothing
 }
 });
 assertEquals(1, sector.classBonus(CharacterClass.ARCHER));
 assertEquals(0, sector.classBonus(CharacterClass.KNIGHT));
 }

 @Test
 public void testGetAreasMethod() {
 Set<Area> expectedAreas = new HashSet<Area>();
 expectedAreas.add(new Area(new Position(1, 1), Arrays.asList(CharacterClass.ARCHER, CharacterClass.ARCHER), CharacterClass.ARCHER));
 expectedAreas.add(new Area(new Position(2, 2), Arrays.asList(CharacterClass.KNIGHT, CharacterClass.KNIGHT), CharacterClass.KNIGHT));
 sector.gainControl(CombatantSide.DEFENDERS);
 sector._addArea(new Area(new Position(1, 1), Arrays.asList(CharacterClass.ARCHER, CharacterClass.ARCHER), CharacterClass.ARCHER));
 sector._addArea(new Area(new Position(2, 2), Arrays.asList(CharacterClass.KNIGHT, CharacterClass.KNIGHT), CharacterClass.KNIGHT));
 assertEquals(expectedAreas, sector.getAreas());
 }

 @Test
 public void testAddAreaMethod() {
 Set<Area> expectedAreas = new HashSet<Area>();
 expectedAreas.add(new Area(new Position(1, 1), Arrays.asList(CharacterClass.ARCHER, CharacterClass.ARCHER), CharacterClass.ARCHER));
 expectedAreas.add(new Area(new Position(2, 2), Arrays.asList(CharacterClass.KNIGHT, CharacterClass.KNIGHT), CharacterClass.KNIGHT));
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.addArea(new Area(new Position(1, 1), Arrays.asList(CharacterClass.ARCHER, CharacterClass.ARCHER), CharacterClass.ARCHER));
 sector.addArea(new Area(new Position(2, 2), Arrays.asList(CharacterClass.KNIGHT, CharacterClass.KNIGHT), CharacterClass.KNIGHT));
 assertEquals(expectedAreas, sector.getAreas());
 }

 @Test
 public void testHashCodeMethod() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 1, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 1, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 11, 5, false, 1, CharacterClass.ARCHER);
 Set<Area> area1 = new HashSet<Area>();
 area1.add(new Area(new Position(1, 1), Arrays.asList(CharacterClass.ARCHER, CharacterClass.ARCHER), CharacterClass.ARCHER));
 Set<Area> area2 = new HashSet<Area>();
 area2.add(new Area(new Position(2, 2), Arrays.asList(CharacterClass.KNIGHT, CharacterClass.KNIGHT), CharacterClass.KNIGHT));
 sector1.addObserver(new SectorObserver() {
 @Override public void occupierChanged() {
 // do nothing
 }
 });
 sector1._addArea(area1);
 sector2._addArea(area2);
 assertEquals(sector1.hashCode(), sector1.hashCode());
 assertNotEquals(sector1.hashCode(), sector2.hashCode());
 assertNotEquals(sector1.hashCode(), sector3.hashCode());
 }

 @Test
 public void testEqualsMethod() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 1, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 1, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 11, 5, false, 1, CharacterClass.ARCHER);
 Set<Area> area1 = new HashSet<Area>();
 area1.add(new Area(new Position(1, 1), Arrays.asList(CharacterClass.ARCHER, CharacterClass.ARCHER), CharacterClass.ARCHER));
 Set<Area> area2 = new HashSet<Area>();
 area2.add(new Area(new Position(2, 2), Arrays.asList(CharacterClass.KNIGHT, CharacterClass.KNIGHT), CharacterClass.KNIGHT));
 sector1.addObserver(new SectorObserver() {
 @Override public void occupierChanged() {
 // do nothing
 }
 });
 sector1._addArea(area1);
 sector2._addArea(area2);
 assertTrue(sector1.equals(sector1));
 assertFalse(sector1.equals(sector2));
 assertFalse(sector1.equals(sector3));
 }
}