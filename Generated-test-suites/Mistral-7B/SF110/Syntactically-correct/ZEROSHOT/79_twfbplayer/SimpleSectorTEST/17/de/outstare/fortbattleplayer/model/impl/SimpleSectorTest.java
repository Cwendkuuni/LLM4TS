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
 public void setUp() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 10, CharacterClass.ARCHER);
 }

 @Test
 public void testSimpleSectorConstruction() {
 // test constructor with given parameters
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(5, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals(5, sector.getDefenseBonus(CharacterClass.ARCHER));
 assertFalse(sector.isFlag());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEquals(0, sector.getOccupierCount());
 assertTrue(sector.areas.isEmpty());
 assertTrue(sector.observers.isEmpty());
 }

 @Test
 public void testFreeMethod() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEquals(0, sector.getOccupierCount());
 }

 @Test
 public void testGainControlMethod() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertSame(CombatantSide.DEFENDER, sector.getOccupier());
 assertTrue(sector.isOccupied());
 assertEquals(1, sector.getOccupierCount());
 }

 @Test
 public void testGetOccupierMethod() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertSame(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testIsOccupiedMethod() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 sector.free();
 assertFalse(sector.isOccupied());
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
 SectorBonus expectedArchBonus = new SectorBonus(10, 10, 5);
 SectorBonus expectedKnightBonus = new SectorBonus(15, 15, 10);
 assertEquals(expectedArchBonus, sector.getBonus(archer));
 assertEquals(expectedKnightBonus, sector.getBonus(knight));
 }

 @Test
 public void testGetAttackBonusMethod() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 int archerBonus = 10;
 int knightBonus = 15;
 assertEquals(archerBonus + 10, sector.getAttackBonus(archer));
 assertEquals(knightBonus + 15, sector.getAttackBonus(knight));
 }

 @Test
 public void testGetDefenseBonusMethod() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 int archerBonus = 10;
 int knightBonus = 15;
 assertEquals(archerBonus + 10, sector.getDefenseBonus(archer));
 assertEquals(knightBonus + 15, sector.getDefenseBonus(knight));
 }

 @Test
 public void testClassBonusMethod() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 assertEquals(10, sector.classBonus(archer));
 assertEquals(10, sector.classBonus(knight));
 }

 @Test
 public void testGetAreasMethod() {
 Set<Area> expectedAreas = new HashSet<>(Arrays.asList(new Area(), new Area(), new Area()));
 sector._addArea(new Area());
 sector._addArea(new Area());
 sector._addArea(new Area());
 assertEquals(expectedAreas, sector.getAreas());
 }

 @Test
 public void testAddObserverMethod() {
 SectorObserver observer = mock(SectorObserver.class);
 sector.addObserver(observer);
 assertTrue(sector.observers.contains(observer));
 }

 @Test
 public void testRemoveObserverMethod() {
 SectorObserver observer = mock(SectorObserver.class);
 sector.addObserver(observer);
 sector.removeObserver(observer);
 assertFalse(sector.observers.contains(observer));
 }
}