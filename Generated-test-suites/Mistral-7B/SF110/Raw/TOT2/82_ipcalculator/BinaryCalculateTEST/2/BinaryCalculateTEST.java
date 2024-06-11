//... (import statements and class initialization)

public class BinaryCalculateTest {

@Test
public void testAddBinaryWithZero() {
    BinaryCalculate binaryCalculate = new BinaryCalculate();
    String result = binaryCalculate.addBinary("0000", "0000");
    Assert.assertEquals("0000", result);
}

@Test
public void testAddBinarySameNumbers() {
    BinaryCalculate binaryCalculate = new BinaryCalculate();
    String result = binaryCalculate.addBinary("1111", "1111");
    Assert.assertEquals("11111111", result);
}

@Test
public void testAddBinaryCarry() {
    BinaryCalculate binaryCalculate = new BinaryCalculate();
    String result = binaryCalculate.addBinary("1111", "1010");
    Assert.assertEquals("10111", result);
}

@Test
public void testAddBinaryDifferentLengths() {
    BinaryCalculate binaryCalculate = new BinaryCalculate();
    String result = binaryCalculate.addBinary("1111", "11");
    Assert.assertEquals("11111", result);
}

@Test(expected = IllegalArgumentException.class)
public void testAddBinaryInvalidInput1() {
    BinaryCalculate binaryCalculate = new BinaryCalculate();
    binaryCalculate.addBinary(null, "1111");
}

@Test(expected = IllegalArgumentException.class)
public void testAddBinaryInvalidInput2() {
    BinaryCalculate binaryCalculate = new BinaryCalculate();
    binaryCalculate.addBinary("1111", "");
}

//... (other test cases for other methods)

}