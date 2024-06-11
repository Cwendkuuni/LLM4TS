package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 }

 @Test
 public void testConstructor() {
 // check initial values
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(2, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals(3, sector.getDefenseBonus(CharacterClass.ARCHER));
 assertTrue(sector.isFlag());
 assertEquals(4, sector.getClassBonus());
 assertEquals(CharacterClass.ARCHER, sector.getClassType());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEmpty(sector.getAreas());
 assertEmpty(sector.getObservers());
 }

 @Test
 public void testFree() {
 // test empty sector
 sector.free();
 assertTrue(sector.isOccupied());
 sector.free();
 assertFalse(sector.isOccupied());
 assertNull(sector.getOccupier());

 // test occupied sector
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(1, sector.getOccupierCount());
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 }

 @Test
 public void testOccupierChanged() {
 Set<SectorObserver> observers = new HashSet<>();
 sector.addObserver(observers);
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(observers.size() > 0);
 }

 @Test
 public void testGainControl() {
 // test occupied sector
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(1, sector.getOccupierCount());
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(1, sector.getOccupierCount());
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());

 // test occupied by another side
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(1, sector.getOccupierCount());
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
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 SectorBonus bonus = sector.getBonus(archer);
 assertEquals(6, bonus.getAttackBonus());
 assertEquals(3, bonus.getDefenseBonus());
 assertEquals(sector.getDamageBonus(), bonus.getDamageBonus());

 bonus = sector.getBonus(knight);
 assertEquals(6, sector.getAttackBonus(knight));
 assertEquals(3, sector.getDefenseBonus(knight));
 assertEquals(sector.getDamageBonus(), bonus.getDamageBonus());
 }

 @Test
 public void testGetAttackBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 assertEquals(2, sector.getAttackBonus(archer));
 assertEquals(6, sector.getAttackBonus(knight));
 }

 @Test
 public void testGetDefenseBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 assertEquals(3, sector.getDefenseBonus(archer));
 assertEquals(3, sector.getDefenseBonus(knight));
 }

 @Test
 public void testClassBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 assertEquals(0, sector.classBonus(archer));
 assertEquals(4, sector.classBonus(knight));
 }

 @Test
 public void testGetAreas() {
 Set<Area> areas = new HashSet<>();
 sector._addArea(new Area());
 sector.getAreas();
 assertEquals(areas.size(), sector.getAreas().size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddArea() {
 sector.addArea(new Area());
 }

 @Test
 public void testHashCode() {
 SimpleSector sector2 = new SimpleSector(5, false, false, 2, 3, false, 4, CharacterClass.ARCHER);
 assertEquals(sector.hashCode(), sector2.hashCode());

 // test different values
 SimpleSector sector3 = new SimpleSector(6, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 assertNotSame(sector.hashCode(), sector3.hashCode());
 }

 @Test
 public void testEquals() {
 SimpleSector sector2 = new SimpleSector(5, false, false, 2, 3, false, 4, CharacterClass.ARCHER);
 assertTrue(sector.equals(sector2));

 SimpleSector sector3 = new SimpleSector(5, false, false, 3, 3, true, 4, CharacterClass.ARCHER);
 assertFalse(sector.equals(sector3));
 }
}