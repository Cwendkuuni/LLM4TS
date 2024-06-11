package ipac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotSame;

import java.math.BigInteger;
import org.junit.Test;

public class BinaryCalculateTest {

 binaryCalculate obj = new binaryCalculate();

 @Test
 public void testCalculate() {
 String IP = "192.168.1.1";
 String IPPrefix = "24";
 obj.prefixInPrefixCalculate(IP, IPPrefix, 5, "32");
 String expectedOutput = "192.168.1.1 /256 Total usable IP Addresses :126  Subnet:11110010.11111100.01110100.00001011 Binary Subnet:11110010.11111100.01110100.00001011 Broadcast Address:11110010.11111100.01110101.00001111 Prefix:32 Netmask:0.0.0.255 Binary Netmask:0.0.0.11111111";
 String actualOutput = "";
 try {
 obj.prefixInPrefixCalculate(IP, IPPrefix, 5, "32");
 actualOutput = obj.getOutput()[5][1];
 } catch (OutOfMemoryError e) {
 //Handle out of memory error
 }
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testAddBinary() {
 String ip1 = "10101010";
 String ip2 = "11111101";
 String expectedOutput = "11110111";
 String actualOutput = obj.addBinary(ip1, ip2);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testSubBinary() {
 String ip1 = "11110111";
 String ip2 = "10101010";
 String expectedOutput = "01010101";
 String actualOutput = obj.subBinary(ip1, ip2);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testIPCalculate() {
 String IP = "192.168.1.1";
 String IPPrefix = "24";
 obj.IPCalculate(IP, IPPrefix);
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255 Usable Range: 192.168.1.2 -- 192.168.1.254 Total usable IP Addresses :254 Subnet: 11111010.11111100.01111000.00011111 Binary Subnet: 11111010.11111100.01111000.00011111 Broadcast Address: 11111010.11111100.01111000.01111111 Prefix: /24 Netmask: 255.255.255.0 Binary Netmask: 00111111.11111111.11111111.00000000";
 String actualOutput = "";
 try {
 obj.IPCalculate(IP, IPPrefix);
 actualOutput = obj.getOutput()[0];
 } catch (OutOfMemoryError e) {
 //Handle out of memory error
 }
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetBinaryIP() {
 String ip = "192.168.1.1";
 String expectedOutput = "01101110.01101100.01101100.01101101";
 String actualOutput = obj.getBinaryIP(ip);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testGetNetmask() {
 String ip = "255.255.255.0";
 String expectedOutput = "00111111.00111111.00111111.00000000";
 String actualOutput = obj.getNetmask(ip);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testCalculateWithInvalidIPPrefix() {
 String IP = "192.168.1.1";
 String IPPrefix = "33";
 obj.prefixInPrefixCalculate(IP, IPPrefix, 5, "32");
 assertNull(obj.getOutput());
 }

 @Test
 public void testIPCalculateWithInvalidIP() {
 String IP = "192.168.1.257";
 String IPPrefix = "24";
 obj.IPCalculate(IP, IPPrefix);
 assertNull(obj.getOutput());
 }

 @Test
 public void testCalculateWithNullIP() {
 String IP = null;
 String IPPrefix = "24";
 obj.prefixInPrefixCalculate(IP, IPPrefix, 5, "32");
 assertNull(obj.getOutput());
 }
}