package ipac;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BinaryCalculateTest {
    private static final String DELIMITER = ":";
    private static final int MAX_PREFIX = 128;
    private static final String EMPTY_IP = "";
    private static final String IPV4_IP = "192.168.1.1";
    private static final String IPV6_IP = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
    private static final String IPV4_IP_PREFIX = "255.255.255.0";
    private static final String IPV6_IP_PREFIX = "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:c000";
    private static final String IPV4_IP_SUFFIX = "0.0.0.1";
    private static final String IPV6_IP_SUFFIX = "::1";
    private static final int NO_PREFIX = 5;
    private static final String END_PREFIX = "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:c000";

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("01111111", "11111111");
        assertEquals(result, "111111111111");

        result = binaryCalculate.addBinary("10101010", "11111111");
        assertEquals(result, "111111101010");

        result = binaryCalculate.addBinary("11111111", "11111111");
        assertEquals(result, "111111111111");

        result = binaryCalculate.addBinary(EMPTY_IP, IPV4_IP);
        assertNull(result);

        result = binaryCalculate.addBinary(IPV4_IP, EMPTY_IP);
        assertNull(result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.subBinary("11111111", "01111111");
        assertEquals(result, "10000000");

        result = binaryCalculate.subBinary("00001111", "11110000");
        assertEquals(result, "11110111");

        result = binaryCalculate.subBinary("11111111", "11111111");
        assertNull(result);

        result = binaryCalculate.subBinary(EMPTY_IP, IPV4_IP);
        assertNull(result);

        result = binaryCalculate.subBinary(IPV4_IP, EMPTY_IP);
        assertNull(result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.IPCalculate(IPV4_IP, IPV4_IP_PREFIX);
        assertEquals(result, "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                             "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                             "Total usable IP Addresses : 254 \n" +
                             "Subnet: 192.168.1.0\n" +
                             "Binary Subnet: 01101110.01101111.01101111.00000000\n" +
                             "Broadcast Address: 192.168.1.255\n" +
                             "Prefix: /24\n" +
                             "Netmask: 255.255.255.0\n" +
                             "Binary Netmask: 00111111.11111111.11111111.00000000");

        result = binaryCalculate.IPCalculate(IPV6_IP, IPV6_IP_PREFIX);
        assertEquals(result, "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFC\n" +
                             "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7333 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFBr\n" +
                             "Total usable IP Addresses : 255 \n" +
                             "Subnet: 2001:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                             "Binary Subnet: 2001:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                             "Broadcast Address: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFC\n" +
                             "Prefix: /64\n" +
                             "Netmask: ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:c000\n" +
                             "Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11110000");

        result = binaryCalculate.IPCalculate(IPV4_IP, IPV4_IP_PREFIX + IPV4_IP_SUFFIX);
        assertEquals(result, "Total Range: 192.168.1.1 -- 192.168.1.2\n" +
                             "Usable Range: 192.168.1.1 -- 192.168.1.1\n" +
                             "Total usable IP Addresses : 1 \n" +
                             "Subnet: 192.168.1.1\n" +
                             "Binary Subnet: 01101110.01101111.01101111.00000101\n" +
                             "Broadcast Address: 192.168.1.1\n" +
                             "Prefix: /32\n" +
                             "Netmask: 255.255.255.255\n" +
                             "Binary Netmask: 11111111.11111111.11111111.11111111");

        result = binaryCalculate.IPCalculate(IPV6_IP, IPV6_IP_PREFIX + IPV6_IP_SUFFIX);
        assertEquals(result, "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7335\n" +
                             "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                             "Total usable IP Addresses : 1 \n" +
                             "Subnet: 2001:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                             "Binary Subnet: 2001:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                             "Broadcast Address: 2001:0db8:85a3:0000:0000:8a2e:0370:7335\n" +
                             "Prefix: /128\n" +
                             "Netmask: 0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0:0");
    }
}