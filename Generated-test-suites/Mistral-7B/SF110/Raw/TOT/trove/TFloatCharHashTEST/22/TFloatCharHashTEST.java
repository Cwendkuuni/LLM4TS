import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(5);
 }

 @Test
 public void testInitialization() {
 assertEquals(5, hash.size());
 assertNull(hash.getNoEntryKey());
 assertEqualCharValue('\0', hash.getNoEntryValue());
 }

 @Test
 public void testContains() {
 // empty hash
 assertFalse(hash.contains(0.5f));
 // add elements
 hash.insert(0.5f, 'A');
 hash.insert(1.2f, 'B');
 assertTrue(hash.contains(0.5f));
 assertTrue(hash.contains(1.2f));
 }

 @Test
 public void testForEach() {
 // empty hash
 assertFalse(hashForEach(procedure -> false));
 // add elements
 hash.insert(0.5f, 'A');
 hash.insert(1.2f, 'B');
 assertTrue(hashForEach(procedure -> procedure.value == 'A' && procedure.key == 0.5f || procedure.value == 'B' && procedure.key == 1.2f));
 }

 @Test
 public void testRemove() {
 // add elements
 hash.insert(0.5f, 'A');
 hash.insert(1.2f, 'B');
 // remove by index
 hash.removeAt(0);
 // verify removal
 assertFalse(hash.contains(0.5f));
 assertNull(hash.get(0.5f));
 assertEquals(1, hash.size());
 }

 @Test
 public void testIndex() {
 // empty hash
 assertEqualIndex(-1, hash.index(0.5f));
 // add elements
 hash.insert(0.5f, 'A');
 hash.insert(1.2f, 'B');
 assertEqualIndex(0, hash.index(0.5f));
 assertEqualIndex(1, hash.index(1.2f));
 }

 @Test
 public void testInsertion() {
 // empty hash
 assertEqualIndex(-1, hash.insertKey(0.5f));
 // full hash
 hash.insert(0.5f, 'A');
 hash.insert(1.2f, 'B');
 hash.insert(2.5f, 'C');
 hash.insert(3.5f, 'D');
 hash.insert(4.5f, 'E');
 assertEqualIndex(5, hash.insertKey(5.5f));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertionFullHash() {
 // full hash
 hash.insert(0.5f, 'A');
 hash.insert(1.2f, 'B');
 hash.insert(2.5f, 'C');
 hash.insert(3.5f, 'D');
 hash.insert(4.5f, 'E');
 hash.insert(5.5f, 'F');
 }

 @Test
 public void testWriteAndReadExternal() throws IOException {
 ByteArrayOutputStream bos = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(bos);
 hash.writeExternal(out);
 TFloatCharHash hashFromByteArray = new TFloatCharHash();
 hashFromByteArray.readExternal(new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray())));
 assertEquals(hash.getNoEntryKey(), hashFromByteArray.getNoEntryKey());
 assertEquals(hash.getNoEntryValue(), hashFromByteArray.getNoEntryValue());
 }

 private boolean hashForEach(TFloatProcedure procedure) {
 return hash.forEach(procedure);
 }

 private char assertEqualCharValue(char expected, char actual) {
 assertEquals(expected, actual);
 return expected;
 }

}