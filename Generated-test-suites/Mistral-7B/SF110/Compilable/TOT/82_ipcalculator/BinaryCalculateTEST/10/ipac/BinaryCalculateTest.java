package ipac;
import org.junit.Assert;
import org.junit.Test;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

 private final BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinaryValidIPs() {
 String ip1 = "10101010";
 String ip2 = "11110110";
 String expectedResult = "11111100";
 String result = binaryCalculate.addBinary(ip1, ip2);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testAddBinaryEdgeCase1() {
 String ip1 = "00000000";
 String ip2 = "11111111";
 String expectedResult = "11111111";
 String result = binaryCalculate.addBinary(ip1, ip2);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testAddBinaryEdgeCase2() {
 String ip1 = "11111111";
 String ip2 = "00000000";
 String expectedResult = "11111111";
 String result = binaryCalculate.addBinary(ip1, ip2);
 Assert.assertEquals(expectedResult, result);
 }

 @Test (expected = RuntimeException.class)
 public void testAddBinaryInvalidIPs() {
 String ip1 = "10101011";
 String ip2 = "11111110";
 binaryCalculate.addBinary(ip1, ip2);
 }

 @Test
 public void testSubBinaryValidIPs() {
 String ip1 = "11111100";
 String ip2 = "11111111";
 String expectedResult = "00000011";
 String result = binaryCalculate.subBinary(ip1, ip2);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testSubBinaryEdgeCase1() {
 String ip1 = "00000000";
 String ip2 = "11111111";
 String expectedResult = "11111111";
 String result = binaryCalculate.subBinary(ip1, ip2);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testSubBinaryEdgeCase2() {
 String ip1 = "11111111";
 String ip2 = "00000000";
 String expectedResult = "11111111";
 String result = binaryCalculate.subBinary(ip1, ip2);
 Assert.assertEquals(expectedResult, result);
 }

 @Test
 public void testIPCalculateValidIP() {
 String ip = "192.168.1.1";
 String prefix = "24";
 String expectedResult = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
 "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
 "Total usable IP Addresses : 254\n" +
 "Subnet: 192.168.1.0\n" +
 "Binary Subnet: 11101100.01111111.01101100.00001111\n" +
 "Broadcast Address: 192.168.1.255\n" +
 "Prefix: /24\n" +
 "Netmask: 255.255.255.0\n" +
 "Binary Netmask: 00111111.11111111.11111111.00000000";
 String result = binaryCalculate.IPCalculate(ip, prefix);
 Assert.assertEquals(expectedResult, result);
 }

 @Test (expected = RuntimeException.class)
 public void testIPCalculateInvalidIP() {
 String ip = "192.168.1.265";
 String prefix = "24";
 binaryCalculate.IPCalculate(ip, prefix);
 }

 @Test
 public void testIPCalculateInvalidPrefix() {
 String ip = "192.168.1.1";
 String prefix = "32";
 String expectedResult = "Total Range: 192.168.1.1 -- 192.168.1.4294967295\n" +
 "Usable Range: 192.168.1.2 -- 192.168.1.4294967293\n" +
 "Total usable IP Addresses : 4294967293\n" +
 "Subnet: 192.168.1.0\n" +
 "Binary Subnet: 11101100.01111111.01101100.00000000\n" +
 "Broadcast Address: 192.168.1.4294967295\n" +
 "Prefix: /32\n" +
 "Netmask: 255.255.255.255\n" +
 "Binary Netmask: 11111111.11111111.11111111.11111111";
 String result = binaryCalculate.IPCalculate(ip, prefix);
 Assert.assertEquals(expectedResult, result);
 }

}