import org.junit.Test;
import static org.junit.Assert.*;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary_IPv4_ValidInput() {
 String ip1 = "11110110.11111111.11110110.00111111";
 String ip2 = "11111111.00111111.11110110.01111101";
 String expected = "11111111.11111101.11110110.01111110";
 assertEquals(expected, binaryCalculate.addBinary(ip1, ip2));
 }

 @Test
 public void testAddBinary_IPv4_Carry() {
 String ip1 = "11111111.11111111.11111111.11111111";
 String ip2 = "00000000.00000000.00000000.00000001";
 String expected = "10000000.00000000.00000001.00000000";
 assertEquals(expected, binaryCalculate.addBinary(ip1, ip2));
 }

 @Test
 public void testAddBinary_IPv6_ValidInput() {
 String ip1 = "2001:0db8:85a3:0000:0270:f1bf:fe1e:1234";
 String ip2 = "2001:0db8:85a3:0000:0270:f1bf:fe2e:1234";
 String expected = "2001:0db8:85a3:0000:0270:f1bf:fe3e:1234";
 assertEquals(expected, binaryCalculate.addBinary(ip1, ip2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidIPv4_Length() {
 String ip = "1111.1111.1111.1111";
 assertFalse(binaryCalculate.addBinary(ip, ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidIPv4_Format() {
 String ip = "11110110.11111111.11110110.256";
 assertFalse(binaryCalculate.addBinary(ip, ""));
 }

 @Test
 public void testSubBinary_IPv4_ValidInput() {
 String ip1 = "11111111.11111111.11111111.11111111";
 String ip2 = "11110110.11111111.11110110.01111101";
 String expected = "00001001.00000001.00001001.00000110";
 assertEquals(expected, binaryCalculate.subBinary(ip1, ip2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubBinary_InvalidIPv4_Length() {
 String ip1 = "11111111.11111111.11111111.";
 String ip2 = "11110110.11111111.11110110.01111101";
 assertFalse(binaryCalculate.subBinary(ip1, ip2));
 }

 @Test
 public void testIPCalculate_ValidIPv4() {
 String ip = "192.168.1.1";
 String prefix = "24";
 String expected = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
 "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
 "Total usable IP Addresses : 254\n" +
 "Subnet: 11111110.11111100.11111010.00011111\n" +
 "Binary Subnet: 11111110.11111100.11111010.00111111\n" +
 "Broadcast Address: 192.168.1.255\n" +
 "Prefix: /24\n" +
 "Netmask: 255.255.255.0\n" +
 "Binary Netmask: 01111111.11111111.11111111.00000000\n";
 assertEquals(expected, binaryCalculate.IPCalculate(ip, prefix));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIPv4_Length() {
 String ip = "192.168.1.";
 String prefix = "24";
 assertFalse(binaryCalculate.IPCalculate(ip, prefix));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIPv4_Format() {
 String ip = "192.168.1.256";
 String prefix = "24";
 assertFalse(binaryCalculate.IPCalculate(ip, prefix));
 }

 @Test
 public void testIPCalculate_ValidIPv6() {
 String ip = "2001:0db8:85a3:0000:0270:f1bf:fe1e:1234";
 String prefix = "64";
 String expected = "Total Range: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
 "Usable Range: 2001:0db8:85a3:0000:0270:f1bf:fe1e:1233\n" +
 "Total usable IP Addresses : 65533\n" +
 "Subnet: 01111111.11111111.11111111.00000000.0270.f1bf.fe1e.0000\n" +
 "Binary Subnet: 01111111.11111111.11111111.00000000.0270.f1bf.fe1e.0000\n" +
 "Broadcast Address: 2001:0db8:85a3:0000:0270:f1bf:fe1e:1235\n" +
 "Prefix: /64\n" +
 "Netmask: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
 "Binary Netmask: 00000000.00000000.00000000.00000000.FFFF.FFFF.FFFF.FFFF\n";
 assertEquals(expected, binaryCalculate.IPCalculate(ip, prefix));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIPv6_Length() {
 String ip = "2001:0db8:85a3:0000:0270:f1bf:fe1e:123.";
 String prefix = "64";
 assertFalse(binaryCalculate.IPCalculate(ip, prefix));
 }

}