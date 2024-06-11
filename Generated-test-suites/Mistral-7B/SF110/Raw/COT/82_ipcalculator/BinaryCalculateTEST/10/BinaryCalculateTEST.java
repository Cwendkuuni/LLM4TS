package ipac;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary_IPv4() {
 String num1 = "10101010";
 String num2 = "11110110";
 String expected = "11111100";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testAddBinary_IPv6() {
 String num1 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String num2 = "2001:0db8:85a3:0000:0000:8a2e:0360:7334";
 String expected = "2001:0db8:85a3:0000:0000:8a2e:0370:7335";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidInput_IPv4() {
 String num1 = "10101010.";
 String num2 = "11110110";
 binaryCalculate.addBinary(num1, num2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidInput_IPv6() {
 String num1 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334:";
 String num2 = "2001:0db8:85a3:0000:0000:8a2e:0360:7334";
 binaryCalculate.addBinary(num1, num2);
 }

 @Test
 public void testSubBinary_IPv4() {
 String num1 = "11111100";
 String num2 = "11110110";
 String expected = "00011010";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testSubBinary_IPv6() {
 String num1 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String num2 = "2001:0db8:85a3:0000:0000:8a2e:0360:7334";
 String expected = "00011010:0000:0000:0000:0000:8a2e:0010:7334";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testIPCalculate_IPv4() {
 String IP = "192.168.1.1";
 String IPPrefix = "24";
 String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                            "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                            "Total usable IP Addresses : 254 \n" +
                            "Subnet: 11110110.11111100.01101110.00000001\n" +
                            "Binary Subnet: 11111110.11111000.01101110.00000011\n" +
                            "Broadcast Address: 11111110.11111100.01101110.00001111\n" +
                            "Prefix: /24\n" +
                            "Netmask: 255.255.255.0\n" +
                            "Binary Netmask: 11111111.11111111.11111111.00000000";
 String resultOutputText = binaryCalculate.IPCalculate(IP, IPPrefix);
 assertEquals(expectedOutputText, resultOutputText);
 }

 @Test
 public void testIPCalculate_IPv6() {
 String IP = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String IPPrefix = "60";
 String expectedOutputText = "Total Range: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                            "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7335\n" +
                            "Total usable IP Addresses : 65534\n" +
                            "Subnet: 01111111.11110110.11010100.00000000.00000000.8a2e.0360.7334\n" +
                            "Binary Subnet: 01111111.11110110.11010100.00000000.00000000.11101110.01101100.01111110\n" +
                            "Broadcast Address: 01111111.11110110.11010100.00001111.ffffffff.ffffffff.ffffffff.ffffffff\n" +
                            "Prefix: /60\n" +
                            "Netmask: FFFE:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                            "Binary Netmask: 01111111.11111111.11111111.11111111.11111111.11111111.11111111.11110000";
 String resultOutputText = binaryCalculate.IPCalculate(IP, IPPrefix);
 assertEquals(expectedOutputText, resultOutputText);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidInput() {
 String IP = "192.168.";
 String IPPrefix = "24";
 binaryCalculate.IPCalculate(IP, IPPrefix);
 }

 @Test
 public void testGetBinaryIP_IPv4() {
 String binaryIP = "11111110.11111100.01101110.00000011";
 String expected = "11111110.11111100.01101110.00001111";
 String result = binaryCalculate.getBinaryIP(binaryIP);
 assertEquals(expected, result);
 }

 @Test
 public void testGetBinaryIP_IPv6() {
 String binaryIP = "01111111.11110110.11010100.00000000.00000000.11101110.01101100.01111110";
 String expected = "01111111.11110110.11010100.00000000.00000000.11101110.01101101.01111111";
 String result = binaryCalculate.getBinaryIP(binaryIP);
 assertEquals(expected, result);
 }

 @Test
 public void testGetNetmask_IPv4() {
 String binaryIP = "11111110.11111100.01101110.00001111";
 String expected = "00001111.11110000.11111100.00000000";
 String result = binaryCalculate.getNetmask(binaryIP);
 assertEquals(expected, result);
 }

 @Test
 public void testGetNetmask_IPv6() {
 String binaryIP = "01111111.11110110.11010100.00000000.00000000.11101110.01101101.01111111";
 String expected = "11111101.11101011.11010100.00000000.00000000.11101110.11010101.11111111";
 String result = binaryCalculate.getNetmask(binaryIP);
 assertEquals(expected, result);
 }
}