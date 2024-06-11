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
        String ip = "192.168.0.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.0.1 -- 192.168.0.255\nUsable Range: 192.168.0.1 -- 192.168.0.254\nTotal usable IP Addresses : 254\nSubnet: 11000000.11111100.00000000.00111101\nBinary Subnet: 11000000.11111100.00000000.00111101\nBroadcast Address: 11000000.11111100.00000000.01011111\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 01111111.11111111.11111111.00000000";
        String result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.0.1";
        String binaryIp = binaryCalculate.getBinaryIP(ip);
        String expected = "11000000.11111100.00000000.00111101";
        assertEquals(expected, binaryIp);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ipPrefix = "24";
        String netmask = binaryCalculate.getNetmask(ipPrefix);
        String expected = "11111100.00000000.00000000.00000000";
        assertEquals(expected, netmask);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        binaryCalculate.calculate(ip, ipPrefix);
        String expectedIPRangeAddress = "192.168.1.1 -- 192.168.1.255";
        String resultIPRangeAddress = binaryCalculate.IPRangeAddress;
        assertEquals(expectedIPRangeAddress, resultIPRangeAddress);

        String expectedUsableRange = "192.168.1.1 -- 192.168.1.254";
        String resultUsableRange = binaryCalculate.usableRange;
        assertEquals(expectedUsableRange, resultUsableRange);

        BigInteger expectedTotalIPAddresses = new BigInteger("254");
        BigInteger resultTotalIPAddresses = binaryCalculate.totalIPAddresses;
        assertEquals(expectedTotalIPAddresses, resultTotalIPAddresses);
    }

    @Test (expected = AssertionError.class)
    public void testInvalidIPPrefixLength() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.0.1";
        String ipPrefix = "33";
        binaryCalculate.IPCalculate(ip, ipPrefix);
    }
}