import org.junit.Test;
import static org.junit.Assert.*;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "11110101";
        String result = binaryCalculate.addBinary(num1, num2);
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
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11000010.11110000.01101100.00000101\nBinary Subnet: 11000010.11110000.01101100.00001010\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 01111111.11111111.11111111.00000000";
        String result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "11110111.11111100.01101100.00001010";
        String expected = "11110111111111000110110000001010";
        String result = binaryCalculate.getBinaryIP(binaryIP);
        assertEquals(expected, result);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "255.255.255.0";
        String expected = "00000000.11111111.11111111.00000000";
        String result = binaryCalculate.getNetmask(binaryIP);
        assertEquals(expected, result);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String subnet = "192.168.1.1";
        String noOfIPAddresses = "254";
        binaryCalculate.calculate(subnet, noOfIPAddresses);
        String expectedTotalRange = "192.168.1.1 -- 192.168.1.255";
        String expectedUsableRange = "192.168.1.2 -- 192.168.1.254";
        String expectedTotalIPAddresses = "254";
        String expectedPrefix = "/24";
        assertEquals(expectedTotalRange, binaryCalculate.IPRangeAddress);
        assertEquals(expectedUsableRange, binaryCalculate.usableRange);
        assertEquals(expectedTotalIPAddresses, binaryCalculate.totalIPAddresses.toString());
        assertEquals(expectedPrefix, binaryCalculate.prefix);
    }
}