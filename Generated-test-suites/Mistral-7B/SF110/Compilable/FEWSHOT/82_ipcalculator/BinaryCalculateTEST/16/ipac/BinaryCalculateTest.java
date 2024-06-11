package ipac.test;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import ipac.BinaryCalculate;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11110110";
        String num2 = "10101010";
        String expected = "11011110";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11111111";
        String num2 = "10101010";
        String expected = "10010101";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testIPCalculate_IPv4() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11101100.11110100.01101000.00001111\nBinary Subnet: 11101100.11110100.01101000.00001111\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.11111111.00000000";
        String result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutputText, result);
    }

    @Test
    public void testIPCalculate_IPv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String ipPrefix = "60";
        String expectedOutputText = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\nUsable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7332 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7334\nTotal usable IP Addresses : 14\nSubnet: 01111010.11011011.01010101.00000000.00111101.10101101.00110110.11111100\nBinary Subnet: 01111010.11011011.01010101.00000000.00111101.10101101.00110110.11111100\nBroadcast Address: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFE:FFFF\nPrefix: /60\nNetmask: FFFE::\nBinary Netmask: 11111111.11111111.11111111.11111111.01111111.01111111.11111111.11111100";
        String result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutputText, result);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        binaryCalculate.calculate(ip, ipPrefix);
        assertEquals("192.168.1.1", binaryCalculate.currentIP);
        assertEquals("255.255.255.0", binaryCalculate.prefix);
        assertEquals("192.168.1.0", binaryCalculate.IPRangeAddress);
        assertEquals("192.168.1.255", binaryCalculate.usableRange.split(" -- ")[1]);
        BigInteger expectedTotalIPAddresses = new BigInteger("256", 2).subtract(new BigInteger("4"));
        assertEquals(expectedTotalIPAddresses, binaryCalculate.totalIPAddresses);
    }
}