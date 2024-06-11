import org.junit.Test;
import ipac.BinaryCalculate;
import static org.junit.Assert.assertEquals;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expected = "11111010";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);

 num1 = "1010";
 num2 = "1111";
 expected = "10011111";
 result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);

 num1 = "0000";
 num2 = "0000";
 expected = "0000";
 result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);

 num1 = "0001";
 num2 = "1111";
 expected = "111001";
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

 num1 = "1010";
 num2 = "1111";
 expected = "00010110";
 result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);

 num1 = "0000";
 num2 = "0000";
 expected = "0000";
 result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);

 num1 = "1111";
 num2 = "0000";
 expected = "1111";
 result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculateInvalidIP() {
 binaryCalculate.IPCalculate("1234567890", "11111111");
 }

 @Test
 public void testIPCalculateIPv4() {
 String ip = "192.168.0.1";
 String prefix = "24";
 String expected = "Total Range: 192.168.0.1 -- 192.168.0.255\n" +
 "Usable Range: 192.168.0.2 -- 192.168.0.254\n" +
 "Total usable IP Addresses : 254 \n" +
 "Subnet: 11001100.11110000.00001111.00000110\n" +
 "Binary Subnet: 11110011 11110000 00001111 00001101\n" +
 "Broadcast Address: 192.168.0.255\n" +
 "Prefix: /24\n" +
 "Netmask: 255.255.255.0\n" +
 "Binary Netmask: 00111111 11111111 11111111 00000000";
 String result = binaryCalculate.IPCalculate(ip, prefix);
 assertEquals(expected, result);
 }

 @Test
 public void testIPCalculateIPv6() {
 String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String prefix = "60";
 String expected = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
 "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7335 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
 "Total usable IP Addresses : 65534\n" +
 "Subnet: 01111110.11011011.01010101.00000000.00001010.11101110.00110101.11110010\n" +
 "Binary Subnet: 00111110 11011011 01010101 00000000 00101010 11101110 00110101 11110010\n" +
 "Broadcast Address: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
 "Prefix: /60\n" +
 "Netmask: 255.255.255.255.255.255.255.255.255.255.255.255.255.255.255.252\n" +
 "Binary Netmask: 11111111 11111111 11111111 11111111 11111111 11111111 11111111 11111101";
 String result = binaryCalculate.IPCalculate(ip, prefix);
 assertEquals(expected, result);
 }
}