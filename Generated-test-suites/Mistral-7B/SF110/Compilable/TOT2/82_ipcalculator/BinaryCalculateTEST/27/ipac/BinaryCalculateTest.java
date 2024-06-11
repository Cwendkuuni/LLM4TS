package ipac;

import org.junit.Assert;
import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1111";
        String expectedResult = "11111";
        String result = binaryCalculate.addBinary(num1, num2);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1110";
        String expectedResult = "0011";
        String result = binaryCalculate.subBinary(num1, num2);
        Assert.assertEquals(expectedResult, result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.1.1";
        String IPPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11000000.11111100.01101100.00001111\nBinary Subnet: 11000000111111000110110000001111\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.11111111.00000000";
        String output = binaryCalculate.IPCalculate(IP, IPPrefix);
        Assert.assertEquals(expectedOutput, output);
    }

    @Test
    public void testAddBinary_EdgeCases() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11111111";
        String num2 = "1";
        String expectedResult = "11111110";
        String result = binaryCalculate.addBinary(num1, num2);
        Assert.assertEquals(expectedResult, result);

        num1 = "1";
        num2 = "11111111";
        expectedResult = "11111110";
        result = binaryCalculate.addBinary(num1, num2);
        Assert.assertEquals(expectedResult, result);

        num1 = "";
        num2 = "1";
        expectedResult = "1";
        result = binaryCalculate.addBinary(num1, num2);
        Assert.assertEquals(expectedResult, result);

        num1 = "1";
        num2 = "";
        expectedResult = "1";
        result = binaryCalculate.addBinary(num1, num2);
        Assert.assertEquals(expectedResult, result);

        num1 = "11111111";
        num2 = "11111111";
        expectedResult = "111111111111";
        result = binaryCalculate.addBinary(num1, num2);
        Assert.assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinary_NullInput() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = null;
        String num2 = "1111";
        String result = binaryCalculate.addBinary(num1, num2);
        Assert.fail("Method should throw an IllegalArgumentException when given a null input.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinary_EmptyStringInput() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "";
        String num2 = "1111";
        String result = binaryCalculate.addBinary(num1, num2);
        Assert.fail("Method should throw an IllegalArgumentException when given an empty string input.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBinary_InvalidInput() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1110";
        String result = binaryCalculate.subBinary(num2, num1);
        Assert.fail("Method should throw an IllegalArgumentException when given invalid input.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_InvalidIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "abcd.efgh.ijkl.mnop";
        String IPPrefix = "24";
        String output = binaryCalculate.IPCalculate(IP, IPPrefix);
        Assert.fail("Method should throw an IllegalArgumentException when given an invalid IP address.");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_InvalidIPPrefix() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.1.1";
        String IPPrefix = "35";
        String output = binaryCalculate.IPCalculate(IP, IPPrefix);
        Assert.fail("Method should throw an IllegalArgumentException when given an invalid IP prefix length.");
    }
}