
package com.google.common.base;

import org.junit.Test;
import static org.junit.Assert.*;;

public class SuppliersTest {
    @Test
    public void testComposeFunctionApplication() {
        Function<Integer, Integer> addOne = i -> i + 1;
        Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);
        Supplier<Integer> composedSupplier = Suppliers.compose(addOne, supplierOfFive);
        Integer result = composedSupplier.get();
        assertEquals(6, result);
    }
    
    @Test
    public void testMemoizeCaching() {
        Supplier<Integer> supplierOfFive = Suppliers.ofInstance(5);
        Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplierOfFive);
        Integer firstResult = memoizedSupplier.get();
        Integer secondResult = memoizedSupplier.get();
        assertEquals(firstResult, secondResult);
    }
    
    @Test
    public void testMemoizeWithExpiration() {
        Supplier<Long> supplierOfCurrentTime = Suppliers.memoizeWithExpiration(() -> System.nanoTime(), 1_000_000_000L, TimeUnit.NANOS);
        Long firstResult = supplierOfCurrentTime.get();
        Long secondResult = supplierOfCurrentTime.get();
        assertEquals(firstResult, secondResult);

        Thread.sleep(2_000); // Wait for 2 seconds
        Long thirdResult = supplierOfCurrentTime.get();
        Long fourthResult = supplierOfCurrentTime.get();
        assertEquals(firstResult, thirdResult); // The supplier should not have changed
        assertEquals(firstResult, fourthResult);
    }
    
    @Test(expected = NullPointerException.class)
    public void testMemoizeNullSupplier() {
        Suppliers.memoize(null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testMemoizeFunctionNull() {
        Function<Object, Object> nullFunction = null;
        Suppliers.memoize((Supplier<Object>)nullFunction);
    }
    
    @Test(expected = NullPointerException.class)
    public void testMemoizeWithExpirationNullSupplier() {
        Suppliers.memoizeWithExpiration(null, 1, TimeUnit.MILLISECONDS);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMemoizeWithNegativeDuration() {
        Suppliers.memoizeWithExpiration(() -> 1L, -1, TimeUnit.MILLISECONDS);
    }
    
    @Test
    public void testMemoizeWithExpirationExceptionSupplier() {
        Supplier<Integer> supplier = () -> {
            throw new RuntimeException("Expected Exception");
        };
        try {
            Suppliers.memoizeWithExpiration(supplier, 1000, TimeUnit.MILLISECONDS);
            fail("Expecting an exception");
        } catch (RuntimeException e) {
            // Expected
        }
    }
    
    @Test
    public void testMemoizeWithExpirationOutputExpired() {
        Supplier<Long> supplierOfExpiredTime = Suppliers.memoizeWithExpiration(() -> System.nanoTime() - 1_000_000_001L, 1_000_000_000L, TimeUnit.NANOS);
        Long firstResult = supplierOfExpiredTime.get();
        Long secondResult = supplierOfExpiredTime.get();
        assertNotEquals(firstResult, secondResult);
    }
}
