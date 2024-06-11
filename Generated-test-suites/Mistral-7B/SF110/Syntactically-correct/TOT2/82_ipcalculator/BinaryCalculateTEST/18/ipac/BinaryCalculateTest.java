import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1111";
        String expected = "11111";

        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "0000";
        num2 = "0000";
        expected = "0000";

        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "1111";
        num2 = "0000";
        expected = "1111";

        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "0000";
        num2 = "1111";
        expected = "1111";

        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "1111";
        num2 = "1111";
        expected = "11110";

        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);
    }
}