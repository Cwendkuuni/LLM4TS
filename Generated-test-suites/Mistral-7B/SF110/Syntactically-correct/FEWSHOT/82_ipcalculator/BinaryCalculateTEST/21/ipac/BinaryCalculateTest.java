package ipac.test;

import static org.junit.Assert.*;

import ipac.BinaryCalculate;
import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11111111";
        String num2 = "10101010";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals("1111111110101010", result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11111111";
        String num2 = "10101010";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals("01010101", result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.0.1";
        String IPPrefix = "24";
        String expectedOutput = "Total Range: 192.168.0.1 -- 192.168.0.255\nUsable Range: 192.168.0.2 -- 192.168.0.254\nTotal usable IP Addresses : 254\nSubnet: 11001100.11111111.00000000.00001111\nBinary Subnet: 11111111.11111111.00000000.00111111\nBroadcast Address: 192.168.0.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.00000000.00000000\n";
        String result = binaryCalculate.IPCalculate(IP, IPPrefix);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "11111111.11111111.00000000.00111111";
        String expectedOutput = "11111111111111110000000000111111";
        String result = binaryCalculate.getBinaryIP(binaryIP);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.0.1";
        String IPPrefix = "24";
        binaryCalculate.calculate(IP, IPPrefix);
        String expectedOutput = "Total Range: 192.168.0.1 -- 192.168.0.255\nUsable Range: 192.168.0.2 -- 192.168.0.254\nTotal usable IP Addresses : 254\n";
        String result = binaryCalculate.IPRangeAddress;
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testConvert() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "11111111.11111111.00000000.00111111";
        String expectedOutput = "192.168.0.255";
        String result = binaryCalculate.convert(binaryIP);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testConvertToBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.0.1";
        String expectedOutput = "11100110.11111100.00001111.00010111";
        String result = binaryCalculate.convertToBinary(IP);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testGetStartIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IPRangeAddress = "192.168.0.1 -- 192.168.0.2";
        String expectedOutput = "192.168.0.1";
        String result = binaryCalculate.getStartIP();
        assertEquals(expectedOutput, result);
    }

    @Test(expected = OutOfMemoryError.class)
    public void testPrefixInPrefixCalculateWithOutOfMemoryError() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.0.1";
        String IPPrefix = "256";
        int noPrefix = 1024;
        String endPrefix = "256";
        binaryCalculate.prefixInPrefixCalculate(IP, IPPrefix, noPrefix, endPrefix);
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.0.1";
        String IPPrefix = "24";
        int noPrefix = 5;
        String endPrefix = "32";
        binaryCalculate.prefixInPrefixCalculate(IP, IPPrefix, noPrefix, endPrefix);
    }
}