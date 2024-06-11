package ipac.test;

import static org.junit.Assert.*;

import ipac.BinaryCalculate;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        assertEquals("11111010", binaryCalculate.addBinary(num1, num2));
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        assertEquals("0101", binaryCalculate.subBinary(num1, num2));
    }

    @Test
    public void testIPCalculate_IPv4() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.1.1";
        String IPPrefix = "24";
        String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254\n" +
                "Subnet: 11100010.11111100.01101100.00001111\n" +
                "Binary Subnet: 11100010.11111100.01101100.00001111\n" +
                "Broadcast Address: 11100010.11111100.01101100.11111111\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 01111111.11111111.11111111.00000000\n";
        assertEquals(expectedOutputText, binaryCalculate.IPCalculate(IP, IPPrefix));
    }

    @Test
    public void testIPCalculate_IPv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String IPPrefix = "60";
        String expectedOutputText = "Total Range: FEDC:BA98:85A3:0000:0000:8A2E:0370:7334\n" +
                "Usable Range: FEDC:BA98:85A3:0000:0000:8A2E:0370:7333\n" +
                "Total usable IP Addresses : 65534\n" +
                "Subnet: 01111111.11011110.10101101.00000000.00001101.10101101.01110111.01111110\n" +
                "Binary Subnet: 01111111.11011110.10101101.00000000.00001101.10101101.01110111.01111110\n" +
                "Broadcast Address: FEDC:BA98:85A3:0000:0000:8A2E:0370:7335\n" +
                "Prefix: /60\n" +
                "Netmask: FE80::\n" +
                "Binary Netmask: 11111111.11111111.11111111.11111111.00000000.00000000.00000000.00001111\n";
        assertEquals(expectedOutputText, binaryCalculate.IPCalculate(IP, IPPrefix));
    }
}