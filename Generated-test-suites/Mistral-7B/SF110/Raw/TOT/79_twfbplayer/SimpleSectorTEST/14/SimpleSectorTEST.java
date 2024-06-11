package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 3, CharacterClass.WARRIOR);
 }

 @Test
 public void testFree() {
 final int initialOccupierCount = sector.getOccupierCount();
 sector.free();
 assertEquals(initialOccupierCount - 1, sector.getOccupierCount());
 sector.free(); // test freeing an already free sector
 assertEquals(initialOccupierCount - 2, sector.getOccupierCount());
 }

 @Test
 public void testIsOccupied() {
 assertFalse(sector.isOccupied()); // test empty sector
 Combatant combatant = new Warrior();
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER, combatant);
 assertTrue(sector.isOccupied());
 sector.free();
 assertTrue(sector.isOccupied()); // test sector with no occupiers but still occupied
 combatant.free();
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGainControl() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER, new Warrior());
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 sector.gainControl(CombatantSide.ATTACKER, new Warrior());
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testGetOccupier() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER, new Warrior());
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 }

 @Test
 public void testGetBonus() {
 final CharacterClass warrior = CharacterClass.WARRIOR;
 final CharacterClass archer = CharacterClass.ARCHER;
 final CharacterClass mage = CharacterClass.MAGE;
 final SectorBonus expectedBonusWarrior = new SectorBonus(11, 7, 5);
 final SectorBonus expectedBonusArcher = new SectorBonus(6, 11, 3);
 final SectorBonus expectedBonusMage = new SectorBonus(1, 15, 7);
 sector.gainControl(CombatantSide.DEFENDER);
 sector._addArea(new Area(new Warrior()));
 sector._addArea(new Area(new Archer()));
 sector._addArea(new Area(new Mage()));
 assertEquals(expectedBonusWarrior, sector.getBonus(warrior));
 assertEquals(expectedBonusArcher, sector.getBonus(archer));
 assertEquals(expectedBonusMage, sector.getBonus(mage));
 }

 @Test
 public void testGetAttackBonus() {
 final CharacterClass warrior = CharacterClass.WARRIOR;
 final CharacterClass archer = CharacterClass.ARCHER;
 final CharacterClass mage = CharacterClass.MAGE;
 final int expectedWarriorBonus = 13;
 final int expectedArcherBonus = 4;
 final int expectedMageBonus = 0;
 sector.gainControl(CombatantSide.DEFENDER);
 sector._addArea(new Area(new Warrior()));
 sector._addArea(new Area(new Archer()));
 sector._addArea(new Area(new Mage()));
 sector.gainControl(CombatantSide.ATTACKER, new Warrior());
 assertEquals(expectedWarriorBonus, sector.getAttackBonus(warrior));
 sector.gainControl(CombatantSide.ATTACKER, new Archer());
 assertEquals(expectedArcherBonus, sector.getAttackBonus(archer));
 sector.gainControl(CombatantSide.ATTACKER, new Mage());
 assertEquals(expectedMageBonus, sector.getAttackBonus(mage));
 }

 @Test
 public void testGetDefenseBonus() {
 final CharacterClass warrior = CharacterClass.WARRIOR;
 final CharacterClass archer = CharacterClass.ARCHER;
 final CharacterClass mage = CharacterClass.MAGE;
 final int expectedWarriorBonus = 11;
 final int expectedArcherBonus = 12;
 final int expectedMageBonus = 15;
 sector.gainControl(CombatantSide.DEFENDER);
 sector._addArea(new Area(new Warrior()));
 sector._addArea(new Area(new Archer()));
 sector._addArea(new Area(new Mage()));
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(expectedWarriorBonus, sector.getDefenseBonus(warrior));
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(expectedArcherBonus, sector.getDefenseBonus(archer));
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(expectedMageBonus, sector.getDefenseBonus(mage));
 }

 @Test
 public void testClassBonus() {
 final CharacterClass warrior = CharacterClass.WARRIOR;
 final CharacterClass archer = CharacterClass.ARCHER;
 final CharacterClass mage = CharacterClass.MAGE;
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER, new Warrior());
 assertEquals(3, sector.classBonus(warrior));
 sector.gainControl(CombatantSide.DEFENDER, new Archer());
 assertEquals(0, sector.classBonus(warrior));
 sector.gainControl(CombatantSide.DEFENDER, new Mage());
 assertEquals(3, sector.classBonus(mage));
 }

 @Test
 public void testHashCodeAndEquals() {
 Set<Area> areaSet1 = new HashSet<Area>(Arrays.asList(new Area(new Warrior()), new Area(new Warrior())));
 Set<Area> areaSet2 = new HashSet<Area>(Arrays.asList(new Area(new Warrior()), new Area(new Archer())));
 SimpleSector sector1 = new SimpleSector(5, true, true, 5, 5, false, 3, CharacterClass.WARRIOR);
 SimpleSector sector2 = new SimpleSector(5, true, true, 5, 5, true, 3, CharacterClass.WARRIOR);
 SimpleSector sector3 = new SimpleSector(5, true, true, 5, 5, false, 3, CharacterClass.WARRIOR);
 SimpleSector sector4 = new SimpleSector(5, true, true, 5, 5, false, 3, CharacterClass.ARCHER);
 SimpleSector sector5 = new SimpleSector(5, true, true, 5, 5, false, 3, CharacterClass.ARCHER);
 SimpleSector sector6 = new SimpleSector(5, true, true, 5, 5, false, 3, CharacterClass.ARCHER);
 SimpleSector sector7 = new SimpleSector(5, false, false, 5, 5, false, 3, CharacterClass.WARRIOR);
 SimpleSector sector8 = new SimpleSector(5, false, false, 5, 5, false, 3, CharacterClass.ARCHER);
 SimpleSector sector9 = new SimpleSector(5, false, false, 5, 5, false, 3, CharacterClass.ARCHER);
 SimpleSector sector10 = new SimpleSector(5, true, true, 5, 5, false, 3, CharacterClass.ARCHER);
 SimpleSector sector11 = new SimpleSector(5, true, true, 5, 5, false, 3, CharacterClass.ARCHER);
 SimpleSector sector12 = new SimpleSector(5, true, true, 4, 5, false, 3, CharacterClass.ARCHER);

 assertEquals(sector1.hashCode(), sector1.hashCode());
 assertEquals(sector1, sector1);
 assertEquals(sector1, sector3);
 assertNotEquals(sector1, sector2);
 assertNotEquals(sector1, sector4);
 assertNotEquals(sector1, sector7);
 assertNotEquals(sector1, sector8);
 assertNotEquals(sector1, sector9);
 assertNotEquals(sector1, sector10);
 assertNotEquals(sector1, sector11);
 assertNotEquals(sector1, sector12);
 }
}