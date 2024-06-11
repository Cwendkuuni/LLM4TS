package com.google.common.base.test;

import static org.junit.Assert.*;
import static com.google.common.base.Preconditions.*;

import java.util.concurrent.*;
import org.junit.*;
import org.junit.runner.*;
import com.google.common.base.*;

@RunWith(JUnit4.class)
public class SuppliersTest {

 @SuppressWarnings("unchecked")
 @Test
 public void testCompose() {
 Supplier<String> function = (Supplier<String> function2) -> function2.get() + " World";
 Supplier<String> supplier = Suppliers.ofInstance("Hello");
 Supplier<String> expected = Suppliers.compose(function, supplier);
 String result = expected.get();
 String expectedResult = "Hello World";
 assertEquals(expectedResult, result);
 }

 @Test(expected = NullPointerException.class)
 public void testComposeNullFunction() {
 Function<? super Object, String> function = null;
 Supplier<Object> supplier = Suppliers.ofInstance(" test");
 Supplier<String> expected = Suppliers.compose(function, supplier);
 }

 @Test(expected = NullPointerException.class)
 public void testComposeNullSupplier() {
 Function<? super Object, String> function = (Function<? super Object, String>) () -> " World";
 Supplier<Object> supplier = null;
 Supplier<String> expected = Suppliers.compose(function, supplier);
 }

 @Test(expected = RuntimeException.class)
 public void testComposeRuntimeException() {
 Function<? super Object, String> function = (Function<? super Object, String>) () -> {
 throw new RuntimeException("Test Exception");
 };
 Supplier<Object> supplier = Suppliers.ofInstance(" test");
 Supplier<String> expected = Suppliers.compose(function, supplier);
 }

 @Test
 public void testMemoize() {
 Supplier<Integer> delegate = () -> 42;
 Supplier<Integer> memoized = Suppliers.memoize(delegate);
 Integer result1 = memoized.get();
 Integer result2 = memoized.get();
 assertEquals(result1, result2);
 }

 @Test(expected = NullPointerException.class)
 public void testMemoizeNullDelegate() {
 Supplier<Object> delegate = null;
 Supplier<Object> memoized = Suppliers.memoize(delegate);
 }

 @Test
 public void testMemoizeWithExpiration() {
 long duration = 10;
 TimeUnit unit = TimeUnit.MILLISECONDS;
 Supplier<Long> delegate = () -> System.currentTimeMillis();
 Supplier<Long> memoized = Suppliers.memoizeWithExpiration(delegate, duration, unit);
 Long result1 = memoized.get();
 Long result2 = memoized.get();
 Long result3 = memoized.get();
 assertEquals(result1, result2);
 assertEquals(result1, result3);
 try {
 Thread.sleep(duration + 10);
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 Long result4 = memoized.get();
 assertNotEquals(result1, result4);
 }

 @Test(expected = NullPointerException.class)
 public void testMemoizeWithExpirationNullDelegate() {
 Supplier<Object> delegate = null;
 long duration = 10;
 TimeUnit unit = TimeUnit.MILLISECONDS;
 Supplier<Object> memoized = Suppliers.memoizeWithExpiration(delegate, duration, unit);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMemoizeWithNegativeExpiration() {
 long duration = -10;
 TimeUnit unit = TimeUnit.MILLISECONDS;
 Supplier<Object> delegate = () -> "";
 Supplier<Object> memoized = Suppliers.memoizeWithExpiration(delegate, duration, unit);
 }

 @Test
 public void testOfInstance() {
 Supplier<String> supplier = Suppliers.ofInstance(" test");
 String result = supplier.get();
 assertEquals(" test", result);
 }

 @Test(expected = NullPointerException.class)
 public void testOfInstanceNullInstance() {
 T instance = null;
 Supplier<String> supplier = Suppliers.ofInstance(instance);
 }

 @Test
 public void testSynchronizedSupplier() {
 Supplier<String> delegate = Suppliers.ofInstance(" test");
 Supplier<String> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
 String result1 = synchronizedSupplier.get();
 String result2 = synchronizedSupplier.get();
 assertEquals(result1, result2);
 }

 @Test
 public void testThreadSafety() {
 ExecutorService executor = Executors.newFixedThreadPool(10);
 Supplier<Integer> delegate = () -> new Random().nextInt();
 Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(delegate);
 
 List<Future<Integer>> results = new ArrayList<>();
 for (int i = 0; i < 1000; i++) {
 results.add(executor.submit(() -> synchronizedSupplier.get()));
 }
 
 for (Future<Integer> result : results) {
 try {
 result.get();
 } catch (InterruptedException | ExecutionException e) {
 e.printStackTrace();
 }
 }
 
 int[] uniqueValues = new int[1000];
 int count = 0;
 for (int value : results.stream().map(Future::get).distinct().collect(Collectors.toList())) {
 uniqueValues[count++] = value;
 }
 assertEquals(1000, count);
 }

}