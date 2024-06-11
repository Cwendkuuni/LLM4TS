package ipac;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BinaryCalculateTest {
    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1111";
        String expected = "11111111";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);
    }
}