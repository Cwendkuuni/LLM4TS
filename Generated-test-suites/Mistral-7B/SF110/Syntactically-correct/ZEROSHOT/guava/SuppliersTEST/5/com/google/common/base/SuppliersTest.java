package com.google.common.base;

import static org.junit.Assert.*;
import java.util.concurrent.*;

import org.junit.*;
import org.junit.runner.*;

import com.google.common.annotations.*;
import com.google.common.base.*;
import static com.google.common.base.Preconditions.*;

@RunWith(Parameterized.class)
public class SuppliersTest {

 @Parameters(name = "{0}-{1}")
 public static Iterable<Object[]> data() {
 return new Object[][] {
 // Add test cases here with appropriate input and expected output
 };
 }

 @Test
 public void testCompose(@Nullable final Object input) {
 Function<? super Object, Object> function = new Function<Object, Object>() {
 @Override
 public Object apply(Object t) {
 checkState(t != null, "Function input cannot be null");
 return t.toString();
 }
 };
 Supplier<Object> supplier = Suppliers.ofInstance(input);
 Supplier<Object> composed = Suppliers.compose(function, supplier);
 assertEquals(input.toString(), composed.get());
 }

 @Test
 public void testMemoize() {
 Supplier<String> supplier = new Supplier<String>() {
 @Override
 public String get() {
 return "Memoized Supplier";
 }
 };
 Supplier<String> memoized = Suppliers.memoize(supplier);
 assertEquals("Memoized Supplier", memoized.get());
 assertEquals("Memoized Supplier", memoized.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<String> supplier = new Supplier<String>() {
 @Override
 public String get() {
 return "Memoized Supplier";
 }
 };
 Supplier<String> memoizedWithExpiration = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);
 assertEquals("Memoized Supplier", memoizedWithExpiration.get());
 assertEquals("Memoized Supplier", memoizedWithExpiration.get());
 try {
 Thread.sleep(150);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 assertEquals("Memoized Supplier", memoizedWithExpiration.get());
 try {
 Thread.sleep(200);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 assertNotEquals("Memoized Supplier", memoizedWithExpiration.get());
 }

 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance(null);
 assertNull(supplier.get());
 Supplier<String> supplierInstance = Suppliers.ofInstance("Instance Supplier");
 assertEquals("Instance Supplier", supplierInstance.get());
 assertEquals("Instance Supplier", supplierInstance.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<String> supplier = new Supplier<String>() {
 @Override
 public String get() {
 checkState(Thread.currentThread().getName().equals("Thread-0"), "Unexpected thread executing get() method");
 return "Synchronized Supplier";
 }
 };
 Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
 Thread thread1 = new Thread(() -> assertEquals("Synchronized Supplier", synchronizedSupplier.get()));
 Thread thread2 = new Thread(() -> assertEquals("Synchronized Supplier", synchronizedSupplier.get()));
 thread1.start();
 thread2.start();
 try {
 thread1.join();
 thread2.join();
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 }
}