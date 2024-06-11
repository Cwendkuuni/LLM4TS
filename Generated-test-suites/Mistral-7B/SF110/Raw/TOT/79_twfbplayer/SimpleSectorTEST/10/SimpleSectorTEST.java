import static org.junit.Assert.*;
import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.*;

public class SimpleSectorTest {
    // ... (other imports and setup code)

    @Test
    public void testFreeWithNoCombatants() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 10, false, 5, CharacterClass.ARCHER);
        sector.free();
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }

    @Test
    public void testFreeWithOneCombatant() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 10, false, 5, CharacterClass.ARCHER);
        Area area = new Area(new Location(0, 0));
        sector._addArea(area);
        Combatant combatant = new Combatant(CharacterClass.ARCHER, new Location(0, 0));
        area.setOccupier(combatant);
        sector.gainControl(CombatantSide.DEFENDERS);
        sector.free();
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }

    @Test(expected = AssertionError.class)
    public void testFreeWithMoreCombatantsThanZero() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 10, false, 5, CharacterClass.ARCHER);
        Area area = new Area(new Location(0, 0));
        sector._addArea(area);
        Combatant combatant1 = new Combatant(CharacterClass.ARCHER, new Location(0, 0));
        area.setOccupier(combatant1);
        Combatant combatant2 = new Combatant(CharacterClass.ARCHER, new Location(0, 0));
        sector.gainControl(CombatantSide.DEFENDERS);
        sector.gainControl(CombatantSide.DEFENDERS);
        sector.free();
        // The test should throw an AssertionError because occupierCount is not zero
        sector.free();
    }

    // ... (other test cases for various methods)
}