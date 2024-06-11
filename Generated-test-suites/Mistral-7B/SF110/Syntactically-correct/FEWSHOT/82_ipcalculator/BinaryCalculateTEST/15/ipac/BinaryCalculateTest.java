package ipac;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11110111";
        String num2 = "1011";
        String expectedResult = "11111110";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "10101010";
        num2 = "11111111";
        expectedResult = "10010101";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11110111";
        String num2 = "1011";
        String expectedResult = "11101100";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "10101010";
        num2 = "11111111";
        expectedResult = "01001010";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "00000000";
        num2 = "11111111";
        assertNull(binaryCalculate.subBinary(num1, num2));
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254\n" +
                "Subnet: 11001100.11111010.00001111.00000001\n" +
                "Binary Subnet: 11111111.11111111.11001100.00001111\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000";
        String outputText = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutputText, outputText);

        ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        ipPrefix = "60";
        expectedOutputText = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7335 -- FFFF:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                "Total usable IP Addresses : 65535\n" +
                "Subnet: 01111101.01111101.01010110.00000000.00001110.11101110.00110110.11011110\n" +
                "Binary Subnet: 00111111.00111111.00101010.00000000.11110110.11110110.01101100.11011110\n" +
                "Broadcast Address: FFFF:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                "Prefix: /60\n" +
                "Netmask: 255.255.255.252\n" +
                "Binary Netmask: 11111111.11111111.11111111.11111111";
        outputText = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutputText, outputText);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "11111111.11111111.11111111.11111111";
        String expectedResult = "11111111111111111111111111111111";
        String result = binaryCalculate.getBinaryIP(binaryIP);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "11111111.11111111.11111111.11111111";
        String ipPrefix = "24";
        String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254\n" +
                "Subnet: 11001100.11111010.00001111.00000001\n" +
                "Binary Subnet: 11111111.11111111.11001100.00001111\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000";
        String outputText = binaryCalculate.IPCalculate(binaryIP, ipPrefix);
        String binaryNetmask = binaryCalculate.getNetmask(outputText.split("Netmask: ")[1].split("\n")[0]);
        assertEquals("11111111.11111111.11111111.00000000", binaryNetmask);
    }
}