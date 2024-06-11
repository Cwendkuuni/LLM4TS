package ipac.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ipac.BinaryCalculate;

public class BinaryCalculateTest {

    private BinaryCalculate binaryCalculate;

    @Before
    public void setUp() {
        binaryCalculate = new BinaryCalculate();
    }

    

    @Test
    public void testAddBinary_ZeroZero() {
        String num1 = "0";
        String num2 = "0";
        String expected = "0";
        String actual = binaryCalculate.addBinary(num1, num2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddBinary_OneZero() {
        String num1 = "1";
        String num2 = "0";
        String expected = "1";
        String actual = binaryCalculate.addBinary(num1, num2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddBinary_ZeroOne() {
        String num1 = "0";
        String num2 = "1";
        String expected = "1";
        String actual = binaryCalculate.addBinary(num1, num2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testAddBinary_OneOne() {
        String num1 = "1";
        String num2 = "1";
        String expected = "10";
        String actual = binaryCalculate.addBinary(num1, num2);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinary_InvalidInput() {
        String num1 = "123";
        String num2 = "456";
        binaryCalculate.addBinary(num1, num2);
    }

    @Test
    public void testSubBinary_ZeroZero() {
        String num1 = "0";
        String num2 = "0";
        String expected = "0";
        String actual = binaryCalculate.subBinary(num1, num2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSubBinary_ZeroOne() {
        String num1 = "0";
        String num2 = "1";
        String expected = "1111111111111111111111111011111101111110111111011111011";
        String actual = binaryCalculate.subBinary(num1, num2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSubBinary_OneZero() {
        String num1 = "1";
        String num2 = "0";
        String expected = "0";
        String actual = binaryCalculate.subBinary(num1, num2);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSubBinary_OneOne() {
        String num1 = "11111111";
        String num2 = "11111111";
        String expected = "00000000";
        String actual = binaryCalculate.subBinary(num1, num2);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBinary_InvalidInput() {
        String num1 = "123";
        String num2 = "456";
        binaryCalculate.subBinary(num1, num2);
    }

    @Test
    public void testIPCalculate_IPv4() {
        String ip = "192.168.1.1";
        String prefix = "24";
        String expected = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254 \n" +
                "Subnet: 11111110.11111100.01101101.00000111\n" +
                "Binary Subnet: 11111110.11111100.01101101.00001111\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000";
        String actual = binaryCalculate.IPCalculate(ip, prefix);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIPCalculate_IPv6() {
        String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String prefix = "48";
        String expected = "Total Range: FFFF:0DB8:85A3:0000:0000:8A2E:0370:7334\n" +
                "Usable Range: FFFF:0DB8:85A3:0000:0000:8A2E:0370:7333\n" +
                "Total usable IP Addresses : 65535 \n" +
                "Subnet: 01111111.11011011.10101011.00000000.0000.8A2E.0370.7333\n" +
                "Binary Subnet: 01111111.11011011.10101011.00000000.0000.11101110.01110100.11011111\n" +
                "Broadcast Address: FFFF:0DB8:85A3:0000:0000:8A2E:0370:7334\n" +
                "Prefix: /48\n" +
                "Netmask: FFFE:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFF0\n" +
                "Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11110000.11110000";
        String actual = binaryCalculate.IPCalculate(ip, prefix);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_InvalidIP() {
        String ip = "123.123.123.123.123.123.123.123";
        String prefix = "33";
        binaryCalculate.IPCalculate(ip, prefix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_InvalidPrefix() {
        String ip = "192.168.1.1";
        String prefix = "35";
        binaryCalculate.IPCalculate(ip, prefix);
    }

    
}