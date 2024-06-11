package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.Weapon;
import de.outstare.fortbattleplayer.model.observer.SectorObserver;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Before
 public void setUp() {
 sector = new SimpleSector(1, true, true, 1, 1, true, 1, CharacterClass.ARCHER);
 }

 @Test
 public void testConstructors() {
 // Test constructor with valid arguments
 SimpleSector sector = new SimpleSector(1, true, true, 1, 1, true, 1, CharacterClass.ARCHER);
 // Test constructor with null arguments
 assertThrows(NullPointerException.class, () -> new SimpleSector(null, true, true, 1, 1, true, 1, CharacterClass.ARCHER));
 // Test constructor with invalid height
 assertThrows(IllegalArgumentException.class, () -> new SimpleSector(-1, true, true, 1, 1, true, 1, CharacterClass.ARCHER));
 }

 @Test
 public void testFree() {
 // Test freeing an empty sector
 sector.free();
 // Test freeing a sector with combatants
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 sector.free();
 // Test freeing a sector with negative occupier count
 sector.free();
 // Test freeing a sector with occupierCount less than 0
 sector.free();
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGainControl() {
 // Test gaining control of an empty sector
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 // Test gaining control of a sector that already has an occupier
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 // Test gaining control of the same side twice
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testGetOccupier() {
 // Test getting the occupier of an unoccupied sector
 assertNull(sector.getOccupier());
 // Test getting the occupier of an occupied sector
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 // Test checking an unoccupied sector
 assertFalse(sector.isOccupied());
 // Test checking an occupied sector
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testGetHeight() {
 assertEquals(1, sector.getHeight());
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
 assertTrue(sector.isFlag());
 }

 @Test
 public void testGetBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 SectorBonus expected = new SectorBonus(1, 1, 0);
 SectorBonus result = sector.getBonus(charClass);
 assertEquals(expected, result);
 }

 @Test
 public void testGetAttackBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 int expected = 2;
 int result = sector.getAttackBonus(charClass);
 assertEquals(expected, result);
 }

 @Test
 public void testGetDefenseBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 int expected = 2;
 int result = sector.getDefenseBonus(charClass);
 assertEquals(expected, result);
 }

 @Test
 public void testClassBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 int expected = 1;
 int result = sector.classBonus(charClass);
 assertEquals(expected, result);
 }

 @Test
 public void testGetAreas() {
 Set<Area> expected = new HashSet<>();
 Set<Area> result = sector.getAreas();
 assertEquals(expected, result);
 }

 @Test
 public void testAddArea() {
 Area area = new Area();
 sector.addArea(area);
 Set<Area> expected = new HashSet<>(Arrays.asList(area));
 Set<Area> result = sector.getAreas();
 assertEquals(expected, result);
 }

 @Test
 public void testHashCode() {
 SimpleSector sector1 = new SimpleSector(1, true, true, 1, 1, true, 1, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(1, true, true, 1, 1, true, 1, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(2, true, true, 1, 1, true, 1, CharacterClass.ARCHER);
 assertEquals(sector1.hashCode(), sector2.hashCode());
 assertNotEquals(sector1.hashCode(), sector3.hashCode());
 }

 @Test
 public void testEquals() {
 SimpleSector sector1 = new SimpleSector(1, true, true, 1, 1, true, 1, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(1, true, true, 1, 1, true, 1, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(2, true, true, 1, 1, true, 1, CharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(1, true, true, 1, 1, true, 1, CharacterClass.ARCHER);
 assertTrue(sector1.equals(sector2));
 assertFalse(sector1.equals(sector3));
 assertFalse(sector1.equals(sector4));
 SimpleSector sector5 = new SimpleSector(1, true, true, 1, 1, true, 1, CharacterClass.ARCHER);
 sector5.addObserver(new SectorObserver() {
 @Override public void occupierChanged() {
 }
 });
 assertFalse(sector1.equals(sector5));
 }

 @Test (expected = UnsupportedOperationException.class)
 public void testRemoveObserver() {
 sector.removeObserver(null);
 }
}