import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1111";
        String expected = "11111";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        // Test edge cases
        String num1Zeroes = "0000";
        String num2Zeroes = "0000";
        String expectedZeroes = "00000";
        assertEquals(expectedZeroes, binaryCalculate.addBinary(num1Zeroes, num2Zeroes));

        String num1Ones = "1111";
        String num2Ones = "1111";
        String expectedOnes = "11111";
        assertEquals(expectedOnes, binaryCalculate.addBinary(num1Ones, num2Ones));

        String num1Overflow = "11111111111111111111111111111111";
        String num2 = "1";
        String expectedOverflow = "10000000000000000000000000000001";
        assertEquals(expectedOverflow, binaryCalculate.addBinary(num1Overflow, num2));
    }
}