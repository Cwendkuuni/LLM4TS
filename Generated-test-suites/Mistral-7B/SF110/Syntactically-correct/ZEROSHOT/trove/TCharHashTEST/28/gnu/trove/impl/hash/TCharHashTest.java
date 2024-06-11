import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TCharHash;
import gnu.trove.procedure.TCharProcedure;

public class TCharHashTest {

 private TCharHash charHash;

 @Before
 public void setUp() {
 charHash = new TCharHash(5);
 }

 @Test
 public void testConstructor() {
 assertEquals("Initial capacity should be set correctly", 5, charHash.size());
 assertNull("No entry value should be null by default", charHash.getNoEntryValue());
 assertArrayEquals("Hash table should be empty", new char[5], charHash._set);
 }

 @Test
 public void testSetNoEntryValue() {
 charHash = new TCharHash(5, 'X');
 assertEquals("No entry value should be set correctly", 'X', charHash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 charHash.put('A', 'A');
 assertTrue("Should contain 'A'", charHash.contains('A'));
 }

 @Test
 public void testDoesNotContain() {
 assertFalse("Should not contain 'B'", charHash.contains('B'));
 }

 @Test
 public void testPutAndGet() {
 charHash.put('A', 'A');
 assertEquals("'A' should be mapped to 'A'", 'A', charHash.get('A'));
 }

 @Test(expected = IllegalStateException.class)
 public void testPutWhenFull() {
 for (int i = 0; i < charHash.size(); i++) {
 charHash.put((char) i, (char) i);
 }
 }

 @Test
 public void testForEach() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 TCharProcedure procedure = new TCharProcedure() {
 @Override
 public boolean execute(char value) {
 assertEquals("Value should be correct", value, charHash.get(value));
 return true;
 }
 };
 charHash.forEach(procedure);
 }

 @Test
 public void testRemove() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.remove('B');
 assertFalse("'B' should not be present anymore", charHash.contains('B'));
 }

 @Test
 public void testRemoveNonExistentValue() {
 charHash.put('A', 'A');
 charHash.remove('Z');
 assertTrue("'Z' should not have been present", charHash.isEmpty());
 }

 @Test
 public void testRehash() {
 charHash.put('A', 'A');
 charHash.put('B', 'B');
 charHash.put('C', 'C');
 charHash.put('D', 'D');
 charHash.put('E', 'E');
 charHash.put('F', 'F');
 charHash.put('G', 'G');
 charHash.put('H', 'H');
 charHash.put('I', 'I');
 charHash.put('J', 'J');
 charHash.put('K', 'K');
 charHash.put('L', 'L');
 charHash.put('M', 'M');
 charHash.put('N', 'N');
 charHash.put('O', 'O');
 charHash.put('P', 'P');
 charHash.put('Q', 'Q');
 charHash.put('R', 'R');
 charHash.put('S', 'S');
 charHash.put('T', 'T');
 charHash.put('U', 'U');
 charHash.put('V', 'V');
 charHash.put('W', 'W');
 charHash.put('X', 'X');
 charHash.put('Y', 'Y');
 charHash.put('Z', 'Z');
 assertTrue("Hash table should have been rehashed", charHash._set.length > 16);
 }

 @After
 public void tearDown() {
 charHash = null;
 }
}