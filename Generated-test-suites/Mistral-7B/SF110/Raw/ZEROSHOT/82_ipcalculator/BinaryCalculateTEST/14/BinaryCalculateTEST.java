package ipac;

import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1110";
        String expectedSum = "11110";
        String actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1110";
        String expectedDifference = "0011";
        String actualDifference = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDifference, actualDifference);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        binaryCalculate.IPCalculate(ip, ipPrefix);
        String expectedIPRangeAddress = "192.168.1.1 -- 192.168.1.255";
        String expectedUsableRange = "192.168.1.2 -- 192.168.1.254";
        String actualIPRangeAddress = binaryCalculate.IPRangeAddress;
        String actualUsableRange = binaryCalculate.usableRange;
        assertEquals(expectedIPRangeAddress, actualIPRangeAddress);
        assertEquals(expectedUsableRange, actualUsableRange);
        binaryCalculate.reset(); // Reset the object's state for the next test

        ip = "255.255.255.0";
        ipPrefix = "24";
        binaryCalculate.IPCalculate(ip, ipPrefix);
        expectedIPRangeAddress = "255.255.255.0 -- 255.255.255.255";
        expectedUsableRange = "0.0.0.1 -- 255.255.255.254";
        actualIPRangeAddress = binaryCalculate.IPRangeAddress;
        actualUsableRange = binaryCalculate.usableRange;
        assertEquals(expectedIPRangeAddress, actualIPRangeAddress);
        assertEquals(expectedUsableRange, actualUsableRange);
        binaryCalculate.reset(); // Reset the object's state for the next test

        ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        ipPrefix = "60";
        binaryCalculate.IPCalculate(ip, ipPrefix);
        expectedIPRangeAddress = "2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFF:FFFF:FFFF";
        expectedUsableRange = "2001:0db8:85a3:0000:0000:8a2e:0370:7333 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFF:FFFF:FFFF:FFF";
        actualIPRangeAddress = binaryCalculate.IPRangeAddress;
        actualUsableRange = binaryCalculate.usableRange;
        assertEquals(expectedIPRangeAddress, actualIPRangeAddress);
        assertEquals(expectedUsableRange, actualUsableRange);
    }
}