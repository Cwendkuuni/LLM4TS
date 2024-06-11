package ipac;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigInteger;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "11111010";
        String actual = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, actual);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "0101";
        String actual = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, actual);
    }

    @Test
    public void testIPCalculateIPv4() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11000010.11110100.01110101.00000101\nBinary Subnet: 11000010.11111010.01111010.00001101\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 11111111.11111111.11111111.00000000";
        String actualOutputText = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutputText, actualOutputText);
    }

    @Test
    public void testIPCalculateIPv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String ip = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String ipPrefix = "60";
        String expectedOutputText = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334\nUsable Range: FFFE:0DB8:85A3:0000:0000:8A2E:0370:7332 -- FFFE:0DB8:85A3:0000:0000:8A2E:0370:7334\nTotal usable IP Addresses : 65534\nSubnet: 10111110.11011011.01010101.00000000.00001110.11101110.00110111.11111110\nBinary Subnet: 10111110.11011011.01010101.00000000.00001110.11101110.00110111.11111100\nBroadcast Address: FFFE:0DB8:85A3:0000:0000:8A2E:0370:7334\nPrefix: /60\nNetmask: FFFE:0:0:0:0:FFFF:FFFE:FFFE\nBinary Netmask: 11111111.11111111.11111111.11111111.00000000.FFFF.FFFF.FFFF";
        String actualOutputText = binaryCalculate.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutputText, actualOutputText);
    }
}