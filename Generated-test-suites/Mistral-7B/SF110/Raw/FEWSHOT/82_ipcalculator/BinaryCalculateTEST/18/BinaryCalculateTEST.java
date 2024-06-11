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
        String expected = "10110";
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
    public void testIPCalculateIPv4() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.1.1";
        String IPPrefix = "255.255.255.0";
        String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254\nSubnet: 11101100.11111111.01110110.00001111\nBinary Subnet: 11101100.11111111.01110110.00001111\nBroadcast Address: 11101100.11111111.01110110.11111111\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.11111111.00000000";
        String resultOutputText = binaryCalculate.IPCalculate(IP, IPPrefix);
        assertEquals(expectedOutputText, resultOutputText);
    }

    @Test
    public void testIPCalculateIPv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String IPPrefix = "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:c000:0000:0200";
        String expectedOutputText = "Total Range: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFC:0000:2000\nUsable Range: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFC:0000:0200\nTotal usable IP Addresses : 65536\nSubnet: 01111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111\nBinary Subnet: 01111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111\nBroadcast Address: 01111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111\nPrefix: /64\nNetmask: ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:c000:0000:0200\nBinary Netmask: 0:0:0:0:0:0:0:0:0:0:0:0:11000000.00000000.00000000.00001111";
        String resultOutputText = binaryCalculate.IPCalculate(IP, IPPrefix);
        assertEquals(expectedOutputText, resultOutputText);
    }

    @Test
    public void testCalculateIPPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.1.1";
        String IPPrefix = "255.255.255.128";
        String endPrefix = "255.255.255.255";
        int noPrefix = 5;
        String[][] expectedOutput = new String[][] {{"192.168.1.1  /27", "192.168.1.0 -- 192.168.1.62"}, {"Total usable IP Addresses : 62"},
                {"Subnet: 11101100.11111111.01110110.00111111"}, {"Binary Subnet: 11101100.11111111.01110110.00111111"},
                {"Broadcast Address: 11101100.11111111.01110110.11111111"}, {"Prefix: /27"}, {"Netmask: 255.255.255.128"},
                {"Binary Netmask: 11111100.00001111.00000000.00111111"}};
        binaryCalculate.prefixInPrefixCalculate(IP, IPPrefix, noPrefix, endPrefix);
        String[][] resultOutput = binaryCalculate.output;
        assertArrayEquals(expectedOutput, resultOutput);
    }
}