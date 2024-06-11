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
import de.outstare.fortbattleplayer.model.impl.SimpleArea;
import de.outstare.fortbattleplayer.model.impl.SimpleWeapon;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Before
 public void setUp() {
 this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 }

 @Test
 public void testConstructor() {
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(10, sector.getAttackBonus(CharacterClass.ARCHER));
 assertEquals(5, sector.getDefenseBonus(CharacterClass.ARCHER));
 assertFalse(sector.isFlag());
 assertNull(sector.getOccupier());
 assertTrue(sector.isOccupied());
 assertTrue(sector.getAreas().isEmpty());
 assertTrue(sector.getObservers().isEmpty());
 }

 @Test
 public void testFree() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(1, sector.getOccupierCount());
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 }

 @Test(expected = AssertionError.class)
 public void testFreeWhenOccupierCountIsNegative() {
 sector.occupierCount = -1;
 sector.free();
 }

 @Test
 public void testGainControl() {
 sector.free();
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(1, sector.getOccupierCount());
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(2, sector.getOccupierCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGainControlWhenSideIsNull() {
 sector.gainControl(null);
 }

 @Test
 public void testGetOccupier() {
 Combatant defender = new SimpleCombatant(CharacterClass.ARCHER, "defender");
 Combatant attacker = new SimpleCombatant(CharacterClass.KNIGHT, "attacker");
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER, defender);
 sector.gainControl(CombatantSide.ATTACKER, attacker);
 assertEquals(defender, sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 assertFalse(sector.isOccupied());
 sector.gainControl(CombatantSide.DEFENDER, new SimpleCombatant(CharacterClass.ARCHER, "defender"));
 assertTrue(sector.isOccupied());
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
 assertEquals(new SectorBonus(15, 7, 3), sector.getBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetAttackBonus() {
 assertEquals(15, sector.getAttackBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDefenseBonus() {
 assertEquals(7, sector.getDefenseBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDamageBonus() {
 assertEquals(3, sector.getDamageBonus());
 }

 @Test
 public void testGetBonusFromWeapons() {
 SimpleArea area = new SimpleArea();
 area.occupy(new SimpleCombatant(CharacterClass.ARCHER, "archer"), new SimpleWeapon(CharacterClass.ARCHER, 5, 5, 5));
 sector.addArea(area);
 assertEquals(5, sector.getBonusFromWeapons(SectorBonus.BonusType.ATTACK));
 }

 @Test
 public void testClassBonus() {
 assertEquals(5, sector.classBonus(CharacterClass.ARCHER));
 assertEqual(0, sector.classBonus(CharacterClass.KNIGHT));
 }

 @Test
 public void testGetAreas() {
 Set<Area> areas = new HashSet<Area>();
 areas.add(new SimpleArea());
 sector.setAreas(areas);
 assertEquals(areas, sector.getAreas());
 }

 @Test
 public void testAddArea() {
 Set<Area> areas = new HashSet<Area>();
 SimpleArea area = new SimpleArea();
 sector.addArea(area);
 assertTrue(areas.contains(area));
 }

 @Test(expected = NullPointerException.class)
 public void testAddAreaWhenAreaIsNull() {
 sector.addArea(null);
 }

 @Test
 public void testHashCode() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(6, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 Set<Sector> sectors = new HashSet<Sector>(Arrays.asList(sector1, sector2, sector3, sector4));
 assertEquals(4, sectors.size());
 }

 @Test
 public void testEquals() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 SimpleSector sector4 = new SimpleSector(6, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 assertTrue(sector1.equals(sector1));
 assertTrue(sector1.equals(sector2));
 assertTrue(sector1.equals(sector3));
 assertFalse(sector1.equals(sector4));
 }

 @Test
 public void testAddObserver() {
 SimpleSectorObserver observer = new SimpleSectorObserver();
 sector.addObserver(observer);
 assertTrue(sector.getObservers().contains(observer));
 }

 @Test(expected = NullPointerException.class)
 public void testAddObserverWhenObserverIsNull() {
 sector.addObserver(null);
 }

 @Test
 public void testRemoveObserver() {
 SimpleSectorObserver observer = new SimpleSectorObserver();
 sector.addObserver(observer);
 sector.removeObserver(observer);
 assertFalse(sector.getObservers().contains(observer));
 }

}

class SimpleCombatant implements Combatant {
 private CharacterClass charClass;
 private String name;

 public SimpleCombatant(CharacterClass charClass, String name) {
 this.charClass = charClass;
 this.name = name;
 }

 @Override
 public CharacterClass getCharacterClass() {
 return charClass;
 }

 @Override
 public String getName() {
 return name;
 }

 @Override
 public Weapon getWeapon() {
 return null;
 }
}

class SimpleArea implements Area {
 private Set<Combatant> combatants;

 public SimpleArea() {
 this.combatants = new HashSet<Combatant>();
 }

 @Override
 public boolean isOccupied() {
 return !combatants.isEmpty();
 }

 @Override
 public Combatant getOccupier() {
 return combatants.iterator().next();
 }

 @Override
 public void occupy(Combatant combatant, Weapon weapon) {
 combatants.add(combatant);
 }

 @Override
 public void free() {
 combatants.clear();
 }
}

class SimpleSectorObserver implements SectorObserver {
 @Override
 public void occupierChanged() {
 // no-op
 }
}