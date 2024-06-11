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
 public void testEmptyHash() {
 assertFalse(hash.contains(Float.NaN));
 assertFalse(hash.contains(1.0f));
 assertFalse(hash.contains(2.0f));
 assertFalse(hash.forEach(new TFloatProcedure() {
 @Override
 public boolean execute(float floatValue) {
 return false;
 }
 }));
 }

 @Test
 public void testAddAndGet() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 assertEquals(1.0f, hash.get(1.0f), 0.0);
 assertEquals(2.0f, hash.get(2.0f), 0.0);
 assertFalse(hash.containsKey(3.0f));
 }

 @Test
 public void testAddDuplicateKey() {
 hash.insertKey(1.0f);
 hash.insertKey(1.0f);
 assertEquals(1.0f, hash.get(1.0f), 0.0);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddKeyWhenFull() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float floatValue) {
 return false;
 }
 };
 for (int i = 0; i < hash.capacity(); i++) {
 hash.insertKey(Float.NaN);
 }
 hash.forEach(procedure);
 }

 @Test
 public void testRemoveKey() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.remove(1.0f);
 assertFalse(hash.containsKey(1.0f));
 assertEquals(2.0f, hash.get(2.0f), 0.0);
 }

 @Test
 public void testContainsKeyWithNaN() {
 hash.insertKey(Float.NaN);
 assertTrue(hash.containsKey(Float.NaN));
 }

 @Test
 public void testContainsKeyWithNegativeNumber() {
 hash.insertKey(-1.0f);
 assertTrue(hash.containsKey(-1.0f));
 }

 @Test
 public void testForEach() {
 hash.insertKey(1.0f);
 hash.insertKey(2.0f);
 hash.forEach(new TFloatProcedure() {
 @Override
 public boolean execute(float floatValue) {
 assertEquals(floatValue, hash.get(floatValue), 0.0);
 return true;
 }
 });
 }

 @Test
 public void testWriteAndReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 TFloatDoubleHash hashToWrite = new TFloatDoubleHash(5, 0.5f, Float.NaN, Double.NaN);
 hashToWrite.insertKey(1.0f);
 hashToWrite.insertKey(2.0f);
 hashToWrite.writeExternal(objectOutputStream);
 objectOutputStream.close();
 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 ObjectInput objectInput = new ObjectInput(byteArrayInputStream);
 TFloatDoubleHash hashRead = (TFloatDoubleHash) objectInput.readObject();
 objectInput.close();
 assertEquals(1.0f, hashRead.get(1.0f), 0.0);
 assertEquals(2.0f, hashRead.get(2.0f), 0.0);
 }
}