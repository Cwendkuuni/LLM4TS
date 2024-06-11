package ipac.test;

import static org.junit.Assert.*;

import ipac.BinaryCalculate;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expectedSum = "11010";
        String sum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, sum);

        num1 = "11111111";
        num2 = "11111111";
        expectedSum = "111111110";
        sum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, sum);

        num1 = "00000000";
        num2 = "11111111";
        expectedSum = "11111111";
        sum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, sum);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expectedDiff = "0011";
        String diff = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDiff, diff);

        num1 = "11111111";
        num2 = "11111111";
        expectedDiff = "0000";
        diff = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDiff, diff);

        num1 = "00000000";
        num2 = "11111111";
        expectedDiff = "11111111";
        diff = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDiff, diff);
    }

    // Add more test cases for other methods in the BinaryCalculate class here.

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11101100.11110100.01101001.00000111\nBinary Subnet: 11101100.11110100.01101001.00001111\nBroadcast Address: 11101100.11110100.01101001.11111111\nPrefix: /23\nNetmask: 255.255.255.224\nBinary Netmask: 11111111.11111111.11111111.11110110";
        String output = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, output);

        // Add more test cases for the IPCalculate method here with different IPs and IP prefixes.
    }
}