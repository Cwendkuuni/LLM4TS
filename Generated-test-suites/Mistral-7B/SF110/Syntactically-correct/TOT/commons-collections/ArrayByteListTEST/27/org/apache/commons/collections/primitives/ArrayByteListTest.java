import org.apache.commons.collections.primitives.ArrayByteList;
import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {

 @Test
 public void testConstructor() {
 ArrayByteList list = new ArrayByteList();
 Assert.assertEquals(0, list.size());
 }

 @Test
 public void testConstructorWithInitialCapacity() {
 ArrayByteList list = new ArrayByteList(5);
 Assert.assertEquals(0, list.size());
 Assert.assertEquals(5, list.capacity());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testConstructorWithNegativeInitialCapacity() {
 ArrayByteList list = new ArrayByteList(-1);
 // This test should fail with an IllegalArgumentException
 }

 @Test
 public void testAddAll() {
 ByteCollection byteCollection = new ArrayByteList();
 byteCollection.add((byte) 1);
 byteCollection.add((byte) 2);
 byteCollection.add((byte) 3);

 ArrayByteList list = new ArrayByteList();
 list.addAll(byteCollection);

 Assert.assertEquals(3, list.size());
 Assert.assertArrayEquals(new byte[]{(byte) 1, (byte) 2, (byte) 3}, list.toByteArray());
 }

 @Test
 public void testGet() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);
 list.set(3, (byte) 4);
 list.set(4, (byte) 5);

 Assert.assertEquals((byte) 1, list.get(0));
 Assert.assertEquals((byte) 2, list.get(1));
 Assert.assertEquals((byte) 3, list.get(2));
 Assert.assertEquals((byte) 4, list.get(3));
 Assert.assertEquals((byte) 5, list.get(4));
 }

 @Test
 public void testSize() {
 ArrayByteList list = new ArrayByteList();
 Assert.assertEquals(0, list.size());

 list.add((byte) 1);
 Assert.assertEquals(1, list.size());

 list.add((byte) 2);
 Assert.assertEquals(2, list.size());
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
 Assert.assertEquals((byte) 3, removedByte);
 Assert.assertEquals(4, list.size());
 }

 @Test
 public void testSet() {
 ArrayByteList list = new ArrayByteList(5);
 list.set(0, (byte) 1);
 list.set(1, (byte) 2);
 list.set(2, (byte) 3);
 list.set(3, (byte) 4);
 list.set(4, (byte) 5);

 list.set(0, (byte) 10);
 list.set(1, (byte) 20);
 list.set(2, (byte) 30);
 list.set(3, (byte) 40);
 list.set(4, (byte) 50);

 Assert.assertArrayEquals(new byte[]{(byte) 10, (byte) 20, (byte) 30, (byte) 40, (byte) 50}, list.toByteArray());
 }

 @Test
 public void testAdd() {
 ArrayByteList list = new ArrayByteList(5);
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 list.add((byte) 4);
 list.add((byte) 5);

 Assert.assertEquals(5, list.size());
 Assert.assertArrayEquals(new byte[]{(byte) 1, (byte) 2, (byte) 3, (byte) 4, (byte) 5}, list.toByteArray());
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
 list.add((byte) 11);
 list.add((byte) 12);
 list.add((byte) 13);
 list.add((byte) 14);
 list.add((byte) 15);
 list.add((byte) 16);
 list.add((byte) 17);
 list.add((byte) 18);
 list.add((byte) 19);
 list.add((byte) 20);
 list.add((byte) 21);
 list.add((byte) 22);
 list.add((byte) 23);
 list.add((byte) 24);
 list.add((byte) 25);
 list.add((byte) 26);
 list.add((byte) 27);
 list.add((byte) 28);
 list.add((byte) 29);
 list.add((byte) 30);
 list.add((byte) 31);
 list.add((byte) 32);
 list.add((byte) 33);
 list.add((byte) 34);
 list.add((byte) 35);
 list.add((byte) 36);
 list.add((byte) 37);
 list.add((byte) 38);
 list.add((byte) 39);
 list.add((byte) 40);
 list.add((byte) 41);
 list.add((byte) 42);
 list.add((byte) 43);
 list.add((byte) 44);
 list.add((byte) 45);
 list.add((byte) 46);
 list.add((byte) 47);
 list.add((byte) 48);
 list.add((byte) 49);
 list.add((byte) 50);
 list.add((byte) 51);
 list.add((byte) 52);
 list.add((byte) 53);
 list.add((byte) 54);
 list.add((byte) 55);
 list.add((byte) 56);
 list.add((byte) 57);
 list.add((byte) 58);
 list.add((byte) 59);
 list.add((byte) 60);
 list.add((byte) 61);
 list.add((byte) 62);
 list.add((byte) 63);
 list.add((byte) 64);
 list.add((byte) 65);
 list.add((byte) 66);
 list.add((byte) 67);
 list.add((byte) 68);
 list.add((byte) 69);
 list.add((byte) 70);
 list.add((byte) 71);
 list.add((byte) 72);
 list.add((byte) 73);
 list.add((byte) 74);
 list.add((byte) 75);
 list.add((byte) 76);
 list.add((byte) 77);
 list.add((byte) 78);
 list.add((byte) 79);
 list.add((byte) 80);
 list.add((byte) 81);
 list.add((byte) 82);
 list.add((byte) 83);
 list.add((byte) 84);
 list.add((byte) 85);
 list.add((byte) 86);
 list.add((byte) 87);
 list.add((byte) 88);
 list.add((byte) 89);
 list.add((byte) 90);
 list.add((byte) 91);
 list.add((byte) 92);
 list.add((byte) 93);
 list.add((byte) 94);
 list.add((byte) 95);
 list.add((byte) 96);
 list.add((byte) 97);
 list.add((byte) 98);
 list.add((byte) 99);
 list.add((byte) 100);

 Assert.assertEquals(100, list.size());
 }

 @Test
 public void testTrimToSize() {
 ArrayByteList list = new ArrayByteList(50);
 for (int i = 0; i < 20; i++) {
 list.add((byte) i);
 }
 list.trimToSize();
 Assert.assertEquals(20, list.size());
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 // Serialize an empty list
 ByteArrayOutputStream baos = new ByteArrayOutputStream();
 ObjectOutputStream oos = new ObjectOutputStream(baos);
 ArrayByteList list = new ArrayByteList();
 oos.writeObject(list);
 oos.close();
 byte[] bytes = baos.toByteArray();
 ArrayByteList deserializedList = new ArrayByteList();
 ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
 ObjectInputStream ois = new ObjectInputStream(bais);
 deserializedList = (ArrayByteList) ois.readObject();
 ois.close();
 bais.close();
 Assert.assertEquals(0, deserializedList.size());

 // Serialize a list with elements
 list = new ArrayByteList();
 list.add((byte) 1);
 list.add((byte) 2);
 list.add((byte) 3);
 oos = new ObjectOutputStream(baos);
 oos.writeObject(list);
 oos.close();
 bytes = baos.toByteArray();
 deserializedList = new ArrayByteList();
 bais = new ByteArrayInputStream(bytes);
 ois = new ObjectInputStream(bais);
 deserializedList = (ArrayByteList) ois.readObject();
 ois.close();
 bais.close();
 Assert.assertArrayEquals(new byte[]{(byte) 1, (byte) 2, (byte) 3}, deserializedList.toByteArray());
 }

 @Test
 public void testCheckRange() {
 ArrayByteList list = new ArrayByteList(5);
 try {
 list.checkRange(-1);
 // This test should fail
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 try {
 list.checkRange(2);
 // This test should fail
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 try {
 list.checkRange(5);
 // This test should pass
 } catch (IndexOutOfBoundsException e) {
 // This test should fail
 Assert.fail();
 }
 }

 @Test
 public void testCheckRangeIncludingEndpoint() {
 ArrayByteList list = new ArrayByteList(5);
 try {
 list.checkRangeIncludingEndpoint(-1);
 // This test should fail
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 try {
 list.checkRangeIncludingEndpoint(2);
 // This test should fail
 } catch (IndexOutOfBoundsException e) {
 // Pass
 }
 try {
 list.checkRangeIncludingEndpoint(5);
 // This test should pass
 } catch (IndexOutOfBoundsException e) {
 // This test should fail
 Assert.fail();
 }
 }
}