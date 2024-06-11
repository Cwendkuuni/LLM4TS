package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.TestCharacterClass;
import de.outstare.fortbattleplayer.model.impl.TestSectorBonus;
import de.outstare.fortbattleplayer.model.impl.TestSectorObserver;
import de.outstare.fortbattleplayer.model.impl.TestWeapon;

public class SimpleSectorTest {

 private SimpleSector sector;
 private Set<Area> areas;
 private Area area1;
 private Area area2;
 private TestSectorObserver sectorObserver;
 private TestCharacterClass character;
 private TestSectorBonus sectorBonus;
 private TestWeapon weapon;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 5, 5, true, 5, CharacterClass.ARCHER);
 areas = new HashSet<Area>();
 area1 = new Area(1, 1);
 area2 = new Area(2, 2);
 sectors.add(area1);
 sectors.add(area2);
 sector.setAreas(areas);
 sectorObserver = new TestSectorObserver();
 sector.addObserver(sectorObserver);
 character = new TestCharacterClass();
 character.setClassType(CharacterClass.ARCHER);
 weapon = new TestWeapon();
 weapon.setSectorBonus(SectorBonus.BonusType.ATTACK, 10);
 }

 @Test
 public void testFree() {
 sector.free();
 assertEquals(0, sector.getOccupierCount());
 assertNull(sector.getOccupier());
 }

 @Test
 public void testGainControl() {
 sector.gainControl(CharacterSide.DEFENDER);
 assertEquals(1, sector.getOccupierCount());
 assertEquals(CharacterSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testGetOccupier() {
 sector.gainControl(CharacterSide.DEFENDER);
 assertEquals(CharacterSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testIsOccupied() {
 sector.gainControl(CharacterSide.DEFENDER);
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
 assertTrue(sector.isFlag());
 }

 @Test
 public void testGetBonus() {
 sectorBonus = sector.getBonus(CharacterClass.ARCHER);
 assertEquals(new SectorBonus(15, 15, 10), sectorBonus);
 }

 @Test
 public void testGetDamageBonus() {
 assertEquals(10, sector.getDamageBonus());
 }

 @Test
 public void testGetAttackBonus() {
 assertEquals(10, sector.getAttackBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDefenseBonus() {
 assertEquals(10, sector.getDefenseBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testClassBonus() {
 assertEquals(5, sector.classBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetAreas() {
 assertEquals(areas, sector.getAreas());
 }

 @Test
 public void testAddArea() {
 sector.addArea(new Area(3, 3));
 assertEquals(3, sector.getAreas().size());
 }

 @Test
 public void testHashCode() {
 Set<Area> areas = new HashSet<>();
 areas.add(area1);
 areas.add(area2);
 SimpleSector sector1 = new SimpleSector(5, true, true, 5, 5, true, 5, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 5, 5, true, 5, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 5, 5, true, 5, CharacterClass.MAGICIAN);
 sector1.setAreas(areas);
 sector2.setAreas(areas);
 assertEquals(sector1.hashCode(), sector2.hashCode());
 assertNotEquals(sector1.hashCode(), sector3.hashCode());
 }

 @Test
 public void testEquals() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 5, 5, true, 5, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 5, 5, true, 5, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 5, 5, true, 5, CharacterClass.MAGICIAN);
 assertTrue(sector1.equals(sector2));
 assertFalse(sector1.equals(sector3));
 }

 @Test
 public void testObserverOccupierChanged() {
 sector.gainControl(CharacterSide.DEFENDER);
 sectorObserver.reset();
 sector.occupierChanged();
 assertTrue(sectorObserver.isOccupierChangedCalled());
 }
}