package ipac;

import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expectedResult = "10111";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "1111";
        num2 = "1111";
        expectedResult = "10000";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "0000";
        num2 = "0000";
        expectedResult = "0000";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "1010";
        num2 = "0111";
        expectedResult = "11110";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "1111";
        num2 = "0000";
        expectedResult = "1111";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinaryWithNullNum1() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = null;
        String num2 = "1010";
        binaryCalculate.addBinary(num1, num2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinaryWithNullNum2() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1010";
        String num2 = null;
        binaryCalculate.addBinary(num1, num2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinaryWithDifferentLength() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        binaryCalculate.addBinary(num1, num2);
        num2 = num2.substring(0, num2.length() - 1);
        binaryCalculate.addBinary(num1, num2);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "0111";
        String expectedResult = "1000";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "0111";
        num2 = "1111";
        expectedResult = "0000";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "1010";
        num2 = "1010";
        expectedResult = "0000";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "1111";
        num2 = "0000";
        expectedResult = "1111";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "1111";
        num2 = "1111";
        expectedResult = "0000";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBinaryWithNullNum1() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = null;
        String num2 = "1010";
        binaryCalculate.subBinary(num1, num2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBinaryWithNullNum2() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1010";
        String num2 = null;
        binaryCalculate.subBinary(num1, num2);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.0";
        String ipPrefix = "24";
        String expectedOutputText = "Total Range: 192.168.1.0 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.1 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254 \n" +
                "Subnet: 11000010.11110000.01101100.00001111\n" +
                "Binary Subnet: 11000010.11110000.01101100.00001111\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 01111111.11111111.11111111.00000000";
        String outputText = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutputText, outputText);

        ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        ipPrefix = "60";
        expectedOutputText = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7332 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                "Total usable IP Addresses : 2\n" +
                "Subnet: 01111110.01111101.01010111.00000000.00001110.11011110.01110111.01100110\n" +
                "Binary Subnet: 01111110.01111101.01010111.00000000.00001110.11011110.01110111.01100110\n" +
                "Broadcast Address: 2001:0db8:85a3:0000:0000:8a2e:0370:7335\n" +
                "Prefix: /60\n" +
                "Netmask: 255.255.255.255.255.255.255.255.255.0.0.0.0.255.255.255.255\n" +
                "Binary Netmask: 11111111.11111111.11111111.11111101.00000000.00001111.11111111.11111111";
        outputText = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutputText, outputText);
    }

    @Test
    public void testIPCalculateWithDifferentIPLength() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.0";
        String ipPrefix = "32";
        String outputText = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals("", outputText);

        ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        ipPrefix = "65";
        outputText = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals("", outputText);
    }
}