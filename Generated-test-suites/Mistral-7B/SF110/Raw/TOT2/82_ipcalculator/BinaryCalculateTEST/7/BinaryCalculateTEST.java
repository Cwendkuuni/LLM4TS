package ipac.test;

import org.junit.Test;
import static org.junit.Assert.*;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {
    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "0011";
        String num2 = "0110";
        String expected = "1111";
        assertEquals(expected, binaryCalculate.addBinary(num1, num2));

        num1 = "1111";
        num2 = "1111";
        expected = "10000";
        assertEquals(expected, binaryCalculate.addBinary(num1, num2));

        num1 = "1111";
        num2 = "0000";
        expected = "1111";
        assertEquals(expected, binaryCalculate.addBinary(num1, num2));

        num1 = "1101";
        num2 = "1010";
        expected = "10111";
        assertEquals(expected, binaryCalculate.addBinary(num1, num2));

        num1 = "1111";
        num2 = "0111";
        expected = "11000";
        assertEquals(expected, binaryCalculate.addBinary(num1, num2));

        num1 = "0000";
        num2 = "1111";
        expected = "1111";
        assertEquals(expected, binaryCalculate.addBinary(num1, num2));
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "0101";
        assertEquals(expected, binaryCalculate.subBinary(num1, num2));

        num1 = "1010";
        num2 = "1111";
        expected = "0001";
        assertEquals(expected, binaryCalculate.subBinary(num1, num2));

        num1 = "1111";
        num2 = "1111";
        assertEquals(expected, binaryCalculate.subBinary(num1, num2));

        num1 = "1111";
        num2 = "0000";
        expected = "1111";
        assertEquals(expected, binaryCalculate.subBinary(num1, num2));

        num1 = "0000";
        num2 = "1111";
        expected = "1111";
        assertEquals(expected, binaryCalculate.subBinary(num1, num2));
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.0";
        String prefix = "24";
        String expected = "192.168.1.0 -- 192.168.1.255";
        assertEquals(expected, binaryCalculate.IPCalculate(ip, prefix));

        ip = "2001:db8:85a3:0:0:8a2e:370:7334";
        prefix = "60";
        expected = "2001:db8:85a3:0:0:8a2e:370:7334 -- ffff:db8:85a3:0:0:8a2e:370:7334";
        assertEquals(expected, binaryCalculate.IPCalculate(ip, prefix));

        ip = "192.168.1.256";
        prefix = "24";
        assertEquals("IP address is not valid", binaryCalculate.IPCalculate(ip, prefix));

        ip = "256.256.256.256";
        prefix = "32";
        assertEquals("IP address is not valid", binaryCalculate.IPCalculate(ip, prefix));

        ip = "192.168.1.0";
        prefix = "33";
        assertEquals("Prefix length is not valid", binaryCalculate.IPCalculate(ip, prefix));

        ip = "192.168.1.0";
        prefix = "8";
        assertEquals("Total Range: 192.168.1.0 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.1 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254 ", binaryCalculate.IPCalculate(ip, prefix));

        ip = "2001:db8:85a3::";
        prefix = "64";
        assertEquals("Total Range: 2001:db8:85a3:: -- ffff:db8:85a3::\n" +
                "Usable Range: 2001:db8:85a3:: -- ffff:db8:85a3:ffff:ffff:ffff:ffff:ffff\n" +
                "Total usable IP Addresses : 65536", binaryCalculate.IPCalculate(ip, prefix));
    }
}