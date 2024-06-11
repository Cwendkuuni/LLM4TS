package ipac;

import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary_TwoZeroBinaryNumbers() {
        BinaryCalculate bc = new BinaryCalculate();
        String result = bc.addBinary("01111111", "01111111");
        String expected = "11111111";
        assertEquals(expected, result);
    }

    @Test
    public void testAddBinary_Carry() {
        BinaryCalculate bc = new BinaryCalculate();
        String result = bc.addBinary("1111", "1111");
        String expected = "10000";
        assertEquals(expected, result);
    }

    @Test
    public void testSubBinary_TwoZeroBinaryNumbers() {
        BinaryCalculate bc = new BinaryCalculate();
        String result = bc.subBinary("01111111", "01111111");
        String expected = "0";
        assertEquals(expected, result);
    }

    @Test(expected = RuntimeException.class)
    public void testSubBinary_InvalidInput() {
        BinaryCalculate bc = new BinaryCalculate();
        String result = bc.subBinary("1111", "1");
    }

    @Test
    public void testIPCalculate_ValidIPv4Address() {
        BinaryCalculate bc = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String result = bc.IPCalculate(ip, ipPrefix);
        String expected = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 252\n" +
                "Subnet: 11001100.01010100.00001001.00000001\n" +
                "Binary Subnet: 11001100.01010100.00001001.00000010\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000\n";
        assertEquals(expected, result);
    }

    @Test
    public void testIPCalculate_ValidIPv6Address() {
        BinaryCalculate bc = new BinaryCalculate();
        String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String ipPrefix = "60";
        String result = bc.IPCalculate(ip, ipPrefix);
        String expected = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7328 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7332\n" +
                "Total usable IP Addresses : 15\n" +
                "Subnet: 01111010.11011011.01010101.00000000.00001110.11101110.00110111.11110110\n" +
                "Binary Subnet: 01111010.11011011.01010101.00000000.00001110.11101110.00110111.11111000\n" +
                "Broadcast Address: 2001:0db8:85a3:0000:0000:8a2e:0370:7335\n" +
                "Prefix: /60\n" +
                "Netmask: ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:fc00:ffff:ffff\n" +
                "Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111100\n";
        assertEquals(expected, result);
    }

    @Test(expected = RuntimeException.class)
    public void testIPCalculate_InvalidInput() {
        BinaryCalculate bc = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "65";
    }
}