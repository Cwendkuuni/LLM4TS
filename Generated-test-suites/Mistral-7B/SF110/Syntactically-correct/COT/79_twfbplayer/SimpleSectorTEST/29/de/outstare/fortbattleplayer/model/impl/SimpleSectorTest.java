package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;
import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.*;
import org.junit.Test;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Test
 public void testSimpleSectorConstruction() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertFalse(sector.isOccupied());
 assertEquals(CharacterClass.ARCHER, sector.getClassType());
 }

 @Test
 public void testFreeMethod() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.ATTACKER);
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test(expected = AssertionError.class)
 public void testFreeMethodWithNegativeOccupierCount() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.ATTACKER);
 sector.free();
 sector.free(); // this should free the last combatant, but we force an AssertionError
 }

 @Test
 public void testOccupierChangedMethod() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 sector.addObserver(new SectorObserver() {
 @Override
 public void occupierChanged() {
 // this observer will be notified when the occupier changes
 }
 });
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(sector.isOccupied());
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGainControlMethod() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(sector.isOccupied());
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 sector.gainControl(CombatantSide.DEFENDER);
 assertFalse(sector.isOccupied());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGainControlMethodWithNullCombatantSide() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 sector.gainControl(null);
 }

 @Test
 public void testGetOccupierMethod() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 sector.free();
 assertNull(sector.getOccupier());
 }

 @Test
 public void testIsOccupiedMethod() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 assertFalse(sector.isOccupied());
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(sector.isOccupied());
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGetHeightMethod() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 assertEquals(5, sector.getHeight());
 }

 @Test
 public void testIsDefenderSpawnMethod() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 assertTrue(sector.isDefenderSpawn());
 }

 @Test
 public void testIsAttackerSpawnMethod() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 assertTrue(sector.isAttackerSpawn());
 }

 @Test
 public void testIsFlagMethod() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 assertFalse(sector.isFlag());
 }

 @Test
 public void testGetBonusMethod() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 CharacterClass archer = CharacterClass.ARCHER;
 SectorBonus expectedBonus = new SectorBonus(10, 5, 0);
 SectorBonus actualBonus = sector.getBonus(archer);
 assertEquals(expectedBonus.getAttackBonus(), actualBonus.getAttackBonus());
 assertEquals(expectedBonus.getDefenseBonus(), actualBonus.getDefenseBonus());
 assertEquals(expectedBonus.getDamageBonus(), actualBonus.getDamageBonus());
 }

 @Test
 public void testGetAttackBonusMethod() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 CharacterClass archer = CharacterClass.ARCHER;
 int expectedBonus = 13; // 10 (attackBonus) + 3 (classBonus)
 int actualBonus = sector.getAttackBonus(archer);
 assertEquals(expectedBonus, actualBonus);
 }

 @Test
 public void testGetDefenseBonusMethod() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 CharacterClass archer = CharacterClass.ARCHER;
 int expectedBonus = 8; // 5 (defendBonus) + 3 (classBonus)
 int actualBonus = sector.getDefenseBonus(archer);
 assertEquals(expectedBonus, actualBonus);
 }

 @Test
 public void testGetDamageBonusMethod() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 int expectedBonus = 0;
 int actualBonus = sector.getDamageBonus();
 assertEquals(expectedBonus, actualBonus);
 }

 @Test
 public void testGetAreasMethod() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 Set<Area> expectedAreas = new HashSet<>();
 Set<Area> actualAreas = sector.getAreas();
 assertEquals(expectedAreas, actualAreas);
 }

 @Test
 public void testAddAreaMethod() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 Area area = new Area();
 sector.addArea(area);
 Set<Area> expectedAreas = new HashSet<>();
 expectedAreas.add(area);
 Set<Area> actualAreas = sector.getAreas();
 assertEquals(expectedAreas, actualAreas);
 }

 @Test
 public void testHashCodeMethod() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 SimpleSector anotherSector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 int expectedHashCode = 1237 * 1231 * 5 + 10 + 1231 + 3 * 1231 + 5 + 1 + 1231 + 3 + 1231 + 5;
 int hashCode1 = sector.hashCode();
 int hashCode2 = anotherSector.hashCode();
 assertEquals(expectedHashCode, hashCode1);
 assertEquals(expectedHashCode, hashCode2);
 }

 @Test
 public void testEqualsMethod() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 SimpleSector anotherSector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 assertTrue(sector.equals(anotherSector));
 }

 @Test
 public void testEqualsMethodWithDifferentAreas() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 SimpleSector anotherSector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 Area area = new Area();
 sector.addArea(area);
 assertFalse(sector.equals(anotherSector));
 }

 @Test
 public void testEqualsMethodWithDifferentAttackBonus() {
 sector = new SimpleSector(5, true, true, 11, 5, true, 3, CharacterClass.ARCHER);
 SimpleSector anotherSector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 assertFalse(sector.equals(anotherSector));
 }

 @Test
 public void testEqualsMethodWithDifferentDefenderSpawn() {
 sector = new SimpleSector(5, false, true, 10, 5, true, 3, CharacterClass.ARCHER);
 SimpleSector anotherSector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 assertFalse(sector.equals(anotherSector));
 }

 @Test
 public void testEqualsMethodWithDifferentClassBonus() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 4, CharacterClass.ARCHER);
 SimpleSector anotherSector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 assertFalse(sector.equals(anotherSector));
 }

 @Test
 public void testEqualsMethodWithDifferentDefendBonus() {
 sector = new SimpleSector(5, true, true, 10, 6, true, 3, CharacterClass.ARCHER);
 SimpleSector anotherSector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 assertFalse(sector.equals(anotherSector));
 }

 @Test
 public void testEqualsMethodWithDifferentFlag() {
 sector = new SimpleSector(5, true, true, 10, 5, false, 3, CharacterClass.ARCHER);
 SimpleSector anotherSector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 assertFalse(sector.equals(anotherSector));
 }

 @Test
 public void testEqualsMethodWithDifferentHeight() {
 sector = new SimpleSector(6, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 SimpleSector anotherSector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.ARCHER);
 assertFalse(sector.equals(anotherSector));
 }

}