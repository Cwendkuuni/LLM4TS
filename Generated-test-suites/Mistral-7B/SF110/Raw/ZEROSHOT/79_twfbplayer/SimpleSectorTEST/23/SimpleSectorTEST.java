package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
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
 sector = new SimpleSector(5, true, true, 1, 1, true, 1, CharacterClass.ARCHER);
 }

 @Test
 public void testSimpleSectorCreation() {
 assertNotNull(sector);
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertFalse(sector.isOccupied());
 assertNull(sector.getOccupier());
 assertFalse(sector.isFlag());
 assertEquals(CharacterClass.ARCHER, sector.getClassType());
 assertEquals(1, sector.getClassBonus());
 assertEquals(0, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals(0, sector.getDefenseBonus(CharacterClass.ARCHER));
 assertNotNull(sector.getAreas());
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testFree() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertFalse(sector.isOccupied());
 assertNull(sector.getOccupier());
 }

 @Test
 public void testGainControl() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testOccupierChanged() {
 Set<SectorObserver> observers = new HashSet<>();
 sector.addObserver(observers);
 sector.free();
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(observers.size() > 0);
 }

 @Test
 public void testGetBonus() {
 sector.addArea(new SimpleArea(new SimpleCombatant(CharacterClass.ARCHER, new SimpleWeapon(CharacterWeaponType.BOW, 10))));
 sector.addArea(new SimpleArea(new SimpleCombatant(CharacterClass.ARCHER, new SimpleWeapon(CharacterWeaponType.BOW, 15))));
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(1, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals(1, sector.getDefenseBonus(CharacterClass.ARCHER));
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(15, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals(1, sector.getDefenseBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testClassBonus() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(1, sector.classBonus(CharacterClass.ARCHER));
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(1, sector.classBonus(CharacterClass.ARCHER));
 sector = new SimpleSector(5, true, true, 1, 1, true, 2, CharacterClass.ARCHER);
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(2, sector.classBonus(CharacterClass.ARCHER));
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(2, sector.classBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testHashCodeAndEquals() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 1, 1, true, 1, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 1, true, 1, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 1, 1, true, 2, CharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(5, false, false, 1, 1, false, 1, CharacterClass.ARCHER);
 SimpleSector sector5 = new SimpleSector(5, true, true, 1, 1, true, 1, CharacterClass.ARCHER);
 Set<Area> areas1 = new HashSet<>(Arrays.asList(new SimpleArea(new SimpleCombatant(CharacterClass.ARCHER, new SimpleWeapon(CharacterWeaponType.BOW, 10))), new SimpleArea(new SimpleCombatant(CharacterClass.ARCHER, new SimpleWeapon(CharacterWeaponType.BOW, 15)))));
 sector1.addAreas(areas1);
 Set<Area> areas2 = new HashSet<>(Arrays.asList(new SimpleArea(new SimpleCombatant(CharacterClass.ARCHER, new SimpleWeapon(CharacterWeaponType.BOW, 15))), new SimpleArea(new SimpleCombatant(CharacterClass.ARCHER, new SimpleWeapon(CharacterWeaponType.BOW, 10)))));
 sector2.addAreas(areas2);
 Set<Area> areas3 = new HashSet<>(Arrays.asList(new SimpleArea(new SimpleCombatant(CharacterClass.ARCHER, new SimpleWeapon(CharacterWeaponType.BOW, 10))), new SimpleArea(new SimpleCombatant(CharacterClass.ARCHER, new SimpleWeapon(CharacterWeaponType.BOW, 15)))));
 sector3.addAreas(areas3);
 Set<Area> areas4 = new HashSet<>();
 sector4.addAreas(areas4);
 Set<SectorObserver> observers1 = new HashSet<>();
 Set<SectorObserver> observers2 = new HashSet<>();
 Set<SectorObserver> observers3 = new HashSet<>();
 sector1.addObserver(observers1);
 sector2.addObserver(observers2);
 sector3.addObserver(observers3);
 assertEquals(sector1.hashCode(), sector1.hashCode());
 assertEquals(sector1.hashCode(), sector5.hashCode());
 assertNotEquals(sector1.hashCode(), sector2.hashCode());
 assertNotEquals(sector1.hashCode(), sector3.hashCode());
 assertNotEquals(sector1.hashCode(), sector4.hashCode());
 assertTrue(sector1.equals(sector1));
 assertFalse(sector1.equals(sector2));
 assertFalse(sector1.equals(sector3));
 assertFalse(sector1.equals(sector4));
 }
}