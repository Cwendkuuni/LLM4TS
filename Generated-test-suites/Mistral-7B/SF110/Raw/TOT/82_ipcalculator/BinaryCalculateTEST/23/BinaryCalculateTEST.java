package ipac;

import static org.junit.Assert.*;

import java.math.BigInteger;

import org.junit.Test;

public class BinaryCalculateTest {

	@Test
	public void testAddBinary_TwoZeroStrings() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String num1 = "0";
		String num2 = "0";
		String expectedResult = "0";
		String result = binaryCalculate.addBinary(num1, num2);
		assertEquals(expectedResult, result);
	}

	@Test
	public void testAddBinary_TwoOneStrings() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String num1 = "1";
		String num2 = "1";
		String expectedResult = "10";
		String result = binaryCalculate.addBinary(num1, num2);
		assertEquals(expectedResult, result);
	}

	@Test
	public void testAddBinary_OneZeroOneString() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String num1 = "0";
		String num2 = "1";
		String expectedResult = "1";
		String result = binaryCalculate.addBinary(num1, num2);
		assertEquals(expectedResult, result);
	}

	@Test
	public void testAddBinary_OneOneZeroString() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String num1 = "1";
		String num2 = "0";
		String expectedResult = "1";
		String result = binaryCalculate.addBinary(num1, num2);
		assertEquals(expectedResult, result);
	}

	@Test
	public void testAddBinary_Carry() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String num1 = "11";
		String num2 = "11";
		String expectedResult = "1010";
		String result = binaryCalculate.addBinary(num1, num2);
		assertEquals(expectedResult, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddBinary_InvalidInput1() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String num1 = "";
		String num2 = "1";
		binaryCalculate.addBinary(num1, num2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAddBinary_InvalidInput2() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String num1 = "1";
		String num2 = "";
		binaryCalculate.addBinary(num1, num2);
	}

	@Test
	public void testSubBinary_TwoZeroStrings() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String num1 = "0";
		String num2 = "0";
		String expectedResult = "0";
		String result = binaryCalculate.subBinary(num1, num2);
		assertEquals(expectedResult, result);
	}

	@Test
	public void testSubBinary_OneZeroOneString() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String num1 = "1";
		String num2 = "0";
		String expectedResult = "1";
		String result = binaryCalculate.subBinary(num1, num2);
		assertEquals(expectedResult, result);
	}

	@Test
	public void testSubBinary_OneOneZeroString() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String num1 = "1";
		String num2 = "0";
		String expectedResult = "0";
		String result = binaryCalculate.subBinary(num2, num1);
		assertEquals(expectedResult, result);
	}

	@Test
	public void testSubBinary_Borrow() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String num1 = "11";
		String num2 = "10";
		String expectedResult = "01";
		String result = binaryCalculate.subBinary(num1, num2);
		assertEquals(expectedResult, result);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSubBinary_InvalidInput1() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String num1 = "";
		String num2 = "1";
		binaryCalculate.subBinary(num1, num2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSubBinary_InvalidInput2() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String num1 = "1";
		String num2 = "";
		binaryCalculate.subBinary(num1, num2);
	}

	@Test
	public void testIPCalculate_ValidIP() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String ip = "192.168.1.1";
		String ipPrefix = "24";
		String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.1.255\n"
				+ "Usable Range: 192.168.1.2 -- 192.168.1.254\n"
				+ "Total usable IP Addresses : 254 \n"
				+ "Subnet: 11001010.11110100.01101001.00000101\n"
				+ "Binary Subnet: 11111110.11111111.01110110.00101001\n"
				+ "Broadcast Address: 11111110.11111111.01101001.11111111\n"
				+ "Prefix: /24\n"
				+ "Netmask: 255.255.255.0\n"
				+ "Binary Netmask: 11111111.11111111.11111111.00000000\n";
		String result = binaryCalculate.IPCalculate(ip, ipPrefix);
		assertEquals(expectedOutputText, result);
	}

	@Test
	public void testIPCalculate_InvalidIP() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String ip = "256.256.256.256";
		String ipPrefix = "24";
		String expectedOutputText = "";
		String result = binaryCalculate.IPCalculate(ip, ipPrefix);
		assertEquals(expectedOutputText, result);
	}

	@Test
	public void testIPCalculate_LongIP() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String ip = "255.255.255.255";
		String ipPrefix = "32";
		String expectedOutputText = "Total Range: FFFFFFFF.FFFFFFFF.FFFFFFFF.FFFFFFFF\n"
				+ "Usable Range: FFFFFFFF.FFFFFFFF.FFFFFFFF.FFFFFFFF\n"
				+ "Total usable IP Addresses : 1\n"
				+ "Subnet: FFFFFFFF.FFFFFFFF.FFFFFFFF.FFFFFFFF\n"
				+ "Binary Subnet: FFFFFFFF.FFFFFFFF.FFFFFFFF.FFFFFFFF\n"
				+ "Broadcast Address: FFFFFFFF.FFFFFFFF.FFFFFFFF.FFFFFFFF\n"
				+ "Prefix: /32\n"
				+ "Netmask: 255.255.255.255\n"
				+ "Binary Netmask: FFFFFFFF.FFFFFFFF.FFFFFFFF.FFFFFFFF\n";
		String result = binaryCalculate.IPCalculate(ip, ipPrefix);
		assertEquals(expectedOutputText, result);
	}

	@Test
	public void testIPCalculate_ValidIPPrefix() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String ip = "192.168.1.1";
		String ipPrefix = "26";
		String expectedOutputText = "Total Range: 192.168.1.1 -- 192.168.255.255\n"
				+ "Usable Range: 192.168.1.2 -- 192.168.255.254\n"
				+ "Total usable IP Addresses : 254 \n"
				+ "Subnet: 11001010.11111100.01101001.00000101\n"
				+ "Binary Subnet: 11111110.11111111.01110110.00101001\n"
				+ "Broadcast Address: 11111110.11111111.01101001.11111111\n"
				+ "Prefix: /26\n"
				+ "Netmask: 255.255.255.192\n"
				+ "Binary Netmask: 11111110.11111111.01111100.00001000\n";
		String result = binaryCalculate.IPCalculate(ip, ipPrefix);
		assertEquals(expectedOutputText, result);
	}

	@Test
	public void testIPCalculate_InvalidIPPrefix() {
		BinaryCalculate binaryCalculate = new BinaryCalculate();
		String ip = "192.168.1.1";
		String ipPrefix = "30";
		String expectedOutputText = "";
		String result = binaryCalculate.IPCalculate(ip, ipPrefix);
		assertEquals(expectedOutputText, result);
	}
}