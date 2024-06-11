package ipac.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import org.junit.Test;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        // Test cases for IPv4 addresses
        assertEquals("01111110", binaryCalculate.addBinary("11111100", "00111100"));
        assertEquals("10110110", binaryCalculate.addBinary("01111100", "01011000"));
        assertEquals("11111111", binaryCalculate.addBinary("11111110", "00000001"));
        assertEquals("00000000", binaryCalculate.addBinary("11111111", "11111111"));

        // Test cases for IPv6 addresses
        assertEquals("20010db800000001", binaryCalculate.addBinary("20010db800000000", "00000001"));
        assertEquals("20010db800000002", binaryCalculate.addBinary("20010db800000001", "00000001"));
        assertEquals("20010db80000000f", binaryCalculate.addBinary("20010db80000000e", "00000001"));
        assertEquals("0000000000000000", binaryCalculate.addBinary("ffffffffffffffffff", "ffffffffffffffffff"));
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        // Test cases for IPv4 addresses
        assertEquals("01111101", binaryCalculate.subBinary("11111110", "00111101"));
        assertEquals("10110101", binaryCalculate.subBinary("11111101", "01011010"));
        assertEquals("11111100", binaryCalculate.subBinary("11111111", "00000011"));
        assertNull(binaryCalculate.subBinary("11111111", "11111111"));

        // Test cases for IPv6 addresses
        assertEquals("20010db7fffffffe", binaryCalculate.subBinary("20010db80000000f", "00000001"));
        assertEquals("20010db80000000e", binaryCalculate.subBinary("20010db80000000f", "00000001"));
        assertEquals("20010db80000000d", binaryCalculate.subBinary("20010db80000000e", "00000001"));
        assertNull(binaryCalculate.subBinary("20010db800000000", "20010db800000000"));
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        // Test case for IPv4 address
        binaryCalculate.currentIP = "192.168.1.1";
        binaryCalculate.IPRangeAddress = "192.168.1.1 - 192.168.1.255";
        binaryCalculate.prefix = "/24";
        binaryCalculate.totalIPAddresses = new BigInteger("256");
        binaryCalculate.IPCalculate("192.168.1.1", "24");

        // Test case for IPv6 address
        binaryCalculate.currentIP = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        binaryCalculate.IPRangeAddress = "2001:0db8:85a3:0000:0000:8a2e:0370:7334 - 2001:0db8:85a3:0000:0000:8a2e:0370:7335";
        binaryCalculate.prefix = "/64";
        binaryCalculate.totalIPAddresses = new BigInteger("2");
        binaryCalculate.IPCalculate("2001:0db8:85a3:0000:0000:8a2e:0370:7334", "64");
    }

    @Test
    public void testCheckIPType() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.checkIPType(31);
        assertTrue(binaryCalculate.isIPv4);
        binaryCalculate.checkIPType(127);
        assertTrue(binaryCalculate.isIPv6);
    }

    @Test
    public void testConvert() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        // Test cases for IPv4 addresses
        assertEquals("192.168.1.1", binaryCalculate.convert("01100110.11101001.01110111.00111111"));
        assertEquals("255.255.255.255", binaryCalculate.convert("11111111.11111111.11111111.11111111"));

        // Test cases for IPv6 addresses
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", binaryCalculate.convert("20010db800000001:0000:0000:8a2e:0037:0733:0470:7334"));
        assertEquals("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff", binaryCalculate.convert("ffffffffffffffffff"));
    }

    @Test
    public void testConvertToBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        // Test cases for IPv4 addresses
        assertEquals("01111110.00111100", binaryCalculate.convertToBinary("192.168.1.2"));
        assertEquals("11111111.11111111", binaryCalculate.convertToBinary("255.255.255.255"));

        // Test cases for IPv6 addresses
        assertEquals("20010db800000001:0000:0000:8a2e:0037:0733:0470:7334", binaryCalculate.convertToBinary("2001:0db8:85a3:0000:0000:8a2e:0370:7334"));
        assertEquals("ffffffffffffffffff", binaryCalculate.convertToBinary("ffffffffffffffffff"));
    }

    @Test
    public void testGetStartIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        assertEquals("192.168.1.1", binaryCalculate.getStartIP());
    }
}