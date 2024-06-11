package ipac;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expected = "11110101";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testSubBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expected = "0101";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testIPCalculate() {
 String ip = "192.168.1.1";
 String ipPrefix = "24";
 String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.1 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11101100.11110100.01101000.00000111\nBinary Subnet: 11110110.11111010.01101000.00001111\nBroadcast Address: 11111111.11111111.01101000.11111111\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.11111111.00000000";
 String resultOutputText = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertEquals(expectedOutputText, resultOutputText);
 }

 @Test
 public void testGetBinaryIP() {
 String binaryIP = "11110110.11111010.01101000.00001111";
 String expected = "11110110111110100110100000001111";
 String result = binaryCalculate.getBinaryIP(binaryIP);
 assertEquals(expected, result);
 }

 @Test
 public void testGetNetmask() {
 String binaryIP = "11110110.11111010.01101000.00001111";
 String expectedNetmask = "00001101.00101100.10010100.11110000";
 String resultNetmask = binaryCalculate.getNetmask(binaryIP);
 String expectedBinaryNetmask = "00001101.00101100.10010100.11100000";
 String resultBinaryNetmask = binaryCalculate.getBinaryIP(resultNetmask);
 assertEquals(expectedNetmask, resultNetmask);
 assertEquals(expectedBinaryNetmask, resultBinaryNetmask);
 }

 @Test
 public void testPrefixInPrefixCalculate() {
 String ip = "192.168.1.1";
 String ipPrefix = "24";
 int noPrefix = 5;
 String endPrefix = "32";
 String[][] expectedOutput = new String[][] {
 {"192.168.1.1 /24", "192.168.1.1 -- 192.168.1.255"},
 {"Total usable IP Addresses :", "254"},
 {"Subnet:", "11101100.11110100.01101000.00000111"},
 {"Binary Subnet:", "11110110.11111010.01101000.00001111"},
 {"Broadcast Address:", "11111111.11111111.01101000.11111111"},
 {"Prefix:", "/32"},
 {"Netmask:", "255.255.255.255"},
 {"Binary Netmask:", "00000000.00000000.00000000.11111111"},
 {"192.168.1.2", "/24"},
 {"192.168.1.3", "/24"},
 {"192.168.1.4", "/24"},
 {"192.168.1.5", "/24"},
 {"192.168.1.6", "/24"},
 {"192.168.1.7", "/24"},
 {"192.168.1.8", "/24"},
 {"192.168.1.9", "/24"},
 {"192.168.1.10", "/24"},
 {"192.168.1.11", "/24"},
 {"192.168.1.12", "/24"},
 {"192.168.1.13", "/24"},
 {"192.168.1.14", "/24"},
 {"192.168.1.15", "/24"},
 {"192.168.1.16", "/24"},
 {"192.168.1.17", "/24"},
 {"192.168.1.18", "/24"},
 {"192.168.1.19", "/24"},
 {"192.168.1.20", "/24"},
 {"192.168.1.21", "/24"},
 {"192.168.1.22", "/24"},
 {"192.168.1.23", "/24"},
 {"192.168.1.24", "/24"},
 {"192.168.1.25", "/24"},
 {"192.168.1.255", "/24"}
 };
 String[][] resultOutput = binaryCalculate.prefixInPrefixCalculate(ip, ipPrefix, noPrefix, endPrefix);
 assertArrayEquals(expectedOutput, resultOutput);
 }

 @Test
 public void testCheckIPType() {
 String ip = "192.168.1.1";
 binaryCalculate.checkIPType(ip.length() - 1);
 assertTrue(binaryCalculate.isIPv4);
 assertTrue(binaryCalculate.lengthOfToken == 8);
 assertTrue(binaryCalculate.DELIMITER.equals("."));
 assertTrue(binaryCalculate.MAXPREFIX == 32);
 }
}