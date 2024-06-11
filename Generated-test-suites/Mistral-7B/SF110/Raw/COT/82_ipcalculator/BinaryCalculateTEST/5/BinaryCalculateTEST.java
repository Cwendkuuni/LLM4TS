package ipac;

import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigInteger;

public class BinaryCalculateTest {
    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expectedResult = "11110101";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testAddBinaryWithCarry() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1111";
        String expectedResult = "10000011";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinaryWithInvalidInput() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "";
        String num2 = "1010";
        binaryCalculate.addBinary(num1, num2);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expectedResult = "0101";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.1.0";
        String IPPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.0 -- 192.168.1.255\n" +
                               "Usable Range: 192.168.1.1 -- 192.168.1.254\n" +
                               "Total usable IP Addresses : 254 \n" +
                               "Subnet: 11101100.11110100.01101000.00001111\n" +
                               "Binary Subnet: 11110111.11111011.01101100.00001111\n" +
                               "Broadcast Address: 11111111.11111111.01101111.11111111\n" +
                               "Prefix: /24\n" +
                               "Netmask: 255.255.255.0\n" +
                               "Binary Netmask: 00111111.11111111.11111111.00000000";
        String result = binaryCalculate.IPCalculate(IP, IPPrefix);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testIPCalculateIPv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String IPPrefix = "60";
        String expectedOutput = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                               "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7332\n" +
                               "Total usable IP Addresses : 65534\n" +
                               "Subnet: 01111011.11011111.10101101.00000000.00001110.10101101.00110111.11101100\n" +
                               "Binary Subnet: 00111101.11011111.10101101.00000000.00111100.10101101.01110111.11110100\n" +
                               "Broadcast Address: ff01:1111:1111:1111:1111:1111:1111:1111\n" +
                               "Prefix: /60\n" +
                               "Netmask: fffe::\n" +
                               "Binary Netmask: 11111111.11111111.11111111.11111111.00000000.00000000.00000000.11111111";
        String result = binaryCalculate.IPCalculate(IP, IPPrefix);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "11111111.11111111.11111111.00000000";
        String expectedResult = "00000000.00000000.00000000.11111111";
        String result = binaryCalculate.getNetmask(binaryIP);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "01111011.11011111.10101101.00000000";
        String expectedResult = "11110111.11111011.01101101.00000000";
        String result = binaryCalculate.getBinaryIP(binaryIP);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testCalculateWithIPv4() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String subnet = "192.168.1.0";
        String noOfIPAddresses = "256";
        binaryCalculate.calculate(subnet, noOfIPAddresses);
        assertEquals("255.255.255.255", binaryCalculate.currentIP);
        assertEquals("/32", binaryCalculate.prefix);
        assertEquals("192.168.1.0 -- 192.168.1.255", binaryCalculate.IPRangeAddress);
        assertEquals("192.168.1.1 -- 192.168.1.254", binaryCalculate.usableRange);
        assertEquals(new BigInteger("256"), binaryCalculate.totalIPAddresses);
    }

    @Test
    public void testCalculateWithIPv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String subnet = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String noOfIPAddresses = "65536";
        binaryCalculate.calculate(subnet, noOfIPAddresses);
        assertEquals("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff", binaryCalculate.currentIP);
        assertEquals("/64", binaryCalculate.prefix);
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff", binaryCalculate.IPRangeAddress);
        BigInteger expectedTotalIPAddresses = new BigInteger("65536");
        expectedTotalIPAddresses = expectedTotalIPAddresses.multiply(new BigInteger("65536"));
        assertEquals(expectedTotalIPAddresses, binaryCalculate.totalIPAddresses);
    }
}