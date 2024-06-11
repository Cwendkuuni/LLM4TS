import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expectedSum = "11001";
        String actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);

        num1 = "1010";
        num2 = "1111";
        expectedSum = "11001";
        actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);

        num1 = "1111";
        num2 = "1111";
        expectedSum = "10000";
        actualSum = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedSum, actualSum);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expectedDiff = "0110";
        String actualDiff = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDiff, actualDiff);

        num1 = "1010";
        num2 = "1111";
        expectedDiff = "0001011011111111";
        actualDiff = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDiff, actualDiff);

        num1 = "1111";
        num2 = "1111";
        expectedDiff = "0000";
        actualDiff = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedDiff, actualDiff);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        binaryCalculate.IPCalculate(ip, ipPrefix);
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11001000.11101000.01010100.00001111\nBinary Subnet: 11110010.11101100.01010100.00001111\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 00111111.11111111.11111111.00000000\n";
        String actualOutput = binaryCalculate.getOutputText();
        assertEquals(expectedOutput, actualOutput);

        ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        ipPrefix = "60";
        binaryCalculate.IPCalculate(ip, ipPrefix);
        expectedOutput = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:FFF:FFF:FFFF:FFFF:FFFF:FFFF:FFFF\nUsable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7335 -- FFFF:FFF:FFF:FFFF:FFFF:FFFF:FFFF:FFFF\nTotal usable IP Addresses : 65534 \nSubnet: 01111011.11011011.01011011.00000000.00000000.10101101.00110011.11110111\nBinary Subnet: 00111110.11011100.01011011.00000000.00000000.10101101.00110011.11110111\nBroadcast Address: FFFF:FFF:FFF:FFFF:FFFF:FFFF:FFFF:FFFF\nPrefix: /60\nNetmask: FFFE:0:0:0:0:0:0:0\nBinary Netmask: 11111111.11111111.11111111.11111110\n";
        actualOutput = binaryCalculate.getOutputText();
        assertEquals(expectedOutput, actualOutput);
    }

    // Add other test methods as needed to test the other methods in the BinaryCalculate class

    private String getOutputText() {
        return binaryCalculate.outputText;
    }
}