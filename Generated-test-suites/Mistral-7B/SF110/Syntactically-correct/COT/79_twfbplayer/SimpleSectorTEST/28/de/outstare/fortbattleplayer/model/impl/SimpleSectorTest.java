package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SimpleSector.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.test.dto.TestCombatant;
import de.outstare.fortbattleplayer.model.impl.test.dto.TestWeapon;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Test
 public void testSimpleSectorConstruction() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 equitychk(this.sector);
 }

 @Test
 public void testFree() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 this.sector.gainControl(CombatantSide.DEFENDERS);
 this.sector.free();
 equitychk(this.sector);
 }

 @Test(expected = AssertionError.class)
 public void testFreeWhenOccupiedCountIsNegative() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 this.sector.gainControl(CombatantSide.DEFENDERS);
 this.sector.gainControl(CombatantSide.DEFENDERS);
 this.sector.free();
 this.sector.free(); // should throw AssertionError
 }

 @Test
 public void testGainControl() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 this.sector.gainControl(CombatantSide.DEFENDERS);
 equitychk(this.sector, CombatantSide.DEFENDERS);
 this.sector.gainControl(CombatantSide.ATTACKERS);
 equitychk(this.sector, CombatantSide.ATTACKERS);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGainControlWhenAlreadyOccupiedByGivenSide() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 this.sector.gainControl(CombatantSide.DEFENDERS);
 this.sector.gainControl(CombatantSide.DEFENDERS); // should throw IllegalArgumentException
 }

 @Test
 public void testGetOccupier() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 this.sector.gainControl(CombatantSide.DEFENDERS);
 assertEquals(CombatantSide.DEFENDERS, this.sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 this.sector.gainControl(CombatantSide.DEFENDERS);
 assertTrue(this.sector.isOccupied());
 }

 @Test
 public void testGetHeight() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 assertEquals(5, this.sector.getHeight());
 }

 @Test
 public void testIsDefenderSpawn() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 assertTrue(this.sector.isDefenderSpawn());
 }

 @Test
 public void testIsAttackerSpawn() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 assertTrue(this.sector.isAttackerSpawn());
 }

 @Test
 public void testIsFlag() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 assertFalse(this.sector.isFlag());
 }

 @Test
 public void testGetBonus() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 TestCombatant defender = new TestCombatant();
 defender.setCharacterClass(CharacterClass.ARCHER);
 TestWeapon defenderWeapon = new TestWeapon();
 defender.setWeapon(defenderWeapon);
 this.sector.addArea(new SimpleArea(new SimpleAreaLocation(1, 1), defender));
 SectorBonus expectedBonus = new SectorBonus(15, 10, 15);
 SectorBonus actualBonus = this.sector.getBonus(CharacterClass.ARCHER);
 assertEquals(expectedBonus, actualBonus);
 }

 @Test
 public void testGetAttackBonus() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 TestCombatant defender = new TestCombatant();
 defender.setCharacterClass(CharacterClass.ARCHER);
 TestWeapon defenderWeapon = new TestWeapon();
 defender.setWeapon(defenderWeapon);
 this.sector.addArea(new SimpleArea(new SimpleAreaLocation(1, 1), defender));
 int expectedAttackBonus = 15;
 int actualAttackBonus = this.sector.getAttackBonus(CharacterClass.ARCHER);
 assertEquals(expectedAttackBonus, actualAttackBonus);
 }

 @Test
 public void testGetDefenseBonus() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 TestCombatant defender = new TestCombatant();
 defender.setCharacterClass(CharacterClass.ARCHER);
 TestWeapon defenderWeapon = new TestWeapon();
 defender.setWeapon(defenderWeapon);
 this.sector.addArea(new SimpleArea(new SimpleAreaLocation(1, 1), defender));
 int expectedDefenseBonus = 10;
 int actualDefenseBonus = this.sector.getDefenseBonus(CharacterClass.ARCHER);
 assertEquals(expectedDefenseBonus, actualDefenseBonus);
 }

 @Test
 public void testGetDamageBonus() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 TestCombatant defender = new TestCombatant();
 defender.setCharacterClass(CharacterClass.ARCHER);
 TestWeapon defenderWeapon = new TestWeapon();
 defender.setWeapon(defenderWeapon);
 this.sector.addArea(new SimpleArea(new SimpleAreaLocation(1, 1), defender));
 int expectedDamageBonus = 15;
 int actualDamageBonus = this.sector.getDamageBonus();
 assertEquals(expectedDamageBonus, actualDamageBonus);
 }

 @Test
 public void testGetAreas() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 Set<Area> expectedAreas = new HashSet<>(Arrays.asList(new SimpleArea(new SimpleAreaLocation(1, 1))));
 Set<Area> actualAreas = this.sector.getAreas();
 assertEquals(expectedAreas, actualAreas);
 }

 @Test
 public void testAddArea() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 Area testArea = new SimpleArea(new SimpleAreaLocation(1, 1));
 this.sector._addArea(testArea);
 Set<Area> expectedAreas = new HashSet<>(Arrays.asList(testArea));
 Set<Area> actualAreas = this.sector.getAreas();
 assertEquals(expectedAreas, actualAreas);
 }

 @Test
 public void testHashCode() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(6, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
assertEquals(sector1.hashCode(), sector1.hashCode());
 assertEquals(sector1.hashCode(), sector2.hashCode());
 assertNotEquals(sector1.hashCode(), sector3.hashCode());
 }

 @Test
 public void testEquals() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(6, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 assertEquals(sector1, sector1);
 assertEquals(sector1, sector2);
 assertNotEquals(sector1, sector3);
 assertNotEquals(sector1, sector4);
 }

 @Test
 public void testAddObserver() {
 Set<SectorObserver> observers = new HashSet<>();
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 SectorObserver testObserver = () -> {};
 this.sector.addObserver(testObserver);
 assertEquals(1, this.sector.getObservers().size());
 }

 @Test
 public void testRemoveObserver() {
 Set<SectorObserver> observers = new HashSet<>();
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 SectorObserver testObserver = () -> {};
 this.sector.addObserver(testObserver);
 this.sector.removeObserver(testObserver);
 assertEquals(0, this.sector.getObservers().size());
 }

 private void equitychk(SimpleSector sector, CombatantSide expectedOccupier) {
 assertEquals(5, sector.getHeight());
 assertEquals(true, sector.isDefenderSpawn());
 assertEquals(true, sector.isAttackerSpawn());
 assertEquals(false, sector.isFlag());
 Set<Area> areas = sector.getAreas();
 assertEquals(0, areas.size());
 Set<SectorObserver> observers = sector.getObservers();
 assertEquals(0, observers.size());
 assertEquals(null, sector.getOccupier());
 assertFalse(sector.isOccupied());
 }
}