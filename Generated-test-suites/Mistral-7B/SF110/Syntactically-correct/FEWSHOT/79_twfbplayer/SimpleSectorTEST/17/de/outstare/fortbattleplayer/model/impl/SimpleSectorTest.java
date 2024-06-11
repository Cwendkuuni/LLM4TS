package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, true, 5, 7, false, 1, CharacterClass.ARCHER);
    }

    @Test
    public void testConstructor() {
        assertNotNull(sector);
        assertEquals(10, sector.getHeight());
        assertFalse(sector.isFlag());
        assertNull(sector.getOccupier());
        assertTrue(sector.isOccupied());
        assertEquals(0, sector.getOccupierCount());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertEquals(1, sector.getClassBonus());
        assertEquals(CharacterClass.ARCHER, sector.getClassType());
        assertNotNull(sector.getAreas());
        assertTrue(sector.observers.isEmpty());
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.getOccupierCount());
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }

    @Test
    public void testGainControl() {
        sector.free();
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());

        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());

        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());

        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGetBonus() {
        sector.addArea(new Area(1, 1, new Combatant(CharacterClass.ARCHER, new Weapon("Sword", 1))));
        sector.addArea(new Area(1, 2, new Combatant(CharacterClass.ARCHER, new Weapon("Bow", 2))));

        SectorBonus bonus = sector.getBonus(CharacterClass.ARCHER);
        assertEquals(12, bonus.getAttackBonus());
        assertEquals(10, bonus.getDefenseBonus());
        assertEquals(5, bonus.getDamageBonus());
    }

    @Test
    public void testGetDamageBonus() {
        sector.addArea(new Area(1, 1, new Combatant(CharacterClass.ARCHER, new Weapon("Sword", 1))));
        sector.addArea(new Area(1, 2, new Combatant(CharacterClass.ARCHER, new Weapon("Bow", 2))));

        assertEquals(5, sector.getDamageBonus());
    }

    @Test
    public void testGetAttackBonus() {
        sector.addArea(new Area(1, 1, new Combatant(CharacterClass.ARCHER, new Weapon("Sword", 1))));
        sector.addArea(new Area(1, 2, new Combatant(CharacterClass.ARCHER, new Weapon("Bow", 2))));

        assertEquals(15, sector.getAttackBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetDefenseBonus() {
        sector.addArea(new Area(1, 1, new Combatant(CharacterClass.ARCHER, new Weapon("Sword", 1))));
        sector.addArea(new Area(1, 2, new Combatant(CharacterClass.ARCHER, new Weapon("Bow", 2))));

        assertEquals(8, sector.getDefenseBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testClassBonus() {
        assertEquals(1, sector.classBonus(CharacterClass.ARCHER));
        assertEquals(0, sector.classBonus(CharacterClass.KNIGHT));
    }

    @Test
    public void testAddArea() {
        sector.free();
        Area area = new Area(1, 1, new Combatant(CharacterClass.ARCHER, new Weapon("Sword", 1)));
        sector._addArea(area);
        assertEquals(1, sector.getAreas().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAreaFail() {
        sector.addArea(null);
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(10, true, true, 5, 7, false, 1, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(10, true, true, 5, 7, false, 1, CharacterClass.ARCHER);

        assertEquals(sector1.hashCode(), sector2.hashCode());

        sector1.addArea(new Area(1, 1, new Combatant(CharacterClass.ARCHER, new Weapon("Sword", 1))));
        assertNotEquals(sector1.hashCode(), sector2.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(10, true, true, 5, 7, false, 1, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(10, true, true, 5, 7, false, 1, CharacterClass.ARCHER);

        assertEquals(sector1, sector2);

        sector1.addArea(new Area(1, 1, new Combatant(CharacterClass.ARCHER, new Weapon("Sword", 1))));
        assertNotEquals(sector1, sector2);
    }

    @Test
    public void testAddObserver() {
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.observers.contains(observer));
    }

    @After
    public void tearDown() {
        sector = null;
    }
}