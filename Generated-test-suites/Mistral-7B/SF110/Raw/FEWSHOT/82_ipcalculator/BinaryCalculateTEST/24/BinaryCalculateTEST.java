package ipac;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "11111111";
        String num2 = "10101010";
        String expected = "1111111110101010";
        String result = bc.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "00000000";
        num2 = "01111111";
        expected = "01111111";
        result = bc.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "11111111";
        num2 = "11111111";
        expected = "1111111111111111";
        result = bc.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "00000000";
        num2 = "00000000";
        expected = "00000000";
        result = bc.addBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "11111111";
        String num2 = "10101010";
        String expected = "01010101";
        String result = bc.subBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "01111111";
        num2 = "11111111";
        expected = "10000000";
        result = bc.subBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "11111111";
        num2 = "11111111";
        expected = "00000000";
        result = bc.subBinary(num1, num2);
        assertEquals(expected, result);

        num1 = null;
        num2 = "11111111";
        expected = null;
        result = bc.subBinary(num1, num2);
        assertNull(result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expected = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254\n" +
                "Subnet: 11010110.11110100.01101000.00001011\n" +
                "Binary Subnet: 11111100.11111000.01110100.00010111\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000\n";
        String result = bc.IPCalculate(ip, ipPrefix);
        assertEquals(expected, result);

        ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        ipPrefix = "64";
        expected = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFFFFFF:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7333 -- FFFFFFFF:0db8:85a3:0000:0000:8a2e:0370:7333\n" +
                "Total usable IP Addresses : 65535\n" +
                "Subnet: 01111111.11010110.01010111.00000000.00000000.10101110.01110110.11001110\n" +
                "Binary Subnet: 11111111.11010110.01010111.00000000.00000000.10101110.01110110.11001110\n" +
                "Broadcast Address: FFFFFFFF:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                "Prefix: /64\n" +
                "Netmask: FFFFFFFF:0db8:85a3:0000:0000:0000:0000:0000\n" +
                "Binary Netmask: 00000000.11111111.11111111.11111111.00000000.00000000.00000000.00000000\n";
        result = bc.IPCalculate(ip, ipPrefix);
        assertEquals(expected, result);
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        String ip = "192.168.1.0";
        String ipPrefix = "24";
        int noPrefix = 5;
        String endPrefix = "32";
        String[][] expected = {
                {"192.168.1.0 /24", "192.168.1.1 -- 192.168.1.5"},
                {"192.168.1.1 /24", "192.168.1.2 -- 192.168.1.6"},
                {"192.168.1.2 /24", "192.168.1.3 -- 192.168.1.7"},
                {"192.168.1.3 /24", "192.168.1.4 -- 192.168.1.8"},
                {"192.168.1.4 /24", "192.168.1.5 -- 192.168.1.9"},
                {"Total usable IP Addresses :", "24"},
                {"Subnet:", "11010110.11110100.01101000.00010111"},
                {"Binary Subnet:", "11111100.11111000.01110100.00010111"},
                {"Broadcast Address:", "192.168.1.5"},
                {"Prefix:", "/32"},
                {"Netmask:", "255.255.255.255"},
                {"Binary Netmask:", "11111111.11111111.11111111.11111111"}
        };
        String[][] result = bc.prefixInPrefixCalculate(ip, ipPrefix, noPrefix, endPrefix);
        assertArrayEquals(expected, result);
    }
}