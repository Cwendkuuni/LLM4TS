package ipac;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinaryCalculateTest {
    @Test
    public void testAddBinary_TwoIPsWithSameLength() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11110110";
        String num2 = "11110110";
        String expectedResult = "11111110";
        String actualResult = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAddBinary_TwoIPsWithDifferentLengths() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11110110";
        String num2 = "00111111";
        String expectedResult = "11111101";
        String actualResult = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testSubBinary_ValidInput() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11111111";
        String num2 = "10101010";
        String expectedResult = "01010101";
        String actualResult = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBinary_InvalidInput() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11111111";
        String num2 = "11111110";
        binaryCalculate.subBinary(num1, num2);
    }

    @Test
    public void testIPCalculate_ValidInput_IPv4() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.1.1";
        String IPPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254\n" +
                "Subnet: 11000000.11111100.01110100.00001011\n" +
                "Binary Subnet: 11111100.11111111.01110100.00001011\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000";
        String actualOutput = binaryCalculate.IPCalculate(IP, IPPrefix);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIPCalculate_ValidInput_IPv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String IPPrefix = "48";
        String expectedOutput = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- " +
                "2001:0db8:85a3:0000:0000:8a2e:0370:7335\n" +
                "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7335 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7337\n" +
                "Total usable IP Addresses : 3\n" +
                "Subnet: 01111111.11011111.10101101.00000000.0000.11101110.00101111.11110111\n" +
                "Binary Subnet: 11111111.11011111.10101101.00000000.0000.11101110.00101111.11110111\n" +
                "Broadcast Address: 2001:0db8:85a3:0000:0000:8a2e:0370:7335\n" +
                "Prefix: /48\n" +
                "Netmask: 255.255.255.255.255.255.255.255.255.255.255.255.255.255.255.255\n" +
                "Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111";
        String actualOutput = binaryCalculate.IPCalculate(IP, IPPrefix);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_InvalidInput() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.1.1";
        String IPPrefix = "25";
        binaryCalculate.IPCalculate(IP, IPPrefix);
    }
}