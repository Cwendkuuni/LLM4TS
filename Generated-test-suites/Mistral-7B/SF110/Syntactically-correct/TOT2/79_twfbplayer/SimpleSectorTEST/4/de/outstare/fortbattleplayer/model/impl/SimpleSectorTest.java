
package de.outstare.fortbattleplayer.model.impl.test;

import org.junit.Assert;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.impl.*;
import de.outstare.fortbattleplayer.model.impl.test.util.TestUtil;

public class SimpleSectorTest {
    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(5, true, false, 1, 1, false, 1, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetHeightWithNegativeHeight() {
        SimpleSector sector = new SimpleSector(-5, true, false, 1, 1, false, 1, CharacterClass.ARCHER);
    }

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(5, true, false, 1, 1, false, 1, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }

    @Test
    public void testOccupierChanged() {
        SimpleSector sector = new SimpleSector(5, true, false, 1, 1, false, 1, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        sector.gainControl(CombatantSide.ATTACKER);
        sector.occupierChanged();
        for (final SectorObserver observer : sector.getObservers()) {
            observer.occupierChanged();
        }
    }

    // Test other methods and their edge cases here
}
