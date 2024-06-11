import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;

public class TShortHashTest {

 private TShortHash hash;

 @Before
 public void setUp() {
 hash = new TShortHash(5);
 }

 @Test
 public void testEmptyHash() {
 assertTrue(hash.isEmpty());
 assertFalse(hash.contains((short) 1));
 }

 @Test
 public void testBasicFunctionality() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);

 assertTrue(hash.contains((short) 1));
 assertTrue(hash.contains((short) 2));
 assertTrue(hash.contains((short) 3));

 assertEquals(3, hash.size());
 }

 @Test
 public void testPutAndGetWithLoadFactor() {
 hash = new TShortHash(5, 0.75f);

 for (short i = 0; i < 7; i++) {
 hash.put((short) i);
 }

 assertEquals(7, hash.size());

 for (short i = 0; i < 7; i++) {
 assertEquals((short) i, hash.get((short) i));
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 try {
 hash = new TShortHash(-5);
 } catch (IllegalArgumentException e) {
 fail("Expected an IllegalArgumentException, but got: " + e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeLoadFactor() {
 try {
 hash = new TShortHash(5, -0.5f);
 } catch (IllegalArgumentException e) {
 fail("Expected an IllegalArgumentException, but got: " + e.getMessage());
 }
 }

 @Test
 public void testPutAndGetWithSameKey() {
 hash.put((short) 1);
 hash.put((short) 1);

 assertEquals((short) 1, hash.get((short) 1));
 }

 @Test
 public void testRemoveKey() {
 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);

 hash.remove((short) 2);

 assertFalse(hash.contains((short) 2));
 assertEquals(2, hash.size());
 }

 @Test
 public void testProcedureCall() {
 final short[] result = new short[3];

 hash.put((short) 1);
 hash.put((short) 2);
 hash.put((short) 3);

 TShortProcedure procedure = new TShortProcedure() {
 @Override
 public boolean execute(short value) {
 result[0] = value;
 return true;
 }
 };

 hash.forEach(procedure);

 assertEquals((short) 1, result[0]);
 assertEquals((short) 2, result[1]);
 assertEquals((short) 3, result[2]);
 }

 @Test
 public void testEdgeCases() {
 // Test with initial capacity of 1
 hash = new TShortHash(1);
 hash.put((short) 1);
 // Should not be able to add another element
 try {
 hash.put((short) 2);
 fail();
 } catch (IllegalStateException e) {
 // Expected
 }

 // Test with initial capacity of 0
 hash = new TShortHash(0);
 // Should not be able to add an element
 try {
 hash.put((short) 1);
 fail();
 } catch (IllegalArgumentException e) {
 // Expected
 }

 // Test with initial capacity of MAX_VALUE
 hash = new TShortHash(Integer.MAX_VALUE);
 for (short i = 0; i < Integer.MAX_VALUE; i++) {
 hash.put((short) i);
 }
 // Should not be able to add another element
 try {
 hash.put((short) Integer.MAX_VALUE);
 fail();
 } catch (IllegalStateException e) {
 // Expected
 }
}

 @Test
 public void testHashFunction() {
 assertEquals(31, HashFunctions.hash((short) 1));
 assertEquals(11, HashFunctions.hash((short) 127));
 }
}