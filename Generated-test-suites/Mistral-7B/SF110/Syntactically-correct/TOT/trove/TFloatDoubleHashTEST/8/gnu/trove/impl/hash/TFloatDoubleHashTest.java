import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash floatHash;

 @Before
 public void setUp() {
 floatHash = new TFloatDoubleHash(5);
 }

 @Test
 public void testContainsEmptyHash() {
 assertFalse(floatHash.contains(0.5f));
 }

 @Test
 public void testContainsKeyExist() {
 floatHash.put(0.5f, 0.5);
 assertTrue(floatHash.contains(0.5f));
 }

 @Test
 public void testContainsKeyNotExist() {
 floatHash.put(1.0f, 1.0);
 assertFalse(floatHash.contains(0.5f));
 }

 @Test
 public void testForEach() {
 final int expectedSize = 3;
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float floatValue) {
 count++;
 return true;
 }
 };
 floatHash.put(1.0f, 1.0);
 floatHash.put(2.0f, 2.0);
 floatHash.put(3.0f, 3.0);
 floatHash.forEach(procedure);
 int count = 0;
 assertEquals(expectedSize, count);
 }

 @Test
 public void testRemoveKey() {
 floatHash.put(1.0f, 1.0);
 floatHash.put(2.0f, 2.0);
 floatHash.put(3.0f, 3.0);
 int initialSize = floatHash.size();
 floatHash.remove(2.0f);
 int finalSize = floatHash.size();
 assertEquals(initialSize - 1, finalSize);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFullHash() {
 for (int i = 0; i < 5; i++) {
 floatHash.put((float) i, i);
 }
 floatHash.put(5.5f, 5.5);
 }

 @Test
 public void testWriteReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
 floatHash.writeExternal(objectOutput);
 ObjectInput objectInput = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
 TFloatDoubleHash readFloatHash = new TFloatDoubleHash();
 readFloatHash.readExternal(objectInput);
 assertArrayEquals(floatHash._set, readFloatHash._set);
 assertEquals(floatHash.size(), readFloatHash.size());
 }
}