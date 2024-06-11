package de.outstare.fortbattleplayer.model.impl.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.*;

public class SimpleSectorTest {


@Test
public void testFree_InitiallyOccupied_DecreasesOccupierCountAndSetsNullOccupier() {
    SimpleSector sector = new SimpleSector(1, true, false, 1, 1, false, 1, CharacterClass.ARCHER);
    sector.gainControl(CombatantSide.ATTACKER);

    sector.free();

    assertEquals(0, sector.getOccupierCount());
    assertNull(sector.getOccupier());
}

@Test
public void testGainControl_Unoccupied_IncreasesOccupierCountAndSetsGivenSide() {
    SimpleSector sector = new SimpleSector(1, false, false, 1, 1, false, 1, CharacterClass.ARCHER);

    sector.gainControl(CombatantSide.DEFENDER);

    assertEquals(1, sector.getOccupierCount());
    assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
}

@Test
public void testGetBonusFromWeapons_NoWeapons_ReturnsZero() {
    SimpleSector sector = new SimpleSector(1, false, false, 1, 1, false, 1, CharacterClass.ARCHER);

    assertEquals(0, sector.getBonusFromWeapons(SectorBonus.BonusType.ATTACK));
}

@Test
public void testGetBonusFromWeapons_WeaponPresent_ReturnsCorrectBonus() {
    SimpleSector sector = new SimpleSector(1, false, false, 1, 1, false, 1, CharacterClass.ARCHER);
    Area area = new SimpleArea(new Point(0, 0), CharacterClass.ARCHER);
    sector._addArea(area);
    Combatant combatant = new SimpleCombatant(CharacterClass.ARCHER, new Weapon(WeaponType.BOW, 10, 5));
    area.setOccupier(combatant);

    assertEquals(5, sector.getBonusFromWeapons(SectorBonus.BonusType.ATTACK));
}

@Test
public void testGetBonusFromWeapons_MultipleWeapons_ReturnsCorrectSumOfBonuses() {
    SimpleSector sector = new SimpleSector(1, false, false, 1, 1, false, 1, CharacterClass.ARCHER);
    Area area1 = new SimpleArea(new Point(0, 0), CharacterClass.ARCHER);
    Area area2 = new SimpleArea(new Point(1, 1), CharacterClass.ARCHER);
    sector._addArea(area1);
    sector._addArea(area2);
    Combatant combatant1 = new SimpleCombatant(CharacterClass.ARCHER, new Weapon(WeaponType.BOW, 5, 3));
    Combatant combatant2 = new SimpleCombatant(CharacterClass.ARCHER, new Weapon(WeaponType.SWORD, 3, 2));
    area1.setOccupier(combatant1);
    area2.setOccupier(combatant2);

    assertEquals(11, sector.getBonusFromWeapons(SectorBonus.BonusType.ATTACK));
}

// Add more test cases for other methods and edge cases as needed

}
