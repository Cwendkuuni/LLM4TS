import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {
    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expectedSum = "11010";
        String actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);

        num1 = "1010";
        num2 = "1111";
        expectedSum = "11110";
        actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);

        num1 = "1111";
        num2 = "1111";
        expectedSum = "10000";
        actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinaryWithNullArgument() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = null;
        String num2 = "1111";
        binaryCalculate.addBinary(num1, num2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinaryWithEmptyStringArgument() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "";
        String num2 = "1111";
        binaryCalculate.addBinary(num1, num2);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expectedDiff = "0101";
        String actualDiff = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDiff, actualDiff);

        num1 = "0111";
        num2 = "1111";
        expectedDiff = "10001";
        actualDiff = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDiff, actualDiff);

        num1 = "1111";
        num2 = "0111";
        expectedDiff = "10011";
        actualDiff = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDiff, actualDiff);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBinaryWithNullArgument() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = null;
        binaryCalculate.subBinary(num1, num2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSubBinaryWithEmptyStringArgument() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "";
        binaryCalculate.subBinary(num1, num2);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254\n" +
                "Subnet: 11000010.11110100.01101000.00001111\n" +
                "Binary Subnet: 11111100.11111010.01101000.00001111\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000";
        String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testIPCalculateWithIPv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String ipPrefix = "60";
        String expectedOutput = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFFFFFF:FFFE:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7335 -- FFFFFFFF:FFFE:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                "Total usable IP Addresses : 65535\n" +
                "Subnet: 01111110.11011111.10101101.00000000.00001110.11011101.01110111.11111111\n" +
                "Binary Subnet: 01111110.11011111.10101101.00000000.00001110.11011101.01110111.11111111\n" +
                "Broadcast Address: FFFF:FFFE:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                "Prefix: /60\n" +
                "Netmask: FFFE:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                "Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111100";
        String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, actualOutput);
    }
}