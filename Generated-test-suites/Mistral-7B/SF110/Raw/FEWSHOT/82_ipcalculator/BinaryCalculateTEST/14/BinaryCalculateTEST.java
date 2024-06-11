package ipac.test;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.apache.commons.cli.BinaryCalculate;
import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11110110";
        String num2 = "11111011";
        String sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("11111111", sum);

        num1 = "00110110";
        num2 = "11111011";
        sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("11111101", sum);

        num1 = "11110110";
        num2 = "00110110";
        sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("11111000", sum);

        num1 = "11110000";
        num2 = "11110000";
        sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("11111111", sum);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11110110";
        String num2 = "11111011";
        String diff = binaryCalculate.subBinary(num1, num2);
        assertEquals("00000101", diff);

        num1 = "11110110";
        num2 = "11111111";
        diff = binaryCalculate.subBinary(num1, num2);
        assertEquals("00001001", diff);

        num1 = "11110110";
        num2 = "00001001";
        diff = binaryCalculate.subBinary(num1, num2);
        assertEquals("11111111", diff);

        num1 = null;
        diff = binaryCalculate.subBinary(num1, num2);
        assertNull(diff);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String output = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertTrue(output.contains("Total Range: 192.168.1.1 -- 192.168.1.255"));
        assertTrue(output.contains("Usable Range: 192.168.1.2 -- 192.168.1.254"));
        assertTrue(output.contains("Total usable IP Addresses : 254"));

        ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        ipPrefix = "60";
        output = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertTrue(output.contains("Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7344"));
        assertTrue(output.contains("Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7335 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7343"));
        assertTrue(output.contains("Total usable IP Addresses : 65504"));
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String endPrefix = "28";
        int noPrefix = 5;
        binaryCalculate.prefixInPrefixCalculate(ip, ipPrefix, noPrefix, endPrefix);

        // Verify the output contains the expected values
        // Add your assertions here
    }
}