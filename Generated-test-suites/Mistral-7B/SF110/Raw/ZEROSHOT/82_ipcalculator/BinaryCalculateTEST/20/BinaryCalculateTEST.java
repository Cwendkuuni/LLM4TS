package ipac;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "11110101";
        String num2 = "10101110";
        String expected = "11011111";

        String result = bc.addBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "11111111";
        String num2 = "10101010";
        String expected = "10010101";

        String result = bc.subBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";

        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                                "Total usable IP Addresses : 254\n" +
                                "Subnet: 11001100.11101000.01101100.00001111\n" +
                                "Binary Subnet: 11001100.11101000.01101100.00001111\n" +
                                "Broadcast Address: 11001100.11101000.01101100.11111111\n" +
                                "Prefix: /24\n" +
                                "Netmask: 255.255.255.0\n" +
                                "Binary Netmask: 01111111.11111111.11111111.00000000\n";

        String result = bc.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate bc = new BinaryCalculate();
        String ip = "192.168.1.1";
        String expected = "11001100.01101100.01101100.00010001";

        String result = bc.getBinaryIP(ip);
        assertEquals(expected, result);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate bc = new BinaryCalculate();
        String ipPrefix = "24";
        String expected = "11111111.00000000.00000000.00011111";

        String result = bc.getNetmask(ipPrefix);
        assertEquals(expected, result);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";

        bc.calculate(ip, ipPrefix);
        BigInteger expected = new BigInteger("254", 10);

        BigInteger result = bc.totalIPAddresses;
        assertEquals(expected, result);
    }
}