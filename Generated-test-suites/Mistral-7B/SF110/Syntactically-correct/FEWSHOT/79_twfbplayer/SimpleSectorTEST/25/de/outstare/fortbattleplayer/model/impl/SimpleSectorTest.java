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
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.Weapon;
import de.outstare.fortbattleplayer.model.observer.SectorObserver;

public class SimpleSectorTest {

    @Test
    public void testSimpleSectorInitialization() {
        SimpleSector sector = new SimpleSector(5, true, false, 3, 4, false, 5, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertFalse(sector.isAttackerSpawn());
        assertEquals(3, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(4, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertFalse(sector.isFlag());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
        assertNotNull(sector.getAreas());
        assertEquals(0, sector.observers.size());
    }

    @Test
    public void testSectorFree() {
        SimpleSector sector = new SimpleSector(5, true, false, 3, 4, false, 5, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }

    @Test
    public void testSectorGainControl() {
        SimpleSector sector = new SimpleSector(5, true, false, 3, 4, false, 5, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertEquals(1, sector.getOccupierCount());
    }

    @Test
    public void testSectorIsOccupied() {
        SimpleSector sector = new SimpleSector(5, true, false, 3, 4, false, 5, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testSectorGetBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 3, 4, false, 5, CharacterClass.ARCHER);
        Area area = new Area(new Combatant(CharacterClass.ARCHER, null), null);
        sector._addArea(area);
        Weapon weapon = new Weapon("TestWep", 5, SectorBonus.BonusType.ATTACK);
        area.setOccupier(new Combatant(CharacterClass.ARCHER, weapon));
        sector.gainControl(CombatantSide.DEFENDER);
        int bonus = sector.getAttackBonus(CharacterClass.ARCHER);
        assertEquals(8, bonus);
    }

    @Test
    public void testSectorGetBonusWhenNoWeapon() {
        SimpleSector sector = new SimpleSector(5, true, false, 3, 4, false, 5, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        int bonus = sector.getAttackBonus(CharacterClass.ARCHER);
        assertEquals(3, bonus);
    }

    @Test
    public void testSectorHashCodeAndEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 3, 4, false, 5, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, false, 3, 4, false, 5, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(6, true, false, 3, 4, false, 5, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, true, false, 3, 4, false, 5, CharacterClass.ARCHER);
        Set<SimpleSector> sectors = new HashSet<>();
        sectors.add(sector1);
        sectors.add(sector2);
        sectors.add(sector3);
        sectors.add(sector4);
        assertTrue(sectors.contains(sector1));
        assertTrue(sectors.contains(sector2));
        assertFalse(sectors.contains(sector3));
    }

    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(5, true, false, 3, 4, false, 5, CharacterClass.ARCHER);
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do nothing
            }
        };
        sector.addObserver(observer);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.observers.size());
    }

    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(5, true, false, 3, 4, false, 5, CharacterClass.ARCHER);
        SectorObserver observer = new SectorObserver() {
            @Override
            public void occupierChanged() {
                // do nothing
            }
        };
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertEquals(0, sector.observers.size());
    }

    @Test
    public void testClassBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 3, 4, false, 5, CharacterClass.ARCHER);
        assertEquals(5, sector.classBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testClassBonusForOtherClass() {
        SimpleSector sector = new SimpleSector(5, true, false, 3, 4, false, 5, CharacterClass.ARCHER);
        assertEquals(0, sector.classBonus(CharacterClass.KNIGHT));
    }
}