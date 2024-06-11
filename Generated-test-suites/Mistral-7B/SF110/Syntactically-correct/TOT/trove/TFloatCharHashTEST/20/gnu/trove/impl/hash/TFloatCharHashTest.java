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
 assertEquals(0, hash.size());
 assertFalse(hash.contains(Float.NaN));
 }

 @Test
 public void testPutAndGet() {
 hash.put(1.0f, 'a');
 assertEquals(1, hash.size());
 assertTrue(hash.containsKey(1.0f));
 assertEquals('a', hash.get(1.0f));
 }

 @Test
 public void testPutDuplicateKey() {
 hash.put(1.0f, 'a');
 hash.put(1.0f, 'b');
 assertEquals(1, hash.size());
 assertEquals('a', hash.get(1.0f));
 }

 @Test(expected = IllegalStateException.class)
 public void testPutFullHash() {
 for (int i = 0; i < hash.capacity(); i++) {
 hash.put(i, (float) i);
 }
 }

 @Test(expected = IllegalStateException.class)
 public void testPutNullValue() {
 hash.put(1.0f, null);
 }

 @Test
 public void testRemove() {
 hash.put(1.0f, 'a');
 hash.remove(1.0f);
 assertEquals(0, hash.size());
 assertFalse(hash.containsKey(1.0f));
 }

 @Test
 public void testContainsNullKey() {
 assertFalse(hash.containsKey(null));
 }

 @Test
 public void testContainsNaNKey() {
 assertFalse(hash.containsKey(Float.NaN));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float key) {
 assertEquals(Float.intBitsToFloat(hashCode() & 0xFFFFFFFFL), key, 0.0f);
 return true;
 }
 };
 hash.put(1.0f, 'a');
 hash.put(2.0f, 'b');
 hash.forEach(procedure);
 assertEquals(2, hash.size());
 }

 @Test
 public void testWriteAndReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutput out = new ObjectOutputStream(baos);
 hash.writeExternal(out);
 ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
 ObjectInput in = new ObjectInput(bais);
 TFloatCharHash hashCopy = new TFloatCharHash();
 hashCopy.readExternal(in);
 assertEquals(hash, hashCopy);
 }
}