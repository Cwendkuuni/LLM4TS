import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(5);
 }

 @Test
 public void testEmptyHash() {
 assertFalse(hash.contains(Float.NaN));
 assertFalse(hash.contains(1.0f));
 assertFalse(hash.contains('a'));
 assertEquals(0, hash.size());
 }

 @Test
 public void testBasicOperations() {
 hash.put(Float.NaN, 'a');
 hash.put(1.0f, 'b');
 hash.put(2.5f, 'c');

 assertTrue(hash.contains(Float.NaN));
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.5f));

 assertEquals('a', hash.get(Float.NaN));
 assertEquals('b', hash.get(1.0f));
 assertEquals('c', hash.get(2.5f));

 assertEquals(3, hash.size());

 hash.remove(1.0f);

 assertFalse(hash.contains(1.0f));
 assertEquals(2, hash.size());

 hash.clear();

 assertFalse(hash.contains(Float.NaN));
 assertFalse(hash.contains(1.0f));
 assertFalse(hash.contains(2.5f));
 assertEquals(0, hash.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullKey() {
 hash.put(null, 'a');
 }

 @Test(expected = IllegalArgumentException.class)
 public void testPutNullValue() {
 hash.put(1.0f, null);
 }

 @Test
 public void testWriteRead() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(baos);
 hash.writeExternal(out);
 ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 TFloatCharHash hashCopy = (TFloatCharHash) in.readObject();

 assertEquals(hash.size(), hashCopy.size());
 for (float key : hash.keySet()) {
 assertEquals(hash.get(key), hashCopy.get(key));
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testIteration() {
 for (Entry<Float, Character> entry : hash.entrySet()) {
 fail();
 }
 }

}