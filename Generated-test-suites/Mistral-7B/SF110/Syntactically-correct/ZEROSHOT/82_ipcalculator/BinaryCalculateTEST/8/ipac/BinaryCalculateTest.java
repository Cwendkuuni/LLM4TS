import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.math.BigInteger;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expected = "10111";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testSubBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expected = "0111";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);
 }

 @Test
 public void testIPCalculate() {
 String ip = "192.168.1.1";
 String ipPrefix = "24";
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11000010.11111100.01101100.00001111\nBinary Subnet: 11110000.11111100.01101100.00010111\nBroadcast Address: 11111111.11111111.01101111.11111111\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.11111111.00000000";
 binaryCalculate.IPCalculate(ip, ipPrefix);
 String resultOutput = binaryCalculate.getOutputText();
 assertEquals(expectedOutput, resultOutput);
 }

 @Test
 public void testPrefixInPrefixCalculate() {
 String ip = "192.168.1.1";
 String ipPrefix = "24";
 String endPrefix = "32";
 int noPrefix = 5;
 String[][] expectedOutput = {{"192.168.1.1 /24", "192.168.1.1 -- 192.168.1.255"}, {"Total usable IP Addresses :254 ", ""}, {"Subnet:", "11000010.11111100.01101100.00001111"}, {"Binary Subnet:", "11110000.11111100.01101100.00010111"}, {"Broadcast Address:", "11111111.11111111.01101111.11111111"}, {"Prefix:", "/32"}, {"Netmask", "0.0.0.0"}, {"Binary Netmask:", "00000000.00000000.00000000.00000000"}, {"192.168.1.2 /24", "192.168.1.2 -- 192.168.1.3"}, {"192.168.1.4 /24", "192.168.1.4 -- 192.168.1.5"}, {"192.168.1.6 /24", "192.168.1.6 -- 192.168.1.7"}, {"192.168.1.8 /24", "192.168.1.8 -- 192.168.1.9"}, {"192.168.1.10 /24", "192.168.1.10 -- 192.168.1.11"}, {"Total usable IP Addresses :126"}, {"Subnet:", "11000010.11111100.01101100.00011100"}, {"Binary Subnet:", "11110000.11111100.01101100.11101100"}, {"Broadcast Address:", "11111111.11111111.01101111.11111111"}, {"Prefix:", "/24"}, {"Netmask", "255.255.255.0"}, {"Binary Netmask:", "11111111.11111111.11111111.00000000"}, {"192.168.1.12 /24", "192.168.1.12 -- 192.168.1.13"}, {"192.168.1.14 /24", "192.168.1.14 -- 192.168.1.15"}, {"192.168.1.16 /24", "192.168.1.16 -- 192.168.1.17"}, {"192.168.1.18 /24", "192.168.1.18 -- 192.168.1.19"}, {"192.168.1.20 /24", "192.168.1.20 -- 192.168.1.21"}, {"Total usable IP Addresses :126"}, {"Subnet:", "11000010.11111100.01101100.00011111"}, {"Binary Subnet:", "11110000.11111100.01101100.11110111"}, {"Broadcast Address:", "11111111.11111111.01101111.11111111"}, {"Prefix:", "/24"}, {"Netmask", "255.255.255.0"}, {"Binary Netmask:", "11111111.11111111.11111111.00000000"}, {"192.168.1.21 /24", "192.168.1.21 -- 192.168.1.22"}, {"192.168.1.23 /24", "192.168.1.23 -- 192.168.1.24"}, {"Total usable IP Addresses :126"}, {"Subnet:", "11000010.11111100.01101100.00011111"}, {"Binary Subnet:", "11110000.11111100.01101100.11111000"}, {"Broadcast Address:", "11111111.11111111.01101111.11111111"}, {"Prefix:", "/24"}, {"Netmask", "255.255.255.0"}, {"Binary Netmask:", "11111111.11111111.11111111.00000000"}, {"192.168.1.24 /24", "192.168.1.24 -- 192.168.1.25"}, {"Total usable IP Addresses :126"}};
 binaryCalculate.prefixInPrefixCalculate(ip, ipPrefix, noPrefix, endPrefix);
 String[][] resultOutput = binaryCalculate.getOutput();
 assertEquals(expectedOutput[0][0], resultOutput[0][0]);
 assertEquals(expectedOutput[0][1], resultOutput[0][1]);
 // Add more assertions for the rest of the output
 }

 @Test
 public void testConvert() {
 String binaryIP = "11110110.11111101.01101101.00101111";
 String expected = "192.168.10.231";
 String result = binaryCalculate.convert(binaryIP);
 assertEquals(expected, result);
 }

 @Test
 public void testConvertToBinary() {
 String ip = "192.168.1.1";
 String expected = "11100101.11111110.01101100.00010111";
 String result = binaryCalculate.convertToBinary(ip);
 assertEquals(expected, result);
 }

 @Test
 public void testGetStartIP() {
 String ipRangeAddress = "192.168.1.1 -- 192.168.1.10";
 String expected = "192.168.1.1";
 String result = binaryCalculate.getStartIP();
 assertEquals(expected, result);
 }

 @Test
 public void testCalculate() {
 String subnet = "11100101.11111110.01101100.00010111";
 String ipPrefix = "24";
 String expectedIPRangeAddress = "192.168.1.1 -- 192.168.1.255";
 String expectedUsableRange = "192.168.1.2 -- 192.168.1.254";
 binaryCalculate.calculate(subnet, ipPrefix);
 assertEquals(expectedIPRangeAddress, binaryCalculate.getIPRangeAddress());
 assertEquals(expectedUsableRange, binaryCalculate.getUsableRange());
 }
}