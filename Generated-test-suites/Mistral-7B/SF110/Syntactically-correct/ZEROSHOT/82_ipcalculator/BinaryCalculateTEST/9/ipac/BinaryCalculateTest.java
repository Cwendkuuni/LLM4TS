package ipac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

public class BinaryCalculateTest {

    private BinaryCalculate binaryCalculate;

    @Before
    public void setUp() {
        binaryCalculate = new BinaryCalculate();
    }

    @Test
    public void testAddBinary_TwoZeroStrings() {
        String num1 = "0";
        String num2 = "0";
        String expected = "0";

        String actual = binaryCalculate.addBinary(num1, num2);

        assertEquals(expected, actual);
    }

    @Test
    public void testAddBinary_TwoOneStrings() {
        String num1 = "1";
        String num2 = "1";
        String expected = "10";

        String actual = binaryCalculate.addBinary(num1, num2);

        assertEquals(expected, actual);
    }

    @Test
    public void testAddBinary_StringWithLongerLength() {
        String num1 = "1111";
        String num2 = "1";

        String expected = "11111";

        String actual = binaryCalculate.addBinary(num1, num2);

        assertEquals(expected, actual);
    }

    @Test
    public void testSubBinary_TwoOneStrings() {
        String num1 = "1";
        String num2 = "1";
        String expected = "0";

        String actual = binaryCalculate.subBinary(num1, num2);

        assertEquals(expected, actual);
    }

    @Test
    public void testSubBinary_StringWithLongerLength() {
        String num1 = "1111";
        String num2 = "1";

        String expected = "1110";

        String actual = binaryCalculate.subBinary(num1, num2);

        assertEquals(expected, actual);
    }

    @Test
    public void testIPCalculate_ValidIPAddressAndPrefix() {
        String ipAddress = "192.168.1.1";
        String prefix = "24";

        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\nSubnet: 11010110.11101000.01101001.00000111\nBinary Subnet: 11101110.11110100.01110111.00001111\nBroadcast Address: 11111111.11111111.01111111.00010000\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.11111111.00000000";

        String actualOutput = binaryCalculate.IPCalculate(ipAddress, prefix);

        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = AssertionError.class)
    public void testIPCalculate_InvalidIPAddressAndPrefix() {
        String ipAddress = "192.168.1.257";
        String prefix = "24";

        binaryCalculate.IPCalculate(ipAddress, prefix);
    }

    @Test
    public void testCalculate_ValidIPAddressAndPrefix() {
        String subnet = "192.168.1.1";
        String prefixLength = "24";

        binaryCalculate.calculate(subnet, prefixLength);

        assertNotNull(binaryCalculate.getIPRangeAddress());
        assertNotNull(binaryCalculate.getUsableRange());
        assertNotNull(binaryCalculate.getCurrentIP());
        assertNotNull(binaryCalculate.getTotalIPAddresses());
    }

    @Test
    public void testGetBinaryIP_ValidIPAddress() {
        String ipAddress = "192.168.1.1";

        String expected = "01101110.11110100.01101001.00000111";

        String actual = binaryCalculate.getBinaryIP(ipAddress);

        assertEquals(expected, actual);
    }

    @Test
    public void testGetNetmask_ValidIPPrefix() {
        String ipPrefix = "24";

        String expected = "11111111.11111111.11111111.00000000";

        String actual = binaryCalculate.getNetmask(ipPrefix);

        assertEquals(expected, actual);
    }

    @Test
    public void testCalculateTotalIPAddresses() {
        String ipAddress = "255.255.255.255";

        binaryCalculate.calculate(ipAddress, "32");

        BigInteger expected = new BigInteger("4294967295");

        BigInteger actual = binaryCalculate.getTotalIPAddresses();

        assertEquals(expected, actual);
    }

    @Test
    public void testCheckIPType_IPv4() {
        binaryCalculate.checkIPType(31);

        assertTrue(binaryCalculate.isIPv4);
        assertEquals(8, binaryCalculate.lengthOfToken);
        assertEquals(".", binaryCalculate.DELIMITER);
        assertEquals(32, binaryCalculate.MAXPREFIX);
    }

    @Test
    public void testCheckIPType_IPv6() {
        binaryCalculate.checkIPType(127);

        assertFalse(binaryCalculate.isIPv4);
        assertEquals(16, binaryCalculate.lengthOfToken);
        assertEquals(":", binaryCalculate.DELIMITER);
        assertEquals(128, binaryCalculate.MAXPREFIX);
    }
}