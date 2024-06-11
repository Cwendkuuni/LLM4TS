package ipac.test;

import static org.junit.Assert.*;

import ipac.BinaryCalculate;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11110110";
        String num2 = "10111011";
        String sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("11011111", sum);

        num1 = "00110110";
        num2 = "11011111";
        sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("11111101", sum);

        num1 = "11111111";
        num2 = "11111111";
        sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("111111111", sum);

        num1 = "00000000";
        num2 = "11111111";
        sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("111111111", sum);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11111111";
        String num2 = "00000011";
        String diff = binaryCalculate.subBinary(num1, num2);
        assertEquals("11111100", diff);

        num1 = "01111111";
        num2 = "10000101";
        diff = binaryCalculate.subBinary(num1, num2);
        assertEquals("00111010", diff);

        num1 = "10000000";
        num2 = "11111111";
        diff = binaryCalculate.subBinary(num1, num2);
        assertNull(diff);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String outputText = binaryCalculate.IPCalculate(ip, ipPrefix);

        assertTrue(outputText.contains("Total Range: 192.168.1.1 -- 192.168.1.255\n"));
        assertTrue(outputText.contains("Usable Range: 192.168.1.2 -- 192.168.1.254\n"));
        assertTrue(outputText.contains("Total usable IP Addresses : 254 \n"));
        assertTrue(outputText.contains("Subnet: 11101100.01111111.00010000.00111111\n"));
        assertTrue(outputText.contains("Binary Subnet: 11101100.01111111.00010000.00111111\n"));
        assertTrue(outputText.contains("Broadcast Address: 192.168.1.255\n"));
        assertTrue(outputText.contains("Prefix: /24\n"));
        assertTrue(outputText.contains("Netmask: 255.255.255.0\n"));
        assertTrue(outputText.contains("Binary Netmask: 01111111.11111111.11111111.00000000\n"));
    }

    @Test
    public void testIPCalculateIPv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String ipPrefix = "60";
        String outputText = binaryCalculate.IPCalculate(ip, ipPrefix);

        assertTrue(outputText.contains("Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- "));
        assertTrue(outputText.contains("Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7332 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7335\n"));
        assertTrue(outputText.contains("Total usable IP Addresses : 14 \n"));
        assertTrue(outputText.contains("Subnet: 01111011.11010111.10101101.00000000.00001010.11101100.01111101.11011110\n"));
        assertTrue(outputText.contains("Binary Subnet: 01111011.11010111.10101101.00000000.00001010.11101100.01111101.11011110\n"));
        assertTrue(outputText.contains("Broadcast Address: 2001:0db8:85a3:0000:0000:8a2e:0370:7335\n"));
        assertTrue(outputText.contains("Prefix: /60\n"));
        assertTrue(outputText.contains("Netmask: ffff:fffe:ffff:ffff:ffff:ffff:ffff:ffff\n"));
        assertTrue(outputText.contains("Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.00000000\n"));
    }
}