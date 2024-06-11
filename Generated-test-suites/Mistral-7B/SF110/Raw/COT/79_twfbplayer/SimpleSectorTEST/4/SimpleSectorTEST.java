package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.*;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 10, 5, false, 3, CharacterClass.ARCHER);
 }

 @Test
 public void testConstructor() {
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertFalse(sector.isFlag());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEmpty(sector.getAreas());
 assertEmpty(sector.getObservers());
 }

 @Test
 public void testFree() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 sector.free();
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 }

 @Test(expected = AssertionError.class)
 public void testFreeWhenOccupierCountIsNegative() {
 sector.free();
 sector.free();
 sector.free();
 sector.free();
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 sector.free(); // this should throw an AssertionError
 }

 @Test
 public void testOccupierChanged() {
 SectorObserver observer = mock(SectorObserver.class);
 sector.addObserver(observer);
 sector.gainControl(CombatantSide.DEFENDER);
 verify(observer).occupierChanged();
 }

 @Test
 public void testGainControl() {
 CombatantSide previousOccupier = sector.getOccupier();
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 assertFalse(previousOccupier.equals(CombatantSide.DEFENDER));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGainControlWhenSideIsNull() {
 sector.gainControl(null);
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
 }

 @Test
 public void testGetHeight() {
 assertEquals(5, sector.getHeight());
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
 public void testGetAttackBonus() {
 assertEquals(13, sector.getAttackBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDefenseBonus() {
 assertEquals(8, sector.getDefenseBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDamageBonus() {
 assertEquals(0, sector.getDamageBonus());
 }

 @Test
 public void testGetBonusFromWeaponsWhenNoWeaponIsPresent() {
 assertEquals(0, sector.getBonusFromWeapons(SectorBonus.BonusType.ATTACK));
 }

 @Test
 public void testGetBonusFromWeaponsWhenWeaponIsPresent() {
 Area area = mock(Area.class);
 when(area.isOccupied()).thenReturn(true);
 when(area.getOccupier()).thenReturn(new Combatant(CharacterClass.ARCHER, new Weapon(1, 1, 1, 1, 2)));
 sector._addArea(area);
 assertEquals(2, sector.getBonusFromWeapons(SectorBonus.BonusType.ATTACK));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddAreaWhenSetIsFull() {
 for (int i = 0; i < 25; i++) {
 sector._addArea(mock(Area.class));
 }
 sector._addArea(mock(Area.class));
 }

 @Test
 public void testHashCode() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 3, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 3, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(6, true, true, 10, 5, false, 3, CharacterClass.ARCHER);
 assertEquals(sector1.hashCode(), sector2.hashCode());
 assertNotEquals(sector1.hashCode(), sector3.hashCode());
 }

 @Test
 public void testEquals() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 3, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 3, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(6, true, true, 10, 5, false, 3, CharacterClass.ARCHER);
 assertTrue(sector1.equals(sector2));
 assertFalse(sector1.equals(sector3));
 }

 @Test
 public void testAddObserver() {
 SectorObserver observer = mock(SectorObserver.class);
 sector.addObserver(observer);
 verify(observer, times(1)).occupierChanged();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddObserverWhenObserverIsNull() {
 sector.addObserver(null);
 }

 @Test
 public void testRemoveObserver() {
 SectorObserver observer = mock(SectorObserver.class);
 sector.addObserver(observer);
 sector.removeObserver(observer);
 verify(observer, times(1)).occupierChanged();
 verify(observer, never()).occupierChangedAfterRemoval();
 }
}