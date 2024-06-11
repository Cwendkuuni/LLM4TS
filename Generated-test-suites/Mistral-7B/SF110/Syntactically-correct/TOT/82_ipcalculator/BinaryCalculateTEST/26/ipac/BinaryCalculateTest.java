import org.junit.Test;
import ipac.BinaryCalculate;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

private BinaryCalculate binaryCalculate = new BinaryCalculate();

@Test
public void testAddBinary() {
String num1 = "1111";
String num2 = "1010";
String expected = "11010";
String result = binaryCalculate.addBinary(num1, num2);
assertEquals(expected, result);

num1 = "0000";
num2 = "1111";
expected = "1111";
result = binaryCalculate.addBinary(num1, num2);
assertEquals(expected, result);

num1 = "1001";
num2 = "1110";
expected = "111101";
result = binaryCalculate.addBinary(num1, num2);
assertEquals(expected, result);

num1 = "1111";
num2 = "1111";
expected = "11111100";
result = binaryCalculate.addBinary(num1, num2);
assertEquals(expected, result);

num1 = "11111111";
num2 = "1";
expected = "111111111";
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

num1 = "1111";
num2 = "1111";
expected = "0000";
result = binaryCalculate.subBinary(num1, num2);
assertEquals(expected, result);

num1 = "1010";
num2 = "1111";
expected = "110101"; // borrow from the previous bit
result = binaryCalculate.subBinary(num1, num2);
assertEquals(expected, result);

num1 = "11111111";
num2 = "0000";
expected = "11111111";
result = binaryCalculate.subBinary(num1, num2);
assertEquals(expected, result);

num1 = "11111111";
num2 = "11111111";
expected = "00000000";
result = binaryCalculate.subBinary(num1, num2);
assertEquals(expected, result);
}

@Test(expected = IllegalArgumentException.class)
public void testSubBinaryWithInvalidNumbers() {
String num1 = "1111";
String num2 = "1112"; // invalid length for IPv4 or IPv6
binaryCalculate.subBinary(num1, num2);
}

@Test
public void testIPCalculate() {
String ip = "192.168.1.1";
String prefix = "24";
String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
"Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
"Total usable IP Addresses : 254 \n" +
"Subnet: 11101100.11110100.01101000.00001011\n" +
"Binary Subnet: 11101100.11110100.01101000.00001011\n" +
"Broadcast Address: 192.168.1.255\n" +
"Prefix: /24\n" +
"Netmask: 255.255.255.0\n" +
"Binary Netmask: 01111111.11111111.11111111.00000000";
String resultOutputText = binaryCalculate.IPCalculate(ip, prefix);
assertEquals(expectedOutputText, resultOutputText);
}

@Test(expected = IllegalArgumentException.class)
public void testIPCalculateWithInvalidIP() {
String ip = "192.168.1.XXX"; // invalid last octet
binaryCalculate.IPCalculate(ip, "24");
}

@Test(expected = IllegalArgumentException.class)
public void testIPCalculateWithInvalidPrefixLength() {
String ip = "192.168.1.1";
String prefix = "25"; // invalid prefix length
binaryCalculate.IPCalculate(ip, prefix);
}
}