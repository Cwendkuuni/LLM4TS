package ipac.test;

import org.junit.Test;
import static org.junit.Assert.*;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "10110";
        String result = bc.addBinary(num1, num2);
        assertEquals(expected, result);

        // Test edge cases and overflow
        String num1_max = "11111111111111111111111111111111";
        String num2_max = "11111111111111111111111111111111";
        String expected_max = "11111111111111111111111111111111";
        String result_max = bc.addBinary(num1_max, num2_max);
        assertEquals(expected_max, result_max);

        String num1_min = "0000";
        String num2_min = "0000";
        String expected_min = "0000";
        String result_min = bc.addBinary(num1_min, num2_min);
        assertEquals(expected_min, result_min);

        String num1_diff = "1111";
        String num2 = "0000";
        String expected_diff_pos = "1111";
        String result_diff_pos = bc.addBinary(num1_diff, num2);
        assertEquals(expected_diff_pos, result_diff_pos);

        String num1_diff = "0000";
        String num2_diff = "1111";
        String expected_diff_neg = "1111";
        String result_diff_neg = bc.addBinary(num1, num2_diff);
        assertEquals(expected_diff_neg, result_diff_neg);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "0101";
        String result = bc.subBinary(num1, num2);
        assertEquals(expected, result);

        // Test edge cases and underflow
        String num1_min = "0000";
        String num2_min = "0000";
        String expected_min = "0000";
        String result_min = bc.subBinary(num1_min, num2_min);
        assertEquals(expected_min, result_min);

        String num1_max = "11111111111111111111111111111111";
        String num2 = "0000";
        String expected_max = "11111111111111111111111111111111";
        String result_max = bc.subBinary(num1_max, num2);
        assertEquals(expected_max, result_max);

        String num1 = "1111";
        String num2_min = "0000";
        String expected_min_neg = "1111";
        String result_min_neg = bc.subBinary(num1, num2_min);
        assertEquals(expected_min_neg, result_min_neg);

        String num1 = "0000";
        String num2_max = "11111111111111111111111111111111";
        String expected_max_neg = "11111111111111111111111111111111";
        String result_max_neg = bc.subBinary(num1, num2_max);
        assertEquals(expected_max_neg, result_max_neg);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254\nSubnet: 11001100.11111111.01101100.01101111\nBinary Subnet: 11111111.11111111.01110010.01111101\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 00111111.11111111.00110010.00111101\n";
        String result = bc.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, result);

        // Test edge cases and out-of-memory errors
        String ip_invalid = "256.256.256.256";
        String ipPrefix_invalid = "32";
        String expectedOutput_invalid = "Your input is not a valid IP address or prefix length.";
        String result_invalid = bc.IPCalculate(ip_invalid, ipPrefix_invalid);
        assertEquals(expectedOutput_invalid, result_invalid);

        String ip_long = "1111111111111111111111111111111111111111111111111111111111111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111";
        String ipPrefix_short = "1";
        String expectedOutput_short = "Your input prefix length is too short.";
        String result_short = bc.IPCalculate(ip_long, ipPrefix_short);
        assertEquals(expectedOutput_short, result_short);

        String ip_long_prefix = "128";
        String ip = "255.255.255.255";
        String expectedOutput_long_prefix = "Your input IP address length is longer than the prefix length.";
        String result_long_prefix = bc.IPCalculate(ip, ip_long_prefix);
        assertEquals(expectedOutput_long_prefix, result_long_prefix);

        String ip_ipv6 = "2001:db8:85a3:0:0:8a2e:370:7334";
        String ipPrefix = "64";
        try {
            bc.IPCalculate(ip_ipv6, ipPrefix);
            fail("Expected OutOfMemoryError");
        } catch (OutOfMemoryError e) {
            // Expected
        }
    }
}