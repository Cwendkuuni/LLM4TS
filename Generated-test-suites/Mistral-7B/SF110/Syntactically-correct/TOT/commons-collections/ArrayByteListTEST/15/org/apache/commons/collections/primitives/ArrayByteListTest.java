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
 public void testInitialCapacity() {
 ArrayByteList list = new ArrayByteList(5);
 Assert.assertEquals(5, list.size());
 }

 @Test
 public void testAddAll() {
 ByteCollection collection = new ArrayByteList();
 collection.add((byte) 1);
 collection.add((byte) 2);
 collection.add((byte) 3);

 ArrayByteList list = new ArrayByteList(collection);
 Assert.assertEquals(3, list.size());
 }

 @Test
 public void testGet() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);

 byte byteValue = list.get(2);
 Assert.assertEquals((byte) 3, byteValue);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetNegativeIndex() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);

 list.get(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetLargerIndex() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);

 list.get(5);
 }

 @Test
 public void testRemoveElementAt() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);

 byte removedByte = list.removeElementAt(3);
 Assert.assertEquals((byte) 4, removedByte);
 Assert.assertEquals(4, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtNegativeIndex() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);

 list.removeElementAt(-1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtLargerIndex() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);

 list.removeElementAt(5);
 }

 @Test
 public void testSet() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);

 byte newByteValue = (byte) 10;
 byte oldByteValue = list.set(3, newByteValue);
 Assert.assertEquals((byte) 4, oldByteValue);
 Assert.assertEquals((byte) 10, list.get(3));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetNegativeIndex() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);

 list.set(-1, (byte) 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetLargerIndex() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);

 list.set(5, (byte) 1);
 }

 @Test
 public void testAdd() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);

 list.add(3, (byte) 10);
 Assert.assertEquals(6, list.size());
 Assert.assertEquals((byte) 10, list.get(3));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddNegativeIndex() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);

 list.add(-1, (byte) 1);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddLargerIndex() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);

 list.add(6, (byte) 1);
 }

 @Test
 public void testEnsureCapacity() {
 ArrayByteList list = new ArrayByteList(5);
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

 list.ensureCapacity(20);
 }

 @Test
 public void testTrimToSize() {
 ArrayByteList list = new ArrayByteList(5);
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
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ArrayByteList list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
 objectOutputStream.writeObject(list);
 ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
 ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();
 Assert.assertEquals(3, deserializedList.size());
 }
}