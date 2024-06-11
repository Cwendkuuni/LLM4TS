package ipac;

import org.junit.Assert;import org.junit.Test;import java.math.BigInteger;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expected = "11111010";
 String actual = binaryCalculate.addBinary(num1, num2);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testSubBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expected = "0101";
 String actual = binaryCalculate.subBinary(num1, num2);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testIPCalculate() {
 String ip = "192.168.1.0";
 String ipPrefix = "24";
 String expectedOutputText = "Total Range: 192.168.1.0 -- 192.168.1.255\nUsable Range: 192.168.1.1 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11000010.11111100.01101100.00001111\nBinary Subnet: 11111001.11111111.01101100.00001111\nBroadcast Address: 11111111.11111111.01101100.00001111\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 00111111.11111111.01110000.00000000";
 String actualOutputText = binaryCalculate.IPCalculate(ip, ipPrefix);
 Assert.assertEquals(expectedOutputText, actualOutputText);
 }

 @Test
 public void testGetBinaryIP() {
 String binaryIP = "11111001.11111111.01101100.00001111";
 String expected = "11111001111111110110110000001111";
 String actual = binaryCalculate.getBinaryIP(binaryIP);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testGetNetmask() {
 String binaryIP = "11111001.11111111.01101100.00001111";
 String ipPrefix = "24";
 String expectedOutputNetmask = "00111111.11111111.01110000.00000000";
 String actualOutputNetmask = binaryCalculate.getNetmask(ipPrefix + "." + binaryIP.substring(binaryIP.lastIndexOf(".")));
 Assert.assertEquals(expectedOutputNetmask, actualOutputNetmask);
 }

 @Test
 public void testCalculate() {
 String ip = "192.168.1.0";
 String ipPrefix = "24";
 String expectedIPRangeAddress = "192.168.1.0 -- 192.168.1.255";
 String expectedUsableRange = "192.168.1.1 -- 192.168.1.254";
 BigInteger expectedTotalIPAddresses = new BigInteger("256", 10).subtract(new BigInteger("2"));
 String expectedPrefix = "/24";
 binaryCalculate.calculate(ip, ipPrefix);
 Assert.assertEquals(expectedIPRangeAddress, binaryCalculate.IPRangeAddress);
 Assert.assertEquals(expectedUsableRange, binaryCalculate.usableRange);
 Assert.assertEquals(expectedTotalIPAddresses, binaryCalculate.totalIPAddresses);
 Assert.assertEquals(expectedPrefix, binaryCalculate.prefix);
 }

}