import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expected = "11111010";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);
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
 public void testIPCalculate_IPv4_ValidIPAddress() {
 String ip = "192.168.1.1";
 String ipPrefix = "24";
 String expected = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.1 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11000000.11111100.01101100.00000111\nBinary Subnet: 11000000.11111100.01101100.00001111\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 00111111.11111111.11111111.00000000";
 binaryCalculate.IPCalculate(ip, ipPrefix);
 assertEquals(expected, binaryCalculate.getOutputText());
 }

 @Test
 public void testIPCalculate_IPv4_InvalidIPAddressLength() {
 String ip = "192.168.";
 String ipPrefix = "24";
 binaryCalculate.checkIPType(3); // Set the IP length to 3 to simulate an invalid IP address length
 String expectedError = "Error: Invalid IP address length.";
 String result = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertTrue(result.contains(expectedError));
 }

 @Test
 public void testIPCalculate_IPv4_IPPrefixInvalidLength() {
 String ip = "192.168.1.1";
 String ipPrefix = "33";
 binaryCalculate.checkIPType(32); // Set the IP prefix length to 32 for IPv4
 String expectedError = "Error: Invalid IP prefix length.";
 String result = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertTrue(result.contains(expectedError));
 }

 @Test
 public void testIPCalculate_IPv6_ValidIPAddress() {
 String ip = "2001:0db8:85a3:0000:0250:0032:0410:8a2e";
 String ipPrefix = "60";
 String expected = "Total Range: 2001:0db8:85a3:0000:0250:0032:0410:8a2e -- FFFFFFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\nUsable Range: 2001:0db8:85a3:0000:0250:0032:0410:8a2e -- FFFFFFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFE\nTotal usable IP Addresses : 65534\nSubnet: 01111101.11011011.10101101.00000000.00101000.01111100.11101000.11011110\nBinary Subnet: 01111101.11011011.10101101.00000000.00101000.01111100.11101000.11011110\nBroadcast Address: FFFFFFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFE\nPrefix: /60\nNetmask: FFFF:FFFF:FFFF:FFFF:0FFFF:FFFF:FFFF:FF00\nBinary Netmask: 11111111.11111111.11111111.11111111.00000000.00000000.00000000.11111100";
 binaryCalculate.checkIPType(127); // Set the IP type to IPv6
 binaryCalculate.IPCalculate(ip, ipPrefix);
 assertEquals(expected, binaryCalculate.getOutputText());
 }

 @Test
 public void testIPCalculate_IPv6_IPPrefixInvalidLength() {
 String ip = "2001:0db8:85a3:0000:0250:0032:0410:8a2e";
 String ipPrefix = "65";
 binaryCalculate.checkIPType(128); // Set the IP type to IPv6 and prefix length to 128 for validation
 String expectedError = "Error: Invalid IP prefix length.";
 String result = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertTrue(result.contains(expectedError));
 }

 private String getOutputText() {
 return binaryCalculate.getOutputText();
 }
}