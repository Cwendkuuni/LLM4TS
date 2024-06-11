package ipac;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1011";
        String num2 = "1100";
        String expectedResult = "11110";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1100";
        String expectedResult = "0111";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIPCalculateWithValidIPv4() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n"
                + "Usable Range: 192.168.1.2 -- 192.168.1.254\n"
                + "Total usable IP Addresses : 254 \n"
                + "Subnet: 11000010.11111100.00111101.00000111\n"
                + "Binary Subnet: 11111100.00111101.00000111.00000000\n"
                + "Broadcast Address: 192.168.1.255\n"
                + "Prefix: /24\n"
                + "Netmask: 255.255.255.0\n"
                + "Binary Netmask: 11111111.11111111.11111111.00000000\n";
        String result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testIPCalculateWithValidIPv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String ipPrefix = "48";
        String expectedOutput = "Total Range: FEDC:BA98:85A3:0000:0000:8A2E:0370:7334 -- FEDC:BA98:85A3:0000:0000:8A2E:0370:7335\n"
                + "Usable Range: FEDC:BA98:85A3:0000:0000:8A2E:0370:7335 -- FEDC:BA98:85A3:0000:0000:8A2E:0370:7334\n"
                + "Total usable IP Addresses : 1\n"
                + "Subnet: 01111101.10111101.10101101.00000000.00000000.01011110.00111110.11110110\n"
                + "Binary Subnet: 00111111.11011101.11010111.00000000.00000000.01011110.00111110.11110110\n"
                + "Broadcast Address: FEDC:BA98:85A3:0000:0000:8A2E:0370:7335\n"
                + "Prefix: /48\n"
                + "Netmask: FF00:FFFF:FFFF:0000:0000:0000:0000:0000\n"
                + "Binary Netmask: 11111111.11111111.11111111.00000000.00000000.00000000.00000000.00000000\n";
        String result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, result);
    }

    @Test(expected = RuntimeException.class)
    public void testIPCalculateWithInvalidIPPrefixLength() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "25";
        binaryCalculate.IPCalculate(ip, ipPrefix);
    }

    @Test
    public void testIPCalculateWithZeroLengthIPPrefix() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "0";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.1\n"
                + "Usable Range: 192.168.1.1 -- 192.168.1.1\n"
                + "Total usable IP Addresses : 1 \n"
                + "Subnet: 11000010.11111100.00111101.00000000\n"
                + "Binary Subnet: 11111111.11111111.11111111.00000000\n"
                + "Broadcast Address: 192.168.1.1\n"
                + "Prefix: /0\n"
                + "Netmask: 255.255.255.255\n"
                + "Binary Netmask: 11111111.11111111.11111111.11111111\n";
        String result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testIPCalculateWithMaxPrefixLength() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "33";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.1\n"
                + "Usable Range: 192.168.1.1 -- 192.168.1.1\n"
                + "Total usable IP Addresses : 1 \n"
                + "Subnet: 11000010.11111100.00111101.00000000\n"
                + "Binary Subnet: 11111111.11111111.11111111.00000000\n"
                + "Broadcast Address: 192.168.1.1\n"
                + "Prefix: /32\n"
                + "Netmask: 255.255.255.255\n"
                + "Binary Netmask: 11111111.11111111.11111111.11111111\n";
        String result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, result);
    }
}