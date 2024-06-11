package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.CharacterClassImpl;
import de.outstare.fortbattleplayer.model.impl.WeaponImpl;

public class SimpleSectorTest {

 private SimpleSector sector;
 private Set<Area> areas;
 private Set<SectorObserver> observers;
 private CharacterClass attackerClass, defenderClass;
 private Area area;
 private Combatant attacker, defender;
 private Weapon weapon;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClassImpl.ARCHER);
 areas = new HashSet<>();
 observers = new HashSet<>();
 attackerClass = CharacterClassImpl.ARCHER;
 defenderClass = CharacterClassImpl.KNIGHT;
 area = new SimpleArea(1, 1);
 attacker = new SimpleCombatant("Attacker", attackerClass);
 defender = new SimpleCombatant("Defender", defenderClass);
 weapon = new SimpleWeapon("Sword", 5);
 }

 @Test
 public void testSimpleSectorConstruction() {
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertFalse(sector.isOccupied());
 assertFalse(sector.isFlag());
 assertNull(sector.getOccupier());
 assertNull(sector.getBonus(attackerClass));
 assertNull(sector.getBonus(defenderClass));
 assertNotNull(sector.getAreas());
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testFree() {
 sector.gainControl(CombatantSide.ATTACKER);
 sector.free();
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testOccupierChanged() {
 sector.gainControl(CombatantSide.ATTACKER);
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(1, sector.getOccupierCount());
 assertTrue(sector.isOccupied());
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testAddArea() {
 sector._addArea(area);
 assertEquals(1, sector.getAreas().size());
 }

 @Test
 public void testHashcodeAndEquals() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClassImpl.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClassImpl.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 1, 2, false, 4, CharacterClassImpl.ARCHER);
 SimpleSector sector4 = new SimpleSector(5, false, true, 1, 2, false, 3, CharacterClassImpl.ARCHER);
 Set<SimpleSector> sectors = new HashSet<SimpleSector>(Arrays.asList(sector1, sector2, sector3, sector4));
 assertTrue(sectors.contains(sector1));
 assertTrue(sectors.contains(sector2));
 assertFalse(sectors.contains(sector3));
 assertFalse(sectors.contains(sector4));
 }

 @Test
 public void testAddObserver() {
 sector.addObserver(new SimpleSectorObserver() {
 @Override public void occupierChanged() {
 // do nothing
 }
 });
 assertEquals(1, sector.getObservers().size());
 }

 @Test
 public void testRemoveObserver() {
 sector.addObserver(new SimpleSectorObserver() {
 @Override public void occupierChanged() {
 // do nothing
 }
 });
 sector.removeObserver(new SimpleSectorObserver() {
 @Override public void occupierChanged() {
 // do nothing
 }
 });
 assertEquals(0, sector.getObservers().size());
 }

 @Test
 public void testGetBonus() {
 area.setOccupier(attacker);
 sector._addArea(area);
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(new SectorBonus(4, 2, 5), sector.getBonus(attackerClass));
 area.setOccupier(defender);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(new SectorBonus(2, 4, 0), sector.getBonus(defenderClass));
 }

 @Test
 public void testGetDamageBonus() {
 assertEquals(5, sector.getDamageBonus());
 }

 @Test
 public void testGetAttackBonus() {
 assertEquals(4, sector.getAttackBonus(attackerClass));
 }

 @Test
 public void testGetDefenseBonus() {
 assertEquals(2, sector.getDefenseBonus(defenderClass));
 }

 @Test
 public void testClassBonus() {
 assertEquals(3, sector.classBonus(attackerClass));
 }
}