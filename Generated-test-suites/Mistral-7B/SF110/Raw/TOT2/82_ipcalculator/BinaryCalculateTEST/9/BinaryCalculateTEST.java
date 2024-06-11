import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("1011", "1110");
        assertEquals("11110", result);
    }

    @Test
    public void testAddBinaryDifferentLengths() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("1011", "11");
        assertEquals("11010", result);
    }

    @Test
    public void testAddBinaryAllZeroes() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("0000", "0000");
        assertEquals("0000", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinaryEmptyString() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.addBinary("1011", "");
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.subBinary("11110111", "11110100");
        assertEquals("00000111", result);
    }

    @Test
    public void testSubBinarySecondStringLonger() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.subBinary("11110111", "1111011111");
        assertEquals("00000000", result);
    }

    @Test
    public void testSubBinaryAllOnes() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.subBinary("11111111", "11111111");
        assertEquals("00000000", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBinaryEmptyString() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.subBinary("11110111", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBinaryFirstStringEmpty() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.subBinary("", "11110111");
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.IPCalculate("192.168.1.1", "24");
        assertEquals("Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254\nSubnet: 11110110\nBinary Subnet: 11110110\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 01111111.01111111.01111111.00000000", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculateInvalidIPLength() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.IPCalculate("192.168.1.1", "25");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculateNegativePrefixLength() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.IPCalculate("192.168.1.1", "-1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculatePrefixLengthLargerThanMaximum() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.IPCalculate("192.168.1.1", "33");
    }

}