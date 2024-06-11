package de.outstare.fortbattleplayer.model.impl.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;

public class SimpleSectorTest1 {
    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(5, true, false, 2, 3, false, 4, CharacterClass.ARCHER);
    }

    @Test
    public void testFree() {
        sector.free();
        Assert.assertNull(sector.getOccupier());
        Assert.assertEquals(0, sector.getOccupierCount());

        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        Assert.assertNull(sector.getOccupier());
        Assert.assertEquals(0, sector.getOccupierCount());

        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        Assert.assertNull(sector.getOccupier());
        Assert.assertEquals(0, sector.getOccupierCount());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.DEFENDER);
        Assert.assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        Assert.assertEquals(1, sector.getOccupierCount());

        sector.gainControl(CombatantSide.ATTACKER);
        Assert.assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        Assert.assertEquals(2, sector.getOccupierCount());

        sector.free();
        Assert.assertNull(sector.getOccupier());
        Assert.assertEquals(0, sector.getOccupierCount());
    }

    @Test(expected = AssertionError.class)
    public void testFreeWhenOccupiedMoreThanZero() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        Assert.assertNull(sector.getOccupier());
        Assert.assertEquals(0, sector.getOccupierCount());

        sector.free(); // should throw an AssertionError
    }

    @Test
    public void testGetHeight() {
        Assert.assertEquals(5, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        Assert.assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        Assert.assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        Assert.assertFalse(sector.isFlag());
    }
}