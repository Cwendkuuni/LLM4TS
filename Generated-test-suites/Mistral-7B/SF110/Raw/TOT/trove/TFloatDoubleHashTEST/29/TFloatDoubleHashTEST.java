import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash<Float, Double> hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatDoubleHash<>(5, 0.5f);
 }

 @Test
 public void testConstructor() {
 assertEquals(5, hashMap.size());
 assertNull(hashMap.getNoEntryKey());
 assertNull(hashMap.getNoEntryValue());
 }

 @Test
 public void testGetNoEntryKey() {
 hashMap.put(0.0f, 0.0);
 assertEquals(0.0f, hashMap.getNoEntryKey());
 }

 @Test
 public void testGetNoEntryValue() {
 hashMap.put(0.0f, 0.0);
 assertEquals(0.0, hashMap.getNoEntryValue());
 }

 @Test
 public void testSetUp() {
 hashMap = new TFloatDoubleHash<>(10, 0.5f);
 assertEquals(10, hashMap.size());
 }

 @Test
 public void testContains() {
 hashMap.put(1.0f, 1.0);
 assertTrue(hashMap.contains(1.0f));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float element) {
 assertEquals(element, 1.0f);
 return true;
 }
 };
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.forEach(procedure);
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveAtIndexOutOfBounds() {
 hashMap.removeAt(-1);
 }

 @Test
 public void testIndex() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 assertEquals(1, hashMap.index(1.0f));
 assertEquals(-1, hashMap.index(3.0f));
 }

 @Test
 public void testInsertKey() {
 hashMap.put(1.0f, 1.0);
 hashMap.put(2.0f, 2.0);
 hashMap.put(3.0f, 3.0);
 assertEquals(3, hashMap.size());
 assertEquals(3.0f, hashMap.get(3.0f));
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 hashMap.writeExternal(objectOutputStream);
 ObjectInput objectInput = new ObjectInput(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
 TFloatDoubleHash<Float, Double> hashMapClone = (TFloatDoubleHash<Float, Double>) objectInput.readObject();
 assertEquals(hashMap, hashMapClone);
 }
}