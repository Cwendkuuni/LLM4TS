package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.Test;

class CaverphoneTest {
Caverphone caverphone = new Caverphone();

@Test
public void testCaverphoneBasic() {
String input = "HelloWorld";
String expectedOutput = "H2o2l2o2w2r2l2";
assertEquals(expectedOutput, caverphone.caverphone(input));
}

// Test case for caverphone(String txt) method - Edge cases and exceptions
@Test(expected = EncoderException.class)
public void testCaverphoneNullInput() {
assertNull(caverphone.caverphone(null));
}

@Test
public void testCaverphoneEmptyInput() {
String expectedOutput = "1111111111";
assertEquals(expectedOutput, caverphone.caverphone(""));
}

// Test case for caverphone(String txt) method - Test encoding of uppercase input
@Test
public void testCaverphoneUpperCaseInput() {
String input = "HELLO WORLD";
String expectedOutput = "H2o2l2o2w2r2l2";
assertEquals(expectedOutput, caverphone.caverphone(input));
}

// Test case for caverphone(String txt) method - Test encoding of input with digits
@Test
public void testCaverphoneInputWithDigits() {
String input = "Hello123World";
String expectedOutput = "H2o2l2o2w2r2l2";
assertEquals(expectedOutput, caverphone.caverphone(input));
}

// Test case for encode(Object pObject) method
@Test
public void testEncodeObject() {
String input = "HelloWorld";
String expectedOutput = "H2o2l2o2w2r2l2";
assertEquals(expectedOutput, caverphone.encode(input));
}

// Test case for encode(String pString) method
@Test
public void testEncodeString() {
String input = "HelloWorld";
String expectedOutput = "H2o2l2o2w2r2l2";
assertEquals(expectedOutput, caverphone.encode(input));
}

// Test case for isCaverphoneEqual(String str1, String str2) method
@Test
public void testIsCaverphoneEqual() {
String input1 = "HelloWorld";
String input2 = "HelloWorld";
boolean expectedOutput = true;
assertEquals(expectedOutput, caverphone.isCaverphoneEqual(input1, input2));
}

// Test case for isCaverphoneEqual(String str1, String str2) method - Different inputs
@Test
public void testIsCaverphoneEqualDifferentInputs() {
String input1 = "HelloWorld";
String input2 = "WorldHello";
boolean expectedOutput = false;
assertEquals(expectedOutput, caverphone.isCaverphoneEqual(input1, input2));
}
}