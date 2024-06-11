package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;
import de.outstare.fortbattleplayer.model.*;
import org.junit.Test;

public class SimpleSectorTest {

    @Test
    public void testSimpleSectorCreation() {
        SimpleSector sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
        assertNotNull(sector);
        assertEquals(5, sector.getHeight());
        assertEquals(true, sector.isDefenderSpawn());
        assertEquals(false, sector.isAttackerSpawn());
        assertEquals(false, sector.isFlag());
        assertEquals(4, sector.getClassBonus());
        assertEquals(CharacterClass.ARCHER, sector.getClassType());
        assertTrue(sector.areas.isEmpty());
        assertTrue(sector.observers.isEmpty());
    }

    @Test
    public void testFreeMethod() {
        SimpleSector sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDERS);
        sector.gainControl(CombatantSide.DEFENDERS);
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }

    @Test
    public void testGainControlMethod() {
        SimpleSector sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.ATTACKERS);
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKERS, sector.getOccupier());
    }

    @Test
    public void testIsOccupiedMethod() {
        SimpleSector sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDERS);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetOccupierMethod() {
        SimpleSector sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDERS);
        assertEquals(CombatantSide.DEFENDERS, sector.getOccupier());
    }

    @Test
    public void testGetBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
        Area area = new Area();
        Combatant combatant = new Combatant(CharacterClass.ARCHER);
        Weapon weapon = new Weapon();
        weapon.setSectorBonus(SectorBonus.BonusType.ATTACK, 5);
        combatant.setWeapon(weapon);
        sector._addArea(area);
        area.setOccupier(combatant);

        SectorBonus bonus = sector.getBonus(CharacterClass.ARCHER);
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(3, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testEquality() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);

        assertEquals(sector1, sector2);
        sector2.addObserver(mock(SectorObserver.class));
        assertNotEquals(sector1, sector2);
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);

        assertEquals(sector1.hashCode(), sector2.hashCode());

        Set<Area> areas = new HashSet<>();
        areas.add(new Area());
        SimpleSector sector3 = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
        sector3.setAreas(areas);

        SimpleSector sector4 = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
        sector4.setAreas(areas);

        assertNotEquals(sector3.hashCode(), sector4.hashCode());
    }
}