package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;

public class SimpleSectorTest {

 private Sector sector;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
 }

 @Test
 public void testSimpleSectorConstruction() {
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(5, sector.getAttackBonus(CharacterClass.WARRIOR));
 assertEquals(3, sector.getDefenseBonus(CharacterClass.WARRIOR));
 assertFalse(sector.isFlag());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEquals(0, sector.occupierCount);
 assertNotNull(sector.areas);
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testFree() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(1, sector.occupierCount);
 sector.free();
 assertEquals(0, sector.occupierCount);
 assertNull(sector.getOccupier());
 }

 @Test
 public void testGainControl() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(1, sector.occupierCount);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test(expected = AssertionError.class)
 public void testGainControlWithOccupiedSector() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 }

 @Test
 public void testGetOccupier() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testFreeWithNoCombatant() {
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testOccupierChanged() {
 Set<SectorObserver> observers = new HashSet<>();
 sector.addObserver(observers);
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(observers.size() > 0);
 }

 @Test
 public void testGetAreas() {
 Set<Area> areas = new HashSet<>();
 sector.addObserver(areas);
 sector._addArea(new Area(1, 1));
 sector._addArea(new Area(1, 2));
 assertEquals(2, sector.getAreas().size());
 }

 @Test
 public void testGetBonus() {
 assertEquals(new SectorBonus(7, 5, 0), sector.getBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetAttackBonus() {
 assertEquals(10, sector.getAttackBonus(CharacterClass.WARRIOR));
 }

 @Test
 public void testGetDefenseBonus() {
 assertEquals(8, sector.getDefenseBonus(CharacterClass.MAGE));
 }

 @Test
 public void testGetDamageBonus() {
 assertEquals(3, sector.getDamageBonus());
 }

 @Test
 public void testClassBonus() {
 assertEquals(2, sector.classBonus(CharacterClass.WARRIOR));
 }

 @Test
 public void testHashCode() {
 Set<Area> areas = new HashSet<>(Arrays.asList(new Area(1, 1), new Area(1, 2), new Area(1, 3)));
 SimpleSector sector1 = new SimpleSector(5, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
 SimpleSector sector2 = new SimpleSector(5, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
 SimpleSector sector3 = new SimpleSector(5, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(5, true, true, 5, 3, true, 2, CharacterClass.MAGE);
 SimpleSector sector5 = new SimpleSector(5, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
 SimpleSector sector6 = new SimpleSector(5, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
 SimpleSector sector7 = new SimpleSector(6, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);

 Set<Sector> sectors = new HashSet<>();
 sectors.add(sector1);
 sectors.add(sector2);
 sectors.add(sector3);
 sectors.add(sector4);
 sectors.add(sector5);
 sectors.add(sector6);
 sectors.add(sector7);

 for (Sector sector : sectors) {
 System.out.println(sector.hashCode());
 }

 assertEquals(sector1.hashCode(), sector5.hashCode());
 assertNotEquals(sector1.hashCode(), sector6.hashCode());
 assertNotEquals(sector1.hashCode(), sector7.hashCode());
 }

 @Test
 public void testEquals() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
 SimpleSector sector2 = new SimpleSector(5, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
 SimpleSector sector3 = new SimpleSector(5, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(5, true, true, 5, 3, true, 2, CharacterClass.MAGE);
 SimpleSector sector5 = new SimpleSector(5, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
 SimpleSector sector6 = new SimpleSector(5, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
 SimpleSector sector7 = new SimpleSector(6, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);

 assertTrue(sector1.equals(sector5));
 assertFalse(sector1.equals(sector3));
 assertFalse(sector1.equals(sector6));
 assertFalse(sector1.equals(sector7));
 }

 @Test
 public void testAddRemoveObserver() {
 Set<SectorObserver> observers = new HashSet<>();
 sector.addObserver(observers);
 sector.removeObserver(observers);
 assertFalse(sector.observers.contains(observers));
 }
}