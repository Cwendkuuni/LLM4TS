import static org.junit.Assert.*;
import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.*;

public class SimpleSectorTest {
    SimpleSector sector;
    Combatant combatant;

    @Before
    public void setUp() {
        sector = new SimpleSector(1, true, true, 1, 1, false, 1, CharacterClass.ARCHER);
        combatant = new Combatant(CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.addObserver(new SectorObserver() {
            @Override
            public void occupierChanged() {
                // no operation
            }
        });
    }

    @Test
    public void testFree() {
        assertEquals(1, sector.getOccupierCount());
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        sector.free();
        assertNull(sector.getOccupier());
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.getOccupierCount());
        try {
            sector.free();
        } catch (AssertionError e) {
            assertTrue(true);
        }
    }
}