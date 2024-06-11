import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hash;

 @Before
 public void setUp() {
 hash = new TFloatDoubleHash(5);
 }

 @Test
 public void testEmptyHash() {
 assertFalse(hash.contains(0.1f));
 assertFalse(hash.contains(0.2f));
 assertFalse(hash.contains(0.3f));
 assertFalse(hash.contains(0.4f));
 assertFalse(hash.contains(0.5f));
 }

 @Test
 public void testAddAndGet() {
 hash.insertKey(0.1f);
 assertEquals(0.1f, hash.get(0.1f), 0.0);
 assertTrue(hash.contains(0.1f));
 }

 @Test
 public void testAddAndGetSameKey() {
 hash.insertKey(0.1f);
 hash.insertKey(0.1f);
 assertEquals(0.1f, hash.get(0.1f), 0.0);
 assertTrue(hash.contains(0.1f));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddWhenFull() {
 for (int i = 0; i < hash.size(); i++) {
 hash.insertKey(i * 0.1f);
 }
 hash.insertKey(5.1f);
 }

 @Test
 public void testRemove() {
 hash.insertKey(0.1f);
 hash.remove(0.1f);
 assertFalse(hash.contains(0.1f));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullKey() {
 hash.insertKey(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNegativeKey() {
 hash.insertKey(-1.0f);
 }

 @Test
 public void testContainsEdgeCases() {
 float minValue = Float.MIN_VALUE;
 float maxValue = Float.MAX_VALUE;
 assertFalse(hash.contains(minValue));
 assertFalse(hash.contains(maxValue));
 }

 @Test
 public void testForEach() {
 float[] values = new float[5];
 for (int i = 0; i < 5; i++) {
 hash.insertKey(i * 0.1f);
 values[i] = i * 0.1f;
 }
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 values[Integer.parseInt(String.valueOf((int) (value * 10))) - 1] += 0.1f;
 return true;
 }
 };
 hash.forEach(procedure);
 assertArrayEquals(new float[]{0.4f, 0.5f, 0.6f, 0.7f, 0.8f}, values, 0.01f);
 }

 @Test
 public void testWriteAndReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream objStream = new ObjectOutputStream(byteStream);
 hash.writeExternal(objStream);
 hash = (TFloatDoubleHash) new ObjectInputStream(new ByteArrayInputStream(byteStream.toByteArray())).readObject();
 hash.insertKey(0.1f);
 assertEquals(0.1f, hash.get(0.1f), 0.0);
 }
}