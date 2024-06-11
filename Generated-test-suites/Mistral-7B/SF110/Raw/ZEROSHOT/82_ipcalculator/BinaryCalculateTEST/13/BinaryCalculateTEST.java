package ipac;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.StringTokenizer;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expectedResult = "10110";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expectedResult = "0101";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        binaryCalculate.IPCalculate(ip, ipPrefix);
        String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11000010.11111100.01101110.00001111\nBinary Subnet: 11000010.11111100.01101110.00001111\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 01111111.11111111.11111111.00000000";
        String result = binaryCalculate.getOutputText();
        assertEquals(expectedOutputText, result);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "11000010.11111100.01101110.00001111";
        String expectedResult = "11000010111111000110111000001111";
        String result = binaryCalculate.getBinaryIP(binaryIP);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ipPrefix = "24";
        String binaryIP = "11000010.11111100.01101110.00001111";
        String expectedResult = "01111111.11111111.11111111.00000000";
        String result = binaryCalculate.getNetmask(binaryIP, ipPrefix);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        binaryCalculate.calculate(ip, ipPrefix);
        String expectedIPRangeAddress = "192.168.1.1 -- 192.168.1.255";
        String expectedUsableRange = "192.168.1.2 -- 192.168.1.254";
        String expectedTotalIPAddressesString = "254";
        BigInteger expectedTotalIPAddresses = new BigInteger(expectedTotalIPAddressesString, 10);
        String expectedPrefix = "/24";
        String expectedCurrentIP = "192.168.1.255";
        String expectedBinarySubnet = "11000010.11111100.01101110.00001111";
        String expectedBinaryNetmask = "01111111.11111111.11111111.00000000";
        String expectedSubnet = "255.255.255.0";

        String ipRangeAddress = binaryCalculate.IPRangeAddress;
        String usableRange = binaryCalculate.usableRange;
        BigInteger totalIPAddresses = binaryCalculate.totalIPAddresses;
        String prefix = binaryCalculate.prefix;
        String currentIP = binaryCalculate.currentIP;
        String binarySubnet = binaryCalculate.binarySubnet;
        String netmask = binaryCalculate.netmask;

        assertEquals(expectedIPRangeAddress, ipRangeAddress);
        assertEquals(expectedUsableRange, usableRange);
        assertEquals(expectedTotalIPAddresses, totalIPAddresses);
        assertEquals(expectedPrefix, prefix);
        assertEquals(expectedCurrentIP, currentIP);
        assertEquals(expectedBinarySubnet, binarySubnet);
        assertEquals(expectedBinaryNetmask, netmask);
        assertEquals(expectedSubnet, binaryCalculate.getSubnet());
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String noPrefix = "5";
        String endPrefix = "32";
        binaryCalculate.prefixInPrefixCalculate(ip, ipPrefix, noPrefix, endPrefix);
        String[][] output = binaryCalculate.getOutput();
        String expectedOutput1 = "Usable IP Address Range:";
        String expectedOutput2 = "192.168.1.1/24 -- 192.168.1.5/24, 192.168.1.6/24 -- 192.168.1.10/24, 192.168.1.11/24 -- 192.168.1.15/24, 192.168.1.16/24 -- 192.168.1.20/24, 192.168.1.21/24 -- 192.168.1.25/24, 192.168.1.26/24 -- 192.168.1.31/24\nTotal usable IP Addresses :25\nSubnet: 11000010.11111100.01101110.00010000\nBinary Subnet: 11000010.11111100.01101110.00010000\nBroadcast Address: 192.168.1.31\nPrefix: /32\nNetmask: 255.255.255.0\nBinary Netmask: 01111111.11111111.11111111.00000000";
        String expectedOutput3 = "Subnet:";
        String expectedOutput4 = "11000010.11111100.01101110.00010000";
        String expectedOutput5 = "Binary Subnet:";
        String expectedOutput6 = "11000010111111000110111001000000";
        String expectedOutput7 = "Broadcast Address:";
        String expectedOutput8 = "192.168.1.31";
        String expectedOutput9 = "Prefix:";
        String expectedOutput10 = "/32";
        String expectedOutput11 = "Netmask:";
        String expectedOutput12 = "255.255.255.0";
        String expectedOutput13 = "Binary Netmask:";
        String expectedOutput14 = "01111111.11111111.11111111.00000000";

        String output1 = output[0][0];
        String output2 = output[1][0];
        String output3 = output[2][0];
        String output4 = output[3][0];
        String output5 = output[4][0];
        String output6 = output[5][0];
        String output7 = output[6][0];
        String output8 = output[7][0];
        String output9 = output[8][0];
        String output10 = output[9][0];
        String output11 = output[10][0];
        String output12 = output[11][0];
        String output13 = output[12][0];

        assertEquals(expectedOutput1, output1);
        assertEquals(expectedOutput2, output2);
        assertEquals(expectedOutput3, output3);
        assertEquals(expectedOutput4, output4);
        assertEquals(expectedOutput5, output5);
        assertEquals(expectedOutput6, output6);
        assertEquals(expectedOutput7, output7);
        assertEquals(expectedOutput8, output8);
        assertEquals(expectedOutput9, output9);
        assertEquals(expectedOutput10, output10);
        assertEquals(expectedOutput11, output11);
        assertEquals(expectedOutput12, output12);
        assertEquals(expectedOutput13, output13);
    }

    private String[][] getOutput() {
        return this.output;
    }

    private String getOutputText() {
        return binaryCalculate.outputText;
    }
}