import org.junit.Test;
import static org.junit.Assert.*;

public class BigIntegerMathTest {
   // Test for positive integers that are powers of two
   @Test
   public void testIsPowerOfTwoPositive() {
      BigInteger number = BigInteger.valueOf(1);
      assertTrue(BigIntegerMath.isPowerOfTwo(number));

      for (int i = 0; i < 10; i++) {
         number = number.shiftLeft(1);
         assertTrue(BigIntegerMath.isPowerOfTwo(number));
      }
   }

   // Test for negative integers
   @Test
   public void testIsPowerOfTwoNegative() {
      BigInteger number = BigInteger.valueOf(-1);
      assertFalse(BigIntegerMath.isPowerOfTwo(number));
   }

   // Test for integers that are not powers of two
   @Test
   public void testIsPowerOfTwoNotPowerOfTwo() {
      BigInteger number = BigInteger.valueOf(3);
      assertFalse(BigIntegerMath.isPowerOfTwo(number));
   }
}