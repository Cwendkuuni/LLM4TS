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
import de.outstare.fortbattleplayer.model.observer.TestSectorObserver;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Before
 public void setUp() {
 charClass = new CharacterClass("Archmage", new int[] { 10, 15, 5, 20, 25, 30, 35 });
 sector = new SimpleSector(5, true, true, 3, 5, true, 10, charClass);
 }

 @Test
 public void testSimpleSectorConstruction() {
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertFalse(sector.isOccupied());
 assertNull(sector.getOccupier());
 assertFalse(sector.isFlag());
 assertNull(sector.getClassType());
 assertEquals(0, sector.getClassBonus());
 assertNull(sector.getBonus(null));
 assertEquals(0, sector.getAttackBonus(null));
 assertEquals(0, sector.getDefenseBonus(null));
 assertFalse(sector.isOccupied());
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getAreas());
 }

 @Test
 public void testFreeMethod() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEquals(0, sector.getOccupierCount());
 }

 @Test
 public void testGainControlMethod() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertFalse(sector.isOccupied());
 assertEquals(1, sector.getOccupierCount());
 }

 @Test(expected = AssertionError.class)
 public void testGainControlMethodWithAlreadyOccupiedSector() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 }

 @Test
 public void testIsOccupiedMethod() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGetBonusMethod() {
 sector.addArea(new SimpleArea(new SimpleSector(1, false, false, 0, 0, false, null, null), new SimpleCombatant(charClass, new SimpleWeapon(CharacterClass.ARCHER, 5))));
 sector.addArea(new SimpleArea(new SimpleSector(1, false, false, 0, 0, false, null, null), new SimpleCombatant(charClass, new SimpleWeapon(CharacterClass.ARCHER, 5))));
 sector.gainControl(CombatantSide.DEFENDER);
 SimpleCombatant defender = new SimpleCombatant(charClass, new SimpleWeapon(CharacterClass.ARCHER, 5));
 SimpleCombatant attacker = new SimpleCombatant(charClass, new SimpleWeapon(CharacterClass.ARCHER, 5));
 CombatantSide side = CombatantSide.ATTACKER;
 sector.gainControl(side);
 SectorBonus bonus = sector.getBonus(charClass);
 assertEquals(new SectorBonus(8, 8, 5), bonus);
 }

 @Test
 public void testGetAttackBonusMethod() {
 sector.addArea(new SimpleArea(new SimpleSector(1, false, false, 0, 0, false, null, null), new SimpleCombatant(charClass, new SimpleWeapon(CharacterClass.ARCHER, 5))));
 sector.addArea(new SimpleArea(new SimpleSector(1, false, false, 0, 0, false, null, null), new SimpleCombatant(charClass, new SimpleWeapon(CharacterClass.ARCHER, 5))));
 sector.gainControl(CombatantSide.DEFENDER);
 SimpleCombatant defender = new SimpleCombatant(charClass, new SimpleWeapon(CharacterClass.ARCHER, 5));
 SimpleCombatant attacker = new SimpleCombatant(charClass, new SimpleWeapon(CharacterClass.ARCHER, 5));
 CombatantSide side = CombatantSide.ATTACKER;
 sector.gainControl(side);
 int attackBonus = sector.getAttackBonus(charClass);
 assertEquals(13, attackBonus);
 }

 @Test
 public void testGetDefenseBonusMethod() {
 sector.addArea(new SimpleArea(new SimpleSector(1, false, false, 0, 0, false, null, null), new SimpleCombatant(charClass, new SimpleWeapon(CharacterClass.ARCHER, 5))));
 sector.addArea(new SimpleArea(new SimpleSector(1, false, false, 0, 0, false, null, null), new SimpleCombatant(charClass, new SimpleWeapon(CharacterClass.ARCHER, 5))));
 sector.gainControl(CombatantSide.DEFENDER);
 SimpleCombatant defender = new SimpleCombatant(charClass, new SimpleWeapon(CharacterClass.ARCHER, 5));
 SimpleCombatant attacker = new SimpleCombatant(charClass, new SimpleWeapon(CharacterClass.ARCHER, 5));
 CombatantSide side = CombatantSide.ATTACKER;
 sector.gainControl(side);
 int defenseBonus = sector.getDefenseBonus(charClass);
 assertEquals(13, defenseBonus);
 }

 @Test
 public void testAddObserverMethod() {
 TestSectorObserver observer = new TestSectorObserver();
 sector.addObserver(observer);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertTrue(observer.isOccupierChangedCalled());
 }

 @Test
 public void testRemoveObserverMethod() {
 TestSectorObserver observer = new TestSectorObserver();
 sector.addObserver(observer);
 sector.addObserver(new TestSectorObserver());
 sector.removeObserver(observer);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertFalse(observer.isOccupierChangedCalled());
 }

 @Test
 public void testHashCodeMethod() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 3, 5, true, 10, charClass);
 SimpleSector sector2 = new SimpleSector(5, true, true, 3, 5, true, 10, charClass);
 SimpleSector sector3 = new SimpleSector(5, true, true, 3, 6, true, 10, charClass);
 assertEquals(sector1.hashCode(), sector1.hashCode());
 assertEquals(sector1.hashCode(), sector2.hashCode());
 assertNotEquals(sector1.hashCode(), sector3.hashCode());
 }

 @Test
 public void testEqualsMethod() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 3, 5, true, 10, charClass);
 SimpleSector sector2 = new SimpleSector(5, true, true, 3, 5, true, 10, charClass);
 SimpleSector sector3 = new SimpleSector(5, true, true, 3, 6, true, 10, charClass);
 SimpleSector sector4 = new SimpleSector(5, true, true, 3, 5, true, 11, charClass);
 SimpleSector sector5 = new SimpleSector(6, true, true, 3, 5, true, 10, charClass);
 assertTrue(sector1.equals(sector1));
 assertTrue(sector1.equals(sector2));
 assertFalse(sector1.equals(sector3));
 assertFalse(sector1.equals(sector4));
 assertFalse(sector1.equals(sector5));
 }

 @Test
 public void testAddObserverMethodWithNullObserver() {
 TestSectorObserver observer = null;
 sector.addObserver(observer);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddObserverMethodWithNullSector() {
 TestSectorObserver observer = new TestSectorObserver();
 SimpleSector sector = null;
 sector.addObserver(observer);
 }
}

class SimpleArea {
 private final Sector sector;
 private final Combatant combatant;

 public SimpleArea(Sector sector, Combatant combatant) {
 this.sector = sector;
 this.combatant = combatant;
 }

 public Sector getSector() {
 return sector;
 }

 public Combatant getOccupier() {
 return combatant;
 }
}

class SimpleCombatant {
 private final CharacterClass characterClass;
 private final Weapon weapon;

 public SimpleCombatant(CharacterClass characterClass, Weapon weapon) {
 this.characterClass = characterClass;
 this.weapon = weapon;
 }

 public CharacterClass getCharacterClass() {
 return characterClass;
 }

 public Weapon getWeapon() {
 return weapon;
 }
}

class SimpleWeapon {
 private final CharacterClass characterClass;
 private final int damage;

 public SimpleWeapon(CharacterClass characterClass, int damage) {
 this.characterClass = characterClass;
 this.damage = damage;
 }

 public CharacterClass getCharacterClass() {
 return characterClass;
 }

 public int getSectorBonus(SectorBonus.BonusType type) {
 return 5;
 }
}

class TestSectorObserver {
 private boolean occupierChangedCalled = false;

 public void occupierChanged() {
 occupierChangedCalled = true;
 }

 public boolean isOccupierChangedCalled() {
 return occupierChangedCalled;
 }
}