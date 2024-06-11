import org.junit.Test;
import org.junit.Assert.*;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorImpl;

public class UnsafeAllocatorTest {

 @Test
 public void testNewInstance() {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();

 // Test with a simple Java class
 Class<String> stringClass = String.class;
 String str = unsafeAllocator.newInstance(stringClass);
 assertTrue(str instanceof String);
 Assert.assertEquals("", str);

 // Test with a generic class
 Class<ArrayList<Integer>> arrayListClass = ArrayList.class;
 ArrayList<Integer> list = null;
 try {
 list = (ArrayList<Integer>) unsafeAllocator.newInstance(arrayListClass);
 } catch (Exception e) {
 fail("Failed to allocate ArrayList instance: " + e.getMessage());
 }
 assertNotNull(list);
 assertFalse(list.isEmpty());
 list.add(1);
 list.add(2);
 List<Integer> expectedList = Arrays.asList(1, 2);
 assertEquals(expectedList, list);

 // Test with a custom class
 class CustomClass implements Cloneable {
 private int value;

 public CustomClass(int value) {
 this.value = value;
 }

 @Override
 public CustomClass clone() throws CloneNotSupportedException {
 return new CustomClass(this.value);
 }
 }

 Class<CustomClass> customClass = CustomClass.class;
 CustomClass original = new CustomClass(1);
 CustomClass cloned = null;
 try {
 cloned = (CustomClass) unsafeAllocator.newInstance(customClass);
 } catch (Exception e) {
 fail("Failed to allocate CustomClass instance: " + e.getMessage());
 }
 assertNotNull(cloned);
 assertNotSame(original, cloned);
 assertEquals(original.value, cloned.value);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnableToAllocateInterface() {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 Class<String> stringInterface = String.class.isInterface() ? String.class : Object.class; // Fallback to Object if String is an interface
 try {
 unsafeAllocator.newInstance(stringInterface);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testUnableToAllocateAbstractClass() {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 Class<AbstractClass> abstractClass = AbstractClass.class;
 try {
 unsafeAllocator.newInstance(abstractClass);
 fail("Expected UnsupportedOperationException");
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 private static class AbstractClass {
 // Abstract class for testing purposes
 }
}