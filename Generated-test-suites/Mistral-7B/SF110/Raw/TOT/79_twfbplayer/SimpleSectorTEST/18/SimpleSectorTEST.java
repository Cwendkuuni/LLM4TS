import static org.junit.Assert.*;
import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.*;

public class SimpleSectorTest {
    

    @Test
    public void testFreeWhenEmpty() {
        SimpleSector sector = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }

    @Test(expected = AssertionError.class)
    public void testFreeWithCombatants() {
        SimpleSector sector = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertEquals(1, sector.getOccupierCount()); // This shouldn't be possible
    }

    
}