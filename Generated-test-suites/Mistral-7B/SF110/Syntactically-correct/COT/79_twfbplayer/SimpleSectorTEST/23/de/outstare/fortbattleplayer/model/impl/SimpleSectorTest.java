package de.outstare.fortbattleplayer.model.impl.test;

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
 sector = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
 }

 @Test
 public void testSimpleSectorConstructor() {
 // Check if all fields are initialized correctly
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(1, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals(2, sector.getDefenseBonus(CharacterClass.ARCHER));
 assertTrue(sector.isFlag());
 assertEquals(3, sector.getClassBonus());
 assertEquals(CharacterClass.ARCHER, sector.getClassType());
 assertNull(sector.getOccupier());
 assertEquals(0, sector.getOccupierCount());
 assertTrue(sector.getAreas().isEmpty());
 assertTrue(sector.getObservers().isEmpty());
 }

 @Test
 public void testFreeMethod() {
 // Test when there are no combatants in the sector
 sector.free();
 assertTrue(sector.isOccupied());
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());

 // Test when there is one combatant in the sector
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(1, sector.getOccupierCount());
 assertNotNull(sector.getOccupier());
 sector.free();
 assertTrue(sector.isOccupied());
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 }

 @Test(expected = AssertionError.class)
 public void testFreeMethodWithNegativeOccupierCount() {
 // Test when the occupierCount is negative
 sector.occupierCount = -1;
 sector.free();
 }

 @Test
 public void testGainControlMethod() {
 // Test when the sector is not occupied
 assertFalse(sector.isOccupied());
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 assertEquals(1, sector.getOccupierCount());
 assertNotNull(sector.getOccupier());
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(2, sector.getOccupierCount());
 assertNotNull(sector.getOccupier());
 }

 @Test(expected = IllegalStateException.class)
 public void testGainControlMethodWithSameSide() {
 // Test when trying to gain control with the same side
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 }

 @Test
 public void testGetOccupierMethod() {
 // Test when the sector is not occupied
 assertNull(sector.getOccupier());
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 }

 @Test
 public void testIsOccupiedMethod() {
 // Test when the sector is not occupied
 assertFalse(sector.isOccupied());
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGetHeightMethod() {
 assertEquals(5, sector.getHeight());
 }

 @Test
 public void testIsDefenderSpawnMethod() {
 assertTrue(sector.isDefenderSpawn());
 }

 @Test
 public void testIsAttackerSpawnMethod() {
 assertTrue(sector.isAttackerSpawn());
 }

 @Test
 public void testIsFlagMethod() {
 assertTrue(sector.isFlag());
 }

 @Test
 public void testGetBonusMethod() {
 // Test with the default class
 SectorBonus bonus = sector.getBonus(CharacterClass.ARCHER);
 assertEquals(4, bonus.getAttackBonus());
 assertEquals(3, bonus.getDefenseBonus());
 assertEquals(0, bonus.getDamageBonus());

 // Test with a different class
 bonus = sector.getBonus(CharacterClass.KNIGHT);
 assertEquals(5, bonus.getAttackBonus());
 assertEquals(4, bonus.getDefenseBonus());
 assertEquals(2, bonus.getDamageBonus());
 }

 @Test
 public void testGetAttackBonusMethod() {
 // Test with the default class
 assertEquals(4, sector.getAttackBonus(CharacterClass.ARCHER));
 // Test with a different class
 assertEquals(6, sector.getAttackBonus(CharacterClass.KNIGHT));
 }

 @Test
 public void testGetDefenseBonusMethod() {
 // Test with the default class
 assertEquals(3, sector.getDefenseBonus(CharacterClass.ARCHER));
 // Test with a different class
 assertEquals(5, sector.getDefenseBonus(CharacterClass.KNIGHT));
 }

 @Test
 public void testGetDamageBonusMethod() {
 // Test when there are no weapons in the sector
 assertEquals(0, sector.getDamageBonus());

 // Test when there is a weapon in the sector
 Area area = new Area();
 Combatant combatant = new Combatant(CharacterClass.ARCHER, new Weapon(1, 1));
 area.setOccupier(combatant);
 sector._addArea(area);
 assertEquals(1, sector.getDamageBonus());
 }

 @Test
 public void testClassBonusMethod() {
 // Test when the classType matches the given class
 assertEquals(3, sector.classBonus(CharacterClass.ARCHER));
 // Test when the classType does not match the given class
 assertEquals(0, sector.classBonus(CharacterClass.KNIGHT));
 }

 @Test
 public void testGetAreasMethod() {
 // Test when there are no areas in the sector
 assertTrue(sector.getAreas().isEmpty());

 // Test when there is an area in the sector
 Area area = new Area();
 sector._addArea(area);
 assertFalse(sector.getAreas().isEmpty());
 }

 @Test
 public void testAddAreaMethod() {
 // Test when adding an area to an empty sector
 Set<Area> areas = sector.getAreas();
 area = new Area();
 sector._addArea(area);
 assertTrue(areas.contains(area));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddAreaMethodWithNullArea() {
 // Test when trying to add a null area
 Set<Area> areas = sector.getAreas();
 sector._addArea(null);
 }

 @Test
 public void testHashCodeMethod() {
 Set<Area> areas = new HashSet<>();
 Area area = new Area();
 areas.add(area);
 SimpleSector sector1 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
 sector3.getAreas().add(area);

 assertEquals(sector1.hashCode(), sector2.hashCode());
 assertNotEquals(sector1.hashCode(), sector3.hashCode());
 }

 @Test
 public void testEqualsMethod() {
 Set<Area> areas1 = new HashSet<>();
 Area area1 = new Area();
 areas1.add(area1);
 SimpleSector sector1 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
 sector1.getAreas().addAll(areas1);
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
 sector3.getAreas().addAll(areas1);

 assertTrue(sector1.equals(sector2));
 assertFalse(sector1.equals(sector3));
 }

 @Test
 public void testAddObserverMethod() {
 SectorObserver observer = new SectorObserver() {
 @Override
 public void occupierChanged() {
 // do nothing
 }
 };
 sector.addObserver(observer);
 assertTrue(sector.getObservers().contains(observer));
 }

 @Test
 public void testRemoveObserverMethod() {
 SectorObserver observer = new SectorObserver() {
 @Override
 public void occupierChanged() {
 // do nothing
 }
 };
 sector.addObserver(observer);
 sector.removeObserver(observer);
 assertFalse(sector.getObservers().contains(observer));
 }
}