package ipac;

import org.junit.Assert;
import org.junit.Test;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary_TwoZeroStrings() {
 String num1 = "0";
 String num2 = "0";
 String expectedSum = "0";
 String actualSum = binaryCalculate.addBinary(num1, num2);
 Assert.assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testAddBinary_TwoOneStrings() {
 String num1 = "1";
 String num2 = "1";
 String expectedSum = "10";
 String actualSum = binaryCalculate.addBinary(num1, num2);
 Assert.assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testAddBinary_OneZeroOneString() {
 String num1 = "0";
 String num2 = "1";
 String expectedSum = "1";
 String actualSum = binaryCalculate.addBinary(num1, num2);
 Assert.assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testAddBinary_LengthMismatch() {
 String num1 = "11111111";
 String num2 = "1";
 Assert.assertThrows(IllegalArgumentException.class, () -> binaryCalculate.addBinary(num1, num2));
 }

 @Test
 public void testSubBinary_TwoZeroStrings() {
 String num1 = "0";
 String num2 = "0";
 String expectedDiff = "0";
 String actualDiff = binaryCalculate.subBinary(num1, num2);
 Assert.assertEquals(expectedDiff, actualDiff);
 }

 @Test
 public void testSubBinary_OneMinusOneString() {
 String num1 = "1";
 String num2 = "0";
 String expectedDiff = "1";
 String actualDiff = binaryCalculate.subBinary(num1, num2);
 Assert.assertEquals(expectedDiff, actualDiff);
 }

 @Test
 public void testSubBinary_OneMinusOneBitDifferenceString() {
 String num1 = "11111111";
 String num2 = "11111110";
 String expectedDiff = "00000010";
 String actualDiff = binaryCalculate.subBinary(num1, num2);
 Assert.assertEquals(expectedDiff, actualDiff);
 }

 @Test
 public void testIPCalculate_IPv4_ValidInput() {
 String ip = "192.168.1.1";
 String ipPrefix = "24";
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                        "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                        "Total usable IP Addresses : 254 \n" +
                        "Subnet: 11001010.11110100.01101001.00000101\n" +
                        "Binary Subnet: 11111100.11111111.01110110.00010101\n" +
                        "Broadcast Address: 192.168.1.255\n" +
                        "Prefix: /24\n" +
                        "Netmask: 255.255.255.0\n" +
                        "Binary Netmask: 11111111.11111111.11111111.00000000";
 String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testIPCalculate_IPv6_ValidInput() {
 String ip = "2001:0db8:0a9b:12f0:0000:ff00:0042:0f0b";
 String ipPrefix = "60";
 String expectedOutput = "Total Range: 2001:0db8:0a9b:12f0:0000:ff00:0042:0f0b -- FFFF:FFF:FFF:FFF:FFF:FFF:FFF:FFFF\n" +
                        "Usable Range: 2001:0db8:0a9b:12f0:0000:ff00:0042:0f0c -- FFFF:FFF:FFF:FFF:FFF:FFF:FFF:FFFF\n" +
                        "Total usable IP Addresses : 65534 \n" +
                        "Subnet: 01111101.01111111.01010101.10101111.00000000.11111111.00101000.11111101\n" +
                        "Binary Subnet: 00111111.11111111.11010101.10101111.00000000.11111111.00101000.11111011\n" +
                        "Broadcast Address: FFFF:FFF:FFF:FFF:FFF:FFF:FFF:FFFF\n" +
                        "Prefix: /60\n" +
                        "Netmask: FFFE::\n" +
                        "Binary Netmask: 11111111.11111111.11111111.11111111.00000000.00000000.00000000.11111110";
 String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIPv4Input() {
 String ip = "192.168.1.257";
 String ipPrefix = "24";
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIPv6Input() {
 String ip = "2001:0db8:0a9b:12f0:0000:ff00:0042:0f0a";
 String ipPrefix = "60";
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }
}