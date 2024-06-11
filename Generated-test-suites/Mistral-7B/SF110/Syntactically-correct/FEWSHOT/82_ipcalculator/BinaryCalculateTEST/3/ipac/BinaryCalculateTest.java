package org.apache.commons.cli;

import static org.junit.Assert.*;

import java.math.BigInteger;
import org.junit.Test;

public class BinaryCalculateTest {

 BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expected = "11111010";
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
 String prefix = "24";
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11101100.11110100.01110100.00010101\nBinary Subnet: 11110110.11111010.01111100.00010101\nBroadcast Address: 11110110.11111010.01111100.00101111\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 01111111.11111111.11111111.00000000";
 String resultOutput = binaryCalculate.IPCalculate(ip, prefix);
 assertEquals(expectedOutput, resultOutput);
 }

 @Test
 public void testCalculate() {
 String ip = "192.168.1.1";
 String prefix = "24";
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254 ";
 binaryCalculate.calculate(ip, prefix);
 assertEquals(expectedOutput, binaryCalculate.fullReassembler.substring(0, binaryCalculate.fullReassembler.length() - 2));
 }

 @Test
 public void testPrefixInPrefixCalculate() {
 String ip = "192.168.1.1";
 String prefix = "24";
 int noPrefix = 5;
 String endPrefix = "32";
 String[][] expectedOutput = {{"192.168.1.1 /24", "192.168.1.1 -- 192.168.1.255"}, {"Total usable IP Addresses :", "255"}, {"Subnet:", "11101100.11110100.01110100.00010101"}, {"Binary Subnet:", "11110110.11111010.01111100.00010101"}, {"Broadcast Address:", "11110110.11111010.01111100.00101111"}, {"Prefix:", "/32"}, {"Netmask:", "255.255.255.0"}, {"Binary Netmask:", "01111111.11111111.11111111.00000000"}};
 String resultOutput = binaryCalculate.prefixInPrefixCalculate(ip, prefix, noPrefix, endPrefix).toString();
 String[] output = resultOutput.split("\n");
 String[] expected = expectedOutput[0];
 for (int i = 0; i < expected.length; i++) {
 String[] row = expected[i].split(":| ");
 String[] rowExpected = resultOutput.substring(output[0].indexOf(row[0])).split("\n")[0].split(":| ");
 assertEquals(row[0], rowExpected[0]);
 assertEquals(row[1], rowExpected[1]);
 }
 }
}