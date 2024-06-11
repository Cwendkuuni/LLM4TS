package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;

public class SimpleSectorTest {

    @Test
    public void testSimpleSectorCreation() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.Archer);
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertEquals(10, sector.getAttackBonus(CharacterClass.Archer));
        assertEquals(5, sector.getDefenseBonus(CharacterClass.Archer));
        assertFalse(sector.isOccupied());
        assertFalse(sector.isFlag());
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
        assertEquals(0, sector.occupierCount);
        Set<Area> expectedAreas = new HashSet<>(Arrays.asList(new Area(), new Area(), new Area()));
        assertEquals(expectedAreas, sector.getAreas());
        assertEquals(0, sector.hashCode());
    }

    @Test
    public void testSimpleSectorFree() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.Archer);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertFalse(sector.isOccupied());
        assertEquals(0, sector.occupierCount);
    }

    @Test(expected = AssertionError.class)
    public void testSimpleSectorFreeError() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.Archer);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        sector.free();
    }

    @Test
    public void testSimpleSectorGainControl() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.Archer);
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertEquals(1, sector.occupierCount);
    }

    @Test
    public void testSimpleSectorGainControlSameSide() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.Archer);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.occupierCount);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testSimpleSectorGainControlDifferentSide() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.Archer);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.occupierCount);
        assertFalse(sector.isOccupiedBy(CombatantSide.DEFENDER));
        assertTrue(sector.isOccupiedBy(CombatantSide.ATTACKER));
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testSimpleSectorGetOccupier() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.Archer);
        sector.gainControl(CombatantSide.DEFENDER);
        Character combatant = new Character("Test", CharacterClass.Archer, null);
        sector._addArea(new Area(sector, combatant));
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testSimpleSectorIsOccupied() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.Archer);
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testSimpleSectorGetBonus() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.Archer);
        assertEquals(new SectorBonus(15, 10, 5), sector.getBonus(CharacterClass.Archer));
        assertEquals(new SectorBonus(10, 5, 5), sector.getBonus(CharacterClass.Knight));
    }

    @Test
    public void testSimpleSectorGetDamageBonus() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.Archer);
        Character combatant = new Character("Test", CharacterClass.Archer, new Weapon("TestWeapon", 10, 5, SectorBonus.BonusType.DAMAGE));
        Area area = new Area(sector, combatant);
        sector._addArea(area);
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(15, sector.getDamageBonus());
    }

    @Test
    public void testSimpleSectorGetAttackBonus() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.Archer);
        Character combatant = new Character("Test", CharacterClass.Archer, null);
        Area area = new Area(sector, combatant);
        sector._addArea(area);
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(15, sector.getAttackBonus(CharacterClass.Archer));
    }

    @Test
    public void testSimpleSectorGetDefenseBonus() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.Archer);
        Character combatant = new Character("Test", CharacterClass.Archer, null);
        Area area = new Area(sector, combatant);
        sector._addArea(area);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(15, sector.getDefenseBonus(CharacterClass.Archer));
    }

    @Test
    public void testSimpleSectorClassBonus() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.Archer);
        assertEquals(5, sector.classBonus(CharacterClass.Archer));
        assertEquals(0, sector.classBonus(CharacterClass.Knight));
    }

    @Test
    public void testSimpleSectorHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.Archer);
        SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.Archer);
        SimpleSector sector3 = new SimpleSector(5, true, true, 11, 5, false, 5, CharacterClass.Archer);
        SimpleSector sector4 = new SimpleSector(5, true, true, 10, 5, false, 6, CharacterClass.Archer);
        SimpleSector sector5 = new SimpleSector(5, false, true, 10, 5, false, 5, CharacterClass.Archer);
        assertEquals(sector1.hashCode(), sector1.hashCode());
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
        assertNotEquals(sector1.hashCode(), sector4.hashCode());
        assertNotEquals(sector1.hashCode(), sector5.hashCode());
    }

    @Test
    public void testSimpleSectorEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.Archer);
        SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.Archer);
        SimpleSector sector3 = new SimpleSector(5, true, true, 11, 5, false, 5, CharacterClass.Archer);
        SimpleSector sector4 = new SimpleSector(5, true, true, 10, 5, false, 6, CharacterClass.Archer);
        SimpleSector sector5 = new SimpleSector(5, false, true, 10, 5, false, 5, CharacterClass.Archer);
        assertTrue(sector1.equals(sector1));
        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
        assertFalse(sector1.equals(sector4));
        assertFalse(sector1.equals(sector5));
    }
}