package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setup() {
        sector = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
    }

    @Test
    public void testSimpleSectorConstructor() {
        assertNotNull(sector);
        assertEquals(5, sector.getHeight());
        assertEquals(true, sector.isDefenderSpawn());
        assertEquals(false, sector.isAttackerSpawn());
        assertEquals(1, sector.getAttackBonus(CharacterClass.ARCHER));
        assertEquals(1, sector.getDefenseBonus(CharacterClass.ARCHER));
        assertEquals(false, sector.isFlag());
        assertEquals(2, sector.getClassBonus());
        assertEquals(CharacterClass.ARCHER, sector.getClassType());
        assertEquals(0, sector.occupierCount);
        assertNull(sector.occupier);
        assertTrue(sector.areas.isEmpty());
        assertTrue(sector.observers.isEmpty());
    }

    @Test
    public void testFreeMethod() {
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertEquals(0, sector.occupierCount);
        assertNull(sector.occupier);
    }

    @Test
    public void testGainControlMethod() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(3, sector.occupierCount);
        assertEquals(CombatantSide.ATTACKER, sector.occupier);
    }

    @Test
    public void testGetOccupierMethod() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testIsOccupiedMethod() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetHeightMethod() {
        assertEquals(5, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawnMethod() {
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawnMethod() {
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlagMethod() {
        assertFalse(sector.isFlag());
    }

    @Test
    public void testGetBonusMethod() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass knight = CharacterClass.KNIGHT;
        sector.addObserver(new SectorObserver() {
            @Override
            public void occupierChanged() {
                if (sector.occupier == CombatantSide.DEFENDER) {
                    assertEquals(3, sector.getBonus(archer).getAttackBonus());
                    assertEquals(3, sector.getBonus(archer).getDefenseBonus());
                    assertEquals(1, sector.getBonus(knight).getAttackBonus());
                    assertEquals(1, sector.getBonus(knight).getDefenseBonus());
                }
            }
        });
        sector.gainControl(CombatantSide.DEFENDER);
    }

    @Test
    public void testGetDamageBonusMethod() {
        assertEquals(0, sector.getDamageBonus());
        Area area = new AreaImpl(5, 5, sector, null, null, null);
        sector._addArea(area);
        Combatant combatant = new CombatantImpl("Test", CharacterClass.ARCHER, null, null, null, null, null, null, null, null, null, null, null, null, null);
        area.setOccupier(combatant);
        Weapon weapon = new WeaponImpl("TestWeapon", 5, null, null, null, null, null, null, null, null, null, null, null, null);
        combatant.setWeapon(weapon);
        assertEquals(weapon.getSectorBonus(SectorBonus.BonusType.DAMAGE), sector.getDamageBonus());
    }

    @Test
    public void testGetAttackBonusMethod() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass knight = CharacterClass.KNIGHT;
        sector.addObserver(new SectorObserver() {
            @Override
            public void occupierChanged() {
                if (sector.occupier == CombatantSide.DEFENDER) {
                    assertEquals(4, sector.getAttackBonus(archer));
                    assertEquals(2, sector.getAttackBonus(knight));
                }
            }
        });
        sector.gainControl(CombatantSide.DEFENDER);
    }

    @Test
    public void testGetDefenseBonusMethod() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass knight = CharacterClass.KNIGHT;
        sector.addObserver(new SectorObserver() {
            @Override
            public void occupierChanged() {
                if (sector.occupier == CombatantSide.DEFENDER) {
                    assertEquals(4, sector.getDefenseBonus(archer));
                    assertEquals(2, sector.getDefenseBonus(knight));
                }
            }
        });
        sector.gainControl(CombatantSide.DEFENDER);
    }

    @Test
    public void testClassBonusMethod() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass knight = CharacterClass.KNIGHT;
        assertEquals(2, sector.classBonus(archer));
        assertEquals(2, sector.classBonus(knight));
    }

    @Test
    public void testGetAreasMethod() {
        Set<Area> areas = new HashSet<>();
        sector._addArea(new AreaImpl(5, 5, sector, null, null, null));
        sector._addArea(new AreaImpl(5, 5, sector, null, null, null));
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testAddAreaMethod() {
        Set<Area> areas = new HashSet<>();
        sector._addArea(new AreaImpl(5, 5, sector, null, null, null));
        assertEquals(1, sector.areas.size());
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testHashCodeMethod() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(6, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        SimpleSector sector5 = new SimpleSector(5, true, false, 1, 1, true, 2, CharacterClass.ARCHER);
        assertEquals(sector.hashCode(), sector1.hashCode());
        assertNotEquals(sector.hashCode(), sector2.hashCode());
        assertNotEquals(sector.hashCode(), sector3.hashCode());
        assertNotEquals(sector.hashCode(), sector4.hashCode());
        assertNotEquals(sector.hashCode(), sector5.hashCode());
    }

    @Test
    public void testEqualsMethod() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(6, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        SimpleSector sector5 = new SimpleSector(5, true, false, 1, 1, true, 2, CharacterClass.ARCHER);
        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
        assertTrue(sector1.equals(sector4));
        assertFalse(sector1.equals(sector5));
    }

    @Test
    public void testAddObserverMethod() {
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));
    }

    @Test
    public void testRemoveObserverMethod() {
        SectorObserver observer = mock(SectorObserver.class);
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.observers.contains(observer));
    }
}