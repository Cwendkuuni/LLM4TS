package ipac.test;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import ipac.BinaryCalculate;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "0110";
        String sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("11110110", sum);

        num1 = "11111111";
        num2 = "11111111";
        sum = binaryCalculate.addBinary(num1, num2);
        assertEquals("111111111111", sum);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "0110";
        String diff = binaryCalculate.subBinary(num1, num2);
        assertEquals("1101", diff);

        num1 = "00111111";
        num2 = "01111110";
        diff = binaryCalculate.subBinary(num1, num2);
        assertEquals("11000001", diff);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.1.1";
        String IPPrefix = "255.255.255.0";
        String outputText = binaryCalculate.IPCalculate(IP, IPPrefix);
        assertTrue(outputText.contains("Total Range: 192.168.1.1 -- 192.168.1.255"));
        assertTrue(outputText.contains("Usable Range: 192.168.1.2 -- 192.168.1.254"));

        IP = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        IPPrefix = "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff:c000";
        outputText = binaryCalculate.IPCalculate(IP, IPPrefix);
        assertTrue(outputText.contains("Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFFFFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF"));
        assertTrue(outputText.contains("Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7333 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7334"));
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = binaryCalculate.getBinaryIP("192.168.1.1");
        assertEquals("11100110.11010100.01101110.00001111", binaryIP);

        binaryIP = binaryCalculate.getBinaryIP("2001:0db8:85a3:0000:0000:8a2e:0370:7334");
        assertEquals("20010db885a30000000008a2e03707334", binaryIP);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = "192.168.1.1";
        String IPPrefix = "255.255.255.0";
        String netmask = binaryCalculate.getNetmask(IPPrefix);
        assertEquals("11111111.11111111.11111111.00000000", netmask);

        binaryIP = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        IPPrefix = "ffff:ffff:ffff:ffff:ffff:ffff:ffff:c000";
        netmask = binaryCalculate.getNetmask(IPPrefix);
        assertEquals("fffffffffffffffffffffffffffffffffffc00000000", netmask);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.1.1";
        String IPPrefix = "255.255.255.0";
        binaryCalculate.calculate(IP, IPPrefix);
        assertEquals("192.168.1.1", binaryCalculate.IPRangeAddress);
        assertEquals("192.168.1.255", binaryCalculate.usableRange);
        assertEquals(new BigInteger("256"), binaryCalculate.totalIPAddresses);

        IP = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        IPPrefix = "ffff:ffff:ffff:ffff:ffff:ffff:ffff:c000";
        binaryCalculate = new BinaryCalculate();
        binaryCalculate.calculate(IP, IPPrefix);
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7334", binaryCalculate.currentIP);
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7340", binaryCalculate.IPRangeAddress);
        assertEquals("2001:0db8:85a3:0000:0000:8a2e:0370:7333", binaryCalculate.usableRange);
        assertEquals(new BigInteger("65534"), binaryCalculate.totalIPAddresses);
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.1.1";
        String IPPrefix = "255.255.255.0";
        int noPrefix = 2;
        String endPrefix = "255.255.255.252";
        binaryCalculate.prefixInPrefixCalculate(IP, IPPrefix, noPrefix, endPrefix);
        assertTrue(binaryCalculate.output.get(noPrefix)[0].contains("192.168.1.1"));
        assertTrue(binaryCalculate.output.get(noPrefix)[1].contains("192.168.1.1 -- 192.168.1.4"));
        assertTrue(binaryCalculate.output.get(noPrefix + 1)[0].contains("Total usable IP Addresses :"));
        assertTrue(binaryCalculate.output.get(noPrefix + 3)[1].contains("11100110.11010100.01101110.00111100"));
        assertTrue(binaryCalculate.output.get(noPrefix + 4)[1].contains("11100110.11010100.01101110.00111101"));
        assertTrue(binaryCalculate.output.get(noPrefix + 5)[1].contains("11111111.11111111.11111111.11110000"));
        assertTrue(binaryCalculate.output.get(noPrefix + 6)[1].contains("11111111.11111111.11111111.00001111"));
        assertTrue(binaryCalculate.output.get(noPrefix + 7)[1].contains("11111111.11111111.11111111.00000111"));
    }

    @Test
    public void testConvert() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = binaryCalculate.convert("192.168.1.1");
        assertEquals("11100110.11010100.01101110.00001111", binaryIP);

        binaryIP = binaryCalculate.convert("2001:0db8:85a3:0000:0000:8a2e:0370:7334");
        assertEquals("20010db885a30000000008a2e03707334", binaryIP);
    }

    @Test
    public void testConvertToBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String binaryIP = binaryCalculate.convertToBinary("192.168.1.1");
        assertEquals("11100110.11010100.01101110.00001111", binaryIP);

        binaryIP = binaryCalculate.convertToBinary("2001:0db8:85a3:0000:0000:8a2e:0370:7334");
        assertEquals("20010db885a30000000008a2e03707334", binaryIP);
    }
}