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
        SimpleSector sector = new SimpleSector(3, true, true, 5, 7, true, 1, CharacterClass.ARCHER);
        assertEquals(3, sector.getHeight());
        assertEquals(true, sector.isDefenderSpawn());
        assertEquals(true, sector.isAttackerSpawn());
        assertEquals(5, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(7, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertEquals(true, sector.isFlag());
        assertEquals(CharacterClass.ARCHER, sector.getClassType());
        assertNull(sector.getOccupier());
        assertEquals(false, sector.isOccupied());
        assertTrue(sector.getAreas().isEmpty());
        assertTrue(sector.getObservers().isEmpty());
    }

    @Test
    public void testFreeMethod() {
        SimpleSector sector = new SimpleSector(3, true, true, 5, 7, true, 1, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDERS);
        sector.gainControl(CombatantSide.DEFENDERS);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.DEFENDERS);
        sector.free();
        assertNull(sector.getOccupier());
        assertEquals(false, sector.isOccupied());
    }

    @Test
    public void testGainControlMethod() {
        SimpleSector sector = new SimpleSector(3, true, true, 5, 7, true, 1, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDERS);
        assertEquals(CombatantSide.DEFENDERS, sector.getOccupier());
        assertEquals(true, sector.isOccupied());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertEquals(false, sector.isOccupied()); // freeing defenders
        sector.gainControl(CombatantSide.DEFENDERS);
        assertEquals(CombatantSide.DEFENDERS, sector.getOccupier());
        assertEquals(true, sector.isOccupied());
    }

    @Test
    public void testGetBonusMethod() {
        SimpleSector sector = new SimpleSector(3, true, true, 5, 7, true, 1, CharacterClass.ARCHER);
        Area area1 = new Area(2, 2, new Combatant(CharacterClass.ARCHER, new Weapon(CharacterClass.ARCHER, "bow", 3, 1, 2)));
        Area area2 = new Area(2, 2, new Combatant(CharacterClass.ARCHER, new Weapon(CharacterClass.ARCHER, "sword", 3, 1, 2)));
        sector._addArea(area1);
        sector._addArea(area2);
        sector.gainControl(CombatantSide.DEFENDERS);
        assertEquals(new SectorBonus(12, 10, 4), sector.getBonus(CharacterClass.ARCHER));
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(new SectorBonus(12, 10, 1), sector.getBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetAttackBonusMethod() {
        SimpleSector sector = new SimpleSector(3, true, true, 5, 7, true, 1, CharacterClass.ARCHER);
        Area area1 = new Area(2, 2, new Combatant(CharacterClass.ARCHER, new Weapon(CharacterClass.ARCHER, "bow", 3, 1, 2)));
        Area area2 = new Area(2, 2, new Combatant(CharacterClass.ARCHER, new Weapon(CharacterClass.ARCHER, "sword", 3, 1, 2)));
        sector._addArea(area1);
        sector._addArea(area2);
        sector.gainControl(CombatantSide.DEFENDERS);
        assertEquals(11, sector.getAttackBonus(CharacterClass.ARCHER));
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(6, sector.getAttackBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetDefenseBonusMethod() {
        SimpleSector sector = new SimpleSector(3, true, true, 5, 7, true, 1, CharacterClass.ARCHER);
        Area area1 = new Area(2, 2, new Combatant(CharacterClass.ARCHER, new Weapon(CharacterClass.ARCHER, "bow", 3, 1, 2)));
        Area area2 = new Area(2, 2, new Combatant(CharacterClass.ARCHER, new Weapon(CharacterClass.ARCHER, "sword", 3, 1, 2)));
        sector._addArea(area1);
        sector._addArea(area2);
        sector.gainControl(CombatantSide.DEFENDERS);
        assertEquals(13, sector.getDefenseBonus(CharacterClass.ARCHER));
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(8, sector.getDefenseBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testClassBonusMethod() {
        SimpleSector sector = new SimpleSector(3, true, true, 5, 7, true, 1, CharacterClass.ARCHER);
        assertEquals(1, sector.classBonus(CharacterClass.ARCHER));
        SimpleSector sector2 = new SimpleSector(3, true, true, 5, 7, true, 2, CharacterClass.ARCHER);
        assertEquals(2, sector2.classBonus(CharacterClass.ARCHER));
        SimpleSector sector3 = new SimpleSector(3, true, true, 5, 7, false, 1, CharacterClass.ARCHER);
        assertEquals(0, sector3.classBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testHashCodeAndEqualsMethod() {
        SimpleSector sector1 = new SimpleSector(3, true, true, 5, 7, true, 1, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(3, true, true, 5, 7, true, 1, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(3, true, true, 5, 6, true, 1, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(3, true, true, 5, 7, true, 1, CharacterClass.ARCHER);
        SimpleSector sector5 = new SimpleSector(2, true, true, 5, 7, true, 1, CharacterClass.ARCHER);
        Set<SimpleSector> sectors = new HashSet<SimpleSector>();
        sectors.add(sector1);
        sectors.add(sector2);
        sectors.add(sector3);
        sectors.add(sector4);
        sectors.add(sector5);
        assertTrue(sectors.contains(sector1));
        assertTrue(sectors.contains(sector2));
        assertFalse(sectors.contains(sector3));
        assertEquals(sector1, sector1);
        assertNotEquals(sector1, sector2);
        assertNotEquals(sector1, sector3);
        assertNotEquals(sector1, sector4);
        assertNotEquals(sector1, sector5);
    }

    @Test
    public void testAddObserverMethod() {
        SimpleSector sector = new SimpleSector(3, true, true, 5, 7, true, 1, CharacterClass.ARCHER);
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }
}