package ipac;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "11111010";
        String actual = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, actual);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "0101";
        String actual = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, actual);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        binaryCalculate.IPCalculate(ip, ipPrefix);
        // Add assertions for the output text here.
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String subnet = "192.168.1";
        String noOfIPAddresses = "256";
        binaryCalculate.calculate(subnet, noOfIPAddresses);
        // Add assertions for the instance variables here.
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        int noPrefix = 5;
        String endPrefix = "32";
        binaryCalculate.prefixInPrefixCalculate(ip, ipPrefix, noPrefix, endPrefix);
        // Add assertions for the output text here.
    }

    @Test
    public void testCheckIPType() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        binaryCalculate.checkIPType(ip.length() - ip.lastIndexOf(".") - 1);
        assertTrue(binaryCalculate.isIPv4);
        binaryCalculate.checkIPType(ip.length() - ip.lastIndexOf(":") - 1);
        assertTrue(binaryCalculate.isIPv6);
    }

    @Test
    public void testConvert() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "11110110111111011110110111111101";
        String expected = "192.168.128.128";
        String actual = binaryCalculate.convert(binaryIP);
        assertEquals(expected, actual);
    }

    @Test
    public void testConvertToBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String expected = "1110011011001000110111001110110111111101";
        String actual = binaryCalculate.convertToBinary(ip);
        assertEquals(expected, actual);
    }

    @Test
    public void testGetStartIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ipRangeAddress = "192.168.1.1 -- 192.168.1.10";
        String expected = "192.168.1.1";
        String actual = binaryCalculate.getStartIP();
        assertEquals(expected, actual);
    }
}