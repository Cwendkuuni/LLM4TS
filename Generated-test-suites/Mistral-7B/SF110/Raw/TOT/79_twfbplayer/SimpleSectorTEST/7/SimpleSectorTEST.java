
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.*;
import org.junit.*;

public class SimpleSectorTest {

    SimpleSector sector;
    Combatant combatant;

    @Before
    public void setup() {
        sector = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        combatant = mock(Combatant.class);
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());

        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());

        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());

        sector.free();
        assertEquals(null, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);

        Assert.assertFalse(sector.isOccupied());

        sector.gainControl(CombatantSide.DEFENDER);
        Assert.assertTrue(sector.isOccupied());

        sector.free();
        Assert.assertFalse(sector.isOccupied());
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);

        SectorBonus expectedBonusArcher = new SectorBonus(3, 5, 2);
        SectorBonus expectedBonusWarrior = new SectorBonus(4, 6, 3);

        assertEquals(expectedBonusArcher, sector.getBonus(CharacterClass.ARCHER));
        assertEquals(expectedBonusWarrior, sector.getBonus(CharacterClass.WARRIOR));
    }

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertEquals(null, sector.getOccupier());
    }

    @Test(expected = AssertionError.class)
    public void testFreeWithCombatants() {
        SimpleSector sector = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
    }

    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(5, true, false, 1, 1, true, 2, CharacterClass.ARCHER);
        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        assertEquals(0, sector.getAreas().size());
    }

    @Test
    public void testAddAndRemoveObserver() {
        SimpleSector sector = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);

        SectorObserver observer = mock(SectorObserver.class);

        sector.addObserver(observer);
        verify(observer).occupierChanged();

        sector.removeObserver(observer);
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void testHashCodeAndEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.ARCHER);

        assertEquals(sector1, sector2);
        assertTrue(sector1.hashCode() == sector2.hashCode());

        SimpleSector sector3 = new SimpleSector(5, true, false, 1, 1, false, 2, CharacterClass.MAGE);
        assertNotSame(sector1, sector3);
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
    }
}
