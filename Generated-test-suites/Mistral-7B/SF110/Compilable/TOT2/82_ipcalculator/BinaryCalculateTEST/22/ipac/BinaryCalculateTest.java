package ipac;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BinaryCalculateTest {
    private BinaryCalculate binaryCalculate = new BinaryCalculate();

    @Test
    public void testAddBinary() {
        String result = binaryCalculate.addBinary("1111", "1111");
        assertEquals("11111111", result);

        result = binaryCalculate.addBinary("11111111", "10101010");
        assertEquals("1100111101010111", result);

        result = binaryCalculate.addBinary("00001111", "11110000");
        assertEquals("11111111", result);

        result = binaryCalculate.addBinary("10", "1");
        assertEquals("11", result);

        result = binaryCalculate.addBinary("1111", "abcd");
        assertEquals("Error: Invalid input", result);
    }

    @Test
    public void testSubBinary() {
        String result = binaryCalculate.subBinary("1111", "1110");
        assertEquals("0011", result);

        result = binaryCalculate.subBinary("11111111", "10101010");
        assertEquals("11010101", result);

        result = binaryCalculate.subBinary("11111111", "11111111");
        assertEquals("00000000", result);

        result = binaryCalculate.subBinary("10", "11");
        assertEquals("Error: Underflow condition", result);

        result = binaryCalculate.subBinary("1111", "abcd");
        assertEquals("Error: Invalid input", result);
    }

    @Test
    public void testIPCalculate() {
        String result = binaryCalculate.IPCalculate("192.168.1.1", "255.255.255.0");
        assertEquals("Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11111110.11111111.11111111.00000000\nBinary Subnet: 11111110.11111111.11111111.00000000\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 00000001.11111111.11111111.11111111\n", result);

        result = binaryCalculate.IPCalculate("192.168.1.1", "255.255.255.255");
        assertEquals("Error: Invalid prefix length", result);

        result = binaryCalculate.IPCalculate("256.0.0.0", "255.255.255.0");
        assertEquals("Error: Invalid IP address", result);

        result = binaryCalculate.IPCalculate("192.168.1.1", "192.168.2.0");
        assertEquals("Error: IP address and prefix do not match", result);

        result = binaryCalculate.IPCalculate("2001:0db8:85a3:0000:0000:8a2e:0370:7334", "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
        assertEquals("Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff\nUsable Range: Error: Prefix length too large for IPv4\nTotal usable IP Addresses : 0\nSubnet: 0000:0db8:85a3:0000:0000:0000:0000:0000\nBinary Subnet: 00000000.0db885a3.00000000.00000000\nBroadcast Address: ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff\nPrefix: /128\nNetmask: ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff\nBinary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111\n", result);
    }

    @Test(expected = OutOfMemoryError.class)
    public void testPrefixInPrefixCalculate_OutOfMemoryError() {
        binaryCalculate.prefixInPrefixCalculate("192.168.1.1", "255.255.255.0", 1000000, "255.255.255.0");
    }
}