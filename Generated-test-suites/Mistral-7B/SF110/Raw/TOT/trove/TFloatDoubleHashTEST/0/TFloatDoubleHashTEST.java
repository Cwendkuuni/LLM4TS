import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import gnu.trove.impl.hash.TFloatDoubleHash;
import gnu.trove.procedure.TFloatProcedure;

public class TFloatDoubleHashTest {

 private TFloatDoubleHash hashMap;

 @Before
 public void setUp() {
 hashMap = new TFloatDoubleHash(5);
 hashMap.put(0.1f, 1.0);
 hashMap.put(0.2f, 2.0);
 hashMap.put(0.3f, 3.0);
 }

 @Test
 public void testGetNoEntryKey() {
 assertEquals(0.0f, hashMap.getNoEntryKey(), 0.0);
 }

 @Test
 public void testGetNoEntryValue() {
 assertEquals(0.0, hashMap.getNoEntryValue(), 0.0);
 }

 @Test
 public void testContainsFloatValue() {
 assertTrue(hashMap.contains(0.1f));
 assertFalse(hashMap.contains(0.4f));
 }

 @Test
 public void testForEachProcedure() {
 TFloatProcedure procedure = new TFloatProcedure() {
 @Override
 public boolean execute(float value) {
 assertEquals(value, hashMap.get(value), 0.0);
 return true;
 }
 };
 assertTrue(hashMap.forEach(procedure));
 }

 @Test
 public void testRemoveAtIndex() {
 hashMap.removeAt(1);
 assertEquals(2, hashMap.size());
 assertFalse(hashMap.contains(0.2f));
 }

 @Test(expected = IllegalStateException.class)
 public void testInsertKeyWithFullMap() {
 hashMap.put(0.4f, 4.0);
 }

 @Test
 public void testWriteReadExternal() throws IOException, ClassNotFoundException {
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutput objOut = new ObjectOutputStream(out);
 hashMap.writeExternal(objOut);
 ObjectInput objIn = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
 TFloatDoubleHash newMap = (TFloatDoubleHash) objIn.readObject();
 assertEquals(2, newMap.size());
 assertEquals(0.1f, newMap.get(0.1f), 0.0);
 assertEquals(1.0, newMap.get(0.1f), 0.0);
 }
}