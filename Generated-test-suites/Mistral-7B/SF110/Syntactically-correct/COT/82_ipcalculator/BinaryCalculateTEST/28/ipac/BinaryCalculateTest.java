package ipac;

import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary_TwoZeroBinaryNumbers() {
        BinaryCalculate calculator = new BinaryCalculate();
        String result = calculator.addBinary("0011", "0110");
        assertEquals("01110", result);
    }

    @Test
    public void testAddBinary_TwoOneBinaryNumbers() {
        BinaryCalculate calculator = new BinaryCalculate();
        String result = calculator.addBinary("1111", "1111");
        assertEquals("10000", result);
    }

    @Test
    public void testAddBinary_CarryInFirstIteration() {
        BinaryCalculate calculator = new BinaryCalculate();
        String result = calculator.addBinary("1111", "1011");
        assertEquals("10111", result);
    }

    @Test
    public void testAddBinary_CarryInLastIteration() {
        BinaryCalculate calculator = new BinaryCalculate();
        String result = calculator.addBinary("1111", "0011");
        assertEquals("10011", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinary_IllegalArgumentException_FirstNumberLongerThanSecond() {
        BinaryCalculate calculator = new BinaryCalculate();
        String result = calculator.addBinary("11111111111111111111111111111111", "1111");
    }

    @Test
    public void testSubBinary_TwoZeroBinaryNumbers() {
        BinaryCalculate calculator = new BinaryCalculate();
        String result = calculator.subBinary("0011", "0110");
        assertEquals("111011", result);
    }

    @Test
    public void testSubBinary_FirstNumberLongerThanSecond() {
        BinaryCalculate calculator = new BinaryCalculate();
        String result = calculator.subBinary("11111111111111111111111111111111", "1111");
        assertEquals("11111111111111111111111111111100", result);
    }

    @Test
    public void testIPCalculate_IPv4_SubnetMask255_0_0_0_0() {
        BinaryCalculate calculator = new BinaryCalculate();
        String result = calculator.IPCalculate("192.168.0.1", "255.0.0.0");
        assertEquals("Total Range: 192.168.0.1 -- 192.168.0.1\nUsable Range: 192.168.0.1 -- 192.168.0.1\nTotal usable IP Addresses : 1 \nSubnet: 192.168.0.0\nBinary Subnet: 11101100.00001111.00000000.00000001\nBroadcast Address: 192.168.0.1\nPrefix: /32\nNetmask: 0.0.0.0\nBinary Netmask: 00000000.00000000.00000000.00000000", result);
    }

    @Test
    public void testIPCalculate_IPv4_SubnetMask255_255_255_0() {
        BinaryCalculate calculator = new BinaryCalculate();
        String result = calculator.IPCalculate("192.168.0.1", "255.255.255.0");
        assertEquals("Total Range: 192.168.0.1 -- 192.168.1.255\nUsable Range: 192.168.0.1 -- 192.168.0.254\nTotal usable IP Addresses : 254 \nSubnet: 192.168.0.0\nBinary Subnet: 11101100.00111111.00000000.00000111\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.00000000.00001111", result);
    }

    @Test
    public void testIPCalculate_IPv4_SubnetMask255_255_255_255() {
        BinaryCalculate calculator = new BinaryCalculate();
        try {
            String result = calculator.IPCalculate("192.168.0.1", "255.255.255.255");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIPCalculate_IPv6_SubnetMask65535_0_0_0_0_0_0_0_0() {
        BinaryCalculate calculator = new BinaryCalculate();
        calculator.isIPv6 = true;
        String result = calculator.IPCalculate("2001:0db8:85a3:0000:0000:8a2e:0370:7334", "65535.0.0.0.0.0.0.0");
        assertEquals("Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\nUsable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:0db8:85a3:0000:0000:8a2e:0370:7334\nTotal usable IP Addresses : 65536 \nSubnet: 2001:0db8:85a3:0000:0000:0000:0000:0000\nBinary Subnet: 001:0101:1101:1011:1111:1111:1111:1111\nBroadcast Address: FFFF:0db8:85a3:0000:0000:8a2e:0370:7334\nPrefix: /64\nNetmask: 65535.0.0.0.0.0.0.0\nBinary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111101", result);
    }

    @Test
    public void testIPCalculate_IPv6_SubnetMask65535_FFFF_FFFF_FFFF_FFFF_FFFF_FFFF_FFFF() {
        BinaryCalculate calculator = new BinaryCalculate();
        calculator.isIPv6 = true;
        try {
            String result = calculator.IPCalculate("2001:0db8:85a3:0000:0000:8a2e:0370:7334", "FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}