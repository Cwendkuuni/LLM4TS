package ipac;

import static org.junit.Assert.*;

import org.junit.Test;

import java.math.BigInteger;
import java.text.NumberFormat;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expectedSum = "11001";
 String actualSum = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testSubBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expectedDiff = "0101";
 String actualDiff = binaryCalculate.subBinary(num1, num2);
 assertEquals(expectedDiff, actualDiff);
 }

 @Test
 public void testIPCalculate() {
 String ip = "192.168.1.1";
 String ipPrefix = "255.255.255.0";
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11001100.11111111.00111111.00000111\nBinary Subnet: 11111111.11111111.01111100.00111101\nBroadcast Address: 11111111.11111111.01111111.00111111\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.11111111.00000000";
 String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertEquals(expectedOutput, actualOutput);
 }
}