package ipac.test;

import org.junit.Test;
import static org.junit.Assert.*;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

    private static final String VALID_IP = "192.168.1.1";
    private static final String VALID_IP_PREFIX = "/24";

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String expected = "1111";
        String result = binaryCalculate.addBinary("1110", "0110");
        assertEquals(expected, result);

        // Edge case testing
        String emptyString1 = "";
        String emptyString2 = "";
        String allZeroString1 = "0000";
        String allZeroString2 = "0000";
        String differentLengthString1 = "1111";
        String differentLengthString2 = "11101";

        String resultEmpty1Empty2 = binaryCalculate.addBinary(emptyString1, emptyString2);
        assertEquals(emptyString1, resultEmpty1Empty2);

        String resultAllZero1AllZero2 = binaryCalculate.addBinary(allZeroString1, allZeroString2);
        assertEquals(allZeroString1, resultAllZero1AllZero2);

        String resultDifferentLength1DifferentLength2 = binaryCalculate.addBinary(differentLengthString1, differentLengthString2);
        assertEquals("11111", resultDifferentLength1DifferentLength2);

        String resultZero1AllZero2 = binaryCalculate.addBinary("0", allZeroString2);
        assertEquals(allZeroString2, resultZero1AllZero2);

        String resultAllZero1Zero1 = binaryCalculate.addBinary(allZeroString1, "1");
        assertEquals("1", resultAllZero1Zero1);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String expected = "0111";
        String result = binaryCalculate.subBinary("1111", "1000");
        assertEquals(expected, result);

        // Edge case testing
        String sameLengthString1SameDigitString2 = "1111";
        String sameLengthString1DifferentDigitString2 = "1110";
        String sameLengthString1ZeroString2 = "1111";
        String zeroString1ZeroString2 = "";

        String resultSameLength1SameDigit2 = binaryCalculate.subBinary(sameLengthString1SameDigitString2, sameLengthString1SameDigitString2);
        assertEquals("0000", resultSameLength1SameDigit2);

        String resultSameLength1DifferentDigit2 = binaryCalculate.subBinary(sameLengthString1DifferentDigitString2, sameLengthString1DifferentDigitString2);
        assertEquals("0000", resultSameLength1DifferentDigit2);

        String resultSameLength1Zero2 = binaryCalculate.subBinary(sameLengthString1ZeroString2, zeroString1ZeroString2);
        assertEquals(sameLengthString1ZeroString2, resultSameLength1Zero2);

        String resultZero1Zero2 = binaryCalculate.subBinary(zeroString1ZeroString2, zeroString1ZeroString2);
        assertEquals(zeroString1ZeroString2, resultZero1ZeroString2);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254 \n" +
                "Subnet: 11000010.11110000.01101100.00001111\n" +
                "Binary Subnet: 11000010.11110000.01101100.00001111\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000";

        String result = binaryCalculate.IPCalculate(VALID_IP, VALID_IP_PREFIX);
        assertEquals(expectedOutput, result);

        // Edge case testing
        String invalidIP = "256.256.256.256";
        String invalidIPPrefix = "/65";

        try {
            binaryCalculate.IPCalculate(invalidIP, invalidIPPrefix);
            fail("Expected an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}