package ipac;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        assertEquals("01101101", binaryCalculate.addBinary("11011010", "00110111"));
        assertEquals("01111111", binaryCalculate.addBinary("11111110", "00000011"));
        assertEquals("10000001", binaryCalculate.addBinary("01111111", "10000000"));
        assertEquals("11111111", binaryCalculate.addBinary("11111111", "00000000"));
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        assertEquals("01101101", binaryCalculate.subBinary("11111111", "00010010"));
        assertEquals("11110111", binaryCalculate.subBinary("11111111", "00000100"));
        assertEquals("01111101", binaryCalculate.subBinary("11111111", "00011110"));
        assertNull(binaryCalculate.subBinary(null, ""));
    }

    // Add more test cases for IPCalculate, getBinaryIP, getNetmask, calculate and other methods here

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        // Provide valid IP addresses and IP prefixes for testing the IPCalculate method
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                               "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                               "Total usable IP Addresses : 254 \n" +
                               "Subnet: 192.168.1.0\n" +
                               "Binary Subnet: 11101100.01111111.01101100.00111111\n" +
                               "Broadcast Address: 192.168.1.255\n" +
                               "Prefix: /24\n" +
                               "Netmask: 255.255.255.0\n" +
                               "Binary Netmask: 11111111.11111111.11111111.00000000";

        String ip6 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String ipPrefix6 = "60";
        String expectedOutput6 = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                                "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7326 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7333\n" +
                                "Total usable IP Addresses : 62\n" +
                                "Subnet: 2001:0db8:85a3:0000:0000:8a2e:0370:7300\n" +
                                "Binary Subnet: 2001:0db8:85a3:0000:0000:8a2e:0370:7300\n" +
                                "Broadcast Address: 2001:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                                "Prefix: /60\n" +
                                "Netmask: 2001:0db8:85a3:0000:0000:ff00:0000:ffff\n" +
                                "Binary Netmask: 2001:0db8:85a3:0000:0000:ffff:0000:0000";

        String ipInvalid = "256.0.0.0";
        String ipPrefixInvalid = "33";

        binaryCalculate.IPCalculate(ip, ipPrefix);
        String actualOutput = binaryCalculate.fullReassembler;
        assertEquals(expectedOutput, actualOutput);

        binaryCalculate.IPCalculate(ip6, ipPrefix6);
        actualOutput = binaryCalculate.fullReassembler;
        assertEquals(expectedOutput6, actualOutput);

        binaryCalculate.IPCalculate(ipInvalid, ipPrefixInvalid);
        assertNull(binaryCalculate.fullReassembler);
    }
}