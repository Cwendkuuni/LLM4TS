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
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.Weapon;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 10, CharacterClass.ARCHER);
 }

 @Test
 public void testSimpleSectorInitialization() {
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEquals(new HashSet<Area>(), sector.getAreas());
 assertEquals(new HashSet<SectorObserver>(), sector.getObservers());
 }

 @Test
 public void testFreeingCombatantFromSector() {
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(1, sector.getOccupierCount());
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGainingControlOfSector() {
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(1, sector.getOccupierCount());
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(1, sector.getOccupierCount());
 sector.getOccupier(); // should not return null
 }

 @Test
 public void testGetOccupier() {
 sector.gainControl(CombatantSide.ATTACKER);
 Combatant combatant = new Combatant("Attacker", CharacterClass.ARCHER);
 combatant.setWeapon(new Weapon("Bow", SectorBonus.BonusType.ATTACK, 3));
 sector.addArea(new Area(combatant));
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(sector.isOccupied());
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGetBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 SectorBonus expectedArchersBonus = new SectorBonus(7, 4, 3);
 SectorBonus expectedKnightsBonus = new SectorBonus(8, 5, 2);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.addArea(new Area(new Combatant("Archer", archer)));
 sector.addArea(new Area(new Combatant("Archer", archer)));
 sector.addArea(new Area(new Combatant("Knight", knight)));
 assertEquals(expectedArchersBonus, sector.getBonus(archer));
 assertEquals(expectedKnightsBonus, sector.getBonus(knight));
 }

 @Test
 public void testGetDamageBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 int expectedArchersBonus = 5;
 int expectedKnightsBonus = 4;
 sector.gainControl(CombatantSide.DEFENDER);
 sector.addArea(new Area(new Combatant("Archer", archer)));
 sector.addArea(new Area(new Combatant("Archer", archer)));
 sector.addArea(new Area(new Combatant("Knight", knight)));
 sector.addArea(new Area(new Combatant("Knight", knight)));
 sector.addArea(new Area(new Combatant("Knight", knight)));
 assertEquals(expectedArchersBonus, sector.getDamageBonus());
 assertEquals(expectedKnightsBonus, sector.getDamageBonus());
 }

 @Test
 public void testGetAttackBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 int expectedArchersBonus = 7;
 int expectedKnightsBonus = 5;
 sector.gainControl(CombatantSide.DEFENDER);
 sector.addArea(new Area(new Combatant("Archer", archer)));
 sector.addArea(new Area(new Combatant("Archer", archer)));
 sector.addArea(new Area(new Combatant("Knight", knight)));
 sector.addArea(new Area(new Combatant("Knight", knight)));
 sector.addArea(new Area(new Combatant("Knight", knight)));
 Weapon weapon = new Weapon("Bow", SectorBonus.BonusType.ATTACK, 3);
 sector.getAreas().get(0).setOccupier(new Combatant("Archer", archer));
 ((Combatant)sector.getAreas().get(0).getOccupier()).setWeapon(weapon);
 assertEquals(expectedArchersBonus, sector.getAttackBonus(archer));
 assertEquals(expectedKnightsBonus, sector.getAttackBonus(knight));
 }

 @Test
 public void testGetDefenseBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 int expectedArchersBonus = 4;
 int expectedKnightsBonus = 5;
 sector.gainControl(CombatantSide.DEFENDER);
 sector.addArea(new Area(new Combatant("Archer", archer)));
 sector.addArea(new Area(new Combatant("Archer", archer)));
 sector.addArea(new Area(new Combatant("Knight", knight)));
 sector.addArea(new Area(new Combatant("Knight", knight)));
 sector.addArea(new Area(new Combatant("Knight", knight)));
 Weapon weapon = new Weapon("Shield", SectorBonus.BonusType.DEFENSE, 3);
 sector.getAreas().get(1).setOccupier(new Combatant("Archer", archer));
 sector.getAreas().get(2).setOccupier(new Combatant("Knight", knight));
 sector.addArea(new Area(new Combatant("Knight", knight)));
 ((Combatant)sector.getAreas().get(1).getOccupier()).setWeapon(weapon);
 assertEquals(expectedArchersBonus, sector.getDefenseBonus(archer));
 assertEquals(expectedKnightsBonus, sector.getDefenseBonus(knight));
 }

 @Test
 public void testClassBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 assertEquals(10, sector.classBonus(archer));
 assertEquals(10, sector.classBonus(knight));
 }

 @Test
 public void testGetAreas() {
 Set<Area> areas = new HashSet<Area>(Arrays.asList(new Area(new Combatant("Archer", CharacterClass.ARCHER)), new Area(new Combatant("Knight", CharacterClass.KNIGHT))));
 sector.setAreas(areas);
 assertEquals(areas, sector.getAreas());
 }

 @Test
 public void testAddArea() {
 Set<Area> areas = new HashSet<Area>();
 sector.setAreas(areas);
 Area area = new Area(new Combatant("Archer", CharacterClass.ARCHER));
 sector.addArea(area);
 assertTrue(sector.getAreas().contains(area));
 }
}