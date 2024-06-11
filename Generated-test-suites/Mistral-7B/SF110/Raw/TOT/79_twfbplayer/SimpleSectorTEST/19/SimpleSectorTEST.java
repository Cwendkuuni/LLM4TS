import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.observer.SectorObserver;

public class SimpleSectorTest {
    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(5, true, false, 10, 5, false, 5, CharacterClass.Knight);
    }

    // Test Cases for each expert go here

    @Test
    public void testAddObserver_WhenObserverAdded_AddedToObserversSet() {
        SectorObserver observer = Mockito.mock(SectorObserver.class);
        sector.addObserver(observer);

        Set<SectorObserver> observers = sector.getObservers();
        assertTrue(observers.contains(observer));
    }

    @Test
    public void testRemoveObserver_WhenObserverRemoved_RemovedFromObserversSet() {
        SectorObserver observer = Mockito.mock(SectorObserver.class);
        sector.addObserver(observer);
        sector.removeObserver(observer);

        Set<SectorObserver> observers = sector.getObservers();
        assertFalse(observers.contains(observer));
    }

    @Test
    public void testGetBonus_WhenCharacterClassIsNotMatching_ReturnsZeroBonus() {
        sector.gainControl(CombatantSide.DEFENDER);
        CharacterClass charClass = CharacterClass.Archer;
        SectorBonus sectorBonus = sector.getBonus(charClass);

        assertEquals(0, sectorBonus.getAttackBonus());
        assertEquals(0, sectorBonus.getDefenseBonus());
        assertEquals(0, sectorBonus.getDamageBonus());
    }

    @Test
    public void testGetBonus_WhenCharacterClassMatches_ReturnsBonus() {
        sector.gainControl(CombatantSide.DEFENDER);
        CharacterClass charClass = CharacterClass.Knight;
        SectorBonus sectorBonus = sector.getBonus(charClass);

        assertEquals(10, sectorBonus.getAttackBonus());
        assertEquals(5, sectorBonus.getDefenseBonus());
        assertEquals(5, sectorBonus.getDamageBonus());
    }

    @Test
    public void testGetDamageBonus_WhenNoBonusArea_ReturnsZero() {
        sector.gainControl(CombatantSide.DEFENDER);

        assertEquals(0, sector.getDamageBonus());
    }

    @Test
    public void testGetDamageBonus_WhenBonusAreaIsPresent_ReturnsBonus() {
        Area area = Mockito.mock(Area.class);
        when(area.isOccupied()).thenReturn(true);
        Weapon weapon = Mockito.mock(Weapon.class);
        when(weapon.getSectorBonus(SectorBonus.BonusType.DAMAGE)).thenReturn(10);
        when(area.getOccupier()).thenReturn(Mockito.mock(Combatant.class));
        when(area.getOccupier().getWeapon()).thenReturn(weapon);

        sector.addArea(area);
        sector.gainControl(CombatantSide.DEFENDER);

        assertEquals(10, sector.getDamageBonus());
    }
}