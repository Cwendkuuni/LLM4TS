import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.*;

public class SimpleSectorTest {

    SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
    }

    @Test
    public void constructorTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
    }

    @Test
    public void freeTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertNull(sector.getOccupier());
    }

    @Test
    public void gainControlTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertSame(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void getOccupierTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertSame(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void isOccupiedTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void getHeightTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
    }

    @Test
    public void isDefenderSpawnTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void isAttackerSpawnTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        assertTrue(sector.isAttackerSpawn());
    }

    @Test
    public void isFlagTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        assertFalse(sector.isFlag());
    }

    @Test
    public void getBonusTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        CharacterClass archer = new CharacterClass("Archer");
        SectorBonus expectedBonus = new SectorBonus(5, 5, 5);
        assertEquals(expectedBonus, sector.getBonus(archer));
    }

    @Test
    public void getDamageBonusTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        assertEquals(5, sector.getDamageBonus());
    }

    @Test
    public void getAttackBonusTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        CharacterClass archer = new CharacterClass("Archer");
        assertEquals(10, sector.getAttackBonus(archer));
    }

    @Test
    public void getDefenseBonusTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        CharacterClass archer = new CharacterClass("Archer");
        assertEquals(5, sector.getDefenseBonus(archer));
    }

    @Test
    public void classBonusTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        CharacterClass archer = new CharacterClass("Archer");
        CharacterClass knight = new CharacterClass("Knight");
        assertEquals(5, sector.classBonus(archer));
        assertEquals(5, sector.classBonus(knight));
    }

    @Test
    public void getAreasTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        Set<Area> areas = new HashSet<>();
        areas.add(new Area());
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void addAreaTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        Set<Area> areas = new HashSet<>();
        areas.add(new Area());
        sector._addArea(areas.iterator().next());
        assertEquals(areas, sector.getAreas());
    }

    @Test
    public void hashCodeTest() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        assertEquals(sector1.hashCode(), sector2.hashCode());
    }

    @Test
    public void equalsTest() {
        SimpleSector sector1 = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        SimpleSector sector2 = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        assertTrue(sector1.equals(sector2));
    }

    @Test
    public void addObserverTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        SectorObserver observer = Mockito.mock(SectorObserver.class);
        sector.addObserver(observer);
        verify(observer).occupierChanged();
    }

    @Test
    public void removeObserverTest() {
        SimpleSector sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
        SectorObserver observer = Mockito.mock(SectorObserver.class);
        sector.addObserver(observer);
        sector.removeObserver(observer);
        verifyNoMoreInteractions(observer);
    }
}