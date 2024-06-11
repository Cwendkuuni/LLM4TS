import org.junit.Test;
import org.junit.experimental.annotations.EdgeCases;
import org.junit.experimental.annotations.Matcher;
import org.junit.experimental.annotations.Repeated;
import org.junit.experimental.annotations.Unrolled;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
@EdgeCases(TestCases.class)
@Unrolled
public class UnsafeAllocatorTest {

 private final Class<?> clazz;
 private final UnsafeAllocator allocator;

 @Parameters(name = "{index}: testClass={0}")
 public static Collection<Object[]> data() {
 return Arrays.asList(new Object[][] {
 new Object[] { String.class },
 new Object[] { Integer.class },
 new Object[] { List.class },
 new Object[] { Map.class },
 new Object[] { MyClass.class }
 });
 }

 public UnsafeAllocatorTest(Class<?> clazz) {
 this.clazz = clazz;
 this.allocator = UnsafeAllocator.create();
 }

 @Test
 public void testNewInstance() throws Exception {
 T result = (T) allocator.newInstance(clazz);
 assertEquals(clazz, result.getClass());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCannotAllocateInterface() throws Exception {
 assertThrows(UnsupportedOperationException.class, () ->
 allocator.newInstance(Interface.class));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testCannotAllocateAbstractClass() throws Exception {
 assertThrows(UnsupportedOperationException.class, () ->
 allocator.newInstance(AbstractClass.class));
 }

 @Test(expected = InstantiationException.class)
 @Repeated(value = 10)
 public void testRandomException() throws Exception {
 try {
 allocator.newInstance(RandomClass.class);
 fail();
 } catch (InstantiationException e) {
 // expected
 }
 }

 @Test
 @EdgeCases(TestCasesEdge.class)
 public void testEdgeCases() throws Exception {
 T result = allocator.newInstance(clazz);
 // edge cases test here
 }

 @Test(expected = NullPointerException.class)
 public void testNullPointerException() throws Exception {
 assertThrows(NullPointerException.class, () ->
 allocator.newInstance(null));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIllegalArgumentException() throws Exception {
 assertThrows(IllegalArgumentException.class, () ->
 allocator.newInstance(Object[].class));
 }

 @Test
 public void testErrorScenario() throws Exception {
 try {
 allocator.newInstance(ThrowingClass.class);
 fail();
 } catch (ThrowingException e) {
 // expected
 }
 }

 @Test
 public void testSerialization() throws IOException, ClassNotFoundException {
 ObjectOutputStream output = new ObjectOutputStream(new ByteArrayOutputStream());
 output.writeObject(allocator.newInstance(SerializableClass.class));
 ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(output.toByteArray()));
 SerializableClass result = (SerializableClass) input.readObject();
 assertEquals(SerializableClass.class, result.getClass());
 }

 @Test
 public void testSerializationFailure() throws IOException, ClassNotFoundException {
 ObjectOutputStream output = new ObjectOutputStream(new ByteArrayOutputStream());
 output.writeObject(allocator.newInstance(SerializableFailClass.class));
 ObjectInputStream input = new ObjectInputStream(new ByteArrayInputStream(output.toByteArray()));
 assertThrows(ClassNotFoundException.class, () ->
 input.readObject());
 }

 private static class TestCasesEdge {
 // edge cases test here
 }

 private static class TestCases {
 // test helper methods here
 }

 private static class MyClass {}
 private static interface Interface {}
 private static abstract class AbstractClass {}
 private static class RandomClass {
// constructor code here
 }
 private static class ThrowingClass {
 static void throwException() throws ThrowingException {
 throw new ThrowingException();
 }
 }
 private static class SerializableClass implements Serializable {
 // serializable class code here
 }
 private static class SerializableFailClass implements Serializable {
 // serializable class code here that throws an exception on deserialization
 }
}