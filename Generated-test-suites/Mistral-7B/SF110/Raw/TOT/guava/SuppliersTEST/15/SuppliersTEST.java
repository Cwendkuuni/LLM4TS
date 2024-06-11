package com.google.common.base.test;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.base.Preconditions;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SuppliersTest {

 private Supplier<String> supplier1;
 private Supplier<Integer> supplier2;
 private Function<Supplier<String>, String> function;

 @Before
 public void setUp() {
 supplier1 = of("Hello");
 supplier2 = of(1);
 function = (Supplier<String> s) -> s.get();
 }

 @Test
 public void testCompose() {
 // Test typical use case
 Supplier<String> composed = Suppliers.compose(function, supplier1);
 assertEquals("Hello", composed.get());

 // Test edge case: null function
 assertNull(Suppliers.compose(null, supplier1));

 // Test error scenario: null supplier
 assertNull(Suppliers.compose(function, null));

 // Test typical use case with mocked function and supplier
 Supplier<String> mockedFunction = mock(Function.class);
 when(mockedFunction.apply((Supplier<String>)any())).thenReturn("Mocked String");
 Supplier<String> composedMocked = Suppliers.compose(mockedFunction, supplier1);
 assertEquals("Mocked String", composedMocked.get());
 }

 @Test
 public void testMemoize() {
 // Test typical use case
 Supplier<Integer> supplierToMemoize = () -> 1;
 Supplier<Integer> memoized = Suppliers.memoize(supplierToMemoize);
 assertEquals(1, memoized.get());
 assertEquals(1, memoized.get());

 // Test edge case: memoized supplier is already an instance of MemoizingSupplier
 Supplier<Integer> memoizedMemoized = Suppliers.memoize(memoized);
 assertEquals(1, memoizedMemoized.get());

 // Test error scenario: null supplier
 assertNull(Suppliers.memoize(null));
 }

 @Test
 public void testMemoizeWithExpiration() {
 // Test typical use case
 Supplier<String> supplierToExpire = () -> "Expired String";
 Supplier<String> expiringMemoized = Suppliers.memoizeWithExpiration(supplierToExpire, 10, TimeUnit.MILLISECONDS);
 assertEquals("Expired String", expiringMemoized.get());
 assertEquals("Expired String", expiringMemoized.get());

 // Wait for expiration
 try {
 Thread.sleep(15);
 } catch (InterruptedException e) {
 throw new RuntimeException(e);
 }

 assertEquals("Expired String", expiringMemoized.get());
 assertNotEquals("Expired String", expiringMemoized.get());
 }

 @Test
 public void testOfInstance() {
 // Test typical use case
 Supplier<String> instanceSupplier = Suppliers.ofInstance("Test String");
 assertEquals("Test String", instanceSupplier.get());

 // Test null instance
 assertNull(Suppliers.ofInstance(null));
 }

 @Test
 public void testSynchronizedSupplier() {
 // Test typical use case
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(() -> 1);
 assertEquals(1, synchronizedSupplier.get());
 assertEquals(1, synchronizedSupplier.get());

 // Test concurrent access
 Thread thread1 = new Thread(() -> assertEquals(1, synchronizedSupplier.get()));
 Thread thread2 = new Thread(() -> assertEquals(1, synchronizedSupplier.get()));
 thread1.start();
 thread2.start();
 try {
 thread1.join();
 thread2.join();
 } catch (InterruptedException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testSupplierFunction() {
 // Test typical use case
 Supplier<String> supplierToConvert = Suppliers.ofInstance("Test String");
 Function<Supplier<String>, String> functionToConvert = Suppliers.supplierFunction();
 assertEquals("Test String", functionToConvert.apply(supplierToConvert));
 }

}