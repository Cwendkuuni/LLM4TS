import ipac.BinaryCalculate;
import org.junit.Test;
import static org.junit.Assert.*;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate calculator = new BinaryCalculate();
        String num1 = "1111";
        String num2 = "1010";
        String expected = "11110101";
        String result = calculator.addBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate calculator = new BinaryCalculate();
        String ip = "192.168.1.1";
        String ipPrefix = "24";
        String expectedOutput = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254 \n" +
                "Subnet: 11000000.11111100.01101100.00000101\n" +
                "Binary Subnet: 11111111.11111100.01101100.00000101\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 01111111.11111111.11111111.00000000";
        String result = calculator.IPCalculate(ip, ipPrefix);
        Assert.assertEquals(expectedOutput, result);
    }

    @Test
    public void testAddBinaryEdgeCases() {
        BinaryCalculate calculator = new BinaryCalculate();

        // Test addBinary with equal length inputs and carry
        String num1 = "1111";
        String num2 = "1111";
        String expected = "11111";
        String result = calculator.addBinary(num1, num2);
        assertEquals(expected, result);

        // Test subBinary with first input as all zeroes and second input as '1'
        String num3 = "0000";
        String num4 = "1";
        String expectedSubBinary = "1";
        String resultSubBinary = calculator.subBinary(num3, num4);
        assertEquals(expectedSubBinary, resultSubBinary);
    }
}