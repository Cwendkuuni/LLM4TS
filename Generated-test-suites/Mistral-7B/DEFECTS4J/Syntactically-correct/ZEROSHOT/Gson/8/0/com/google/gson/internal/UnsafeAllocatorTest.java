import org.junit.Test;
import org.junit.Assert;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorInstance;

public class UnsafeAllocatorTest {

 private UnsafeAllocatorInstance unsafeAllocator;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() throws Exception {
 unsafeAllocator = UnsafeAllocator.create();
 }

 @Test
 public void testNewInstanceForObject() throws Exception {
 Object obj = unsafeAllocator.newInstance(Object.class);
 Assert.assertNotNull(obj);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstanceForInterface() throws Exception {
 Assert.assertThrows(UnsupportedOperationException.class, () -> {
 unsafeAllocator.newInstance(Object.class.getInterfaces()[0]);
 });
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstanceForAbstractClass() throws Exception {
 Assert.assertThrows(UnsupportedOperationException.class, () -> {
 unsafeAllocator.newInstance(AbstractClass.class);
 });
 }

 @Test
 public void testNewInstanceForCustomClass() throws Exception {
 public static class CustomClass {
 // empty class
 }

 Object customObj = unsafeAllocator.newInstance(CustomClass.class);
 Assert.assertNotNull(customObj);
 }

 @Test
 public void testNewInstanceForArray() throws Exception {
 int[] intArray = (int[]) unsafeAllocator.newInstance(int[].class);
 Assert.assertNotNull(intArray);
 }

 @Test
 public void testNewInstanceForCustomArray() throws Exception {
 public static class CustomArray extends int[] {
 // empty custom array
 }

 CustomArray customArray = (CustomArray) unsafeAllocator.newInstance(CustomArray[].class);
 Assert.assertNotNull(customArray);
 }

 @Test
 public void testNewInstanceForSerializableClass() throws Exception {
 public static class SerializableClass implements Serializable {
 // empty serializable class
 }

 Object serializableObj = unsafeAllocator.newInstance(SerializableClass.class);
 ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(
 SerializationUtils.serialize(serializableObj)));
 Object deserializedObj = ois.readObject();
 Assert.assertEquals(serializableObj, deserializedObj);
 }

 @Test
 public void testNewInstanceForSerializableArray() throws Exception {
 public static class SerializableArray extends int[] implements Serializable {
 // empty serializable array
 }

 SerializableArray serializableArray = (SerializableArray) unsafeAllocator.newInstance(SerializableArray[].class);
 ObjectOutputStream oos = new ObjectOutputStream(new ByteArrayOutputStream());
 oos.writeObject(serializableArray);
 ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(oos.toByteArray()));
 Object deserializedArray = (SerializableArray) ois.readObject();
 Assert.assertArrayEquals(serializableArray, deserializedArray);
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstanceForThrowingExceptionClass() throws Exception {
 Assert.assertThrows(InstantiationException.class, () -> {
 unsafeAllocator.newInstance(ThrowingException.class);
 });
 }

 @Test(expected = InstantiationException.class)
 public void testNewInstanceForThrowingExceptionArray() throws Exception {
 public static class ThrowingExceptionArray extends Throwable implements Serializable {
 // empty throwing exception array
 }

 Assert.assertThrows(InstantiationException.class, () -> {
 unsafeAllocator.newInstance(ThrowingExceptionArray[].class);
 });
 }

}