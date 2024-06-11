package ipac;

import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

 @Test
 public void testAddBinary() {
 String num1 = "1111";
 String num2 = "1010";
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String expectedSum = "11110101";
 String actualSum = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testSubBinary() {
 String num1 = "1111";
 String num2 = "1010";
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String expectedDiff = "0101";
 String actualDiff = binaryCalculate.subBinary(num1, num2);
 assertEquals(expectedDiff, actualDiff);
 }

 @Test
 public void testIPCalculate_IPv4() {
 String ip = "192.168.1.1";
 String ipPrefix = "255.255.255.0";
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254\nSubnet: 11101100.11111111.01110110.00001111\nBinary Subnet: 11101100.11111111.01110110.00001111\nBroadcast Address: 11101100.11111111.01110110.11111111\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.11111111.00000000\n";
 String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testIPCalculate_IPv6() {
 String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String ipPrefix = "ffff:ffff:ffff:ffff:ffff:fff8:0000:0000";
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String expectedOutput = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334\nUsable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7332 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7334\nTotal usable IP Addresses : 4\nSubnet: 01111111.11011111.10101110.00000000.00000000.11101110.00111111.11111100\nBinary Subnet: 01111111.11011111.10101110.00000000.00000000.11101110.00111111.11111100\nBroadcast Address: 01111111.11011111.10101110.00000111.11111111.11101110.11111111.11111011\nPrefix: /63\nNetmask: ffff:ffff:ffff:ffff:ffff:fff8:0000:0000\nBinary Netmask: 11111111.11111111.11111111.11111111.00000000.11111110.00000000.00000000\n";
 String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidInput1() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String num1 = "1111";
 String num2 = "11";
 binaryCalculate.addBinary(num1, num2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidInput2() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String num1 = "1111";
 String num2 = "111111";
 binaryCalculate.addBinary(num1, num2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIP1() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip = "192.168.1.";
 String ipPrefix = "255.255.255.0";
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIP2() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip = "255.255.255.255";
 String ipPrefix = "255.255.255.256";
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIP3() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334:1234";
 String ipPrefix = "ffff:ffff:ffff:ffff:ffff:fff8:0000:0000";
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIP4() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip = "2001:0db8:85a3::1";
 String ipPrefix = "ffff:ffff:ffff:ffff:ffff:fff8:0000:0000";
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }
}