package ipac;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11110101";
        String num2 = "11011010";
        String sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("11111111", sum);

        num1 = "00001111";
        num2 = "00110110";
        sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("01001101", sum);

        num1 = "11111111";
        num2 = "11111111";
        sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("111111111", sum);

        num1 = "10000000";
        num2 = "10000000";
        sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("10000001", sum);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11111111";
        String num2 = "11111101";
        String diff = binaryCalculate.subBinary(num1, num2);
        assertEquals("00000110", diff);

        num1 = "00110110";
        num2 = "11111111";
        diff = binaryCalculate.subBinary(num1, num2);
        assertEquals("-11111001_10110100", diff);

        num1 = "11111111";
        num2 = "00000000";
        diff = binaryCalculate.subBinary(num1, num2);
        assertEquals("11111111", diff);

        num1 = "00000000";
        num2 = "11111111";
        diff = binaryCalculate.subBinary(num1, num2);
        assertEquals("-11111111_00000000", diff);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.1.1";
        String IPPrefix = "24";
        String output = binaryCalculate.IPCalculate(IP, IPPrefix);
        assertTrue(output.contains("Total Range: 192.168.1.1 -- 192.168.1.255"));
        assertTrue(output.contains("Usable Range: 192.168.1.2 -- 192.168.1.254"));

        IP = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        IPPrefix = "64";
        output = binaryCalculate.IPCalculate(IP, IPPrefix);
        assertTrue(output.contains("Total Range: FEDC:BA98:85A3:0:0:8A2E:370:7334 -- FEDC:BA98:85A3:FF:FF:FF:FF:FF"));
        assertTrue(output.contains("Usable Range: FEDC:BA98:85A3:1:0:8A2E:370:7333 -- FEDC:BA98:85A3:FF:FF:FF:FF:FF:FF"));

        IP = "123.45.67.89";
        IPPrefix = "22";
        output = binaryCalculate.IPCalculate(IP, IPPrefix);
        assertTrue(output.contains("Total Range: 123.45.67.89 -- 123.45.67.111"));
        assertTrue(output.contains("Usable Range: 123.45.67.90 -- 123.45.67.110"));
    }

    @Test(expected = RuntimeException.class)
    public void testIPCalculateInvalidIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "invalidIP";
        String IPPrefix = "24";
        binaryCalculate.IPCalculate(IP, IPPrefix);
    }

    @Test
    public void testIPCalculateIPv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String IPPrefix = "64";
        String output = binaryCalculate.IPCalculate(IP, IPPrefix);
        assertTrue(output.contains("Total Range: FEDC:BA98:85A3:0:0:8A2E:370:7334 -- FEDC:BA98:85A3:FF:FF:FF:FF:FF"));
        assertTrue(output.contains("Usable Range: FEDC:BA98:85A3:1:0:8A2E:370:7333 -- FEDC:BA98:85A3:FF:FF:FF:FF:FF:FF"));
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String subnet = "11111110";
        String noOfIPAddresses = "00111111";
        binaryCalculate.calculate(subnet, noOfIPAddresses);
        assertEquals("11111111", binaryCalculate.currentIP);
        assertEquals("11111110", binaryCalculate.fullReassembler.substring(binaryCalculate.MAXPREFIX, binaryCalculate.fullReassembler.length()));
        assertEquals("192.168.1.1", binaryCalculate.IPRangeAddress);
        assertEquals("192.168.1.1 -- 192.168.1.255", binaryCalculate.usableRange);
        assertEquals("192.168.1.2", binaryCalculate.getStartIP());

        subnet = "11111111111111111111111111111101";
        noOfIPAddresses = "00000000000000000000000000001111";
        binaryCalculate.calculate(subnet, noOfIPAddresses);
        assertEquals("11111111111111111111111111111111", binaryCalculate.currentIP);
        assertEquals("11111111111111111111111111111101", binaryCalculate.fullReassembler.substring(binaryCalculate.MAXPREFIX, binaryCalculate.fullReassembler.length()));
        assertEquals("2001:0db8:85a3:0:0:8a2e:370:7334 -- 2001:0db8:85a3:ff:ff:ff:ff:ff:ff", binaryCalculate.IPRangeAddress);
        assertEquals("2001:0db8:85a3:1:0:8a2e:370:7333 -- 2001:0db8:85a3:ff:ff:ff:ff:ff:ff:ff", binaryCalculate.usableRange);
        assertEquals("2001:0db8:85a3:0:0:8a2e:370:7334", binaryCalculate.getStartIP());
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.1.1";
        String IPPrefix = "24";
        String endPrefix = "32";
        binaryCalculate.prefixInPrefixCalculate(IP, IPPrefix, 5, endPrefix);

        String[][] output = binaryCalculate.output;
        assertEquals("Usable Range: 192.168.1.2 -- 192.168.1.5", output[0][1]);
        assertEquals("Total usable IP Addresses : 4", output[1][1]);
        assertEquals("Subnet: 192.168.1.0/28", output[2][1]);
        assertEquals("Binary Subnet: 00111111.01111111.01111111.00000110", output[3][1]);
        assertEquals("Broadcast Address: 192.168.1.5", output[4][1]);
        assertEquals("Prefix: /32", output[5][1]);
        assertEquals("Netmask: 255.255.255.255", output[6][1]);
        assertEquals("Binary Netmask: 11111111.11111111.11111111.11111111", output[7][1]);

        IP = "2001:0db8:85a3:0:0:8a2e:370:7334";
        IPPrefix = "64";
        endPrefix = "128";
        binaryCalculate.prefixInPrefixCalculate(IP, IPPrefix, 7, endPrefix);

        output = binaryCalculate.output;
        assertEquals("Usable Range: 2001:0db8:85a3:1:0:8a2e:370:7335 -- 2001:0db8:85a3:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff", output[0][1]);
        assertEquals("Total usable IP Addresses : 65536", output[1][1]);
        assertEquals("Subnet: 2001:0db8:85a3:0:0:8a2e:370:7334/126", output[2][1]);
        assertEquals("Binary Subnet: 00111111.11111111.11111111.11111100", output[3][1]);
        assertEquals("Broadcast Address: 2001:0db8:85a3:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff", output[4][1]);
        assertEquals("Prefix: /128", output[5][1]);
        assertEquals("Netmask: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF", output[6][1]);
        assertEquals("Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111", output[7][1]);
    }
}