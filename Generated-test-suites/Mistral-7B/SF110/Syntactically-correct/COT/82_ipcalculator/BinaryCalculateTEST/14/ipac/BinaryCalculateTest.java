package ipac;

import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {
    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1110";
        String expected = "11111";
        String actual = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, actual);

        // Test for edge cases
        String num1Edge = "0000";
        String num2Edge = "1111";
        String expectedEdge = "1000000001";
        String actualEdge = binaryCalculate.addBinary(num1Edge, num2Edge);
        assertEquals(expectedEdge, actualEdge);

        num1 = "1";
        num2 = "1";
        expected = "10";
        actual = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, actual);

        // Test for exceptions
        try {
            String emptyNum1 = "";
            String emptyNum2 = "";
            binaryCalculate.addBinary(emptyNum1, emptyNum2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1110";
        String expected = "0011";
        String actual = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, actual);

        // Test for edge cases
        String num1Edge = "1111";
        String num2Edge = "0000";
        String expectedEdge = "111111111111";
        String actualEdge = binaryCalculate.subBinary(num1Edge, num2Edge);
        assertEquals(expectedEdge, actualEdge);

        num1 = "1111";
        num2 = "1111";
        expected = "0000";
        actual = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, actual);

        // Test for exceptions
        try {
            String emptyNum1 = "";
            String nonEmptyNum2 = "1111";
            binaryCalculate.subBinary(emptyNum1, nonEmptyNum2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expected = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254\n" +
                "Subnet: 11101100.11110100.01000001.00000001\n" +
                "Binary Subnet: 11101100.11110100.01000001.00000010\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 01111111.11111111.11111111.00000000";
        String actual = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expected, actual);

        // Test for edge cases
        ip = "255.255.255.255";
        ipPrefix = "32";
        expected = "Total Range: 255.255.255.255 -- 255.255.255.255\n" +
                "Usable Range: \n" +
                "Total usable IP Addresses : 0\n" +
                "Subnet: 00000000.00000000.00000000.00000001\n" +
                "Binary Subnet: 00000000.00000000.00000000.00000010\n" +
                "Broadcast Address: 255.255.255.255\n" +
                "Prefix: /32\n" +
                "Netmask: 255.255.255.255\n" +
                "Binary Netmask: 11111111.11111111.11111111.11111111";
        actual = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expected, actual);

        ip = "::";
        ipPrefix = "128";
        expected = "Total Range: :: -- ::\n" +
                "Usable Range: :: -- ::\n" +
                "Total usable IP Addresses : 65536\n" +
                "Subnet: 0000:0000:0000:0000:0000:0000:0000:0000\n" +
                "Binary Subnet: 0000:0000:0000:0000:0000:0000:0000:0000\n" +
                "Broadcast Address: ::\n" +
                "Prefix: /128\n" +
                "Netmask: 0:0:0:0:0:0:0:0\n" +
                "Binary Netmask: 00000000:00000000:00000000:00000000:00000000:00000000:00000000:00000000";
        actual = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expected, actual);

        // Test for exceptions
        try {
            String invalidIp = "256.0.0.0";
            String ipPrefixInvalid = "33";
            binaryCalculate.IPCalculate(invalidIp, ipPrefixInvalid);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}