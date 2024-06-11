package ipac;

import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "11111010";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "0001";
        num2 = "1111";
        expected = "11111001";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "1111";
        num2 = "1111";
        expected = "11111111";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "0000";
        num2 = "0000";
        expected = "0000";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "1111";
        num2 = "0000";
        expected = "1111";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinaryWithIllegalArgument() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "";
        binaryCalculate.addBinary(num1, num2);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "0101";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "1010";
        num2 = "1111";
        expected = "0001";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "1111";
        num2 = "1111";
        expected = "0000";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "0000";
        num2 = "0000";
        expected = "0000";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "0000";
        num2 = "1111";
        expected = "11111111";
        result = binaryCalculate.subBinary(num1, num2);
        assertNotEquals(expected, result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254\n" +
                "Subnet: 11000010.11110000.01101100.00000101\n" +
                "Binary Subnet: 11111100.11111111.01101100.00000101\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000\n";
        String resultOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, resultOutput);

        ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        ipPrefix = "64";
        expectedOutput = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7333\n" +
                "Total usable IP Addresses : 65534\n" +
                "Subnet: 01111111.01110111.10101101.00000000.00001110.11101101.01111011.11110110\n" +
                "Binary Subnet: 11111111.11111111.10101101.00000000.00111100.11101101.10111011.11110110\n" +
                "Broadcast Address: 2001:0db8:85a3:0000:0000:8a2e:0370:7335\n" +
                "Prefix: /64\n" +
                "Netmask: 255.255.255.255.255.255.255.255\n" +
                "Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111\n";
        resultOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, resultOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculateWithIllegalArgument() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "";
        binaryCalculate.IPCalculate(ip, ipPrefix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculateWithInvalidIPPrefixLength() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "33";
        binaryCalculate.IPCalculate(ip, ipPrefix);
    }
}