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
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SimpleWeapon;
import de.outstare.fortbattleplayer.model.observer.impl.SimpleSectorObserver;

public class SimpleSectorTest {

 private SimpleSector sector;
 private SimpleWeapon weapon;
 private SimpleSectorObserver observer;

 @Before
 public void setup() {
 sector = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
 weapon = new SimpleWeapon("Sword", 10, 5, 1);
 }

 @Test
 public void testConstructor() {
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(1, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals(2, sector.getDefenseBonus(CharacterClass.ARCHER));
 assertNull(sector.getOccupier());
 assertEquals(0, sector.getOccupierCount());
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testFree() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 sector.free();
 assertNull(sector.getOccupier());
 assertEquals(0, sector.getOccupierCount());
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testOccupierChanged() {
 observer = new SimpleSectorObserver() {
 @Override
 public void occupierChanged() {
 assertEquals(sector.getOccupier(), SimpleSectorTest.this.sector.getOccupier());
 }
 };
 sector.addObserver(observer);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 }

 @Test
 public void testGainControl() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 }

 @Test
 public void testGetOccupier() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
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
 assertTrue(sector.isFlag());
 }

 @Test
 public void testGetBonus() {
 assertEquals(new SectorBonus(1, 2, 0), sector.getBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetAttackBonus() {
 assertEquals(4, sector.getAttackBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDefenseBonus() {
 assertEquals(3, sector.getDefenseBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testClassBonus() {
 assertEquals(3, sector.classBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetAreas() {
 assertEquals(new HashSet<Area>(), sector.getAreas());
 }

 @Test
 public void testAddArea() {
 Area area = new SimpleArea();
 sector.addArea(area);
 assertTrue(sector.getAreas().contains(area));
 }

 @Test
 public void testHashCode() {
 Set<Area> areas = new HashSet<Area>(Arrays.asList(new SimpleArea(), new SimpleArea()));
 SimpleSector sector1 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER, areas);
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER, areas);
 assertEquals(sector1.hashCode(), sector2.hashCode());
 }

 @Test
 public void testEquals() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER, new HashSet<Area>());
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER, new HashSet<Area>());
 assertTrue(sector1.equals(sector2));
 }

 @Test
 public void testAddObserver() {
 sector.addObserver(observer);
 assertEquals(1, sector.getObservers().size());
 }

 @Test
 public void testRemoveObserver() {
 sector.addObserver(observer);
 sector.removeObserver(observer);
 assertEquals(0, sector.getObservers().size());
 }
}