import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import com.google.gson.internal.UnsafeAllocator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class UnsafeAllocatorTest {

 @Test
 public void testNewInstanceForString() {
 StringCls = String.class;
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 try {
 String instance = unsafeAllocator.newInstance(StringCls);
 assertNotNull(instance);
 } catch (Exception e) {
 throw new AssertionError("Error creating instance of String", e);
 }
 }

 @Test
 public void testNewInstanceForArray() {
 IntArrayCls = int[].class;
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 try {
 int[] instance = (int[]) unsafeAllocator.newInstance(IntArrayCls);
 assertNotNull(instance);
 } catch (Exception e) {
 throw new AssertionError("Error creating instance of int[]", e);
 }
 }

 @Test
 public void testNewInstanceForCustomClass() throws Exception {
 CustomCls = MyCustomClass.class;
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 MyCustomClass instance = (MyCustomClass) unsafeAllocator.newInstance(CustomCls);
 assertNotNull(instance);
 // add testing for specific methods and fields if custom class has them
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstanceForInterface() {
 InterfaceCls = MyCustomInterface.class;
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 try {
 unsafeAllocator.newInstance(InterfaceCls);
 } catch (Exception e) {
 if (e instanceof UnsupportedOperationException) {
 return;
 } else {
 throw e;
 }
 }
 throw new AssertionError("Unexpected exception: " + e.getMessage());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testNewInstanceForAbstractClass() {
 AbstractCls = MyAbstractClass.class;
 UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
 try {
 unsafeAllocator.newInstance(AbstractCls);
 } catch (Exception e) {
 if (e instanceof UnsupportedOperationException) {
 return;
 } else if (e instanceof InvocationTargetException) {
 throw ((InvocationTargetException) e).getCause();
 } else if (e instanceof UndeclaredThrowableException) {
 throw ((UndeclaredThrowableException) e).getException();
 } else {
 throw e;
 }
 }
 throw new AssertionError("Unexpected exception: " + e.getMessage());
 }
}