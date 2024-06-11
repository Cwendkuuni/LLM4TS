package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.test.TestArea;
import de.outstare.fortbattleplayer.model.impl.test.TestCombatant;
import de.outstare.fortbattleplayer.model.impl.test.TestSectorObserver;

public class SimpleSectorTest {

 private SimpleSector sector;
 private TestArea area;
 private TestCombatant combatant;
 private TestSectorObserver observer;

 @Before
 public void setUp() {
 sector = new SimpleSector(1, true, true, 1, 1, true, 1, CharacterClass.ARCANE);
 area = new TestArea(1, 1);
 combatant = new TestCombatant(CharacterClass.ARCANE);
 observer = new TestSectorObserver();
 sector.addObserver(observer);
 }

 @Test
 public void testConstructor() {
 assertEquals(1, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(1, sector.getAttackBonus(CharacterClass.ARCANE));
 assertEquals(1, sector.getDefenseBonus(CharacterClass.ARCANE));
 assertFalse(sector.isOccupied());
 assertEmpty(sector.getAreas());
 assertEmpty(sector.getObservers());
 }

 @Test
 public void testFree_NoCombatant() {
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 }

 @Test
 public void testFree_OneCombatant() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 }

 @Test
 public void testFree_MultipleCombatants() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 }

 @Test(expected = AssertionError.class)
 public void testFree_NegativeOccupierCount() {
 sector.free();
 sector.free();
 sector.free();
 sector.free();
 sector.free();
 assertFalse(sector.isOccupied());
 sector.free();
 }

 @Test
 public void testOccupierChanged() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(observer.isOccupierChangedCalled());
 }

 @Test
 public void testGainControl_SameSide() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(1, sector.getOccupierCount());
 }

 @Test
 public void testGainControl_DifferentSide() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(1, sector.getOccupierCount());
 }

 @Test
 public void testGetOccupier() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGetHeight() {
 assertEquals(1, sector.getHeight());
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
 public void testGetBonus_SameClass() {
 assertEquals(2, sector.getBonus(CharacterClass.ARCANE).getAttackBonus());
 }

 @Test
 public void testGetBonus_DifferentClass() {
 assertEquals(0, sector.getBonus(CharacterClass.FOOTSOLDIER).getAttackBonus());
 }

 @Test
 public void testGetDamageBonus() {
 assertEquals(0, sector.getDamageBonus());
 sector.addArea(area);
 combatant.equipWeapon(new TestWeapon(SectorBonus.BonusType.DAMAGE, 1));
 area.setOccupier(combatant);
 assertEquals(1, sector.getDamageBonus());
 }

 @Test
 public void testGetAttackBonus() {
 assertEquals(1, sector.getAttackBonus(CharacterClass.ARCANE));
 sector.addArea(area);
 combatant.equipWeapon(new TestWeapon(SectorBonus.BonusType.ATTACK, 1));
 area.setOccupier(combatant);
 assertEquals(2, sector.getAttackBonus(CharacterClass.ARCANE));
 }

 @Test
 public void testGetDefenseBonus() {
 assertEquals(1, sector.getDefenseBonus(CharacterClass.ARCANE));
 sector.addArea(area);
 combatant.equipWeapon(new TestWeapon(SectorBonus.BonusType.DEFENSE, 1));
 area.setOccupier(combatant);
 assertEquals(2, sector.getDefenseBonus(CharacterClass.ARCANE));
 }

 @Test
 public void testClassBonus() {
 assertEquals(1, sector.classBonus(CharacterClass.ARCANE));
 }

 @Test
 public void testClassBonus_DifferentClass() {
 assertEquals(0, sector.classBonus(CharacterClass.FOOTSOLDIER));
 }

 @Test
 public void testGetAreas() {
 assertEquals(0, sector.getAreas().size());
 sector.addArea(area);
 assertEquals(1, sector.getAreas().size());
 }

 @Test
 public void testAddArea() {
 assertEmpty(sector.getAreas());
 sector.addArea(area);
 assertEquals(1, sector.getAreas().size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveArea_EmptySet() {
 sector.removeArea(area);
 }

 @Test
 public void testRemoveArea_OneArea() {
 sector.addArea(area);
 sector.removeArea(area);
 assertEquals(0, sector.getAreas().size());
 }

 @Test
 public void testRemoveArea_MultipleAreas() {
 sector.addArea(area);
 sector.addArea(area);
 sector.removeArea(area);
 assertEquals(1, sector.getAreas().size());
 }

 @Test
 public void testHashCode() {
 Set<Area> areas = new HashSet<>();
 areas.add(area);
 SimpleSector other = new SimpleSector(2, true, true, 1, 1, true, 1, CharacterClass.ARCANE);
 SimpleSector other2 = new SimpleSector(1, true, true, 1, 1, true, 1, CharacterClass.ARCANE);
 other.addArea(areas);
 other2.addArea(areas);
 assertEquals(other.hashCode(), other2.hashCode());
 }

 @Test
 public void testEquals_SameObject() {
 SimpleSector other = sector;
 assertTrue(sector.equals(other));
 }

 @Test
 public void testEquals_DifferentObject() {
 SimpleSector other = new SimpleSector(1, true, true, 1, 1, true, 1, CharacterClass.ARCANE);
 assertFalse(sector.equals(other));
 }

 @Test
 public void testEquals_Null() {
 assertFalse(sector.equals(null));
 }

 @Test
 public void testEquals_DifferentClass() {
 Object other = new Object();
 assertFalse(sector.equals(other));
 }

 @Test
 public void testAddObserver() {
 sector.addObserver(observer);
 assertTrue(observer.isAdded());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddObserver_NullObserver() {
 sector.addObserver(null);
 }

 @Test
 public void testRemoveObserver() {
 sector.addObserver(observer);
 sector.removeObserver(observer);
 assertFalse(observer.isAdded());
 }

 @Test
 public void testRemoveObserver_NonExistingObserver() {
 sector.removeObserver(observer);
 assertFalse(observer.isAdded());
 }
}