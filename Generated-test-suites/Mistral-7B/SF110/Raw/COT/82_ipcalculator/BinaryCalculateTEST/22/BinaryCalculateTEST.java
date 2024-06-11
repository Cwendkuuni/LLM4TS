package ipac.test;

import static org.junit.Assert.*;

import ipac.BinaryCalculate;
import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary_twoZeroBinaries() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("0000", "0000");
        assertEquals("0000", result);
    }

    @Test
    public void testAddBinary_twoOneBinaries() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("0001", "0001");
        assertEquals("0010", result);
    }

    @Test
    public void testAddBinary_carry() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("0111", "1010");;
        assertEquals("11110", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinary_invalidInput1() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.addBinary("01", "1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinary_invalidInput2() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        binaryCalculate.addBinary("11111111111111111111111111111111", "11111111111111111111111111111111");
    }

    @Test
    public void testSubBinary_validInput() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.subBinary("1111", "1010");
        assertEquals("0111", result);
    }

    @Test
    public void testIPCalculate_validIPv4() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.IPCalculate("192.168.1.1", "24");
        assertTrue(result.contains("Total Range: 192.168.1.1 -- 192.168.1.255"));
        assertTrue(result.contains("Usable Range: 192.168.1.2 -- 192.168.1.254"));
    }

    @Test
    public void testIPCalculate_validIPv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.IPCalculate("2001:0db8:85a3:0000:0000:8a2e:0370:7334", "60");
        assertTrue(result.contains("Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334"));
        assertTrue(result.contains("Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7328 -- 2001:0db8:85a3:0000:0000:8a2e:0370:7342"));
    }

    @Test
    public void testIPCalculate_mismatchedIPVersion() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.IPCalculate("192.168.1.1", "60");
        assertEquals("Your input IP address and IP prefix length are not compatible.", binaryCalculate.getCurrentIP());
    }

    @Test
    public void testGetBinaryIP_validBinaryIP() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.getBinaryIP("192.168.1.1");
        assertEquals("11000010.11110100.01101101.01101111", result);
    }

    @Test
    public void testGetNetmask_validNetmask() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.getNetmask("24");
        assertEquals("11111111.00000000.00000000.00001111", result);
    }
}