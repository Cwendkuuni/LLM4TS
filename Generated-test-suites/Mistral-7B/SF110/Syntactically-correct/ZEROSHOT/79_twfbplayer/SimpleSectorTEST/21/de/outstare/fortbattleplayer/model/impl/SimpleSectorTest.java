package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

    @Test
    public void testSimpleSectorConstruction() {
        SimpleSector sector = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertEquals(1, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(2, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertTrue(sector.isFlag());
        assertEquals(3, sector.getClassBonus());
        assertEquals(CharacterClass.ARCHER, sector.getClassType());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
        assertEmpty(sector.getAreas());
        assertEmpty(sector.getObservers());
    }

    @Test
    public void testFreeMethod() {
        SimpleSector sector = createTestSector();
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }

    @Test(expected = AssertionError.class)
    public void testFreeMethodWithNegativeOccupierCount() {
        SimpleSector sector = createTestSector();
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        sector.free();
    }

    @Test
    public void testGainControlMethod() {
        SimpleSector sector = createTestSector();
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertEquals(1, sector.getOccupierCount());
    }

    @Test
    public void testIsOccupiedMethod() {
        SimpleSector sector = createTestSector();
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetOccupierMethod() {
        SimpleSector sector = createTestSector();
        assertNull(sector.getOccupier());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testAddAreaMethod() {
        SimpleSector sector = createTestSector();
        SimpleArea area = new SimpleArea(1, 1, null, null);
        sector.addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }

    @Test
    public void testGetAreasMethod() {
        SimpleSector sector = createTestSector();
        SimpleArea area1 = new SimpleArea(1, 1, null, null);
        SimpleArea area2 = new SimpleArea(1, 1, null, null);
        sector.addArea(area1);
        sector.addArea(area2);
        Set<Area> expectedAreas = new HashSet<>(Arrays.asList(area1, area2));
        assertEquals(expectedAreas, sector.getAreas());
    }

    @Test
    public void testHashCodeMethod() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
    }

    @Test
    public void testEqualsMethod() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, true, 1, 2, false, 3, CharacterClass.ARCHER);
        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
    }

    @Test
    public void testAddAndRemoveObserverMethod() {
        SimpleSector sector = createTestSector();
        SimpleSectorObserver observer = new SimpleSectorObserver();
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }

    @Test
    public void testBonusMethods() {
        SimpleSector sector = createTestSector();
        SimpleWeapon weapon = new SimpleWeapon("TestWeapon", 10, 5, null);
        Combatant combatant = new SimpleCombatant("TestCombatant", CharacterClass.ARCHER, 10, 10, weapon);
        SimpleArea area = new SimpleArea(1, 1, combatant, null);
        sector.addArea(area);

        // Test getDamageBonus()
        int damageBonus = sector.getDamageBonus();
        assertEquals(0, damageBonus);
        sector.gainControl(CombatantSide.ATTACKER);
        damageBonus = sector.getDamageBonus();
        assertEquals(5, damageBonus);

        // Test getAttackBonus()
        int attackBonus = sector.getAttackBonus(CharacterClass.ARCHER);
        assertEquals(1, attackBonus);
        sector.gainControl(CombatantSide.ATTACKER);
        attackBonus = sector.getAttackBonus(CharacterClass.ARCHER);
        assertEquals(6, attackBonus);

        // Test getDefenseBonus()
        int defenseBonus = sector.getDefenseBonus(CharacterClass.ARCHER);
        assertEquals(2, defenseBonus);
        sector.gainControl(CombatantSide.DEFENDER);
        defenseBonus = sector.getDefenseBonus(CharacterClass.ARCHER);
        assertEquals(3, defenseBonus);
    }

    private SimpleSector createTestSector() {
        return new SimpleSector(5, true, true, 1, 2, true, 3, CharacterClass.ARCHER);
    }

}

class SimpleSectorObserver {
    public void occupierChanged() {
        // Implement the observer logic here
    }
}