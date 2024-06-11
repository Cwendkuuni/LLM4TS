import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary_TwoZeroBinaryNumbers() {
 String num1 = "01111111";
 String num2 = "01111111";
 String expectedResult = "11111111";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testAddBinary_TwoOneBinaryNumbers() {
 String num1 = "0011";
 String num2 = "0110";
 String expectedResult = "1001";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testAddBinary_TwoDifferentLengthBinaryNumbers() {
 String num1 = "0111";
 String num2 = "1010";
 String expectedResult = "11011";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedResult, result);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAddBinary_NullNum1() {
 String num2 = "1010";
 String result = binaryCalculate.addBinary(null, num2);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAddBinary_NullNum2() {
 String num1 = "0111";
 String result = binaryCalculate.addBinary(num1, null);
 }

 @Test
 public void testSubBinary_TwoZeroBinaryNumbers() {
 String num1 = "1111";
 String num2 = "1111";
 String expectedResult = "0000";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSubBinary_OneBiggerNumberSubtractedFromSmallerNumber() {
 String num1 = "1111";
 String num2 = "0111";
 String expectedResult = "1000";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testSubBinary_TwoDifferentLengthBinaryNumbers() {
 String num1 = "1111";
 String num2 = "01111";
 String expectedResult = "10000";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expectedResult, result);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testSubBinary_NullNum1() {
 String num2 = "1010";
 String result = binaryCalculate.subBinary(null, num2);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testSubBinary_NullNum2() {
 String num1 = "1111";
 String result = binaryCalculate.subBinary(num1, null);
 }

 @Test
 public void testIPCalculate_ValidIPAddressAndPrefixLength() {
 String ipAddress = "192.168.1.0";
 String prefixLength = "24";
 String expectedOutputText = "Total Range: 192.168.1.0 -- 192.168.1.255\n" +
 "Usable Range: 192.168.1.1 -- 192.168.1.254\n" +
 "Total usable IP Addresses : 254\n" +
 "Subnet: 11101100.11110000.01101100.00001111\n" +
 "Binary Subnet: 11101100.11110000.01101100.00001111\n" +
 "Broadcast Address: 192.168.1.255\n" +
 "Prefix: /24\n" +
 "Netmask: 255.255.255.0\n" +
 "Binary Netmask: 11111111.11111111.11111111.00000000";
 String result = binaryCalculate.IPCalculate(ipAddress, prefixLength);
 assertEquals(expectedOutputText, result);
 }

 @Test
 public void testIPCalculate_IPv6AddressAndPrefixLength() {
 String ipAddress = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String prefixLength = "48";
 String expectedOutputText = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
 "Usable Range:\n" +
 "Total usable IP Addresses : 65536\n" +
 "Subnet: 01111010.11011011.10101101.00000000.00001010.11101110.00110111.11010110\n" +
 "Binary Subnet: 01111010.11011011.10101101.00000000.00001010.11101110.00110111.11010110\n" +
 "Broadcast Address: 2001:0db8:85a3:0000:0000:8a2e:0370:7335\n" +
 "Prefix: /48\n" +
 "Netmask: ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff\n" +
 "Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111";
 String result = binaryCalculate.IPCalculate(ipAddress, prefixLength);
 assertEquals(expectedOutputText, result);
 }

 @Test
 public void testIPCalculate_InvalidIPAddressAndPrefixLength() {
 String ipAddress = "abcdefg";
 String prefixLength = "32";
 String expectedErrorMessage = "Invalid IP address: abcdefg";
 try {
 binaryCalculate.IPCalculate(ipAddress, prefixLength);
 fail("Expected IllegalArgumentException with message: " + expectedErrorMessage);
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 @Test
 public void testIPCalculate_IPAddressWithLongerPrefixLengthThanIPv4AddressSpace() {
 String ipAddress = "192.168.1.0";
 String prefixLength = "33";
 String expectedErrorMessage = "Invalid prefix length: 33";
 try {
 binaryCalculate.IPCalculate(ipAddress, prefixLength);
 fail("Expected IllegalArgumentException with message: " + expectedErrorMessage);
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 @Test
 public void testIPCalculate_IPAddressWithLongerPrefixLengthThanIPv6AddressSpace() {
 String ipAddress = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String prefixLength = "129";
 String expectedErrorMessage = "Invalid prefix length: 129";
 try {
 binaryCalculate.IPCalculate(ipAddress, prefixLength);
 fail("Expected IllegalArgumentException with message: " + expectedErrorMessage);
 } catch (IllegalArgumentException e) {
 assertEquals(expectedErrorMessage, e.getMessage());
 }
 }

 @Test
 public void testPrefixInPrefixCalculate_ValidInputParameters() {
 String ipAddress = "192.168.1.0";
 String ipPrefix = "24";
 int noPrefix = 3;
 String endPrefix = "30";
 String[][] output = binaryCalculate.prefixInPrefixCalculate(ipAddress, ipPrefix, noPrefix, endPrefix);
 String expectedOutput = "Total usable IP Addresses : 222\n" +
 "Subnet: 11101100.11110000.01101100.00001111\n" +
 "Binary Subnet: 11101100.11110000.01101100.00001111\n" +
 "Broadcast Address: 192.168.1.30\n" +
 "Prefix: /30\n" +
 "Netmask: 255.255.255.252\n" +
 "Binary Netmask: 11111111.11111111.11111111.11111101";
 for (int i = 0; i < output.length; i++) {
 String expectedOutputLine = "";
 switch (i) {
 case 0:
 expectedOutputLine = "IP Subnet: ";
 break;
 case 1:
 expectedOutputLine = "Start IP Address: ";
 break;
 case 2:
 expectedOutputLine = "End IP Address: ";
 break;
 case 3:
 expectedOutputLine = "Usable IP Addresses: ";
 break;
 default:
 expectedOutputLine = "";
 }
 String outputLine = output[i][0];
 assertEquals(expectedOutputLine, outputLine.substring(0, outputLine.indexOf(": ")));
 String outputValue = output[i][1];
 if (i == 3) {
 String expectedOutputValue = "222";
 String outputValueStartIndex = outputValue.indexOf(": ");
 String outputValueSubstring = outputValue.substring(outputValueStartIndex + 2);
 assertEquals(expectedOutputValue, outputValueSubstring);
 } else {
 String expectedOutputValue = expectedOutput.split("\n")[i + 1].split(": ")[1];
 assertEquals(expectedOutputValue, outputValue);
 }
 }
 }

 @Test
 public void testPrefixInPrefixCalculate_InvalidIPAddress() {
 String ipAddress = "abcdefg";
 String ipPrefix = "24";
 int noPrefix = 3;
 String endPrefix = "30";
 try {
 binaryCalculate.prefixInPrefixCalculate(ipAddress, ipPrefix, noPrefix, endPrefix);
 fail("Expected IllegalArgumentException with message: " + "Invalid IP address: abcdefg");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid IP address: abcdefg", e.getMessage());
 }
 }

 @Test
 public void testPrefixInPrefixCalculate_InvalidPrefixLength() {
 String ipAddress = "192.168.1.0";
 String ipPrefix = "33";
 int noPrefix = 3;
 String endPrefix = "30";
 try {
 binaryCalculate.prefixInPrefixCalculate(ipAddress, ipPrefix, noPrefix, endPrefix);
 fail("Expected IllegalArgumentException with message: " + "Invalid prefix length: 33");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid prefix length: 33", e.getMessage());
 }
 }

 @Test
 public void testPrefixInPrefixCalculate_InvalidNumberOfPrefixes() {
 String ipAddress = "192.168.1.0";
 String ipPrefix = "24";
 int noPrefix = 4;
 String endPrefix = "30";
 try {
 binaryCalculate.prefixInPrefixCalculate(ipAddress, ipPrefix, noPrefix, endPrefix);
 fail("Expected IllegalArgumentException with message: " + "Number of prefixes: 4");
 } catch (IllegalArgumentException e) {
 assertEquals("Number of prefixes: 4", e.getMessage());
 }
 }

 @Test
 public void testPrefixInPrefixCalculate_InvalidEndPrefixLength() {
 String ipAddress = "192.168.1.0";
 String ipPrefix = "24";
 int noPrefix = 3;
 String endPrefix = "25";
 try {
 binaryCalculate.prefixInPrefixCalculate(ipAddress, ipPrefix, noPrefix, endPrefix);
 fail("Expected IllegalArgumentException with message: " + "Invalid prefix length: 25");
 } catch (IllegalArgumentException e) {
 assertEquals("Invalid prefix length: 25", e.getMessage());
 }
 }
}