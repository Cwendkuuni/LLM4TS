package ipac.test;

import static org.junit.Assert.*;

import ipac.BinaryCalculate;

import java.math.BigInteger;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("11110101", sum);

        num1 = "1000";
        num2 = "1111";
        sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("11110000", sum);

        num1 = "0101";
        num2 = "1010";
        sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("1101", sum);

        num1 = "1111";
        num2 = "1111";
        sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("11111111", sum);

        num1 = "0000";
        num2 = "0000";
        sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("0000", sum);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String diff = binaryCalculate.subBinary(num1, num2);
        assertEquals("0101", diff);

        num1 = "0111";
        num2 = "1110";
        diff = binaryCalculate.subBinary(num1, num2);
        assertEquals("1001", diff);

        num1 = "1001";
        num2 = "1001";
        diff = binaryCalculate.subBinary(num1, num2);
        assertEquals("0000", diff);

        num1 = "1111";
        num2 = "0000";
        diff = binaryCalculate.subBinary(num1, num2);
        assertEquals("1111", diff);

        num1 = "0000";
        num2 = "1111";
        try {
            diff = binaryCalculate.subBinary(num1, num2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String output = binaryCalculate.IPCalculate(ip, ipPrefix);
        String expected = "Total Range: 192.168.1.1 -- 192.168.1.255\n"
                         + "Usable Range: 192.168.1.2 -- 192.168.1.254\n"
                         + "Total usable IP Addresses : 254 \n"
                         + "Subnet: 11010110.01111100.01110100.00001111\n"
                         + "Binary Subnet: 11111010.11111011.11110100.00111111\n"
                         + "Broadcast Address: 11111111.01111100.01110100.00001111\n"
                         + "Prefix: /24\n"
                         + "Netmask: 255.255.255.0\n"
                         + "Binary Netmask: 11111111.11111111.11111111.00000000\n";
        assertEquals(expected, output);

        ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        ipPrefix = "64";
        output = binaryCalculate.IPCalculate(ip, ipPrefix);
        expected = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:DBFF:FBFF:FFFE:FFFF:FFFF:FFFF:FFFF\n"
                 + "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7333 -- FFFF:DBFF:FBFF:FFFE:FFFF:FFFF:FFFF:FFFF\n"
                 + "Total usable IP Addresses : 65534 \n"
                 + "Subnet: 01111110.11011101.10101111.00000000.00001110.11101101.01111110.00111111\n"
                 + "Binary Subnet: 11111010.01101101.10110111.00000000.00111100.11101101.10111100.11111101\n"
                 + "Broadcast Address: FFFF:1111:1111:1111:FFFF:FFFF:FFFF:FFFF\n"
                 + "Prefix: /64\n"
                 + "Netmask: 255.255.255.255.255.255.255.255\n"
                 + "Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111\n";
        assertEquals(expected, output);
    }

}