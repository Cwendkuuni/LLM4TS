package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SimpleSectorTest.TestCharacterClass;
import de.outstare.fortbattleplayer.model.impl.SimpleWeapon;

public class SimpleSectorTest {

 private SimpleSector sector;
 private Area area;
 private Combatant combatant;
 private SectorObserver sectorObserver;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 1, 1, false, 2, CharacterClass.ARCHER);
 area = new SimpleArea(1, 1);
 combatant = new SimpleCombatant(new TestCharacterClass("test"));
 sectorObserver = new SimpleSectorObserver();
 }

 @Test
 public void testFree() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 sector.gainControl(CombatantSide.ATTACKER);
 sector.gainControl(CombatantSide.ATTACKER);
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.getOccupier() == CombatantSide.DEFENDER);
 sector.free();
 assertNull(sector.getOccupier());
 }

 @Test
 public void testOccupierChanged() {
 sector.addObserver(sectorObserver);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(sectorObserver.isOccupierChangedCalled());
 }

 @Test
 public void testGainControl() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertNull(sector.getOccupier());
 sector.gainControl(CombatantSide.DEFENDER);
 assertSame(CombatantSide.DEFENDER, sector.getOccupier());
 sector.gainControl(CombatantSide.ATTACKER);
 assertSame(CombatantSide.ATTACKER, sector.getOccupier());
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
 public void testGetBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 SectorBonus bonus = sector.getBonus(charClass);
 assertEquals(3, bonus.getAttackBonus());
 assertEquals(2, bonus.getDefenseBonus());
 assertEquals(0, bonus.getDamageBonus());
 }

 @Test
 public void testGetAttackBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 assertEquals(2, sector.getAttackBonus(charClass));
 }

 @Test
 public void testGetDefenseBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 assertEquals(1, sector.getDefenseBonus(charClass));
 }

 @Test
 public void testClassBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 assertEquals(2, sector.classBonus(charClass));
 }

 @Test
 public void testGetAreas() {
 Set<Area> areas = new HashSet<Area>(Arrays.asList(area));
 assertEquals(areas, sector.getAreas());
 }

 @Test
 public void testAddArea() {
 sector.addArea(area);
 assertTrue(sector.getAreas().contains(area));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddNullArea() {
 sector.addArea(null);
 fail("Should not accept a null area");
 }

 @Test
 public void testHashCode() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 1, false, 2, CharacterClass.ARCHER);
 assertTrue(sector.hashCode() != sector2.hashCode());
 }

 @Test
 public void testEquals() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 1, 1, false, 2, CharacterClass.ARCHER);
 assertFalse(sector.equals(sector2));
 sector2.setAreas(sector.getAreas());
 assertTrue(sector.equals(sector2));
 }

 @Test
 public void testAddObserver() {
 sector.addObserver(sectorObserver);
 assertTrue(sector.getObservers().contains(sectorObserver));
 }

 @Test
 public void testRemoveObserver() {
 sector.addObserver(sectorObserver);
 sector.removeObserver(sectorObserver);
 assertFalse(sector.getObservers().contains(sectorObserver));
 }
}

class SimpleCharacterClass implements CharacterClass {
 @Override
 public String getName() {
 return "test";
 }
}

class TestCharacterClass extends SimpleCharacterClass {
 private final String name;

 TestCharacterClass(String name) {
 this.name = name;
 }

 @Override
 public String toString() {
 return name;
 }
}

class SimpleArea implements Area {
 private final int x;
 private final int y;

 public SimpleArea(int x, int y) {
 this.x = x;
 this.y = y;
 }

 @Override
 public int getX() {
 return x;
 }

 @Override
 public int getY() {
 return y;
 }

 @Override
 public boolean isOccupied() {
 return false;
 }

 @Override
 public Combatant getOccupier() {
 return null;
 }
}

class SimpleCombatant implements Combatant {
 private final CharacterClass characterClass;

 public SimpleCombatant(CharacterClass characterClass) {
 this.characterClass = characterClass;
 }

 @Override
 public CharacterClass getCharacterClass() {
 return characterClass;
 }

 @Override
 public Weapon getWeapon() {
 return new SimpleWeapon();
 }
}

class SimpleSectorObserver implements SectorObserver {
 private boolean isOccupierChangedCalled = false;

 @Override
 public void occupierChanged() {
 isOccupierChangedCalled = true;
 }

 public boolean isOccupierChangedCalled() {
 return isOccupierChangedCalled;
 }
}

class SimpleWeapon implements Weapon {
 @Override
 public int getSectorBonus(SectorBonus.BonusType type) {
 return 0;
 }
}