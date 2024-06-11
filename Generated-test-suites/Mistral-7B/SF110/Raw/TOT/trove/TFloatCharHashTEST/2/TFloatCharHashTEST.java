import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.impl.hash.TFloatCharHash;
import gnu.trove.impl.hash.TFloatProcedure;
import gnu.trove.procedure.TFloatProcedureAdapter;

public class TFloatCharHashTest {

 private TFloatCharHash hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatCharHash(5, 0.75f);
 }

 @Test
 public void testContains() {
 // Test empty hash map
 assertFalse(hashMap.contains(1.0f));

 // Test with an element added
 hashMap.insertKey(1.0f);
 hashMap.insertKeyAt(0, '\u0001'); // Assign char value for testing purposes
 assertTrue(hashMap.contains(1.0f));

 // Test with non-existent key
 assertFalse(hashMap.contains(2.0f));
 }

 @Test
 public void testForEach() {
 // Test empty hash map
 TFloatProcedure procedure = new TFloatProcedureAdapter() {
 @Override
 public boolean execute(float value) {
 fail("Should not call this method for an empty hash map");
 return false;
 }
 };
 assertFalse(hashMap.forEach(procedure));

 // Test with an element added
 hashMap.insertKey(1.0f);
 hashMap.insertKeyAt(0, '\u0001'); // Assign char value for testing purposes
 TFloatProcedure procedure2 = new TFloatProcedureAdapter() {
 @Override
 public boolean execute(float value) {
 assertEquals(1.0f, value, 0.0f);
 return true;
 }
 };
 assertTrue(hashMap.forEach(procedure2));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertionAndDeletionInconsistency() {
 // Test insertion and deletion inconsistency
 hashMap.insertKey(1.0f);
 hashMap.insertKeyAt(0, '\u0001'); // Assign char value for testing purposes
 hashMap.removeAt(0);
 hashMap.insertKey(1.0f); // This should fail due to inconsistency
 }

 @Test
 public void testWriteAndReadExternal() throws IOException, ClassNotFoundException {
 // Test writing and reading from an external file
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
 TFloatCharHash hashMapToSerialize = new TFloatCharHash(5, 0.75f);
 hashMapToSerialize.insertKey(1.0f);
 hashMapToSerialize.insertKeyAt(0, '\u0001'); // Assign char value for testing purposes
 hashMapToSerialize.writeExternal(objectOutput);
 objectOutput.close();

 ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
 ObjectInput objectInput = new ObjectInput(byteArrayInputStream);
 TFloatCharHash deserializedHashMap = new TFloatCharHash((int) objectInput.readInt());
 deserializedHashMap.readExternal(objectInput);
 objectInput.close();

 assertEquals(1.0f, deserializedHashMap.getNoEntryKey(), 0.0f);
 assertTrue(deserializedHashMap.contains(1.0f));
 }
}