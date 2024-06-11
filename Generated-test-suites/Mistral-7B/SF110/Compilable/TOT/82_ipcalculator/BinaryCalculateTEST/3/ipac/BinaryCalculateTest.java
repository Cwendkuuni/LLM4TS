package ipac;
import org.junit.Test;
import ipac.BinaryCalculate;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary_IPv4_IPv4() {
 String num1 = "11110110.11111101.01101100.01101111";
 String num2 = "11111011.11101001.01110010.01111110";
 String expected = "11111111.11111111.01111110.01111111";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testAddBinary_IPv4_IPv4_Carry() {
 String num1 = "11111111.11111111.11111111.11111111";
 String num2 = "00000000.00000000.00000000.00000001";
 String expected = "11111111.11111111.11111111.11111111";
 String expectedCarry = "00000001.00000000.00000000.00000000";
 String result = binaryCalculate.addBinary(num1, num2);
 String resultCarry = binaryCalculate.addBinary(expected, expectedCarry);
 assertEquals(resultCarry, result);
 }

 @Test
 public void testAddBinary_IPv4_IPv4_Negative() {
 String num1 = "00001111.00111100.11001101.11110111";
 String num2 = "11110101.11010011.10111010.01110110";
 String expected = "11111110.11111111.11110111.00000002";
 String result = binaryCalculate.addBinary(num1, num2);
 String expectedNegative = "11111110.11111111.11110111.11111110";
 String resultNegative = binaryCalculate.addBinary(expected, "11111111.11111111.11111111.11111111");
 assertEquals(resultNegative, result);
 }

 @Test
 public void testSubBinary_IPv4_IPv4() {
 String num1 = "11111111.11111111.11111111.11111111";
 String num2 = "11111011.11101001.01110010.01111110";
 String expected = "00000100.00010111.10011002.01100002";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testSubBinary_IPv4_IPv4_Negative() {
 String num1 = "00001111.00111100.11001101.11110111";
 String num2 = "11110101.11010011.10111010.01110110";
 String expected = "11111000.11001010.10110101.10001101";
 String result = binaryCalculate.subBinary(num2, num1);
 assertEquals(expected, result);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIPv4() {
 String ip = "1111.1111.1111.1111";
 String prefix = "32";
 binaryCalculate.IPCalculate(ip, prefix);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIPv6() {
 String ip = ":::";
 String prefix = "32";
 binaryCalculate.IPCalculate(ip, prefix);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidPrefixLengthIPv4() {
 String ip = "192.168.1.1";
 String prefix = "256";
 binaryCalculate.IPCalculate(ip, prefix);
 }

 @Test (expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidPrefixLengthIPv6() {
 String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String prefix = "256";
 binaryCalculate.IPCalculate(ip, prefix);
 }

 @Test
 public void testIPCalculate_ValidIPv4() {
 String ip = "192.168.1.1";
 String prefix = "32";
 binaryCalculate.IPCalculate(ip, prefix);
 }

 @Test
 public void testIPCalculate_ValidIPv6() {
 String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String prefix = "64";
 binaryCalculate.IPCalculate(ip, prefix);
 }

}