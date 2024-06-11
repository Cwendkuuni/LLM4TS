package ipac;

import org.junit.Assert;import org.junit.Test;import java.math.BigInteger;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expected = "11111010";
 String result = binaryCalculate.addBinary(num1, num2);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testSubBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expected = "0101";
 String result = binaryCalculate.subBinary(num1, num2);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIPCalculate_IPv4() {
 String ip = "192.168.1.1";
 String ipPrefix = "24";
 String expected = "Total Range: 192.168.1.1 -- 192.168.1.255\nSubnet: 11000010.11101000.01100101.00000111\nBinary Subnet: 11111110.11110100.01111010.00001111\nBroadcast Address: 11111110.11110100.01111011.00001111\nPrefix: /24";
 binaryCalculate.checkIPType(ip.length() - 1);
 String result = binaryCalculate.IPCalculate(ip, ipPrefix);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIPCalculate_IPv6() {
 String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String ipPrefix = "64";
 String expected = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\nSubnet: 01111110.01111101.01010110.00000000.00001110.11101110.00101111.11110110\nBinary Subnet: 01111110.01111101.01010110.00000000.00001110.11101110.00101111.11110110\nBroadcast Address: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\nPrefix: /64";
 binaryCalculate.checkIPType(ip.length() - 1);
 String result = binaryCalculate.IPCalculate(ip, ipPrefix);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testCalculate() {
 String ip = "192.168.1.1";
 String ipPrefix = "24";
 String expectedTotalIPAddresses = "256";
 binaryCalculate.checkIPType(ip.length() - 1);
 binaryCalculate.calculate(binaryCalculate.convert(ip), ipPrefix);
 BigInteger actualTotalIPAddresses = binaryCalculate.totalIPAddresses;
 Assert.assertEquals(expectedTotalIPAddresses, actualTotalIPAddresses.intValue());
 }
}