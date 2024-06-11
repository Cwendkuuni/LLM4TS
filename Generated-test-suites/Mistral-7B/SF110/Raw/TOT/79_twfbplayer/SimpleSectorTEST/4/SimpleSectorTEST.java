package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*; import org.junit.*; import org.mockito.*;

import de.outstare.fortbattleplayer.model.*; import de.outstare.fortbattleplayer.model.impl.*;

public class SimpleSectorTest {

    @Test
    public void testGainControl() {
        // ... Expert 1's test case
    }

    @Test
    public void testFree() {
        // ... Expert 2's test case
    }

    @Test
    public void testGetBonus() {
        // ... Expert 3's test case
    }

    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(5, true, false, 2, 3, false, 1, CharacterClass.ARCHER);
        assertEquals(false, sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDERS);
        assertEquals(true, sector.isOccupied());
        sector.free();
        assertEquals(false, sector.isOccupied());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeHeight() {
        new SimpleSector(-5, true, false, 2, 3, false, 1, CharacterClass.ARCHER);
    }

    @Test(expected = NullPointerException.class)
    public void testNullCharClass() {
        SimpleSector sector = new SimpleSector(5, true, false, 2, 3, false, 1, null);
        assertNull(sector.getBonus(null));
    }

    @Test
    public void testClassBonus() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 2, 3, false, 1, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, false, 2, 3, false, 2, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, false, 2, 3, false, 1, CharacterClass.CAVALRY);
        assertEquals(1, sector1.classBonus(CharacterClass.ARCHER));
        assertEquals(2, sector2.classBonus(CharacterClass.ARCHER));
        assertEquals(0, sector3.classBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testHashCodeAndEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 2, 3, false, 1, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, false, 2, 3, false, 1, CharacterClass.ARCHER);
        SimpleSector sector3 = new SimpleSector(5, true, false, 2, 3, false, 1, CharacterClass.CAVALRY);
        assertEquals(sector1, sector1);
        assertEquals(sector1, sector2);
        assertNotEquals(sector1, sector3);
    }
}