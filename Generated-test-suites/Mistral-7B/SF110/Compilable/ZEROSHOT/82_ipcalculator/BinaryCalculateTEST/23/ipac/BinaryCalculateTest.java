package ipac;

import org.junit.Assert; // For Assert.assertEquals and Assert.fail
import org.junit.Test;

import java.math.BigInteger;

public class BinaryCalculateTest {

    @Test
    public void testAddBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String num1 = "1111";
        String num2 = "1010";
        String expected = "11110101";
        String result = binaryCalculate.addBinary(num1, num2);
        Assert.assertEquals(expected, result);

        num1 = "1010";
        num2 = "1111";
        expected = "10011101";
        result = binaryCalculate.addBinary(num1, num2);
        Assert.assertEquals(expected, result);

        num1 = "11111111";
        num2 = "11111111";
        expected = "111111110000";
        result = binaryCalculate.addBinary(num1, num2);
        Assert.assertEquals(expected, result);

        num1 = "0000";
        num2 = "0000";
        expected = "0000";
        result = binaryCalculate.addBinary(num1, num2);
        Assert.assertEquals(expected, result);

        num1 = "0001";
        num2 = "1111";
        Assert.fail("Should throw an exception for invalid input size.");
    }

    @Test
    public void testSubBinary() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String num1 = "1111";
        String num2 = "1010";
        String expected = "0101";
        String result = binaryCalculate.subBinary(num1, num2);
        Assert.assertEquals(expected, result);

        num1 = "1010";
        num2 = "1111";
        expected = "0101000000000001";
        result = binaryCalculate.subBinary(num1, num2);
        Assert.assertEquals(expected, result);

        num1 = "11111111";
        num2 = "11111111";
        expected = "0000000000000000";
        result = binaryCalculate.subBinary(num1, num2);
        Assert.assertEquals(expected, result);

        num1 = "0000";
        num2 = "0000";
        expected = "0000";
        result = binaryCalculate.subBinary(num1, num2);
        Assert.assertEquals(expected, result);

        num1 = "0001";
        num2 = "1111";
        Assert.fail("Should throw an exception for invalid input size.");
    }

    @Test
    public void testIPCalculate() {
        BinaryCalculate binaryCalculate = new BinaryCalculate();

        String ip = "192.168.1.1";
        String ipPrefix = "24";
        binaryCalculate.IPCalculate(ip, ipPrefix);

        String expectedIPRangeAddress = "192.168.1.1 -- 192.168.1.255";
        String resultIPRangeAddress = binaryCalculate.IPRangeAddress;
        Assert.assertEquals(expectedIPRangeAddress, resultIPRangeAddress);

        String expectedUsableRange = "192.168.1.2 -- 192.168.1.254";
        String resultUsableRange = binaryCalculate.usableRange;
        Assert.assertEquals(expectedUsableRange, resultUsableRange);

        ip = "2001:0db8:85a3:0000:0242:0ba1:0243:0456";
        ipPrefix = "60";
        binaryCalculate = new BinaryCalculate();
        binaryCalculate.IPCalculate(ip, ipPrefix);

        String expectedIPRangeAddress2 = "2001:0db8:85a3:0000:0242:0ba1:0243:0456 -- FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF:FFFF";
        String resultIPRangeAddress2 = binaryCalculate.IPRangeAddress;
        Assert.assertEquals(expectedIPRangeAddress2, resultIPRangeAddress2);

        String expectedUsableRange2 = "2001:0db8:85a3:0000:0242:0ba1:0243:0455 -- 2001:0db8:85a3:0000:0242:0ba1:0243:0456";
        String resultUsableRange2 = binaryCalculate.usableRange;
        Assert.assertEquals(expectedUsableRange2, resultUsableRange2);
    }
}