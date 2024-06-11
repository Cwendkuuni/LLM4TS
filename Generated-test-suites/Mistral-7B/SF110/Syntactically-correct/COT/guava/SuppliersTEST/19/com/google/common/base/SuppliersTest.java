import static org.junit.Assert.*;
import static com.google.common.base.Preconditions.*;
import static com.google.common.base.Suppliers.*;

import java.util.concurrent.*;
import org.junit.*;

public class SuppliersTest {

 @SuppressWarnings("unchecked")
 @Test
 public void testCompose() {
 Supplier<Integer> supplier = ofInstance(1);
 Function<Integer, Integer> function = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input * 2;
 }
 };
 Supplier<Integer> expectedResult = compose(function, supplier);
 Supplier<Integer> actualResult = expectedResult.get();
 assertEquals(expectedResult, actualResult);
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullFunction() {
 Function<Integer, Integer> function = null;
 Supplier<Integer> supplier = ofInstance(1);
 try {
 compose(function, supplier);
 } catch (NullPointerException e) {
 assertEquals("function", e.getMessage());
 throw e;
 }
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullSupplier() {
 Supplier<Integer> supplier = null;
 Function<Integer, Integer> function = new Function<Integer, Integer>() {
 @Override
 public Integer apply(Integer input) {
 return input * 2;
 }
 };
 try {
 compose(function, supplier);
 } catch (NullPointerException e) {
 assertEquals("supplier", e.getMessage());
 throw e;
 }
 }

 @SuppressWarnings("unchecked")
 @Test
 public void testMemoize() {
 Supplier<String> supplier = memoize(() -> "Hello");
 Supplier<String> actualResult = supplier.get();
 Supplier<String> expectedResult = ofInstance("Hello");
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testMemoizeMultipleCalls() {
 Supplier<String> supplier = memoize(() -> "Hello");
 String result1 = supplier.get();
 String result2 = supplier.get();
 assertEquals(result1, result2);
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<String> supplier = memoizeWithExpiration(() -> "Hello", 1, TimeUnit.SECONDS);
 String result1 = supplier.get();
 String result2 = supplier.get();
 assertEquals(result1, result2);
 String result3 = supplier.get();
 assertNotEquals(result1, result3);
 }

 @Test(expected = NullPointerException.class)
 public void testMemoizeWithNullSupplier() {
 Supplier<String> supplier = null;
 try {
 memoize(supplier);
 } catch (NullPointerException e) {
 assertEquals("supplier", e.getMessage());
 throw e;
 }
 }

 @Test
 public void testOfInstance() {
 Supplier<String> supplier = ofInstance("Hello");
 String actualResult = supplier.get();
 String expectedResult = "Hello";
 assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<String> supplier = synchronizedSupplier(() -> "Hello");
 String result1 = supplier.get();
 String result2 = supplier.get();
 assertEquals(result1, result2);
 }

}