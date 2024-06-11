package ipac;

import org.junit.Test;
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
 }

 @Test
 public void testSubBinary() {
 String num1 = "11111111";
 String num2 = "10101010";
 String expected = "01010101";
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
                        "Subnet: 11000000.11111100.01101100.00001111\n" +
                        "Binary Subnet: 11111100.11111111.01101100.00001111\n" +
                        "Broadcast Address: 192.168.1.255\n" +
                        "Prefix: /24\n" +
                        "Netmask: 255.255.255.0\n" +
                        "Binary Netmask: 11111100.00000000.00000000.00000000";
 String resultOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertEquals(expectedOutput, resultOutput);
 }
}