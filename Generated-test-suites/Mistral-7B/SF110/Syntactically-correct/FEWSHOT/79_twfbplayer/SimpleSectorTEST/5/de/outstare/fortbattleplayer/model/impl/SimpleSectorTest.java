package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.BonusType;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SimpleSectorObserver;

public class SimpleSectorTest {

    private SimpleSector sector;
    private Area area1, area2;
    private CharacterClass charClass;
    private Combatant combatant;
    private Weapon weapon;
    private SectorObserver observer;

    @Before
    public void setUp() {
        this.sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
        this.area1 = new SimpleArea(1, 1, 1, 1);
        this.area2 = new SimpleArea(1, 2, 1, 1);
        this.charClass = CharacterClass.ARCHER;
        this.combatant = new SimpleCombatant("testName", "testHealth", CharacterClass.ARCHER, 1, new SimpleWeapon("testWeaponName", 10));
        this.weapon = this.combatant.getWeapon();
        this.observer = new SimpleSectorObserver() {

            @Override
            public void occupierChanged() {
            }
        };
    }

    @Test
    public void testSimpleSectorConstructor() {
        assertEquals(5, this.sector.getHeight());
        assertEquals(true, this.sector.isDefenderSpawn());
        assertEquals(true, this.sector.isAttackerSpawn());
        assertEquals(0, this.sector.getAttackerBonus(CharacterClass.ARCHER));
        assertEquals(0, this.sector.getDefenderBonus(CharacterClass.ARCHER));
        assertEquals(0, this.sector.getDamageBonus());
        assertNull(this.sector.getOccupier());
        assertEquals(0, this.sector.getOccupierCount());
        assertTrue(this.sector.isOccupied());
        assertEquals(0, this.sector.hashCode());
        assertFalse(this.sector.equals(null));
        assertFalse(this.sector.equals(new Object()));
    }

    @Test
    public void testFree() {
        this.sector.gainControl(CombatantSide.DEFENDER);
        this.sector.gainControl(CombatantSide.ATTACKER);
        this.sector.gainControl(CombatantSide.DEFENDER);
        this.sector.gainControl(CombatantSide.ATTACKER);
        this.sector.gainControl(CombatantSide.DEFENDER);
        this.sector.gainControl(CombatantSide.ATTACKER);
        this.sector.gainControl(CombatantSide.DEFENDER);
        this.sector.gainControl(CombatantSide.ATTACKER);
        this.sector.free();
        assertNull(this.sector.getOccupier());
        assertEquals(0, this.sector.getOccupierCount());
    }

    @Test
    public void testGainControl() {
        this.sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, this.sector.getOccupier());
        assertEquals(1, this.sector.getOccupierCount());
        this.sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, this.sector.getOccupier());
        assertEquals(2, this.sector.getOccupierCount());
        assertFalse(this.sector.isOccupied());
        this.sector.free();
        assertEquals(null, this.sector.getOccupier());
        assertEquals(0, this.sector.getOccupierCount());
    }

    @Test
    public void testGetOccupier() {
        this.sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, this.sector.getOccupier());
        this.sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, this.sector.getOccupier());
        this.sector.free();
        assertNull(this.sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        this.sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(this.sector.isOccupied());
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
        SectorBonus bonus = this.sector.getBonus(this.charClass);
        assertEquals(10, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAttackBonus() {
        assertEquals(15, this.sector.getAttackBonus(this.charClass));
    }

    @Test
    public void testGetDefenseBonus() {
        assertEquals(10, this.sector.getDefenseBonus(this.charClass));
    }

    @Test
    public void testGetDamageBonusFromWeapons() {
        this.sector._addArea(this.area1);
        this.area1._addCombatant(this.combatant);
        assertEquals(10, this.sector.getDamageBonus());

        this.sector._addArea(this.area2);
        this.area2._addCombatant(this.combatant);
        weapon.setSectorBonus(BonusType.DAMAGE, 5);
        assertEquals(15, this.sector.getDamageBonus());
    }

    @Test
    public void testClassBonus() {
        assertEquals(5, this.sector.classBonus(this.charClass));
        assertFalse(this.sector.classBonus(CharacterClass.ARCHER_KNIGHT));
    }

    @Test
    public void testGetAreas() {
        assertEquals(new HashSet<Area>(Arrays.asList(this.area1, this.area2)), this.sector.getAreas());
    }

    @Test
    public void testHashCode() {
        SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
        assertEquals(this.sector.hashCode(), sector2.hashCode());

        this.sector.areas.add(new SimpleArea(1, 1, 1, 1));
        assertNotEquals(this.sector.hashCode(), sector2.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
        assertTrue(this.sector.equals(sector2));

        this.sector.areas.add(new SimpleArea(1, 1, 1, 1));
        assertFalse(this.sector.equals(sector2));
    }

    @Test
    public void testAddObserver() {
        this.sector.addObserver(this.observer);
        this.sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(this.observer.isCalled());
    }

    @Test
    public void testRemoveObserver() {
        this.sector.addObserver(this.observer);
        this.sector.gainControl(CombatantSide.ATTACKER);
        assertFalse(this.observer.isCalled());

        this.sector.removeObserver(this.observer);
        this.sector.gainControl(CombatantSide.DEFENDER);
        assertFalse(this.observer.isCalled());
    }

    class SimpleArea implements Area {
        private final int x, y, width, height;

        public SimpleArea(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
        public int getX() {
            return this.x;
        }

        @Override
        public int getY() {
            return this.y;
        }

        @Override
        public int getWidth() {
            return this.width;
        }

        @Override
        public int getHeight() {
            return this.height;
        }

        @Override
        public boolean isOccupied() {
            return this.occupied;
        }

        @Override
        public Combatant getOccupier() {
            return this.occupiedCombatant;
        }

        @Override
        public void _addCombatant(Combatant combatant) {
            this.occupied = true;
            this.occupiedCombatant = combatant;
        }

        private boolean occupied = false;
        private Combatant occupiedCombatant;
    }

    class SimpleCombatant implements Combatant {
        private String name;
        private int health;
        private CharacterClass characterClass;
        private int id;
        private Weapon weapon;

        public SimpleCombatant(String name, int health, CharacterClass characterClass, int id, Weapon weapon) {
            this.name = name;
            this.health = health;
            this.characterClass = characterClass;
            this.id = id;
            this.weapon = weapon;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getHealth() {
            return this.health;
        }

        @Override
        public CharacterClass getCharacterClass() {
            return this.characterClass;
        }

        @Override
        public int getId() {
            return this.id;
        }

        @Override
        public Weapon getWeapon() {
            return this.weapon;
        }
    }

    class SimpleWeapon implements Weapon {
        private String name;
        private int damage;

        public SimpleWeapon(String name, int damage) {
            this.name = name;
            this.damage = damage;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getDamage() {
            return this.damage;
        }

        @Override
        public int getSectorBonus(BonusType type) {
            return 0;
        }
    }

    static class SimpleSectorObserver implements SectorObserver {
        private boolean isCalled = false;

        @Override
        public void occupierChanged() {
            this.isCalled = true;
        }
    }
}