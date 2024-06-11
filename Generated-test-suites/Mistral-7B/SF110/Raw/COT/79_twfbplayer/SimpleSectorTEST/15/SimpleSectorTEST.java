package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.test.dummies.DummyCharacterClass;
import de.outstare.fortbattleplayer.model.impl.test.dummies.DummyCombatant;
import de.outstare.fortbattleplayer.model.impl.test.dummies.DummySectorObserver;
import de.outstare.fortbattleplayer.model.impl.test.dummies.DummyWeapon;
import de.outstare.fortbattleplayer.model.impl.test.dummies.DummyArea;

public class SimpleSectorTest {

 private SimpleSector sector;
 private Set<Area> areas;
 private Set<SectorObserver> observers;

 @Before
 public void setUp() {
 this.sector = new SimpleSector(5, true, true, 2, 1, true, 3, DummyCharacterClass.BOWMAN);
 this.areas = new HashSet<>();
 this.observers = new HashSet<>();
 }

 @Test
 public void testSimpleSectorConstructor() {
 assertEquals(5, this.sector.getHeight());
 assertTrue(this.sector.isDefenderSpawn());
 assertTrue(this.sector.isAttackerSpawn());
 assertEquals(2, this.sector.getAttackBonus(DummyCharacterClass.BOWMAN));
 assertEquals(1, this.sector.getDefenseBonus(DummyCharacterClass.BOWMAN));
 assertNull(this.sector.getOccupier());
 assertFalse(this.sector.isOccupied());
 assertEquals(0, this.sector.getOccupierCount());
 assertEmpty(this.sector.getAreas());
 assertEmpty(this.sector.getObservers());
 }

 @Test
 public void testFree() {
 this.sector.gainControl(CombatantSide.DEFENDER);
 this.sector.free();
 assertNull(this.sector.getOccupier());
 assertFalse(this.sector.isOccupied());
 assertEquals(0, this.sector.getOccupierCount());
 }

 @Test(expected = AssertionError.class)
 public void testFreeWithNegativeOccupierCount() {
 this.sector.gainControl(CombatantSide.DEFENDER);
 this.sector.free();
 this.sector.free();
 assertThrows(AssertionError.class, () -> this.sector.free());
 }

 @Test
 public void testOccupierChanged() {
 final DummySectorObserver observer = new DummySectorObserver();
 this.sector.addObserver(observer);
 this.sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(observer.isOccupierChangedCalled());
 }

 @Test
 public void testGainControl() {
 assertFalse(this.sector.isOccupied());
 this.sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(this.sector.isOccupied());
 assertSame(CombatantSide.DEFENDER, this.sector.getOccupier());
 assertEquals(1, this.sector.getOccupierCount());
 }

 @Test(expected = IllegalStateException.class)
 public void testGainControlWithOccupiedSector() {
 this.sector.gainControl(CombatantSide.DEFENDER);
 this.sector.gainControl(CombatantSide.ATTACKER);
 assertThrows(IllegalStateException.class, () -> this.sector.gainControl(CombatantSide.DEFENDER));
 }

 @Test
 public void testGetOccupier() {
 this.sector.gainControl(CombatantSide.DEFENDER);
 assertSame(CombatantSide.DEFENDER, this.sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 this.sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(this.sector.isOccupied());
 }

 @Test
 public void testGetHeight() {
 assertEquals(5, this.sector.getHeight());
 }

 @Test
 public void testIsDefenderSpawn() {
 assertTrue(this.sector.isDefenderSpawn());
 }

 @Test
 public void testIsAttackerSpawn() {
 assertTrue(this.sector.isAttackerSpawn());
 }

 @Test
 public void testIsFlag() {
 assertTrue(this.sector.isFlag());
 }

 @Test
 public void testGetBonus() {
 final CharacterClass charClass = DummyCharacterClass.BOWMAN;
 final SectorBonus expected = new SectorBonus(4, 1, 5);
 assertEquals(expected, this.sector.getBonus(charClass));
 }

 @Test
 public void testGetAttackBonus() {
 final CharacterClass charClass = DummyCharacterClass.BOWMAN;
 final int expected = 4;
 assertEquals(expected, this.sector.getAttackBonus(charClass));
 }

 @Test
 public void testGetDefenseBonus() {
 final CharacterClass charClass = DummyCharacterClass.BOWMAN;
 final int expected = 1;
 assertEquals(expected, this.sector.getDefenseBonus(charClass));
 }

 @Test
 public void testClassBonus() {
 final CharacterClass charClass = DummyCharacterClass.BOWMAN;
 final int expected = 3;
 assertEquals(expected, this.sector.classBonus(charClass));
 }

 @Test
 public void testGetAreas() {
 assertEmpty(this.sector.getAreas());
 }

 @Test
 public void testAddArea() {
 final DummyArea area = new DummyArea();
 this.sector._addArea(area);
 assertFalse(this.sector.getAreas().isEmpty());
 }

 @Test
 public void testHashCode() {
 final SimpleSector sector2 = new SimpleSector(5, true, true, 2, 1, true, 3, DummyCharacterClass.BOWMAN);
 assertEquals(this.sector.hashCode(), sector2.hashCode());
 }

 @Test
 public void testEquals() {
 final SimpleSector sector2 = new SimpleSector(5, true, true, 2, 1, true, 3, DummyCharacterClass.BOWMAN);
 assertTrue(this.sector.equals(sector2));
 }

 @Test
 public void testEqualsWithDifferentClassType() {
 final SimpleSector sector2 = new SimpleSector(5, true, true, 2, 1, true, 3, DummyCharacterClass.ARCHER);
 assertFalse(this.sector.equals(sector2));
 }

 @Test
 public void testEqualsWithDifferentHeight() {
 final SimpleSector sector2 = new SimpleSector(6, true, true, 2, 1, true, 3, DummyCharacterClass.BOWMAN);
 assertFalse(this.sector.equals(sector2));
 }

 @Test
 public void testEqualsWithDifferentDefenderSpawn() {
 final SimpleSector sector2 = new SimpleSector(5, false, true, 2, 1, true, 3, DummyCharacterClass.BOWMAN);
 assertFalse(this.sector.equals(sector2));
 }

 @Test
 public void testEqualsWithDifferentAttackerSpawn() {
 final SimpleSector sector2 = new SimpleSector(5, true, false, 2, 1, true, 3, DummyCharacterClass.BOWMAN);
 assertFalse(this.sector.equals(sector2));
 }

 @Test
 public void testEqualsWithDifferentFlag() {
 final SimpleSector sector2 = new SimpleSector(5, true, true, 2, 1, false, 3, DummyCharacterClass.BOWMAN);
 assertFalse(this.sector.equals(sector2));
 }

 @Test
 public void testEqualsWithDifferentAreas() {
 final SimpleSector sector2 = new SimpleSector(5, true, true, 2, 1, true, 3, DummyCharacterClass.BOWMAN);
 final DummyArea area = new DummyArea();
 sector2._addArea(area);
 assertFalse(this.sector.equals(sector2));
 }

 @Test
 public void testAddObserver() {
 this.sector.addObserver(new DummySectorObserver());
 assertTrue(this.sector.getObservers().contains(new DummySectorObserver()));
 }

 @Test
 public void testRemoveObserver() {
 this.sector.addObserver(new DummySectorObserver());
 this.sector.removeObserver(new DummySectorObserver());
 assertFalse(this.sector.getObservers().contains(new DummySectorObserver()));
 }

 @Test
 public void testGetObservers() {
 final Set<SectorObserver> expected = new HashSet<>(Arrays.asList(new DummySectorObserver(), new DummySectorObserver()));
 assertEquals(expected, this.sector.getObservers());
 }
}