package ipac.test;

import static org.junit.Assert.*;

import ipac.BinaryCalculate;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "11111010";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "0101";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "255.255.255.252";
        String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.7\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.6\n" +
                "Total usable IP Addresses : 4 \n" +
                "Subnet: 11111110.11111111.11111111.11111111\n" +
                "Binary Subnet: 11111110\n" +
                "Broadcast Address: 11111111.11111111.11111111.11111111\n" +
                "Prefix: /27\n" +
                "Netmask: 255.255.255.224\n" +
                "Binary Netmask: 0.11110110\n";
        String result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutputText, result);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String expected = "01100001.11101100.11010101.00000001";
        String result = binaryCalculate.getBinaryIP(ip);
        assertEquals(expected, result);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ipPrefix = "255.255.255.252";
        String expected = "0.11110111.11111111.11111111.11110110";
        String result = binaryCalculate.getNetmask(ipPrefix);
        assertEquals(expected, result);
    }
}