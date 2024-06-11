import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.internal.UnsafeAllocator.UnsafeAllocator_;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

 private UnsafeAllocator allocator;

 @Test
 public void testNewInstance() throws Exception {
 allocator = UnsafeAllocator.create();

 // Test with Object type
 Object instance = allocator.newInstance(Object.class);
 assertNotNull(instance);
 assertInstanceOf(Object.class, instance);

 // Test with custom class
 class TestClass {
 // Empty class
 }
 UnsafeAllocator_ unsafeAllocator = (UnsafeAllocator_) allocator;
 TestClass instance = (TestClass) unsafeAllocator.newInstance(TestClass.class);
 assertNotNull(instance);
 assertInstanceOf(TestClass.class, instance);

 // Test unsupported types
 try {
 unsafeAllocator.newInstance(String.class);
 } catch (UnsupportedOperationException e) {
 // Expected
 }

 try {
 unsafeAllocator.newInstance(int[].class);
 } catch (UnsupportedOperationException e) {
 // Expected
 }
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstanceWithAbstractClass() throws Exception {
 allocator = UnsafeAllocator.create();
 UnsafeAllocator_ unsafeAllocator = (UnsafeAllocator_) allocator;
 assertInstanceOf(UnsafeAllocator.class, unsafeAllocator);
 AbstractClass instance = (AbstractClass) unsafeAllocator.newInstance(AbstractClass.class);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstanceWithInterface() throws Exception {
 allocator = UnsafeAllocator.create();
 UnsafeAllocator_ unsafeAllocator = (UnsafeAllocator_) allocator;
 assertInstanceOf(UnsafeAllocator.class, unsafeAllocator);
 Interface instance = (Interface) unsafeAllocator.newInstance(Interface.class);
 }

 @Test
 public void testNewInstanceWithPrivateConstructor() throws Exception {
 allocator = UnsafeAllocator.create();
 UnsafeAllocator_ unsafeAllocator = (UnsafeAllocator_) allocator;

 class TestClass {
 private TestClass() {}
 }
 TestClass instance = (TestClass) unsafeAllocator.newInstance(TestClass.class);
 assertNotNull(instance);
 assertInstanceOf(TestClass.class, instance);

 Constructor<TestClass> constructor = TestClass.class.getDeclaredConstructor();
 constructor.setAccessible(true);
 try {
 instance = constructor.newInstance();
 } catch (InstantiationException | IllegalAccessException e) {
 // Expected
 }
 assertNotSame(instance, unsafeAllocator.newInstance(TestClass.class));
 }
}