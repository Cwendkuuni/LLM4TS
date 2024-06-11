package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.test.TestArea;
import de.outstare.fortbattleplayer.model.impl.test.TestCombatant;
import de.outstare.fortbattleplayer.model.impl.test.TestSectorObserver;

public class SimpleSectorTest {

 private SimpleSector sector;
 private TestCombatant combatant;
 private TestArea area;
 private TestSectorObserver observer;

 @Test
 public void testSimpleSectorConstructor() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertFalse(sector.isFlag());
 assertNull(sector.getOccupier());
 assertEquals(0, sector.getOccupierCount());
 assertNotNull(sector.getAreas());
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testFree() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.free();
 assertNull(sector.getOccupier());
 assertEquals(0, sector.getOccupierCount());
 }

 @Test(expected = AssertionError.class)
 public void testFreeWithCombatantLeft() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.free();
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.free();
 }

 @Test
 public void testOccupierChanged() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 sector.addObserver(observer = new TestSectorObserver());
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.gainControl(CombatantSide.ATTACKERS);
 assertEquals(2, sector.getOccupierCount());
 assertTrue(sector.isOccupied());
 assertSame(CombatantSide.ATTACKERS, sector.getOccupier());
 observer.assertOccupierChangedCalled();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGainControlWithNullSide() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 sector.gainControl(null);
 }

 @Test
 public void testGetOccupier() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 sector.addObserver(observer = new TestSectorObserver());
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.gainControl(CombatantSide.DEFENDERS);
 assertEquals(CombatantSide.DEFENDERS, sector.getOccupier());
 observer.assertOccupierChangedCalled();
 }

 @Test
 public void testIsOccupied() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 sector.addObserver(observer = new TestSectorObserver());
 sector.gainControl(CombatantSide.DEFENDERS);
 sector.gainControl(CombatantSide.DEFENDERS);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testGetHeight() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 assertEquals(5, sector.getHeight());
 }

 @Test
 public void testIsDefenderSpawn() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 assertTrue(sector.isDefenderSpawn());
 }

 @Test
 public void testIsAttackerSpawn() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 assertTrue(sector.isAttackerSpawn());
 }

 @Test
 public void testIsFlag() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 assertFalse(sector.isFlag());
 }

 @Test
 public void testGetBonus() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 sector.addArea(new TestArea(CombatantSide.DEFENDERS, combatant = new TestCombatant(CharacterClass.ARCHER, new Weapon("weapon", 5, 5))));
 assertEquals(new SectorBonus(5, 5, 5), sector.getBonus(CharacterClass.ARCHER));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetBonusWithNullCharacterClass() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 sector.getBonus(null);
 }

 @Test
 public void testGetAttackBonus() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 sector.addArea(new TestArea(CombatantSide.DEFENDERS, combatant = new TestCombatant(CharacterClass.ARCHER, new Weapon("weapon", 5, 5))));
 assertEquals(15, sector.getAttackBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDefenseBonus() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 sector.addArea(new TestArea(CombatantSide.DEFENDERS, combatant = new TestCombatant(CharacterClass.ARCHER, new Weapon("weapon", 5, 5))));
 assertEquals(10, sector.getDefenseBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDamageBonus() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 sector.addArea(new TestArea(CombatantSide.DEFENDERS, combatant = new TestCombatant(CharacterClass.ARCHER, new Weapon("weapon", 5, 5))));
 assertEquals(5, sector.getDamageBonus());
 }

 @Test
 public void testGetBonusFromWeapons() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 sector.addArea(new TestArea(CombatantSide.DEFENDERS, combatant = new TestCombatant(CharacterClass.ARCHER, new Weapon("weapon1", 0, 5))));
 sector.addArea(new TestArea(CombatantSide.DEFENDERS, combatant = new TestCombatant(CharacterClass.ARCHER, new Weapon("weapon2", 5, 5))));
 assertEquals(5, sector.getBonusFromWeapons(SectorBonus.BonusType.ATTACK));
 }

 @Test
 public void testClassBonus() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 assertEquals(20, sector.classBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testClassBonusWithWrongCharacterClass() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 assertEquals(0, sector.classBonus(CharacterClass.KNIGHT));
 }

 @Test
 public void testGetAreas() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 Set<Area> areas = new HashSet<Area>();
 areas.add(new TestArea(CombatantSide.DEFENDERS, combatant = new TestCombatant(CharacterClass.ARCHER, new Weapon("weapon", 5, 5))));
 areas.add(new TestArea(CombatantSide.ATTACKERS, new TestCombatant(CharacterClass.ARCHER, new Weapon("weapon", 5, 5))));
 sector.setAreas(areas);
 assertEquals(areas, sector.getAreas());
 }

 @Test
 public void testAddArea() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 Set<Area> areas = new HashSet<Area>();
 areas.add(new TestArea(CombatantSide.DEFENDERS, combatant = new TestCombatant(CharacterClass.ARCHER, new Weapon("weapon", 5, 5))));
 sector.setAreas(areas);
 sector.addArea(new TestArea(CombatantSide.ATTACKERS, new TestCombatant(CharacterClass.ARCHER, new Weapon("weapon", 5, 5))));
 assertTrue(sector.getAreas().contains(new TestArea(CombatantSide.ATTACKERS, combatant)));
 }

 @Test
 public void testHashCode() {
 Set<Area> areas = new HashSet<Area>(Arrays.asList(new TestArea(CombatantSide.DEFENDERS, new TestCombatant(CharacterClass.ARCHER, new Weapon("weapon", 5, 5))), new TestArea(CombatantSide.ATTACKERS, new TestCombatant(CharacterClass.ARCHER, new Weapon("weapon", 5, 5)))));
 SimpleSector sector1 = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 sector1.setAreas(areas);
 sector2.setAreas(areas);
 assertEquals(sector1.hashCode(), sector2.hashCode());
 }

 @Test
 public void testEquals() {
 Set<Area> areas = new HashSet<Area>(Arrays.asList(new TestArea(CombatantSide.DEFENDERS, new TestCombatant(CharacterClass.ARCHER, new Weapon("weapon", 5, 5))), new TestArea(CombatantSide.ATTACKERS, new TestCombatant(CharacterClass.ARCHER, new Weapon("weapon", 5, 5)))));
 SimpleSector sector1 = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 sector1.setAreas(areas);
 sector2.setAreas(areas);
 assertTrue(sector1.equals(sector2));
 }

 @Test
 public void testAddObserver() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 assertFalse(sector.hasObserver(observer));
 sector.addObserver(observer);
 assertTrue(sector.hasObserver(observer));
 }

 @Test
 public void testRemoveObserver() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 sector.addObserver(observer);
 assertTrue(sector.hasObserver(observer));
 sector.removeObserver(observer);
 assertFalse(sector.hasObserver(observer));
 }

 @Test
 public void testHasObserver() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 20, CharacterClass.ARCHER);
 assertFalse(sector.hasObserver(observer));
 sector.addObserver(observer);
 assertTrue(sector.hasObserver(observer));
 }
}