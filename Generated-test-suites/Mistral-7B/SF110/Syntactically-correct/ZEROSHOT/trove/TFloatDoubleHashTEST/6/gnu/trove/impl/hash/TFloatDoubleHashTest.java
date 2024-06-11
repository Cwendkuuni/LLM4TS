import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;

 @Before
 public void setUp() {
 hash = new TFloatDoubleHash(5);
 }

 @Test
 public void testConstructor() {
 assertEquals(5, hash.size());
 assertNull(hash.getNoEntryValue());
 assertEquals(0.0f, hash.getNoEntryKey(), 0.01f);
 }

 @Test
 public void testContains() {
 assertFalse(hash.contains(1.0f));
 hash.put(1.0f, 1.0);
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, 1.0f, 0.01f);
 return true;
 }
 };
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.forEach(procedure);
 }

 @Test
 public void testRemove() {
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.remove(1.0f);
 assertFalse(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 }

 @Test
 public void testIndex() {
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 assertEquals(1, hash.index(1.0f));
 assertEquals(-1, hash.index(3.0f));
 }

 @Test
 public void testInsertKey() {
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 assertEquals(3, hash.size());
 assertFalse(hash.containsKey(4.0f));
 int index = hash.insertKey(4.0f);
 assertTrue(hash.containsKey(4.0f));
 assertEquals(4, hash.size());
 assertEquals(index, hash.indexOfKey(4.0f));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFull() {
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);
 hash.put(5.0f, 5.0);
 hash.put(6.0f, 6.0); // should throw an IllegalStateException
 }

 @Test
 public void testWriteReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 hash.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
 TFloatDoubleHash hash2 = (TFloatDoubleHash) in.readObject();
 assertEquals(5, hash2.size());
 assertFalse(hash2.containsKey(1.0f));
 hash2.put(1.0f, 1.0);
 assertTrue(hash2.containsKey(1.0f));
 }
}