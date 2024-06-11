package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.*;

@SuppressWarnings("unchecked")
public class SuppliersTest {

 @BeforeClass
 public static void setup() {
 System.out.println("### Starting SuppliersTest ###");
 }

 @AfterClass
 public static void teardown() {
 System.out.println("### Finishing SuppliersTest ###");
 }

 @Test
 public void testCompose() {
 Function<Integer, String> function = new Function<Integer, String>() {
 @Override
 public String apply(Integer input) {
 return "Function output: " + input;
 }
 };
 Supplier<Integer> supplier = Suppliers.ofInstance(42);
 Supplier<String> supplierComposition = Suppliers.compose(function, supplier);
 assertEquals("Function output: 42", supplierComposition.get());
 }

 @Test
 public void testMemoize() {
 Supplier<String> supplier = new Supplier<String>() {
 @Override
 public String get() {
 return "Supplier output";
 }
 };
 Supplier<String> memoizedSupplier = Suppliers.memoize(supplier);
 assertEquals("Supplier output", memoizedSupplier.get());
 assertEquals("Supplier output", memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Supplier<String> supplier = new Supplier<String>() {
 @Override
 public String get() {
 return "Supplier output";
 }
 };
 Supplier<String> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(supplier, 100, TimeUnit.MILLISECONDS);
 assertEquals("Supplier output", expiringMemoizingSupplier.get());
 assertEquals("Supplier output", expiringMemoizingSupplier.get());
 try {
 Thread.sleep(150);
 } catch (InterruptedException e) {
 // Handle exception
 }
 assertEquals("Supplier output", expiringMemoizingSupplier.get()); // This should call the supplier again
 }

 @Test
 public void testOfInstance() {
 Supplier<String> ofInstanceSupplier = Suppliers.ofInstance("Instance output");
 assertEquals("Instance output", ofInstanceSupplier.get());
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<String> supplier = new Supplier<String>() {
 @Override
 public String get() {
 return "Supplier output";
 }
 };
 Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
 assertEquals("Supplier output", synchronizedSupplier.get()); // First call
 assertEquals("Supplier output", synchronizedSupplier.get()); // Second call
 }

 @Test
 public void testSupplierFunction() {
 Supplier<Integer> supplier = Suppliers.ofInstance(42);
 Function<Supplier<Integer>, Integer> supplierFunction = Suppliers.supplierFunction();
 assertEquals(42, supplierFunction.apply(supplier).intValue());
 }
}