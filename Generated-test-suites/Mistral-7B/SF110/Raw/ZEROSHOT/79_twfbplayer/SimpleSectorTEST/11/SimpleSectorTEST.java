package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;

public class SimpleSectorTest {

	@Test
	public void testSimpleSectorConstruction() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		assertEquals(5, sector.getHeight());
		assertTrue(sector.isDefenderSpawn());
		assertTrue(sector.isAttackerSpawn());
		assertEquals(2, sector.getAttackBonus(CharacterClass.ARCHER));
		assertEquals(3, sector.getDefenseBonus(CharacterClass.ARCHER));
		assertTrue(sector.isFlag());
		assertEquals(4, sector.getClassBonus());
		assertEquals(CharacterClass.ARCHER, sector.getClassType());
		assertNull(sector.getOccupier());
		assertFalse(sector.isOccupied());
		assertEmpty(sector.getAreas());
		assertEquals(0, sector.hashCode());
	}

	@Test
	public void testFree() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		Character occupier = new Character("test", CharacterClass.ARCHER);
		sector.gainControl(CombatantSide.DEFENDER);
		assertEquals(1, sector.getOccupierCount());
		sector.free();
		assertEquals(0, sector.getOccupierCount());
		assertNull(sector.getOccupier());
		assertFalse(sector.isOccupied());
	}

	@Test
	public void testGainControl() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		Character occupier = new Character("test", CharacterClass.ARCHER);
		assertEquals(0, sector.getOccupierCount());
		assertNull(sector.getOccupier());
		assertFalse(sector.isOccupied());
		sector.gainControl(CombatantSide.DEFENDER);
		assertEquals(1, sector.getOccupierCount());
		assertEquals(CombatantSide.DEFENDER, sector.getOccupier().getSide());
		assertTrue(sector.isOccupied());
	}

	@Test
	public void testGetOccupier() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		Character occupier = new Character("test", CharacterClass.ARCHER);
		assertEquals(0, sector.getOccupierCount());
		assertNull(sector.getOccupier());
		assertFalse(sector.isOccupied());
		sector.gainControl(CombatantSide.DEFENDER);
		assertEquals(1, sector.getOccupierCount());
		assertEquals(CombatantSide.DEFENDER, sector.getOccupier().getSide());
		assertTrue(sector.isOccupied());
	}

	@Test
	public void testIsOccupied() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		Character occupier = new Character("test", CharacterClass.ARCHER);
		assertEquals(0, sector.getOccupierCount());
		assertFalse(sector.isOccupied());
		sector.gainControl(CombatantSide.DEFENDER);
		assertEquals(1, sector.getOccupierCount());
		assertTrue(sector.isOccupied());
	}

	@Test
	public void testGetHeight() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		assertEquals(5, sector.getHeight());
	}

	@Test
	public void testIsDefenderSpawn() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		assertTrue(sector.isDefenderSpawn());
	}

	@Test
	public void testIsAttackerSpawn() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		assertTrue(sector.isAttackerSpawn());
	}

	@Test
	public void testIsFlag() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		assertTrue(sector.isFlag());
	}

	@Test
	public void testGetBonus() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		SectorBonus bonus = sector.getBonus(CharacterClass.ARCHER);
		assertEquals(2, bonus.getAttackBonus());
		assertEquals(3, bonus.getDefenseBonus());
		assertEquals(0, bonus.getDamageBonus());
	}

	@Test
	public void testGetAttackBonus() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		assertEquals(4, sector.getAttackBonus(CharacterClass.ARCHER));
	}

	@Test
	public void testGetDefenseBonus() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		assertEquals(7, sector.getDefenseBonus(CharacterClass.ARCHER));
	}

	@Test
	public void testClassBonus() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		assertEquals(4, sector.classBonus(CharacterClass.ARCHER));
	}

	@Test
	public void testGetAreas() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		Set<Area> areas = new HashSet<>();
		sector.setAreas(areas);
		assertEquals(areas, sector.getAreas());
	}

	@Test
	public void testAddArea() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		Area area = new Area();
		sector.addArea(area);
		assertEquals(1, sector.getAreas().size());
		assertEquals(area, sector.getAreas().iterator().next());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testRemoveArea() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		Area area = new Area();
		sector.addArea(area);
		sector.removeArea(area);
	}

	@Test
	public void testHashCode() {
		SimpleSector sector1 = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		SimpleSector sector2 = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		SimpleSector sector3 = new SimpleSector(6, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		SimpleSector sector4 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
		SimpleSector sector5 = new SimpleSector(5, true, true, 2, 4, true, 4, CharacterClass.ARCHER);
		assertEquals(Integer.hashCode(5), sector1.hashCode());
		assertNotEquals(sector1.hashCode(), sector2.hashCode());
		assertNotEquals(sector1.hashCode(), sector3.hashCode());
		assertNotEquals(sector1.hashCode(), sector4.hashCode());
		assertNotEquals(sector1.hashCode(), sector5.hashCode());
	}

	@Test
	public void testEquals() {
		SimpleSector sector1 = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		SimpleSector sector2 = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		SimpleSector sector3 = new SimpleSector(6, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		SimpleSector sector4 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
		SimpleSector sector5 = new SimpleSector(5, true, true, 2, 4, true, 4, CharacterClass.ARCHER);
		assertTrue(sector1.equals(sector1));
		assertTrue(sector1.equals(sector2));
		assertFalse(sector1.equals(sector3));
		assertFalse(sector1.equals(sector4));
		assertFalse(sector1.equals(sector5));
	}

	@Test
	public void testAddObserver() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		SectorObserver observer = mock(SectorObserver.class);
		sector.addObserver(observer);
		assertEquals(1, sector.getObservers().size());
		assertTrue(sector.getObservers().contains(observer));
	}

	@Test
	public void testRemoveObserver() {
		SimpleSector sector = new SimpleSector(5, true, true, 2, 3, true, 4, CharacterClass.ARCHER);
		SectorObserver observer = mock(SectorObserver.class);
		sector.addObserver(observer);
		sector.removeObserver(observer);
		assertEquals(0, sector.getObservers().size());
		assertFalse(sector.getObservers().contains(observer));
	}
}