package ipac;

import org.junit.Assert;import org.junit.Test;import java.math.BigInteger;import static org.junit.Assert.assertEquals;

public class BinaryCalculateTest {

BinaryCalculate binaryCalculate = new BinaryCalculate();

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
 String num1 = "1111";
 String num2 = "1010";
 String expected = "0101";
 String result = binaryCalculate.subBinary(num1, num2);
 assertEquals(expected, result);
}

@Test
public void testIPCalculate() {
 String ip = "192.168.1.1";
 String ipPrefix = "24";
 String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.1 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11000000.11111100.01101100.00001111\nBinary Subnet: 11111100.01110000.01101100.00001111\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111100";
 String result = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertEquals(expectedOutputText, result);
}

@Test
public void testGetBinaryIP() {
 String binaryIP = "11111100.11110000.01101100.00001111";
 String expected = "11111100.11110000.01101100.00001111";
 String result = binaryCalculate.getBinaryIP(binaryIP);
 assertEquals(expected, result);
}

@Test
public void testGetNetmask() {
 String binaryIP = "11111100.11110000.01101100.00001111";
 String expected = "00001111.00001111.11110011.11111000";
 String result = binaryCalculate.getNetmask(binaryIP);
 assertEquals(expected, result);
}

@Test
public void testCalculate() {
 String subnet = "11111100.11110000.01101100.00001111";
 String noOfIPAddresses = "256";
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 binaryCalculate.calculate(subnet, noOfIPAddresses);
 BigInteger expectedTotalIPAddresses = new BigInteger("257", 10);
 BigInteger resultTotalIPAddresses = binaryCalculate.totalIPAddresses;
 assertEquals(expectedTotalIPAddresses, resultTotalIPAddresses);
}

@Test
public void testPrefixInPrefixCalculate() {
 String ip = "192.168.1.1";
 String ipPrefix = "24";
 int noPrefix = 4;
 String endPrefix = "32";
 String[][] expectedOutput = {{"192.168.1.1 /24", "192.168.1.1 -- 192.168.1.255"},
 {"Total usable IP Addresses :", "254"},
 {"Subnet:", "11000000.11111100.01101100.00001111"},
 {"Binary Subnet:", "11111100.01110000.01101100.00001111"},
 {"Broadcast Address:", "192.168.1.255"},
 {"Prefix:", "/32"},
 {"Netmask", "255.255.255.0"},
 {"Binary Netmask:", "11111100"}};
 String[][] resultOutput = binaryCalculate.prefixInPrefixCalculate(ip, ipPrefix, noPrefix, endPrefix);
 assertEquals(expectedOutput, resultOutput);
}
}