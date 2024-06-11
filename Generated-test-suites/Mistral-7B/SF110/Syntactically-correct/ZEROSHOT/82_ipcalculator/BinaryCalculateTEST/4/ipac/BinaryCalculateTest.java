package ipac;

import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "11110101";
        String num2 = "11010101";
        String expected = "11111010";
        String result = bc.addBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "00001111";
        num2 = "00111111";
        expected = "01011111";
        result = bc.addBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "11110101";
        String num2 = "11010101";
        String expected = "00100000";
        String result = bc.subBinary(num1, num2);
        assertEquals(expected, result);

        num1 = "01010101";
        num2 = "11111111";
        expected = "10101010";
        result = bc.subBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254 \n" +
                "Subnet: 11011010.11111111.00111010.00010001\n" +
                "Binary Subnet: 11111111.11111111.11110110.00100101\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000";
        String resultOutput = bc.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, resultOutput);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate bc = new BinaryCalculate();
        String binaryIP = "11111111.11111111.11111111.11111111";
        String expected = "11111111111111111111111111111111";
        String result = bc.getBinaryIP(binaryIP);
        assertEquals(expected, result);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate bc = new BinaryCalculate();
        String binaryIP = "255.255.255.0";
        String expected = "00000000.00000000.00000111.11111100";
        String result = bc.getNetmask(binaryIP);
        assertEquals(expected, result);
    }
}