import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "11111010";
        String result = bc.addBinary(num1, num2);
        assertEquals(expected, result);
    }

    // Add more test cases here
}