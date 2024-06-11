import org.junit.Test;
import static org.junit.Assert.*;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary() {
 // Test adding two binary numbers of same length
 String num1 = "1111";
 String num2 = "1111";
 String expectedSum = "11111";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedSum, result);

 // Test adding two binary numbers of different lengths
 String num3 = "1111";
 String num4 = "1";
 String expectedSum2 = "11111";
 String result2 = binaryCalculate.addBinary(num3, num4);
 assertEquals(expectedSum2, result2);

 // Test carrying over when adding two binary numbers
 String num5 = "1011";
 String num6 = "1101";
 String expectedSum3 = "11110";
 String result3 = binaryCalculate.addBinary(num5, num6);
 assertEquals(expectedSum3, result3);
 }

 @Test
 public void testSubBinary() {
 // Test subtracting a smaller binary number from a larger one
 String num1 = "1111";
 String num2 = "1110";
 String expectedDiff = "0011";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expectedDiff, result);

 // Test subtracting a larger binary number from a smaller one
 String num3 = "1011";
 String num4 = "1111";
 String expectedDiff2 = "01001";
 String result2 = binaryCalculate.subBinary(num3, num4);
 assertEquals(expectedDiff2, result2);

 // Test handling two's complement
 String num5 = "1111";
 String expectedTwosComplement = "01111";
 String num6 = binaryCalculate.twosComplement(num5);
 String result3 = binaryCalculate.subBinary(num5, num6);
 assertEquals(expectedTwosComplement, num6);
 assertEquals("0", result3);
 }

 @Test
 public void testIPCalculate() {
 // Test IPv4 calculation
 String ip = "192.168.1.1";
 String ipPrefix = "24";
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
 "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
 "Total usable IP Addresses : 254 \n" +
 "Subnet: 11101100.11110100.01110100.00001111\n" +
 "Binary Subnet: 11101100.11110100.01110100.00001111\n" +
 "Broadcast Address: 192.168.1.255\n" +
 "Prefix: /24\n" +
 "Netmask: 255.255.255.0\n" +
 "Binary Netmask: 11111111.11111111.11111111.00000000\n";
 String result = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertEquals(expectedOutput, result);

 // Test IPv6 calculation
 String ip6 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String ipPrefix6 = "60";
 String expectedOutput6 = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
 "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7333 -- FFFE:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
 "Total usable IP Addresses : 65534\n" +
 "Subnet: 01111101.01111111.10101101.00000000.00001010.11101110.00110011.11010100\n" +
 "Binary Subnet: 01111101.01111111.10101101.00000000.00001010.11101110.00110011.11010100\n" +
 "Broadcast Address: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
 "Prefix: /60\n" +
 "Netmask: 255.255.255.252.255.255.255.255.255.255.255.255.255.255.255.255\n" +
 "Binary Netmask: 01111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111\n";
 String result6 = binaryCalculate.IPCalculate(ip6, ipPrefix6);
 assertEquals(expectedOutput6, result6);
 }

 // Test error cases
 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculateWithInvalidIPPrefixLength() {
 binaryCalculate.IPCalculate("192.168.1.1", "16");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculateWithInvalidIP() {
 binaryCalculate.IPCalculate("256.256.256.256", "24");
 }
}