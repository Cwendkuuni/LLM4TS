import org.junit.Test;
import static org.junit.Assert.assertEquals;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expectedSum = "11001"; // '0' for carry
 String actualSum = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testAddBinaryCarry() {
 String num1 = "1111";
 String num2 = "1111";
 String expectedSum = "10000"; // '1' for carry
 String actualSum = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testSubBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expectedDiff = "01011"; // '1' for borrow
 String actualDiff = binaryCalculate.subBinary(num1, num2);
 assertEquals(expectedDiff, actualDiff);
 }

 @Test
 public void testSubBinaryBorrow() {
 String num1 = "0000";
 String num2 = "1111";
 String expectedDiff = "11111"; // '1' for borrow
 String actualDiff = binaryCalculate.subBinary(num1, num2);
 assertEquals(expectedDiff, actualDiff);
 }

 @Test
 public void testIPCalculateValidIPV4() {
 String ip = "192.168.1.1";
 String ipPrefix = "24";
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11101100.11110111.01110100.00001111\nBinary Subnet: 11101100.11110111.01110100.00001111\nBroadcast Address: 11101100.11110111.01110100.00011111\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.11111111.00000000";
 String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testIPCalculateInvalidIPV4Length() {
 String ip = "192.168.";
 String ipPrefix = "24";
 // Expected exception: java.lang.IllegalArgumentException: Invalid IP address length
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test
 public void testIPCalculateInvalidIPV4Format() {
 String ip = "192.168.1.256";
 String ipPrefix = "24";
 // Expected exception: java.lang.IllegalArgumentException: Invalid IP address format
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test
 public void testIPCalculateValidIPV6() {
 String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String ipPrefix = "48";
 String expectedOutput = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7337\nUsable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7335 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7336\nTotal usable IP Addresses : 65536\nSubnet: 01111010.11011111.10101101.00000000.0000.11011010.01110111.11011010\nBinary Subnet: 01111010.11011111.10101101.00000000.0000.11011010.01110111.11011010\nBroadcast Address: 01111010.11011111.10101101.00000000.0000.11011010.01111111.11011111\nPrefix: /48\nNetmask: ffff:ffff:ffff:ffff:ffff:ffff:ff00:0000\nBinary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.00000000.00000000";
 String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testIPCalculateInvalidIPV6Length() {
 String ip = "2001:0db8:85a3:";
 String ipPrefix = "48";
 // Expected exception: java.lang.IllegalArgumentException: Invalid IP address length
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test
 public void testIPCalculateInvalidIPV6Format() {
 String ip = "2001:0db8:85a3::1";
 String ipPrefix = "48";
 // Expected exception: java.lang.IllegalArgumentException: Invalid IP address format
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

}