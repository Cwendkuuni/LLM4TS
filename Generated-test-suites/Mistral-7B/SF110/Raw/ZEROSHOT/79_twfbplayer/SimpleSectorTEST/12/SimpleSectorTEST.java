package de.outstare.fortbattleplayer.model.impl;

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
import de.outstare.fortbattleplayer.model.impl.Weapon;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
    }

    @Test
    public void testSimpleSectorConstruction() {
        assertNotNull(sector);
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
    public void testGetOccupier() {
        assertNull(sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testFree() {
        sector.free();
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertTrue(sector.isOccupied());

        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetBonus() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass knight = CharacterClass.KNIGHT;

        SectorBonus bonusArcher = new SectorBonus(5, 3, 2);
        SectorBonus bonusKnight = new SectorBonus(2, 7, 1);

        sector.addObserver(new TestSectorObserver());

        sector._addArea(new Area() {
            @Override
            public Combatant getOccupier() {
                return new Combatant(CombatantSide.DEFENDER, new Weapon(archer, 1));
            }

            @Override
            public boolean isOccupied() {
                return true;
            }
        });

        assertEquals(bonusArcher, sector.getBonus(archer));
        assertEquals(bonusKnight, sector.getBonus(knight));

        sector.free();
        assertEquals(null, sector.getBonus(archer));
        assertEquals(null, sector.getBonus(knight));
    }

    @Test
    public void testGetAttackBonus() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass knight = CharacterClass.KNIGHT;

        int attackBonusArcher = 3;
        int attackBonusKnight = 1;

        sector.addObserver(new TestSectorObserver());

        sector._addArea(new Area() {
            @Override
            public Combatant getOccupier() {
                return new Combatant(CombatantSide.DEFENDER, new Weapon(archer, 1));
            }

            @Override
            public boolean isOccupied() {
                return true;
            }
        });

        assertEquals(attackBonusArcher + 2, sector.getAttackBonus(archer)); // bonus from weapon and sector
        assertEquals(attackBonusKnight, sector.getAttackBonus(knight));

        sector.free();
        assertEquals(0, sector.getAttackBonus(archer));
        assertEquals(attackBonusKnight, sector.getAttackBonus(knight));
    }

    @Test
    public void testGetDefenseBonus() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass knight = CharacterClass.KNIGHT;

        int defenseBonusArcher = 2;
        int defenseBonusKnight = 4;

        sector.addObserver(new TestSectorObserver());

        sector._addArea(new Area() {
            @Override
            public Combatant getOccupier() {
                return new Combatant(CombatantSide.DEFENDER, new Weapon(archer, 1));
            }

            @Override
            public boolean isOccupied() {
                return true;
            }
        });

        assertEquals(defenseBonusArcher + 1, sector.getDefenseBonus(archer)); // bonus from weapon
        assertEquals(defenseBonusKnight + 1, sector.getDefenseBonus(knight)); // bonus from weapon and sector

        sector.free();
        assertEquals(0, sector.getDefenseBonus(archer));
        assertEquals(defenseBonusKnight, sector.getDefenseBonus(knight));
    }

    @Test
    public void testGetDamageBonus() {
        assertEquals(2, sector.getDamageBonus());
    }

    @Test
    public void testClassBonus() {
        CharacterClass archer = CharacterClass.ARCHER;
        CharacterClass knight = CharacterClass.KNIGHT;

        int classBonusArcher = 3;
        int classBonusKnight = 1;

        assertEquals(classBonusArcher, sector.classBonus(archer));
        assertEquals(classBonusKnight, sector.classBonus(knight));

        sector.setClassBonus(knight, 5);
        assertEquals(classBonusKnight + 5, sector.classBonus(knight));
    }

    @Test
    public void testGetAreas() {
        Set<Area> areas = new HashSet<Area>(Arrays.asList(new Area(), new Area()));
        sector.setAreas(areas);

        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void testAddArea() {
        Area area = new Area();
        sector.addArea(area);

        Set<Area> areas = new HashSet<Area>(Arrays.asList(area));
        assertEquals(areas, sector.getAreas());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveArea() {
        sector.removeArea(null);
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(2, true, true, 1, 1, false, 1, CharacterClass.ARCHER);

        assertEquals(sector1.hashCode(), sector1.hashCode());
        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(2, true, true, 1, 1, false, 1, CharacterClass.ARCHER);

        assertTrue(sector1.equals(sector1));
        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
    }

    @Test
    public void testAddObserver() {
        TestSectorObserver observer = new TestSectorObserver();
        sector.addObserver(observer);
        sector.occupierChanged();
        assertTrue(observer.occupierChangedCalled);
    }

    @Test
    public void testRemoveObserver() {
        TestSectorObserver observer = new TestSectorObserver();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        sector.occupierChanged();
        assertFalse(observer.occupierChangedCalled);
    }
}

class TestSectorObserver implements SectorObserver {
    boolean occupierChangedCalled = false;

    @Override
    public void occupierChanged() {
        occupierChangedCalled = true;
    }
}