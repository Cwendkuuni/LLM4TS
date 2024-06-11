import org.junit.Test;
import static org.junit.Assert.*;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

 private final static String IP_V4_VALID = "192.168.1.1";
 private final static String IP_V4_INVALID_LENGTH = "192.168.1.";
 private final static String IP_V4_INVALID_FORMAT = "256.255.255.255";
 private final static String IP_V6_VALID = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 private final static String IP_V6_INVALID_LENGTH = "2001:0db8:85a3:0000:0000:8a2e:";
 private final static String IP_V6_INVALID_FORMAT = ":::0:0:0:0:0:1";
 private final static String IP_PREFIX_VALID_V4 = "24";
 private final static String IP_PREFIX_VALID_V6 = "64";
 private final static String IP_PREFIX_INVALID_LENGTH_V4 = "33";
 private final static String IP_PREFIX_INVALID_FORMAT_V4 = "56";
 private final static String IP_PREFIX_INVALID_LENGTH_V6 = "129";
 private final static String IP_PREFIX_INVALID_FORMAT_V6 = "128:";

 @Test
 public void testAddBinary_ValidIPv4() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip1 = "11110110.11111111.01101100.01101100";
 String ip2 = "11111011.00101100.11001110.01101100";
 String expectedResult = "11111111.11111111.01111100.01111100";
 String result = binaryCalculate.addBinary(ip1, ip2);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidIPv4Format() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip1 = IP_V4_INVALID_FORMAT;
 String ip2 = "00000000.00000000.00000000.00000001";
 binaryCalculate.addBinary(ip1, ip2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidIPv4Length() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip1 = IP_V4_INVALID_LENGTH;
 String ip2 = "00111111.11111111.11111111.11111111";
 binaryCalculate.addBinary(ip1, ip2);
 }

 @Test
 public void testAddBinary_ValidIPv6() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip1 = "11110110:1111:1111:1111:1111:1111:1111:1111";
 String ip2 = "0111:1111:1111:1111:1111:1111:1111:1111";
 String expectedResult = "11111111:1111:1111:1111:1111:1111:1111:1111";
 String result = binaryCalculate.addBinary(ip1, ip2);
 assertEquals(expectedResult, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidIPv6Length() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip1 = IP_V6_INVALID_LENGTH;
 String ip2 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 binaryCalculate.addBinary(ip1, ip2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddBinary_InvalidIPv6Format() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip1 = IP_V6_INVALID_FORMAT;
 String ip2 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 binaryCalculate.addBinary(ip1, ip2);
 }

 @Test
 public void testIPCalculate_ValidIPv4() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip = IP_V4_VALID;
 String ipPrefix = "24";
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIPv4Format() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip = IP_V4_INVALID_FORMAT;
 String ipPrefix = IP_PREFIX_VALID_V4;
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIPv4Length() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip = IP_V4_INVALID_LENGTH;
 String ipPrefix = IP_PREFIX_VALID_V4;
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test
 public void testIPCalculate_ValidIPv6() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip = IP_V6_VALID;
 String ipPrefix = IP_PREFIX_VALID_V6;
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIPv6Format() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip = IP_V6_INVALID_FORMAT;
 String ipPrefix = IP_PREFIX_VALID_V6;
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIPv6Length() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip = IP_V6_INVALID_LENGTH;
 String ipPrefix = IP_PREFIX_INVALID_LENGTH_V6;
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_InvalidIPv6FormatLength() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip = IP_V6_VALID;
 String ipPrefix = IP_PREFIX_INVALID_FORMAT_V6;
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }
}