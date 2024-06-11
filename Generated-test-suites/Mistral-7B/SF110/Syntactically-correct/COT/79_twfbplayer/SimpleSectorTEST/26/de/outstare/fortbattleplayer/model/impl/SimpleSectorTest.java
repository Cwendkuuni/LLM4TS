import org.junit.Test;
import org.junit.Assert.*;
import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.*;

public class SimpleSectorTest {

 private SimpleSector sector;

 @Before
 public void setUp() {
 sector = new SimpleSector(5, true, true, 5, 5, false, 10, CharacterClass.ARCHER);
 }

 @Test
 public void testConstructor() {
 // Test constructor with valid arguments
 assertNotNull(sector);
 assertEquals(5, sector.getHeight());
 assertTrue(sector.isDefenderSpawn());
 assertTrue(sector.isAttackerSpawn());
 assertEquals(10, sector.getClassBonus());
 assertEquals(CharacterClass.ARCHER, sector.getClassType());
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 assertEmpty(sector.getAreas());
 assertEmpty(sector.getObservers());
 }

 @Test
 public void testFree() {
 // Test free method with combatant already freed
 sector.free();
 sector.free();
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());

 // Test free method with combatant still present
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());
 }

 @Test(expected = AssertionError.class)
 public void testFreeWithNegativeOccupierCount() {
 sector.free();
 sector.free();
 sector.free();
 sector.free();
 sector.free(); // should throw AssertionError
 }

 @Test
 public void testOccupierChanged() {
 // Test occupierChanged method with no observer
 sector = new SimpleSector(5, true, true, 5, 5, false, 10, CharacterClass.ARCHER);
 sector.addObserver(mock(SectorObserver.class));
 sector.gainControl(CombatantSide.DEFENDER);
 sector.free();
 verify(mock(SectorObserver.class)).occupierChanged();
 }

 @Test
 public void testGainControl() {
 // Test gainControl method with null side
 sector.gainControl(null);
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());

 // Test gainControl method with valid side
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 assertTrue(sector.isOccupied());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGainControlWithInvalidSide() {
 sector.gainControl(CombatantSide.values()[3]);
 }

 @Test
 public void testGetOccupier() {
 // Test getOccupier method with no occupier
 assertNull(sector.getOccupier());
 assertFalse(sector.isOccupied());

 // Test getOccupier method with occupier present
 sector.gainControl(CombatantSide.DEFENDER);
 assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
 assertTrue(sector.isOccupied());
 }

 @Test
 public void testIsOccupied() {
 // Test isOccupied method with no occupier
 assertFalse(sector.isOccupied());

 // Test isOccupied method with occupier present
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
 public void testGetBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 SectorBonus expectedBonus = new SectorBonus(5, 5, 0);
 SectorBonus actualBonus = sector.getBonus(charClass);
 assertEquals(expectedBonus.getAttackBonus(), actualBonus.getAttackBonus());
 assertEquals(expectedBonus.getDefenseBonus(), actualBonus.getDefenseBonus());
 assertEquals(expectedBonus.getDamageBonus(), actualBonus.getDamageBonus());
 }

 @Test
 public void testGetDamageBonus() {
 assertEquals(15, sector.getDamageBonus());
 }

 @Test
 public void testGetAttackBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 int expectedBonus = 10 + 15;
 int actualBonus = sector.getAttackBonus(charClass);
 assertEquals(expectedBonus, actualBonus);
 }

 @Test
 public void testGetDefenseBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 int expectedBonus = 5 + 15;
 int actualBonus = sector.getDefenseBonus(charClass);
 assertEquals(expectedBonus, actualBonus);
 }

 @Test
 public void testClassBonus() {
 CharacterClass charClass = CharacterClass.ARCHER;
 int expectedBonus = 10;
 int actualBonus = sector.classBonus(charClass);
 assertEquals(expectedBonus, actualBonus);
 }

 @Test
 public void testGetAreas() {
 // Test getAreas method with empty areas set
 assertEmpty(sector.getAreas());

 // Test getAreas method with areas present
 sector._addArea(mock(Area.class));
 verify(mock(Area.class)).addObserver(mock(SectorObserver.class));
 assertEquals(1, sector.getAreas().size());
 }

 @Test
 public void testAddArea() {
 Area area = mock(Area.class);
 sector._addArea(area);
 verify(area).addObserver(mock(SectorObserver.class));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddAreaWithNullArea() {
 sector._addArea(null);
 }

 @Test
 public void testHashCode() {
 SimpleSector other = new SimpleSector(5, true, true, 5, 5, false, 10, CharacterClass.ARCHER);
 int expectedHashCode = 246340483;
 int actualHashCode = sector.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 assertEquals(expectedHashCode, other.hashCode());
 }

 @Test
 public void testEquals() {
 SimpleSector other = new SimpleSector(5, true, true, 5, 5, false, 10, CharacterClass.ARCHER);
 assertTrue(sector.equals(other));
 }

 @Test
 public void testEqualsWithDifferentClasses() {
 SimpleSector other = new SimpleSector(5, true, true, 5, 5, false, 10, CharacterClass.ARCHER);
 SimpleSector otherWithDifferentClass = new SimpleSector(5, true, true, 5, 5, false, 10, CharacterClass.MAGE);
 assertFalse(sector.equals(otherWithDifferentClass));
 }

 @Test
 public void testEqualsWithDifferentOccupiers() {
 SimpleSector other = new SimpleSector(5, true, true, 5, 5, false, 10, CharacterClass.ARCHER);
 other.gainControl(CombatantSide.ATTACKER);
 assertFalse(sector.equals(other));
 }

 @Test
 public void testEqualsWithDifferentAreas() {
 SimpleSector other = new SimpleSector(5, true, true, 5, 5, false, 10, CharacterClass.ARCHER);
 other._addArea(mock(Area.class));
 assertFalse(sector.equals(other));
 }

 @Test
 public void testAddObserver() {
 SectorObserver observer = mock(SectorObserver.class);
 sector.addObserver(observer);
 verify(observer).occupierChanged();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddObserverWithNullObserver() {
 sector.addObserver(null);
 }

 @Test
 public void testRemoveObserver() {
 SectorObserver observer = mock(SectorObserver.class);
 sector.addObserver(observer);
 sector.removeObserver(observer);
 verifyNoMoreInteractions(observer);
 }
}