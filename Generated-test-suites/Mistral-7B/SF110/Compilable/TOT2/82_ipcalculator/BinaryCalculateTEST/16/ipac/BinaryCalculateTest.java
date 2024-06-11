package ipac;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("1111", "1111");
        assertEquals("11111111", result);
    }
}