package com.google.common.base;

import static org.junit.Assert.*;

import java.util.concurrent.*;

import org.junit.*;

@SuppressWarnings("unchecked")
public class SuppliersTest {

	@Test
	public void testCompose() {
		Function<Integer, String> function = new Function<Integer, String>() {
			@Override
			public String apply(Integer input) {
				return "Function result: " + input;
			}
		};
		Supplier<Integer> supplier = Suppliers.ofInstance(42);
		Supplier<String> composedSupplier = Suppliers.compose(function, supplier);
		String result = composedSupplier.get();
		assertEquals("Function result: 42", result);
	}

	@Test
	public void testMemoize() {
		Supplier<Integer> supplier = new Supplier<Integer>() {
			private int counter = 0;
			@Override
			public Integer get() {
				counter++;
				return counter;
			}
		};
		Supplier<Integer> memoizedSupplier = Suppliers.memoize(supplier);
		assertEquals(1, memoizedSupplier.get().intValue());
		assertEquals(1, memoizedSupplier.get().intValue());
	}

	@Test
	public void testMemoizeWithExpiration() {
		Supplier<Long> supplier = new Supplier<Long>() {
			@Override
			public Long get() {
				return System.nanoTime();
			}
		};
		Supplier<Long> memoizedSupplierWithExpiration = Suppliers.memoizeWithExpiration(supplier, 1, TimeUnit.SECONDS);
		long firstResult = memoizedSupplierWithExpiration.get();
		long secondResult = memoizedSupplierWithExpiration.get();
		assertEquals(firstResult, secondResult);
		// Wait for some time to expire
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Long thirdResult = memoizedSupplierWithExpiration.get();
		assertNotSame(firstResult, thirdResult);
	}

	@Test
	public void testOfInstance() {
		String instance = "Instance";
		Supplier<String> supplier = Suppliers.ofInstance(instance);
		String result = supplier.get();
		assertEquals(instance, result);
	}

	@Test
	public void testSynchronizedSupplier() {
		Supplier<Integer> supplier = new Supplier<Integer>() {
			private int counter = 0;
			@Override
			public Integer get() {
				counter++;
				return counter;
			}
		};
		Supplier<Integer> synchronizedSupplier = Suppliers.synchronizedSupplier(supplier);
		Thread thread1 = new Thread(() -> {
			for (int i = 0; i < 1000; i++) {
				assertEquals(i + 1, synchronizedSupplier.get().intValue());
			}
		});
		Thread thread2 = new Thread(() -> {
			for (int i = 0; i < 1000; i++) {
				assertEquals(i + 1, synchronizedSupplier.get().intValue());
			}
		});
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