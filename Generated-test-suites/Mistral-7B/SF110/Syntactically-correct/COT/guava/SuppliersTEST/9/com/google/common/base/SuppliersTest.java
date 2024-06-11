import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;
import com.google.common.base.*;
import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SuppliersTest {

 private Function<? super Object, Object> functionMock;
 private Supplier<Object> supplierMock;
 private Supplier<Object> memoizedSupplier;
 private Supplier<Object> memoizedWithExpirationSupplier;
 private Supplier<Object> supplierOfInstance;
 private Supplier<Object> synchronizedSupplier;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 this.functionMock = Mockito.mock(Function.class);
 this.supplierMock = Mockito.mock(Supplier.class);
 this.memoizedSupplier = Suppliers.memoize(this.supplierMock);
 this.memoizedWithExpirationSupplier = Suppliers.memoizeWithExpiration(this.supplierMock, 10, TimeUnit.MILLISECONDS);
 this.supplierOfInstance = Suppliers.ofInstance(new Object());
 this.synchronizedSupplier = Suppliers.synchronizedSupplier(this.supplierMock);
 }

 @Test
 public void testCompose() {
 Object output = Suppliers.compose(this.functionMock, this.supplierMock).get();
 Mockito.verify(this.functionMock).apply(this.supplierMock);
 assertEquals(output, this.functionMock.apply(this.supplierMock.get()));
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullFunction() {
 Suppliers.compose(null, this.supplierMock);
 }

 @Test(expected = NullPointerException.class)
 public void testComposeWithNullSupplier() {
 Suppliers.compose(this.functionMock, null);
 }

 @Test
 public void testMemoize() {
 Object output = this.memoizedSupplier.get();
 Mockito.verify(this.supplierMock, Mockito.times(1)).get();
 assertEquals(output, this.memoizedSupplier.get());
 }

 @Test
 public void testMemoizeWithExpiration() {
 Object firstOutput = this.memoizedWithExpirationSupplier.get();
 Object secondOutput = this.memoizedWithExpirationSupplier.get();
 Mockito.verify(this.supplierMock, Mockito.times(1)).get();
 assertEquals(firstOutput, secondOutput);
 try {
 Thread.sleep(15);
 this.memoizedWithExpirationSupplier.get();
 fail("Expected exception");
 } catch (Exception e) {
 // expected
 }
 }

 @Test(expected = NullPointerException.class)
 public void testMemoizeWithNullSupplier() {
 Suppliers.memoize(null);
 }

 @Test
 public void testOfInstance() {
 Object output = this.supplierOfInstance.get();
 assertEquals(output, this.supplierOfInstance.instance);
 }

 @Test
 public void testSynchronizedSupplier() {
 Object output1 = this.synchronizedSupplier.get();
 Object output2 = this.synchronizedSupplier.get();
 assertEquals(output1, output2);
 Thread thread = new Thread(() -> {
 this.synchronizedSupplier.get();
 });
 thread.start();
 try {
 thread.join();
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 }

 @Test(expected = NullPointerException.class)
 public void testSynchronizedSupplierWithNullSupplier() {
 Suppliers.synchronizedSupplier(null);
 }

}