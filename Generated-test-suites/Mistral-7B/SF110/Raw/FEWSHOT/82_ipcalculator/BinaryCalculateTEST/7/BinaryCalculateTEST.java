package ipac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        assertEquals("01101011", BinaryCalculate.addBinary("11101110", "00101101"));
        assertEquals("11111111", BinaryCalculate.addBinary("11111111", "00000000"));
        assertEquals("10110101", BinaryCalculate.addBinary("01101010", "10010101"));
        assertEquals("00000111", BinaryCalculate.addBinary("00000110", "00000011"));
        assertEquals("11111111", BinaryCalculate.addBinary("11111111", "11111111"));
    }

    @Test
    public void testSubBinary() {
        assertEquals("01101100", BinaryCalculate.subBinary("11110100", "10001000"));
        assertEquals("11110101", BinaryCalculate.subBinary("11111111", "00000010"));
        assertEquals("01101011", BinaryCalculate.subBinary("11111111", "10010100"));
        assertNull(BinaryCalculate.subBinary(null, "11111111"));
        assertNull(BinaryCalculate.subBinary("11111111", null));
    }

    @Test
    public void testIPCalculate() {
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String outputText = new BinaryCalculate().IPCalculate(ip, ipPrefix);
        // Add assertions for the different parts of the outputText
        assertEquals("Total Range: 192.168.1.1 -- 192.168.1.255\n", outputText.substring(0, 39));
        assertEquals("Usable Range: 192.168.1.2 -- 192.168.1.254\n", outputText.substring(40, 87));
        assertEquals("Total usable IP Addresses : 254 ", outputText.substring(88, 103));
        // Add more assertions for the rest of the outputText

        ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        ipPrefix = "60";
        outputText = new BinaryCalculate().IPCalculate(ip, ipPrefix);
        // Add assertions for the different parts of the outputText
        assertEquals("Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n", outputText.substring(0, 117));
        assertEquals("Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7335 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7342\n", outputText.substring(118, 193));
        assertEquals("Total usable IP Addresses : 33\n", outputText.substring(194, 209));
        // Add more assertions for the rest of the outputText
    }

    @Test
    public void testCalculate() {
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        new BinaryCalculate().calculate(ip, ipPrefix);
        assertEquals("192.168.1.1", new BinaryCalculate().getStartIP());
        assertEquals("192.168.1.255", new BinaryCalculate().currentIP);
        assertEquals("192.168.1.0/24", new BinaryCalculate().prefix);
        assertEquals("256", Integer.toString(new BinaryCalculate().totalIPAddresses.intValue()));
    }

    @Test
    public void testConvert() {
        assertEquals("11110110", BinaryCalculate.convert("190"));
        assertEquals("0111110101101100110011011101110111101111", BinaryCalculate.convert("2001:0db8:85a3:0000:0000:8a2e:0370:7334"));
        assertEquals("11111111", BinaryCalculate.convert("255.255.255.255"));
        assertNull(BinaryCalculate.convert(null));
    }

    @Test
    public void testConvertToBinary() {
        assertEquals("01101110", BinaryCalculate.convertToBinary("11101110"));
        assertEquals("11111111", BinaryCalculate.convertToBinary("255.255.255.255"));
        assertEquals("10101010", BinaryCalculate.convertToBinary("154"));
        assertNull(BinaryCalculate.convertToBinary(null));
    }

    @Test
    public void testGetBinaryIP() {
        assertEquals("01101110.01101110.11101110.00001100", BinaryCalculate.getBinaryIP("192.168.110.128"));
        assertEquals("01111111.01111111.11111111.11111111.11111111.11111111.11111111.11111101", BinaryCalculate.getBinaryIP("2001:0db8:85a3:0000:0000:8a2e:0370:7334"));
        assertNull(BinaryCalculate.getBinaryIP(null));
    }

    @Test
    public void testGetNetmask() {
        assertEquals("11111100.01111100.01111100.01111100", BinaryCalculate.getNetmask("224.0.0.224"));
        assertEquals("11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111", BinaryCalculate.getNetmask("255.255.255.255"));
        assertNull(BinaryCalculate.getNetmask(null));
    }
}