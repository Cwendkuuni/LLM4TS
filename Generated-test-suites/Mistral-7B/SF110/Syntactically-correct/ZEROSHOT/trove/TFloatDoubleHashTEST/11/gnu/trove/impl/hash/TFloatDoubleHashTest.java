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
 assertEquals(5, hash.capacity());
 assertNull(hash.getNoEntryKey());
 assertEquals(0.0, hash.getNoEntryValue(), 0.0);
 }

 @Test
 public void testContains() {
 assertFalse(hash.contains(1.0f));
 hash.put(1.0f, 1.0);
 assertTrue(hash.contains(1.0f));
 }

 @Test
 public void testForEach() {
 int count = 0;
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 count++;
 return true;
 }
 };
 hash.forEach(procedure);
 assertEquals(0, hash.size());
 assertEquals(5, count);
 }

 @Test
 public void testRemoveAt() {
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 int index = hash.index(2.0f);
 hash.removeAt(index);
 assertEquals(2, hash.size());
 assertFalse(hash.contains(2.0f));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testIndexOutOfBounds() {
 int index = hash.index(-1.0f);
 fail("Expected IndexOutOfBoundsException");
 }

 @Test
 public void testInsertKey() {
 hash.put(1.0f, 1.0);
 hash.put(2.0f, 2.0);
 hash.put(3.0f, 3.0);
 hash.put(4.0f, 4.0);
 assertEquals(4, hash.size());
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 assertTrue(hash.contains(4.0f));
 }

 @Test(expected = IllegalStateException.class)
 public void testNoFreeOrRemovedSlots() {
 for (int i = 0; i < hash.capacity(); i++) {
 hash.put(i, i);
 }
 fail("Expected IllegalStateException");
 }

 @Test
 public void testWriteReadExternal() throws IOException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 hash.writeExternal(out);
 byte[] bytes = byteStream.toByteArray();
 ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
 TFloatDoubleHash hashCopy = (TFloatDoubleHash) in.readObject();
 assertEquals(hash.capacity(), hashCopy.capacity());
 assertEquals(hash.size(), hashCopy.size());
 for (float key : hash.keySet()) {
 assertEquals(hash.get(key), hashCopy.get(key));
 }
 }
}