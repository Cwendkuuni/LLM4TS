package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.*;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Test
 public void testSimpleSectorConstruction() {
 sector = new SimpleSector(10, true, true, 5, 3, true, 1, CharacterClass.ARCHER);
 assertEquals(10, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertFalse(sector.isOccupied());
 assertNull(sector.getOccupier());
 assertFalse(sector.isFlag());
 assertEquals(1, sector.getClassBonus());
 assertEquals(CharacterClass.ARCHER, sector.getClassType());
 assertEmpty(sector.getAreas());
 assertEmpty(sector.getObservers());
 }

 @Test
 public void testSetAndGetOccupier() {
 sector = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 Combatant combatant = new Combatant(CharacterClass.ARCHER, "TestCombatant");
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER, combatant);
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 assertEquals(combatant, sector.getOccupier(CombatantSide.ATTACKER));
 assertNull(sector.getOccupier(CombatantSide.DEFENDER));
 sector.free();
 assertNull(sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 sector = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 assertFalse(sector.isOccupied());
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGainControlWithNegativeOccupierCount() {
 sector = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER, new Combatant(CharacterClass.ARCHER, "TestCombatant"));
 sector.free();
 sector.free();
 sector.free();
 sector.gainControl(-1, new Combatant(CharacterClass.ARCHER, "TestCombatant"));
 }

 @Test
 public void testFreeWithZeroOccupierCount() {
 sector = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertFalse(sector.isOccupied());
 assertNull(sector.getOccupier());
 }

 @Test
 public void testOccupierChanged() {
 sector = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 SectorObserver observer = new SectorObserver() {
 @Override
 public void occupierChanged() {
 System.out.println("Occupier changed.");
 }
 };
 sector.addObserver(observer);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER, new Combatant(CharacterClass.ARCHER, "TestCombatant"));
 assertEquals(2, sector.getOccupierCount());
 assertTrue(sector.isOccupied());
 assertNotNull(sector.getOccupier());
 observer.occupierChanged();
 assertEquals("Occupier changed.\n", systemOut.getHistory());
 }

 @Test
 public void testGetBonusWithNullCharacterClass() {
 sector = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 assertNull(sector.getBonus(null));
 }

 @Test
 public void testGetBonusWithValidCharacterClass() {
 sector = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 SectorBonus bonus = sector.getBonus(CharacterClass.ARCHER);
 assertEquals(7, bonus.getAttackBonus());
 assertEquals(5, bonus.getDefenseBonus());
 assertEquals(0, bonus.getDamageBonus());
 }

 @Test
 public void testGetAttackBonusWithNullCharacterClass() {
 sector = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 assertEquals(2, sector.getAttackBonus(null));
 }

 @Test
 public void testGetAttackBonusWithValidCharacterClass() {
 sector = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 assertEquals(7, sector.getAttackBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDefenseBonusWithNullCharacterClass() {
 sector = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 assertEquals(5, sector.getDefenseBonus(null));
 }

 @Test
 public void testGetDefenseBonusWithValidCharacterClass() {
 sector = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 assertEquals(5, sector.getDefenseBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testClassBonusWithNullCharacterClass() {
 sector = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 assertEquals(0, sector.classBonus(null));
 }

 @Test
 public void testClassBonusWithValidCharacterClass() {
 sector = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 assertEquals(0, sector.classBonus(CharacterClass.ARCHER));
 sector = new SimpleSector(5, false, false, 2, 2, true, 1, CharacterClass.ARCHER);
 assertEquals(1, sector.classBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testHashCodeAndEquals() {
 SimpleSector sector1 = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 SimpleSector sector5 = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 SimpleSector sector6 = new SimpleSector(5, false, false, 2, 2, false, 1, CharacterClass.ARCHER);

 Set<SimpleSector> set = new HashSet<>();
 set.add(sector1);
 set.add(sector2);
 set.add(sector3);
 set.add(sector4);
 set.add(sector5);

 assertTrue(set.contains(sector1));
 assertTrue(set.contains(sector2));
 assertTrue(set.contains(sector3));
 assertTrue(set.contains(sector4));
 assertTrue(set.contains(sector5));
 assertFalse(set.contains(sector6));

 assertEquals(sector1, sector1);
 assertEquals(sector1, sector1);
 assertEquals(sector1, sector2); // equals should be reflexive, symmetric and transitive
 assertNotSame(sector1, sector6);
 }

 @Test
 public void testAddAndRemoveObserver() {
 sector = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 SectorObserver observer1 = new SectorObserver() {
 @Override
 public void occupierChanged() {
 System.out.println("Observer1: Occupier changed.");
 }
 };
 sector.addObserver(observer1);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER, new Combatant(CharacterClass.ARCHER, "TestCombatant"));
 assertEquals(1, sector.getObservers().size());
 sector.removeObserver(observer1);
 assertEquals(0, sector.getObservers().size());
 }

 @Test
 public void testAddAndRemoveMultipleObservers() {
 sector = new SimpleSector(5, false, false, 2, 2, false, 0, CharacterClass.ARCHER);
 SectorObserver observer1 = new SectorObserver() {
 @Override
 public void occupierChanged() {
 System.out.println("Observer1: Occupier changed.");
 }
 };
 SectorObserver observer2 = new SectorObserver() {
 @Override
 public void occupierChanged() {
 System.out.println("Observer2: Occupier changed.");
 }
 };
 sector.addObserver(observer1);
 sector.addObserver(observer2);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER, new Combatant(CharacterClass.ARCHER, "TestCombatant"));
 assertEquals(2, sector.getObservers().size());
 sector.removeObserver(observer1);
 assertEquals(1, sector.getObservers().size());
 sector.removeObserver(observer2);
 assertEquals(0, sector.getObservers().size());
 }
}