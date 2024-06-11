package ipac;

import static org.junit.Assert.*;

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
        String ipPrefix = "24";
        String expected = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                          "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                          "Total usable IP Addresses : 254 \n" +
                          "Subnet: 11000010.11110100.01101100.00001111\n" +
                          "Binary Subnet: 11111100.11111010.01101100.00001111\n" +
                          "Broadcast Address: 192.168.1.255\n" +
                          "Prefix: /24\n" +
                          "Netmask: 255.255.255.0\n" +
                          "Binary Netmask: 11111111.11111111.11111111.00000000";
        String result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expected, result);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "11111111.11111111.01101100.00001111";
        String expected = "11111111111111110110110000001111";
        String result = binaryCalculate.getBinaryIP(binaryIP);
        assertEquals(expected, result);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "11111111.11111111.01101100.00001111";
        String ipPrefix = "24";
        String expected = "00111111.00111111.11011011.11110000";
        String result = binaryCalculate.getNetmask(binaryIP + " " + ipPrefix);
        assertEquals(expected, result);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String subnet = "11000010.11110100.01101100.00001111";
        String noOfIPAddresses = "255";
        String expected = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                          "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                          "Total usable IP Addresses : 254 \n" +
                          "Subnet: 11000010.11110100.01101100.00001111\n" +
                          "Binary Subnet: 11111100.11111010.01101100.00001111\n" +
                          "Broadcast Address: 192.168.1.255\n" +
                          "Prefix: /24\n" +
                          "Netmask: 255.255.255.0\n" +
                          "Binary Netmask: 11111111.11111111.11111111.00000000";
        binaryCalculate.calculate(subnet, noOfIPAddresses);
        String result = binaryCalculate.IPCalculate(subnet, ipPrefix);
        assertEquals(expected, result);
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String noPrefix = "5";
        String endPrefix = "32";
        String[][] expectedOutput = {{"192.168.1.1 /24", "192.168.1.1 -- 192.168.1.255"},
                                     {"Total usable IP Addresses :", "254 "},
                                     {"Subnet:", "11000010.11110100.01101100.00001111"},
                                     {"Binary Subnet:", "11111100.11111010.01101100.00001111"},
                                     {"Broadcast Address:", "192.168.1.255"},
                                     {"Prefix:", "/32"},
                                     {"Netmask:", "255.255.255.255"},
                                     {"Binary Netmask:", "11111111.11111111.11111111.11111111"}};
        binaryCalculate.prefixInPrefixCalculate(ip, ipPrefix, noPrefix, endPrefix);
        String[][] resultOutput = binaryCalculate.getOutput();
        for (int i = 0; i < resultOutput.length; i++) {
            assertArrayEquals(expectedOutput[i], resultOutput[i]);
        }
    }
}