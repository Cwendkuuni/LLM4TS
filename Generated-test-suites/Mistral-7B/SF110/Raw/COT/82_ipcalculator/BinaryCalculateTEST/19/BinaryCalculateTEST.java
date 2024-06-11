package ipac.test;

import static org.junit.Assert.*;

import org.junit.Test;

import ipac.BinaryCalculate;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "11110101";
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
    public void testIPCalculate_IPv4() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254 \n" +
                "Subnet: 11000000.11111100.01101100.00001111\n" +
                "Binary Subnet: 11000000.11111100.01101100.00001111\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000\n";
        String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIPCalculate_IPv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String ipPrefix = "60";
        String expectedOutput = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFFFFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7335 -- FFFFFFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                "Total usable IP Addresses : 65535 \n" +
                "Subnet: 01111110.11011111.10101101.00000000.0000:0000:8a2e:0370\n" +
                "Binary Subnet: 01111110.11011111.10101101.00000000.0000:0000:8a2e:0370\n" +
                "Broadcast Address: FFFFFFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                "Prefix: /60\n" +
                "Netmask: 255.255.255.255.255.255.255.255.255.255.0.0.0.0.0.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11110000\n";
        String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_InvalidIPv4Prefix() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "33";
        binaryCalculate.IPCalculate(ip, ipPrefix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_InvalidIPv6Prefix() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String ipPrefix = "73";
        binaryCalculate.IPCalculate(ip, ipPrefix);
    }
}