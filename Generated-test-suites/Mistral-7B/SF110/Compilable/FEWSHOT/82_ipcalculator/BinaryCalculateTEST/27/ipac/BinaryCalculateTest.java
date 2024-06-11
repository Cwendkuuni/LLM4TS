package ipac.test;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import ipac.BinaryCalculate;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        assertEquals("11111010", bc.addBinary(num1, num2));
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        assertEquals("0101", bc.subBinary(num1, num2));
    }

    @Test
    public void testIPCalculateIPv4() {
        BinaryCalculate bc = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254 \n" +
                "Subnet: 11100110.11110100.01101110.00001111\n" +
                "Binary Subnet: 11100110.11110100.01101110.00001111\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000\n";
        assertEquals(expectedOutput, bc.IPCalculate(ip, ipPrefix));
    }

    @Test
    public void testIPCalculateIPv6() {
        BinaryCalculate bc = new BinaryCalculate();
        String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String ipPrefix = "60";
        String expectedOutput = "Total Range: FFFF:0DB8:85A3:0000:0000:8A2E:FFFF:FFFF\n" +
                "Usable Range: FFFF:0DB8:85A3:0000:0000:8A2E:0370:0000 -- FFFF:0DB8:85A3:0000:0000:8A2E:FFFF:FFFF\n" +
                "Total usable IP Addresses : 65536 \n" +
                "Subnet: 01111111.11011011.10101011.00000000.00001010.11101110.11110111.11111111\n" +
                "Binary Subnet: 01111111.11011011.10101011.00000000.00001010.11101110.11110111.11111111\n" +
                "Broadcast Address: FFFF:0DB8:85A3:0000:0000:8A2E:FFFF:FFFF\n" +
                "Prefix: /60\n" +
                "Netmask: FFFE::\n" +
                "Binary Netmask: 11111111.11111111.11111111.11111111.00000000.00000000.00000000.00000000\n";
        assertEquals(expectedOutput, bc.IPCalculate(ip, ipPrefix));
    }
}