package ipac.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import ipac.BinaryCalculate;

public class BinaryCalculateTest {

    private BinaryCalculate cal;

    @Before
    public void setUp() {
        cal = new BinaryCalculate();
    }

    @Test
    public void testAddBinary_TwoZeroBinaryNumbers() {
        String num1 = "0111";
        String num2 = "0111";
        String sum = cal.addBinary(num1, num2);
        assertEquals("1111", sum);
    }

    @Test
    public void testAddBinary_TwoOneBinaryNumbers() {
        String num1 = "1011";
        String num2 = "1011";
        String sum = cal.addBinary(num1, num2);
        assertEquals("11110", sum);
    }

    @Test
    public void testAddBinary_OneZeroOneBinaryNumbers() {
        String num1 = "0111";
        String num2 = "1011";
        String sum = cal.addBinary(num1, num2);
        assertEquals("10010", sum);
    }

    @Test
    public void testAddBinary_OneCarry() {
        String num1 = "1111";
        String num2 = "1111";
        String sum = cal.addBinary(num1, num2);
        assertEquals("11111", sum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinary_InvalidLength() {
        String num1 = "11";
        String num2 = "11";
        cal.addBinary(num1, num2);
    }

    @Test
    public void testSubBinary_ValidCase() {
        String num1 = "1011";
        String num2 = "0111";
        String diff = cal.subBinary(num1, num2);
        assertEquals("1000", diff);
    }

    @Test
    public void testSubBinary_FirstNumberBigger() {
        String num1 = "1101";
        String num2 = "1011";
        String diff = cal.subBinary(num1, num2);
        assertEquals("0010", diff);
    }

    @Test
    public void testSubBinary_FirstNumberSmaller() {
        String num1 = "0111";
        String num2 = "1011";
        String diff = cal.subBinary(num1, num2);
        assertEquals("11001", diff);
    }

    @Test
    public void testIPCalculate_ValidIPAddressAndPrefix() {
        String ip = "192.168.1.1";
        String prefix = "24";
        String output = cal.IPCalculate(ip, prefix);
        // Add assertions for the expected output
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_InvalidIPAddressLength() {
        String ip = "192.";
        String prefix = "24";
        cal.IPCalculate(ip, prefix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_InvalidPrefixLength() {
        String ip = "192.168.1.1";
        String prefix = "33";
        cal.IPCalculate(ip, prefix);
    }

    @Test
    public void testCalculate_ValidInput() {
        String subnet = "192.168.1.0";
        String prefix = "24";
        cal.calculate(subnet, prefix);
        // Add assertions for the expected instance variables (e.g., totalIPAddresses, IPRangeAddress, and usableRange)
    }

    @Test
    public void testCalculate_IPv6() {
        String subnet = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String prefix = "60";
        cal.calculate(subnet, prefix);
        // Add assertions for the expected instance variables
    }

}