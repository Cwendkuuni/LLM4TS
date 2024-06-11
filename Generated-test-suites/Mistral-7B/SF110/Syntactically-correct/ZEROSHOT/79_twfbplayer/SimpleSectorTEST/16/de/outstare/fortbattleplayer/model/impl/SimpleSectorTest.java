package de.outstare.fortbattleplayer.model.impl;

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
import de.outstare.fortbattleplayer.model.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.WeaponStub;
import de.outstare.fortbattleplayer.model.impl.AreaStub;
import de.outstare.fortbattleplayer.model.impl.SectorObserverStub;

public class SimpleSectorTest {

 private SimpleSector sector;
 private Set<Area> areas;
 private Area area1;
 private Area area2;
 private Set<SectorObserver> observers;
 private SectorObserver observer1;
 private SectorObserver observer2;

 @Before
 public void setup() {
 sector = new SimpleSector(5, true, true, 2, 3, false, 5, CharacterClass.ARCHER);
 areas = new HashSet<>();
 area1 = new AreaStub();
 area2 = new AreaStub();
 observers = new HashSet<>();
 observer1 = new SectorObserverStub();
 observer2 = new SectorObserverStub();
 sector.addObserver(observer1);
 sector.addObserver(observer2);
 sector._addArea(area1);
 sector._addArea(area2);
 }

 @Test
 public void testConstructor() {
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertFalse(sector.isFlag());
 assertEquals(5, sector.getClassBonus());
 assertEquals(CharacterClass.ARCHER, sector.getClassType());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEquals(0, sector.getOccupierCount());
 assertEquals(0, sector.hashCode());
 assertFalse(sector.equals(null));
 assertFalse(sector.equals(new Object()));
 }

 @Test
 public void testFree() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testOccupierChanged() {
 int observerChangeCount = 0;
 sector.addObserver((observer) -> observerChangeCount++);
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(1, observerChangeCount);
 }

 @Test
 public void testGainControl() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 assertTrue(sector.isOccupied());
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 assertTrue(sector.isOccupied());
 }

 @Test(expected = AssertionError.class)
 public void testGainControlWithOccupiedSector() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 }

 @Test
 public void testGetOccupier() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
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
 CharacterClass charClass1 = CharacterClass.ARCHER;
 CharacterClass charClass2 = CharacterClass.KNIGHT;
 SectorBonus expectedBonus1 = new SectorBonus(1, 2, 3);
 SectorBonus expectedBonus2 = new SectorBonus(4, 5, 6);
 assertEquals(expectedBonus1, sector.getBonus(charClass1));
 assertEquals(expectedBonus2, sector.getBonus(charClass2));
 }

 @Test
 public void testGetAttackBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 int expectedBonus = 6;
 assertEquals(expectedBonus, sector.getAttackBonus(charClass));
 }

 @Test
 public void testGetDefenseBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 int expectedBonus = 4;
 assertEquals(expectedBonus, sector.getDefenseBonus(charClass));
 }

 @Test
 public void testClassBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 int expectedBonus = 5;
 assertEquals(expectedBonus, sector.classBonus(charClass));
 }

 @Test
 public void testGetAreas() {
 assertEquals(2, sector.getAreas().size());
 }

 @Test
 public void testAddArea() {
 Set<Area> areasCopy = new HashSet<>(sector.getAreas());
 area1.setOccupier(new Combatant(CombatantSide.DEFENDER, new WeaponStub()));
 sector._addArea(area1);
 assertEquals(area1, sector.getAreas().iterator().next());
 assertFalse(areasCopy.contains(area1));
 }

 @Test
 public void testRemoveArea() {
 Set<Area> areasCopy = new HashSet<>(sector.getAreas());
 sector.removeArea(area1);
 assertFalse(sector.getAreas().contains(area1));
 assertTrue(areasCopy.contains(area1));
 }

 @Test
 public void testHashCode() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 2, 3, false, 5, CharacterClass.ARCHER);
 assertEquals(sector.hashCode(), sector2.hashCode());
 }

 @Test
 public void testEquals() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 2, 3, false, 5, CharacterClass.ARCHER);
 assertTrue(sector.equals(sector2));
 }

 @Test
 public void testAddObserver() {
 assertEquals(2, sector.getObservers().size());
 sector.addObserver(new SectorObserverStub());
 assertEquals(3, sector.getObservers().size());
 }

 @Test
 public void testRemoveObserver() {
 assertEquals(3, sector.getObservers().size());
 sector.removeObserver(observer1);
 assertEquals(2, sector.getObservers().size());
 }
}