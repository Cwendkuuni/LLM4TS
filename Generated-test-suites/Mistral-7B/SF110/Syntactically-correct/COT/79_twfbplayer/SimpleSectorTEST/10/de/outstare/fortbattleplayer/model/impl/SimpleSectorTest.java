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
import de.outstare.fortbattleplayer.model.impl.SectorObserver;
import de.outstare.fortbattleplayer.model.impl.test.TestHelper;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Test
 public void testSimpleSectorConstruction() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(2, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals(3, sector.getDefenseBonus(CharacterClass.ARCHER));
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEmpty(sector.getAreas());
 }

 @Test
 public void testFree() {
 sector = TestHelper.createSectorWithOccupants(2, CharacterClass.ARCHER, true);
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 }

 @Test(expected = AssertionError.class)
 public void testFreeNegativeOccupierCount() {
 sector = TestHelper.createSectorWithOccupants(2, CharacterClass.ARCHER, true);
 sector.free();
 sector.free(); // This should free the last combatant
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 sector.free(); // This should throw an AssertionError
 }

 @Test
 public void testOccupierChanged() {
 sector = TestHelper.createSectorWithOccupants(2, CharacterClass.ARCHER, true);
 Set<SectorObserver> observers = new HashSet<>();
 sector.addObserver(observers);
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.getOccupier() == CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 assertTrue(observers.size() == 1);
 sector.free();
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertTrue(observers.isEmpty());
 }

 @Test
 public void testGainControl() {
 sector = TestHelper.createSectorWithOccupants(2, CharacterClass.ARCHER, true);
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.getOccupier() == CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(sector.getOccupier() == CombatantSide.ATTACKER);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testGetOccupier() {
 sector = TestHelper.createSectorWithOccupants(2, CharacterClass.ARCHER, true);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 sector = TestHelper.createSectorWithOccupants(2, CharacterClass.ARCHER, true);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testGetHeight() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 assertEquals(5, sector.getHeight());
 }

 @Test
 public void testIsDefenderSpawn() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 assertTrue(sector.isDefenderSpawn());
 }

 @Test
 public void testIsAttackerSpawn() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 assertTrue(sector.isAttackerSpawn());
 }

 @Test
 public void testIsFlag() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 assertFalse(sector.isFlag());
 }

 @Test
 public void testGetBonus() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 SectorBonus bonus = sector.getBonus(CharacterClass.ARCHER);
 assertEquals(new SectorBonus(4, 4, 0), bonus);
 }

 @Test
 public void testGetDamageBonus() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 assertEquals(0, sector.getDamageBonus());
 }

 @Test
 public void testGetAttackBonus() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 assertEquals(4, sector.getAttackBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDefenseBonus() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 assertEquals(4, sector.getDefenseBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testClassBonus() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 assertEquals(5, sector.classBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetAreas() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 assertEquals(new HashSet<Area>(), sector.getAreas());
 }

 @Test
 public void testAddArea() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 Area area = TestHelper.createAreaWithCombatant(CharacterClass.ARCHER, true);
 sector._addArea(area);
 assertEquals(Arrays.asList(area), sector.getAreas());
 }

 @Test
 public void testHashCode() {
 Set<Area> areas = new HashSet<>();
 areas.add(TestHelper.createAreaWithCombatant(CharacterClass.ARCHER, true));
 SimpleSector sector1 = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 sector4.addObserver(TestHelper.createDummySectorObserver());
 SimpleSector sector5 = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 sector5._addArea(TestHelper.createAreaWithCombatant(CharacterClass.ARCHER, true));
 assertEquals(sector1.hashCode(), sector2.hashCode());
 assertEquals(sector1.hashCode(), sector3.hashCode());
 assertEquals(sector1.hashCode(), sector4.hashCode());
 assertNotEquals(sector1.hashCode(), sector5.hashCode());
 }

 @Test
 public void testEquals() {
 Set<Area> areas = new HashSet<>();
 areas.add(TestHelper.createAreaWithCombatant(CharacterClass.ARCHER, true));
 SimpleSector sector1 = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 SimpleSector sector5 = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 sector5._addArea(TestHelper.createAreaWithCombatant(CharacterClass.ARCHER, true));
 assertTrue(sector1.equals(sector2));
 assertTrue(sector1.equals(sector3));
 assertTrue(sector1.equals(sector4));
 assertEquals(CharacterClass.ARCHER, sector1.getClassType());
 assertFalse(sector1.equals(sector5));
 }

 @Test
 public void testAddObserver() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 SectorObserver observer = TestHelper.createDummySectorObserver();
 sector.addObserver(observer);
 assertEquals(1, sector.getObservers().size());
 }

 @Test
 public void testRemoveObserver() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 5, CharacterClass.ARCHER);
 SectorObserver observer = TestHelper.createDummySectorObserver();
 sector.addObserver(observer);
 sector.removeObserver(observer);
 assertEquals(0, sector.getObservers().size());
 }
}