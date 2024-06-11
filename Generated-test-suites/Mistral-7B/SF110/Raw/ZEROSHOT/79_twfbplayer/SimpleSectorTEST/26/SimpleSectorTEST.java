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
 sector = new SimpleSector(5, true, true, 10, 5, false, 2, CharacterClass.ARCHER);
 }

 @Test
 public void testSimpleSectorConstruction() {
 // Test initialization
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertFalse(sector.isFlag());
 assertEquals(2, sector.getClassBonus());
 assertEquals(CharacterClass.ARCHER, sector.getClassType());
 assertNull(sector.getOccupier());
 assertEquals(0, sector.getOccupierCount());
 assertEmpty(sector.getAreas());
 assertEmpty(sector.getObservers());
 }

 @Test
 public void testFree() {
 // Test freeing an empty sector
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());

 // Test freeing a sector with combatants
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(3, sector.getOccupierCount());
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 }

 @Test
 public void testOccupierChanged() {
 Set<SectorObserver> observers = new HashSet<>();
 sector.addObserver(observers);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(1, sector.getOccupierCount());
 assertSame(sector.getOccupier(), CombatantSide.DEFENDER);
 assertEquals(1, observers.size());
 sector.occupierChanged();
 assertTrue(observers.stream().anyMatch(o -> o.occupierChanged()));
 }

 @Test
 public void testGainControl() {
 // Test gaining control of an empty sector
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(1, sector.getOccupierCount());
 assertSame(sector.getOccupier(), CombatantSide.DEFENDER);

 // Test gaining control of a sector occupied by the opposing side
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(1, sector.getOccupierCount());
 assertSame(sector.getOccupier(), CombatantSide.ATTACKER);

 // Test gaining control of a sector occupied by the same side
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(2, sector.getOccupierCount());
 assertSame(sector.getOccupier(), CombatantSide.DEFENDER);
 }

 @Test
 public void testGetOccupier() {
 // Test getting occupier of an empty sector
 assertNull(sector.getOccupier());

 // Test getting occupier of a sector occupied by the defender
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());

 // Test getting occupier of a sector occupied by the attacker
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 // Test isOccupied of an empty sector
 assertFalse(sector.isOccupied());

 // Test isOccupied of a sector occupied by the defender
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());

 // Test isOccupied of a sector occupied by the attacker
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testGetHeight() {
 // Test getting height of a sector
 assertEquals(5, sector.getHeight());
 }

 @Test
 public void testIsDefenderSpawn() {
 // Test isDefenderSpawn of a sector
 assertTrue(sector.isDefenderSpawn());
 }

 @Test
 public void testIsAttackerSpawn() {
 // Test isAttackerSpawn of a sector
 assertTrue(sector.isAttackerSpawn());
 }

 @Test
 public void testIsFlag() {
 // Test isFlag of a sector
 assertFalse(sector.isFlag());
 }

 @Test
 public void testGetBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 Combatant archerCombatant = new StaticCombatant(archer, new Weapon(CharacterClass.ARCHER, 5));
 Combatant knightCombatant = new StaticCombatant(knight, new Weapon(CharacterClass.KNIGHT, 10));
 Area archerArea = new SimpleArea(new Point(0, 0), archerCombatant);
 Area knightArea = new SimpleArea(new Point(1, 1), knightCombatant);
 sector.addArea(archerArea);
 sector.addArea(knightArea);
 SectorBonus archerBonus = sector.getBonus(archer);
 SectorBonus knightBonus = sector.getBonus(knight);
 assertEquals(15, archerBonus.getAttackBonus());
 assertEquals(12, archerBonus.getDefenseBonus());
 assertEquals(3, archerBonus.getDamageBonus());
 assertEquals(5, knightBonus.getAttackBonus());
 assertEquals(15, knightBonus.getDefenseBonus());
 assertEquals(4, knightBonus.getDamageBonus());
 }

 @Test
 public void testGetDamageBonus() {
 // Test getting damage bonus of an empty sector
 assertEquals(0, sector.getDamageBonus());

 // Test getting damage bonus of a sector with weapons
 sector.addArea(new SimpleArea(new Point(0, 0), new StaticCombatant(CharacterClass.ARCHER, new Weapon(CharacterClass.ARCHER, 5))));
 sector.addArea(new SimpleArea(new Point(1, 1), new StaticCombatant(CharacterClass.ARCHER, new Weapon(CharacterClass.ARCHER, 5))));
 sector.addArea(new SimpleArea(new Point(2, 2), new StaticCombatant(CharacterClass.KNIGHT, new Weapon(CharacterClass.KNIGHT, 10))));
 assertEquals(5, sector.getDamageBonus());
 }

 @Test
 public void testGetAttackBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 Combatant archerCombatant = new StaticCombatant(archer, new Weapon(CharacterClass.ARCHER, 5));
 Combatant knightCombatant = new StaticCombatant(knight, new Weapon(CharacterClass.KNIGHT, 10));
 Area archerArea = new SimpleArea(new Point(0, 0), archerCombatant);
 Area knightArea = new SimpleArea(new Point(1, 1), knightCombatant);
 sector.addArea(archerArea);
 sector.addArea(knightArea);
 assertEquals(15, sector.getAttackBonus(archer));
 assertEquals(5, sector.getAttackBonus(knight));
 }

 @Test
 public void testGetDefenseBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 Combatant archerCombatant = new StaticCombatant(archer, new Weapon(CharacterClass.ARCHER, 5));
 Combatant knightCombatant = new StaticCombatant(knight, new Weapon(CharacterClass.KNIGHT, 10));
 Area archerArea = new SimpleArea(new Point(0, 0), archerCombatant);
 Area knightArea = new SimpleArea(new Point(1, 1), knightCombatant);
 sector.addArea(archerArea);
 sector.addArea(knightArea);
 assertEquals(12, sector.getDefenseBonus(archer));
 assertEquals(15, sector.getDefenseBonus(knight));
 }

 @Test
 public void testClassBonus() {
 CharacterClass archer = CharacterClass.ARCHER;
 CharacterClass knight = CharacterClass.KNIGHT;
 assertEquals(2, sector.classBonus(archer));
 assertEquals(2, sector.classBonus(knight));
 }

 @Test
 public void testGetAreas() {
 Area archerArea = new SimpleArea(new Point(0, 0), new StaticCombatant(CharacterClass.ARCHER, new Weapon(CharacterClass.ARCHER, 5)));
 Area knightArea = new SimpleArea(new Point(1, 1), new StaticCombatant(CharacterClass.KNIGHT, new Weapon(CharacterClass.KNIGHT, 10)));
 sector.addArea(archerArea);
 sector.addArea(knightArea);
 Set<Area> expectedAreas = new HashSet<>(Arrays.asList(archerArea, knightArea));
 assertEquals(expectedAreas, sector.getAreas());
 }

 @Test
 public void testAddArea() {
 Area archerArea = new SimpleArea(new Point(0, 0), new StaticCombatant(CharacterClass.ARCHER, new Weapon(CharacterClass.ARCHER, 5)));
 sector.addArea(archerArea);
 Set<Area> expectedAreas = new HashSet<>(Arrays.asList(archerArea));
 assertEquals(expectedAreas, sector.getAreas());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullArea() {
 sector.addArea(null);
 }

 @Test
 public void testHashCode() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 2, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 2, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 11, 5, false, 2, CharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(4, true, true, 10, 5, false, 2, CharacterClass.ARCHER);
 Set<SimpleSector> sectors = new HashSet<>();
 sectors.add(sector1);
 sectors.add(sector2);
 sectors.add(sector3);
 sectors.add(sector4);
 assertTrue(sectors.contains(sector1));
 assertTrue(sectors.contains(sector2));
 assertFalse(sectors.contains(sector3));
 assertFalse(sectors.contains(sector4));
 }

 @Test
 public void testEquals() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 2, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 2, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 9, 5, false, 2, CharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(4, true, true, 10, 5, false, 2, CharacterClass.ARCHER);
 assertTrue(sector1.equals(sector1));
 assertTrue(sector1.equals(sector2));
 assertFalse(sector1.equals(sector3));
 assertFalse(sector1.equals(sector4));
 }

 @Test
 public void testAddObserver() {
 Set<SectorObserver> observers = new HashSet<>();
 sector.addObserver(observers);
 assertEquals(1, observers.size());
 }

 @Test
 public void testRemoveObserver() {
 Set<SectorObserver> observers = new HashSet<>();
 sector.addObserver(observers);
 assertEquals(1, observers.size());
 sector.removeObserver(observers);
 assertEquals(0, observers.size());
 }
}