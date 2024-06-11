import org.junit.Test;
import ipac.BinaryCalculate;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary_TwoZeroBits_ReturnsCorrectSum() {
 String num1 = "00101110";
 String num2 = "01100110";
 String expectedSum = "10011100";
 String resultSum = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedSum, resultSum);
 }

 @Test
 public void testAddBinary_CarryPropagation_ReturnsCorrectSum() {
 String num1 = "11111111";
 String num2 = "11111111";
 String expectedSum = "100000001";
 String resultSum = binaryCalculate.addBinary(num1, num2);
 assertEquals(expectedSum, resultSum);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidInput_ThrowsException() {
 String num1 = "abcd";
 String num2 = "efgh";
 binaryCalculate.addBinary(num1, num2);
 fail("Expected IllegalArgumentException to be thrown");
 }

 @Test
 public void testSubBinary_ValidInput_ReturnsCorrectDifference() {
 String num1 = "11110110";
 String num2 = "11101101";
 String expectedDiff = "00011011";
 String resultDiff = binaryCalculate.subBinary(num1, num2);
 assertEquals(expectedDiff, resultDiff);
 }

 @Test
 public void testSubBinary_NegativeNumber_ConvertsToTwoComplementForm() {
 String num1 = "00111111";
 String expectedTwoComplement = "11000000";
 String num2 = binaryCalculate.addBinary(expectedTwoComplement, "1");
 String resultTwoComplement = binaryCalculate.subBinary(num1, num2);
 assertEquals(expectedTwoComplement, resultTwoComplement);
 }

 @Test
 public void testIPCalculate_ValidIPAddressAndPrefix_ReturnsCorrectOutput() {
 String ip = "192.168.1.1";
 String prefix = "24";
 String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
 "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
 "Total usable IP Addresses : 254\n" +
 "Subnet: 11101100.11110100.01101000.00001111\n" +
 "Binary Subnet: 11101100.11110100.01101000.00001111\n" +
 "Broadcast Address: 192.168.1.255\n" +
 "Prefix: /24\n" +
 "Netmask: 255.255.255.0\n" +
 "Binary Netmask: 01111111.11111111.11111111.00000000";
 String resultOutput = binaryCalculate.IPCalculate(ip, prefix);
 assertEquals(expectedOutput, resultOutput);
 }

 @Test
 public void testIPCalculate_InvalidIPAddressAndPrefix_ThrowsException() {
 String ip = "192.168.1.256";
 String prefix = "24";
 binaryCalculate.IPCalculate(ip, prefix);
 fail("Expected IllegalArgumentException to be thrown");
 }

 @Test
 public void testIPCalculate_IPv6AddressAndPrefix_ReturnsCorrectOutput() {
 String ip = "2001:0db8:85a3:0000:0270:12f0:0000:0001";
 String prefix = "60";
 String expectedOutput = "Total Range: 2001:0db8:85a3:0000:0270:12f0:0000:0001 -- FFFF:FFF:FFF:FFF:FFF:FFF:FFF:FFFF\n" +
 "Usable Range: 2001:0db8:85a3:0000:0270:12f0:0000:0002 -- FFFE:FFF:FFF:FFF:FFF:FFF:FFF:FFFF\n" +
 "Total usable IP Addresses : 65534\n" +
 "Subnet: 01111111.01111111.10101011.00000000.00101111.10110100.00000000.00000001\n" +
 "Binary Subnet: 01111111.01111111.10101011.00000000.00101111.10110100.00000000.00000001\n" +
 "Broadcast Address: FFFF:FFF:FFF:FFF:FFF:FFF:FFF:FFFF\n" +
 "Prefix: /60\n" +
 "Netmask: FFFF:FFFF:FFFF:0000:0000:0000:0000:0000\n" +
 "Binary Netmask: 00000000.00000000.00000000.FFFFFFFF.00000000.00000000.00000000.00000000";
 String resultOutput = binaryCalculate.IPCalculate(ip, prefix);
 assertEquals(expectedOutput, resultOutput);
 }
}