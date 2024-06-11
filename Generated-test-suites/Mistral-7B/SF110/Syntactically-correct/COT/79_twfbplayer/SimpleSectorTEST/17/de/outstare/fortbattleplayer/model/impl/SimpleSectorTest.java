package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SectorObserver;

public class SimpleSectorTest {

    private SimpleSector sector;
    private Set<Area> areas;
    private Area area1;
    private Area area2;
    private Combatant combatant;
    private CharacterClass charClass;
    private SectorObserver sectorObserver;

    @Before
    public void setup() {
        this.charClass = CharacterClass.ARCHER;
        this.sector = new SimpleSector(5, true, true, 2, 3, false, 4, this.charClass);
        this.areas = new HashSet<>();
        this.area1 = new Area(3, 3);
        this.area2 = new Area(3, 4);
        this.combatant = new Combatant("John", this.charClass, 100, new Weapon("Bow", 10, 5));
        this.sectorObserver = new SectorObserver() {
            @Override
            public void occupierChanged() {
            }
        };
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.sector);
    }

    @Test
    public void testFree() {
        this.sector.gainControl(CombatantSide.DEFENDER);
        this.sector.free();
        assertNull(this.sector.getOccupier());
        this.sector.gainControl(CombatantSide.DEFENDER);
        this.sector.free();
        assertNull(this.sector.getOccupier());
    }

    @Test(expected = AssertionError.class)
    public void testFreeNegativeOccupierCount() {
        this.sector.gainControl(CombatantSide.DEFENDER);
        this.sector.free();
        this.sector.gainControl(CombatantSide.DEFENDER);
        this.sector.gainControl(CombatantSide.DEFENDER);
        this.sector.free();
        this.sector.free();
    }

    @Test
    public void testGainControl() {
        assertNull(this.sector.getOccupier());
        this.sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, this.sector.getOccupier());
        this.sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, this.sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        assertFalse(this.sector.isOccupied());
        this.sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(this.sector.isOccupied());
        this.sector.free();
        assertFalse(this.sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        assertEquals(5, this.sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        assertTrue(this.sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        assertTrue(this.sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        assertFalse(this.sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SectorBonus bonus = this.sector.getBonus(this.charClass);
        assertEquals(2, bonus.getAttackBonus());
        assertEquals(3, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetDamageBonus() {
        assertEquals(0, this.sector.getDamageBonus());
    }

    @Test
    public void testGetAttackBonus() {
        int attackBonus = this.sector.getAttackBonus(this.charClass);
        assertEquals(6, attackBonus);
    }

    @Test
    public void testGetDefenseBonus() {
        int defenseBonus = this.sector.getDefenseBonus(this.charClass);
        assertEquals(7, defenseBonus);
    }

    @Test
    public void testClassBonus() {
        assertEquals(4, this.sector.classBonus(this.charClass));
    }

    @Test
    public void testGetAreas() {
        assertNotNull(this.sector.getAreas());
        assertTrue(this.sector.getAreas().isEmpty());
    }

    @Test
    public void testAddArea() {
        this.sector._addArea(this.area1);
        this.sector._addArea(this.area2);
        assertEquals(2, this.sector.getAreas().size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAreaUnsupportedOperationException() {
        this.sector.getAreas().add(this.area1);
    }

    @Test
    public void testHashCode() {
        SimpleSector simpleSector1 = new SimpleSector(5, true, true, 2, 3, false, 4, this.charClass);
        SimpleSector simpleSector2 = new SimpleSector(5, true, true, 2, 3, false, 4, this.charClass);
        Set<Area> areas1 = new HashSet<>(Arrays.asList(this.area1, this.area2));
        Set<Area> areas2 = new HashSet<>(Arrays.asList(this.area1, this.area2));
        SimpleSector sector1 = new SimpleSector(5, true, true, 2, 3, false, 4, this.charClass, areas1);
        SimpleSector sector2 = new SimpleSector(5, true, true, 2, 3, false, 4, this.charClass, areas2);
        int hashCode1 = sector1.hashCode();
        int hashCode2 = sector2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        SimpleSector simpleSector1 = new SimpleSector(5, true, true, 2, 3, false, 4, this.charClass);
        SimpleSector simpleSector2 = new SimpleSector(5, true, true, 2, 3, false, 4, this.charClass);
        SimpleSector simpleSector3 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        SimpleSector simpleSector4 = new SimpleSector(5, true, true, 3, 3, false, 4, this.charClass);
        Set<Area> areas1 = new HashSet<>(Arrays.asList(this.area1, this.area2));
        Set<Area> areas2 = new HashSet<>(Arrays.asList(this.area1, this.area2));
        SimpleSector sector1 = new SimpleSector(5, true, true, 2, 3, false, 4, this.charClass, areas1);
        SimpleSector sector2 = new SimpleSector(5, true, true, 2, 3, false, 4, this.charClass, areas2);
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertTrue(simpleSector1.equals(simpleSector1));
        assertTrue(simpleSector1.equals(simpleSector2));
        assertFalse(simpleSector1.equals(simpleSector3));
        assertFalse(simpleSector1.equals(simpleSector4));
        assertFalse(simpleSector1.equals(obj1));
        assertFalse(simpleSector1.equals(obj2));
    }

    @Test
    public void testAddObserver() {
        this.sector.addObserver(this.sectorObserver);
        this.sector.occupierChanged();
        assertTrue(this.sectorObserver.isOccupierChangedCalled());
        this.sector.removeObserver(this.sectorObserver);
        this.sector.occupierChanged();
        assertFalse(this.sectorObserver.isOccupierChangedCalled());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddObserverIllegalArgumentException() {
        this.sector.addObserver(null);
    }

}