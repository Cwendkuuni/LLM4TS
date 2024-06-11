import org.junit.jupiter.api.*;
import ipac.BinaryCalculate;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate;

 @BeforeAll
 public void setUp() {
 binaryCalculate = new BinaryCalculate();
 }

 @Test
 public void testAddBinaryWithZeroLengthStrings() {
 String num1 = "";
 String num2 = "";
 String expectedResult = "";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testAddBinaryWithTwoZeroStrings() {
 String num1 = "0";
 String num2 = "0";
 String expectedResult = "0";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testAddBinaryWithTwoNonZeroStrings() {
 String num1 = "11";
 String num2 = "11";
 String expectedResult = "100";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testAddBinaryWithCarry() {
 String num1 = "1111";
 String num2 = "1111";
 String expectedResult = "10000";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSubBinaryWithCorrectSubtraction() {
 String num1 = "1111";
 String num2 = "1010";
 String expectedResult = "0101";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSubBinaryWithCarry() {
 String num1 = "1111";
 String num2 = "1111";
 String expectedResult = "0000";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIPCalculateWithIPv4AndCidr8() {
 String ip = "192.168.0.1";
 String prefix = "8";
 String expectedOutput = "Total Range: 192.168.0.1 -- 192.168.0.255\n" +
 "Usable Range: 192.168.0.2 -- 192.168.0.254\n" +
 "Total usable IP Addresses : 254\n" +
 "Subnet: 192.168.0.0\n" +
 "Binary Subnet: 11100110.11111111.00000000.00000111\n" +
 "Broadcast Address: 192.168.0.255\n" +
 "Prefix: /8\n" +
 "Netmask: 255.0.0.0\n" +
 "Binary Netmask: 00000000.11111111.00000000.00000000";
 String result = binaryCalculate.IPCalculate(ip, prefix);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testIPCalculateWithIPv4AndCidr16() {
 String ip = "192.168.0.1";
 String prefix = "16";
 String expectedOutput = "Total Range: 192.168.0.1 -- 192.168.1.255\n" +
 "Usable Range: 192.168.0.2 -- 192.168.0.254\n" +
 "Total usable IP Addresses : 254\n" +
 "Subnet: 192.168.0.0\n" +
 "Binary Subnet: 11100110.11111111.00000000.00000000\n" +
 "Broadcast Address: 192.168.1.255\n" +
 "Prefix: /16\n" +
 "Netmask: 255.255.0.0\n" +
 "Binary Netmask: 01111111.00000000.00000000.00000000";
 String result = binaryCalculate.IPCalculate(ip, prefix);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testIPCalculateWithIPv4AndCidr24() {
 String ip = "192.168.0.1";
 String prefix = "24";
 String expectedOutput = "Total Range: 192.168.0.1 -- 192.168.0.255\n" +
 "Usable Range: 192.168.0.2 -- 192.168.0.254\n" +
 "Total usable IP Addresses : 254\n" +
 "Subnet: 192.168.0.0\n" +
 "Binary Subnet: 11100110.11111111.00000000.00001111\n" +
 "Broadcast Address: 192.168.0.255\n" +
 "Prefix: /24\n" +
 "Netmask: 255.255.255.0\n" +
 "Binary Netmask: 11111111.11111111.11111111.00000000";
 String result = binaryCalculate.IPCalculate(ip, prefix);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testIPCalculateWithIPv4AndCidr32() {
 String ip = "192.168.0.1";
 String prefix = "32";
 String expectedOutput = "Total Range: 192.168.0.1\n" +
 "Usable Range: 192.168.0.1\n" +
 "Total usable IP Addresses : 1\n" +
 "Subnet: 0.0.0.0\n" +
 "Binary Subnet: 00000000.00000000.00000000.00000001\n" +
 "Broadcast Address: 192.168.0.1\n" +
 "Prefix: /32\n" +
 "Netmask: 255.255.255.255\n" +
 "Binary Netmask: 11111111.11111111.11111111.11111111";
 String result = binaryCalculate.IPCalculate(ip, prefix);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testIPCalculateWithIPv6AndCidr48() {
 String ip = "2001:0db8:001e:8500:0000:8a2e:0370:7334";
 String prefix = "48";
 String expectedOutput = "Total Range: 2001:0db8:001e:8500:0000:8a2e:0370:7334\n" +
 "Usable Range: 2001:0db8:001e:8500:0000:8a2e:0372:0000 -- 2001:0db8:001e:8500:0000:8a2e:0372:ffff\n" +
 "Total usable IP Addresses : 65534\n" +
 "Subnet: 2001:0db8:001e:8500:0000:8a2e:0000:0000\n" +
 "Binary Subnet: 2001:0db8:001e:8500:0000:8a2e:0000:0000\n" +
 "Broadcast Address: 2001:0db8:001e:8500:0000:8a2e:0372:ffff\n" +
 "Prefix: /48\n" +
 "Netmask: fe00:0:0:0:0:0:0:ffff\n" +
 "Binary Netmask: ff00:0:0:0:0:0:0:ffff";
 String result = binaryCalculate.IPCalculate(ip, prefix);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testIPCalculateWithIPv6AndCidr64() {
 String ip = "2001:0db8:001e:8500:0000:8a2e:0370:7334";
 String prefix = "64";
 String expectedOutput = "Total Range: 2001:0db8:001e:8500:0000:8a2e:0370:7334\n" +
 "Usable Range: 2001:0db8:001e:8500:0000:8a2e:0370:7334 -- 2001:0db8:001e:8500:0000:8a2e:0370:7334\n" +
 "Total usable IP Addresses : 1\n" +
 "Subnet: 2001:0db8:001e:8500:0000:8a2e:0370:7334\n" +
 "Binary Subnet: 2001:0db8:001e:8500:0000:8a2e:0370:7334\n" +
 "Broadcast Address: 2001:0db8:001e:8500:0000:8a2e:0370:7334\n" +
 "Prefix: /64\n" +
 "Netmask: ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff\n" +
 "Binary Netmask: ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff";
 String result = binaryCalculate.IPCalculate(ip, prefix);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testIPCalculateWithIPv6AndCidr128() {
 String ip = "2001:0db8:001e:8500:0000:8a2e:0370:7334";
 String prefix = "128";
 String expectedOutput = "Total Range: 2001:0db8:001e:8500:0000:8a2e:0370:7334\n" +
 "Usable Range: 2001:0db8:001e:8500:0000:8a2e:0370:7334 -- 2001:0db8:001e:8500:0000:8a2e:0370:7334\n" +
 "Total usable IP Addresses : 1\n" +
 "Subnet: 2001:0db8:001e:8500:0000:8a2e:0370:7334\n" +
 "Binary Subnet: 2001:0db8:001e:8500:0000:8a2e:0370:7334\n" +
 "Broadcast Address: 2001:0db8:001e:8500:0000:8a2e:0370:7334\n" +
 "Prefix: /128\n" +
 "Netmask: 0:0:0:0:0:0:0:0\n" +
 "Binary Netmask: 0:0:0:0:0:0:0:0";
 String result = binaryCalculate.IPCalculate(ip, prefix);
 assertEquals(expectedOutput, result);
 }
}