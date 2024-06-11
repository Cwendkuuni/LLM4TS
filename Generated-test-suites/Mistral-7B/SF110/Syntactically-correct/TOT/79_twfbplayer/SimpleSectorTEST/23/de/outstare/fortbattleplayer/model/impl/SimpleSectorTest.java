import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SimpleCharacter;

public class SimpleSectorTest {
    private SimpleSector sector;
    private Set<Area> areas;
    private CharacterClass characterClass;
    private SimpleCharacter character;
    private Combatant combatant;

    @Test
    public void testFree() {
        
        
        // Test initialization
        sector = new SimpleSector(1, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        assertEquals(0, sector.getOccupierCount());
        assertFalse(sector.isOccupied());
        assertNull(sector.getOccupier());

        // Test with no combatant
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertFalse(sector.isOccupied());
        assertNull(sector.getOccupier());

        // Test with one combatant
        characterClass = CharacterClass.ARCHER;
        character = new SimpleCharacter(characterClass);
        combatant = new Combatant(character, CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER); // occupierCount should be 2, but should be 3 due to constructor initialization
        sector.free();
        assertEquals(1, sector.getOccupierCount());
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());

        // Test with more combatants
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        sector.free();
        assertEquals(1, sector.getOccupierCount());
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier(1));

        // Test with no combatants
        sector.free();
        assertEquals(0, sector.getOccupierCount());
        assertFalse(sector.isOccupied());
        assertNull(sector.getOccupier());

        
    }
}