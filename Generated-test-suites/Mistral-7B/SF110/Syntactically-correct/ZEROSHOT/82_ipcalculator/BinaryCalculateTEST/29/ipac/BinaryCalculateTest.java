package ipac;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigInteger;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String expected = "111101101101";
        String num1 = "11110110";
        String num2 = "01101101";
        String actual = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, actual);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String expected = "011110101101";
        String num1 = "11111111";
        String num2 = "100001110";
        String actual = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, actual);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11101100.11110100.01101000.00001111\nBinary Subnet: 11101100.11110100.01101000.00001111\n";
        String actualOutputText = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutputText, actualOutputText);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String expected = "11101100.01101000.01101100.00010101";
        String actual = binaryCalculate.getBinaryIP(ip);
        assertEquals(expected, actual);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        int noPrefix = 5;
        String endPrefix = "32";
        binaryCalculate.calculate(ip, ipPrefix);
        String[][] expectedOutput = new String[noPrefix + 8][2];
        expectedOutput[0][0] = "192.168.1.1 /24";
        expectedOutput[0][1] = "192.168.1.1 -- 192.168.1.255";
        expectedOutput[1][0] = "Total usable IP Addresses :";
        expectedOutput[1][1] = "254 ";
        expectedOutput[2][0] = "Subnet:";
        expectedOutput[2][1] = "11101100.01101000.01101100.00010101";
        expectedOutput[3][0] = "Binary Subnet:";
        expectedOutput[3][1] = "11101100.01101000.01101100.00010101";
        expectedOutput[4][0] = "Broadcast Address:";
        expectedOutput[4][1] = "192.168.1.255";
        expectedOutput[5][0] = "Prefix:";
        expectedOutput[5][1] = "32";
        expectedOutput[6][0] = "Netmask:";
        expectedOutput[6][1] = "255.255.255.0";
        expectedOutput[7][0] = "Binary Netmask:";
        expectedOutput[7][1] = "01111111.11111111.11111111.00000000";
        String[][] actualOutput = binaryCalculate.prefixInPrefixCalculate(ip, ipPrefix, noPrefix, endPrefix);
        assertArrayEquals(expectedOutput, actualOutput);
    }
}