package ipac;
import ipac.BinaryCalculate;
import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {
    
    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "11111010";
        String actual = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, actual);
    }
}