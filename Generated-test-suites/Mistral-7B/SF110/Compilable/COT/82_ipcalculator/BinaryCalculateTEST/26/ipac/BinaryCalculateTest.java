package ipac.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;
import java.text.NumberFormat;

import org.junit.Before;
import org.junit.Test;

import ipac.BinaryCalculate;

public class BinaryCalculateTest {

    private BinaryCalculate binaryCalculate;

    @Before
    public void setUp() {
        binaryCalculate = new BinaryCalculate();
    }

    @Test
    public void testAddBinary() {
        String num1 = "1111";
        String num2 = "1010";
        String expectedResult = "11110101";

        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "0000";
        num2 = "1111";
        expectedResult = "11110000";

        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "1111";
        num2 = "0000";
        expectedResult = "11110000";

        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "1111";
        num2 = "1111";
        expectedResult = "11111111";

        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "1000";
        num2 = "1010";
        expectedResult = "10101000";

        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "1111";
        num2 = "11111111";
        expectedResult = "1000000000000000";

        result = binaryCalculate.addBinary(num1, num2);
        assertNull(result);

        num1 = "1111";
        num2 = "1111";
        expectedResult = "11111111";

        result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSubBinary() {
        String num1 = "1111";
        String num2 = "1010";
        String expectedResult = "0101";

        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "1010";
        num2 = "1111";
        expectedResult = "10011111";

        result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "1111";
        num2 = "0000";
        expectedResult = "11110000";

        result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedResult, result);

        num1 = "1111";
        num2 = "1111";
        expectedResult = "0000";

        result = binaryCalculate.subBinary(num1, num2);
        assertNull(result);

        num1 = "0111";
        num2 = "0111";
        expectedResult = "0000";

        result = binaryCalculate.subBinary(num1, num2);
        assertNull(result);

        num1 = "1111";
        num2 = "11111111";
        expectedResult = "0000000000000000";

        result = binaryCalculate.subBinary(num1, num2);
        assertNull(result);
    }

    @Test
    public void testIPCalculate() {
        String ip = "192.168.1.1";
        String ipPrefix = "24";

        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n"
                + "Usable Range: 192.168.1.2 -- 192.168.1.254\n"
                + "Total usable IP Addresses : 254 \n"
                + "Subnet: 11001100.01101000.00010001.00000011\n"
                + "Binary Subnet: 11111100.11010100.00101101.00111101\n"
                + "Broadcast Address: 192.168.1.255\n"
                + "Prefix: /24\n"
                + "Netmask: 255.255.255.0\n"
                + "Binary Netmask: 11111111.11111111.11111111.00000000\n";

        String result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, result);

        ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        ipPrefix = "60";

        expectedOutput = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n"
                + "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7335 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n"
                + "Total usable IP Addresses : 65534 \n"
                + "Subnet: 01111101.11011111.10101011.00000000.0000.11101110.01110111.11010110\n"
                + "Binary Subnet: 11111101.11111111.10101011.00000000.0000.11101110.01110111.11010110\n"
                + "Broadcast Address: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n"
                + "Prefix: /60\n"
                + "Netmask: FFFE::\n"
                + "Binary Netmask: 11111111.11111111.11111111.11111111.0000.0000.0000.0000\n";

        result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculateInvalidIP() {
        String ip = "192.168.1.257";
        String ipPrefix = "24";

        binaryCalculate.IPCalculate(ip, ipPrefix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculateInvalidIPPrefix() {
        String ip = "192.168.1.1";
        String ipPrefix = "33";

        binaryCalculate.IPCalculate(ip, ipPrefix);
    }
}