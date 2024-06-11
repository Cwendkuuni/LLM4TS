package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SimpleSectorObserver;
import de.outstare.fortbattleplayer.model.impl.Weapon;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Test
 public void testSimpleSectorConstruction() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testFreeMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test(expected = AssertionError.class)
 public void testFreeNegativeOccupierCount() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 sector.free();
 }

 @Test
 public void testOccupierChangedMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 sector.addObserver(new SimpleSectorObserver() {
 @Override
 public void occupierChanged() {
 System.out.println("Occupier changed!");
 }
 });
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals("Occupier changed!", sector.getOccupierChangedLog());
 }

 @Test
 public void testGainControlMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 Combatant defender = new Combatant(CharacterClass.ARCHER, new Weapon("Bow", 10, 5, 10));
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGainControlNullOccupier() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 sector.gainControl(null);
 }

 @Test
 public void testGetOccupierMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testIsOccupiedMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testGetBonusMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 Combatant defender = new Combatant(CharacterClass.ARCHER, new Weapon("Bow", 10, 5, 10));
 sector.gainControl(CombatantSide.DEFENDER);
 SectorBonus bonus = sector.getBonus(CharacterClass.ARCHER);
 assertEquals(5, bonus.getAttackBonus());
 assertEquals(3, bonus.getDefenseBonus());
 }

 @Test
 public void testGetAttackBonusMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 Combatant defender = new Combatant(CharacterClass.ARCHER, new Weapon("Bow", 10, 5, 10));sector.addArea(new SimpleArea(3, 3));
 assertEquals(6, sector.getAttackBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDefenseBonusMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 Combatant defender = new Combatant(CharacterClass.ARCHER, new Weapon("Shield", 5, 10, 5));
 sector.addArea(new SimpleArea(3, 3));
 assertEquals(8, sector.getDefenseBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testClassBonusMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(4, sector.classBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetAreasMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 Set<Area> areas = new HashSet<Area>();
 sector.addArea(new SimpleArea(3, 3));
 sector.addArea(new SimpleArea(3, 4));
 assertEquals(areas, sector.getAreas());
 }

 @Test
 public void testAddAreaMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 Set<Area> areas = new HashSet<Area>();
 sector.addArea(new SimpleArea(3, 3));
 assertEquals(1, sector.getAreas().size());
 }

 @Test
 public void testHashCodeMethod() {
 Set<Area> areas1 = new HashSet<Area>();
 Set<Area> areas2 = new HashSet<Area>();
 SimpleSector sector1 = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 sector1.addArea(new SimpleArea(3, 3));
 sector2.addArea(new SimpleArea(3, 4));
 assertEquals(sector1.hashCode(), sector1.hashCode());
 assertNotEquals(sector1.hashCode(), sector2.hashCode());
 }

 @Test
 public void testEqualsMethod() {
 Set<Area> areas1 = new HashSet<Area>();
 Set<Area> areas2 = new HashSet<Area>();
 SimpleSector sector1 = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 sector1.addArea(new SimpleArea(3, 3));
 sector2.addArea(new SimpleArea(3, 4));
 assertFalse(sector1.equals(sector2));
 }

 @Test
 public void testAddObserverMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 SimpleSectorObserver observer = new SimpleSectorObserver() {
 @Override
 public void occupierChanged() {
 System.out.println("Occupier changed!");
 }
 };
 sector.addObserver(observer);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals("Occupier changed!", sector.getOccupierChangedLog());
 }

 @Test
 public void testRemoveObserverMethod() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 SimpleSectorObserver observer = new SimpleSectorObserver() {
 @Override
 public void occupierChanged() {
 System.out.println("Occupier changed!");
 }
 };
 sector.addObserver(observer);
 sector.removeObserver(observer);
 sector.gainControl(CombatantSide.DEFENDER);
 assertFalse(sector.getOccupierChangedLog().contains("Occupier changed!"));
 }
}