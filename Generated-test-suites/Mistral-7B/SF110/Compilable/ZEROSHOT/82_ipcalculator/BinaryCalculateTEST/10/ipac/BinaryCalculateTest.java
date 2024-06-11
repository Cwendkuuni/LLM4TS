package ipac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String num1 = "1111";
        String num2 = "1010";
        String expectedSum = "11001";
        String actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);

        num1 = "1010";
        num2 = "1111";
        expectedSum = "11110";
        actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);

        num1 = "0000";
        num2 = "0000";
        expectedSum = "0000";
        actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);

        num1 = "1111";
        num2 = "0000";
        expectedSum = "1111";
        actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);

        num1 = "1010";
        num2 = "1111";
        expectedSum = "11011";
        actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);

        num1 = "1111";
        num2 = "1111";
        expectedSum = "10000";
        actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);

        num1 = "1111";
        num2 = "1111";
        expectedSum = "11110";
        actualSum = binaryCalculate.addBinary(num1, num2);
        assertFalse(expectedSum.equals(actualSum));
        assertEquals("10000", actualSum);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String num1 = "1111";
        String num2 = "0010";
        String expectedDiff = "11011";
        String actualDiff = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDiff, actualDiff);

        num1 = "0111";
        num2 = "1010";
        expectedDiff = "01011";
        actualDiff = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDiff, actualDiff);

        num1 = "1111";
        num2 = "1111";
        expectedDiff = "0000";
        actualDiff = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDiff, actualDiff);

        num1 = "1010";
        num2 = "1010";
        expectedDiff = "0000";
        actualDiff = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDiff, actualDiff);

        num1 = "1111";
        num2 = "0000";
        expectedDiff = "1111";
        actualDiff = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDiff, actualDiff);

        num1 = "0000";
        num2 = "1111";
        expectedDiff = "11111111111111111111110110110101010100111111011011001101100110110011";
        actualDiff = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDiff.substring(0, 16), actualDiff.substring(0, 16));
        assertEquals(expectedDiff.substring(16), actualDiff.substring(16));
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                                    "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                                    "Total usable IP Addresses : 254 \n" +
                                    "Subnet: 192.168.1.0\n" +
                                    "Binary Subnet: 11101110.11110100.00001101.00001111\n" +
                                    "Broadcast Address: 192.168.1.255\n" +
                                    "Prefix: /24\n" +
                                    "Netmask: 255.255.255.0\n" +
                                    "Binary Netmask: 11111111.11111111.11111111.00000000";
        String actualOutputText = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutputText, actualOutputText);

        ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        ipPrefix = "60";
        expectedOutputText = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:0db8:85a3:0000:0000:8a2e:0370:7335\n" +
                              "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7335 -- FFFF:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                              "Total usable IP Addresses : 65536 \n" +
                              "Subnet: 2001:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                              "Binary Subnet: 00111110.11011011.10101101.00000000.00001110.11011110.01110100.11011111\n" +
                              "Broadcast Address: FFFF:0db8:85a3:0000:0000:8a2e:0370:7335\n" +
                              "Prefix: /60\n" +
                              "Netmask: FFFE:: \n" +
                              "Binary Netmask: 11111111.11111111.11111111.11111111.00000000.00000000.00000000.00000000";
        actualOutputText = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutputText, actualOutputText);
    }

    @Test
    public void testConstructor() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        assertNull(binaryCalculate.currentIP);
        assertNull(binaryCalculate.fullReassembler);
        assertNull(binaryCalculate.DELIMITER);
        assertEquals(0, binaryCalculate.lengthOfToken);
        assertNull(binaryCalculate.IPRangeAddress);
        assertNull(binaryCalculate.usableRange);
        assertEquals(0, binaryCalculate.semiCol);
        assertNull(binaryCalculate.prefix);
        assertEquals(0, binaryCalculate.MAXPREFIX);
        assertNull(binaryCalculate.totalIPAddresses);
        assertEquals(0, binaryCalculate.MAXPREFIX);
    }
}

class Output {
    // Placeholder for actual implementation
}