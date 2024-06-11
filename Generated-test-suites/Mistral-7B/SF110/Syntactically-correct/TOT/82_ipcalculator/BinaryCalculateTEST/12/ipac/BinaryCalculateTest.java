import org.junit.Test;
import ipac.BinaryCalculate;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

 private BinaryCalculate binaryCalculate = new BinaryCalculate();

 @Test
 public void testAddBinary() {
 String num1 = "1111";
 String num2 = "1010";
 String expected = "11001";
 String result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);

 num1 = "1111";
 num2 = "1111";
 expected = "11110";
 result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);

 num1 = "0000";
 num2 = "1111";
 expected = "1111";
 result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);

 num1 = "1111";
 num2 = "0000";
 expected = "1111";
 result = binaryCalculate.addBinary(num1, num2);
 assertEquals(expected, result);

 num1 = "1111";
 num2 = "1111";
 expected = "11111"; // carry
 result = binaryCalculate.addBinary(num1, num2);
 assertTrue(expected.startsWith(result.substring(0, 7)));
 assertEquals("1", result.substring(7, 8));
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
 assertFalse(expected.equals(result));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_invalidIP() {
 String ip = "256.0.0.0";
 String ipPrefix = "24";
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIPCalculate_invalidIPPrefix() {
 String ip = "192.168.0.0";
 String ipPrefix = "33";
 binaryCalculate.IPCalculate(ip, ipPrefix);
 }

 @Test
 public void testIPCalculate_ipv4() {
 String ip = "192.168.1.1";
 String ipPrefix = "16";
 String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
 "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
 "Total usable IP Addresses : 254\n" +
 "Subnet: 11110110.11110000.01101101.00000001\n" +
 "Binary Subnet: 11110110.11110000.01101101.00000001\n" +
 "Broadcast Address: 11110110.11110000.01101101.11111111\n" +
 "Prefix: /14\n" +
 "Netmask: 255.255.252.0\n" +
 "Binary Netmask: 01111111.11111111.11111100.00000000\n";
 String resultOutputText = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertEquals(expectedOutputText, resultOutputText);
 }

 @Test
 public void testIPCalculate_ipv6() {
 String ip = "2001:0db8:001e:8500:0010:ff00:0042:8329";
 String ipPrefix = "60";
 String expectedOutputText = "Total Range: 2001:0db8:001e:8500:0010:ff00:0042:8329 -- FFFFFFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
 "Usable Range: 2001:0db8:001e:8500:0010:ff00:0042:8328 -- 2001:0db8:001e:8500:0010:ff00:0042:8337\n" +
 "Total usable IP Addresses : 65504\n" +
 "Subnet: 01111110.01111111.11011111.01010111.00101111.11111100.00101100.01101011\n" +
 "Binary Subnet: 01111110.01111111.11011111.01010111.00101111.11111100.00101100.01101011\n" +
 "Broadcast Address: FFFFFFFF.FFFF.FFFF.FFFF.FFFF.FFFF.FFFF.FFFF\n" +
 "Prefix: /60\n" +
 "Netmask: FFFFFFFC.00000000.00000000.00000000.00000000.00000000.00000000.00001111\n" +
 "Binary Netmask: 01111111.11111111.11111111.11111111.11111111.11111111.11111100.00000111\n";
 String resultOutputText = binaryCalculate.IPCalculate(ip, ipPrefix);
 assertEquals(expectedOutputText, resultOutputText);
 }
}