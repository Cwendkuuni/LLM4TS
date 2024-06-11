package ipac;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expectedResult = "11111010";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expectedResult = "0101";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.0.1";
        String IPPrefix = "255.255.255.0";
        String expectedOutputText = "Total Range: 192.168.0.1 -- 192.168.0.1\nUsable Range: 192.168.0.1 -- 192.168.0.1\nTotal usable IP Addresses : 1 \nSubnet: 00111010.11011100.00001111.00000101\nBinary Subnet: 00111010.11011100.00001111.00000101\nBroadcast Address: 192.168.0.1\nPrefix: /32\nNetmask: 255.255.255.0\nBinary Netmask: 01111111.11111111.11111111.00000000";
        String outputText = binaryCalculate.IPCalculate(IP, IPPrefix);
        assertEquals(expectedOutputText, outputText);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.0.1";
        String IPPrefix = "255.255.255.0";
        binaryCalculate.calculate(IP, IPPrefix);
        String expectedIPRangeAddress = "192.168.0.1 -- 192.168.0.1";
        String expectedUsableRange = "192.168.0.1 -- 192.168.0.1";
        String expectedTotalIPAddresses = "1";
        String expectedPrefix = "/32";
        assertEquals(expectedIPRangeAddress, binaryCalculate.IPRangeAddress);
        assertEquals(expectedUsableRange, binaryCalculate.usableRange);
        assertEquals(expectedTotalIPAddresses, binaryCalculate.totalIPAddresses.toString());
        assertEquals(expectedPrefix, binaryCalculate.prefix);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "00111010.11011100.00001111.00000101";
        String expectedResult = "00111010110111000000111100000101";
        String result = binaryCalculate.getBinaryIP(binaryIP);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "255.255.255.0";
        String expectedResult = "00111111.00111111.00111111.00000000";
        String result = binaryCalculate.getNetmask(binaryIP);
        assertEquals(expectedResult, result);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidIPPrefixLength() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.0.1";
        String IPPrefix = "255.255.255.256";
        binaryCalculate.IPCalculate(IP, IPPrefix);
    }
}