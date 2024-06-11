import org.junit.Test;
import static org.junit.Assert.*;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

 private final BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary_TwoZeroStrings() {
 String num1 = "0000";
 String num2 = "0000";
 String expectedSum = "0000";
 String actualSum = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testAddBinary_TwoOneStrings() {
 String num1 = "0011";
 String num2 = "0110";
 String expectedSum = "1011";
 String actualSum = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testAddBinary_Carry() {
 String num1 = "1111";
 String num2 = "1111";
 String expectedSum = "10000";
 String actualSum = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedSum, actualSum);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidInput() {
 String num1 = "";
 String num2 = "1111";
 String expectedMessage = "Invalid input format";
 assertThrows(IllegalArgumentException.class, () -> binaryCalculate.addBinary(num1, num2));
 assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testSubBinary_TwoEqualStrings() {
 String num1 = "1111";
 String num2 = "1111";
 String expectedDiff = "0000";
 String actualDiff = binaryCalculate.subBinary(num1, num2);
 assertEquals(expectedDiff, actualDiff);
 }

 @Test
 public void testSubBinary_DifferentStrings() {
 String num1 = "1111";
 String num2 = "0110";
 String expectedDiff = "1001";
 String actualDiff = binaryCalculate.subBinary(num1, num2);
 assertEquals(expectedDiff, actualDiff);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubBinary_InvalidInput() {
 String num1 = "1111";
 String num2 = "";
 String expectedMessage = "Invalid input format";
 assertThrows(IllegalArgumentException.class, () -> binaryCalculate.subBinary(num1, num2));
 assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testIPCalculate_IPv4() {
 String ip = "192.168.0.1";
 String prefix = "24";
 String expectedOutput = "Total Range: 192.168.0.1 -- 192.168.0.255\n" +
                        "Usable Range: 192.168.0.2 -- 192.168.0.254\n" +
                        "Total usable IP Addresses : 254 \n" +
                        "Subnet: 11101100.11110100.00001011.00001001\n" +
                        "Binary Subnet: 11110000.11111100.00111110.00110110\n" +
                        "Broadcast Address: 192.168.0.255\n" +
                        "Prefix: /24\n" +
                        "Netmask: 255.255.255.0\n" +
                        "Binary Netmask: 11111111.11111111.11111111.00000000";
 binaryCalculate.IPCalculate(ip, prefix);
 String actualOutput = binaryCalculate.getOutputText();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testIPCalculate_IPv6() {
 String ip = "2001:0db8:85a3:0000:0270:f1ff:fe21:67cf";
 String prefix = "60";
 String expectedOutput = "Total Range: 2001:0db8:85a3:0000:0270:f1ff:fe21:67cf -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                        "Usable Range: 2001:0db8:85a3:0000:0270:f1ff:fe21:67cflocalhost -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FF\n" +
                        "Total usable IP Addresses : 65504 \n" +
                        "Subnet: 01111111.11011111.10101011.00000000.00101111.11111111.11111110.11111111\n" +
                        "Binary Subnet: 01111111.11011111.10101011.00000000.00101111.11111111.11111110.11111111\n" +
                        "Broadcast Address: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                        "Prefix: /60\n" +
                        "Netmask: FFFE::\n" +
                        "Binary Netmask: 11111111.11111111.11111111.11111111.00000000.00000000.00000000.00000000";
 binaryCalculate.IPCalculate(ip, prefix);
 String actualOutput = binaryCalculate.getOutputText();
 assertEquals(expectedOutput, actualOutput);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIP() {
 String ip = "abcd.efgh.ijk.lmn";
 String prefix = "24";
 String expectedMessage = "Invalid IP address format";
 assertThrows(IllegalArgumentException.class, () -> binaryCalculate.IPCalculate(ip, prefix));
 assertEquals(expectedMessage, exception.getMessage());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidPrefix() {
 String ip = "192.168.0.1";
 String prefix = "33";
 String expectedMessage = "Invalid prefix length";
 assertThrows(IllegalArgumentException.class, () -> binaryCalculate.IPCalculate(ip, prefix));
 assertEquals(expectedMessage, exception.getMessage());
 }

 @Test
 public void testPrefixInPrefixCalculate() {
 String ip = "192.168.0.0";
 String prefix = "24";
 int noPrefix = 5;
 String endPrefix = "32";
 binaryCalculate.prefixInPrefixCalculate(ip, prefix, noPrefix, endPrefix);
 String expectedOutput = "Total usable IP Addresses : 240 \n" +
                       "Subnet: 11101100.11110100.00001011.00000000\n" +
                       "Binary Subnet: 11110000.11111100.00111110.00000000\n" +
                       "Broadcast Address: 192.168.0.255\n" +
                       "Prefix: /24\n" +
                       "Netmask: 255.255.255.0\n" +
                       "Binary Netmask: 11111111.11111111.11111111.00000000\n";
 String actualOutput = binaryCalculate.getOutputText();
 assertEquals(expectedOutput, actualOutput);
 }

}