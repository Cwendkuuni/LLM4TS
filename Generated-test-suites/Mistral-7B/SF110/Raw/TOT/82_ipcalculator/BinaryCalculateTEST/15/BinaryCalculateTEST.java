import org.junit.Test;
import static org.junit.Assert.*;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary() {
 String num1 = "1111";
 String num2 = "1110";
 String expected = "11111";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testAddBinaryWithCarry() {
 String num1 = "1111";
 String num2 = "1111";
 String expected = "10000";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testSubBinary() {
 String num1 = "1111";
 String num2 = "1110";
 String expected = "0011";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testSubBinaryWithCarry() {
 String num1 = "1111";
 String num2 = "1111";
 String expected = "00001";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testIPCalculate() {
 String ip = "192.168.1.1";
 String ipPrefix = "24";
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
 "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
 "Total usable IP Addresses : 254 \n" +
 "Subnet: 11101100.11110100.01010101.00000011\n" +
 "Binary Subnet: 11101100.11110100.01010101.00001101\n" +
 "Broadcast Address: 192.168.1.255\n" +
 "Prefix: /24\n" +
 "Netmask: 255.255.255.0\n" +
 "Binary Netmask: 01111111.11111111.11111111.00000000";
 String result = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testIPCalculateWithIPv6() {
 String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String ipPrefix = "64";
 String expectedOutput = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFFFFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
 "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7335 -- FFFFFFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
 "Total usable IP Addresses : 65535 \n" +
 "Subnet: 01111101.01101111.10101011.00000000.00001110.11101101.00110011.11110101\n" +
 "Binary Subnet: 01111101.01101111.10101011.00000000.00001110.11101101.00110011.11111010\n" +
 "Broadcast Address: FFFFFFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
 "Prefix: /64\n" +
 "Netmask: FFFFFFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
 "Binary Netmask: 00000000.00000000.00000000.00000000.00000000.00000000.00000000.11111111";
 String result = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testIPCalculateWithInvalidIPv4Prefix() {
 String ip = "192.168.1.1";
 String ipPrefix = "65";
 String expectedOutput = "Error: Invalid IPv4 prefix length. Expected a value between 1 and 32.\n";
 String result = "";
 try {
 result = binaryCalculate.IPCalculate(ip, ipPrefix);
 } catch (Exception e) {
 result = e.getMessage();
 }
 assertEquals(expectedOutput, result);
 }

 @Test
 public void testIPCalculateWithInvalidIPv6Prefix() {
 String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String ipPrefix = "129";
 String expectedOutput = "Error: Invalid IPv6 prefix length. Expected a value between 1 and 128.\n";
 String result = "";
 try {
 result = binaryCalculate.IPCalculate(ip, ipPrefix);
 } catch (Exception e) {
 result = e.getMessage();
 }
 assertEquals(expectedOutput, result);
 }
}