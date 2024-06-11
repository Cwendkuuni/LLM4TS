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
 assertFalse(hash.contains(0.0f));
 assertFalse(hash.contains('A'));
 assertEquals(0, hash.size());
 }

 @Test
 public void testBasicOperations() {
 hash.put(1.0f, 'A');
 hash.put(2.0f, 'B');
 hash.put(3.0f, 'C');

 assertEquals(3, hash.size());
 assertTrue(hash.contains(1.0f));
 assertTrue(hash.contains(2.0f));
 assertTrue(hash.contains(3.0f));
 assertEquals('A', hash.get('A'));
 assertEquals('B', hash.get('B'));
 assertEquals('C', hash.get('C'));

 hash.remove('A');
 assertEquals(2, hash.size());
 assertFalse(hash.contains('A'));
 assertEquals('B', hash.get('B'));
 assertEquals('C', hash.get('C'));
 }

 @Test(expected = IllegalStateException.class)
 public void testHashFullException() {
 for (int i = 0; i < hash.capacity(); i++) {
 hash.put(i, (float) i);
 }
 }

 @Test
 public void testRehashing() {
 for (int i = 0; i < hash.capacity(); i++) {
 hash.put((float) i, (char) i);
 }

 assertEquals(hash.capacity(), hash.size());

 hash.put(10.0f, 'Z');

 assertEquals(hash.capacity() * 2, hash.size());
 }

 @Test
 public void testWriteReadExternal() throws IOException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 hash.writeExternal(oos);
 oos.close();

 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInputStream ois = new ObjectInputStream(bais);
 TFloatCharHash hashCopy = (TFloatCharHash) ois.readObject();
 ois.close();

 assertEquals(hash.size(), hashCopy.size());
 for (int i = 0; i < hash.size(); i++) {
 assertEquals(hash.get(i), hashCopy.get(i));
 }
 }
}