package ipac;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinaryCalculateTest {
    private BinaryCalculate binaryCalculate;

    @Test
    public void testAddBinary_validInput_returnsCorrectSum() {
        binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expectedSum = "11010";
        String actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);
    }

    @Test
    public void testAddBinary_sameLengthInput_returnsCorrectSum() {
        binaryCalculate = new BinaryCalculate();
        String num1 = "1010";
        String num2 = "1010";
        String expectedSum = "11110";
        String actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);
    }

    @Test
    public void testAddBinary_longerInput_leftShift() {
        binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "10";
        String expectedSum = "10011110";
        String actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);
    }

    @Test
    public void testAddBinary_shorterInput_rightShift() {
        binaryCalculate = new BinaryCalculate();
        String num1 = "10";
        String num2 = "1111";
        String expectedSum = "111101";
        String actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);
    }

    // Test subBinary method with valid and edge cases
    // ...

    @Test
    public void testIPCalculate_validIP_returnsCorrectOutput() {
        binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254 \n" +
                "Subnet: 11101100.11110100.01101000.00001111\n" +
                "Binary Subnet: 11101100.11110100.01101000.00001111\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000\n";
        String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIPCalculate_IPv6_returnsCorrectOutput() {
        binaryCalculate = new BinaryCalculate();
        String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String ipPrefix = "64";
        String expectedOutput = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- ffff:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7333 -- ffff:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                "Total usable IP Addresses : 65535\n" +
                "Subnet: 01111101.01111111.10101101.00000000.00000000.10101101.01111110.11110100\n" +
                "Binary Subnet: 01111101.01111111.10101101.00000000.00000000.10101101.01111110.11110100\n" +
                "Broadcast Address: ffff:0db8:85a3:0000:0000:8a2e:0370:7334\n" +
                "Prefix: /64\n" +
                "Netmask: ffff:0db8:85a3:0000:0000:0000:0000:0000\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000.00000000.00000000.00000000.00000000\n";
        String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_invalidIP_throwsIllegalArgumentException() {
        binaryCalculate = new BinaryCalculate();
        String ip = "abc.def.ghi.jkl";
        String ipPrefix = "24";
        binaryCalculate.IPCalculate(ip, ipPrefix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_invalidIPPrefix_throwsIllegalArgumentException() {
        binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "33";
        binaryCalculate.IPCalculate(ip, ipPrefix);
    }

    // Test other methods with valid and edge cases
    // ...
}