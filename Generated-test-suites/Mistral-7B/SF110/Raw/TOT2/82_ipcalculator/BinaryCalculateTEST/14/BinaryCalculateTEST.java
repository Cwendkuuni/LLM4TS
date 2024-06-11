import org.junit.Assert;
import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();

        // Test basic functionality
        assertEquals(bc.addBinary("11", "11"), "111");

        // Test edge cases
        assertEquals(bc.addBinary("0000", "0000"), "0000");
        assertEquals(bc.addBinary("1111", "1111"), "11111111");

        // Test longer strings
        assertEquals(bc.addBinary("11111111111111111111111111111111", "11111111111111111111111111111111"), "1111111111111111111111111111111111111111111111111111111111111111");

        // Test exception scenario
        assertThrows(IllegalArgumentException.class, () -> bc.addBinary("1111", "1110"));
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();

        // Test basic functionality
        assertEquals(bc.subBinary("1111", "1110"), "0111");

        // Test edge cases
        assertEquals(bc.subBinary("0000", "0000"), "0000");
        assertEquals(bc.subBinary("1111", "1111"), "0000");

        // Test longer strings
        assertEquals(bc.subBinary("11111111111111111111111111111111", "11111111111111111111111111111110"), "00000000000000000000000000000111");

        // Test exception scenario
        assertThrows(IllegalArgumentException.class, () -> bc.subBinary("1111", "11111"));
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();

        // Test basic functionality
        assertEquals(bc.IPCalculate("192.168.0.1", "24"), "Total Range: 192.168.0.1 -- 192.168.0.1\nUsable Range: 192.168.0.1 -- 192.168.0.1\nTotal usable IP Addresses : 1 \nSubnet: 11101100.00111111.00000000.00000001\nBinary Subnet: 111011000001111110000000010000001\nBroadcast Address: 192.168.0.1\nPrefix: /24");

        // Test edge cases
        assertEquals(bc.IPCalculate("255.255.255.255", "32"), "Total Range: 255.255.255.255 -- 255.255.255.255\nUsable Range: 0.0.0.0 -- 0.0.0.0\nTotal usable IP Addresses : 0 \nSubnet: 11111111.11111111.11111111.11111111\nBinary Subnet: 11111111111111111111111111111111\nBroadcast Address: 255.255.255.255\nPrefix: /32");

        // Test longer prefix lengths
        assertEquals(bc.IPCalculate("123.45.67.89", "48"), "Total Range: 123.45.67.89 -- 123.45.67.89\nUsable Range: 123.45.67.89 -- 123.45.67.89\nTotal usable IP Addresses : 1 \nSubnet: 01111101.10101101.01101111.10001100\nBinary Subnet: 011111010101011010111111001001100\nBroadcast Address: 123.45.67.89\nPrefix: /48");

        // Test exception scenarios
        assertThrows(IllegalArgumentException.class, () -> bc.IPCalculate("123.45.67.89", "1"));
        assertThrows(IllegalArgumentException.class, () -> bc.IPCalculate("256.255.255.255", "32"));
    }

}