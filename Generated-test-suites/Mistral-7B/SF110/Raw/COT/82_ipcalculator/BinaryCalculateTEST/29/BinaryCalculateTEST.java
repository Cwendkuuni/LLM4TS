package ipac;

import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String num1 = "1111";
        String num2 = "1010";
        String expected = "11110101";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "0000";
        num2 = "1111";
        expected = "11110000";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "1111";
        num2 = "1111";
        expected = "11111111";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinaryNullOrEmptyString() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String num1 = null;
        String num2 = "1010";
        binaryCalculate.addBinary(num1, num2);
        fail("IllegalArgumentException expected but not thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinaryDifferentLengthStrings() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String num1 = "1111";
        String num2 = "10";
        binaryCalculate.addBinary(num1, num2);
        fail("IllegalArgumentException expected but not thrown.");
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String num1 = "1111";
        String num2 = "1010";
        String expected = "0101";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "1111";
        num2 = "1111";
        expected = "0000";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "1111";
        num2 = "0000";
        expected = "1111";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBinaryNullOrEmptyString() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String num1 = "1111";
        String num2 = null;
        binaryCalculate.subBinary(num1, num2);
        fail("IllegalArgumentException expected but not thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBinaryDifferentLengthStrings() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String num1 = "1111";
        String num2 = "10";
        binaryCalculate.subBinary(num1, num2);
        fail("IllegalArgumentException expected but not thrown.");
    }

    @Test
    public void testIPCalculateIPv4() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String ip = "192.168.1.1";
        String ipPrefix = "255.255.255.0";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254\n" +
                "Subnet: 11000000.11111100.01010100.00000001\n" +
                "Binary Subnet: 11111111.11111100.01101100.00000011\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000";
        String resultOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, resultOutput);
    }

    @Test
    public void testIPCalculateIPv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String ipPrefix = "ffff:ffff:ffff:ffff:ffff:ffff:ffff:c000";
        String expectedOutput = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:0DB8:85A3:0000:0000:8A2E:0370:7335\n" +
                "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7335 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7337\n" +
                "Total usable IP Addresses : 3\n" +
                "Subnet: 01111111.11011111.10101101.00000000.00001110.11101110.00101111.11011111\n" +
                "Binary Subnet: 11111111.11011111.10101101.00000000.00001110.11101110.00101111.11011111\n" +
                "Broadcast Address: FFFF:0DB8:85A3:0000:0000:8A2E:0370:7335\n" +
                "Prefix: /64\n" +
                "Netmask: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:C000\n" +
                "Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.00111100";
        String resultOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, resultOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculateInvalidIPv4() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String ip = "192.168.1.256";
        String ipPrefix = "255.255.255.0";
        binaryCalculate.IPCalculate(ip, ipPrefix);
        fail("IllegalArgumentException expected but not thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculateInvalidIPv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String ip = "2001:0db8:85a3::";
        String ipPrefix = "ffff:ffff:ffff:ffff:ffff:ffff:ffff:c000";
        binaryCalculate.IPCalculate(ip, ipPrefix);
        fail("IllegalArgumentException expected but not thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculateInvalidIPv4Prefix() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String ip = "192.168.1.1";
        String ipPrefix = "255.255.255.32";
        binaryCalculate.IPCalculate(ip, ipPrefix);
        fail("IllegalArgumentException expected but not thrown.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculateInvalidIPv6Prefix() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String ipPrefix = "ffff:ffff:ffff:ffff:ffff:ffff:ffff:b000";
        binaryCalculate.IPCalculate(ip, ipPrefix);
        fail("IllegalArgumentException expected but not thrown.");
    }
}