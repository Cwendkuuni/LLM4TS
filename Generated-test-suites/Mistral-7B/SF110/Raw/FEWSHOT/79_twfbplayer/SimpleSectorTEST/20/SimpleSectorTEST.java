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
        CharacterClass classType = new Warrior();
        sector = new SimpleSector(5, true, false, 10, 5, false, 5, classType);
    }

    @Test
    public void testConstructor() {
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertFalse(sector.isAttackerSpawn());
        assertEquals(0, sector.getAttackBonus(null));
        assertEquals(0, sector.getDefenseBonus(null));
        assertFalse(sector.isFlag());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
        assertTrue(sector.getAreas().isEmpty());
        assertTrue(sector.getObservers().isEmpty());
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.DEFENDERS);
        sector.gainControl(CombatantSide.DEFENDERS);
        sector.gainControl(CombatantSide.DEFENDERS);
        sector.free();
        assertTrue(sector.isFree());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKERS);
        assertEquals(CombatantSide.ATTACKERS, sector.getOccupier());
        assertTrue(sector.isOccupied());
        assertEquals(1, sector.getOccupierCount());
    }

    @Test
    public void testIsOccupied() {
        sector.gainControl(CombatantSide.DEFENDERS);
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
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        CharacterClass warrior = new Warrior();
        CharacterClass archer = new Archer();
        assertEquals(new SectorBonus(10, 5, 0), sector.getBonus(warrior));
        assertEquals(new SectorBonus(10, 5, 0), sector.getBonus(archer));
        sector.addArea(new Area(5, 5, new Defender(warrior)));
        assertEquals(new SectorBonus(15, 7, 0), sector.getBonus(warrior));
        assertEquals(new SectorBonus(10, 5, 0), sector.getBonus(archer));
    }

    @Test
    public void testGetAttackBonus() {
        CharacterClass warrior = new Warrior();
        CharacterClass archer = new Archer();
        assertEquals(5, sector.getAttackBonus(warrior));
        assertEquals(0, sector.getAttackBonus(archer));
        sector.addArea(new Area(5, 5, new Defender(warrior)));
        assertEquals(8, sector.getAttackBonus(warrior));
        assertEquals(0, sector.getAttackBonus(archer));
    }

    @Test
    public void testGetDefenseBonus() {
        CharacterClass warrior = new Warrior();
        CharacterClass archer = new Archer();
        assertEquals(5, sector.getDefenseBonus(warrior));
        assertEquals(0, sector.getDefenseBonus(archer));
        sector.addArea(new Area(5, 5, new Defender(warrior)));
        assertEquals(8, sector.getDefenseBonus(warrior));
        assertEquals(0, sector.getDefenseBonus(archer));
    }

    @Test
    public void testClassBonus() {
        CharacterClass warrior = new Warrior();
        CharacterClass archer = new Archer();
        CharacterClass mage = new Mage();
        assertEquals(5, sector.classBonus(warrior));
        assertEquals(0, sector.classBonus(archer));
        assertEquals(5, sector.classBonus(mage));
        sector.setClassType(CharacterClass.ARCHER);
        assertEquals(0, sector.classBonus(warrior));
        assertEquals(5, sector.classBonus(archer));
        assertEquals(0, sector.classBonus(mage));
    }

    @Test
    public void testGetAreas() {
        Set<Area> areas = new HashSet<>();
        areas.add(new Area(5, 5, new Defender(new Warrior())));
        areas.add(new Area(5, 5, new Defender(new Archer())));
        sector.setAreas(areas);
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testAddArea() {
        Set<Area> areas = new HashSet<>();
        Area area = new Area(5, 5, new Defender(new Warrior()));
        sector.addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 5, false, 5, new Warrior());
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, false, 5, new Warrior());
        SimpleSector sector3 = new SimpleSector(6, true, false, 10, 5, false, 5, new Warrior());
        Set<Area> areas = new HashSet<>();
        Area area = new Area(5, 5, new Defender(new Warrior()));
        sector.addArea(area);
        assertEquals(sector.hashCode(), sector1.hashCode());
        assertNotEquals(sector.hashCode(), sector2.hashCode());
        assertNotEquals(sector.hashCode(), sector3.hashCode());
        Set<Area> areas2 = new HashSet<>(Arrays.asList(area, new Area(5, 5, new Defender(new Archer()))));
        SimpleSector sector4 = new SimpleSector(5, true, false, 10, 5, false, 5, new Warrior());
        sector4.setAreas(areas2);
        assertEquals(sector.hashCode(), sector4.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 5, false, 5, new Warrior());
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, false, 5, new Warrior());
        SimpleSector sector3 = new SimpleSector(6, true, false, 10, 5, false, 5, new Warrior());
        SimpleSector sector4 = new SimpleSector(5, true, false, 10, 5, false, 5, new Warrior());
        Set<Area> areas = new HashSet<>();
        Area area = new Area(5, 5, new Defender(new Warrior()));
        sector.addArea(area);
        assertTrue(sector1.equals(sector));
        assertTrue(sector.equals(sector1));
        assertNotEquals(sector, sector3);
        assertNotEquals(sector, sector4);
        SimpleSector sector5 = new SimpleSector(5, true, false, 10, 5, false, 5, new Warrior());
        sector5.setAreas(areas);
        assertTrue(sector.equals(sector5));
        assertTrue(sector5.equals(sector));
    }

    @Test
    public void testAddObserver() {
        SimpleSectorObserver observer = mock(SimpleSectorObserver.class);
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        SimpleSectorObserver observer = mock(SimpleSectorObserver.class);
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }
}