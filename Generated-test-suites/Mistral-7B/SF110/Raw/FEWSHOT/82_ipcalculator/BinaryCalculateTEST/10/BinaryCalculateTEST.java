package org.ipac; // Assuming the package name is ipac instead of ipac.ipac

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigInteger;
import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        assertEquals("1111", binaryCalculate.addBinary("1110", "0011"));
        assertEquals("101010", binaryCalculate.addBinary("1010", "1010"));
        assertEquals("10111111", binaryCalculate.addBinary("10111100", "00000111"));
        assertEquals("00000000", binaryCalculate.addBinary("11111111", "11111111"));
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        assertEquals("0111", binaryCalculate.subBinary("1010", "0111"));
        assertEquals("110011", binaryCalculate.subBinary("111110", "110101"));
        assertEquals("11111111", binaryCalculate.subBinary("11111111", "00000000"));
        assertNull(binaryCalculate.subBinary(null, "1111"));
    }

    @Test
    public void testIPCalculateIpv4() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.checkIPType(31);
        binaryCalculate.IPCalculate("192.168.1.1", "24");
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                                "Total usable IP Addresses : 254\n" +
                                "Subnet: 192.168.1\n" +
                                "Binary Subnet: 11000010.10101000.01110100.00001111\n" +
                                "Broadcast Address: 192.168.1.255\n" +
                                "Prefix: /24\n" +
                                "Netmask: 255.255.255.0\n" +
                                "Binary Netmask: 11111111.11111111.11111111.00000000\n";
        assertEquals(expectedOutput, binaryCalculate.getOutputText());
    }

    @Test
    public void testIPCalculateIpv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.checkIPType(127);
        binaryCalculate.IPCalculate("2001:0db8:85a3:0000:0000:8a2e:0370:7334", "60");
        String expectedOutput = "Total Range: FEDC:BA98:85A3:0000:0000:8A2E:0370:7334\n" +
                                "Usable Range: FEDC:BA98:85A3:0000:0000:8A2E:0370:7332\n" +
                                "Total usable IP Addresses : 62\n" +
                                "Subnet: FEDC:BA98:85A3:0:0:8A2E:0\n" +
                                "Binary Subnet: 11111111.11110101.11010111.00000000.00001010.01111110.00110100.00110010\n" +
                                "Broadcast Address: FEDC:BA98:85A3:0:0:8A2E:3\n" +
                                "Prefix: /60\n" +
                                "Netmask: FE80:0:0:0:0:0:FF00:C000\n" +
                                "Binary Netmask: 11111111.11111111.11111111.00000000.00000000.FFFF0000.C0000000\n";
        assertEquals(expectedOutput, binaryCalculate.getOutputText());
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.checkIPType(31);
        binaryCalculate.prefixInPrefixCalculate("192.168.1.1", "24", 5, "30");
        String expectedOutput = "Total usable IP Addresses : 30\n" +
                                "Subnet: 192.168.1\n" +
                                "Binary Subnet: 11000010.10101000.01110100.00001111\n" +
                                "Broadcast Address: 192.168.1.30\n" +
                                "Prefix: /30\n" +
                                "Netmask: 255.255.255.252\n" +
                                "Binary Netmask: 11111111.11111111.11111111.11111100\n";
        assertEquals(expectedOutput, binaryCalculate.getOutputText());
    }

    private String getOutputText() {
        return new String(this.fullReassembler.getBytes());
    }
}