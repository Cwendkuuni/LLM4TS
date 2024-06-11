package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Before
 public void setup() {
 sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.Knight);
 }

 // Testing constructor and getters
 @Test
 public void testConstructorAndGetters() {
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertFalse(sector.isFlag());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEquals(0, sector.getAttackBonus(CharacterClass.Archer));
 assertEquals(0, sector.getDefenseBonus(CharacterClass.Archer));
 assertEquals(0, sector.getDamageBonus());
 assertEquals(CharacterClass.Knight, sector.getClassType());
 assertNotNull(sector.getAreas());
 assertFalse(sector.isOccupied());
 }

 // Testing free method
 @Test
 public void testFree() {
 sector.gainControl(CombatantSide.ATTACKER);
 sector.free();
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 }

 // Testing gainControl method
 @Test
 public void testGainControl() {
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(sector.isOccupied());
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 }

 // Testing getOccupier method
 @Test
 public void testGetOccupier() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 // Testing isOccupied method
 @Test
 public void testIsOccupied() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 sector.free();
 assertFalse(sector.isOccupied());
 }

 // Testing getBonus method
 @Test
 public void testGetBonus() {
 assertEquals(new SectorBonus(15, 15, 5), sector.getBonus(CharacterClass.Archer));
 }

 // Testing getAttackBonus method
 @Test
 public void testGetAttackBonus() {
 assertEquals(15, sector.getAttackBonus(CharacterClass.Archer));
 }

 // Testing getDefenseBonus method
 @Test
 public void testGetDefenseBonus() {
 assertEquals(15, sector.getDefenseBonus(CharacterClass.Archer));
 }

 // Testing getDamageBonus method
 @Test
 public void testGetDamageBonus() {
 assertEquals(5, sector.getDamageBonus());
 }

 // Testing classBonus method with correct class
 @Test
 public void testClassBonusCorrectClass() {
 assertEquals(5, sector.classBonus(CharacterClass.Knight));
 }

 // Testing classBonus method with wrong class
 @Test (expected = AssertionError.class)
 public void testClassBonusWrongClass() {
 assertEquals(5, sector.classBonus(CharacterClass.Archer));
 }

 // Testing addArea method
 @Test
 public void testAddArea() {
 sector.addArea(new Area());
 assertTrue(sector.getAreas().contains(new Area()));
 }

 // Testing equals method
 @Test
 public void testEquals() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.Knight);
 assertTrue(sector.equals(sector2));
 sector2.addArea(new Area());
 assertFalse(sector.equals(sector2));
 }

 // Testing hashCode method
 @Test
 public void testHashCode() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.Knight);
 assertEquals(sector.hashCode(), sector2.hashCode());
 sector.addArea(new Area());
 assertNotEquals(sector.hashCode(), sector2.hashCode());
 }

 // Testing observer methods
 @Test
 public void testObserverMethods() {
 SectorObserver observer = mock(SectorObserver.class);
 sector.addObserver(observer);
 sector.gainControl(CombatantSide.ATTACKER);
 verify(observer).occupierChanged();
 sector.free();
 verifyNoMoreInteractions(observer);
 }

 // Testing exception when more combatants left than enter the sector
 @Test (expected = AssertionError.class)
 public void testFreeMoreCombatantsLeftThanEnter() {
 sector.gainControl(CombatantSide.ATTACKER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 sector.free();
 }
}