import org.junit.Test;
import static org.junit.Assert.*;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expected = "11010";
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

 @Test(expected = RuntimeException.class)
 public void testAddBinaryInvalidInput() {
 String num1 = "";
 String num2 = "1111";
 binaryCalculate.addBinary(num1, num2);
 fail("Expected RuntimeException");
 }

 @Test
 public void testSubBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expected = "0101";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testSubBinaryBorrow() {
 String num1 = "0011";
 String num2 = "1010";
 String expected = "11011";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test(expected = RuntimeException.class)
 public void testSubBinaryInvalidInput() {
 String num1 = "1111";
 String num2 = "";
 binaryCalculate.subBinary(num1, num2);
 fail("Expected RuntimeException");
 }

 @Test
 public void testIPCalculateValidIPAddress() {
 String ipAddress = "192.168.1.1";
 String ipPrefix = "24";
 String expected = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
 "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
 "Total usable IP Addresses : 254 \n" +
 "Subnet: 11101100.11110100.01110100.00001011\n" +
 "Binary Subnet: 11101100.11110100.01110100.00001011\n" +
 "Broadcast Address: 192.168.1.255\n" +
 "Prefix: /24\n" +
 "Netmask: 255.255.255.0\n" +
 "Binary Netmask: 01111111.11111111.11111111.00000000";
 String result = binaryCalculate.IPCalculate(ipAddress, ipPrefix);
 assertEquals(expected, result);
 }

 @Test
 public void testIPCalculateInvalidIPAddressLong() {
 String ipAddress = "192.168.1.256";
 String ipPrefix = "24";
 String expected = "Invalid IP Address";
 String result = binaryCalculate.IPCalculate(ipAddress, ipPrefix);
 assertEquals(expected, result);
 }

 @Test
 public void testIPCalculateInvalidIPAddressShort() {
 String ipAddress = "192.168.";
 String ipPrefix = "24";
 String expected = "Invalid IP Address";
 String result = binaryCalculate.IPCalculate(ipAddress, ipPrefix);
 assertEquals(expected, result);
 }

 @Test
 public void testIPCalculateIPv6Address() {
 String ipAddress = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String ipPrefix = "48";
 String expected = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7340\n" +
 "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7335 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7340\n" +
 "Total usable IP Addresses : 65534 \n" +
 "Subnet: 01111110.11011111.10101101.00000000.00000000.10101101.01111101.11011000\n" +
 "Binary Subnet: 01111110.11011111.10101101.00000000.00000000.10101101.01111101.11011000\n" +
 "Broadcast Address: 2001:0db8:85a3:0000:0000:8a2e:0370:7340\n" +
 "Prefix: /48\n" +
 "Netmask: ffff:fffe:ffff:ffff:ffff:ffff:fffe:0000\n" +
 "Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111100";
 String result = binaryCalculate.IPCalculate(ipAddress, ipPrefix);
 assertEquals(expected, result);
 }

 @Test
 public void testIPCalculateIPv6AddressInvalidPrefixLength() {
 String ipAddress = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String ipPrefix = "32";
 String expected = "Invalid IP Prefix Length";
 String result = binaryCalculate.IPCalculate(ipAddress, ipPrefix);
 assertEquals(expected, result);
 }
}