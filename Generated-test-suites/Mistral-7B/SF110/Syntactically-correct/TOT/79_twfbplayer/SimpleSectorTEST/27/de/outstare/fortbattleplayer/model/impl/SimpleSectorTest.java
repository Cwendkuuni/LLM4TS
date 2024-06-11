import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SimpleSectorObserver;

public class SimpleSectorTest {

    @org.junit.Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.ARCHER);
        Combatant combatant = new Combatant(CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDERS);
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());

        sector.gainControl(CombatantSide.DEFENDERS);
        sector.addObserver(new SimpleSectorObserver() {
            @Override
            public void occupierChanged() {
                // assert occupier changed
            }
        });
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertNull(sector.getOccupier());
    }
}