import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 @Test
 public void testConstructorWithInitialCapacity() {
 ArrayByteList list = new ArrayByteList(5);
 Assert.assertEquals(5, list.size());
 }

 @Test
 public void testConstructorWithEmptyByteCollection() {
 ByteCollection byteCollection = new ArrayList<>();
 ArrayByteList list = new ArrayByteList((ByteCollection) byteCollection);
 Assert.assertEquals(0, list.size());
 }

 @Test
 public void testGetMethod() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 byte element = list.get(0);
 Assert.assertEquals((byte) 1, element);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetMethodWithNegativeIndex() {
 ArrayByteList list = new ArrayByteList(5);
 try {
 list.get(-1);
 } catch (IndexOutOfBoundsException e) {
 Assert.assertEquals("Should be at least 0 and less than 5, found -1", e.getMessage());
 throw e;
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetMethodWithIndexGreaterThanSize() {
 ArrayByteList list = new ArrayByteList(5);
 try {
 list.get(6);
 } catch (IndexOutOfBoundsException e) {
 Assert.assertEquals("Should be at least 0 and less than 5, found 6", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testSizeMethod() {
 ArrayByteList list = new ArrayByteList(5);
 Assert.assertEquals(5, list.size());
 }

 @Test
 public void testRemoveElementAtMethod() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 byte removedElement = list.removeElementAt(0);
 Assert.assertEquals((byte) 1, removedElement);
 Assert.assertEquals(4, list.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtMethodWithNegativeIndex() {
 ArrayByteList list = new ArrayByteList(5);
 try {
 list.removeElementAt(-1);
 } catch (IndexOutOfBoundsException e) {
 Assert.assertEquals("Should be at least 0 and less than 5, found -1", e.getMessage());
 throw e;
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testRemoveElementAtMethodWithIndexGreaterThanSize() {
 ArrayByteList list = new ArrayByteList(5);
 try {
 list.removeElementAt(6);
 } catch (IndexOutOfBoundsException e) {
 Assert.assertEquals("Should be at least 0 and less than 5, found 6", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testSetMethod() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 byte newElement = (byte) 2;
 list.set(0, newElement);
 Assert.assertEquals((byte) 2, list.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetMethodWithNegativeIndex() {
 ArrayByteList list = new ArrayByteList(5);
 try {
 list.set(-1, (byte) 1);
 } catch (IndexOutOfBoundsException e) {
 Assert.assertEquals("Should be at least 0 and less than 5, found -1", e.getMessage());
 throw e;
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetMethodWithIndexGreaterThanSize() {
 ArrayByteList list = new ArrayByteList(5);
 try {
 list.set(6, (byte) 1);
 } catch (IndexOutOfBoundsException e) {
 Assert.assertEquals("Should be at least 0 and less than 5, found 6", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testAddMethod() {
 ArrayByteList list = new ArrayByteList(5);
 list.add(2, (byte) 3);
 Assert.assertEquals(6, list.size());
 Assert.assertEquals((byte) 3, list.get(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddMethodWithNegativeIndex() {
 ArrayByteList list = new ArrayByteList(5);
 try {
 list.add(-1, (byte) 1);
 } catch (IndexOutOfBoundsException e) {
 Assert.assertEquals("Should be at least 0 and less than 5, found -1", e.getMessage());
 throw e;
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testAddMethodWithIndexGreaterThanSize() {
 ArrayByteList list = new ArrayByteList(5);
 try {
 list.add(6, (byte) 1);
 } catch (IndexOutOfBoundsException e) {
 Assert.assertEquals("Should be at least 0 and less than 5, found 6", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testEnsureCapacityMethod() {
 ArrayByteList list = new ArrayByteList(5);
 list.ensureCapacity(10);
 Assert.assertEquals(10, list._data.length);
 }

 @Test
 public void testTrimToSizeMethod() {
 ArrayByteList list = new ArrayByteList(10);
 list.trimToSize();
 Assert.assertEquals(10, list._data.length);
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 // Serialize the list
 ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
 ObjectOutputStream out = new ObjectOutputStream(byteStream);
 out.writeObject(new ArrayByteList(5));
 out.close();
 byte[] byteArray = byteStream.toByteArray();

 // Deserialize the list
 ByteArrayInputStream byteInput = new ByteArrayInputStream(byteArray);
 ObjectInputStream in = new ObjectInputStream(byteInput);
 ArrayByteList deserializedList = (ArrayByteList) in.readObject();
 in.close();

 // Check if the list is equal to the original one
 Assert.assertArrayEquals(new byte[]{0, 0, 0, 0, 0}, deserializedList._data);
 Assert.assertEquals(5, deserializedList.size());
 }
}