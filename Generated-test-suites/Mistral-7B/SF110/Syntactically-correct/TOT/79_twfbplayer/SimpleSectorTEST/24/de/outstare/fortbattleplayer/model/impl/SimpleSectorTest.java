import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SimpleSectorTest {

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.ARCHER);
        Combatant combatant = new Combatant("Test Combatant", CharacterClass.ARCHER, new Weapon("Test Weapon", 5, 3));
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER, combatant);
        sector.free();
        assertFalse(sector.isOccupied());
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }
}