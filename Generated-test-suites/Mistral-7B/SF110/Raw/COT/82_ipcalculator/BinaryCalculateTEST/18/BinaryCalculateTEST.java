package ipac;

import static org.junit.Assert.*;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary_TwoZeroBits_ReturnsCorrectSum() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "0100";
        String num2 = "0100";
        String expectedSum = "1010";
        String actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);
    }

    @Test
    public void testAddBinary_OneZeroBitAndOneOneBit_ReturnsCorrectSum() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "0100";
        String num2 = "1010";
        String expectedSum = "1110";
        String actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);
    }

    @Test
    public void testAddBinary_TwoOneBits_ReturnsCorrectCarry() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1100";
        String num2 = "1100";
        String expectedSum = "10110";
        String actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinary_OneStringIsEmpty_ThrowsIllegalArgumentException() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "";
        String num2 = "1100";
        binaryCalculate.addBinary(num1, num2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddBinary_BothStringsAreEmpty_ThrowsIllegalArgumentException() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "";
        String num2 = "";
        binaryCalculate.addBinary(num1, num2);
    }

    @Test
    public void testSubBinary_TwoIPsWithSameLength_ReturnsCorrectDifference() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11110110";
        String num2 = "11101101";
        String expectedDifference = "00010011";
        String actualDifference = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDifference, actualDifference);
    }

    @Test
    public void testIPCalculate_ValidIPAddressAndPrefixLength_ReturnsCorrectOutput() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n"
                + "Usable Range: 192.168.1.2 -- 192.168.1.254\n"
                + "Total usable IP Addresses : 254 \n"
                + "Subnet: 11000010.11111100.01101000.00001011\n"
                + "Binary Subnet: 11000010.11111100.01101000.00001011\n"
                + "Broadcast Address: 192.168.1.255\n"
                + "Prefix: /24\n"
                + "Netmask: 255.255.255.0\n"
                + "Binary Netmask: 01111111.11111111.11111111.00000000";
        String actualOutput = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, actualOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_IPAddressLengthIsInvalid_ThrowsIllegalArgumentException() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "26";
        binaryCalculate.IPCalculate(ip, ipPrefix);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIPCalculate_PrefixLengthIsInvalid_ThrowsIllegalArgumentException() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "33";
        binaryCalculate.IPCalculate(ip, ipPrefix);
    }

    @Test
    public void testPrefixInPrefixCalculate_ValidInput_ReturnsCorrectOutput() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.0";
        String ipPrefix = "24";
        String endPrefix = "32";
        int noPrefix = 5;
        String[][] expectedOutput = new String[noPrefix + 8][2];
        String output1 = "192.168.1.0 /24";
        String output2 = "192.168.1.0 -- 192.168.1.255";
        String output3 = "Total usable IP Addresses :254 ";
        String output4 = "11000010.11111100.01101000.00000000";
        String output5 = "192.168.1.255";
        String output6 = "/32";
        String output7 = "255.255.255.0";
        String output8 = "FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF";
        String output9 = "Your computer does not have sufficient memory to process this query.";
        String output10 = "Subnet:";
        String output11 = "Binary Subnet:";
        String output12 = "Broadcast Address:";
        String output13 = "Prefix:";
        String output14 = "Netmask";
        String output15 = "Binary Netmask:";
        expectedOutput[0][0] = output1;
        expectedOutput[0][1] = output2;
        expectedOutput[1][0] = output3;
        expectedOutput[2][0] = output10;
        expectedOutput[2][1] = output4;
        expectedOutput[3][0] = output11;
        expectedOutput[3][1] = output5;
        expectedOutput[4][0] = output12;
        expectedOutput[4][1] = output5;
        expectedOutput[5][0] = output13;
        expectedOutput[5][1] = output6;
        expectedOutput[6][0] = output14;
        expectedOutput[6][1] = output7;
        expectedOutput[7][0] = output15;
        expectedOutput[7][1] = output8;
        binaryCalculate.prefixInPrefixCalculate(ip, ipPrefix, noPrefix, endPrefix);
        for (int i = 0; i < noPrefix + 8; i++) {
            String output = binaryCalculate.getOutput(i);
            assertEquals(expectedOutput[i][0], output);
        }
    }

    @Test(expected = OutOfMemoryError.class)
    public void testPrefixInPrefixCalculate_InputCausesOutOfMemoryError_ThrowsOutOfMemoryError() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.0";
        String ipPrefix = "24";
        int noPrefix = 256000;
        String endPrefix = "32";
        binaryCalculate.prefixInPrefixCalculate(ip, ipPrefix, noPrefix, endPrefix);
    }
}