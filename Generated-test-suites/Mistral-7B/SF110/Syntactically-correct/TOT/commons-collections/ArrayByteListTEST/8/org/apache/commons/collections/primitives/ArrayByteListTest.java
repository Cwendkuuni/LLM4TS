import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 @Test
 public void testEmptyList() {
 ArrayByteList list = new ArrayByteList();
 Assert.assertEquals(0, list.size());
 }

 @Test
 public void testInitialCapacity() {
 ArrayByteList list = new ArrayByteList(5);
 Assert.assertEquals(5, list.size());
 }

 @Test
 public void testAddAll() {
 ByteCollection collection = new ArrayByteList();
 collection.add((byte) 1);
 collection.add((byte) 2);
 ArrayByteList list = new ArrayByteList(collection);
 list.add((byte) 3);
 list.add((byte) 4);

 Assert.assertEquals(4, list.size());
 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 Assert.assertEquals((byte) 3, list.get(2));
 Assert.assertEquals((byte) 4, list.get(3));
 }

 @Test
 public void testGet() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);

 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNegativeIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);

 list.get(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetBeyondLastIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);

 list.get(1);
 }

 @Test
 public void testSet() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);

 list.set(0, (byte) 3);
 list.set(1, (byte) 4);

 Assert.assertEquals((byte) 3, list.get(0));
 Assert.assertEquals((byte) 4, list.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetNegativeIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);

 list.set(-1, (byte) 3);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetBeyondLastIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);

 list.set(1, (byte) 3);
 list.set(2, (byte) 4);
 list.set(3, (byte) 5);

 list.set(5, (byte) 6);
 }

 @Test
 public void testRemoveElementAt() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);

 Assert.assertEquals((byte) 1, list.removeElementAt(0));
 Assert.assertEquals(1, list.size());
 Assert.assertEquals((byte) 2, list.get(0));

 Assert.assertEquals((byte) 2, list.removeElementAt(1));
 Assert.assertEquals(0, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtNegativeIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);

 list.removeElementAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtBeyondLastIndex() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);

 list.removeElementAt(0);

 Assert.assertEquals(0, list.size());
 list.removeElementAt(1);
 }

 @Test
 public void testEnsureCapacity() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.add((byte) 6);
 list.add((byte) 7);
 list.add((byte) 8);
 list.add((byte) 9);
 list.add((byte) 10);

 Assert.assertEquals(10, list.size());
 list.ensureCapacity(11);
 Assert.assertEquals(11, list.size());
 }

 @Test
 public void testTrimToSize() {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.add((byte) 6);
 list.add((byte) 7);
 list.add((byte) 8);
 list.add((byte) 9);
 list.add((byte) 10);
 list.trimToSize();

 Assert.assertEquals(10, list.size());
 }

 @Test
 public void testWriteReadObject() throws IOException, ClassNotFoundException {
 // Write an ArrayByteList to a byte array
 ByteArrayOutputStream out = new ByteArrayOutputStream();
 ObjectOutputStream objOut = new ObjectOutputStream(out);
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 objOut.writeObject(list);
 objOut.close();

 byte[] bytes = out.toByteArray();

 // Read an ArrayByteList from a byte array
 ByteArrayInputStream in = new ByteArrayInputStream(bytes);
 ObjectInputStream objIn = new ObjectInputStream(in);
 ArrayByteList list2 = (ArrayByteList) objIn.readObject();
 objIn.close();

 Assert.assertEquals(3, list2.size());
 Assert.assertEquals((byte) 1, list2.get(0));
 Assert.assertEquals((byte) 2, list2.get(1));
 Assert.assertEquals((byte) 3, list2.get(2));
 }
}