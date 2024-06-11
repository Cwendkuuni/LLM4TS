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
 public void testEmptyHash() {
 assertTrue(hash.isEmpty());
 assertFalse(hash.contains(0.0f));
 assertNull(hash.getNoEntryValue());
 assertEquals(0.0f, hash.getNoEntryKey());
 }

 @Test
 public void testBasicOperations() {
 // Add elements
 hash.insert(0.5f, 'A');
 hash.insert(1.0f, 'B');
 hash.insert(1.5f, 'C');

 // Check size and contains
 assertEquals(3, hash.size());
 assertTrue(hash.contains(0.5f));
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(1.5f));

 // Remove an element
 hash.remove(1.0f);

 // Check size and contains
 assertEquals(2, hash.size());
 assertFalse(hash.contains(1.0f));

 // For-each operation
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, 0.5f, 0.01f);
 return true;
 }
 };
 hash.forEach(procedure);
 }

 @Test
 public void testSerialization() {
 byte[] bytes = null;
 try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 out.writeObject(hash);
 bytes = byteStream.toByteArray();
 }
 TFloatCharHash deserializedHash = null;
 try (ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes)) {
 ObjectInputStream in = new ObjectInputStream(byteStream);
 deserializedHash = (TFloatCharHash) in.readObject();
 }
 assertTrue(deserializedHash != null);
 assertEquals(3, deserializedHash.size());
 assertTrue(deserializedHash.contains(0.5f));
 assertTrue(deserializedHash.contains(1.5f));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertDuplicateKey() {
 hash.insert(1.5f, 'C'); // already exists
 hash.insert(1.5f, 'D'); // exception expected
 }

 @Test(expected = IllegalStateException.class)
 public void testFullHash() {
 // Fill the hash with elements and check for exceptions when trying to add more
 for (int i = 0; i < TFloatCharHash.DEFAULT_INITIAL_CAPACITY; i++) {
 hash.insert(i, (char) i);
 }
 hash.insert(TFloatCharHash.DEFAULT_INITIAL_CAPACITY, (char) TFloatCharHash.DEFAULT_INITIAL_CAPACITY);
 }
}