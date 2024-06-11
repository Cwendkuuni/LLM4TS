package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.BonusType;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.TestArea;
import de.outstare.fortbattleplayer.model.impl.TestCharacterClass;
import de.outstare.fortbattleplayer.model.impl.TestWeapon;

public class SimpleSectorTest {

    @Test
    public void testConstructor() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
        assertEquals(true, sector.isDefenderSpawn());
        assertEquals(true, sector.isAttackerSpawn());
        assertEquals(10, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(5, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertEquals(false, sector.isFlag());
        assertEquals(5, sector.getClassBonus());
        assertEquals(CharacterClass.ARCHER, sector.getClassType());
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
        assertEquals(new HashSet<Area>(), sector.getAreas());
        assertEquals(new HashSet<SectorObserver>(), sector.getObservers());
    }

    @Test
    public void testFree() {
        SimpleSector sector = createTestSector();
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = createTestSector();
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(2, sector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        SimpleSector sector = createTestSector();
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        SimpleSector sector = createTestSector();
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        SimpleSector sector = createTestSector();
        assertEquals(5, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = createTestSector();
        assertEquals(true, sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = createTestSector();
        assertEquals(true, sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        SimpleSector sector = createTestSector();
        assertEquals(false, sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = createTestSector();
        assertEquals(new SectorBonus(15, 10, 5), sector.getBonus(CharacterClass.ARCHER));
        assertEquals(new SectorBonus(15, 10, 5), sector.getBonus(CharacterClass.ARCHER_HEAVY));
    }

    @Test
    public void testGetAttackBonus() {
        SimpleSector sector = createTestSector();
        assertEquals(10, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(15, sector.getAttackBonus(CharacterClass.ARCHER_HEAVY));
    }

    @Test
    public void testGetDefenseBonus() {
        SimpleSector sector = createTestSector();
        assertEquals(5, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertEquals(10, sector.getDefenseBonus(CharacterClass.ARCHER_HEAVY));
    }

    @Test
    public void testGetDamageBonus() {
        SimpleSector sector = createTestSector();
        TestWeapon weapon = new TestWeapon();
        TestCharacterClass character = new TestCharacterClass();
        Combatant combatant = new Combatant(character, weapon);
        TestArea area = new TestArea();
        area.setOccupier(combatant);
        sector.addArea(area);
        assertEquals(5, sector.getDamageBonus());
        sector.getAreas().remove(area);
        area.setOccupier(null);
    }

    @Test
    public void testClassBonus() {
        SimpleSector sector = createTestSector();
        assertEquals(5, sector.classBonus(CharacterClass.ARCHER));
        assertEquals(5, sector.classBonus(CharacterClass.ARCHER_HEAVY));
        assertEquals(5, sector.classBonus(CharacterClass.ARCHER_AGILE));
        assertEquals(0, sector.classBonus(CharacterClass.ARCHER_HEAVY_AGILE));
    }

    @Test
    public void testGetAreas() {
        SimpleSector sector = createTestSector();
        Set<Area> areas = new HashSet<Area>(Arrays.asList(new TestArea(), new TestArea()));
        sector.addArea(new TestArea());
        assertEquals(areas, sector.getAreas());
        sector.removeArea(new TestArea());
        assertEquals(new HashSet<Area>(), sector.getAreas());
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, true, 11, 5, false, 5, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, true, true, 10, 5, true, 5, CharacterClass.ARCHER);
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
        assertNotEquals(sector1.hashCode(), sector4.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, true, 11, 5, false, 5, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, true, true, 10, 5, true, 5, CharacterClass.ARCHER);
        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
        assertFalse(sector1.equals(sector4));
    }

    @Test
    public void testAddObserver() {
        SimpleSector sector = createTestSector();
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        assertEquals(1, sector.getObservers().size());
    }

    @Test
    public void testRemoveObserver() {
        SimpleSector sector = createTestSector();
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertEquals(0, sector.getObservers().size());
    }

    private SimpleSector createTestSector() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
        sector.addObserver(mock(SectorObserver.class));
        return sector;
    }
}