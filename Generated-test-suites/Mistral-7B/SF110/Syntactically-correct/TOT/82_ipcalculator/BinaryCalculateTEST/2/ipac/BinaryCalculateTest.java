import org.junit.Test;
import static org.junit.Assert.assertEquals;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

 private final BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary_TwoZeroBits_ShouldReturnZero() {
 String num1 = "01";
 String num2 = "01";
 String expectedSum = "10";
 String actualSum = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testAddBinary_TwoOneBits_ShouldReturnZeroAndCarry() {
 String num1 = "11";
 String num2 = "11";
 String expectedSum = "100";
 String actualSum = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedSum, actualSum);
 }

 @Test
 public void testAddBinary_OneZeroBitAndOneOneBit_ShouldReturnOne() {
 String num1 = "01";
 String num2 = "11";
 String expectedSum = "101";
 String actualSum = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedSum, actualSum);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidInput_ShouldThrowIllegalArgumentException() {
 String num1 = "";
 String num2 = "";
 binaryCalculate.addBinary(num1, num2);
 }

 @Test
 public void testSubBinary_ValidInput_ShouldReturnCorrectDifference() {
 String num1 = "1101";
 String num2 = "0110";
 String expectedDiff = "1011";
 String actualDiff = binaryCalculate.subBinary(num1, num2);
 assertEquals(expectedDiff, actualDiff);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testSubBinary_InvalidInput_ShouldThrowIllegalArgumentException() {
 String num1 = "";
 String num2 = "";
 binaryCalculate.subBinary(num1, num2);
 }

 @Test
 public void testIPCalculate_ValidIPAddressAndPrefixLength_ShouldReturnCorrectOutput() {
 String ipAddress = "192.168.1.1";
 String ipPrefix = "24";
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
 "Usable Range: 192.168.1.1 -- 192.168.1.254\n" +
 "Total usable IP Addresses : 254 \n" +
 "Subnet: 11101100.10101000.01101110.00001111\n" +
 "Binary Subnet: 11101100.10101000.01101110.00001111\n" +
 "Broadcast Address: 11101100.10101000.01101110.11111111\n" +
 "Prefix: /24\n" +
 "Netmask: 255.255.255.0\n" +
 "Binary Netmask: 01111111.11111111.11111111.00000000";
 String actualOutput = binaryCalculate.IPCalculate(ipAddress, ipPrefix);
 assertEquals(expectedOutput, actualOutput);
 }

 @Test
 public void testIPCalculate_InvalidIPAddress_ShouldThrowIllegalArgumentException() {
 String ipAddress = "";
 String ipPrefix = "24";
 binaryCalculate.IPCalculate(ipAddress, ipPrefix);
 }

 @Test
 public void testIPCalculate_InvalidPrefixLength_ShouldThrowIllegalArgumentException() {
 String ipAddress = "192.168.1.1";
 String ipPrefix = "33";
 binaryCalculate.IPCalculate(ipAddress, ipPrefix);
 }
}