package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.test.DummyCharacterClass;
import de.outstare.fortbattleplayer.model.test.DummyCombatant;
import de.outstare.fortbattleplayer.model.test.DummySectorObserver;
import de.outstare.fortbattleplayer.model.test.DummyWeapon;
import de.outstare.fortbattleplayer.model.test.DummyArea;

public class SimpleSectorTest {

 private SimpleSector sector;
 private Set<Area> areas;
 private SectorObserver observer;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
 areas = new HashSet<Area>();
 observer = new DummySectorObserver();
 }

 @Test
 public void testSimpleSectorConstructor() {
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertFalse(sector.isFlag());
 assertEquals(20, sector.getClassBonus());
 assertSame(CharacterClass.ARCHER, sector.getClassType());
 assertEquals(0, sector.occupierCount);
 assertNotNull(sector.areas);
 assertNotNull(sector.observers);
 }

 @Test
 public void testFreeMethod() {
 sector.occupierCount = 2;
 sector.free();
 assertEquals(0, sector.occupierCount);
 assertNull(sector.occupier);
 }

 @Test(expected = AssertionError.class)
 public void testFreeMethodWithNegativeOccupierCount() {
 sector.free();
 }

 @Test
 public void testGainControlMethod() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(1, sector.occupierCount);
 assertSame(CombatantSide.DEFENDER, sector.getOccupier());
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(2, sector.occupierCount);
 assertSame(CombatantSide.ATTACKER, sector.getOccupier());
 }

 @Test
 public void testGetOccupierMethod() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
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
 CharacterClass charClass = new DummyCharacterClass();
 SectorBonus expectedBonus = new SectorBonus(15, 12, 5);
 SectorBonus actualBonus = sector.getBonus(charClass);
 assertEquals(expectedBonus.getAttackBonus(), actualBonus.getAttackBonus());
 assertEquals(expectedBonus.getDefenseBonus(), actualBonus.getDefenseBonus());
 assertEquals(expectedBonus.getDamageBonus(), actualBonus.getDamageBonus());
 }

 @Test
 public void testGetAttackBonusMethod() {
 CharacterClass archer = new DummyCharacterClass();
 CharacterClass mage = new DummyCharacterClass();
 int archerAttackBonus = 10 + 20;
 int mageAttackBonus = 10 + 20 + 5;
 assertEquals(archerAttackBonus, sector.getAttackBonus(archer));
 assertEquals(mageAttackBonus, sector.getAttackBonus(mage));
 }

 @Test
 public void testGetDefenseBonusMethod() {
 CharacterClass archer = new DummyCharacterClass();
 CharacterClass mage = new DummyCharacterClass();
 int archerDefenseBonus = 10 + 20;
 int mageDefenseBonus = 10 + 20 + 5;
 assertEquals(archerDefenseBonus, sector.getDefenseBonus(archer));
 assertEquals(mageDefenseBonus, sector.getDefenseBonus(mage));
 }

 @Test
 public void testClassBonusMethod() {
 CharacterClass archer = new DummyCharacterClass();
 CharacterClass mage = new DummyCharacterClass();
 assertEquals(20, sector.classBonus(archer));
 assertEquals(20, sector.classBonus(mage));
 }

 @Test
 public void testGetAreasMethod() {
 assertNotNull(sector.getAreas());
 }

 @Test
 public void testAddAreaMethod() {
 Area area = new DummyArea();
 sector._addArea(area);
 assertTrue(sector.areas.contains(area));
 }

 @Test(expected = NullPointerException.class)
 public void testAddAreaMethodWithNullArea() {
 sector._addArea(null);
 }

 @Test
 public void testHashCodeMethod() {
 SimpleSector simpleSector1 = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
 SimpleSector simpleSector2 = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
 SimpleSector simpleSector3 = new SimpleSector(6, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
 int expectedHashCode = 59735526;
 assertEquals(expectedHashCode, simpleSector1.hashCode());
 assertEquals(expectedHashCode, simpleSector2.hashCode());
 int differentHeightHashCode = 106346274;
 assertEquals(differentHeightHashCode, simpleSector1.hashCode());
 assertEquals(differentHeightHashCode, simpleSector3.hashCode());
 }

 @Test
 public void testEqualsMethod() {
 SimpleSector simpleSector1 = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
 SimpleSector simpleSector2 = new SimpleSector(5, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
 SimpleSector simpleSector3 = new SimpleSector(6, true, true, 10, 15, true, 20, CharacterClass.ARCHER);
 assertTrue(simpleSector1.equals(simpleSector2));
 assertFalse(simpleSector1.equals(simpleSector3));
 }

 @Test
 public void testAddObserverMethod() {
 sector.addObserver(observer);
 assertTrue(sector.observers.contains(observer));
 }

 @Test(expected = NullPointerException.class)
 public void testAddObserverMethodWithNullObserver() {
 sector.addObserver(null);
 }

 @Test
 public void testRemoveObserverMethod() {
 sector.addObserver(observer);
 sector.removeObserver(observer);
 assertFalse(sector.observers.contains(observer));
 }
}