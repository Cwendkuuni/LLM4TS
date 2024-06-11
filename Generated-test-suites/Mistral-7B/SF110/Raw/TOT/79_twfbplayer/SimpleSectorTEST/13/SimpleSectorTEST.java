import static org.junit.Assert.*;
import de.outstare.fortbattleplayer.model.impl.*;

public class SimpleSectorTest {
    // test setup and teardown code here
    
    @Test
    public void testFreeEmptySector() {
        SimpleSector sector = new SimpleSector(1, false, false, 0, 0, false, 0, CharacterClass.ARCHER);
        sector.free();
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }

    @Test(expected = AssertionError.class)
    public void testFreeNotEmptySector() {
        SimpleSector sector = new SimpleSector(1, false, false, 0, 0, false, 0, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
    }
}