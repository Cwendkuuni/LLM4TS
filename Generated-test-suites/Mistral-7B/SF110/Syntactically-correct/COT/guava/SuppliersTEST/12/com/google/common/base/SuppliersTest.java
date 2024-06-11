import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static com.google.common.base.Preconditions.*;
import com.google.common.base.Function;
import com.google.common.base.Supplier;

@RunWith(MockitoJUnitRunner.class)
public class SuppliersTest {

 private Function<Integer, Integer> addOneFunction;
 private Supplier<Integer> constantSupplierOfFive;
 private Supplier<Integer> supplierUnderTest;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 addOneFunction = Mockito.mock(Function.class);
 constantSupplierOfFive = Suppliers.ofInstance(5);
 supplierUnderTest = Suppliers.compose(addOneFunction, constantSupplierOfFive);
 }

 @Test
 public void testComposeFunctionAndSupplier() {
 final int expected = 6;
 Mockito.when(addOneFunction.apply(Mockito.any(Integer.class))).thenReturn(expected);
 final int result = supplierUnderTest.get();
 assertEquals(expected, result);
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullFunction() {
 supplierUnderTest = Suppliers.compose(null, constantSupplierOfFive);
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullSupplier() {
 supplierUnderTest = Suppliers.compose(addOneFunction, null);
 }

 @Test
 public void testMemoizeSupplier() {
 final MemoizingSupplier<Integer> memoizedSupplier = Suppliers.memoize(constantSupplierOfFive);
 final int result1 = memoizedSupplier.get();
 final int result2 = memoizedSupplier.get();
 assertEquals(result1, result2);
 }

 @Test
 public void testMemoizeWithExpirationSupplier() {
 final ExpiringMemoizingSupplier<Integer> expiringMemoizingSupplier = Suppliers.memoizeWithExpiration(constantSupplierOfFive, 10, TimeUnit.MILLISECONDS);
 final int result1 = expiringMemoizingSupplier.get();
 final int result2 = expiringMemoizingSupplier.get();
 assertNotSame(result1, result2);
 }

 @Test(expected = NullPointerException.class)
 public void testMemoizeWithNullSupplier() {
 Suppliers.memoize(null);
 }

 @Test
 public void testOfInstanceNull() {
 final Supplier<Integer> supplier = Suppliers.ofInstance(null);
 assertNull(supplier);
 }

 @Test
 public void testOfInstanceNotNull() {
 final Supplier<Integer> supplier = Suppliers.ofInstance(5);
 final int result = supplier.get();
 assertEquals(5, result);
 }

 @Test
 public void testSynchronizedSupplier() {
 final Supplier<Integer> supplier = Suppliers.synchronizedSupplier(constantSupplierOfFive);
 final Thread thread1 = new Thread(() -> supplier.get());
 final Thread thread2 = new Thread(() -> supplier.get());
 thread1.start();
 thread2.start();
 try {
 thread1.join();
 thread2.join();
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 final int result1 = supplier.get();
 final int result2 = supplier.get();
 assertEquals(result1, result2);
 }

 @Test(expected = NullPointerException.class)
 public void testSynchronizedSupplierWithNullSupplier() {
 Suppliers.synchronizedSupplier(null);
 }
}