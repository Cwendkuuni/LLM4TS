package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SimpleArea;
import de.outstare.fortbattleplayer.model.impl.Weapon;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Test
 public void testSimpleSectorConstructor() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(2, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals(3, sector.getDefenseBonus(CharacterClass.ARCHER));
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEmpty(sector.getAreas());
 assertEmpty(sector.getObservers());
 }

 @Test
 public void testFreeMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 }

 @Test(expected = AssertionError.class)
 public void testFreeMethodNegativeOccupierCount() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 sector.free();
 }

 @Test
 public void testGainControlMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testGetOccupierMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testIsOccupiedMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 assertTrue(sector.isOccupied());
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGetHeightMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 assertEquals(5, sector.getHeight());
 }

 @Test
 public void testIsDefenderSpawnMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 assertTrue(sector.isDefenderSpawn());
 }

 @Test
 public void testIsAttackerSpawnMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 assertTrue(sector.isAttackerSpawn());
 }

 @Test
 public void testIsFlagMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 assertFalse(sector.isFlag());
 }

 @Test
 public void testGetBonusMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 CharacterClass charClass = CharacterClass.ARCHER;
 SectorBonus expected = new SectorBonus(2, 3, 0);
 SectorBonus actual = sector.getBonus(charClass);
 assertEquals(expected.getAttackBonus(), actual.getAttackBonus());
 assertEquals(expected.getDefenseBonus(), actual.getDefenseBonus());
 assertEquals(expected.getDamageBonus(), actual.getDamageBonus());
 }

 @Test
 public void testGetDamageBonusMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 int expected = 0;
 int actual = sector.getDamageBonus();
 assertEquals(expected, actual);
 }

 @Test
 public void testGetAttackBonusMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 int expected = 2;
 CharacterClass charClass = CharacterClass.ARCHER;
 int actual = sector.getAttackBonus(charClass);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetDefenseBonusMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 int expected = 3;
 CharacterClass charClass = CharacterClass.ARCHER;
 int actual = sector.getDefenseBonus(charClass);
 assertEquals(expected, actual);
 }

 @Test
 public void testClassBonusMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 int expected = 4;
 CharacterClass charClass = CharacterClass.ARCHER;
 int actual = sector.classBonus(charClass);
 assertEquals(expected, actual);
 }

 @Test
 public void testGetAreasMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 Set<Area> expected = Collections.emptySet();
 Set<Area> actual = sector.getAreas();
 assertEquals(expected, actual);
 }

 @Test
 public void testAddAreaMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 Area area = new SimpleArea(4, 4);
 sector.addArea(area);
 Set<Area> expected = new HashSet<>(Arrays.asList(area));
 Set<Area> actual = sector.getAreas();
 assertEquals(expected, actual);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAreaMethodUnmodifiableSet() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 Set<Area> unmodifiableSet = Collections.unmodifiableSet(sector.getAreas());
 Area area = new SimpleArea(4, 4);
 unmodifiableSet.add(area);
 }

 @Test
 public void testHashCodeMethod() {
 Set<Area> areas = new HashSet<>();
 areas.add(new SimpleArea(4, 4));
 SimpleSector sector1 = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER, areas);
 SimpleSector sector2 = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER, areas);
 assertEquals(sector1.hashCode(), sector2.hashCode());
 }

 @Test
 public void testEqualsMethod() {
 Set<Area> areas1 = new HashSet<>();
 areas1.add(new SimpleArea(4, 4));
 SimpleSector sector1 = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER, areas1);
 Set<Area> areas2 = new HashSet<>();
 areas2.add(new SimpleArea(4, 4));
 SimpleSector sector2 = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER, areas2);
 SimpleSector sector3 = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER, areas1);
 assertTrue(sector1.equals(sector3));
 assertFalse(sector1.equals(sector2));
 }

 @Test
 public void testAddObserverMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 SectorObserver observer = new SectorObserver() {
 @Override
 public void occupierChanged() {
 // empty
 }
 };
 sector.addObserver(observer);
 Set<SectorObserver> expected = new HashSet<>(Collections.singleton(observer));
 Set<SectorObserver> actual = sector.getObservers();
 assertEquals(expected, actual);
 }

 @Test
 public void testRemoveObserverMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 SectorObserver observer = new SectorObserver() {
 @Override
 public void occupierChanged() {
 // empty
 }
 };
 sector.addObserver(observer);
 sector.removeObserver(observer);
 Set<SectorObserver> expected = Collections.emptySet();
 Set<SectorObserver> actual = sector.getObservers();
 assertEquals(expected, actual);
 }
}