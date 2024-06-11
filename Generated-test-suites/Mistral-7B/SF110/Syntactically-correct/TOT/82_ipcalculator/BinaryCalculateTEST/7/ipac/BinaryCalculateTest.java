import org.junit.Test;
import static org.junit.Assert.*;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expected = "11001";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);

 num1 = "00001011";
 num2 = "11011011";
 expected = "1101110001";
 result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);

 num1 = "11111111";
 num2 = "11111111";
 expected = "1111111111000";
 result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testSubBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expected = "0101";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);

 num1 = "01111111";
 num2 = "11111111";
 expected = "11110000";
 result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidIPPrefixLength() {
 binaryCalculate.IPCalculate("192.168.1.1", "256");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidIPAddress() {
 binaryCalculate.IPCalculate("256.256.256.256", "24");
 }

 @Test
 public void testIPCalculate() {
 String ip = "192.168.1.1";
 String prefixLength = "24";
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
 "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
 "Total usable IP Addresses : 254 \n" +
 "Subnet: 11000010.11111100.00111111.01111100\n" +
 "Binary Subnet: 11000010.11111100.00111111.01111100\n" +
 "Broadcast Address: 192.168.1.255\n" +
 "Prefix: /24\n" +
 "Netmask: 255.255.255.0\n" +
 "Binary Netmask: 01111111.11111111.11111111.00000000";
 String resultOutput = binaryCalculate.IPCalculate(ip, prefixLength);
 assertEquals(expectedOutput, resultOutput);
 }

}