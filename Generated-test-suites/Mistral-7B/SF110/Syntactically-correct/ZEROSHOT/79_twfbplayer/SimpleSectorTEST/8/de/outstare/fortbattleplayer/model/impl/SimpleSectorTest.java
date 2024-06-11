package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SimpleWeapon;
import de.outstare.fortbattleplayer.model.impl.SimpleCharacterClass;
import de.outstare.fortbattleplayer.model.impl.SimpleArea;
import de.outstare.fortbattleplayer.model.impl.SimpleCombatant;
import de.outstare.fortbattleplayer.model.impl.SimpleSectorObserver;

public class SimpleSectorTest {

 private SimpleSector sector;
 private Set<Area> areas;
 private Area area1;
 private Area area2;
 private SimpleCharacterClass charClass;
 private SimpleCombatant combatant1;
 private SimpleCombatant combatant2;
 private SimpleWeapon weapon;
 private SimpleSectorObserver observer;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 10, 5, true, 1, SimpleCharacterClass.ARCHER);
 areas = new HashSet<>();
 area1 = new SimpleArea(1, 1);
 area2 = new SimpleArea(1, 2);
 charClass = new SimpleCharacterClass(SimpleCharacterClass.ARCHER);
 combatant1 = new SimpleCombatant(charClass, new SimpleName("Combatant1"), weapon);
 combatant2 = new SimpleCombatant(charClass, new SimpleName("Combatant2"), weapon);
 weapon = new SimpleWeapon();
 observer = new SimpleSectorObserver() {
 @Override
 public void occupierChanged() {
 // Empty
 }
 };
 sector.addObserver(observer);
 }

 @Test
 public void testSimpleSectorConstructor() {
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertFalse(sector.isOccupied());
 assertNull(sector.getOccupier());
 assertFalse(sector.isFlag());
 assertEquals(1, sector.getClassBonus());
 assertEquals(SimpleCharacterClass.ARCHER, sector.getClassType());
 assertNotNull(sector.getAreas());
 assertFalse(sector.observers.isEmpty());
 }

 @Test
 public void testFree() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertFalse(sector.isOccupied());
 assertNull(sector.getOccupier());
 }

 @Test
 public void testOccupierChanged() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 sector.free();
 observer.occupierChanged();
 assertTrue(observer.isOccupierChangedCalled);
 }

 @Test
 public void testGainControl() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 assertTrue(sector.isOccupied());
 }

 @Test(expected = AssertionError.class)
 public void testGainControlWrongSide() {
 sector.free();
 sector.gainControl(CombatantSide.ATTACKER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 sector.gainControl(CombatantSide.DEFENDER); // Should throw an AssertionError
 }

 @Test
 public void testGetOccupier() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 sector.free();
 assertFalse(sector.isOccupied());
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
 SimpleSectorTest sectorTest = new SimpleSectorTest();
 SimpleSector sector2 = sectorTest.new SimpleSector(5, true, true, 10, 5, true, 1, SimpleCharacterClass.ARCHER);
 int attackBonus = sector2.getAttackBonus(charClass);
 int defenseBonus = sector2.getDefenseBonus(charClass);
 int damageBonus = sector2.getDamageBonus();
 SectorBonus sectorBonus = sector2.getBonus(charClass);
 assertEquals(10, attackBonus);
 assertEquals(5, defenseBonus);
 assertEquals(0, damageBonus);
 assertEquals(new SectorBonus(10, 5, 0), sectorBonus);
 }

 @Test
 public void testGetAttackBonus() {
 int attackBonus = sector.getAttackBonus(charClass);
 int classBonus = sector.classBonus(charClass);
 int weaponBonus = sector.getBonusFromWeapons(SectorBonus.BonusType.ATTACK);
 int expectedAttackBonus = 10 + 1 + weaponBonus;
 assertEquals(expectedAttackBonus, attackBonus);
 }

 @Test
 public void testGetDefenseBonus() {
 int defenseBonus = sector.getDefenseBonus(charClass);
 int classBonus = sector.classBonus(charClass);
 int weaponBonus = sector.getBonusFromWeapons(SectorBonus.BonusType.DEFENSE);
 int expectedDefenseBonus = 5 + 1 + weaponBonus;
 assertEquals(expectedDefenseBonus, defenseBonus);
 }

 @Test
 public void testClassBonus() {
 int classBonus = sector.classBonus(charClass);
 int expectedClassBonus = 1;
 assertEquals(expectedClassBonus, classBonus);
 }

 @Test
 public void testGetAreas() {
 assertEquals(0, sector.getAreas().size());
 sector._addArea(area1);
 sector._addArea(area2);
 assertEquals(2, sector.getAreas().size());
 }

 @Test
 public void testHashCode() {
 int expectedHashCode = 31 * 31 * 1231 * 1 + 5 + 1231 + 1 + charClass.hashCode() + 5 + 1231 + 1 + 1231 + 1 + 1 + 1 + 1;
 assertEquals(expectedHashCode, sector.hashCode());
 }

 @Test
 public void testEquals() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, true, 1, SimpleCharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 10, 5, true, 1, SimpleCharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(5, false, true, 10, 5, true, 1, SimpleCharacterClass.ARCHER);
 assertTrue(sector.equals(sector));
 assertTrue(sector.equals(sector2));
 assertFalse(sector.equals(sector3));
 assertFalse(sector.equals(sector4));
 }

 @Test
 public void testAddObserver() {
 sector.addObserver(observer);
 assertTrue(sector.observers.contains(observer));
 }

 @Test
 public void testRemoveObserver() {
 sector.addObserver(observer);
 sector.removeObserver(observer);
 assertFalse(sector.observers.contains(observer));
 }
}