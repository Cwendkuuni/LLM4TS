package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SimpleSectorObserver;
import de.outstare.fortbattleplayer.model.impl.test.TestUtils;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Test
 public void testSimpleSectorCreation() {
 sector = new SimpleSector(10, true, true, 5, 3, true, 5, CharacterClass.ARCHER);
 assertEquals(10, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testFree() {
 sector = TestUtils.createTestSector();
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test(expected = AssertionError.class)
 public void testFreeWhenNoCombatants() {
 sector = TestUtils.createTestSector();
 sector.free();
 }

 @Test
 public void testOccupierChanged() {
 sector = TestUtils.createTestSector();
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 assertNull(sector.getOccupier());
 sector.occupierChanged();
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 }

 @Test
 public void testGainControl() {
 sector = TestUtils.createTestSector();
 sector.gainControl(CombatantSide.DEFENDER);
 assertFalse(sector.isOccupied());
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(sector.isOccupied());
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGainControlWhenSameSide() {
 sector = TestUtils.createTestSector();
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 }

 @Test
 public void testGetOccupier() {
 sector = TestUtils.createTestSector();
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 sector = TestUtils.createTestSector();
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testGetBonus() {
 sector = TestUtils.createTestSector();
 sector._addArea(TestUtils.createTestAreaWithCombatant(new Combatant("Test", CharacterClass.ARCHER, new Weapon("TestWeapon", 5, 3, null))));
 sector._addArea(TestUtils.createTestAreaWithCombatant(new Combatant("Test2", CharacterClass.ARCHER, new Weapon("TestWeapon2", 4, 4, null))));
 SectorBonus expectedBonus = new SectorBonus(13, 11, 1);
 SectorBonus actualBonus = sector.getBonus(CharacterClass.ARCHER);
 assertEquals(expectedBonus, actualBonus);
 }

 @Test
 public void testGetAttackBonus() {
 sector = TestUtils.createTestSector();
 sector._addArea(TestUtils.createTestAreaWithCombatant(new Combatant("Test", CharacterClass.ARCHER, new Weapon("TestWeapon", 5, 3, null))));
 sector._addArea(TestUtils.createTestAreaWithCombatant(new Combatant("Test2", CharacterClass.ARCHER, new Weapon("TestWeapon2", 4, 4, null))));
 sector._addArea(TestUtils.createTestAreaWithCombatant(new Combatant("Test3", CharacterClass.ARCHER, new Weapon("TestWeapon3", 3, 3, null))));
 int expectedAttackBonus = 11;
 int actualAttackBonus = sector.getAttackBonus(CharacterClass.ARCHER);
 assertEquals(expectedAttackBonus, actualAttackBonus);
 }

 @Test
 public void testGetDefenseBonus() {
 sector = TestUtils.createTestSector();
 sector._addArea(TestUtils.createTestAreaWithCombatant(new Combatant("Test", CharacterClass.ARCHER, new Weapon("TestWeapon", 5, 3, null))));
 sector._addArea(TestUtils.createTestAreaWithCombatant(new Combatant("Test2", CharacterClass.ARCHER, new Weapon("TestWeapon2", 4, 4, null))));
 sector._addArea(TestUtils.createTestAreaWithCombatant(new Combatant("Test3", CharacterClass.ARCHER, new Weapon("TestWeapon3", 3, 3, null))));
 int expectedDefenseBonus = 11;
 int actualDefenseBonus = sector.getDefenseBonus(CharacterClass.ARCHER);
 assertEquals(expectedDefenseBonus, actualDefenseBonus);
 }

 @Test
 public void testClassBonus() {
 sector = TestUtils.createTestSector();
 sector._addArea(TestUtils.createTestAreaWithCombatant(new Combatant("Test", CharacterClass.ARCHER, new Weapon("TestWeapon", 5, 3, null))));
 sector._addArea(TestUtils.createTestAreaWithCombatant(new Combatant("Test2", CharacterClass.ARCHER, new Weapon("TestWeapon2", 4, 4, null))));
 sector._addArea(TestUtils.createTestAreaWithCombatant(new Combatant("Test3", CharacterClass.ARCHER, new Weapon("TestWeapon3", 3, 3, null))));
 sector._addArea(TestUtils.createTestAreaWithCombatant(new Combatant("Test4", CharacterClass.KNIGHT, new Weapon("TestWeapon4", 5, 3, null))));
 int expectedClassBonus = 5;
 int actualClassBonus = sector.classBonus(CharacterClass.ARCHER);
 assertEquals(expectedClassBonus, actualClassBonus);
 int actualClassBonus2 = sector.classBonus(CharacterClass.KNIGHT);
 assertEquals(0, actualClassBonus2);
 }

 @Test
 public void testGetAreas() {
 sector = TestUtils.createTestSector();
 Set<Area> expectedAreas = new HashSet<>(Arrays.asList(TestUtils.createTestArea(), TestUtils.createTestArea()));
 Set<Area> actualAreas = sector.getAreas();
 assertEquals(expectedAreas, actualAreas);
 }

 @Test
 public void testAddArea() {
 sector = TestUtils.createTestSector();
 Area area = TestUtils.createTestArea();
 sector._addArea(area);
 Set<Area> expectedAreas = new HashSet<>(Arrays.asList(area));
 Set<Area> actualAreas = sector.getAreas();
 assertEquals(expectedAreas, actualAreas);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAreaWhenNull() {
 sector = TestUtils.createTestSector();
 sector._addArea(null);
 }

 @Test
 public void testHashCode() {
 sector = TestUtils.createTestSector();
 SimpleSector other = new SimpleSector(10, true, true, 5, 3, true, 5, CharacterClass.ARCHER);
 assertEquals(sector.hashCode(), other.hashCode());
 sector.getAreas().add(TestUtils.createTestArea());
 other.getAreas().add(TestUtils.createTestArea());
 assertNotEquals(sector.hashCode(), other.hashCode());
 }

 @Test
 public void testEquals() {
 sector = TestUtils.createTestSector();
 SimpleSector other = new SimpleSector(10, true, true, 5, 3, true, 5, CharacterClass.ARCHER);
 assertTrue(sector.equals(other));
 sector.getAreas().add(TestUtils.createTestArea());
 other.getAreas().add(TestUtils.createTestArea());
 assertFalse(sector.equals(other));
 }

 @Test
 public void testAddObserver() {
 sector = TestUtils.createTestSector();
 SimpleSectorObserver observer = new SimpleSectorObserver();
 sector.addObserver(observer);
 observer.occupierChangedCalled.notify();
 assertTrue(observer.occupierChangedCalled.get());
 }

 @Test
 public void testRemoveObserver() {
 sector = TestUtils.createTestSector();
 SimpleSectorObserver observer = new SimpleSectorObserver();
 sector.addObserver(observer);
 sector.removeObserver(observer);
 assertFalse(observer.occupierChangedCalled.get());
 }
}