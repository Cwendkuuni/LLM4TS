package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Test
 public void testSimpleSectorConstruction() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 assertEquals("Incorrect height", 5, sector.getHeight());
 assertTrue("Defender spawn should be set", sector.isDefenderSpawn());
 assertTrue("Attacker spawn should be set", sector.isAttackerSpawn());
 assertEquals("Incorrect attack bonus", 3, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals("Incorrect defend bonus", 4, sector.getDefenseBonus(CharacterClass.ARCHER));
 assertEquals("Incorrect flag status", true, sector.isFlag());
 assertEquals("Incorrect class bonus", 5, sector.getClassBonus());
 assertNull("Occupier should be null", sector.getOccupier());
 assertFalse("Should not be occupied", sector.isOccupied());
 assertFalse("Areas should be empty", sector.getAreas().isEmpty());
 assertFalse("Observers should be empty", sector.getObservers().isEmpty());
 }

 @Test
 public void testFreeMethod() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.ATTACKER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertNull("Occupier should be null after freeing", sector.getOccupier());
 assertFalse("Should not be occupied after freeing", sector.isOccupied());
 }

 @Test(expected = AssertionError.class)
 public void testFreeMethodWithNegativeOccupierCount() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 sector.free();
 sector.free();
 sector.free();
 assertNull("Occupier should be null after freeing", sector.getOccupier());
 assertFalse("Should not be occupied after freeing", sector.isOccupied());
 sector.free(); // should throw an AssertionError
 }

 @Test
 public void testGainControlMethod() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals("Incorrect occupier after gaining control", CombatantSide.DEFENDER, sector.getOccupier());
 assertTrue("Should be occupied", sector.isOccupied());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGainControlMethodWithInvalidSide() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 sector.gainControl(null);
 }

 @Test
 public void testGetOccupierMethod() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals("Incorrect occupier after gaining control", CombatantSide.ATTACKER, sector.getOccupier());
 }

 @Test
 public void testIsOccupiedMethod() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue("Should be occupied", sector.isOccupied());
 sector.free();
 assertFalse("Should not be occupied after freeing", sector.isOccupied());
 }

 @Test
 public void testGetHeightMethod() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 assertEquals("Incorrect height", 5, sector.getHeight());
 }

 @Test
 public void testIsDefenderSpawnMethod() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 assertTrue("Defender spawn should be set", sector.isDefenderSpawn());
 }

 @Test
 public void testIsAttackerSpawnMethod() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 assertTrue("Attacker spawn should be set", sector.isAttackerSpawn());
 }

 @Test
 public void testIsFlagMethod() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 assertTrue("Flag should be false by default", !sector.isFlag());
 sector.setFlag(true);
 assertTrue("Flag should be true after setting it", sector.isFlag());
 }

 @Test
 public void testGetBonusMethod() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 SectorBonus bonus = sector.getBonus(CharacterClass.ARCHER);
 assertEquals("Incorrect attack bonus", 8, bonus.getAttackBonus());
 assertEquals("Incorrect defense bonus", 7, bonus.getDefenseBonus());
 assertEquals("Incorrect damage bonus", 5, bonus.getDamageBonus());
 }

 @Test
 public void testGetDamageBonusMethod() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 Set<Area> areas = new HashSet<Area>(Arrays.asList(new Area(1, 1, CharacterClass.ARCHER), new Area(1, 1, CharacterClass.ARCHER)));
 sector.setAreas(areas);
 assertEquals("Incorrect damage bonus", 5, sector.getDamageBonus());
 }

 @Test
 public void testGetAttackBonusMethod() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 Set<Area> areas = new HashSet<Area>(Arrays.asList(new Area(1, 1, CharacterClass.ARCHER), new Area(1, 1, CharacterClass.ARCHER)));
 sector.setAreas(areas);
 Combatant combatant = new Combatant("Test Combatant", CharacterClass.ARCHER, new SimpleWeapon("Test Weapon", 5, 3, 10));
 sector.getAreas().get(0).setOccupier(combatant);
 assertEquals("Incorrect attack bonus", 11, sector.getAttackBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDefenseBonusMethod() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 Set<Area> areas = new HashSet<Area>(Arrays.asList(new Area(1, 1, CharacterClass.ARCHER), new Area(1, 1, CharacterClass.ARCHER)));
 sector.setAreas(areas);
 Combatant combatant = new Combatant("Test Combatant", CharacterClass.ARCHER, new SimpleWeapon("Test Weapon", 5, 3, 10));
 sector.getAreas().get(0).setOccupier(combatant);
 assertEquals("Incorrect defense bonus", 12, sector.getDefenseBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testClassBonusMethod() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 assertEquals("Incorrect class bonus for archer", 5, sector.classBonus(CharacterClass.ARCHER));
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.KNIGHT);
 assertEquals("Incorrect class bonus for knight", 5, sector.classBonus(CharacterClass.KNIGHT));
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.MAGE);
 assertEquals("Incorrect class bonus for mage", 0, sector.classBonus(CharacterClass.MAGE));
 }

 @Test
 public void testAddAreaMethod() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 Area area = new Area(1, 1, CharacterClass.ARCHER);
 sector.addArea(area);
 Set<Area> areas = sector.getAreas();
 assertTrue("Area should be added to the sector", areas.contains(area));
 }

 @Test(expected = NullPointerException.class)
 public void testAddAreaMethodWithNullArea() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 Set<Area> areas = sector.getAreas();
 try {
 sector.addArea(null);
 } catch (NullPointerException e) {
 throw e;
 }
 }

 @Test
 public void testSetAreasMethod() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 Set<Area> areas = new HashSet<Area>(Arrays.asList(new Area(1, 1, CharacterClass.ARCHER), new Area(1, 1, CharacterClass.ARCHER)));
 sector.setAreas(areas);
 Set<Area> sectorsAreas = sector.getAreas();
 assertEquals("Incorrect number of areas after setting", 2, sectorsAreas.size());
 }

 @Test
 public void testHashCodeMethod() {
 Set<Area> areas = new HashSet<Area>(Arrays.asList(new Area(1, 1, CharacterClass.ARCHER), new Area(1, 1, CharacterClass.ARCHER)));
 SimpleSector sector1 = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 sector3.setAreas(areas);
 assertEquals("Incorrect hashcode for equal objects without areas", sector1.hashCode(), sector2.hashCode());
 assertEquals("Incorrect hashcode for equal objects with areas", sector1.hashCode(), sector3.hashCode());
 }

 @Test
 public void testEqualsMethodWithNullObject() {
 assertTrue("Sector should be equal to null if it is itself", sector.equals(sector));
 assertFalse("Sector should not be equal to null if it is not", sector.equals(null));
 }

 @Test
 public void testEqualsMethodWithDifferentObjectType() {
 assertFalse("Sector should not be equal to an object of another type", sector.equals(new Object()));
 }

 @Test
 public void testEqualsMethodWithDifferentCharacterClass() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.KNIGHT);
 assertFalse("Sectors with different character classes should not be equal", sector1.equals(sector2));
 }

 @Test
 public void testEqualsMethodWithDifferentHeight() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(6, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 assertFalse("Sectors with different heights should not be equal", sector1.equals(sector2));
 }

 @Test
 public void testEqualsMethodWithDifferentDefenderSpawn() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, false, true, 3, 4, true, 5, CharacterClass.ARCHER);
 assertFalse("Sectors with different defender spawn should not be equal", sector1.equals(sector2));
 }

 @Test
 public void testEqualsMethodWithDifferentAttackerSpawn() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, false, 3, 4, true, 5, CharacterClass.ARCHER);
 assertFalse("Sectors with different attacker spawn should not be equal", sector1.equals(sector2));
 }

 @Test
 public void testEqualsMethodWithDifferentAttackBonus() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 4, 4, true, 5, CharacterClass.ARCHER);
 assertFalse("Sectors with different attack bonus should not be equal", sector1.equals(sector2));
 }

 @Test
 public void testEqualsMethodWithDifferentDefendBonus() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 3, 5, true, 5, CharacterClass.ARCHER);
 assertFalse("Sectors with different defend bonus should not be equal", sector1.equals(sector2));
 }

 @Test
 public void testEqualsMethodWithDifferentFlag() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 sector2.setFlag(false);
 assertFalse("Sectors with different flags should not be equal", sector1.equals(sector2));
 }

 @Test
 public void testAddObserverMethod() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 SectorObserver observer = mock(SectorObserver.class);
 sector.addObserver(observer);
 verify(observer).occupierChanged();
 }

 @Test
 public void testRemoveObserverMethod() {
 sector = new SimpleSector(5, true, true, 3, 4, true, 5, CharacterClass.ARCHER);
 SectorObserver observer = mock(SectorObserver.class);
 sector.addObserver(observer);
 sector.removeObserver(observer);
 verifyNoMoreInteractions(observer);
 }
}