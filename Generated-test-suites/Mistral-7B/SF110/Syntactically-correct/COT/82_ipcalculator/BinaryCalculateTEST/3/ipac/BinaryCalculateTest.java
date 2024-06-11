package ipac;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary() {
 String num1 = "1011";
 String num2 = "1101";
 String expected = "11110";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinaryWithIllegalArgument() {
 String num1 = "";
 String num2 = "1011";
 String result = binaryCalculate.addBinary(num1, num2);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testSubBinary() {
 String num1 = "11111111";
 String num2 = "10101010";
 String expected = "01010101";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubBinaryWithIllegalArgument() {
 String num1 = "11111111";
 String num2 = "";
 String result = binaryCalculate.subBinary(num1, num2);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testIPCalculate() {
 String ip = "192.168.1.1";
 String ipPrefix = "24";
 String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254\nSubnet: 11000010.11111100.01101000.00001011\nBinary Subnet: 11000010.11111100.01101000.00001011\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 01111111.11111111.11111111.00000000";
 String resultOutputText = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertEquals(expectedOutputText, resultOutputText);
 }

 @Test
 public void testIPCalculateWithIPv6() {
 String ip = "2001:0db8:85a3:0000:0242:1001:0240:0811";
 String ipPrefix = "60";
 String expectedOutputText = "Total Range: 2001:0db8:85a3:0000:0242:1001:0240:0811 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\nUsable Range: 2001:0db8:85a3:0000:0242:1001:0240:0810 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FF\nTotal usable IP Addresses : 65536\nSubnet: 01111101.01101111.01010101.00000000.00101110.11001110.11001100.01011011\nBinary Subnet: 01111101.01101111.01010101.00000000.00101110.11001110.11001100.01011011\nBroadcast Address: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FF\nPrefix: /60\nNetmask: FFFFFFFF:FFFF:FFFF:0000:0000:0000:0000:0000\nBinary Netmask: 00000000.00000000.00000000.01111111.00000000.00000000.00000000.00000000";
 String resultOutputText = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertEquals(expectedOutputText, resultOutputText);
 }

}