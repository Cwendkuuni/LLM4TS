import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

 private UnsafeAllocator allocator = UnsafeAllocator.create();

 @Test(expected = UnsupportedOperationException.class)
 public void testCannotAllocateInterface() throws Exception {
 Class<Object> objectClass = Object.class;
 allocator.newInstance(objectClass);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCannotAllocateAbstractClass() throws Exception {
 Class<AbstractClass> abstractClass = AbstractClass.class;
 allocator.newInstance(abstractClass);
 }

 @Test
 public void testAllocateSimpleClass() throws Exception {
 Class<SimpleClass> simpleClass = SimpleClass.class;
 SimpleClass instance = allocator.newInstance(simpleClass);
 // Perform some assertions on the instance if needed
 }

 @Test
 public void testAllocateArray() throws Exception {
 Class<int[]> intArrayClass = int[].class;
 int[] instance = (int[]) allocator.newInstance(intArrayClass);
 // Perform some assertions on the instance if needed
 }

 @Test
 public void testAllocateNestedClass() throws Exception {
 Class<NestedClass> nestedClass = NestedClass.class;
 NestedClass instance = allocator.newInstance(nestedClass);
 // Perform some assertions on the instance if needed
 }

 @Test(expected = Exception.class)
 public void testAllocateFailingClass() throws Exception {
 Class<FailingClass> failingClass = FailingClass.class;
 allocator.newInstance(failingClass);
 }

 @Test(expected = IllegalAccessError.class)
 public void testReflectiveAccessFailure() throws Exception {
 Class<?> clazz = UnsafeAllocator.class;
 Method newInstance = clazz.getDeclaredMethod("newInstance", Class.class);
 newInstance.setAccessible(true);
 newInstance.invoke(null, Object.class);
 }

 private static class AbstractClass {
 }

 private static class FailingClass {
 private FailingClass() {
 throw new RuntimeException("Cannot create instance of FailingClass");
 }
 }

 private static class NestedClass {
 }

 private static class SimpleClass {
 }
}