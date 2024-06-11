package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SimpleSectorObserver;

public class SimpleSectorTest {

 private SimpleSector sector;
 private Set<Area> areas;
 private Set<SectorObserver> observers;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.HEALER);
 areas = new HashSet<Area>();
 observers = new HashSet<SectorObserver>();
 sector.addObserver(new SimpleSectorObserver() {
 @Override
 public void occupierChanged() {
 // TODO Auto-generated method stub
 }
 });
 }

 @After
 public void tearDown() {
 sector = null;
 areas = null;
 observers = null;
 System.out.println("Test finished.");
 }

 @Test
 public void testSimpleSectorConstruction() {
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 assertTrue(sector.isFlag());
 assertEquals(3, sector.getClassBonus());
 assertEquals(CharacterClass.HEALER, sector.getClassType());
 assertEmpty(sector.getAreas());
 assertEmpty(sector.getObservers());
 }

 @Test
 public void testFree() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(3, sector.getOccupierCount());
 sector.free();
 assertEquals(2, sector.getOccupierCount());
 sector.free();
 assertEquals(1, sector.getOccupierCount());
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 sector.occupierChanged();
 }

 @Test
 public void testOccupierChanged() {
 SimpleSectorObserver observer = new SimpleSectorObserver() {
 @Override
 public void occupierChanged() {
 int sizeBefore = observers.size();
 System.out.println("Observer notified: " + sector.getOccupier());
 }
 };
 sector.addObserver(observer);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(2, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 sector.free();
 assertEquals(1, sector.getOccupierCount());
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 sector.occupierChanged();
 observer.occupierNotified(sector.getOccupier());
 assertEquals(1, sizeBefore + 1);
 }

 @Test
 public void testGainControl() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(1, sector.getOccupierCount());
 assertSame(CombatantSide.DEFENDER, sector.getOccupier());
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(2, sector.getOccupierCount());
 assertSame(CombatantSide.ATTACKER, sector.getOccupier());
 sector.free();
 assertEquals(1, sector.getOccupierCount());
 assertSame(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testGetOccupier() {
 Combatant defender = new Combatant("Defender", CharacterClass.ARCHER, 100);
 Combatant attacker = new Combatant("Attacker", CharacterClass.ARCHER, 100);
 sector.gainControl(CombatantSide.DEFENDER, defender);
 sector.gainControl(CombatantSide.ATTACKER, attacker);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 assertEquals(defender, sector.getOccupier());
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier(CombatantSide.ATTACKER));
 assertEquals(attacker, sector.getOccupier(CombatantSide.ATTACKER));
 }

 @Test
 public void testIsOccupied() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(sector.isOccupied());
 sector.free();
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGetBonus() {
 assertEquals(new SectorBonus(1, 2, 0), sector.getBonus(CharacterClass.ARCHER));
 assertEquals(new SectorBonus(1, 2, 0), sector.getBonus(CharacterClass.HEALER));
 assertEquals(new SectorBonus(1, 2, 3), sector.getBonus(CharacterClass.HEALER));
 }

 @Test
 public void testGetAttackBonus() {
 assertEquals(4, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals(6, sector.getAttackBonus(CharacterClass.HEALER));
 assertEquals(6, sector.getAttackBonus(CharacterClass.HEALER));
 }

 @Test
 public void testGetDefenseBonus() {
 assertEquals(2, sector.getDefenseBonus(CharacterClass.ARCHER));
 assertEquals(5, sector.getDefenseBonus(CharacterClass.HEALER));
 assertEquals(5, sector.getDefenseBonus(CharacterClass.HEALER));
 }

 @Test
 public void testClassBonus() {
 assertEquals(3, sector.classBonus(CharacterClass.HEALER));
 assertEquals(0, sector.classBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetAreas() {
 areas.add(new Area(1, 2));
 areas.add(new Area(2, 3));
 sector._addArea(areas.toArray(new Area[0])[0]);
 sector._addArea(areas.toArray(new Area[0])[1]);
 assertEquals(areas, sector.getAreas());
 }

 @Test
 public void testHashCode() {
 Set<Area> areasTest = new HashSet<Area>(Arrays.asList(new Area(1, 1), new Area(2, 2), new Area(3, 3)));
 SimpleSector sectorTest = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.HEALER);
 SimpleSector sectorTest2 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.HEALER);
 SimpleSector sectorTest3 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
 SimpleSector sectorTest4 = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.HEALER);
 Set<Area> areasTest2 = new HashSet<Area>(Collections.singleton(new Area(1, 1)));
 SimpleSector sectorTest5 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.HEALER);
 SimpleSector sectorTest6 = new SimpleSector(5, false, true, 1, 2, true, 3, CharacterClass.HEALER);
 SimpleSector sectorTest7 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.HEALER);
 SimpleSector sectorTest8 = new SimpleSector(6, true, true, 1, 2, true, 3, CharacterClass.HEALER);
 assertEquals(sectorTest.hashCode(), sectorTest2.hashCode());
 assertNotEquals(sectorTest.hashCode(), sectorTest3.hashCode());
 assertNotEquals(sectorTest.hashCode(), sectorTest4.hashCode());
 assertNotEquals(sectorTest.hashCode(), sectorTest5.hashCode());
 assertNotEquals(sectorTest.hashCode(), sectorTest6.hashCode());
 assertNotEquals(sectorTest.hashCode(), sectorTest7.hashCode());
 assertNotEquals(sectorTest.hashCode(), sectorTest8.hashCode());
 }

 @Test
 public void testEquals() {
 assertTrue(sectorTest.equals(sectorTest2));
 assertFalse(sectorTest.equals(sectorTest3));
 assertFalse(sectorTest.equals(sectorTest4));
 assertFalse(sectorTest.equals(sectorTest5));
 assertFalse(sectorTest.equals(sectorTest6));
 assertFalse(sectorTest.equals(sectorTest7));
 SimpleSector sectorTest9 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.HEALER);
 sectorTest9.setAreas(Collections.singleton(new Area(1, 1)));
 assertFalse(sectorTest.equals(sectorTest9));
 SimpleSector sectorTest10 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.HEALER);
 sectorTest10.setOccupier(CombatantSide.DEFENDER);
 assertFalse(sectorTest.equals(sectorTest10));
 }

 @Test
 public void testAddObserver() {
 sector.addObserver(observers.toArray(new SectorObserver[0])[0]);
 assertEquals(1, sector.getObservers().size());
 }

 @Test
 public void testRemoveObserver() {
 sector.addObserver(observers.toArray(new SectorObserver[0])[0]);
 sector.removeObserver(observers.toArray(new SectorObserver[0])[0]);
 assertEquals(0, sector.getObservers().size());
 }
}