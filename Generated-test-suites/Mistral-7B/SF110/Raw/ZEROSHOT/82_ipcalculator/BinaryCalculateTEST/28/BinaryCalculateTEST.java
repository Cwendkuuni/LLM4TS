package ipac;

import org.junit.Test;
import static org.junit.Assert.*;

import java.math.BigInteger;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "1011";
        String num2 = "1101";
        String expected = "11110";
        String result = bc.addBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "1101";
        String num2 = "1011";
        String expected = "0110";
        String result = bc.subBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String outputText = bc.IPCalculate(ip, ipPrefix);
        // Add assertions here to check the contents of outputText
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate bc = new BinaryCalculate();
        String binaryIP = "11110110111111011111100111111100";
        String expected = "11110110111111011111100111111100";
        String result = bc.getBinaryIP(binaryIP);
        assertEquals(expected, result);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate bc = new BinaryCalculate();
        String binaryIP = "11111111111111111111111111111101";
        String expected = "00000000000000000000000000000010";
        String result = bc.getNetmask(binaryIP);
        assertEquals(expected, result);
    }

    @Test
    public void testCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        String subnet = "192.168.1.1";
        String ipPrefix = "24";
        bc.calculate(subnet, ipPrefix);
        String expectedIPRangeAddress = "192.168.1.1 -- 192.168.1.255";
        String resultIPRangeAddress = bc.IPRangeAddress;
        assertEquals(expectedIPRangeAddress, resultIPRangeAddress);

        BigInteger expectedTotalIPAddresses = new BigInteger("256", 10);
        BigInteger resultTotalIPAddresses = bc.totalIPAddresses;
        assertEquals(expectedTotalIPAddresses, resultTotalIPAddresses);
    }
}