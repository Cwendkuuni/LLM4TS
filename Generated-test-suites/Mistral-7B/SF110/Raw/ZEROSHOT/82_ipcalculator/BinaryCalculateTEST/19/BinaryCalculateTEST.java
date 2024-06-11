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
        String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11000010.11110000.00001001.00000001\nBinary Subnet: 11111100.11111111.00101101.00000101\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.11111111.00000000";
        String result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutputText, result);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "11111111.11111111.00101101.00000101";
        String expected = "11111111111111110010110100001011";
        String result = binaryCalculate.getBinaryIP(binaryIP);
        assertEquals(expected, result);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "255.255.255.0";
        String expected = "00000000.00000000.00001111.00000000";
        String result = binaryCalculate.getNetmask(binaryIP);
        assertEquals(expected, result);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String subnet = "192.168.1.0";
        String noOfIPAddresses = "254";
        binaryCalculate.calculate(subnet, noOfIPAddresses);
        String expectedIPRangeAddress = "192.168.1.0 -- 192.168.1.255";
        String expectedUsableRange = "192.168.1.1 -- 192.168.1.254";
        assertEquals(expectedIPRangeAddress, binaryCalculate.IPRangeAddress);
        assertEquals(expectedUsableRange, binaryCalculate.usableRange);
    }

    @Test (expected = IllegalArgumentException.class)
    public void testCalculateWithInvalidIPPrefix() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String subnet = "192.168.1.0";
        String ipPrefix = "256";
        binaryCalculate.calculate(subnet, ipPrefix);
    }
}