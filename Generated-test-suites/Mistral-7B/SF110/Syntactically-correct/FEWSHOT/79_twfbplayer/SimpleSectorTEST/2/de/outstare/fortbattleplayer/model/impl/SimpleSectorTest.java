package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;
import de.outstare.fortbattleplayer.model.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {
    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(5, true, false, 1, 1, false, 1, CharacterClass.ARCHER);
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.DEFFENDER);
        sector.free();
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.DEFFENDER);
        assertEquals(CombatantSide.DEFFENDER, sector.getOccupier());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        sector.gainControl(CombatantSide.DEFFENDER);
        assertEquals(CombatantSide.DEFFENDER, sector.getOccupier());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        sector.gainControl(CombatantSide.DEFFENDER);
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
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass mage = CharacterClass.MAGE;

        assertEquals(new SectorBonus(4, 3, 5), sector.getBonus(archer));
        assertEquals(new SectorBonus(3, 4, 5), sector.getBonus(mage));
    }

    @Test
    public void testGetBonusFromWeapons_EmptySector() {
        assertEquals(0, sector.getBonusFromWeapons(SectorBonus.BonusType.ATTACK));
        assertEquals(0, sector.getBonusFromWeapons(SectorBonus.BonusType.DEFENSE));
    }

    @Test
    public void testGetBonusFromWeapons_OccupiedSector() {
        sector.gainControl(CombatantSide.DEFFENDER);
        Area area = new Area(1, 1, new Combatant(CharacterClass.ARCHER, new Weapon(new Damage(1, 1, DamageType.SLASH), 1, "Sword")));
        sector._addArea(area);

        assertEquals(1, sector.getBonusFromWeapons(SectorBonus.BonusType.ATTACK));
        assertEquals(1, sector.getBonusFromWeapons(SectorBonus.BonusType.DEFENSE));
    }

    @Test
    public void testGetAttackBonus() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass mage = CharacterClass.MAGE;

        assertEquals(6, sector.getAttackBonus(archer));
        assertEquals(4, sector.getAttackBonus(mage));
    }

    @Test
    public void testGetDefenseBonus() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass mage = CharacterClass.MAGE;

        assertEquals(3, sector.getDefenseBonus(archer));
        assertEquals(4, sector.getDefenseBonus(mage));
    }

    @Test
    public void testClassBonus() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass mage = CharacterClass.MAGE;

        assertEquals(1, sector.classBonus(archer));
        assertEquals(1, sector.classBonus(mage));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testClassBonus_InvalidClass() {
        sector.classBonus(null);
    }

    @Test
    public void testHashCode() {
        SimpleSector sector2 = new SimpleSector(5, true, false, 1, 1, false, 1, CharacterClass.ARCHER);
        assertEquals(sector.hashCode(), sector2.hashCode());

        Set<Area> areas = new HashSet<Area>();
        areas.add(new Area(1, 1, new Combatant(CharacterClass.ARCHER, new Weapon(new Damage(1, 1, DamageType.SLASH), 1, "Sword"))));
        sector2.areas = areas;
        assertNotEquals(sector.hashCode(), sector2.hashCode());

        sector2.attackBonus = 2;
        assertNotEquals(sector.hashCode(), sector2.hashCode());

        sector2.defenderSpawn = false;
        assertNotEquals(sector.hashCode(), sector2.hashCode());

        sector2.classBonus = 2;
        assertNotEquals(sector.hashCode(), sector2.hashCode());

        sector2.classType = CharacterClass.MAGE;
        assertNotEquals(sector.hashCode(), sector2.hashCode());

        sector2.defendBonus = 2;
        assertNotEquals(sector.hashCode(), sector2.hashCode());

        sector2.flag = true;
        assertNotEquals(sector.hashCode(), sector2.hashCode());

        sector2.height = 6;
        assertNotEquals(sector.hashCode(), sector2.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector2 = new SimpleSector(5, true, false, 1, 1, false, 1, CharacterClass.ARCHER);

        assertTrue(sector.equals(sector2));

        Set<Area> areas = new HashSet<Area>();
        areas.add(new Area(1, 1, new Combatant(CharacterClass.ARCHER, new Weapon(new Damage(1, 1, DamageType.SLASH), 1, "Sword"))));
        sector2.areas = areas;
        assertFalse(sector.equals(sector2));

        sector2.attackBonus = 2;
        assertFalse(sector.equals(sector2));

        sector2.defenderSpawn = false;
        assertFalse(sector.equals(sector2));

        sector2.classBonus = 2;
        assertFalse(sector.equals(sector2));

        sector2.classType = CharacterClass.MAGE;
        assertFalse(sector.equals(sector2));

        sector2.defendBonus = 2;
        assertFalse(sector.equals(sector2));

        sector2.flag = true;
        assertFalse(sector.equals(sector2));

        sector2.height = 6;
        assertFalse(sector.equals(sector2));

        SimpleSector sector3 = new SimpleSector(5, true, false, 1, 1, false, 1, CharacterClass.ARCHER);
        assertTrue(sector.equals(sector3));
    }

    @Test
    public void testAddObserver() {
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        sector.occupierChanged();
        verify(observer).occupierChanged();
    }

    @Test
    public void testRemoveObserver() {
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        sector.removeObserver(observer);
        sector.occupierChanged();
        verifyNoMoreInteractions(observer);
    }
}