package ipac;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

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
        String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254 \n" +
                "Subnet: 11000010.11111100.01101100.00001111 \n" +
                "Binary Subnet: 11111111.11111100.01110110.00001111 \n" +
                "Broadcast Address: 192.168.1.255 \n" +
                "Prefix: /24 \n" +
                "Netmask: 255.255.255.0 \n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000";
        String actualOutputText = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutputText, actualOutputText);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "11111111.11111100.01101100.00001111";
        String expected = "11111111111111000110110000001111";
        String actual = binaryCalculate.getBinaryIP(binaryIP);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "11111111.11111100.01101100.00001111";
        String expected = "00000001.00001111.10010110.11110000";
        String actual = binaryCalculate.getNetmask(binaryIP);
        assertEquals(expected, actual);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String subnet = "11111111.11111100.01101100.00001111";
        String ipPrefix = "24";
        binaryCalculate.calculate(subnet, ipPrefix);
        String expectedIPRangeAddress = "192.168.1.1 -- 192.168.1.255";
        String expectedUsableRange = "192.168.1.2 -- 192.168.1.254";
        BigInteger expectedTotalIPAddresses = new BigInteger("256", 2).subtract(new BigInteger("4", 2));
        String expectedPrefix = "/24";
        assertEquals(expectedIPRangeAddress, binaryCalculate.IPRangeAddress);
        assertEquals(expectedUsableRange, binaryCalculate.usableRange);
        assertEquals(expectedTotalIPAddresses.toString(), binaryCalculate.totalIPAddresses.toString());
        assertEquals(expectedPrefix, binaryCalculate.prefix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckIPTypeWithInvalidIPLength() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.checkIPType(-1);
    }

    @Test
    public void testConvert() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "11111111111111000110110000001111";
        String expectedIP = "255.255.252.240";
        String actualIP = binaryCalculate.convert(binaryIP);
        assertEquals(expectedIP, actualIP);
    }

    @Test
    public void testConvertToBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String expected = "11110010.11111100.01101100.00001111";
        String actual = binaryCalculate.convertToBinary(ip);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStartIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ipRangeAddress = "192.168.1.1 -- 192.168.1.255";
        String expected = "192.168.1.1";
        String actual = binaryCalculate.getStartIP();
        assertEquals(expected, actual);
    }
}