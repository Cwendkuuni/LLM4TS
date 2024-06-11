import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class SimpleSectorTest {

 private SimpleSector simpleSector;
 private Area area1;
 private Area area2;
 private SectorObserver sectorObserver;

 @Before
 public void setUp() {
 simpleSector = new SimpleSector(1, true, true, 5, 3, false, 2, CharacterClass.ARCHER);
 area1 = Mockito.mock(Area.class);
 area2 = Mockito.mock(Area.class);
 sectorObserver = Mockito.mock(SectorObserver.class);
 }

 @Test
 public void testFree_WhenNoCombatantIsPresent_ThenOccupierIsNullAndOccupierCountIsZero() {
 simpleSector.free();
 assertNull(simpleSector.getOccupier());
 assertEquals(0, simpleSector.getOccupierCount());
 }

 @Test
 public void testFree_WhenCombatantIsPresent_ThenOccupierIsNullAndOccupierCountIsDecremented() {
 simpleSector.gainControl(CombatantSide.DEFENDER);
 simpleSector.free();
 assertNull(simpleSector.getOccupier());
 assertEquals(0, simpleSector.getOccupierCount());
 }

 @Test(expected = AssertionError.class)
 public void testFree_WhenMoreCombatantsAreLeftThanEntered_ThenThrowsAssertionError() {
 simpleSector.gainControl(CombatantSide.ATTACKER);
 simpleSector.gainControl(CombatantSide.DEFENDER);
 simpleSector.free();
 simpleSector.free(); // Intentionally adding an extra free call
 }

 @Test
 public void testOccupierChanged_WhenObserverIsPresent_ThenOccupierChangedIsCalled() {
 simpleSector.addObserver(sectorObserver);
 simpleSector.gainControl(CombatantSide.ATTACKER);
 verify(sectorObserver).occupierChanged();
 }

 @Test
 public void testGainControl_WhenSectorIsNotOccupied_ThenNewCombatantIsOccupierAndOccupierCountIsIncremented() {
 simpleSector.free();
 simpleSector.gainControl(CombatantSide.ATTACKER);
 assertSame(CombatantSide.ATTACKER, simpleSector.getOccupier());
 assertEquals(1, simpleSector.getOccupierCount());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGainControl_WhenSectorIsAlreadyOccupiedByOtherSide_ThenThrowsIllegalArgumentException() {
 simpleSector.gainControl(CombatantSide.DEFENDER);
 simpleSector.gainControl(CombatantSide.ATTACKER);
 try {
 simpleSector.gainControl(CombatantSide.DEFENDER);
 } catch (IllegalArgumentException e) {
 assertEquals("Sector is already occupied by another side", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testGetOccupier_WhenSectorIsOccupied_ThenReturnsOccupier() {
 simpleSector.gainControl(CombatantSide.ATTACKER);
 assertSame(CombatantSide.ATTACKER, simpleSector.getOccupier());
 }

 @Test
 public void testIsOccupied_WhenSectorIsOccupied_ThenReturnsTrue() {
 simpleSector.gainControl(CombatantSide.ATTACKER);
 assertTrue(simpleSector.isOccupied());
 }

 @Test
 public void testIsOccupied_WhenSectorIsNotOccupied_ThenReturnsFalse() {
 simpleSector.free();
 assertFalse(simpleSector.isOccupied());
 }

 @Test
 public void testGetHeight_WhenHeightIsSet_ThenReturnsHeight() {
 assertEquals(1, simpleSector.getHeight());
 }

 @Test
 public void testIsDefenderSpawn_WhenDefenderSpawnIsSet_ThenReturnsTrue() {
 assertTrue(simpleSector.isDefenderSpawn());
 }

 @Test
 public void testIsAttackerSpawn_WhenAttackerSpawnIsSet_ThenReturnsTrue() {
 assertTrue(simpleSector.isAttackerSpawn());
 }

 @Test
 public void testIsFlag_WhenFlagIsSet_ThenReturnsTrue() {
 assertTrue(simpleSector.isFlag());
 }

 @Test
 public void testGetBonus_WhenCharClassIsNotMatching_ThenReturnsZero() {
 assertEquals(0, simpleSector.getBonus(CharacterClass.MAGE));
 }

 @Test
 public void testGetBonus_WhenCharClassIsMatching_ThenReturnsCorrectBonus() {
 assertEquals(5, simpleSector.getBonus(CharacterClass.ARCHER).getAttackBonus());
 assertEquals(3, simpleSector.getBonus(CharacterClass.ARCHER).getDefenseBonus());
 }

 @Test
 public void testGetDamageBonus_WhenNoWeaponIsPresent_ThenReturnsZero() {
 assertEquals(0, simpleSector.getDamageBonus());
 }

 @Test
 public void testGetDamageBonus_WhenWeaponIsPresent_ThenReturnsCorrectBonus() {
 simpleSector._addArea(area1);
 Mockito.when(area1.getOccupier()).thenReturn(new Combatant(CharacterClass.ARCHER, new Weapon(5)));
 assertEquals(5, simpleSector.getDamageBonus());
 }

 @Test
 public void testGetAttackBonus_WhenCharClassIsNotMatching_ThenReturnsBaseAttackBonus() {
 assertEquals(5, simpleSector.getAttackBonus(CharacterClass.MAGE));
 }

 @Test
 public void testGetAttackBonus_WhenCharClassIsMatching_ThenReturnsCorrectAttackBonus() {
 assertEquals(10, simpleSector.getAttackBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDefenseBonus_WhenCharClassIsNotMatching_ThenReturnsBaseDefenseBonus() {
 assertEquals(3, simpleSector.getDefenseBonus(CharacterClass.MAGE));
 }

 @Test
 public void testGetDefenseBonus_WhenCharClassIsMatching_ThenReturnsCorrectDefenseBonus() {
 assertEquals(6, simpleSector.getDefenseBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testClassBonus_WhenCharClassIsNotMatching_ThenReturnsZero() {
 assertEquals(0, simpleSector.classBonus(CharacterClass.MAGE));
 }

 @Test
 public void testClassBonus_WhenCharClassIsMatching_ThenReturnsCorrectClassBonus() {
 assertEquals(2, simpleSector.classBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetAreas_WhenAreasArePresent_ThenReturnsAreas() {
 Set<Area> areas = new HashSet<>(Arrays.asList(area1, area2));
 simpleSector._addArea(area1);
 simpleSector._addArea(area2);
 assertEquals(areas, simpleSector.getAreas());
 }

 @Test
 public void testAddObserver_WhenObserverIsAdded_ThenObserverIsAddedToObserverSet() {
 simpleSector.addObserver(sectorObserver);
 assertTrue(simpleSector.observers.contains(sectorObserver));
 }

 @Test
 public void testRemoveObserver_WhenObserverIsPresent_ThenObserverIsRemovedFromObserverSet() {
 simpleSector.addObserver(sectorObserver);
 simpleSector.removeObserver(sectorObserver);
 assertFalse(simpleSector.observers.contains(sectorObserver));
 }

 @Test
 public void testHashCode_WhenObjectIsCreatedWithGivenData_ThenReturnsCorrectHashCode() {
 SimpleSector simpleSector1 = new SimpleSector(1, true, true, 5, 3, false, 2, CharacterClass.ARCHER);
 SimpleSector simpleSector2 = new SimpleSector(1, true, true, 5, 3, false, 2, CharacterClass.ARCHER);
 assertEquals(simpleSector1.hashCode(), simpleSector2.hashCode());
 }

 @Test
 public void testEquals_WhenObjectsAreEqual_ThenReturnsTrue() {
 SimpleSector simpleSector1 = new SimpleSector(1, true, true, 5, 3, false, 2, CharacterClass.ARCHER);
 SimpleSector simpleSector2 = new SimpleSector(1, true, true, 5, 3, false, 2, CharacterClass.ARCHER);
 assertTrue(simpleSector1.equals(simpleSector2));
 }

 @Test
 public void testEquals_WhenObjectsAreNotEqual_ThenReturnsFalse() {
 SimpleSector simpleSector1 = new SimpleSector(1, true, true, 5, 3, false, 2, CharacterClass.ARCHER);
 SimpleSector simpleSector2 = new SimpleSector(1, true, true, 5, 3, false, 2, CharacterClass.MAGE);
 assertFalse(simpleSector1.equals(simpleSector2));
 }
}