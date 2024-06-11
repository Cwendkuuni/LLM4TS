package ipac;
import ipac.BinaryCalculate;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BinaryCalculateTest {
    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1111";
        String sum = bc.addBinary(num1, num2);
        assertEquals("11111111", sum);
    }
}