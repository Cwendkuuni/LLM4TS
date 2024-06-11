package ipac.test;

import static org.junit.Assert.*;

import ipac.BinaryCalculate;
import java.math.BigInteger;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11110110";
        String num2 = "11010111";
        String expectedSum = "11101111";
        String sum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, sum);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11111111";
        String num2 = "11111101";
        String expectedDiff = "00000110";
        String diff = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDiff, diff);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String outputText = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertTrue(outputText.contains("Total Range: 192.168.1.1 -- 192.168.1.255"));
        assertTrue(outputText.contains("Usable Range: 192.168.1.2 -- 192.168.1.254"));
        BigInteger expectedTotalIPAddresses = new BigInteger("256", 10);
        BigInteger totalIPAddresses = new BigInteger(binaryCalculate.getTotalIPAddressesFromOutputText(outputText));
        assertEquals(expectedTotalIPAddresses, totalIPAddresses);
    }

    @Test
    public void testGetTotalIPAddresses() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String outputText = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254";
        BigInteger totalIPAddresses = binaryCalculate.getTotalIPAddressesFromOutputText(outputText);
        BigInteger expectedTotalIPAddresses = new BigInteger("256", 10);
        assertEquals(expectedTotalIPAddresses, totalIPAddresses);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String subnet = "192.168.1.1";
        String noOfIPAddresses = "1";
        binaryCalculate.calculate(subnet, noOfIPAddresses);
        String expectedIPRangeAddress = "192.168.1.1 -- 192.168.1.1";
        String ipRangeAddress = binaryCalculate.getIPRangeAddress();
        assertEquals(expectedIPRangeAddress, ipRangeAddress);
        String expectedUsableRange = "192.168.1.1 -- 192.168.1.1";
        String usableRange = binaryCalculate.getUsableRange();
        assertEquals(expectedUsableRange, usableRange);
        BigInteger expectedTotalIPAddresses = new BigInteger("2", 10);
        BigInteger totalIPAddresses = binaryCalculate.getTotalIPAddresses();
        assertEquals(expectedTotalIPAddresses, totalIPAddresses);
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        int noPrefix = 5;
        String endPrefix = "32";
        binaryCalculate.prefixInPrefixCalculate(ip, ipPrefix, noPrefix, endPrefix);
        String expectedOutput1 = "Subnet: 192.168.1.0\nBinary Subnet: 11101110.00001111.01101100.00000001\nBroadcast Address: 192.168.1.255\nPrefix: 32";
        String expectedOutput2 = "Total usable IP Addresses :254";
        String output1 = binaryCalculate.getOutput1();
        String output2 = binaryCalculate.getOutput2();
        assertEquals(expectedOutput1, output1);
        assertEquals(expectedOutput2, output2);
    }
}