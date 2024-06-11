import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BinaryCalculateTest {

    private BinaryCalculate binaryCalculate = new BinaryCalculate();

    @Test
    public void testAddBinary() {
        String num1 = "1111";
        String num2 = "1111";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals("11111111", result);

        num1 = "00101101";
        num2 = "11001001";
        result = binaryCalculate.addBinary(num1, num2);
        assertEquals("10010110", result);

        num1_long = "11111111111111111111111111111111";
        num2_long = "11111111111111111111111111111110";
        result = binaryCalculate.addBinary(num1_long, num2_long);
        assertEquals("11111111111111111111111111111111", result);
    }

    @Test
    public void testSubBinary() {
        String num1 = "1111";
        String num2 = "1010";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals("0110", result);

        num1 = "1010";
        num2 = "1111";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals("0001", result);

        num1 = "11111111";
        num2 = "11111110";
        result = binaryCalculate.subBinary(num1, num2);
        assertEquals("00000011", result);
    }

    @Test
    public void testIPCalculate() {
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254 \n" +
                "Subnet: 11011010.01111100.01110100.00001001\n" +
                "Binary Subnet: 11111100.01111011.01110100.00001001\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111100.00000000.00000000.00000000";
        String result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, result);

        ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        ipPrefix = "64";
        expectedOutput = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFFFFFF:FFF:FFF:FFF:FFF:FFF:FFF:FFF\n" +
                "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7333 -- FFFFFFFF:FFF:FFF:FFF:FFF:FFF:FFF:FFF:FFF:FFF:FFF:FFF:FFF:FFF:FFF:FFF\n" +
                "Total usable IP Addresses : 65535 \n" +
                "Subnet: 01111110.11110111.10101011.00000000.00001110.11111101.00110111.11111010\n" +
                "Binary Subnet: 01111110.01111101.10101011.00000000.00001110.11111101.00110111.11110100\n" +
                "Broadcast Address: FFFFFFFF.FFF.FFF.FFF.FFFFFFF.FFFF.FFF.FFF\n" +
                "Prefix: /64\n" +
                "Netmask: FFFFFFFF.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0\n" +
                "Binary Netmask: 00000000.00000000.00000000.00000000.00000000.00000000.00000000.00000000.00000000.00000000.00000000.00000000.00000000.00000000.00000000.00000000.00000000.00000000.00000000.00000000.00000000";
        result = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, result);

        // Test for incorrect IP or IPPrefix length
        ip = "192.168.";
        ipPrefix = "24";
        try {
            binaryCalculate.IPCalculate(ip, ipPrefix);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        ipPrefix = "33";
        try {
            binaryCalculate.IPCalculate(ip, ipPrefix);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }

        // Test for incorrect IP format
        ip = "192.168.1.1.1";
        try {
            binaryCalculate.IPCalculate(ip, ipPrefix);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
}