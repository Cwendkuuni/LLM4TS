package ipac.test;

import static org.junit.Assert.*;

import ipac.BinaryCalculate;
import java.math.BigInteger;
import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary_TwoZeroStrings() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("0", "0");
        assertEquals("0", result);
    }

    @Test
    public void testAddBinary_TwoOneStrings() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("1", "1");
        assertEquals("0", result);
        result = binaryCalculate.addBinary("1", "1");
        assertEquals("1", result);
    }

    @Test
    public void testAddBinary_OneZeroOne() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("0", "1");
        assertEquals("1", result);
    }

    @Test
    public void testAddBinary_OneOneOneZero() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "111";
        String num2 = "010";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals("1110", result);
    }

    @Test
    public void testAddBinary_LargeInputs() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "10111010110110101101101011011010";
        String num2 = "11110011110011110011110011110011";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals("10001111011111110111111101111110", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinary_IllegalArgument() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("a", "b");
    }

    @Test
    public void testSubBinary_TwoEqualStrings() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "111";
        String num2 = num1;
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals("000", result);
    }

    @Test
    public void testSubBinary_OneBiggerThanOther() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1110";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals("0011", result);
    }

    @Test
    public void testSubBinary_OneSmallerThanOther() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1110";
        String num2 = "1111";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals("0001", result);
    }

    @Test
    public void testSubBinary_TwosComplement() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num = "01111111";
        String twosComp = binaryCalculate.twosComplement(num);
        String result = binaryCalculate.addBinary(twosComp, "1");
        assertEquals("10000000", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBinary_IllegalArgument() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.subBinary("a", "b");
    }

    @Test
    public void testIPCalculate_ValidIPv4Address() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.0.1";
        String ipPrefix = "24";

        String result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertTrue(result.contains("Total Range: 192.168.0.1 -- 192.168.0.255"));
        assertTrue(result.contains("Usable Range: 192.168.0.2 -- 192.168.0.254"));
        assertTrue(result.contains("Subnet: 11101100.00011111.00000000.00111111"));
        assertTrue(result.contains("Binary Subnet: 11101100.00011111.00000000.00111111"));
        assertTrue(result.contains("Broadcast Address: 192.168.0.255"));
        assertTrue(result.contains("Prefix: /24"));
        assertTrue(result.contains("Netmask: 255.255.255.0"));
        assertTrue(result.contains("Binary Netmask: 01111111.11111111.00000000.00000001"));
    }

    @Test
    public void testIPCalculate_ValidIPv6Address() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String ipPrefix = "60";

        String result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertTrue(result.contains("Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- "));
        assertTrue(result.contains("Usable Range: "));
        assertTrue(result.contains("Subnet: 01111011.11010111.10101011.00000000.00000000.10101110.00110111.11011110"));
        assertTrue(result.contains("Binary Subnet: "));
        assertTrue(result.contains("Broadcast Address: "));
        assertTrue(result.contains("Prefix: /60"));
        assertTrue(result.contains("Netmask: ..."));
        assertTrue(result.contains("Binary Netmask: "));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_InvalidIPv4Address() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "a.b.c.d";
        String ipPrefix = "24";
        binaryCalculate.IPCalculate(ip, ipPrefix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_InvalidIPv6Address() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "a:b:c:d:e:f:g:h";
        String ipPrefix = "60";
        binaryCalculate.IPCalculate(ip, ipPrefix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_InvalidIPv4Prefix() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.0.0";
        String ipPrefix = "32";
        binaryCalculate.IPCalculate(ip, ipPrefix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_InvalidIPv6Prefix() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String ipPrefix = "128";
        binaryCalculate.IPCalculate(ip, ipPrefix);
    }
}