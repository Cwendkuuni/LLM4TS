package ipac.test;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import ipac.BinaryCalculate;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "11111010";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "11111111";
        num2 = "11111111";
        expected = "111111111111";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "0000";
        num2 = "1111";
        assertNull(binaryCalculate.addBinary(num1, num2));
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "0101";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "11111111";
        num2 = "11111111";
        assertNull(binaryCalculate.subBinary(num1, num2));

        num1 = "0000";
        num2 = "1111";
        assertNull(binaryCalculate.subBinary(num1, num2));
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String outputText = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertTrue(outputText.contains("Total Range: 192.168.1.1 -- 192.168.1.255"));
        assertTrue(outputText.contains("Usable Range: 192.168.1.2 -- 192.168.1.254"));

        ip = "2001:db8:85a3:0:56d:1123:f12f:3b0a";
        ipPrefix = "64";
        outputText = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertTrue(outputText.contains("Total Range: 2001:db8:85a3:0:56d:1123:f12f:3b0a -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"));
        assertTrue(outputText.contains("Usable Range: 2001:db8:85a3:0:56d:1123:f12f:3b0a -- FFFE:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"));
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = binaryCalculate.getBinaryIP("192.168.1.1");
        assertEquals("11100110.11010100.01101101.00110101", binaryIP);

        binaryIP = binaryCalculate.getBinaryIP("2001:db8:85a3:0:56d:1123:f12f:3b0a");
        assertEquals("20010db80db885a30000056d1123f12f3b0a", binaryIP);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "255.255.255.0";
        String expected = "00111111.00111111.00111111.00000000";
        String result = binaryCalculate.getNetmask(binaryIP);
        assertEquals(expected, result);

        binaryIP = "255.255.255.255";
        expected = "11111111.11111111.11111111.11111111";
        result = binaryCalculate.getNetmask(binaryIP);
        assertEquals(expected, result);

        binaryIP = "FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF";
        expected = "00000000.00000000.00000000.00000000.00000000.00000000.00000000.00000000";
        result = binaryCalculate.getNetmask(binaryIP);
        assertEquals(expected, result);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String subnet = "192.168.1.0";
        String noOfIPAddresses = "256";
        binaryCalculate.calculate(subnet, noOfIPAddresses.substring(0, noOfIPAddresses.length() - 2));
        assertEquals("192.168.1.1 -- 192.168.1.255", binaryCalculate.IPRangeAddress);
        assertEquals("192.168.1.0 -- 192.168.1.255", binaryCalculate.usableRange);
        assertEquals("11100110.11010100.01101101.01111111", binaryCalculate.getBinaryIP(binaryCalculate.currentIP));
        assertEquals("11111111.11111111.11111111.00000000", binaryCalculate.getBinaryIP(binaryCalculate.prefix));
        assertEquals("192.168.1.0", binaryCalculate.subnet);
        assertEquals(256, binaryCalculate.totalIPAddresses.intValue());

        subnet = "2001:db8:85a3:0:56d:1123:f12f:3b0a";
        noOfIPAddresses = "4096";
        binaryCalculate = new BinaryCalculate();
        binaryCalculate.calculate(subnet, noOfIPAddresses.substring(0, noOfIPAddresses.length() - 2));
        assertEquals("2001:db8:85a3:0:56d:1123:f12f:3b0a -- FFFFFFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF", binaryCalculate.IPRangeAddress);
        assertEquals("2001:db8:85a3:0:56d:1123:f12f:3b0a -- FFFFE:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF", binaryCalculate.usableRange);
        assertEquals("20010db80db885a30000056d1123f12f3b0a", binaryCalculate.getBinaryIP(binaryCalculate.currentIP));
        assertEquals("20010db80db885a3ff0000056d1123ff000000", binaryCalculate.getBinaryIP(binaryCalculate.prefix));
        assertEquals("2001:db8:85a3:0:56d:1123:f12f:3b0a", binaryCalculate.subnet);
        assertEquals(4096, binaryCalculate.totalIPAddresses.intValue());
    }
}