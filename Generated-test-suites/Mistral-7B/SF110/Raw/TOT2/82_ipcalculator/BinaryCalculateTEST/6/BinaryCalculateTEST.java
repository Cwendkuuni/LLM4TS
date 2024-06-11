import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("1111", "1111");
        assertEquals("11111111", result); // Test for all ones

        String result2 = binaryCalculate.addBinary("0000", "0000");
        assertEquals("00000000", result2); // Test for all zeros

        String result3 = binaryCalculate.addBinary("1010", "1111"); // Test for different lengths
        assertEquals("11111010", result3);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.subBinary("1111", "1111");
        assertEquals("0000", result); // Test for all ones

        String result2 = binaryCalculate.subBinary("0000", "0000");
        assertEquals("0000", result2); // Test for all zeros
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String prefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254 \n" +
                "Subnet: 11001100.11101000.01101111.00000001\n" +
                "Binary Subnet: 11111100.11110110.01110111.00000010\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000\n";
        String result = binaryCalculate.IPCalculate(ip, prefix);
        assertEquals(expectedOutput, result);

        String ip2 = "127.0.0.1";
        String prefix2 = "32";
        String expectedOutput2 = "Total Range: 127.0.0.1 -- 127.0.0.1\n" +
                "Usable Range: 127.0.0.1 -- 127.0.0.1\n" +
                "Total usable IP Addresses : 1 \n" +
                "Subnet: 01111111.00000000.00000001.00000001\n" +
                "Binary Subnet: 01111111.00000000.00000001.00000010\n" +
                "Broadcast Address: 127.0.0.1\n" +
                "Prefix: /32\n" +
                "Netmask: 255.255.255.255\n" +
                "Binary Netmask: 11111111.11111111.11111111.11111111\n";
        String result2 = binaryCalculate.IPCalculate(ip2, prefix2);
        assertEquals(expectedOutput2, result2);

        String ip3 = "255.255.255.255";
        String prefix3 = "33";
        String expectedOutput3 = "Your computer does not have sufficient memory to process this query.";
        try {
            binaryCalculate.IPCalculate(ip3, prefix3);
            fail("Expected an OutOfMemoryError");
        } catch (OutOfMemoryError e) {
            // Expected
        }
    }
}