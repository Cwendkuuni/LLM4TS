package ipac;
import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {
    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1101";
        String num2 = "1010";
        String expectedSum = "10110";
        assertEquals(expectedSum, binaryCalculate.addBinary(num1, num2));

        num1 = "11111111";
        num2 = "11111111";
        expectedSum = "1111111111";
        assertEquals(expectedSum, binaryCalculate.addBinary(num1, num2));

        num1 = "00000000";
        num2 = "00000000";
        String expectedZero = "00000000";
        assertEquals(expectedZero, binaryCalculate.addBinary(num1, num2));

        num1 = "11111111";
        num2 = "nonBinary";
        try {
            binaryCalculate.addBinary(num1, num2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}