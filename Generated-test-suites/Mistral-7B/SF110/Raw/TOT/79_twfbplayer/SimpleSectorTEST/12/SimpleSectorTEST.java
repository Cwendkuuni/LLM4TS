package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.*;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Before
 public void setUp() {
 sector = new SimpleSector(3, true, true, 2, 1, false, 3, CharacterClass.ARCHER);
 }

 @Test
 public void testFree() {
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 }

 @Test(expected = AssertionError.class)
 public void testFreeWhenOccupied() {
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.free();
 assertNotNull(sector.getOccupier());
 }

 @Test
 public void testGainControl() {
 sector.gainControl(CombatantSide.DEFENDERS);
 assertEquals(1, sector.getOccupierCount());
 assertSame(CombatantSide.DEFENDERS, sector.getOccupier());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGainControlWhenAlreadyOccupiedBySameSide() {
 sector.gainControl(CombatantSide.DEFENDERS);
 try {
 sector.gainControl(CombatantSide.DEFENDERS);
 } catch (IllegalArgumentException e) {
 assertEquals("Sector is already occupied by this side", e.getMessage());
 }
 }

 @Test
 public void testGetOccupier() {
 sector.gainControl(CombatantSide.DEFENDERS);
 assertEquals(CombatantSide.DEFENDERS, sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 sector.gainControl(CombatantSide.DEFENDERS);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testGetHeight() {
 assertEquals(3, sector.getHeight());
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
 SectorBonus expectedBonus = new SectorBonus(4, 2, 5);
 SectorBonus bonus = sector.getBonus(archer);
 assertEquals(expectedBonus, bonus);
 bonus = sector.getBonus(knight);
 assertEquals(new SectorBonus(2, 1, 3), bonus);
 }

 @Test
 public void testGetDamageBonus() {
 assertEquals(5, sector.getDamageBonus());
 }

 @Test
 public void testGetAttackBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 int expectedAttackBonusArchery = 4;
 int expectedAttackBonusKnight = 2;
 int attackBonusArchery = sector.getAttackBonus(archer);
 assertEquals(expectedAttackBonusArchery, attackBonusArchery);
 int attackBonusKnight = sector.getAttackBonus(knight);
 assertEquals(expectedAttackBonusKnight, attackBonusKnight);
 }

 @Test
 public void testGetDefenseBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 int expectedDefenseBonusArchery = 2;
 int expectedDefenseBonusKnight = 1;
 int defenseBonusArchery = sector.getDefenseBonus(archer);
 assertEquals(expectedDefenseBonusArchery, defenseBonusArchery);
 int defenseBonusKnight = sector.getDefenseBonus(knight);
 assertEquals(expectedDefenseBonusKnight, defenseBonusKnight);
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
 Set<Area> expectedAreas = new HashSet<Area>(Arrays.asList(new Area(), new Area()));
 Set<Area> areas = sector.getAreas();
 assertEquals(expectedAreas, areas);
 }

 @Test
 public void testAddArea() {
 Set<Area> expectedAreas = new HashSet<Area>(Arrays.asList(new Area(), new Area(), new Area()));
 sector._addArea(new Area());
 assertEquals(expectedAreas, sector.getAreas());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAreaWhenAreasSetIsFinal() {
 SimpleSector sectorWithFinalAreas = new SimpleSector(3, true, true, 2, 1, false, 3, CharacterClass.ARCHER) {
 private final Set<Area> areas = Collections.unmodifiableSet(Arrays.asList(new Area()));
 };
 try {
 sectorWithFinalAreas._addArea(new Area());
 } catch (UnsupportedOperationException e) {
 //expected
 }
 }

 @Test
 public void testHashCode() {
 SimpleSector sector1 = new SimpleSector(3, true, true, 2, 1, false, 3, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(3, true, true, 2, 1, false, 3, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(3, true, true, 2, 1, false, 3, CharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(3, true, true, 3, 1, false, 3, CharacterClass.ARCHER);
 SimpleSector sector5 = new SimpleSector(4, true, true, 2, 1, false, 3, CharacterClass.ARCHER);
 Set<Area> areas = new HashSet<Area>(Arrays.asList(new Area(), new Area()));
 SimpleSector sector6 = new SimpleSector(3, true, true, 2, 1, false, 3, CharacterClass.ARCHER) {
 private final Set<Area> areas = areas;
 };
 int hashCode1 = sector1.hashCode();
 int hashCode2 = sector2.hashCode();
 int hashCode3 = sector3.hashCode();
 int hashCode4 = sector4.hashCode();
 int hashCode5 = sector5.hashCode();
 int hashCode6 = sector6.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertEquals(hashCode1, hashCode3);
 assertEquals(hashCode1, hashCode6);
 assertNotEquals(hashCode1, hashCode4);
 assertNotEquals(hashCode1, hashCode5);
 }

 @Test
 public void testEquals() {
 SimpleSector sector1 = new SimpleSector(3, true, true, 2, 1, false, 3, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(3, true, true, 2, 1, false, 3, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(3, true, true, 2, 1, false, 3, CharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(3, true, true, 3, 1, false, 3, CharacterClass.ARCHER);
 SimpleSector sector5 = new SimpleSector(4, true, true, 2, 1, false, 3, CharacterClass.ARCHER);
 Set<Area> areas = new HashSet<Area>(Arrays.asList(new Area(), new Area()));
 SimpleSector sector6 = new SimpleSector(3, true, true, 2, 1, false, 3, CharacterClass.ARCHER) {
 private final Set<Area> areas = areas;
 };
 assertTrue(sector1.equals(sector2));
 assertTrue(sector1.equals(sector3));
 assertTrue(sector1.equals(sector6));
 assertFalse(sector1.equals(sector4));
 assertFalse(sector1.equals(sector5));
 }

 @After
 public void tearDown() {
 sector = null;
 }
}