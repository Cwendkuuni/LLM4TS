import org.junit.Test;
import static org.junit.Assert.*;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

 private final BinaryCalculate calculator = new BinaryCalculate();

 @Test
 public void testAddBinary_SameLengthIPs() {
 String ip1 = "11110110111111011111011011111101";
 String ip2 = "11111111111111111111111111111111";
 String expectedResult = "1111111111111111111111111111111111010101";
 String result = calculator.addBinary(ip1, ip2);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testAddBinary_DifferentLengthIPs() {
 String ip1 = "11110110111111011111011011111101";
 String ip2 = "01111111111111111111111111111111";
 String expectedResult = "1111111111111111111111111111111111010101";
 String result = calculator.addBinary(ip1, ip2);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testAddBinary_Carry() {
 String ip1 = "11110110111111011111011011111101";
 String ip2 = "11111111111111111111111111111111";
 String expectedResult = "11111111111111111111111111111111111010101";
 String result = calculator.addBinary(ip1, ip2);
 assertEquals(expectedResult, result);
 }

 @Test(expected = RuntimeException.class)
 public void testSubBinary_InvalidIP() {
 String ip1 = "11110110111111011111011011111101";
 String ip2 = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
 String expectedResult = "";
 String result = calculator.subBinary(ip1, ip2);
 assertEquals(expectedResult, result);
 }

 @Test(expected = RuntimeException.class)
 public void testSubBinary_InvalidOperation() {
 String ip1 = "11110110111111011111011011111101";
 String ip2 = "11110110111111011111011011111101";
 String expectedResult = "";
 String result = calculator.subBinary(ip2, ip1);
 assertEquals(expectedResult, result);
 }

 @Test
 public void testIPCalculate_ValidIP() {
 String ip = "192.168.1.1";
 String prefix = "24";
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\nSubnet: 11001010.11111100.00001101.00000111\nBinary Subnet: 11001010.11111100.00001101.00000111\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 01111111.11111111.11111111.00000000";
 String output = calculator.IPCalculate(ip, prefix);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testIPCalculate_InvalidIP() {
 String ip = "256.256.256.256";
 String prefix = "24";
 String expectedOutput = "";
 String output = calculator.IPCalculate(ip, prefix);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testIPCalculate_InvalidPrefix() {
 String ip = "192.168.1.1";
 String prefix = "33";
 String expectedOutput = "";
 String output = calculator.IPCalculate(ip, prefix);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testIPCalculate_IPv6() {
 String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 String prefix = "64";
 String expectedOutput = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- 2001:0db8:85a3:0000:0000:8a2e:ff:ff\nSubnet: 01111101.01111011.01010101.00000000.00000000.0a2e.0370.7334\nBinary Subnet: 01111101.01111011.01010101.00000000.00000000.0a2e.0370.7334\nBroadcast Address: 2001:0db8:85a3:ff:ff:ff:ff:ff\nPrefix: /64\nNetmask: ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff\nBinary Netmask: 00000000.ffffffff.ffffffff.ffffffffff.ffffffff.ffffffff.ffffffffff.ffffffffff";
 String output = calculator.IPCalculate(ip, prefix);
 assertEquals(expectedOutput, output);
 }

}