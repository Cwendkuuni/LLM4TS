import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.procedure.TFloatProcedure;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectOutput;

public class TFloatCharHashTest {

 private TFloatCharHash hash;

 @Before
 public void setUp() {
 hash = new TFloatCharHash(5);
 }

 @Test
 public void testConstructor() {
 assertTrue(hash instanceof TFloatCharHash);
 assertNotNull(hash._set);
 assertEquals(5, hash._set.length);
 }

 @Test
 public void testGetNoEntryKey() {
 float noEntryKey = hash.getNoEntryKey();
 assertEquals(0.0f, noEntryKey, 0.0f);
 }

 @Test
 public void testGetNoEntryValue() {
 char noEntryValue = hash.getNoEntryValue();
 assertEquals('\0', noEntryValue);
 }

 @Test
 public void testSetUp() {
 assertEquals(5, hash.setUp(5));
 }

 @Test
 public void testContains() {
 assertTrue(hash.contains(Float.NaN));
 assertFalse(hash.contains(Float.NEGATIVE_INFINITY));
 hash.insert(Float.NEGATIVE_INFINITY);
 assertTrue(hash.contains(Float.NEGATIVE_INFINITY));
 hash.remove(Float.NEGATIVE_INFINITY);
 assertFalse(hash.contains(Float.NEGATIVE_INFINITY));
 }

 @Test
 public void testForEach() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 return value >= 0.0f;
 }
 };
 hash.insert(0.0f);
 hash.insert(Float.NaN);
 hash.insert(-1.0f);
 hash.forEach(procedure);
 assertTrue(procedure.wasCalledWith(0.0f));
 assertFalse(procedure.wasCalledWith(Float.NaN));
 assertFalse(procedure.wasCalledWith(-1.0f));
 }

 @Test
 public void testRemoveAt() {
 hash.insert(0.0f);
 hash.insert(Float.NaN);
 hash.insert(-1.0f);
 hash.removeAt(1);
 assertFalse(hash.contains(Float.NaN));
 }

 @Test
 public void testIndex() {
 hash.insert(0.0f);
 hash.insert(Float.NaN);
 hash.insert(-1.0f);
 hash.insert(Float.POSITIVE_INFINITY);
 hash.insert(1.0f);
 int index = hash.index(0.0f);
 assertEquals(0, index);
 index = hash.index(Float.POSITIVE_INFINITY);
 assertEquals(4, index);
 }

 @Test
 public void testInsertKey() {
 hash.insert(0.0f);
 hash.insert(Float.NaN);
 hash.insert(-1.0f);
 int index = hash.insertKey(1.0f);
 assertEquals(5, hash._set.length);
 assertEquals(1, index);
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyFull() {
 hash.insert(0.0f);
 hash.insert(Float.NaN);
 hash.insert(-1.0f);
 hash.insert(Float.POSITIVE_INFINITY);
 hash.insert(1.0f);
 hash.insert(Float.NEGATIVE_INFINITY);
 }

 @Test
 public void testWriteExternalAndReadExternal() throws IOException {
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutput objectOutput = new java.io.ObjectOutputStream(byteArrayOutputStream);
 hash.writeExternal(objectOutput);
 byte[] bytes = byteArrayOutputStream.toByteArray();
 ObjectInput objectInput = new ByteArrayInputStream(bytes);
 TFloatCharHash hash2 = new TFloatCharHash();
 hash2.readExternal(objectInput);
 assertEquals(hash.size(), hash2.size());
 }
}