import static org.junit.Assert.*;
import de.outstare.fortbattleplayer.model.*;
import org.junit.*;

public class SimpleSectorTest {
    

    @Test
    public void testSimpleSectorConstructor() {
        SimpleSector sector = new SimpleSector(1, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        assertEquals(1, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        // ... other assertions
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleSectorNegativeHeight() {
        new SimpleSector(-1, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
    }

    // ... other test methods for each public method

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(1, true, true, 5, 3, true, 2, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDERS);
        assertEquals(1, sector.getOccupierCount());
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }

    // ... other test methods for edge cases and exception handling

    
}