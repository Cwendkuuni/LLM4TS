package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.BonusType;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.TestWeapon;
import de.outstare.fortbattleplayer.model.impl.TestWeaponType;
import de.outstare.fortbattleplayer.model.impl.TestCombatant;
import de.outstare.fortbattleplayer.model.impl.TestCombatantSide;

public class SimpleSectorTest {

 private SimpleSector sector;
 private Set<Area> areas;
 private Area area1, area2;
 private TestCombatant combatant1, combatant2;
 private TestCombatantSide side1, side2;
 private TestWeapon weapon1, weapon2;
 private TestWeaponType weaponType;
 private Set<SectorObserver> observers;

 @Before
 public void setUp() {
 sector = new SimpleSector(10, true, true, 5, 5, false, 1, CharacterClass.ARCHER);
 areas = new HashSet<Area>();
 area1 = new Area();
 area2 = new Area();
 combatant1 = new TestCombatant("Combatant 1");
 combatant2 = new TestCombatant("Combatant 2");
 side1 = new TestCombatantSide(CombatantSide.DEFENDER);
 side2 = new TestCombatantSide(CombatantSide.ATTACKER);
 weapon1 = new TestWeapon("Weapon 1", weaponType);
 weapon2 = new TestWeapon("Weapon 2", weaponType);
 weaponType = new TestWeaponType();
 }

 @Test
 public void testSimpleSectorConstruction() {
 assertEquals(10, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(5, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals(5, sector.getDefenseBonus(CharacterClass.ARCHER));
 assertEquals(0, sector.getDamageBonus());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEquals(0, sector.getOccupierCount());
 assertNotNull(sector.getAreas());
 assertEquals(0, sector.getAreas().size());
 assertFalse(sector.isFlag());
 assertEquals(CharacterClass.ARCHER, sector.getClassType());
 }

 @Test
 public void testFree() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 }

 @Test
 public void testGainControl() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(2, sector.getOccupierCount());
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testGetOccupier() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 assertNull(sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGetHeight() {
 assertEquals(10, sector.getHeight());
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
 SectorBonus bonus = sector.getBonus(CharacterClass.ARCHER);
 assertEquals(10, bonus.getAttackBonus());
 assertEquals(10, bonus.getDefenseBonus());
 assertEquals(0, bonus.getDamageBonus());
 }

 @Test
 public void testGetDamageBonus() {
 assertEquals(0, sector.getDamageBonus());
 }

 @Test
 public void testGetAttackBonus() {
 assertEquals(5, sector.getAttackBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDefenseBonus() {
 assertEquals(5, sector.getDefenseBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testClassBonus() {
 assertEquals(1, sector.classBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetAreas() {
 assertEquals(0, sector.getAreas().size());
 }

 @Test
 public void testAddArea() {
 sector.free();
 areas.add(area1);
 sector._addArea(area1);
 assertEquals(1, sector.getAreas().size());
 assertEquals(1, sector.getAreas().size());
 }

 @Test
 public void testHashCode() {
 Set<Area> testAreas = new HashSet<Area>(Arrays.asList(area1, area2));
 SimpleSector testSector = new SimpleSector(10, true, true, 5, 5, false, 1, CharacterClass.ARCHER);
 testSector._addArea(area1);
 testSector._addArea(area2);
 assertEquals(testSector.hashCode(), sector.hashCode());
 }

 @Test
 public void testEquals() {
 SimpleSector testSector = new SimpleSector(10, true, true, 5, 5, false, 1, CharacterClass.ARCHER);
 testSector._addArea(area1);
 testSector._addArea(area2);
 assertTrue(sector.equals(testSector));
 }

 @Test
 public void testAddObserver() {
 sector.addObserver(new TestSectorObserver());
 assertEquals(1, sector.getObservers().size());
 }

 @Test
 public void testRemoveObserver() {
 sector.addObserver(new TestSectorObserver());
 sector.removeObserver(new TestSectorObserver());
 assertEquals(0, sector.getObservers().size());
 }

 @Test
 public void testOccupierChanged() {
 CombatantSide previousOccupier = sector.getOccupier();
 observers.add(new TestSectorObserver());
 sector.gainControl(CombatantSide.DEFENDER);
 assertFalse(previousOccupier.equals(sector.getOccupier()));
 }
}

class TestSectorObserver implements SectorObserver {

 @Override
 public void occupierChanged() {
 // TODO Auto-generated method stub

 }
}