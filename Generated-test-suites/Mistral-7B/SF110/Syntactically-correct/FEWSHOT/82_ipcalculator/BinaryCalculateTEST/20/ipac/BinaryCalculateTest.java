package ipac.test;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

import ipac.BinaryCalculate;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        assertEquals("01111111", binaryCalculate.addBinary("1111", "1111"));
        assertEquals("10101010", binaryCalculate.addBinary("10101010", "0111"));
        assertEquals("11111111", binaryCalculate.addBinary("11111111", "0000"));
        assertEquals("00000101", binaryCalculate.addBinary("00000000", "00000101"));
        assertEquals("1111111111111111", binaryCalculate.addBinary("11111111", "11111111"));
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        assertEquals("01101101", binaryCalculate.subBinary("11010101", "10010010"));
        assertEquals("11111111", binaryCalculate.subBinary("11111111", "00000000"));
        assertEquals("01110100", binaryCalculate.subBinary("11110101", "10000100"));
        assertEquals("00000000", binaryCalculate.subBinary("00000000", "11111111"));
        assertEquals("11111111", binaryCalculate.subBinary("11111111", "00000000"));
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        binaryCalculate.IPCalculate("192.168.1.1", "255.255.255.0");
        binaryCalculate.IPCalculate("203.0.113.128", "255.255.255.240");
        binaryCalculate.IPCalculate("255.255.255.255", "255.255.255.255");
        binaryCalculate.IPCalculate("255.255.255.255", "255.255.255.0");
        binaryCalculate.IPCalculate("2001:0db8:85a3:0000:0000:8a2e:0370:7334", "ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff");
    }

    @Test
    public void testPrefixInPrefixCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        binaryCalculate.prefixInPrefixCalculate("192.168.1.1", "255.255.255.0", 5, "255.255.255.224");
        binaryCalculate.prefixInPrefixCalculate("203.0.113.128", "255.255.255.240", 10, "255.255.255.192");
        binaryCalculate.prefixInPrefixCalculate("255.255.255.255", "255.255.255.255", 1, "255.255.255.255");
        binaryCalculate.prefixInPrefixCalculate("255.255.255.255", "255.255.255.0", 1, "0.0.0.0");
        binaryCalculate.prefixInPrefixCalculate("2001:0db8:85a3:0000:0000:8a2e:0370:7334", "ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff:ff", 6, "2001:0db8:85a3:0000:0000:0000:0000:0000");
    }
}