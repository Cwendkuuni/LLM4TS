package ipac;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate calculator = new BinaryCalculate();
        String expected = "1111";
        String result = calculator.addBinary("1110", "101");
        assertEquals(expected, result);
    }

    @Test
    public void testAddBinaryEdgeCases() {
        BinaryCalculate calculator = new BinaryCalculate();
        String expected = "0000";
        String result = calculator.addBinary("0000", "0000");
        assertEquals(expected, result);

        expected = "1001";
        result = calculator.addBinary("1001", "1111");
        assertEquals(expected, result);

        expected = "10001";
        result = calculator.addBinary("1000", "01");
        assertEquals(expected, result);
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate calculator = new BinaryCalculate();
        String expected = "1111";
        String result = calculator.subBinary("11111111", "11111110");
        assertEquals(expected, result);
    }

    @Test
    public void testSubBinaryEdgeCases() {
        BinaryCalculate calculator = new BinaryCalculate();
        String expected = "0000";
        String result = calculator.subBinary("00000000", "00000000");
        assertEquals(expected, result);

        expected = "1111";
        result = calculator.subBinary("00000000", "11111111");
        assertEquals(expected, result);

        expected = "1011";
        result = calculator.subBinary("11111111", "11110100");
        assertEquals(expected, result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate calculator = new BinaryCalculate();
        String ip = "192.168.1.1";
        String prefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\nUsable Range: 192.168.1.1 -- 192.168.1.254\nTotal usable IP Addresses : 254\nSubnet: 11101100.11110100.01101000.00001011\nBinary Subnet: 11101100.11110100.01101000.00001011\nBroadcast Address: 11101100.11110100.01101000.00011111\nPrefix: /24\nNetmask: 255.255.255.0\nBinary Netmask: 01111111.11111111.11111111.00000000";
        String result = calculator.IPCalculate(ip, prefix);
        assertEquals(expectedOutput, result);
    }

    @Test
    public void testIPCalculateEdgeCases() {
        BinaryCalculate calculator = new BinaryCalculate();
        String ip = "255.255.255.255";
        String prefix = "32";
        String expectedOutput = "Total Range: FFFFFFFF.FFFFFFFF.FFFFFFFF.FFFFFFFF\nUsable Range: \nTotal usable IP Addresses : 0\nSubnet: \nBinary Subnet: 00000000.00000000.00000000.00000000\nBroadcast Address: FFFFFFFF.FFFFFFFF.FFFFFFFF.FFFFFFFE\nPrefix: /32\nNetmask: 255.255.255.255\nBinary Netmask: FFFFFFFF.FFFFFFFF.FFFFFFFF.FFFFFFFF\n";
        String result = calculator.IPCalculate(ip, prefix);
        assertEquals(expectedOutput, result);

        ip = "0.0.0.0";
        prefix = "0";
        expectedOutput = "Total Range: 0.0.0.0 -- 0.0.0.0\nUsable Range: 0.0.0.0 -- 0.0.0.0\nTotal usable IP Addresses : 1\nSubnet: \nBinary Subnet: \nBroadcast Address: 0.0.0.0\nPrefix: /0\nNetmask: \nBinary Netmask: \n";
        result = calculator.IPCalculate(ip, prefix);
        assertEquals(expectedOutput, result);

        ip = ":::::";
        prefix = "124";
        expectedOutput = "Total Range: :::::\nUsable Range: :::::\nTotal usable IP Addresses : 0\nSubnet: \nBinary Subnet: \nBroadcast Address: :::::\nPrefix: /124\nNetmask: \nBinary Netmask: \n";
        result = calculator.IPCalculate(ip, prefix);
        assertEquals(expectedOutput, result);
    }
}