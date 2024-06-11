
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.*;

public class SimpleSectorTest {

    @Test
    public void testSimpleSectorInitialization() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        assertEquals(5, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertTrue(sector.isAttackerSpawn());
        assertFalse(sector.isFlag());
        assertEquals(5, sector.getClassBonus());
        assertEquals(CharacterClass.ARCHER, sector.getClassType());
        assertTrue(sector.isOccupied()); // default occupier is null
    }

    @Test
    public void testSimpleSectorEmptyInitialization() {
        SimpleSector sector = new SimpleSector(5);
        assertEquals(5, sector.getHeight());
        assertFalse(sector.isDefenderSpawn());
        assertFalse(sector.isAttackerSpawn());
        assertFalse(sector.isFlag());
        assertEquals(0, sector.getClassBonus());
        assertNull(sector.getClassType());
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testFreeMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDERS);
        sector.free();
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }

    @Test
    public void testOccupierChangedMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        Mockito.spy(new SectorObserver() {
            @Override
            public void occupierChanged() {
                verify(this).occupierChanged();
            }
        });
        sector.addObserver(Mockito.spy(new SectorObserver() {}));
        sector.gainControl(CombatantSide.ATTACKERS);
        sector.free();
        sector.gainControl(CombatantSide.DEFENDERS);
    }

    @Test
    public void testGetBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        SectorBonus expectedBonus = new SectorBonus(15, 10, 5);
        SectorBonus actualBonus = sector.getBonus(CharacterClass.ARCHER);
        assertEquals(expectedBonus, actualBonus);
    }

    @Test
    public void testGetBonusMethodWithInvalidCharacterClass() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        SectorBonus expectedBonus = new SectorBonus(0, 0, 0);
        SectorBonus actualBonus = sector.getBonus(CharacterClass.MAGE);
        assertEquals(expectedBonus, actualBonus);
    }

    @Test
    public void testGainControlMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDERS);
        sector.gainControl(CombatantSide.DEFENDERS);
        assertEquals(CombatantSide.DEFENDERS, sector.getOccupier());
    }

    @Test
    public void testGetOccupierMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDERS);
        assertEquals(CombatantSide.DEFENDERS, sector.getOccupier());
    }

    @Test
    public void testIsOccupiedMethodWithNoOccupier() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testIsOccupiedMethodWithOccupier() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        sector.gainControl(CombatantSide.DEFENDERS);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testFreeMethodWithNoOccupier() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        assertFalse(sector.isOccupied());
        sector.free();
        assertNull(sector.getOccupier());
        assertEquals(0, sector.getOccupierCount());
    }

    @Test(expected = IllegalStateException.class)
    public void testGainControlMethodWithMaxCapacity() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        for (int i = 0; i < 6; i++) {
            sector.gainControl(CombatantSide.DEFENDERS);
        }
        sector.gainControl(CombatantSide.DEFENDERS);
    }

    @Test
    public void testGetDamageBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        Area area = Mockito.spy(new Area() {
            @Override
            public Combatant getOccupier() {
                return Mockito.mock(Combatant.class);
            }

            @Override
            public Weapon getWeapon() {
                return Mockito.mock(Weapon.class);
            }
        });
        sector.addArea(area);
        Mockito.when(area.isOccupied()).thenReturn(true);
        Mockito.when(area.getOccupier().getWeapon().getSectorBonus(SectorBonus.BonusType.DAMAGE)).thenReturn(3);
        assertEquals(13, sector.getDamageBonus());
    }

    @Test
    public void testGetDefenseBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        Area area = Mockito.spy(new Area() {
            @Override
            public Combatant getOccupier() {
                return Mockito.mock(Combatant.class);
            }

            @Override
            public Weapon getWeapon() {
                return Mockito.mock(Weapon.class);
            }
        });
        sector.addArea(area);
        Mockito.when(area.isOccupied()).thenReturn(true);
        Mockito.when(area.getOccupier().getWeapon().getSectorBonus(SectorBonus.BonusType.DEFENSE)).thenReturn(3);
        assertEquals(13, sector.getDefenseBonus());
    }

    @Test
    public void testClassBonusMethod() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        assertEquals(5, sector.classBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testClassBonusMethodWithInvalidCharacterClass() {
        SimpleSector sector = new SimpleSector(5, true, true, 10, 15, false, 5, CharacterClass.ARCHER);
        assertEquals(0, sector.classBonus(CharacterClass.MAGE));
    }
}
