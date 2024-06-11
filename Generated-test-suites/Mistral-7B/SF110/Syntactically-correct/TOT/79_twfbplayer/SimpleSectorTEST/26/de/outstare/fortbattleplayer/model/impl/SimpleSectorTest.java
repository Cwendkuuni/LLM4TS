import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {

 private SimpleSector sector;
 private Area area1;
 private Area area2;
 private CharacterClass charClass1;
 private CharacterClass charClass2;
 private Combatant combatant1;
 private Combatant combatant2;
 private SectorObserver sectorObserver;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
 area1 = Mockito.mock(Area.class);
 area2 = Mockito.mock(Area.class);
 charClass1 = CharacterClass.ARCHER;
 charClass2 = CharacterClass.KNIGHT;
 combatant1 = new Combatant(charClass1);
 combatant2 = new Combatant(charClass2);
 sectorObserver = Mockito.mock(SectorObserver.class);
 }

 @Test
 public void testFree_WhenSectorIsNotOccupied_ThenOccupierIsNull() {
 sector.free();
 assertNull(sector.getOccupier());
 }

 @Test
 public void testFree_WhenSectorIsOccupied_ThenOccupierCountDecreasesAndOccupierIsNull() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 verify(sector).occupierChanged();
 assertNull(sector.getOccupier());
 }

 @Test(expected = AssertionError.class)
 public void testFree_WhenSectorIsNotEmpty_ThenThrowsAssertionError() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 sector.free();
 }

 @Test
 public void testOccupierChanged_WhenObserverIsRegistered_ThenOccupierChangedIsCalled() {
 sector.addObserver(sectorObserver);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 verify(sectorObserver).occupierChanged();
 }

 @Test
 public void testGainControl_WhenSectorIsEmpty_ThenCombatantCanEnter() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testGainControl_WhenSectorIsOccupiedByDifferentSide_ThenCombatantCanEnter() {
 sector.gainControl(CombatantSide.ATTACKER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGainControl_WhenSectorIsAlreadyOccupiedBySameSide_ThenThrowsIllegalArgumentException() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 }

 @Test
 public void testGetOccupier_WhenSectorIsOccupied_ThenReturnsOccupier() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testIsOccupied_WhenSectorIsOccupied_ThenReturnsTrue() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testIsOccupied_WhenSectorIsNotOccupied_ThenReturnsFalse() {
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGetHeight_WhenHeightIsSet_ThenReturnsHeight() {
 assertEquals(5, sector.getHeight());
 }

 @Test
 public void testIsDefenderSpawn_WhenDefenderSpawnIsSetToTrue_ThenReturnsTrue() {
 assertTrue(sector.isDefenderSpawn());
 }

 @Test
 public void testIsDefenderSpawn_WhenDefenderSpawnIsSetToFalse_ThenReturnsFalse() {
 assertFalse(sector.isDefenderSpawn());
 }

 @Test
 public void testIsAttackerSpawn_WhenAttackerSpawnIsSetToTrue_ThenReturnsTrue() {
 assertTrue(sector.isAttackerSpawn());
 }

 @Test
 public void testIsAttackerSpawn_WhenAttackerSpawnIsSetToFalse_ThenReturnsFalse() {
 assertFalse(sector.isAttackerSpawn());
 }

 @Test
 public void testIsFlag_WhenFlagIsSetToTrue_ThenReturnsTrue() {
 assertTrue(sector.isFlag());
 }

 @Test
 public void testIsFlag_WhenFlagIsSetToFalse_ThenReturnsFalse() {
 assertFalse(sector.isFlag());
 }

 @Test
 public void testGetBonus_WhenCharacterClassIsSameAsClassType_ThenReturnsBonus() {
 assertEquals(new SectorBonus(10, 10, 5), sector.getBonus(charClass1));
 }

 @Test
 public void testGetBonus_WhenCharacterClassIsDifferentFromClassType_ThenReturnsZeroBonus() {
 assertEquals(new SectorBonus(0, 0, 0), sector.getBonus(charClass2));
 }

 @Test
 public void testGetAttackBonus_WhenCharacterClassIsSameAsClassType_ThenReturnsAttackBonusPlusClassBonusPlusWeaponBonus() {
 Mockito.when(area1.getOccupier()).thenReturn(combatant1);
 Mockito.when(area1.isOccupied()).thenReturn(true);
 Mockito.when(area2.isOccupied()).thenReturn(false);
 sector.addArea(area1);
 sector.addArea(area2);
 Mockito.when(combatant1.getWeapon()).thenReturn(Mockito.mock(Weapon.class));
 Mockito.when(combatant1.getWeapon().getSectorBonus(SectorBonus.BonusType.ATTACK)).thenReturn(3);
 assertEquals(13, sector.getAttackBonus(charClass1));
 }

 @Test
 public void testGetDefenseBonus_WhenCharacterClassIsSameAsClassType_ThenReturnsDefenseBonusPlusClassBonusPlusWeaponBonus() {
 Mockito.when(area1.getOccupier()).thenReturn(combatant1);
 Mockito.when(area1.isOccupied()).thenReturn(true);
 Mockito.when(area2.isOccupied()).thenReturn(false);
 sector.addArea(area1);
 sector.addArea(area2);
 Mockito.when(combatant1.getWeapon()).thenReturn(Mockito.mock(Weapon.class));
 Mockito.when(combatant1.getWeapon().getSectorBonus(SectorBonus.BonusType.DEFENSE)).thenReturn(4);
 assertEquals(10, sector.getDefenseBonus(charClass1));
 }

 @Test
 public void testClassBonus_WhenCharacterClassIsSameAsClassType_ThenReturnsClassBonus() {
 assertEquals(5, sector.classBonus(charClass1));
 }

 @Test
 public void testClassBonus_WhenCharacterClassIsDifferentFromClassType_ThenReturnsZeroBonus() {
 assertEquals(0, sector.classBonus(charClass2));
 }

 @Test
 public void testGetAreas_WhenAreasAreAdded_ThenReturnsSetOfAreas() {
 Set<Area> areas = new HashSet<Area>(Arrays.asList(area1, area2));
 assertEquals(areas, sector.getAreas());
 }

 @Test
 public void testAddArea_WhenAreaIsAdded_ThenAreaIsAddedToSet() {
 sector.addArea(area1);
 assertEquals(1, sector.getAreas().size());
 }

 @Test(expected = NullPointerException.class)
 public void testAddArea_WhenAreaIsNull_ThenThrowsNullPointerException() {
 sector.addArea(null);
 }

 @Test
 public void testHashCode_WhenTwoSectorsAreEqual_ThenHaveSameHashCode() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
 assertEquals(sector.hashCode(), sector2.hashCode());
 }

 @Test
 public void testEquals_WhenTwoObjectsAreEqual_ThenReturnTrue() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
 assertTrue(sector.equals(sector2));
 }

 @Test
 public void testEquals_WhenTwoObjectsAreNotEqual_ThenReturnFalse() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 5, 5, false, 5, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 5, 5, true, 5, CharacterClass.ARCHER);
 assertFalse(sector.equals(sector3));
 }

 @Test
 public void testAddObserver_WhenObserverIsAdded_ThenObserverIsAddedToSet() {
 sector.addObserver(sectorObserver);
 assertEquals(1, sector.getObservers().size());
 }

 @Test
 public void testRemoveObserver_WhenObserverIsRemoved_ThenObserverIsRemovedFromSet() {
 sector.addObserver(sectorObserver);
 sector.removeObserver(sectorObserver);
 assertEquals(0, sector.getObservers().size());
 }

 @Test
 public void testRemoveObserver_WhenObserverIsNotRegistered_ThenDoesNotThrowException() {
 sector.removeObserver(sectorObserver);
 }
}