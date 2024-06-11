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
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.Weapon;

public class SimpleSectorTest {

 private Sector sector;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 }

 @Test
 public void testSimpleSectorConstruction() {
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertFalse(sector.isFlag());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEquals(0, sector.getClassBonus());
 assertSame(CharacterClass.ARCHER, sector.getClassType());
 assertNotNull(sector.getAreas());
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testFree() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGainControl() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 assertSame(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test(expected = AssertionError.class)
 public void testOccupierChangedWhenNoCombatants() {
 sector.occupierChanged();
 }

 @Test
 public void testOccupierChangedWhenOneCombatant() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 sector.occupierChanged();
 }

 @Test
 public void testOccupierChangedWhenMultipleCombatants() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 sector.occupierChanged();
 }

 @Test
 public void testGetBonus() {
CharacterClass charClass = CharacterClass.ARCHER;
 SectorBonus bonus = sector.getBonus(charClass);
 assertEquals(1, bonus.getAttackBonus());
 assertEquals(1, bonus.getDefenseBonus());
 assertEquals(0, bonus.getDamageBonus());
 }

 @Test
 public void testGetAttackBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 int attackBonus = sector.getAttackBonus(charClass);
 assertEquals(2, attackBonus);
 }

 @Test
 public void testGetDefenseBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 int defenseBonus = sector.getDefenseBonus(charClass);
 assertEquals(2, defenseBonus);
 }

 @Test
 public void testGetDamageBonus() {
 int damageBonus = sector.getDamageBonus();
 assertEquals(0, damageBonus);
 }

 @Test
 public void testGetBonusFromWeapons() {
 SectorBonus bonus = new SectorBonus(1, 1, 1);
 Weapon weapon = new Weapon("Sword", 5, bonus);
 Area area = new Area(new Coordinate(1, 1), new SimpleArea(5, 5), weapon);
 sector.addObserver(new SectorObserver() {
 @Override
 public void occupierChanged() {
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 }
 });
 sector._addArea(area);
 sector.gainControl(CombatantSide.ATTACKER);
 sector.occupierChanged();
 int bonusFromWeapons = sector.getBonusFromWeapons(BonusType.ATTACK);
 assertEquals(1, bonusFromWeapons);
 }

 @Test
 public void testClassBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 int classBonus = sector.classBonus(charClass);
 assertEquals(0, classBonus);
 }

 @Test
 public void testHashCode() {
 Set<Area> areas = new HashSet<>();
 areas.add(new Area(new Coordinate(1, 1), new SimpleArea(5, 5), null));
 SimpleSector sector1 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector5 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.KNIGHT);
 SimpleSector sector6 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector7 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector8 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector9 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector10 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector11 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector12 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector13 = new SimpleSector(5, true, true, 1, 1, false, 0, null);
 Set<SimpleSector> sectors = new HashSet<>(Arrays.asList(sector1, sector2, sector3, sector4, sector5, sector6, sector7, sector8, sector9, sector10, sector11, sector12, sector13));
 int hashCode = sectors.hashCode();
 assertEquals(13, hashCode);
 }

 @Test
 public void testEquals() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector5 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector6 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector7 = new SimpleSector(5, true, true, 1, 1, false, 0, CharacterClass.ARCHER);
 SimpleSector sector8 = new SimpleSector(5, true, true, 1, 1, false, 0, null);
 assertTrue(sector1.equals(sector2));
 assertTrue(sector1.equals(sector3));
 assertTrue(sector1.equals(sector4));
 assertFalse(sector1.equals(sector5));
 assertFalse(sector1.equals(sector6));
 assertFalse(sector1.equals(sector7));
 assertFalse(sector1.equals(sector8));
 }

 @Test
 public void testAddObserver() {
 sector.addObserver(new SectorObserver() {
 @Override
 public void occupierChanged() {
 fail();
 }
 });
 sector.occupierChanged();
 }

 @Test
 public void testRemoveObserver() {
 sector.addObserver(new SectorObserver() {
 @Override
 public void occupierChanged() {
 fail();
 }
 });
 sector.removeObserver(new SectorObserver() {
 @Override
 public void occupierChanged() {
 fail();
 }
 });
 sector.occupierChanged();
 }
}