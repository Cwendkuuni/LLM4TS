import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 @Test
 public void testEmptyArrayByteList() {
 ArrayByteList list = new ArrayByteList();
 Assert.assertEquals(0, list.size());
 }

 @Test
 public void testArrayByteListWithInitialCapacity() {
 ArrayByteList list = new ArrayByteList(5);
 Assert.assertEquals(0, list.size());
 Assert.assertArrayEquals(new byte[5], list._data);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNegativeInitialCapacity() {
 ArrayByteList list = new ArrayByteList(-5);
 }

 @Test
 public void testArrayByteListWithCollection() {
 ByteCollection byteCollection = new ArrayByteList();
 byteCollection.add((byte) 1);
 byteCollection.add((byte) 2);
 ArrayByteList list = new ArrayByteList(byteCollection);
 Assert.assertEquals(2, list.size());
 }

 @Test
 public void testGetByte() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetByteOutOfBounds() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.get(-1));
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.get(5));
 }

 @Test
 public void testSetByte() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 Assert.assertEquals((byte) 1, list.set(0, (byte) 3));
 Assert.assertEquals((byte) 2, list.get(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetByteOutOfBounds() {
 ArrayByteList list = new ArrayByteList(5);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.set(-1, (byte) 1));
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.set(5, (byte) 1));
 }

 @Test
 public void testAddByte() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 Assert.assertEquals(1, list.size());
 Assert.assertEquals((byte) 1, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddByteOutOfBounds() {
 ArrayByteList list = new ArrayByteList(5);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.add(-1, (byte) 1));
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.add(6, (byte) 1));
 }

 @Test
 public void testAddAllByte() {
 ArrayByteList list1 = new ArrayByteList(5);
 list1.add((byte) 1);
 list1.add((byte) 2);
 ArrayByteList list2 = new ArrayByteList(3);
 list2.add((byte) 3);
 list2.add((byte) 4);
 list2.add((byte) 5);
 list1.addAll(3, list2);
 Assert.assertEquals(8, list1.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddAllByteOutOfBounds() {
 ArrayByteList list1 = new ArrayByteList(5);
 list1.add((byte) 1);
 list1.add((byte) 2);
 ArrayByteList list2 = new ArrayByteList(3);
 list2.add((byte) 3);
 list2.add((byte) 4);
 list2.add((byte) 5);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list1.addAll(-1, list2));
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list1.addAll(6, list2));
 }

 @Test
 public void testRemoveElementAt() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);
 list.set(3, (byte) 4);
 list.set(4, (byte) 5);
 byte removedByte = list.removeElementAt(2);
 Assert.assertEquals(4, list.size());
 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 Assert.assertEquals((byte) 4, list.get(3));
 Assert.assertEquals((byte) 5, list.get(4));
 Assert.assertEquals((byte) 3, removedByte);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtOutOfBounds() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(-1));
 Assert.assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(5));
 }

 @Test
 public void testEnsureCapacity() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 6);
 list.add((byte) 7);
 list.add((byte) 8);
 list.add((byte) 9);
 list.add((byte) 10);
 list.add((byte) 11);
 list.add((byte) 12);
 list.ensureCapacity(20);
 Assert.assertEquals(13, list.size());
 }

 @Test
 public void testTrimToSize() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);
 list.trimToSize();
 Assert.assertEquals(5, list.size());
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 // Test writing and reading a byte list
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 oos.writeObject(list);
 oos.close();
 ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
 ArrayByteList deserializedList = (ArrayByteList) ois.readObject();
 ois.close();
 Assert.assertEquals(2, deserializedList.size());
 Assert.assertEquals((byte) 1, deserializedList.get(0));
 Assert.assertEquals((byte) 2, deserializedList.get(1));
 }
}