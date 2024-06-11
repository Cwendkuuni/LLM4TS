package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
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
    private Area area1;
    private Area area2;
    private CharacterClass charClass;
    private Combatant combatant;
    private Weapon weapon;

    @Before
    public void setUp() {
        this.sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        this.area1 = new Area();
        this.area2 = new Area();
        this.charClass = CharacterClass.ARCHER;
        this.combatant = new Combatant(CharacterClass.ARCHER, null);
        this.weapon = new Weapon("TestWeapon", 0, 0, 1);
    }

    @Test
    public void testConstructor() {
        assertNotNull(this.sector);
        assertEquals(5, this.sector.getHeight());
        assertFalse(this.sector.isDefenderSpawn());
        assertTrue(this.sector.isAttackerSpawn());
        assertEquals(2, this.sector.getAttackBonus(this.charClass));
        assertEquals(3, this.sector.getDefenseBonus(this.charClass));
        assertFalse(this.sector.isFlag());
        assertEquals(4, this.sector.getClassBonus());
        assertNull(this.sector.getOccupier());
        assertFalse(this.sector.isOccupied());
        assertEmpty(this.sector.getAreas());
        assertEmpty(this.sector.getObservers());
    }

    @Test
    public void testFree() {
        this.sector.gainControl(CombatantSide.DEFENDER);
        this.sector.gainControl(CombatantSide.DEFENDER);
        this.sector.free();
        assertEquals(0, this.sector.getOccupierCount());
        assertNull(this.sector.getOccupier());
    }

    @Test
    public void testGainControl() {
        this.sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, this.sector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, this.sector.getOccupier());
        this.sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(2, this.sector.getOccupierCount());
        assertEquals(CombatantSide.DEFENDER, this.sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        this.sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, this.sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        this.sector.gainControl(CombatantSide.ATTACKER);
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
        assertEquals(new SectorBonus(5, 5, 0), this.sector.getBonus(this.charClass));

        this.sector._addArea(this.area1);
        this.area1.setOccupier(this.combatant);
        this.combatant.setWeapon(this.weapon);
        assertEquals(new SectorBonus(6, 5, 1), this.sector.getBonus(this.charClass));

        this.sector._addArea(this.area2);
        this.area2.setOccupier(this.combatant);
        this.combatant.setWeapon(this.weapon);
        assertEquals(new SectorBonus(6, 6, 2), this.sector.getBonus(this.charClass));
    }

    @Test
    public void testGetAttackBonus() {
        assertEquals(2, this.sector.getAttackBonus(this.charClass));

        this.sector._addArea(this.area1);
        this.area1.setOccupier(this.combatant);
        this.combatant.setWeapon(this.weapon);
        assertEquals(4, this.sector.getAttackBonus(this.charClass));
    }

    @Test
    public void testGetDefenseBonus() {
        assertEquals(3, this.sector.getDefenseBonus(this.charClass));

        this.sector._addArea(this.area1);
        this.area1.setOccupier(this.combatant);
        this.combatant.setWeapon(this.weapon);
        assertEquals(5, this.sector.getDefenseBonus(this.charClass));
    }

    @Test
    public void testClassBonus() {
        assertEquals(4, this.sector.classBonus(this.charClass));

        this.sector._addArea(this.area1);
        this.area1.setOccupier(new Combatant(CharacterClass.KNIGHT, null));
        assertEquals(5, this.sector.classBonus(this.charClass));
    }

    @Test
    public void testGetAreas() {
        assertEquals(new HashSet<Area>(), this.sector.getAreas());

        this.sector._addArea(this.area1);
        assertEquals(Arrays.asList(this.area1), new ArrayList<Area>(this.sector.getAreas()));
    }

    @Test
    public void testAddArea() {
        this.sector.addObserver(new SectorObserver() {
            @Override
            public void occupierChanged() {
                fail("occupierChanged should not be called");
            }
        });

        this.sector._addArea(this.area1);
        assertEquals(Arrays.asList(this.area1), new ArrayList<Area>(this.sector.getAreas()));
        assertFalse(this.sector.getObservers().isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveArea() {
        this.sector._addArea(this.area1);
        this.sector.removeArea(this.area1);
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, true, true, 3, 3, false, 4, CharacterClass.ARCHER);

        Set<Sector> set = new HashSet<>();
        set.add(sector1);
        set.add(sector2);
        set.add(sector3);
        set.add(sector4);

        assertEquals(1, set.size());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
        SimpleSector sector4 = new SimpleSector(5, true, true, 3, 3, false, 4, CharacterClass.ARCHER);
        SimpleSector sector5 = new SimpleSector(5, true, true, 2, 3, false, 5, CharacterClass.ARCHER);

        Set<Sector> set = new HashSet<>();
        set.add(sector1);
        set.add(sector2);
        set.add(sector3);

        assertTrue(sector1.equals(sector2));
        assertTrue(sector1.equals(sector3));
        assertFalse(sector1.equals(sector4));
        assertFalse(sector1.equals(sector5));
        assertFalse(sector1.equals(null));
        assertFalse(sector1.equals(""));
    }

    @Test
    public void testAddObserver() {
        this.sector.addObserver(new SectorObserver() {
            @Override
            public void occupierChanged() {
                fail("occupierChanged should not be called");
            }
        });

        this.sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(this.sector.getObservers().size() > 0);
    }

    @Test
    public void testRemoveObserver() {
        this.sector.addObserver(new SectorObserver() {
            @Override
            public void occupierChanged() {
                fail("occupierChanged should not be called");
            }
        });

        this.sector.removeObserver(new SectorObserver() {
            @Override
            public void occupierChanged() {
                fail("occupierChanged should not be called");
            }
        });

        assertFalse(this.sector.getObservers().isEmpty());
    }

    @After
    public void tearDown() {
        this.sector = null;
        this.area1 = null;
        this.area2 = null;
        this.charClass = null;
        this.combatant = null;
        this.weapon = null;
    }
}