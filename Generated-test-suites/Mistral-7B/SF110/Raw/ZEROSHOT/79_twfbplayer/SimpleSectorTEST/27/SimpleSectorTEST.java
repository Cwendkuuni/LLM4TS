package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SimpleWeapon;
import de.outstare.fortbattleplayer.model.observer.SectorObserver;

public class SimpleSectorTest {

 private Sector sector;
 private Set<Area> areas;
 private Set<SectorObserver> observers;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 areas = new HashSet<>();
 observers = new HashSet<>();
 }

 @Test
 public void testSimpleSectorInitialization() {
 // check fields
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(2, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals(3, sector.getDefenseBonus(CharacterClass.ARCHER));
 assertFalse(sector.isFlag());
 assertEquals(4, sector.getClassBonus());
 assertEquals(CharacterClass.ARCHER, sector.getClassType());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEquals(0, sector.getOccupierCount());
 assertEmpty(sector.getAreas());
 assertEmpty(sector.getObservers());
 }

 @Test
 public void testFreeMethod() {
 // test free method with combatant
 Combatant combatant = new Combatant("testCombatant", CharacterClass.ARCHER, new SimpleWeapon("testWeapon", 5, 5));
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER, combatant);
 sector.free();
 assertTrue(sector.getOccupierCount() == 0);
 assertNull(sector.getOccupier());
 // test free method with no combatant
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertTrue(sector.getOccupierCount() == 0);
 assertNull(sector.getOccupier());
 }

 @Test
 public void testOccupierChangedMethod() {
 SectorObserver sectorObserver = new SectorObserver() {
 @Override
 public void occupierChanged() {
 System.out.println("Occupier changed in sector!");
 }
 };
 sector.addObserver(sectorObserver);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(sectorObserver.isOccupierChangedCalled());
 }

 @Test
 public void testGainControlMethod() {
 // test gain control with combatant and empty sector
 Combatant combatant = new Combatant("testCombatant", CharacterClass.ARCHER, new SimpleWeapon("testWeapon", 5, 5));
 sector.gainControl(CombatantSide.DEFENDER, combatant);
 assertTrue(sector.getOccupier() == CombatantSide.DEFENDER && sector.getOccupierCount() == 1);
 // test gain control with combatant and occupied sector by another combatant
 Combatant combatant2 = new Combatant("testCombatant2", CharacterClass.ARCHER, new SimpleWeapon("testWeapon2", 5, 5));
 sector.gainControl(CombatantSide.ATTACKER, combatant2);
 sector.gainControl(CombatantSide.DEFENDER, combatant);
 assertTrue(sector.getOccupier() == CombatantSide.DEFENDER && sector.getOccupierCount() == 2);
 // test gain control with combatant and occupied sector by same side
 sector.gainControl(CombatantSide.DEFENDER, combatant);
 sector.gainControl(CombatantSide.DEFENDER, combatant2);
 assertTrue(sector.getOccupier() == CombatantSide.DEFENDER && sector.getOccupierCount() == 2);
 }

 @Test
 public void testGetOccupierMethod() {
 Combatant combatant = new Combatant("testCombatant", CharacterClass.ARCHER, new SimpleWeapon("testWeapon", 5, 5));
 sector.gainControl(CombatantSide.DEFENDER, combatant);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testIsOccupiedMethod() {
 Combatant combatant = new Combatant("testCombatant", CharacterClass.ARCHER, new SimpleWeapon("testWeapon", 5, 5));
 sector.gainControl(CombatantSide.DEFENDER, combatant);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testGetBonusMethod() {
 assertEquals(new SectorBonus(4, 5, 6), sector.getBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDamageBonusMethod() {
 assertEquals(6, sector.getDamageBonus());
 }

 @Test
 public void testGetAttackBonusMethod() {
 assertEquals(6, sector.getAttackBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDefenseBonusMethod() {
 assertEquals(5, sector.getDefenseBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testClassBonusMethod() {
 assertEquals(4, sector.classBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetAreasMethod() {
 areas.add(new SimpleArea(1, 1));
 sector._addArea(areas.iterator().next());
 assertEquals(areas, sector.getAreas());
 }

 @Test
 public void testHashCodeMethod() {
 SimpleSector simpleSector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 SimpleSector simpleSector2 = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 SimpleSector simpleSector3 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
 Set<Sector> sectors = new HashSet<>();
 sectors.add(simpleSector);
 sectors.add(simpleSector2);
 sectors.add(simpleSector3);
 assertTrue(sectors.contains(sector));
 assertTrue(sectors.contains(simpleSector));
 assertEquals(sector.hashCode(), simpleSector.hashCode());
 }

 @Test
 public void testEqualsMethod() {
 SimpleSector simpleSector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 SimpleSector simpleSector2 = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 SimpleSector simpleSector3 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
 SimpleSector simpleSector4 = new SimpleSector(6, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
 assertTrue(simpleSector.equals(simpleSector2));
 assertFalse(simpleSector.equals(simpleSector3));
 assertFalse(simpleSector.equals(simpleSector4));
 }

 @Test
 public void testAddObserverMethod() {
 sector.addObserver(new SectorObserver() {
 @Override
 public void occupierChanged() {
 System.out.println("Occupier changed in sector!");
 }
 });
 assertTrue(sector.getObservers().contains(new SectorObserver() {
 @Override
 public void occupierChanged() {
 System.out.println("Occupier changed in sector!");
 }
 }));
 }

 @Test
 public void testRemoveObserverMethod() {
 sector.addObserver(new SectorObserver() {
 @Override
 public void occupierChanged() {
 System.out.println("Occupier changed in sector!");
 }
 });
 sector.removeObserver(new SectorObserver() {
 @Override
 public void occupierChanged() {
 System.out.println("Occupier changed in sector!");
 }
 });
 assertFalse(sector.getObservers().contains(new SectorObserver() {
 @Override
 public void occupierChanged() {
 System.out.println("Occupier changed in sector!");
 }
 }));
 }
}