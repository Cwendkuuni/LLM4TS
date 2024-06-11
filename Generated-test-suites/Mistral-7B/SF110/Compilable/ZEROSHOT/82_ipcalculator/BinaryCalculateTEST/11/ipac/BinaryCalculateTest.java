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
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254 \n" +
                "Subnet: 11000010.11111100.01101100.00001111\n" +
                "Binary Subnet: 11111010.11111100.01101100.00001111\n" +
                "Broadcast Address: 11111111.11111100.01101100.00001111\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 01111111.11111111.11111111.00000000\n";
        String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String subnet = "192.168.1.0";
        String noOfIPAddresses = "256";
        binaryCalculate.calculate(subnet, noOfIPAddresses);
        String expectedIPRangeAddress = "192.168.1.0 -- 192.168.1.255";
        String expectedUsableRange = "192.168.1.1 -- 192.168.1.254";
        String expectedPrefix = "/24";
        String expectedTotalIPAddresses = "256";
        assertEquals(expectedIPRangeAddress, binaryCalculate.IPRangeAddress);
        assertEquals(expectedUsableRange, binaryCalculate.usableRange);
        assertEquals(expectedPrefix, binaryCalculate.prefix);
        assertEquals(expectedTotalIPAddresses, binaryCalculate.totalIPAddresses.toString());
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.0";
        String ipPrefix = "24";
        int noPrefix = 5;
        String endPrefix = "32";
        binaryCalculate.prefixInPrefixCalculate(ip, ipPrefix, noPrefix, endPrefix);
        // Verify the output contains the expected substrings
        // Add assertions here based on the expected output
    }

    @Test(expected = OutOfMemoryError.class)
    public void testPrefixInPrefixCalculate_OutOfMemoryError() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.0";
        String ipPrefix = "24";
        int noPrefix = Integer.MAX_VALUE;
        String endPrefix = "32";
        binaryCalculate.prefixInPrefixCalculate(ip, ipPrefix, noPrefix, endPrefix);
    }
}