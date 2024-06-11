package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SimpleArea;
import de.outstare.fortbattleplayer.model.impl.SimpleCharacterClass;
import de.outstare.fortbattleplayer.model.impl.SimpleWeapon;
import de.outstare.fortbattleplayer.model.impl.SimpleCombatant;
import de.outstare.fortbattleplayer.model.impl.SimpleCombatantSide;
import de.outstare.fortbattleplayer.model.impl.SimpleSectorObserver;

public class SimpleSectorTest {

 private SimpleSector sector;
 private Set<Area> areas;
 private SimpleArea area1, area2, area3;
 private Set<SectorObserver> observers;
 private SimpleSectorObserver observer1, observer2;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 1, 2, true, 3, SimpleCharacterClass.ARCHER);
 areas = new HashSet<>();
 area1 = new SimpleArea();
 area2 = new SimpleArea();
 area3 = new SimpleArea();
 observers = new HashSet<>();
 observer1 = new SimpleSectorObserver() {
 @Override
 public void occupierChanged() {
 // empty implementation
 }
 };
 observer2 = new SimpleSectorObserver() {
 @Override
 public void occupierChanged() {
 // empty implementation
 }
 };
 sector.addObserver(observer1);
 sector.addObserver(observer2);
 sector._addArea(area1);
 sector._addArea(area2);
 sector._addArea(area3);
 }

 @Test
 public void testConstructor() {
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(3, sector.getBonus(SimpleCharacterClass.ARCHER).getAttackBonus());
 assertEquals(2, sector.getBonus(SimpleCharacterClass.ARCHER).getDefenseBonus());
 assertFalse(sector.isFlag());
 assertNull(sector.getOccupier());
 assertEquals(0, sector.getOccupierCount());
 assertFalse(sector.isOccupied());
 assertEquals(3, sector.getAreas().size());
 }

 @Test
 public void testFree() {
 sector.gainControl(SimpleCombatantSide.DEFENDER);
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 }

 @Test
 public void testGainControl() {
 sector.gainControl(SimpleCombatantSide.DEFENDER);
 assertEquals(SimpleCombatantSide.DEFENDER, sector.getOccupier());
 assertEquals(1, sector.getOccupierCount());
 }

 @Test
 public void testOccupied() {
 sector.gainControl(SimpleCombatantSide.ATTACKER);
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
 CharacterClass characterClass = SimpleCharacterClass.ARCHER;
 SectorBonus bonus = sector.getBonus(characterClass);
 assertEquals(4, bonus.getAttackBonus());
 assertEquals(2, bonus.getDefenseBonus());
 assertEquals(0, bonus.getDamageBonus());
 }

 @Test
 public void testGetDamageBonus() {
 assertEquals(0, sector.getDamageBonus());
 }

 @Test
 public void testGetAttackBonus() {
 CharacterClass characterClass = SimpleCharacterClass.ARCHER;
 assertEquals(3, sector.getAttackBonus(characterClass));
 }

 @Test
 public void testGetDefenseBonus() {
 CharacterClass characterClass = SimpleCharacterClass.ARCHER;
 assertEquals(2, sector.getDefenseBonus(characterClass));
 }

 @Test
 public void testClassBonus() {
 CharacterClass characterClass = SimpleCharacterClass.ARCHER;
 assertEquals(3, sector.classBonus(characterClass));
 }

 @Test
 public void testGetAreas() {
 assertEquals(3, sector.getAreas().size());
 }

 @Test
 public void testAddArea() {
 Set<Area> areasClone = new HashSet<>(sector.getAreas());
 sector._addArea(new SimpleArea());
 assertEquals(4, sector.getAreas().size());
 assertEquals(3, areasClone.size());
 }

 @Test
 public void testHashCode() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 2, true, 3, SimpleCharacterClass.ARCHER);
 assertEquals(hashCode(), sector2.hashCode());
 }

 @Test
 public void testEquals() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 2, true, 3, SimpleCharacterClass.ARCHER);
 assertTrue(sector.equals(sector2));
 }

 @Test
 public void testAddObserver() {
 sector.addObserver(observer1);
 assertEquals(2, observers.size());
 }

 @Test
 public void testRemoveObserver() {
 sector.removeObserver(observer1);
 assertEquals(1, observers.size());
 }
}