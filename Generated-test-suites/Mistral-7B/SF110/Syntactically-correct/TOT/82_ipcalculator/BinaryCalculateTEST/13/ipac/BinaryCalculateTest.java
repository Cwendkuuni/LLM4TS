import org.junit.Test;
import static org.junit.Assert.*;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

 private final BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary_IPv4_ValidIPs() {
 String ip1 = "192.168.1.1";
 String ip2 = "192.168.1.2";
 String expected = "11100110.11110010.00001101.00111100";
 String sum = binaryCalculate.addBinary(ip1, ip2);
 assertEquals(expected, sum);
 }

 @Test
 public void testAddBinary_IPv6_ValidIPs() {
 String ip1 = "2001:0db8:85a3:0000:0270:f5ff:fe2e:0001";
 String ip2 = "2001:0db8:85a3:0000:0271:f5ff:fe2e:0002";
 String expected = "2001:0db8:85a3:0000:0271:f5ff:fe2e:0003";
 String sum = binaryCalculate.addBinary(ip1, ip2);
 assertEquals(expected, sum);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidIPv4() {
 String ip1 = "192.168.1.1.";
 String ip2 = "192.168.1.2";
 binaryCalculate.addBinary(ip1, ip2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidIPv6() {
 String ip1 = "2001:0db8:85a3:0000:0270:f5ff:fe2e:0001:1";
 String ip2 = "2001:0db8:85a3:0000:0271:f5ff:fe2e:0002";
 binaryCalculate.addBinary(ip1, ip2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_DifferentIPVersions() {
 String ip1 = "192.168.1.1";
 String ip2 = "2001:0db8:85a3:0000:0270:f5ff:fe2e:0001";
 binaryCalculate.addBinary(ip1, ip2);
 }

 @Test
 public void testSubBinary_IPv4_ValidIPs() {
 String ip1 = "192.168.1.4";
 String ip2 = "192.168.1.2";
 String expected = "11100110.11110010.00001101.00111101";
 String difference = binaryCalculate.subBinary(ip1, ip2);
 assertEquals(expected, difference);
 }

 @Test
 public void testSubBinary_IPv6_ValidIPs() {
 String ip1 = "2001:0db8:85a3:0000:0270:f5ff:fe2e:0001";
 String ip2 = "2001:0db8:85a3:0000:0271:f5ff:fe2e:0002";
 String expected = "2001:0db8:85a3:0000:0270:f5ff:fe2e:0000";
 String difference = binaryCalculate.subBinary(ip1, ip2);
 assertEquals(expected, difference);
 }

 @Test
 public void testIPCalculate_IPv4_ValidIPs() {
 String ip = "192.168.1.1";
 String ipPrefix = "24";
 String expectedTotalRange = "192.168.1.1 -- 192.168.1.254";
 String expectedUsableRange = "192.168.1.2 -- 192.168.1.253";
 String output = binaryCalculate.IPCalculate(ip, ipPrefix);
 String totalRange = output.split("\n")[1].split(": ")[1];
 String usableRange = output.split("\n")[2].split(": ")[1];
 assertEquals(expectedTotalRange, totalRange);
 assertEquals(expectedUsableRange, usableRange);
 }

 @Test
 public void testIPCalculate_IPv4_SubnetMask() {
 String ip = "192.168.1.1";
 String ipPrefix = "255.255.255.240";
 String expectedTotalRange = "192.168.1.1 -- 192.168.1.7";
 String expectedUsableRange = "192.168.1.2 -- 192.168.1.6";
 String output = binaryCalculate.IPCalculate(ip, ipPrefix);
 String totalRange = output.split("\n")[1].split(": ")[1];
 String usableRange = output.split("\n")[2].split(": ")[1];
 assertEquals(expectedTotalRange, totalRange);
 assertEquals(expectedUsableRange, usableRange);
 }

 @Test
 public void testIPCalculate_IPv6_ValidIPs() {
 String ip = "2001:0db8:85a3:0000:0270:f5ff:fe2e:0001";
 String ipPrefix = "64";
 String expectedTotalRange = "2001:0db8:85a3:0000:0270:f5ff:fe2e:0001 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF";
 String expectedUsableRange = "2001:0db8:85a3:0000:0270:f5ff:fe2e:0001 -- 2001:0db8:85a3:0000:FFFF:FFFF:FFFF:FFFF";
 String output = binaryCalculate.IPCalculate(ip, ipPrefix);
 String totalRange = output.split("\n")[1].split(": ")[1];
 String usableRange = output.split("\n")[2].split(": ")[1];
 assertEquals(expectedTotalRange, totalRange);
 assertEquals(expectedUsableRange, usableRange);
 }

 @Test
 public void testIPCalculate_IPv6_SubnetMask() {
 String ip = "2001:0db8:85a3:0000:0270:f5ff:fe2e:0001";
 String ipPrefix = "120";
 String expectedTotalRange = "2001:0db8:85a3:0000:0270:f5ff:fe2e:0001 -- 2001:0db8:85a3:0000:0000:0000:FFFF:FFFF";
 String expectedUsableRange = "2001:0db8:85a3:0000:0270:f5ff:fe2e:0001 -- 2001:0db8:85a3:0000:0000:FFFF:FFFF:FFFF";
 String output = binaryCalculate.IPCalculate(ip, ipPrefix);
 String totalRange = output.split("\n")[1].split(": ")[1];
 String usableRange = output.split("\n")[2].split(": ")[1];
 assertEquals(expectedTotalRange, totalRange);
 assertEquals(expectedUsableRange, usableRange);
 }

 @Test
 public void testIPCalculate_MismatchedIPVersions() {
 String ip = "192.168.1.1";
 String ipPrefix = "200::";
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

}