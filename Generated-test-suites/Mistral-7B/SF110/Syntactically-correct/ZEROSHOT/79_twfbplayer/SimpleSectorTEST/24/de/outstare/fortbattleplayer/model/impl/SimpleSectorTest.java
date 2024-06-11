package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SimpleCharacterClass;
import de.outstare.fortbattleplayer.model.impl.SimpleCombatant;
import de.outstare.fortbattleplayer.model.impl.SimpleWeapon;
import de.outstare.fortbattleplayer.model.impl.SimpleArea;
import de.outstare.fortbattleplayer.model.impl.SimpleSectorObserver;

public class SimpleSectorTest {

 private SimpleSector sector;
 private CharacterClass knight = new SimpleCharacterClass(CharacterClassType.KNIGHT);
 private CharacterClass archer = new SimpleCharacterClass(CharacterClassType.ARCHER);
 private Combatant knightCombatant = new SimpleCombatant(knight);
 private Combatant archerCombatant = new SimpleCombatant(archer);
 private Area area = new SimpleArea();
 private Set<Area> areas = new HashSet<Area>();
 private Set<SectorObserver> observers = new HashSet<SectorObserver>();
 private SimpleSectorObserver observer = new SimpleSectorObserver();

 @Before
 public void initialize() {
 sector = new SimpleSector(10, true, true, 2, 3, true, 5, knight);
 areas.add(area);
 sector.setAreas(areas);
 }

 @Test
 public void testConstructor() {
 assertEquals(10, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertFalse(sector.isOccupied());
 assertNull(sector.getOccupier());
 assertFalse(sector.isFlag());
 assertEquals(knight, sector.getClassType());
 assertNull(sector.getOccupier());
 assertFalse(sector.getOccupierChanged().isEmpty());
 assertNotNull(sector.getAreas());
 assertFalse(sector.getObservers().isEmpty());
 }

 @Test
 public void testFree() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 assertFalse(sector.isOccupied());
 assertNull(sector.getOccupier());
 }

 @Test
 public void testGainControl() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertFalse(sector.isOccupied());
 assertNull(sector.getOccupier());
 sector.gainControl(CombatantSide.ATTACKER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 assertSame(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testGetOccupier() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertSame(CombatantSide.DEFENDER, sector.getOccupier());
 sector.gainControl(CombatantSide.ATTACKER);
 assertSame(CombatantSide.ATTACKER, sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(sector.isOccupied());
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGetHeight() {
 assertEquals(10, sector.getHeight());
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
 SectorBonus bonus = sector.getBonus(knight);
 assertEquals(2, bonus.getAttackBonus());
 assertEquals(3, bonus.getDefenseBonus());
 assertEquals(0, bonus.getDamageBonus());
 }

 @Test
 public void testGetAttackBonus() {
 assertEq(5, sector.getAttackBonus(knight));
 }

 @Test
 public void testGetDefenseBonus() {
 assertEq(8, sector.getDefenseBonus(knight));
 }

 @Test
 public void testClassBonus() {
 assertEq(5, sector.classBonus(knight));
 }

 @Test
 public void testAddObserver() {
 sector.addObserver(observer);
 assertTrue(sector.getObservers().contains(observer));
 }

 @Test
 public void testRemoveObserver() {
 sector.addObserver(observer);
 sector.removeObserver(observer);
 assertFalse(sector.getObservers().contains(observer));
 }

 @Test
 public void testOccupierChanged() {
 SimpleSector sectorCopy = new SimpleSector(10, true, true, 2, 3, true, 5, knight);
 sectorCopy.occupier = CombatantSide.DEFENDER;
 sector.gainControl(CombatantSide.ATTACKER);
 observer.occupierChanged();
 assertTrue(observer.isChanged());
 }
}