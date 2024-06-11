package gnu.trove.impl.hash; // Make sure the test class is in the same package as the tested class

import org.junit.*;
import static org.junit.Assert.*;
import gnu.trove.procedure.TShortProcedure;
import gnu.trove.TShortHash;
import gnu.trove.impl.hash.TShortHashConstants;

public class TShortHashTest {

 @BeforeClass
 public static void setUpBeforeClass() {
 System.out.println("Setting up before class");
 }

 @AfterClass
 public static void tearDownAfterClass() {
 System.out.println("Tearing down after class");
 }

 @Before
 public void setUp() {
 System.out.println("Setting up test");
 }

 @After
 public void tearDown() {
 System.out.println("Tearing down test");
 }

 @Test
 public void testEmptyTShortHash() {
 TShortHash hash = new TShortHash();
 assertEquals(0, hash.size());
 assertFalse(hash.contains((short) 1));
 }

 @Test
 public void testInitialCapacityTShortHash() {
 TShortHash hash = new TShortHash(10);
 assertEquals(10, hash.size());
 for (short i = 0; i < 10; i++) {
 hash.put((short) i, (short) i);
 assertEquals((short) i, hash.get((short) i));
 }
 }

 @Test
 public void testLoadFactorTShortHash() {
 TShortHash hash = new TShortHash(5, 0.75f);
 assertEquals(5, hash.size());
 for (short i = 0; i < 7; i++) {
 hash.put((short) i, (short) i);
 }
 assertEquals(7, hash.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 TShortHash hash = new TShortHash(-1);
 fail("Should throw IllegalArgumentException with negative initial capacity");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 TShortHash hash = new TShortHash(10, -0.5f);
 fail("Should throw IllegalArgumentException with negative load factor");
 }

 @Test
 public void testPutGetRemove() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 assertEquals((short) 1, hash.get((short) 1));
 hash.remove((short) 1);
 assertFalse(hash.contains((short) 1));
 }

 @Test
 public void testHashCollision() {
 TShortHash hash = new TShortHash();
 for (short i = 0; i < 100; i++) {
 hash.put((short) i, (short) i);
 }
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash(10);
 for (short i = 0; i < 10; i++) {
 hash.put((short) i, (short) i);
 }
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short val) {
 assertEquals((short) val, hash.get((short) val));
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testForEachConcurrentModification() {
 TShortHash hash = new TShortHash(10);
 for (short i = 0; i < 10; i++) {
 hash.put((short) i, (short) i);
 }
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short val) {
 hash.remove((short) val);
 return true;
 }
 };
 hash.forEach(procedure);
 }

 @Test
 public void testNoEntryValue() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1, (short) 1);
 assertEquals(TShortHashConstants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testCustomNoEntryValue() {
 TShortHash hash = new TShortHash(10, TShortHashConstants.DEFAULT_SHORT_NO_ENTRY_VALUE, (short) 42);
 assertEquals((short) 42, hash.getNoEntryValue());
 }

}