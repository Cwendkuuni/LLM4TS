package ipac;

import org.junit.Assert;
import org.junit.Test;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expectedSum = "10110";
 String actualSum = binaryCalculate.addBinary(num1, num2);
 Assert.assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testAddBinary_Carry() {
 String num1 = "1111";
 String num2 = "1111";
 String expectedSum = "100000";
 String actualSum = binaryCalculate.addBinary(num1, num2);
 Assert.assertEquals(expectedSum, actualSum);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidInput() {
 String num1 = "1111";
 String num2 = "abcd";
 binaryCalculate.addBinary(num1, num2);
 }

 @Test
 public void testSubBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expectedDiff = "0101";
 String actualDiff = binaryCalculate.subBinary(num1, num2);
 Assert.assertEquals(expectedDiff, actualDiff);
 }

 @Test
 public void testSubBinary_TwosComplement() {
 String num1 = "0011";
 String num2 = "1100";
 String expectedDiff = "1111";
 String actualDiff = binaryCalculate.subBinary(num1, num2);
 Assert.assertEquals(expectedDiff, actualDiff);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubBinary_InvalidInput() {
 String num1 = "1111";
 String num2 = "abcd";
 binaryCalculate.subBinary(num1, num2);
 }

 @Test
 public void testIPCalculate() {
 String ip = "192.168.1.1";
 String ipPrefix = "24";
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
         "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
         "Total usable IP Addresses : 254\n" +
         "Subnet: 11110110.11111000.00111101.00000111\n" +
         "Binary Subnet: 11110110.11111000.00111101.00000111\n" +
         "Broadcast Address: 192.168.1.255\n" +
         "Prefix: /24\n" +
         "Netmask: 255.255.255.0\n" +
         "Binary Netmask: 01111111.11111111.11111111.00000000";
 String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testIPCalculate_IPv6() {
 String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String ipPrefix = "60";
 String expectedOutput = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 --\n" +
         "FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
         "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7335 --\n" +
         "FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
         "Total usable IP Addresses : 65535\n" +
         "Subnet: 01111110.01111111.10101011.00000000.00000000.8a2e.0370.7335\n" +
         "Binary Subnet: 01111110.01111111.10101011.00000000.00000000.10101110.01110111.11110111\n" +
         "Broadcast Address: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
         "Prefix: /60\n" +
         "Netmask: 255.255.255.252\n" +
         "Binary Netmask: 11111101.11111111.11111111.11111100";
 String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIP() {
 String ip = "abcd.efgh.ijk.lmn";
 String ipPrefix = "24";
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidPrefix() {
 String ip = "192.168.1.1";
 String ipPrefix = "33";
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

}