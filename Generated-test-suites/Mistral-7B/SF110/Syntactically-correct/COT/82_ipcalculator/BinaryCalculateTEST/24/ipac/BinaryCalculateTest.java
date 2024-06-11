import org.junit.Test;
import static org.junit.Assert.*;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expectedResult = "11111010";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSubBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expectedResult = "0101";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIPCalculateIPv4() {
 String IP = "192.168.1.1";
 String IPPrefix = "24";
 String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11000010.11111100.01101100.00001111\nBinary Subnet: 11111111.11111011.11101100.00001111\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.11111111.00000000";
 String outputText = binaryCalculate.IPCalculate(IP, IPPrefix);
 assertEquals(expectedOutputText, outputText);
 }

 @Test
 public void testIPCalculateIPv6() {
 String IP = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String IPPrefix = "60";
 String expectedOutputText = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\nUsable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7332 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\nTotal usable IP Addresses : 65534 \nSubnet: 01111111.11011011.10101101.00000000.00001110.11011010.00110111.11110110\nBinary Subnet: 11111111.11011011.10101101.00000000.00001110.11011010.00110111.11110110\nBroadcast Address: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\nPrefix: /60\nNetmask: FFFE:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\nBinary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111110";
 String outputText = binaryCalculate.IPCalculate(IP, IPPrefix);
 assertEquals(expectedOutputText, outputText);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculateInvalidIPPrefix() {
 String IP = "192.168.1.1";
 String IPPrefix = "50";
 binaryCalculate.IPCalculate(IP, IPPrefix);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculateInvalidIP() {
 String IP = "192.168.1..1";
 String IPPrefix = "24";
 binaryCalculate.IPCalculate(IP, IPPrefix);
 }

}