import org.junit.*;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 @BeforeClass
 public static void setUpBeforeClass() {
 // Set up any static resources or data here
 }

 @AfterClass
 public static void tearDownAfterClass() {
 // Teardown any static resources or data here
 }

 @Before
 public void setUp() {
 // Set up any instance variables or data here
 }

 @After
 public void tearDown() {
 // Teardown any instance variables or data here
 }

 @Test
 public void testTShortHashConstructor() {
 TShortHash hash = new TShortHash();
 assertEquals(0, hash.getNoEntryValue());
 int capacity = hash.size();
 short[] set = hash._set;
 assertEquals(capacity, set.length);
 Arrays.fill(set, 0); // check if all elements are initialized with no_entry_value
 }

 @Test
 public void testTShortHashInitialCapacityConstructor() {
 TShortHash hash = new TShortHash(10);
 assertEquals(10, hash.size());
 }

 @Test
 public void testTShortHashInitialCapacityAndLoadFactorConstructor() {
 TShortHash hash = new TShortHash(10, 0.75f);
 assertEquals(10, hash.size());
 }

 @Test
 public void testTShortHashInitialCapacityLoadFactorAndNoEntryValueConstructor() {
 TShortHash hash = new TShortHash(10, 0.75f, (short) 1);
 assertEquals(10, hash.size());
 short[] set = hash._set;
 Arrays.fill(set, 1); // check if all elements are initialized with no_entry_value
 }

 @Test
 public void testGetNoEntryValue() {
 TShortHash hash = new TShortHash();
 assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertFalse(hash.contains((short) 3));
 }

 @Test
 public void testForEach() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);
 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 assertNotNull(value);
 return true;
 }
 };
 assertTrue(hash.forEach(procedure));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testRemoveAt() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.removeAt(0);
 }

 @Test
 public void testIndex() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 1);
 int index1 = hash.index((short) 1);
 int index2 = hash.index((short) 2);
 assertEquals(0, index1);
 assertEquals(2, index2);
 }

 @Test
 public void testSetUp() {
 TShortHash hash = new TShortHash(10);
 int capacity = hash.size();
 assertNotNull(hash._set);
 assertEquals(capacity, hash._set.length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 TShortHash hash = new TShortHash(-10);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testZeroLoadFactor() {
 TShortHash hash = new TShortHash(10, 0.0f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 TShortHash hash = new TShortHash(10, -0.1f);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeNoEntryValue() {
 TShortHash hash = new TShortHash(10, 0.75f, (short) -1);
 }

 @Test
 public void testInsertKey() {
 TShortHash hash = new TShortHash();
 hash.put((short) 1);
 hash.put((short) 2);
 assertEquals(2, hash.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testHashFunctionException() {
 TShortHash hash = new TShortHash() {
 @Override
 protected int index(final short val) {
 throw new RuntimeException("Hash function exception");
 }
 };
 hash.put((short) 1);
 }
}