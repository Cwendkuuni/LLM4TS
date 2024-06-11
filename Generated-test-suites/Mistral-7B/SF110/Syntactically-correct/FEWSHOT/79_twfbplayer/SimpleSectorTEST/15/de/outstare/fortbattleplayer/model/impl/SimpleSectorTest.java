package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import de.outstare.fortbattleplayer.model.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {
    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
    }

    @Test
    public void testConstructor() {
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertFalse(sector.isFlag());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
        assertTrue(sector.areas.isEmpty());
        assertTrue(sector.observers.isEmpty());
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.getOccupierCount());
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
        sector.occupierChanged();
        assertTrue(sector.observers.isEmpty());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        sector.occupierChanged();
        assertTrue(sector.observers.isEmpty());

        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        sector.occupierChanged();
        assertTrue(sector.observers.isEmpty());
    }

    @Test
    public void testGetOccupier() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        sector.free();
        assertNull(sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
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
        assertTrue(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass knight = CharacterClass.KNIGHT;

        assertEquals(new SectorBonus(3, 2, 4), sector.getBonus(archer));
        assertEquals(new SectorBonus(3, 2, 4), sector.getBonus(knight));

        sector = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        assertEquals(new SectorBonus(4, 2, 4), sector.getBonus(archer));
        assertEquals(new SectorBonus(3, 2, 4), sector.getBonus(knight));

        sector = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.KNIGHT);
        assertEquals(new SectorBonus(3, 2, 4), sector.getBonus(archer));
        assertEquals(new SectorBonus(4, 2, 4), sector.getBonus(knight));
    }

    @Test
    public void testGetAttackBonus() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass knight = CharacterClass.KNIGHT;

        assertEquals(6, sector.getAttackBonus(archer));
        assertEquals(6, sector.getAttackBonus(knight));

        sector = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        assertEquals(7, sector.getAttackBonus(archer));
        assertEquals(6, sector.getAttackBonus(knight));

        sector = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.KNIGHT);
        assertEquals(6, sector.getAttackBonus(archer));
        assertEquals(7, sector.getAttackBonus(knight));
    }

    @Test
    public void testGetDefenseBonus() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass knight = CharacterClass.KNIGHT;

        assertEquals(2, sector.getDefenseBonus(archer));
        assertEquals(2, sector.getDefenseBonus(knight));

        sector = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        assertEquals(3, sector.getDefenseBonus(archer));
        assertEquals(2, sector.getDefenseBonus(knight));

        sector = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.KNIGHT);
        assertEquals(2, sector.getDefenseBonus(archer));
        assertEquals(3, sector.getDefenseBonus(knight));
    }

    @Test
    public void testGetDamageBonus() {
        assertEquals(4, sector.getDamageBonus());
    }

    @Test
    public void testAddArea() {
        Area area = new Area();
        sector.addArea(area);
        assertTrue(sector.areas.contains(area));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAreaThrowException() {
        SimpleSector sector = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        sector.areas.add(new Area());
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, false, true, 1, 2, false, 3, CharacterClass.ARCHER);

        int hashCode1 = sector1.hashCode();
        int hashCode2 = sector2.hashCode();
        int hashCode3 = sector3.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, false, true, 1, 2, false, 3, CharacterClass.ARCHER);

        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
    }

    @Test
    public void testAddObserver() {
        SimpleSectorObserver observer = mock(SimpleSectorObserver.class);
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddObserverThrowException() {
        SimpleSector sector = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        sector.observers.add(mock(SimpleSectorObserver.class));
    }

    @Test
    public void testRemoveObserver() {
        SimpleSectorObserver observer = mock(SimpleSectorObserver.class);
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.observers.contains(observer));
    }
}