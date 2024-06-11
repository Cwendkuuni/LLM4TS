package ipac;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "10111";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "0111";
        num2 = "1111";
        expected = "11110";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "1111";
        num2 = "1111";
        expected = "10000";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1011";
        String num2 = "1111";
        String expected = "0100";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "1111";
        num2 = "1011";
        expected = "0001";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "0000";
        num2 = "0000";
        expected = "0000";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expected = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254 \n" +
                "Subnet: 11000010.11110100.00001101.00000011\n" +
                "Binary Subnet: 11111100.11111010.00111101.00000111\n" +
                "Broadcast Address: 11111111.11111111.00111111.11111111\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000";
        String result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expected, result);

        ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        ipPrefix = "64";
        expected = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:FFFE:FFFE:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7333 -- FFFF:FFFE:FFFE:FFFF:FFFF:FFFF:FFFF:FFFF:FF\n" +
                "Total usable IP Addresses : 65535 \n" +
                "Subnet: 01101110.11011111.10101101.00000000.0000.11101010.00111101.11111011\n" +
                "Binary Subnet: 00111111.01101110.10101101.00000000.00111100.11101010.00111101.11111101\n" +
                "Broadcast Address: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FF\n" +
                "Prefix: /64\n" +
                "Netmask: 255.255.255.255.255.255.255.255\n" +
                "Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111";
        result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expected, result);
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String endPrefix = "28";
        int noPrefix = 5;
        String[][] expectedOutput = {{"192.168.1.1 /24", "192.168.1.1 -- 192.168.1.255"},
                {"Total usable IP Addresses :", "254"},
                {"Subnet:", "11000010.11110100.00001101.00000011"},
                {"Binary Subnet:", "11111100.11111010.00111101.00000111"},
                {"Broadcast Address:", "11111111.11111111.00111111.11111111"},
                {"Prefix:", "/28"},
                {"Netmask:", "255.255.255.252"},
                {"Binary Netmask:", "11111111.11111111.11111111.11111100"}};
        String[][] resultOutput = binaryCalculate.prefixInPrefixCalculate(ip, ipPrefix, noPrefix, endPrefix);
        assertArrayEquals(expectedOutput, resultOutput);
    }
}