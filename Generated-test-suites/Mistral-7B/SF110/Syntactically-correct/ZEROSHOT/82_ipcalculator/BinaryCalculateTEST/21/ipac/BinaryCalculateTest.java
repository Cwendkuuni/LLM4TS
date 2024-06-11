import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "11010";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "0111";
        num2 = "1011";
        expected = "11111";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "1111";
        num2 = "1111";
        expected = "11111";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "0101";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "1000";
        num2 = "1111";
        expected = "00011111";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "1111";
        num2 = "1111";
        expected = "0000";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "255.255.255.0";
        binaryCalculate.IPCalculate(ip, ipPrefix);

        String expectedIPRangeAddress = "192.168.1.1 -- 192.168.1.255";
        String actualIPRangeAddress = binaryCalculate.IPRangeAddress;
        assertEquals(expectedIPRangeAddress, actualIPRangeAddress);

        expectedIPRangeAddress = "255.255.255.255 -- 255.255.255.255";
        ipPrefix = "255.255.255.255";
        binaryCalculate.IPCalculate(ip, ipPrefix);
        actualIPRangeAddress = binaryCalculate.IPRangeAddress;
        assertEquals(expectedIPRangeAddress, actualIPRangeAddress);
    }
}