import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BinaryCalculateTest {
    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expectedSum = "11001";
        String actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);
    }
}