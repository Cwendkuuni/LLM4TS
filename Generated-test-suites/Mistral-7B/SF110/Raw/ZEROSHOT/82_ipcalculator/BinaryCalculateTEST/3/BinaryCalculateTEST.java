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

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "0101";
        String actual = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, actual);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.1 -- 192.168.1.254\nTotal usable IP Addresses : 254\nSubnet: 11000010.11111100.01101100.00001111\nBinary Subnet: 11000010.11111100.01101100.00001111\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 01111111.11111111.11111111.00000000";
        String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String expected = "11000010.11111100.01101100.00001111";
        String actual = binaryCalculate.getBinaryIP(ip);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "255.255.255.0";
        String expected = "00111111.11111111.11111111.00000000";
        String actual = binaryCalculate.getNetmask(ip);
        assertEquals(expected, actual);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedIPRangeAddress = "192.168.1.1 -- 192.168.1.255";
        String expectedUsableRange = "192.168.1.1 -- 192.168.1.254";
        String expectedPrefix = "/24";
        binaryCalculate.calculate(ip, ipPrefix);
        assertEquals(expectedIPRangeAddress, binaryCalculate.IPRangeAddress);
        assertEquals(expectedUsableRange, binaryCalculate.usableRange);
        assertEquals(expectedPrefix, binaryCalculate.prefix);
    }
}