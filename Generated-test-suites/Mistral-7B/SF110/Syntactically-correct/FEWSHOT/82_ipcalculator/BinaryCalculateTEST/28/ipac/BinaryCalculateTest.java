package ipac;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();

        // Test cases for IPv4
        assertEquals("01111110", bc.addBinary("11111101", "11110010"));
        assertEquals("10000000", bc.addBinary("01111111", "00000000"));
        assertEquals("11111111", bc.addBinary("11111110", "00000001"));

        // Test cases for IPv6
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", bc.addBinary("2001:0db8:85a3:0000:0000:8a2e:0370:7333", "00:00:00:00:0000:0001:0000:0003"));

        // Test cases for overflow
        assertEquals("0000000000000001", bc.addBinary("1111111111111111", "1111111111111111"));
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();

        // Test cases for IPv4 and IPv6
        assertEquals("01111110", bc.subBinary("11111111", "11111101"));
        assertEquals("00000000", bc.subBinary("11111111", "11111111"));
        assertEquals("11111111", bc.subBinary("00000000", "11111110"));

        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", bc.subBinary("2001:0db8:85a3:0000:0000:8a2e:0370:7335", "2001:0db8:85a3:0000:0000:8a2e:0370:7336"));
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();

        String ip = "192.168.1.1";
        String ipPrefix = "24";

        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n"
                + "Usable Range: 192.168.1.2 -- 192.168.1.254\n"
                + "Total usable IP Addresses : 254 \n"
                + "Subnet: 11110110.11111111.01101100.00111111\n"
                + "Binary Subnet: 11111011 11111111 01101100 00111111\n"
                + "Broadcast Address: 11111111.01101100.00111111.00000000\n"
                + "Prefix: /24\n"
                + "Netmask: 255.255.255.0\n"
                + "Binary Netmask: 11111111.11111111.11111111.00000000\n";

        assertEquals(expectedOutput, bc.IPCalculate(ip, ipPrefix));

        ip = "2001:0db8:85a3:0:0:8a2e:370:7334";
        ipPrefix = "64";

        expectedOutput = "Total Range: 2001:0db8:85a3:0:0:8a2e:370:7334 -- FFFFFFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n"
                + "Usable Range: 2001:0db8:85a3:0:0:8a2e:370:7335 -- 2001:0db8:85a3:0:0:8a2e:370:7335:FFFF\n"
                + "Total usable IP Addresses : 65535 \n"
                + "Subnet: 01111110.01111101.01101011.00000000.00001110.11011110.00111111.11111111\n"
                + "Binary Subnet: 01111110.01111101.01101011.00000000.00001110.11011110.00111111.11111111\n"
                + "Broadcast Address: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n"
                + "Prefix: /64\n"
                + "Netmask: 255.255.255.255.255.255.255.255\n"
                + "Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111\n";

        assertEquals(expectedOutput, bc.IPCalculate(ip, ipPrefix));
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate bc = new BinaryCalculate();

        String ip = "192.168.1.1";
        String ipPrefix = "24";
        int noPrefix = 5;
        String endPrefix = "32";

        String[][] expectedOutput = new String[][]{
                {"192.168.1.1 /24", "192.168.1.1 -- 192.168.1.255"},
                {"Total usable IP Addresses :", "254 "},
                {"Subnet:", "11110110.11111111.01101100.00111111"},
                {"Binary Subnet:", "11111011 11111111 01101100 00111111"},
                {"Broadcast Address:", "11111111.01101100.00111111.00000000"},
                {"Prefix:", "/32"},
                {"Netmask:", "255.255.255.0"},
                {"Binary Netmask:", "11111111.11111111.11111111.00000000"},
                {"", ""},
                {"Total usable IP Addresses :", "128 "},
                {"Subnet:", "11110110.11111111.01101100.00111100"},
                {"Binary Subnet:", "11111011 11111111 01101100 00111100"},
                {"Broadcast Address:", "11111111.01101100.00111111.00000000"},
                {"Prefix:", "/32"},
                {"Netmask:", "255.255.255.0"},
                {"Binary Netmask:", "11111111.11111111.11111111.00000000"},
                {"", ""},
                {"Total usable IP Addresses :", "128 "},
                {"Subnet:", "11110110.11111111.01101100.00111101"},
                {"Binary Subnet:", "11111011 11111111 01101100 00111101"},
                {"Broadcast Address:", "11111111.01101100.00111111.00000000"},
                {"Prefix:", "/32"},
                {"Netmask:", "255.255.255.0"},
                {"Binary Netmask:", "11111111.11111111.11111111.00000000"},
                {"", ""},
                {"Total usable IP Addresses :", "128 "},
                {"Subnet:", "11110110.11111111.01101100.00111110"},
                {"Binary Subnet:", "11111011 11111111 01101100 00111110"},
                {"Broadcast Address:", "11111111.01101100.00111111.00000000"},
                {"Prefix:", "/32"},
                {"Netmask:", "255.255.255.0"},
                {"Binary Netmask:", "11111111.11111111.11111111.00000000"},
                {"", ""},
                {"Total usable IP Addresses :", "128 "},
                {"Subnet:", "11110110.11111111.01101100.00111111"},
                {"Binary Subnet:", "11111011 11111111 01101100 00111111"},
                {"Broadcast Address:", "11111111.01101100.00111111.00000000"},
                {"Prefix:", "/32"},
                {"Netmask:", "255.255.255.0"},
                {"Binary Netmask:", "11111111.11111111.11111111.00000000"},
        };

        bc.prefixInPrefixCalculate(ip, ipPrefix, noPrefix, endPrefix);

        for (int i = 0; i < expectedOutput.length; i++) {
            assertEquals(expectedOutput[i][0], bc.output[i][0]);
            assertEquals(expectedOutput[i][1], bc.output[i][1]);
        }
    }
}