package ipac;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("1111", "1010");
        assertEquals("11111010", result);

        result = binaryCalculate.addBinary("0111", "1010");
        assertEquals("11011010", result);

        result = binaryCalculate.addBinary("1111", "1111");
        assertEquals("11111111", result);

        result = binaryCalculate.addBinary("0000", "0000");
        assertEquals("0000", result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "0111";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals("1000", result);

        num1 = "0111";
        num2 = "1111";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals("10001111", result);

        num1 = "1111";
        num2 = "1111";
        result = binaryCalculate.subBinary(num1, num2);
        assertNull(result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "255.255.255.0";
        String outputText = binaryCalculate.IPCalculate(ip, ipPrefix);
        //TODO: Assert the outputText with expected values

        ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        ipPrefix = "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:c000";
        outputText = binaryCalculate.IPCalculate(ip, ipPrefix);
        //TODO: Assert the outputText with expected values
    }

    // Add more test cases for other methods in the BinaryCalculate class here
}