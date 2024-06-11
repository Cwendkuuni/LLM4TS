import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.*;

public class SimpleSectorTest {

 private SimpleSector sector;
 private Area area;
 private SectorObserver observer;
 private Combatant combatant;
 private Weapon weapon;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 area = mock(Area.class);
 observer = mock(SectorObserver.class);
 combatant = mock(Combatant.class);
 weapon = mock(Weapon.class);
 when(combatant.getWeapon()).thenReturn(weapon);
 }

 @Test
 public void testFree_WhenNoCombatant_ThenNothingHappens() {
 sector.free();
 verifyNoMoreInteractions(observer);
 }

 @Test
 public void testFree_WhenCombatantPresent_ThenCombatantIsRemoved() {
 sector.gainControl(CombatantSide.ATTACKER);
 sector.free();
 verify(combatant).leaveSector();
 }

 @Test
 public void testFree_WhenCombatantCountIsZero_ThenNothingHappens() {
 sector.free();
 verifyNoInteractions(observer);
 }

 @Test(expected = AssertionError.class)
 public void testFree_WhenCombatantCountIsNegative_ThenErrorIsThrown() {
 sector.gainControl(CombatantSide.ATTACKER);
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 sector.free();
 sector.free();
 assert sector.occupierCount < 0;
 }

 @Test
 public void testOccupierChanged_WhenObserverIsPresent_ThenObserverIsNotified() {
 sector.addObserver(observer);
 sector.gainControl(CombatantSide.ATTACKER);
 verify(observer).occupierChanged();
 }

 @Test
 public void testGainControl_WhenSameSideIsPresent_ThenSideIsGained() {
 sector.free();
 sector.gainControl(CombatantSide.ATTACKER);
 verify(combatant).enterSector();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGainControl_WhenDifferentSideIsPresent_ThenErrorIsThrown() {
 sector.free();
 sector.gainControl(CombatantSide.DEFENDER);
 }

 @Test
 public void testGetOccupier_WhenOccupied_ThenCorrectSideIsReturned() {
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 }

 @Test
 public void testIsOccupied_WhenOccupied_ThenTrueIsReturned() {
 sector.gainControl(CombatantSide.ATTACKER);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testIsOccupied_WhenEmpty_ThenFalseIsReturned() {
 sector.free();
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testGetHeight_WhenSet_ThenCorrectValueIsReturned() {
 assertEquals(5, sector.getHeight());
 }

 @Test
 public void testIsDefenderSpawn_WhenSetToTrue_ThenTrueIsReturned() {
 assertTrue(sector.isDefenderSpawn());
 }

 @Test
 public void testIsAttackerSpawn_WhenSetToTrue_ThenTrueIsReturned() {
 assertTrue(sector.isAttackerSpawn());
 }

 @Test
 public void testIsFlag_WhenSetToFalse_ThenFalseIsReturned() {
 assertFalse(sector.isFlag());
 }

 @Test
 public void testGetBonus_WhenOtherClassIsPresent_ThenZeroIsReturned() {
 assertEquals(new SectorBonus(0, 0, 0), sector.getBonus(CharacterClass.KNIGHT));
 }

 @Test
 public void testGetBonus_WhenSameClassIsPresent_ThenCorrectBonusIsReturned() {
 assertEquals(new SectorBonus(15, 15, 5), sector.getBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDamageBonus_WhenNoAreaIsOccupied_ThenZeroIsReturned() {
 assertEquals(0, sector.getDamageBonus());
 }

 @Test
 public void testGetDamageBonus_WhenAreaIsOccupied_ThenCorrectBonusIsReturned() {
 sector._addArea(area);
 when(area.isOccupied()).thenReturn(true);
 when(weapon.getSectorBonus(SectorBonus.BonusType.DAMAGE)).thenReturn(3);
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(3, sector.getDamageBonus());
 }

 @Test
 public void testGetAttackBonus_WhenOtherClassIsPresent_ThenZeroIsReturned() {
 assertEquals(10, sector.getAttackBonus(CharacterClass.KNIGHT));
 }

 @Test
 public void testGetAttackBonus_WhenSameClassIsPresent_ThenCorrectBonusIsReturned() {
 assertEquals(15, sector.getAttackBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDefenseBonus_WhenOtherClassIsPresent_ThenZeroIsReturned() {
 assertEquals(5, sector.getDefenseBonus(CharacterClass.KNIGHT));
 }

 @Test
 public void testGetDefenseBonus_WhenSameClassIsPresent_ThenCorrectBonusIsReturned() {
 assertEquals(10, sector.getDefenseBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testClassBonus_WhenOtherClassIsPresent_ThenZeroIsReturned() {
 assertEquals(0, sector.classBonus(CharacterClass.KNIGHT));
 }

 @Test
 public void testClassBonus_WhenSameClassIsPresent_ThenCorrectBonusIsReturned() {
 assertEquals(5, sector.classBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testHashCode_WhenEquals_ThenEqualHashCodeIsReturned() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 assertEquals(sector.hashCode(), sector2.hashCode());
 }

 @Test
 public void testEquals_WhenEqualObjects_ThenTrueIsReturned() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 assertTrue(sector.equals(sector2));
 }

 @Test
 public void testEquals_WhenDifferentObjects_ThenFalseIsReturned() {
 SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(5, true, true, 10, 5, false, 5, CharacterClass.KNIGHT);
 assertFalse(sector.equals(sector3));
 }

 @Test
 public void testAddObserver_WhenObserverIsAdded_ThenObserverIsAdded() {
 sector.addObserver(observer);
 assertTrue(sector.observers.contains(observer));
 }

 @Test
 public void testRemoveObserver_WhenObserverIsRemoved_ThenObserverIsRemoved() {
 sector.addObserver(observer);
 sector.removeObserver(observer);
 assertFalse(sector.observers.contains(observer));
 }
}