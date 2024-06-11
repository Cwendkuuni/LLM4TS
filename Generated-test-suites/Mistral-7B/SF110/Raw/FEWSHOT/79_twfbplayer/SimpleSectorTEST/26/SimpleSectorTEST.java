package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.Weapon;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }

    @Test(expected = AssertionError.class)
    public void testFreeWithNegativeOccupierCount() {
        sector.free();
    }

    @Test
    public void testGainControl() {
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertEquals(1, sector.getOccupierCount());
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertEquals(2, sector.getOccupierCount());
    }

    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDER);
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
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass knight = CharacterClass.KNIGHT;
        SectorBonus bonus = sector.getBonus(archer);
        assertEquals(1 + 2, bonus.getAttackBonus());
        assertEquals(1 + 2, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
        bonus = sector.getBonus(knight);
        assertEquals(1, bonus.getAttackBonus());
        assertEquals(1, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAttackBonus() {
        CharacterClass archer = CharacterClass.ARCHER;
        assertEquals(2, sector.getAttackBonus(archer));
    }

    @Test
    public void testGetDefenseBonus() {
        CharacterClass archer = CharacterClass.ARCHER;
        assertEquals(2, sector.getDefenseBonus(archer));
    }

    @Test
    public void testClassBonus() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass knight = CharacterClass.KNIGHT;
        assertEquals(2, sector.classBonus(archer));
        assertEquals(0, sector.classBonus(knight));
    }

    @Test
    public void testGetAreas() {
        Set<Area> areas = new HashSet<>();
        sector.setAreas(areas);
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(1, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(1, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(1, true, false, 2, 1, false, 2, CharacterClass.ARCHER);
        assertEquals(sector1.hashCode(), sector1.hashCode());
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(1, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(1, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(1, true, false, 1, 1, false, 3, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(1, true, false, 1, 1, true, 2, CharacterClass.ARCHER);
        SimpleSector sector5 = new SimpleSector(1, false, false, 1, 1, false, 2, CharacterClass.ARCHER);
        assertTrue(sector1.equals(sector1));
        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
        assertFalse(sector1.equals(sector4));
        assertFalse(sector1.equals(sector5));
    }

    @Test
    public void testAddObserver() {
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }
}

class SimpleSectorTestHelper {

    @Test
    public void testGetBonusFromWeapons() {
        SimpleSector sector = new SimpleSector(1, false, false, 0, 0, false, 0, null);
        Area area1 = new Area(1, new Combatant(CharacterClass.ARCHER, "archer1", null, null), null, null);
        Area area2 = new Area(2, new Combatant(CharacterClass.ARCHER, "archer2", null, null), null, null);
        sector.setAreas(Arrays.asList(area1, area2));
        Weapon weapon1 = new Weapon("weapon1", 1, SectorBonus.BonusType.ATTACK);
        Weapon weapon2 = new Weapon("weapon2", 2, SectorBonus.BonusType.DEFENSE);
        area1.setOccupier(new Combatant(CharacterClass.ARCHER, "archer1", null, weapon1));
        area2.setOccupier(new Combatant(CharacterClass.ARCHER, "archer2", null, weapon2));
        assertEquals(1, sector.getBonusFromWeapons(SectorBonus.BonusType.ATTACK));
        assertEquals(2, sector.getBonusFromWeapons(SectorBonus.BonusType.DEFENSE));
    }
}