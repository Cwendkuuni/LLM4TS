package ipac;

import org.junit.Assert;
import org.junit.Test;

public class BinaryCalculateTest {

 private final BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary_ValidIPs() {
 String ip1 = "11110110.10111001.11010100.01101101";
 String ip2 = "11111111.11111111.11111111.11111101";
 String expected = "11111111.11111111.11111111.11111110";
 String result = binaryCalculate.addBinary(ip1, ip2);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testAddBinary_ZeroIP() {
 String ip1 = "00000000.00000000.00000000.00000000";
 String ip2 = "11111111.11111111.11111111.11111111";
 String expected = "11111111.11111111.11111111.11111111";
 String result = binaryCalculate.addBinary(ip1, ip2);
 Assert.assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidIPs() {
 String ip1 = "11110110.10111001.11010100.01101101.";
 String ip2 = "11111111.11111111.11111111.11111101";
 binaryCalculate.addBinary(ip1, ip2);
 }

 @Test
 public void testSubBinary_ValidIPs() {
 String ip1 = "11111111.11111111.11111111.11111111";
 String ip2 = "11110110.10111001.11010100.01101101";
 String expected = "00001001.01000100.00101001.10010010";
 String result = binaryCalculate.subBinary(ip1, ip2);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testSubBinary_ZeroIP() {
 String ip1 = "00000000.00000000.00000000.00000000";
 String ip2 = "11111111.11111111.11111111.11111111";
 String expected = "FFFFFFFF.FFFFFFFF.FFFFFFFF.FFFFFFFF";
 String result = binaryCalculate.subBinary(ip1, ip2);
 Assert.assertEquals(expected, result);
 }

 @Test
 public void testIPCalculate_ValidIPAndPrefix() {
 String ip = "192.168.1.1";
 String prefix = "24";
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\nSubnet: 11110110.10111000\nBinary Subnet: 11110110.10111000\nBroadcast Address: 192.168.1.255\nPrefix: /24";
 binaryCalculate.IPCalculate(ip, prefix);
 Assert.assertEquals(expectedOutput, binaryCalculate.fullReassembler);
 }

 @Test
 public void testIPCalculate_ZeroPrefix() {
 String ip = "192.168.1.1";
 String prefix = "0";
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.1\nSubnet: 11110110.10111000\nBinary Subnet: 11110110.10111000\nBroadcast Address: 192.168.1.1\nPrefix: /0";
 binaryCalculate.IPCalculate(ip, prefix);
 Assert.assertEquals(expectedOutput, binaryCalculate.fullReassembler);
 }

 @Test
 public void testIPCalculate_InvalidIP() {
 String ip = "192.168.1.1.";
 String prefix = "24";
 binaryCalculate.IPCalculate(ip, prefix);
 }

 @Test
 public void testIPCalculate_InvalidPrefix() {
 String ip = "192.168.1.1";
 String prefix = "33";
 binaryCalculate.IPCalculate(ip, prefix);
 }
}