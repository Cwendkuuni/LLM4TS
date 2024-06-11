package ipac;

import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary_SameLength_ZeroAndZero() {
 String num1 = "01111111";
 String num2 = "01111111";
 String expected = "11111111";
 String actual = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddBinary_SameLength_ZeroAndOne() {
 String num1 = "01111111";
 String num2 = "10111111";
 String expected = "100000001";
 String actual = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddBinary_SameLength_OneAndZero() {
 String num1 = "10111111";
 String num2 = "01111111";
 String expected = "11000001";
 String actual = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddBinary_SameLength_OneAndOne() {
 String num1 = "10111111";
 String num2 = "11011111";
 String expected = "10000010";
 String actual = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddBinary_DifferentLengths_ZeroAndZero() {
 String num1 = "0111";
 String num2 = "0011";
 String expected = "1010";
 String actual = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddBinary_DifferentLengths_ZeroAndOne() {
 String num1 = "0111";
 String num2 = "1011";
 String expected = "1110";
 String actual = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddBinary_DifferentLengths_OneAndZero() {
 String num1 = "1011";
 String num2 = "0111";
 String expected = "1110";
 String actual = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, actual);
 }

 @Test
 public void testAddBinary_DifferentLengths_OneAndOne() {
 String num1 = "1011";
 String num2 = "1111";
 String expected = "10000";
 String actual = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidInput_EmptyString() {
 String num1 = "";
 String num2 = "1111";
 binaryCalculate.addBinary(num1, num2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidInput_Null() {
 String num1 = null;
 String num2 = "1111";
 binaryCalculate.addBinary(num1, num2);
 }

 @Test
 public void testSubBinary_SameLength_ZeroAndZero() {
 String num1 = "01111111";
 String num2 = "01111111";
 String expected = "00000000";
 String actual = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, actual);
 }

 @Test
 public void testSubBinary_SameLength_ZeroAndOne() {
 String num1 = "01111111";
 String num2 = "10111111";
 String expected = "10000001";
 String actual = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, actual);
 }

 @Test
 public void testSubBinary_SameLength_OneAndZero() {
 String num1 = "10111111";
 String num2 = "01111111";
 String expected = "10000000";
 String actual = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, actual);
 }

 @Test
 public void testSubBinary_SameLength_OneAndOne() {
 String num1 = "10111111";
 String num2 = "11011111";
 String expected = "00100000";
 String actual = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, actual);
 }

 @Test
 public void testSubBinary_DifferentLengths_ZeroAndZero() {
 String num1 = "0111";
 String num2 = "0011";
 String expected = "1010";
 String actual = binaryCalculate.subBinary(num2, num1);
 assertEquals(expected, actual);
 }

 @Test
 public void testSubBinary_DifferentLengths_ZeroAndOne() {
 String num1 = "0111";
 String num2 = "1011";
 String expected = "1100";
 String actual = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, actual);
 }

 @Test
 public void testSubBinary_DifferentLengths_OneAndZero() {
 String num1 = "1011";
 String num2 = "0111";
 String expected = "1100";
 String actual = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, actual);
 }

 @Test
 public void testSubBinary_DifferentLengths_OneAndOne() {
 String num1 = "1011";
 String num2 = "1111";
 String expected = "0100";
 String actual = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubBinary_InvalidInput_EmptyString() {
 String num1 = "";
 String num2 = "1111";
 binaryCalculate.subBinary(num1, num2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubBinary_InvalidInput_Null() {
 String num1 = null;
 String num2 = "1111";
 binaryCalculate.subBinary(num1, num2);
 }

 @Test
 public void testIPCalculate_IPv4_ValidInput() {
 String ip = "192.168.1.1";
 String prefix = "24";
 String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
         "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
         "Total usable IP Addresses : 254 \n" +
         "Subnet: 11011010.01111101.01101100.00001111\n" +
         "Binary Subnet: 11110110.11011010.11011000.00011111\n" +
         "Broadcast Address: 192.168.1.255\n" +
         "Prefix: /24\n" +
         "Netmask: 255.255.255.0\n" +
         "Binary Netmask: 11111111.11111111.11111111.00000000";
 String actualOutputText = binaryCalculate.IPCalculate(ip, prefix);
 assertEquals(expectedOutputText, actualOutputText);
 }

 @Test
 public void testIPCalculate_IPv4_InvalidInput_Length() {
 String ip = "192.168.1";
 String prefix = "24";
 binaryCalculate.IPCalculate(ip, prefix);
 }

 @Test
 public void testIPCalculate_IPv4_InvalidInput_PrefixLength() {
 String ip = "192.168.1.1";
 String prefix = "25";
 binaryCalculate.IPCalculate(ip, prefix);
 }

 @Test
 public void testIPCalculate_IPv6_ValidInput() {
 String ip = "2001:0db8:85a3:0000:0242:100a:0064:1234";
 String prefix = "60";
 String expectedOutputText = "Total Range: 2001:0db8:85a3:0000:0242:100a:0064:1234 -- FFFFFFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
         "Usable Range: 2001:0db8:85a3:0000:0242:100a:0064:1235 -- FFFFFFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
         "Total usable IP Addresses : 65536 \n" +
         "Subnet: 01111110.01111101.01101111.00000000.0242.0100.0064.1234\n" +
         "Binary Subnet: 01111110.01111101.01101111.00000000.0242.0100.0064.1234\n" +
         "Broadcast Address: FFFFFFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
         "Prefix: /60\n" +
         "Netmask: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
         "Binary Netmask: 00000000.00000000.00000000.00000000.FFFF:FFFF:FFFF:FFFF";
 String actualOutputText = binaryCalculate.IPCalculate(ip, prefix);
 assertEquals(expectedOutputText, actualOutputText);
 }

 @Test
 public void testIPCalculate_IPv6_InvalidInput_Length() {
 String ip = "2001:0db8:85a3:0000:0242:100a:0064:123";
 String prefix = "60";
 binaryCalculate.IPCalculate(ip, prefix);
 }

 @Test
 public void testIPCalculate_IPv6_InvalidInput_PrefixLength() {
 String ip = "2001:0db8:85a3:0000:0242:100a:0064:1234";
 String prefix = "61";
 binaryCalculate.IPCalculate(ip, prefix);
 }
}