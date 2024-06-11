import org.junit.Test;
import org.junit.Assert;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorInstance;

public class UnsafeAllocatorTest {

 private UnsafeAllocatorInstance unsafeAllocator;

 @Test
 public void testNewInstanceForObject() {
 Object obj = unsafeAllocator.newInstance(Object.class);
 Assert.assertNotNull(obj);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstanceForInterface() {
 Class<Comparable> comparableClass = Comparable.class;
 try {
 unsafeAllocator.newInstance(comparableClass);
 } catch (Exception e) {
 Assert.assertEquals("Cannot allocate " + comparableClass, e.getMessage());
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstanceForAbstractClass() {
 Class<AbstractClass> abstractClass = AbstractClass.class;
 try {
 unsafeAllocator.newInstance(abstractClass);
 } catch (Exception e) {
 Assert.assertEquals("Cannot allocate " + abstractClass, e.getMessage());
 }
 }

 @Test
 public void testNewInstanceForCustomClass() {
 Class<CustomClass> customClass = CustomClass.class;
 try {
 CustomClass instance = (CustomClass) unsafeAllocator.newInstance(customClass);
 Assert.assertNotNull(instance);
 // perform some checks on the instance here
 } catch (Exception e) {
 Assert.fail("Failed to instantiate custom class: " + e.getMessage());
 }
 }

 @Test(expected = Exception.class)
 public void testNewInstanceForThrowingClass() {
 Class<ThrowingClass> throwingClass = ThrowingClass.class;
 try {
 unsafeAllocator.newInstance(throwingClass);
 } catch (Exception e) {
 // expected exception
 }
 }

 @Test
 public void testNewInstanceForNullClass() {
 try {
 unsafeAllocator.newInstance(null);
 Assert.fail("Expected NullPointerException");
 } catch (NullPointerException e) {
 // expected null pointer exception
 }
 }

 @Test
 public void testNewInstanceForArray() {
 int[] intArray = (int[]) unsafeAllocator.newInstance(int[].class);
 Assert.assertNotNull(intArray);
 // perform some checks on the array here
 }
}