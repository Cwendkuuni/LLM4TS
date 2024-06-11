package ipac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BinaryCalculateTest {
    private BinaryCalculate binaryCalculate;

    @Before
    public void setUp() {
        binaryCalculate = new BinaryCalculate();
    }

    @Test
    public void testConstructor() {
        assertNotNull(binaryCalculate);
        assertEquals(false, binaryCalculate.isIPv4);
        assertEquals(false, binaryCalculate.isIPv6);
        assertEquals("", binaryCalculate.currentIP);
        assertEquals("", binaryCalculate.fullReassembler);
        assertEquals("", binaryCalculate.DELIMITER);
        assertEquals(0, binaryCalculate.lengthOfToken);
        assertEquals("", binaryCalculate.IPRangeAddress);
        assertEquals("", binaryCalculate.usableRange);
        assertEquals(0, binaryCalculate.semiCol);
        assertEquals("", binaryCalculate.prefix);
        assertEquals(0, binaryCalculate.MAXPREFIX);
        assertEquals(0, binaryCalculate.totalIPAddresses.intValue());
        assertEquals(0, binaryCalculate.MAXPREFIX);
    }

    @Test
    public void testAddBinary_SameLength_NoCarry() {
        String expected = "1111";
        String num1 = "1111";
        String num2 = "1111";
        String actual = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddBinary_SameLength_WithCarry() {
        String expected = "10000";
        String num1 = "1111";
        String num2 = "1111";
        String actual = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, actual);
    }

    @Test
    public void testAddBinary_DifferentLengths_NoCarry() {
        String expected = "11110";
        String num1 = "1111";
        String num2 = "0001";
        String actual = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinary_DifferentLengths_WithCarry() {
        String num1 = "1111";
        String num2 = "0001";
        binaryCalculate.addBinary(num1, num2);
    }

    @Test
    public void testSubBinary_SameLength() {
        String num1 = "1111";
        String num2 = "0001";
        String expected = "1110";
        String actual = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, actual);
    }

    @Test
    public void testSubBinary_DifferentLengths() {
        String num1 = "1111";
        String num2 = "0001";
        String expected = "1110";
        String actual = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBinary_WithInvalidInput() {
        binaryCalculate.subBinary("1111", "0001");
    }

    @Test
    public void testIPCalculate_IPv4() {
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n"
                + "Usable Range: 192.168.1.2 -- 192.168.1.254\n"
                + "Total usable IP Addresses : 254 \n"
                + "Subnet: 11000010.11110100.01101001.00001111\n"
                + "Binary Subnet: 11000010.11110100.01101001.00001111\n"
                + "Broadcast Address: 192.168.1.255\n"
                + "Prefix: /24\n"
                + "Netmask: 255.255.255.0\n"
                + "Binary Netmask: 01111111.11111111.11111111.00000000\n";
        String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIPCalculate_IPv6() {
        String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String ipPrefix = "48";
        String expectedOutput = "Total Range: FFFF:0DB8:85A3:0000:0000:8A2E:0370:7334 -- FFFF:0DB8:85A3:0000:0000:8A2E:0370:7335\n"
                + "Usable Range: FFFF:0DB8:85A3:0000:0000:8A2E:0370:7335 -- FFFF:0DB8:85A3:0000:0000:8A2E:0370:7337\n"
                + "Total usable IP Addresses : 3\n"
                + "Subnet: 01111111.01111011.01010101.00000000.00001010.11101110.00111101.11011100\n"
                + "Binary Subnet: 01111111.01111011.01010101.00000000.00001010.11101110.00111101.11011100\n"
                + "Broadcast Address: FFFF:0DB8:85A3:0000:0000:8A2E:0370:7335\n"
                + "Prefix: /48\n"
                + "Netmask: FFFE:0DB8:85A3:0000:0000:8A2E:0370:7FFF\n"
                + "Binary Netmask: 11111111.11111111.11111111.00000000.00001010.11101110.11111111.11111111\n";
        String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_IPv4_InvalidIP() {
        String ip = "192.168.1.257";
        String ipPrefix = "24";
        binaryCalculate.IPCalculate(ip, ipPrefix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_IPv6_InvalidIP() {
        String ip = "2001:0db8:85a3::1";
        String ipPrefix = "48";
        binaryCalculate.IPCalculate(ip, ipPrefix);
    }
}