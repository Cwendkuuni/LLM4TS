package ipac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "11111010";
        String result = bc.addBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "0101";
        String result = bc.subBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                                    "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                                    "Total usable IP Addresses : 254 \n" +
                                    "Subnet: 11011010.11111100.00111000.00001111\n" +
                                    "Binary Subnet: 11111111.11111000.00111000.00001111\n" +
                                    "Broadcast Address: 192.168.1.255\n" +
                                    "Prefix: /24\n" +
                                    "Netmask: 255.255.255.0\n" +
                                    "Binary Netmask: 00111111.11111111.00111000.00000000";

        String resultOutputText = bc.IPCalculate(ip, ipPrefix);
        assertNotNull(resultOutputText);
        assertEquals(expectedOutputText, resultOutputText);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate bc = new BinaryCalculate();
        String binaryIP = "11111111.11111111.00111000.00001111";
        String expected = "11111111111111110011100000001111";
        String result = bc.getBinaryIP(binaryIP);
        assertEquals(expected, result);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate bc = new BinaryCalculate();
        String binaryIP = "11111111.11111111.00111000.00001111";
        String ipPrefix = "24";
        String expectedNetmask = "00111111.11111111.00111000.00000000";
        String resultNetmask = bc.getNetmask(binaryIP);
        assertEquals(expectedNetmask, resultNetmask);
    }

}