import org.junit.Test;
import org.junit.Assert.*;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocatorImpl;

public class UnsafeAllocatorTest {

 @Test
 public void testNewInstance_Object() {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 Object obj = unsafeAllocator.newInstance(Object.class);
 assertNotNull(obj);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_Interface() {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 try {
 unsafeAllocator.newInstance(String.class);
 } catch (Exception e) {
 throw new AssertionError(e);
 }
 fail("Expected UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_AbstractClass() {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 try {
 unsafeAllocator.newInstance(AbstractClass.class);
 } catch (Exception e) {
 throw new AssertionError(e);
 }
 fail("Expected UnsupportedOperationException");
 }

 @Test
 public void testNewInstance_CustomClass() {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 CustomClass customClass = (CustomClass) unsafeAllocator.newInstance(CustomClass.class);
 // Add custom assertions or tests here
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_Throwable() {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 try {
 unsafeAllocator.newInstance(Throwable.class);
 } catch (Exception e) {
 throw new AssertionError(e);
 }
 fail("Expected UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_Array() {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 try {
 unsafeAllocator.newInstance(int[].class);
 } catch (Exception e) {
 throw new AssertionError(e);
 }
 fail("Expected UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_Primitive() {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 try {
 unsafeAllocator.newInstance(int.class);
 } catch (Exception e) {
 throw new AssertionError(e);
 }
 fail("Expected UnsupportedOperationException");
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstance_Null() {
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 try {
 unsafeAllocator.newInstance(null);
 } catch (Exception e) {
 throw new AssertionError(e);
 }
 fail("Expected UnsupportedOperationException");
 }

 @Test
 public void testNewInstance_CustomAllocator() {
 UnsafeAllocator unsafeAllocator = new UnsafeAllocatorImpl();
 CustomClass customClass = (CustomClass) unsafeAllocator.newInstance(CustomClass.class);
 // Add custom assertions or tests here
 }
}

class AbstractClass {
 // Abstract class definition here
}

class CustomClass {
 // Custom class definition here
}