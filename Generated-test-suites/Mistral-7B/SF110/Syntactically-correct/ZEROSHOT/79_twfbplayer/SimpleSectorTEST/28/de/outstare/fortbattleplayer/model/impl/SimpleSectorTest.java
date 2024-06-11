package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.BonusType;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SectorObserver;
import de.outstare.fortbattleplayer.model.impl.TestWeapon;

public class SimpleSectorTest {

 private SimpleSector sector;
 private Area area;
 private Weapon weapon;
 private CharacterClass charClass;

 @Before
 public void setUp() {
 charClass = CharacterClass.ARCHER;
 weapon = new TestWeapon();
 weapon.setSectorBonus(BonusType.ATTACK, 5);
 sector = new SimpleSector(5, true, true, 1, 1, true, 2, charClass);
 area = new Area();
 area.setCombatant(new Combatant(CombatantSide.ATTACKER, charClass, null));
 area.setOccupier(null);
 area.addWeapon(weapon);
 sector._addArea(area);
 }

 @Test
 public void testHeight() {
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
 assertTrue(sector.isFlag());
 }

 @Test
 public void testGetBonus() {
 SectorBonus bonus = sector.getBonus(charClass);
 assertEquals(3, bonus.getAttackBonus());
 assertEquals(3, bonus.getDefenseBonus());
 assertEquals(5, bonus.getDamageBonus());
 }

 @Test
 public void testGetAttackBonus() {
 int attackBonus = sector.getAttackBonus(charClass);
 assertEquals(3, attackBonus);
 }

 @Test
 public void testGetDefenseBonus() {
 int defenseBonus = sector.getDefenseBonus(charClass);
 assertEquals(3, defenseBonus);
 }

 @Test
 public void testFree() {
 sector.free();
 assertNull(sector.getOccupier());
 assertEquals(0, sector.getOccupierCount());
 }

 @Test(expected = AssertionError.class)
 public void testFreeWhenOccupierCountIsNegative() {
 sector.free();
 sector.free();
 sector.free();
 sector.free();
 sector.free();
 assertNotNull(sector.getOccupier());
 sector.free();
 }

 @Test
 public void testGainControl() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 assertEquals(1, sector.getOccupierCount());
 }

 @Test
 public void testGainControlWhenSameSide() {
 sector.gainControl(CombatantSide.ATTACKER);
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(sector.isOccupied());
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 assertEquals(2, sector.getOccupierCount());
 }

 @Test
 public void testGetOccupier() {
 assertTrue(sector.isOccupied());
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier().getSide());
 }

 @Test
 public void testIsOccupied() {
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testAddObserver() {
 SectorObserver observer = new SectorObserver() {
 @Override public void occupierChanged() {
 fail();
 }
 };
 sector.addObserver(observer);
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 assertTrue(sector.getOccupier().getSide() == CombatantSide.DEFENDER);
 assertEquals(1, sector.getOccupierCount());
 assertTrue(observer.isOccupierChangedCalled());
 }

 @Test
 public void testRemoveObserver() {
 SectorObserver observer = new SectorObserver() {
 @Override public void occupierChanged() {
 fail();
 }
 };
 sector.addObserver(observer);
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 assertTrue(sector.getOccupier().getSide() == CombatantSide.DEFENDER);
 assertEquals(1, sector.getOccupierCount());
 sector.removeObserver(observer);
 sector.gainControl(CombatantSide.ATTACKER);
 assertFalse(sector.isOccupied());
 assertNull(sector.getOccupier());
 assertEquals(0, sector.getOccupierCount());
 assertFalse(observer.isOccupierChangedCalled());
 }

 @Test
 public void testHashCode() {
 Set<Area> areas = new HashSet<Area>(Arrays.asList(area));
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 1, true, 2, charClass);
 sector2._addArea(area);
 assertEquals(sector.hashCode(), sector2.hashCode());
 }

 @Test
 public void testEquals() {
 Set<Area> areas = new HashSet<Area>(Arrays.asList(area));
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 1, true, 2, charClass);
 sector2._addArea(area);
 assertTrue(sector.equals(sector2));
 }

 @Test
 public void testEqualsWhenDifferentAreas() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 1, true, 2, charClass);
 assertFalse(sector.equals(sector2));
 }

 @Test
 public void testEqualsWhenDifferentFlag() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 1, false, 2, charClass);
 assertFalse(sector.equals(sector2));
 }

 @Test
 public void testEqualsWhenDifferentDefenderSpawn() {
 SimpleSector sector2 = new SimpleSector(5, false, true, 1, 1, true, 2, charClass);
 assertFalse(sector.equals(sector2));
 }

 @Test
 public void testEqualsWhenDifferentAttackerSpawn() {
 SimpleSector sector2 = new SimpleSector(5, true, false, 1, 1, true, 2, charClass);
 assertFalse(sector.equals(sector2));
 }

 @Test
 public void testEqualsWhenDifferentHeight() {
 SimpleSector sector2 = new SimpleSector(6, true, true, 1, 1, true, 2, charClass);
 assertFalse(sector.equals(sector2));
 }

 @Test
 public void testEqualsWhenDifferentClassBonus() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 1, true, 3, charClass);
 assertFalse(sector.equals(sector2));
 }
}