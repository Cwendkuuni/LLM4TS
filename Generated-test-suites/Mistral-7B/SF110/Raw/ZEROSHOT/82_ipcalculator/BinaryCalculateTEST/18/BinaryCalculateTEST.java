import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "11110101";
        String result = bc.addBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate bc = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "0101";
        String result = bc.subBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate bc = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.2 -- 192.168.1.254\nTotal usable IP Addresses : 254 \nSubnet: 11000010.11111100.01101100.00001111\nBinary Subnet: 11000010.11111100.01101100.00001111\nBroadcast Address: 192.168.1.255\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 01111111.11111111.11111111.00000000";
        String result = bc.IPCalculate(ip, ipPrefix);
        assertEquals(expectedOutputText, result);
    }

    @Test
    public void testGetBinaryIP() {
        BinaryCalculate bc = new BinaryCalculate();
        String binaryIP = "11001101.11101010.01110101.01101110";
        String expected = "11001101.11101010.01110101.01101110";
        String result = bc.getBinaryIP(binaryIP);
        assertEquals(expected, result);
    }

    @Test
    public void testGetNetmask() {
        BinaryCalculate bc = new BinaryCalculate();
        String binaryIP = "255.255.255.0";
        String expected = "00111111.00111111.00111111.00000000";
        String result = bc.getNetmask(binaryIP);
        assertEquals(expected, result);
    }
}