import org.junit.Test;
import static org.junit.Assert.*;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

 private final BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary_IPv4_Valid() {
 String num1 = "11111110";
 String num2 = "10101010";
 String expected = "1111111010101010";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testAddBinary_IPv4_Carry() {
 String num1 = "11111111";
 String num2 = "11111111";
 String expected = "100000000001";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testAddBinary_IPv6_Valid() {
 String num1 = "2540:0:0:0:0:FFFF:FE12:34";
 String num2 = "2540:0:0:0:0:FFFF:FE11:56";
 String expected = "2540:0:0:0:0:FFFF:FE12:34:56";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_IPv4_InvalidLength_Num1() {
 String num1 = "1111111";
 String num2 = "10101010";
 binaryCalculate.addBinary(num1, num2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_IPv4_InvalidLength_Num2() {
 String num1 = "11111110";
 String num2 = "101010";
 binaryCalculate.addBinary(num1, num2);
 }

 @Test
 public void testSubBinary_IPv4_Valid() {
 String num1 = "11111111";
 String num2 = "10101010";
 String expected = "00010101";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testSubBinary_IPv4_Carry() {
 String num1 = "11111111";
 String num2 = "11111110";
 String expected = "00000001";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testIPCalculate_IPv4_Valid() {
 String ip = "192.168.1.1";
 String prefix = "24";
 String expected = "Total Range: 192.168.1.1 -- 192.168.1.255\nSubnet: 192.168.1\nBinary Subnet: 11100110.11101000.01101100.00001111\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 01111111.11111111.11111111.00000000";
 binaryCalculate.IPCalculate(ip, prefix);
 String output = binaryCalculate.getOutputText();
 assertEquals(expected, output);
 }

 @Test
 public void testIPCalculate_IPv4_InvalidInput() {
 String ip = "256.256.256.256";
 String prefix = "33";
 binaryCalculate.IPCalculate(ip, prefix);
 }

 @Test
 public void testIPCalculate_IPv6_Valid() {
 String ip = "2001:0db8:85a3:0000:0270:f1ff:fe21:67cf";
 String prefix = "64";
 String expected = "Total Range: 2001:0db8:85a3:0000:0270:f1ff:fe21:67cf -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\nSubnet: 2001:0db8:85a3:0000:0270:f1ff:fe21:\nBinary Subnet: 2001:0db8:85a3:0000:0270:f1ff:fe21:00000000\nBroadcast Address: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\nPrefix: /64\nNetmask: 255.255.255.255.255.255.255.255\nBinary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111";
 binaryCalculate.IPCalculate(ip, prefix);
 String output = binaryCalculate.getOutputText();
 assertEquals(expected, output);
 }

 @Test
 public void testGetBinaryIP_IPv4_Valid() {
 String binaryIP = "11111110.10101010.01101100.00001111";
 String expected = "11111110101010100110110000011111";
 String result = binaryCalculate.getBinaryIP(binaryIP);
 assertEquals(expected, result);
 }

 @Test
 public void testGetBinaryIP_IPv4_InvalidInput() {
 String binaryIP = "11111110.10101010.01101100.11111111";
 binaryCalculate.getBinaryIP(binaryIP);
 }

 @Test
 public void testGetNetmask_IPv4_Valid() {
 String binaryIP = "11111110.10101010.01101100.00001111";
 String prefix = "24";
 String expected = "01111111.11111111.11111111.00000000";
 String result = binaryCalculate.getBinaryIP(binaryCalculate.getNetmask(binaryIP) + "");
 assertEquals(expected, result);
 }

 @Test
 public void testGetNetmask_IPv4_InvalidInput() {
 String binaryIP = "11111110.10101010.01101100.11111111";
 String prefix = "33";
 binaryCalculate.getNetmask(binaryIP);
 }

 @Test
 public void testCalculate_IPv4_Valid() {
 String subnet = "192.168.1.0";
 String noOfIPAddresses = "254";
 binaryCalculate.calculate(subnet, noOfIPAddresses);
 String expected = "Total Range: 192.168.1.1 -- 192.168.1.254\nSubnet: 192.168.1\nBinary Subnet: 11100110.11101000.01101100.00111110\nBroadcast Address: 192.168.1.255\nPrefix: /24\n";
 String output = binaryCalculate.getOutputText();
 assertEquals(expected, output);
 }

 @Test
 public void testCalculate_IPv4_Carry() {
 String subnet = "192.168.1.0";
 String noOfIPAddresses = "256";
 binaryCalculate.calculate(subnet, noOfIPAddresses);
 }

 @Test
 public void testCalculate_IPv6_Valid() {
 String subnet = "2001:0db8:85a3:0000:0270:f1ff:fe21:67cf";
 String noOfIPAddresses = "65534";
 binaryCalculate.calculate(subnet, noOfIPAddresses);
 String expected = "Total Range: 2001:0db8:85a3:0000:0270:f1ff:fe21:67cf -- FFFE:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\nSubnet: 2001:0db8:85a3:0000:0270:f1ff:fe21:\nBinary Subnet: 2001:0db8:85a3:0000:0270:f1ff:fe21:00000000\nBroadcast Address: FFFE:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\nPrefix: /64\n";
 String output = binaryCalculate.getOutputText();
 assertEquals(expected, output);
 }

 @Test
 public void testCalculate_IPv6_Carry() {
 String subnet = "2001:0db8:85a3:0000:0270:f1ff:fe21:67cf";
 String noOfIPAddresses = "65535";
 binaryCalculate.calculate(subnet, noOfIPAddresses);
 }
}