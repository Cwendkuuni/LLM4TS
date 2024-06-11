package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.Weapon;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Test
 public void testSimpleSectorConstruction() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 assertEquals("Height incorrect", 5, sector.getHeight());
 assertTrue("Defender Spawn incorrect", sector.isDefenderSpawn());
 assertTrue("Attacker Spawn incorrect", sector.isAttackerSpawn());
 assertFalse("Flag incorrect", sector.isFlag());
 assertEquals("Class Bonus incorrect", 5, sector.getClassBonus());
 assertEquals("Class Type incorrect", CharacterClass.ARCHANGEL, sector.getClassType());
 assertNull("Occupier incorrect", sector.getOccupier());
 assertFalse("Is Occupied incorrect", sector.isOccupied());
 assertEmpty("Areas incorrect", sector.getAreas());
 assertEmpty("Observers incorrect", sector.getObservers());
 }

 @Test
 public void testFree() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertNull("Error: Combatant still present in sector after free method is called", sector.getOccupier());
 assertFalse("Error: Is Occupied still true after free method is called", sector.isOccupied());
 }

 @Test
 public void testOccupierChanged() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 sector.addObserver(mock(SectorObserver.class));
 sector.gainControl(CombatantSide.DEFENDER);
 verify(mock(SectorObserver.class)).occupierChanged();
 }

 @Test
 public void testGainControl() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals("Error: Occupier incorrect after gainControl method is called", CombatantSide.DEFENDER, sector.getOccupier());
 assertTrue("Error: Is Occupied still false after gainControl method is called", sector.isOccupied());
 }

 @Test
 public void testGetOccupier() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals("Error: Occupier incorrect", CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue("Error: Is Occupied incorrect", sector.isOccupied());
 }

 @Test
 public void testGetHeight() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 assertEquals("Error: Height incorrect", 5, sector.getHeight());
 }

 @Test
 public void testIsDefenderSpawn() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 assertTrue("Error: Defender Spawn incorrect", sector.isDefenderSpawn());
 }

 @Test
 public void testIsAttackerSpawn() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 assertTrue("Error: Attacker Spawn incorrect", sector.isAttackerSpawn());
 }

 @Test
 public void testIsFlag() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 assertFalse("Error: Flag incorrect", sector.isFlag());
 }

 @Test
 public void testGetBonus() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 CharacterClass testCharacter = CharacterClass.ARCHER;
 SectorBonus expectedBonus = new SectorBonus(5, 5, 10);
 assertEquals("Error: Bonus incorrect", expectedBonus, sector.getBonus(testCharacter));
 }

 @Test
 public void testGetAttackBonus() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 CharacterClass testCharacter = CharacterClass.ARCHER;
 int expectedAttackBonus = 10;
 int expectedClassBonus = 5;
 int expectedDamageBonus = 0;
 int actualAttackBonus = sector.getAttackBonus(testCharacter);
 int actualClassBonus = sector.getClassBonus(testCharacter);
 int actualDamageBonus = sector.getDamageBonus();
 assertEquals("Error: Attack Bonus incorrect", expectedAttackBonus + expectedClassBonus, actualAttackBonus);
 assertEquals("Error: Class Bonus incorrect", expectedClassBonus, actualClassBonus);
 assertEquals("Error: Damage Bonus incorrect", expectedDamageBonus, actualDamageBonus);
 }

 @Test
 public void testGetDefenseBonus() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 CharacterClass testCharacter = CharacterClass.ARCHER;
 int expectedDefenseBonus = 5;
 int expectedClassBonus = 5;
 int expectedDamageBonus = 0;
 int actualDefenseBonus = sector.getDefenseBonus(testCharacter);
 int actualClassBonus = sector.getClassBonus(testCharacter);
 int actualDamageBonus = sector.getDamageBonus();
 assertEquals("Error: Defense Bonus incorrect", expectedDefenseBonus + expectedClassBonus, actualDefenseBonus);
 assertEquals("Error: Class Bonus incorrect", expectedClassBonus, actualClassBonus);
 assertEquals("Error: Damage Bonus incorrect", expectedDamageBonus, actualDamageBonus);
 }

 @Test
 public void testClassBonus() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 CharacterClass testCharacter = CharacterClass.ARCHER;
 int expectedClassBonus = 5;
 int actualClassBonus = sector.classBonus(testCharacter);
 assertEquals("Error: Class Bonus incorrect", expectedClassBonus, actualClassBonus);
 }

 @Test
 public void testGetAreas() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 Set<Area> expectedAreas = new HashSet<>(Arrays.asList(new Area()));
 Set<Area> actualAreas = sector.getAreas();
 assertEquals("Error: Areas incorrect", expectedAreas, actualAreas);
 }

 @Test
 public void testAddArea() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 Area testArea = new Area();
 sector.addArea(testArea);
 Set<Area> expectedAreas = new HashSet<>(Arrays.asList(testArea));
 Set<Area> actualAreas = sector.getAreas();
 assertEquals("Error: Areas incorrect", expectedAreas, actualAreas);
 }

 @Test
 public void testHashCode() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 SimpleSector copy = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 assertEquals("Error: HashCodes incorrect", sector.hashCode(), copy.hashCode());
 }

 @Test
 public void testEquals() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 SimpleSector copy = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 assertTrue("Error: Objects should be equal", sector.equals(copy));
 }

 @Test
 public void testAddObserver() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 Set<SectorObserver> observers = new HashSet<>();
 sector.addObserver(mock(SectorObserver.class));
 assertEquals("Error: Observers incorrect", 1, sector.getObservers().size());
 }

 @Test
 public void testRemoveObserver() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHANGEL);
 Set<SectorObserver> observers = new HashSet<>(Arrays.asList(mock(SectorObserver.class), mock(SectorObserver.class)));
 sector.addObserver(mock(SectorObserver.class));
 sector.removeObserver(mock(SectorObserver.class));
 assertEquals("Error: Observers incorrect", 1, sector.getObservers().size());
 }
}