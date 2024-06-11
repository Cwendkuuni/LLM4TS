package ipac.test;

import static org.junit.Assert.*;

import ipac.BinaryCalculate;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11111111";
        String num2 = "10101010";
        String expected = "11110110";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "00110110";
        num2 = "11010011";
        expected = "11101111";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "11111111";
        num2 = "11111111";
        expected = "111111110";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11111111";
        String num2 = "10101010";
        String expected = "01010101";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "01101101";
        num2 = "11010011";
        expected = "10101110";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "11111111";
        num2 = "11111111";
        expected = "00000000";
        result = binaryCalculate.subBinary(num1, num2);
        assertNull(result);
    }

    // Add more test cases for other methods in BinaryCalculate class.

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.1.1";
        String IPPrefix = "24";
        String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                                      "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                                      "Total usable IP Addresses : 254 \n" +
                                      "Subnet: 11010100.11111111.00111010.00001111\n" +
                                      "Binary Subnet: 11101100.11111111.00111010.00001111\n" +
                                      "Broadcast Address: 11111111.11111111.00111010.00001111\n" +
                                      "Prefix: /24\n" +
                                      "Netmask: 255.255.255.0\n" +
                                      "Binary Netmask: 11111111.11111111.00000000.00000000\n";
        String resultOutputText = binaryCalculate.IPCalculate(IP, IPPrefix);
        assertEquals(expectedOutputText, resultOutputText);
    }

    // Add more test cases for other methods in BinaryCalculate class.

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "11111111.11111111.01101101.00001111";
        String expected = "11111111111111110110110100001111";
        String result = binaryCalculate.getBinaryIP(binaryIP);
        assertEquals(expected, result);
    }

    // Add more test cases for other methods in BinaryCalculate class.

    // Test constructor and other methods if needed.

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.1.1";
        String IPPrefix = "24";
        int noPrefix = 5;
        String endPrefix = "31";
        // Set up the expected output here.
        String[][] expectedOutput = new String[noPrefix + 8][2];
        // Call the method and store the result in the resultOutput array.
        binaryCalculate.prefixInPrefixCalculate(IP, IPPrefix, noPrefix, endPrefix);
        // Compare the expected and actual output here.
        // Add assertions for each element in the output array.
    }
}