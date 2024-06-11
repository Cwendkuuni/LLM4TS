package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.test.TestData;
import de.outstare.fortbattleplayer.model.observer.SectorObserver;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
    }

    @Test
    public void testConstructor() {
        assertNotNull(sector);
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertEquals(null, sector.getOccupier());
    }

    @Test(expected = AssertionError.class)
    public void testFreeWithNegativeOccupierCount() {
        sector.free();
    }

    @Test
    public void testGainControl() {
        sector.free();
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test(expected = IllegalStateException.class)
    public void testGainControlWithSameSide() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
    }

    @Test
    public void testGetOccupier() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
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
        assertEquals(1, sector.getHeight());
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
        CharacterClass charClass = CharacterClass.ARCHER;
        SectorBonus bonus = sector.getBonus(charClass);
        assertEquals(0, bonus.getAttackBonus());
        assertEquals(0, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());

        sector = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
        bonus = sector.getBonus(charClass);
        assertEquals(1, bonus.getAttackBonus());
        assertEquals(1, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());

        sector = new SimpleSector(1, true, true, 1, 1, false, 2, CharacterClass.ARCHER);
        bonus = sector.getBonus(charClass);
        assertEquals(1, bonus.getAttackBonus());
        assertEquals(1, bonus.getDefenseBonus());
        assertEquals(2, bonus.getDamageBonus());

        CharacterClass charClass2 = CharacterClass.KNIGHT;
        bonus = sector.getBonus(charClass2);
        assertEquals(0, bonus.getAttackBonus());
        assertEquals(0, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetDamageBonus() {
        assertEquals(0, sector.getDamageBonus());

        Area area = new TestData().getTestArea();
        sector._addArea(area);
        Combatant combatant = new TestData().getTestCombatant();
        area.setOccupier(combatant);
        Weapon weapon = new TestData().getTestWeapon();
        combatant.setWeapon(weapon);
        int damageBonus = sector.getDamageBonus();
        assertEquals(weapon.getSectorBonus(SectorBonus.BonusType.DAMAGE), damageBonus);

        sector = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
        area = new TestData().getTestArea();
        sector._addArea(area);
        combatant = new TestData().getTestCombatant();
        area.setOccupier(combatant);
        weapon = new TestData().getTestWeapon();
        combatant.setWeapon(weapon);
        damageBonus = sector.getDamageBonus();
        assertEquals(0, damageBonus);
    }

    @Test
    public void testGetAttackBonus() {
        CharacterClass charClass = CharacterClass.ARCHER;
        assertEquals(1, sector.getAttackBonus(charClass));

        sector = new SimpleSector(1, true, true, 1, 1, false, 2, CharacterClass.ARCHER);
        assertEquals(3, sector.getAttackBonus(charClass));

        charClass = CharacterClass.KNIGHT;
        assertEquals(0, sector.getAttackBonus(charClass));

        sector = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
        sector.setAttackBonus(10);
        assertEquals(10, sector.getAttackBonus(charClass));

        sector.setAttackBonus(-5);
        assertEquals(0, sector.getAttackBonus(charClass));
    }

    @Test
    public void testGetDefenseBonus() {
        CharacterClass charClass = CharacterClass.ARCHER;
        assertEquals(1, sector.getDefenseBonus(charClass));

        sector = new SimpleSector(1, true, true, 1, 1, false, 2, CharacterClass.ARCHER);
        assertEquals(3, sector.getDefenseBonus(charClass));

        charClass = CharacterClass.KNIGHT;
        assertEquals(0, sector.getDefenseBonus(charClass));

        sector = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
        sector.setDefenseBonus(10);
        assertEquals(10, sector.getDefenseBonus(charClass));

        sector.setDefenseBonus(-5);
        assertEquals(0, sector.getDefenseBonus(charClass));
    }

    @Test
    public void testClassBonus() {
        CharacterClass charClass = CharacterClass.ARCHER;
        assertEquals(1, sector.classBonus(charClass));

        charClass = CharacterClass.KNIGHT;
        assertEquals(0, sector.classBonus(charClass));

        charClass = CharacterClass.ARCHER;
        sector.setClassType(CharacterClass.KNIGHT);
        assertEquals(0, sector.classBonus(charClass));

        sector.setClassType(CharacterClass.ARCHER);
        sector.setClassBonus(10);
        assertEquals(10, sector.classBonus(charClass));

        sector.setClassType(CharacterClass.KNIGHT);
        sector.setClassBonus(-5);
        assertEquals(0, sector.classBonus(charClass));
    }

    @Test
    public void testGetAreas() {
        Set<Area> areas = new HashSet<Area>();
        assertSame(areas, sector.getAreas());

        Area area = new TestData().getTestArea();
        sector._addArea(area);
        areas = sector.getAreas();
        assertEquals(1, areas.size());
        assertTrue(areas.contains(area));
    }

    @Test
    public void testAddArea() {
        Set<Area> areas = new HashSet<Area>();
        sector.setAreas(areas);

        Area area = new TestData().getTestArea();
        sector._addArea(area);
        areas = sector.getAreas();
        assertEquals(1, areas.size());
        assertTrue(areas.contains(area));
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
        SimpleSector sector5 = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);

        Set<Area> areas1 = new HashSet<Area>();
        Set<Area> areas2 = new HashSet<Area>();

        sector1.setAreas(areas1);
        sector2.setAreas(areas2);

        Set<SectorObserver> observers1 = new HashSet<SectorObserver>();
        Set<SectorObserver> observers2 = new HashSet<SectorObserver>();

        sector1.setObservers(observers1);
        sector2.setObservers(observers2);

        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertEquals(sector1.hashCode(), sector3.hashCode());
        assertEquals(sector1.hashCode(), sector4.hashCode());
        assertEquals(sector1.hashCode(), sector5.hashCode());

        areas1.add(new TestData().getTestArea());
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertEquals(sector1.hashCode(), sector3.hashCode());
        assertEquals(sector1.hashCode(), sector4.hashCode());
        assertEquals(sector1.hashCode(), sector5.hashCode());

        areas1.clear();
        areas2.add(new TestData().getTestArea());
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertEquals(sector1.hashCode(), sector3.hashCode());
        assertEquals(sector1.hashCode(), sector4.hashCode());
        assertNotEquals(sector1.hashCode(), sector5.hashCode());

        observers1.add(new TestData().getTestObserver());
        observers2.add(new TestData().getTestObserver());
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
        assertNotEquals(sector1.hashCode(), sector4.hashCode());
        assertNotEquals(sector1.hashCode(), sector5.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
        SimpleSector sector5 = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);

        Set<Area> areas1 = new HashSet<Area>();
        Set<Area> areas2 = new HashSet<Area>();

        sector1.setAreas(areas1);
        sector2.setAreas(areas2);

        Set<SectorObserver> observers1 = new HashSet<SectorObserver>();
        Set<SectorObserver> observers2 = new HashSet<SectorObserver>();

        sector1.setObservers(observers1);
        sector2.setObservers(observers2);

        assertTrue(sector1.equals(sector2));
        assertTrue(sector1.equals(sector3));
        assertTrue(sector1.equals(sector4));
        assertTrue(sector1.equals(sector5));

        areas1.add(new TestData().getTestArea());
        assertFalse(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
        assertFalse(sector1.equals(sector4));
        assertFalse(sector1.equals(sector5));

        observers1.add(new TestData().getTestObserver());
        assertFalse(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
        assertFalse(sector1.equals(sector4));
        assertFalse(sector1.equals(sector5));

        sector1.setDefenderSpawn(false);
        assertFalse(sector1.equals(sector2));
    }

    @Test
    public void testAddObserver() {
        Set<SectorObserver> observers = new HashSet<SectorObserver>();
        sector.addObserver(new TestData().getTestObserver());
        assertEquals(1, sector.getObservers().size());
        assertTrue(sector.getObservers().contains(new TestData().getTestObserver()));

        sector.removeObserver(new TestData().getTestObserver());
        assertEquals(0, sector.getObservers().size());
        assertFalse(sector.getObservers().contains(new TestData().getTestObserver()));

        sector.addObserver(new TestData().getTestObserver());
        assertEquals(1, sector.getObservers().size());
        assertTrue(sector.getObservers().contains(new TestData().getTestObserver()));

        sector.addObserver(new TestData().getTestObserver());
        assertEquals(1, sector.getObservers().size());
        assertTrue(sector.getObservers().contains(new TestData().getTestObserver()));

        sector.addObserver(new TestData().getTestObserver());
        assertEquals(1, sector.getObservers().size());
        assertTrue(sector.getObservers().containsAll(Arrays.asList(new TestData().getTestObserver(), new TestData().getTestObserver(), new TestData().getTestObserver())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullObserver() {
        sector.addObserver(null);
    }
}