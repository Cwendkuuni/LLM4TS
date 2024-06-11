import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {
    // ... (import statements and instance variable declarations)

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
    }

    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }

    // ... (other test cases for other methods)
}