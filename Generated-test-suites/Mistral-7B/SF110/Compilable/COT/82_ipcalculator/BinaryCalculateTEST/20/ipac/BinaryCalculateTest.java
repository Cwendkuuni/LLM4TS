package ipac.test;

import static org.junit.Assert.*;

import ipac.BinaryCalculate;
import org.junit.Test;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary_BothNumbersAreZero() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String expected = "0";
        String result = binaryCalculate.addBinary("0", "0");
        assertEquals(expected, result);
    }

    @Test
    public void testAddBinary_BothNumbersAreOne() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String expected = "1";
        String result = binaryCalculate.addBinary("1", "1");
        assertEquals(expected, result);
    }

    @Test
    public void testAddBinary_Num1IsLongerThanNum2() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11111111111111111111111111111101";
        String num2 = "11111111111111111111111111111010";
        String expected = "11111111111111111111111111111111";
        String result = binaryCalculate.addBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test(expected = AssertionError.class)
    public void testAddBinary_InputIsInvalid() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.addBinary("1", "a");
    }

    @Test
    public void testSubBinary_InputIsValid() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String num1 = "11111111111111111111111111111101";
        String num2 = "11111111111111111111111111111010";
        String expected = "00000000000000000000000000001001";
        String result = binaryCalculate.subBinary(num1, num2);
        assertEquals(expected, result);
    }

    @Test(expected = AssertionError.class)
    public void testSubBinary_InputIsInvalid() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String result = binaryCalculate.subBinary("1", "a");
    }

    @Test
    public void testIPCalculate_IPv4_ValidInput() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.1.1";
        String IPPrefix = "24";
        String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254 \n" +
                "Subnet: 11101100.11110100.01101001.00001111\n" +
                "Binary Subnet: 11101100.11110100.01101001.00001111\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000\n";
        String resultOutputText = binaryCalculate.IPCalculate(IP, IPPrefix);
        assertEquals(expectedOutputText, resultOutputText);
    }

    @Test
    public void testIPCalculate_IPv6_ValidInput() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String IPPrefix = "64";
        String expectedOutputText = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7333 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:0001\n" +
                "Total usable IP Addresses : 65535 \n" +
                "Subnet: 01111011.11011011.01010111.00000000.00001110.11100111.00100101.11110101\n" +
                "Binary Subnet: 01111011.11011011.01010111.00000000.00001110.11100111.00100101.11110101\n" +
                "Broadcast Address: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                "Prefix: /64\n" +
                "Netmask: 255.255.255.255.255.255.255.255.0.0.0.0.0.0.0.0.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.00000000\n";
        String resultOutputText = binaryCalculate.IPCalculate(IP, IPPrefix);
        assertEquals(expectedOutputText, resultOutputText);
    }

    @Test(expected = AssertionError.class)
    public void testIPCalculate_InputIsInvalid_IPv4() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "192.168.1.1.1";
        String IPPrefix = "24";
        binaryCalculate.IPCalculate(IP, IPPrefix);
    }

    @Test(expected = AssertionError.class)
    public void testIPCalculate_InputIsInvalid_IPv6() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String IP = "2001:0db8:85a3::";
        String IPPrefix = "64";
        binaryCalculate.IPCalculate(IP, IPPrefix);
    }

    @Test(expected = AssertionError.class)
    public void testCalculate_InvalidIPLength() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String subnet = "192.168.1.";
        String noOfIPAddresses = "10";
        binaryCalculate.calculate(subnet, noOfIPAddresses);
    }

    @Test
    public void testCalculate_IPv4() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();
        String subnet = "192.168.1.1";
        String noOfIPAddresses = "256";
        binaryCalculate.calculate(subnet, noOfIPAddresses);
        String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\n" +
                "Usable Range: 192.168.1.2 -- 192.168.1.254\n" +
                "Total usable IP Addresses : 254 \n" +
                "Subnet: 11101100.11110100.01101001.00001111\n" +
                "Binary Subnet: 11101100.11110100.01101001.00001111\n" +
                "Broadcast Address: 192.168.1.255\n" +
                "Prefix: /24\n" +
                "Netmask: 255.255.255.0\n" +
                "Binary Netmask: 11111111.11111111.11111111.00000000\n";
        String resultOutputText = binaryCalculate.IPRangeAddress;
        String resultUsableRange = binaryCalculate.usableRange;
        String resultPrefix = binaryCalculate.prefix;
        assertEquals(expectedOutputText, resultOutputText);
        assertEquals(expectedOutputText.substring(0, expectedOutputText.indexOf("\n") + 1), resultOutputText.substring(0, resultOutputText.indexOf("\n") + 1));
        assertEquals(expectedOutputText.substring(expectedOutputText.lastIndexOf("\n")), resultOutputText.substring(resultOutputText.lastIndexOf("\n")));
        assertEquals(expectedOutputText.split("\n")[4], resultPrefix);

        // Test IPv6
        String subnetIPv6 = "2001:0db8:85a3:0000:0000:8a2e:0370:7334";
        String noOfIPAddressesIPv6 = "65536";
        binaryCalculate.calculate(subnetIPv6, noOfIPAddressesIPv6);
        expectedOutputText = "Total Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFE\n" +
                "Usable Range: 2001:0db8:85a3:0000:0000:8a2e:0370:7334 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                "Total usable IP Addresses : 65534 \n" +
                "Subnet: 01111011.11011011.01010111.00000000.00001110.11100111.00100101.11110101\n" +
                "Binary Subnet: 01111011.11011011.01010111.00000000.00001110.11100111.00100101.11110101\n" +
                "Broadcast Address: FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF\n" +
                "Prefix: /64\n" +
                "Netmask: 255.255.255.255.255.255.255.255.255.255.255.255.255.255.255.255\n" +
                "Binary Netmask: 11111111.11111111.11111111.11111111.11111111.11111111.11111111.11111111\n";
        resultOutputText = binaryCalculate.IPRangeAddress;
        resultUsableRange = binaryCalculate.usableRange;
        resultPrefix = binaryCalculate.prefix;
        assertEquals(expectedOutputText, resultOutputText);
        assertEquals(expectedOutputText.substring(0, expectedOutputText.indexOf("\n") + 1), resultOutputText.substring(0, resultOutputText.indexOf("\n") + 1));
        assertEquals(expectedOutputText.substring(expectedOutputText.lastIndexOf("\n")), resultOutputText.substring(resultOutputText.lastIndexOf("\n")));
        assertEquals(expectedOutputText.split("\n")[4], resultPrefix);
    }
}