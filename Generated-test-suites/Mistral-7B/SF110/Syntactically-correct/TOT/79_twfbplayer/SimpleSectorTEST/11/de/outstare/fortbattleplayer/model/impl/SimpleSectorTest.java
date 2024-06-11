import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
 }

 @Test
 public void testFree_NoCombatants() {
 sector.free();
 assertNull(sector.getOccupier());
 }

 @Test(expected = AssertionError.class)
 public void testFree_MoreCombatantsThanZero() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 sector.free();
 sector.free(); // This should cause an AssertionError
 }

 @Test
 public void testGainControl_SameSide() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 }

 @Test
 public void testGainControl_DifferentSide() {
 sector.gainControl(CombatantSide.DEFENDER);
 sector.gainControl(CombatantSide.ATTACKER);
 assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
 }

 @Test
 public void testIsOccupied_NoCombatants() {
 assertFalse(sector.isOccupied());
 }

 @Test
 public void testIsOccupied_OneCombatant() {
 sector.gainControl(CombatantSide.DEFENDER);
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testGetHeight() {
 assertEquals(5, sector.getHeight());
 }

 @Test
 public void testIsDefenderSpawn() {
 assertTrue(sector.isDefenderSpawn());
 }

 @Test
 public void testIsAttackerSpawn() {
 assertTrue(sector.isAttackerSpawn());
 }

 @Test
 public void testIsFlag() {
 assertFalse(sector.isFlag());
 }

 @Test
 public void testGetBonus_Archer() {
 SectorBonus bonus = sector.getBonus(CharacterClass.ARCHER);
 assertEquals(new SectorBonus(5, 3, 0), bonus);
 }

 @Test
 public void testGetBonus_Swordsman() {
 sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.SWORDSMAN);
 SectorBonus bonus = sector.getBonus(CharacterClass.ARCHER);
 assertEquals(new SectorBonus(5, 3, 0), bonus); // Bonus should not depend on character class of the sector
 }

 @Test
 public void testGetBonus_Mage() {
 sector = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.MAGE);
 SectorBonus bonus = sector.getBonus(CharacterClass.MAGE);
 assertEquals(new SectorBonus(5, 3, 0), bonus); // Bonus should not depend on character class of the sector
 }

 @Test
 public void testGetAttackBonus_Archer() {
 assertEquals(7, sector.getAttackBonus(CharacterClass.ARCHER));
 }

 @Test
 public void testGetDefenseBonus_Archer() {
 assertEquals(6, sector.getDefenseBonus(CharacterClass.ARCHER));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddArea_NullArea() {
 sector.addArea(null);
 }

 @Test
 public void testHashCodeAndEquals() {
 SimpleSector sector1 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
 SimpleSector sector2 = new SimpleSector(5, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
 SimpleSector sector3 = new SimpleSector(6, true, true, 2, 3, false, 4, CharacterClass.ARCHER);
 assertEquals(sector1, sector1);
 assertEquals(sector1, sector2);
 assertNotEquals(sector1, sector3);
 }

 @Test
 public void testObserver() {
 SectorObserver observer = mock(SectorObserver.class);
 sector.addObserver(observer);
 sector.occupierChanged();
 verify(observer).occupierChanged();
 }
}