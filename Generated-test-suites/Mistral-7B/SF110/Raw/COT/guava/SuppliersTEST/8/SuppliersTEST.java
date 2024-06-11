import static org.junit.Assert.*;
import static com.google.common.base.Preconditions.*;

import java.util.concurrent.Supplier;
import org.junit.Test;

public class SuppliersTest {

 // Test for compose method
 @Test
 public void testCompose() {
 Supplier<Integer> supplier1 = () -> 1;
 Function<Integer, Integer> function = i -> i * i;
 Supplier<Integer> expectedSupplier = () -> 1 * 1;
 Supplier<Integer> supplierComposition = Suppliers.compose(function, supplier1);
 assertEquals(expectedSupplier, supplierComposition);
 }

 // Test for memoize method
 @Test
 public void testMemoize() {
 Supplier<Integer> supplier = () -> 1 + 2;
 Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
assertNotSame(supplier, memoizedSupplier);
 assertEquals(1 + 2, memoizedSupplier.get());
 assertEquals(1 + 2, memoizedSupplier.get());
 }

 // Test for memoizeWithExpiration method
 @Test
 public void testMemoizeWithExpiration() {
 Supplier<Long> supplier = () -> System.nanoTime();
 Supplier<Long> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);
 long initialTime = expiringMemoizingSupplier.get();
 long expiredTime = expiringMemoizingSupplier.get();
 long newTime = System.nanoTime();
 assertEquals(initialTime, expiringMemoizingSupplier.get());
 assertEquals(newTime, expiringMemoizingSupplier.get());
 assertNotEquals(initialTime, expiredTime);
 }

 // Test for ofInstance method
 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance("Test Instance");
 String expected = "Test Instance";
 assertEquals(expected, supplier.get());
 }

 // Test for synchronizedSupplier method
 @Test
 public void testSynchronizedSupplier() {
 Supplier<Integer> supplier = () -> 1;
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
 Supplier<Integer> supplier1 = Suppliers.synchronizedSupplier(supplier);
 assertNotSame(synchronizedSupplier, supplier1);
 assertEquals(1, synchronizedSupplier.get());
 assertEquals(1, synchronizedSupplier.get());
 }

 // Test for compose method with null arguments
 @Test(expected = NullPointerException.class)
 public void testComposeWithNullArguments() {
 Function<Object, Object> function = null;
 Supplier<Object> supplier = () -> null;
 Suppliers.compose(function, supplier);
 }

 // Test for memoize method with null argument
 @Test(expected = NullPointerException.class)
 public void testMemoizeWithNullArgument() {
 Supplier<Object> supplier = null;
 Suppliers.memoize(supplier);
 }

 // Test for memoizeWithExpiration method with null argument
 @Test(expected = NullPointerException.class)
 public void testMemoizeWithExpirationWithNullArgument() {
 Supplier<Object> supplier = null;
 Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);
 }

 // Test for ofInstance method with null argument
 @Test(expected = NullPointerException.class)
 public void testOfInstanceWithNullArgument() {
 Supplier<Object> supplier = Suppliers.ofInstance(null);
 }

 // Test for synchronizedSupplier method with null argument
 @Test(expected = NullPointerException.class)
 public void testSynchronizedSupplierWithNullArgument() {
 Supplier<Object> supplier = null;
 Suppliers.synchronizedSupplier(supplier);
 }
}