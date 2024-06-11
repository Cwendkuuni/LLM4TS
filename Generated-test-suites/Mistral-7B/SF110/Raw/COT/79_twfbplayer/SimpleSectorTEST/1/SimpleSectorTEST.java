package de.outstare.fortbattleplayer.model.impl.test;

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
import de.outstare.fortbattleplayer.model.impl.Weapon;
import de.outstare.fortbattleplayer.model.observer.SectorObserver;

public class SimpleSectorTest {

 private SimpleSector sector;
 private Set<Area> areas;
 private SectorObserver observer;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, false, 5, 5, false, 5, CharacterClass.ARCHER);
 areas = new HashSet<>();
 observer = new SectorObserver() {
 @Override
 public void occupierChanged() {
 // empty
 }
 };
 }

 @Test
 public void testSimpleSectorConstructor() {
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertFalse(sector.isAttackerSpawn());
 assertEquals(5, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals(5, sector.getDefenseBonus(CharacterClass.ARCHER));
 assertFalse(sector.isFlag());
 assertEquals(CharacterClass.ARCHER, sector.getClassType());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEmpty(sector.getAreas());
 }

 @Test
 public void testFree() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(1, sector.getOccupierCount());
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGainControl() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(1, sector.getOccupierCount());
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test(expected = AssertionError.class)
 public void testFreeWhenOccupiedButNoCombatantLeft() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 }

 @Test
 public void testOccupierChanged() {
 sector.addObserver(observer);
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(observer.isOccupierChangedCalled());
 }

 @Test
 public void testGetBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 int bonus = sector.getBonus(charClass).getAttackBonus();
 assertEquals(10, bonus); // 5 from constructor, 5 from weapon
 }

 @Test
 public void testGetDamageBonus() {
 assertEquals(0, sector.getDamageBonus());
 Area area = new Area();
 Combatant combatant = new Combatant(CharacterClass.ARCHER, new Weapon());
 area.setOccupier(combatant);
 sector.addArea(area);
 int bonus = sector.getDamageBonus();
 assertEquals(combatant.getWeapon().getSectorBonus(SectorBonus.BonusType.DAMAGE), bonus);
 }

 @Test
 public void testGetAttackBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 int bonus = sector.getAttackBonus(charClass);
 assertEquals(10, bonus); // 5 from constructor, 5 from weapon
 }

 @Test
 public void testGetDefenseBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 int bonus = sector.getDefenseBonus(charClass);
 assertEquals(10, bonus); // 5 from constructor, 5 from weapon
 }

 @Test
 public void testClassBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 assertEquals(5, sector.classBonus(charClass));
 assertEquals(CharacterClass.ARCHER, sector.getClassType());
 CharacterClass charClass2 = CharacterClass.KNIGHT;
 assertEquals(0, sector.classBonus(charClass2));
 }

 @Test
 public void testHashCodeAndEquals() {
 SimpleSector sector1 = new SimpleSector(5, true, false, 5, 5, false, 5, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, false, 5, 5, false, 5, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, false, 6, 5, false, 5, CharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(5, true, false, 5, 6, false, 5, CharacterClass.ARCHER);
 SimpleSector sector5 = new SimpleSector(5, true, false, 5, 5, true, 5, CharacterClass.ARCHER);
 Set<SimpleSector> sectors = new HashSet<>(Arrays.asList(sector1, sector2, sector3, sector4, sector5));
 assertTrue(sectors.remove(sector1));
 assertTrue(sectors.remove(sector2));
 assertFalse(sectors.remove(sector3));
 assertFalse(sectors.remove(sector4));
 assertFalse(sectors.remove(sector5));
 }

 @Test
 public void testAddObserverAndRemoveObserver() {
 sector.addObserver(observer);
 observer.occupierChanged();
 assertTrue(observer.isOccupierChangedCalled());
 sector.removeObserver(observer);
 observer.occupierChanged();
 assertFalse(observer.isOccupierChangedCalled());
 }
}