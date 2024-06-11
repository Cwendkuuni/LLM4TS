package ipac;
import org.junit.Test;
import static org.junit.Assert.*;
import ipac.BinaryCalculate;

public class BinaryCalculateTest {

 private final static String VALID_IPV4_IP = "192.168.1.1";
 private final static String VALID_IPV4_IP_PREFIX = "24";
 private final static String VALID_IPV6_IP = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
 private final static String VALID_IPV6_IP_PREFIX = "64";
 private final static String INVALID_IPV4_IP = "123.123.123.123.123.123.123.123";
 private final static String INVALID_IPV6_IP = "2001:0db8:85a3:0000:0000:8a2e:0370:7334:abcd";
 private final static String INVALID_IP_PREFIX = "33";
 private final static String INVALID_LENGTH_IP_PREFIX = "127.127.127.127.127.127.127.127.127.127.127.127.127.127.127.127.127.127.127.127.127";
 private final static int NO_PREFIX = 5;
 private final static String END_PREFIX = "32";

 @Test
 public void addBinaryTest() {
 String ip1 = "11110110";
 String ip2 = "11111011";
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String sum = binaryCalculate.addBinary(ip1, ip2);
 assertEquals("11111111", sum);
 }

 @Test(expected = IllegalArgumentException.class)
 public void addBinaryInvalidInputTest() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 binaryCalculate.addBinary("", "");
 }

 @Test
 public void subBinaryTest() {
 String ip1 = "11110110";
 String ip2 = "11111011";
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String diff = binaryCalculate.subBinary(ip1, ip2);
 assertEquals("00001101", diff);
 }

 @Test(expected = IllegalArgumentException.class)
 public void subBinaryInvalidInputTest() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 binaryCalculate.subBinary("", "");
 }

 @Test
 public void IPCalculateValidIPv4Test() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip = VALID_IPV4_IP;
 String ipPrefix = VALID_IPV4_IP_PREFIX;
 binaryCalculate.IPCalculate(ip, ipPrefix);
 // Check output
 }

 @Test
 public void IPCalculateValidIPv6Test() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip = VALID_IPV6_IP;
 String ipPrefix = VALID_IPV6_IP_PREFIX;
 binaryCalculate.IPCalculate(ip, ipPrefix);
 // Check output
 }

 @Test(expected = IllegalArgumentException.class)
 public void IPCalculateInvalidIPv4Test() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip = INVALID_IPV4_IP;
 String ipPrefix = VALID_IPV4_IP_PREFIX;
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test(expected = IllegalArgumentException.class)
 public void IPCalculateInvalidIPv6Test() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip = INVALID_IPV6_IP;
 String ipPrefix = VALID_IPV6_IP_PREFIX;
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test
 public void IPCalculateInvalidIPPrefixTest() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip = VALID_IPV4_IP;
 String ipPrefix = INVALID_IP_PREFIX;
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test
 public void IPCalculateInvalidLengthIPPrefixTest() {
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 String ip = VALID_IPV4_IP;
 String ipPrefix = INVALID_LENGTH_IP_PREFIX;
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test
 public void prefixInPrefixCalculateTest() {
 String ip = VALID_IPV4_IP;
 String ipPrefix = VALID_IPV4_IP_PREFIX;
 int noPrefix = NO_PREFIX;
 String endPrefix = END_PREFIX;
 BinaryCalculate binaryCalculate = new BinaryCalculate();
 binaryCalculate.prefixInPrefixCalculate(ip, ipPrefix, noPrefix, endPrefix);
 // Check output
 }

}