package ipac;

import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary_twoZeroBinaries() {
     String num1 = "01111111";
     String num2 = "01111111";
     String expectedSum = "11111111";
     String actualSum = binaryCalculate.addBinary(num1, num2);
     assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testAddBinary_carryPropagation() {
     String num1 = "1111";
     String num2 = "1111";
     String expectedSum = "10000";
     String actualSum = binaryCalculate.addBinary(num1, num2);
     assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testAddBinary_differentLengthBinaries() {
     String num1 = "1111";
     String num2 = "11";
     String expectedSum = "10010";
     String actualSum = binaryCalculate.addBinary(num1, num2);
     assertEquals(expectedSum, actualSum);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_firstNumberNull() {
     String num2 = "1111";
     String num1 = null;
     binaryCalculate.addBinary(num1, num2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_secondNumberNull() {
     String num1 = "1111";
     String num2 = null;
     binaryCalculate.addBinary(num1, num2);
 }

 @Test
 public void testSubBinary_twoZeroBinaries() {
     String num1 = "11111111";
     String num2 = "11111111";
     String expectedDiff = "00000000";
     String actualDiff = binaryCalculate.subBinary(num1, num2);
     assertEquals(expectedDiff, actualDiff);
 }

 @Test
 public void testSubBinary_secondNumberIsLonger() {
     String num1 = "1111";
     String num2 = "11111111";
     String expectedDiff = "10000000";
     String actualDiff = binaryCalculate.subBinary(num1, num2);
     assertEquals(expectedDiff, actualDiff);
 }

 @Test
 public void testSubBinary_carryPropagation() {
     String num1 = "1111";
     String num2 = "1110";
     String expectedDiff = "00010";
     String actualDiff = binaryCalculate.subBinary(num1, num2);
     assertEquals(expectedDiff, actualDiff);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubBinary_firstNumberNull() {
     String num2 = "1111";
     String num1 = null;
     binaryCalculate.subBinary(num1, num2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubBinary_secondNumberNull() {
     String num1 = "1111";
     String num2 = null;
     binaryCalculate.subBinary(num1, num2);
 }

 @Test
 public void testIPCalculate_IPv4() {
     String ip = "192.168.1.1";
     String ipPrefix = "255.255.255.0";
     String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
             "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
             "Total usable IP Addresses : 254\n" +
             "Subnet: 192.168.1.0\n" +
             "Binary Subnet: 11100110.01111111.01101100.00001111\n" +
             "Broadcast Address: 192.168.1.255\n" +
             "Prefix: /24\n" +
             "Netmask: 255.255.255.0\n" +
             "Binary Netmask: 0.11111111.11111111.11111111.00000000";
     String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
     assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testIPCalculate_IPv6() {
     String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
     String ipPrefix = "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:c000";
     String expectedOutput = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:0DB8:85A3:0000:0000:8A2E:0370:7334\n" +
             "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7333 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
             "Total usable IP Addresses : 1\n" +
             "Subnet: 2001:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
             "Binary Subnet: 00111110.11011111.10101011.00000000.00001110.11101110.00110101.11110110\n" +
             "Broadcast Address: FFFF:0DB8:85A3:0000:0000:8A2E:0370:7334\n" +
             "Prefix: /112\n" +
             "Netmask: ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:c000\n" +
             "Binary Netmask: 0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.11000000";
     String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
     assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetBinaryIP_IPv4() {
     String binaryIP = "11101110.11011111.01101100.00001111";
     String expectedOutput = "192.168.1.255";
     String actualOutput = binaryCalculate.getBinaryIP(binaryIP);
     assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetBinaryIP_IPv6() {
     String binaryIP = "00111110.11011111.10101011.00000000.00001110.11101110.00110101.11110110";
     String expectedOutput = "2001:0db8:85a3:0:0:8a2e:370:7334";
     String actualOutput = binaryCalculate.getBinaryIP(binaryIP);
     assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetNetmask_IPv4() {
     String binaryIP = "11101110.11011111.01101100.00001111";
     String ipPrefix = "255.255.255.0";
     String binaryNetmask = "0.11111111.11111111.11111111.00000000";
     String expectedOutput = "255.255.255.0";
     String actualOutput = binaryCalculate.getNetmask(binaryIP);
     assertEquals(expectedOutput, actualOutput);
     String expectedBinaryNetmask = binaryNetmask;
     String actualBinaryNetmask = binaryCalculate.getBinaryNetmask(ipPrefix);
     assertEquals(expectedBinaryNetmask, actualBinaryNetmask);
 }

 @Test
 public void testGetNetmask_IPv6() {
     String binaryIP = "00111110.11011111.10101011.00000000.00001110.11101110.00110101.11110110";
     String ipPrefix = "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:c000";
     String binaryNetmask = "0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.11000000";
     String expectedOutput = "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:c000";
     String actualOutput = binaryCalculate.getNetmask(binaryIP);
     assertEquals(expectedOutput, actualOutput);
     String expectedBinaryNetmask = binaryNetmask;
     String actualBinaryNetmask = binaryCalculate.getBinaryNetmask(ipPrefix);
     assertEquals(expectedBinaryNetmask, actualBinaryNetmask);
 }
}