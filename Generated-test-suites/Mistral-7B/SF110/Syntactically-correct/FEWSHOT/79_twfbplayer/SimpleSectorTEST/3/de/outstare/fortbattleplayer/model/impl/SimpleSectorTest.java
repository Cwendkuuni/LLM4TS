package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

    @Test
    public void testSimpleSectorCreation() {
        SimpleSector sector = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        assertEquals(3, sector.getHeight());
        assertEquals(true, sector.isDefenderSpawn());
        assertEquals(true, sector.isAttackerSpawn());
        assertEquals(2, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(1, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertEquals(true, sector.isFlag());
        assertEquals(5, sector.getClassBonus());
        assertEquals(CharacterClass.ARCHER, sector.getClassType());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
        assertEquals(new HashSet<Area>(), sector.getAreas());
        assertEquals(new HashSet<SectorObserver>(), sector.getObservers());
    }

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }

    @Test
    public void testOccupierChanged() {
        SimpleSector sector = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        sector.gainControl(CombatantSide.DEFENDER);
        verify(observer).occupierChanged();
        sector.gainControl(CombatantSide.ATTACKER);
        verify(observer, times(2)).occupierChanged();
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        Combatant defender = mock(Combatant.class);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertEquals(1, sector.getOccupierCount());
        Area area = mock(Area.class);
        sector._addArea(area);
        Combatant attacker = mock(Combatant.class);
        sector.gainControl(CombatantSide.ATTACKER, attacker);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertEquals(2, sector.getOccupierCount());
        assertEquals(attacker, sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        SimpleSector sector = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        Combatant defender = mock(Combatant.class);
        sector.gainControl(CombatantSide.DEFENDER, defender);
        assertEquals(defender, sector.getOccupier());
        Combatant attacker = mock(Combatant.class);
        sector.gainControl(CombatantSide.ATTACKER, attacker);
        assertEquals(attacker, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        assertEquals(3, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        assertTrue(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        SectorBonus bonus = sector.getBonus(CharacterClass.ARCHER);
        assertEquals(2, bonus.getAttackBonus());
        assertEquals(1, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());

        SimpleSector sector2 = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        sector2.addArea(new Area(1, 1, 1, null, null, null, null, null, null, null, null));
        Combatant combatant = mock(Combatant.class);
        Weapon weapon = mock(Weapon.class);
        when(weapon.getSectorBonus(SectorBonus.BonusType.ATTACK)).thenReturn(3);
        when(combatant.getWeapon()).thenReturn(weapon);
        sector2.addCombatant(combatant);
        SectorBonus bonus2 = sector2.getBonus(CharacterClass.ARCHER);
        assertEquals(5, bonus2.getAttackBonus());
    }

    @Test
    public void testGetAttackBonus() {
        SimpleSector sector = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        assertEquals(2, sector.getAttackBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetDefenseBonus() {
        SimpleSector sector = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        assertEquals(1, sector.getDefenseBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testClassBonus() {
        SimpleSector sector = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        assertEquals(5, sector.classBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        Set<Area> areas1 = new HashSet<Area>(Arrays.asList(new Area(1, 1, 1, null, null, null, null, null, null, null, null)));
        Set<Area> areas2 = new HashSet<Area>(Arrays.asList(new Area(1, 1, 1, null, null, null, null, null, null, null, null)));
        SimpleSector sector3 = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(3, true, true, 2, 1, false, 5, CharacterClass.ARCHER);
        SimpleSector sector5 = new SimpleSector(3, false, true, 2, 1, true, 5, CharacterClass.ARCHER);
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
        assertNotEquals(sector1.hashCode(), sector4.hashCode());
        assertNotEquals(sector1.hashCode(), sector5.hashCode());
        assertNotEquals(sector1.hashCode(), null);
        assertNotEquals(sector1.hashCode(), new Object());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        Set<Area> areas1 = new HashSet<Area>(Arrays.asList(new Area(1, 1, 1, null, null, null, null, null, null, null, null)));
        Set<Area> areas2 = new HashSet<Area>(Arrays.asList(new Area(1, 1, 1, null, null, null, null, null, null, null, null)));
        SimpleSector sector3 = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(3, true, true, 2, 1, false, 5, CharacterClass.ARCHER);
        SimpleSector sector5 = new SimpleSector(3, false, true, 2, 1, true, 5, CharacterClass.ARCHER);
        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
        assertFalse(sector1.equals(sector4));
        assertFalse(sector1.equals(sector5));
        assertFalse(sector1.equals(null));
        assertFalse(sector1.equals(new Object()));
        SimpleSector sector6 = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        sector6.setAreas(areas1);
        sector1.setAreas(areas1);
        assertTrue(sector1.equals(sector6));
    }

    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        SectorObserver observer1 = mock(SectorObserver.class);
        sector.addObserver(observer1);
        SectorObserver observer2 = mock(SectorObserver.class);
        sector.addObserver(observer2);
        assertEquals(2, sector.getObservers().size());
    }

    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(3, true, true, 2, 1, true, 5, CharacterClass.ARCHER);
        SectorObserver observer1 = mock(SectorObserver.class);
        sector.addObserver(observer1);
        SectorObserver observer2 = mock(SectorObserver.class);
        sector.addObserver(observer2);
        sector.removeObserver(observer1);
        assertEquals(1, sector.getObservers().size());
    }
}