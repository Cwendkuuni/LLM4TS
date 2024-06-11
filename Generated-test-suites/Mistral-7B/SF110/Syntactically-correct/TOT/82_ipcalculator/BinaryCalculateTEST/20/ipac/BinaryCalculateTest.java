import org.junit.Assert;
import org.junit.Test;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary_TwoZeroBinaryNumbers() {
 String num1 = "01111111";
 String num2 = "01111111";
 String expectedResult = "11111111";
 String actualResult = binaryCalculate.addBinary(num1, num2);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testAddBinary_TwoOneBinaryNumbers() {
 String num1 = "0011";
 String num2 = "0110";
 String expectedResult = "1001";
 String actualResult = binaryCalculate.addBinary(num1, num2);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testAddBinary_CarryOver() {
 String num1 = "1111";
 String num2 = "1111";
 String expectedResult = "10000";
 String actualResult = binaryCalculate.addBinary(num1, num2);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidInput() {
 String num1 = "abc";
 String num2 = "def";
 binaryCalculate.addBinary(num1, num2);
 }

 @Test
 public void testSubBinary_TwoZeroBinaryNumbers() {
 String num1 = "11111111";
 String num2 = "00000000";
 String expectedResult = "11111111";
 String actualResult = binaryCalculate.subBinary(num1, num2);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSubBinary_TwoOneBinaryNumbers() {
 String num1 = "1011";
 String num2 = "1100";
 String expectedResult = "0111";
 String actualResult = binaryCalculate.subBinary(num1, num2);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test
 public void testSubBinary_TwoNumbersWithCarryOver() {
 String num1 = "1111";
 String num2 = "1011";
 String expectedResult = "0100";
 String actualResult = binaryCalculate.subBinary(num1, num2);
 Assert.assertEquals(expectedResult, actualResult);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubBinary_InvalidInput() {
 String num1 = "abc";
 String num2 = "def";
 binaryCalculate.subBinary(num1, num2);
 }

 @Test
 public void testIPCalculate_IPv4_ValidIP() {
 String ip = "192.168.1.1";
 String prefix = "24";
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
 "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
 "Total usable IP Addresses : 254\n" +
 "Subnet: 11010110.11110100.01101000.00001111\n" +
 "Binary Subnet: 11111010.11111000.01101000.00001111\n" +
 "Broadcast Address: 11111111.11111111.01101000.00001111\n" +
 "Prefix: /24\n" +
 "Netmask: 255.255.255.0\n" +
 "Binary Netmask: 00111111.11111111.01110000.00000000";
 String actualOutput = binaryCalculate.IPCalculate(ip, prefix);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testIPCalculate_IPv4_InvalidIP() {
 String ip = "abc.def.ghi.ijk";
 String prefix = "24";
 binaryCalculate.IPCalculate(ip, prefix);
 }

 @Test
 public void testIPCalculate_IPv6_ValidIP() {
 String ip = "2001:0db8:85a3:0000:0270:f1bf:0101:1011";
 String prefix = "64";
 String expectedOutput = "Total Range: 2001:0db8:85a3:0000:0270:f1bf:0101:1011\n" +
 "Usable Range: 2001:0db8:85a3:0000:0270:f1bf:0101:1010 -- 2001:0db8:85a3:0000:0270:f1bf:0101:1011\n" +
 "Total usable IP Addresses : 65\n" +
 "Subnet: 01111101.01111011.01010101.00000000.00111101.11111110.01010101.01010100\n" +
 "Binary Subnet: 01111101.01111011.01010101.00000000.00111101.11111110.01010101.01010100\n" +
 "Broadcast Address: 2001:0db8:85a3:0000:0270:f1bf:feff:ffff\n" +
 "Prefix: /64\n" +
 "Netmask: ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff\n" +
 "Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111";
 String actualOutput = binaryCalculate.IPCalculate(ip, prefix);
 Assert.assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testIPCalculate_IPv6_InvalidIP() {
 String ip = "abc:def:ghi:ijk:lmn:opq:pqr:stu";
 String prefix = "64";
 binaryCalculate.IPCalculate(ip, prefix);
 }
}