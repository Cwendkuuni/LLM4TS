
import static org.junit.Assert.*;
import java.util.*;

public class DenseInstanceTest {

   @Test
   public void testConstructorWithValidArguments() {
       double[] attributes = new double[]{1.0, 2.0, 3.0};
       DenseInstance denseInstance = new DenseInstance(attributes);
       // assertions
   }

   @Test(expected = NullPointerException.class)
   public void testConstructorWithNullArguments() {
       new DenseInstance(null);
   }

   @Test(expected = NullPointerException.class)
   public void testConstructorWithNullClassValue() {
       double[] attributes = new double[]{1.0, 2.0, 3.0};
       new DenseInstance(attributes, null);
   }

   @Test
   public void testValueWithValidIndex() {
       double[] attributes = new double[]{1.0, 2.0, 3.0};
       DenseInstance denseInstance = new DenseInstance(attributes);
       double result = denseInstance.value(1);
       // assertions
   }

   @Test(expected = IllegalArgumentException.class)
   public void testValueWithInvalidIndex() {
       double[] attributes = new double[]{1.0, 2.0, 3.0};
       DenseInstance denseInstance = new DenseInstance(attributes);
       denseInstance.value(-1);
   }

   @Test
   public void testContainsKeyWithMinIndex() {
       double[] attributes = new double[]{1.0, 2.0, 3.0};
       DenseInstance denseInstance = new DenseInstance(attributes);
       boolean result = denseInstance.containsKey(0);
       // assertions
   }

   @Test
   public void testContainsKeyWithMaxIndex() {
       double[] attributes = new double[]{1.0, 2.0, 3.0};
       DenseInstance denseInstance = new DenseInstance(attributes);
       boolean result = denseInstance.containsKey(2);
       // assertions
   }

   @Test(expected = IllegalArgumentException.class)
   public void testContainsKeyWithNegativeIndex() {
       double[] attributes = new double[]{1.0, 2.0, 3.0};
       DenseInstance denseInstance = new DenseInstance(attributes);
       denseInstance.containsKey(-1);
   }

   @Test
   public void testPutWithMinIndex() {
       double[] attributes = new double[]{1.0, 2.0, 3.0};
       DenseInstance denseInstance = new DenseInstance(attributes);
       Double oldValue = denseInstance.put(0, 0.5);
       // assertions
   }

   @Test
   public void testPutWithMaxIndex() {
       double[] attributes = new double[]{1.0, 2.0, 3.0};
       DenseInstance denseInstance = new DenseInstance(attributes);
       double oldValue = denseInstance.put(2, 4.5);
       // assertions
   }

   @Test(expected = IndexOutOfBoundsException.class)
   public void testPutWithInvalidIndex() {
       double[] attributes = new double[]{1.0, 2.0, 3.0};
       DenseInstance denseInstance = new DenseInstance(attributes);
       denseInstance.put(-1, 4.5);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testPutWithNullKey() {
       double[] attributes = new double[]{1.0, 2.0, 3.0};
       DenseInstance denseInstance = new DenseInstance(attributes);
       denseInstance.put(null, 4.5);
   }

   // Add more test cases for other methods as needed
}