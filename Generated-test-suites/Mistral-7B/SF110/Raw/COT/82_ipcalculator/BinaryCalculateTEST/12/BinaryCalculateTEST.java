import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expectedResult = "11111010";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSubBinaryWithCarry() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1011";
        String expectedResult = "0110";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSubBinaryWithoutCarry() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1110";
        String expectedResult = "0011";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254 \n" +
                "Subnet: 11101100.11111010.01101110.00001111\n" +
                "Binary Subnet: 11101100.11111010.01101110.00001111\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 01111111.11111111.11111111.00000000";
        String output = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testIPCalculateWithIPv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String ipPrefix = "48";
        String expectedOutput = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                "Usable Range:\n" +
                "Total usable IP Addresses : 65535 \n" +
                "Subnet: 01111110.11011111.10101011.00000000.00001110.11101101.00100100.11110110\n" +
                "Binary Subnet: 01111110.11011111.10101011.00000000.00001110.11101101.00100100.11110110\n" +
                "Broadcast Address: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                "Prefix: /48\n" +
                "Netmask: 255.255.255.255.255.255.255.255.255.255.255.255.255.255.255.252\n" +
                "Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111100";
        String output = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, output);
    }
}