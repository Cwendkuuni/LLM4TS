import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.EncoderException;

public class CaverphoneTest {

 private final StringEncoder caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
 String input = "";
 String expectedOutput = "1111111111";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testSingleLetter() {
 String input = "a";
 String expectedOutput = "A";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testSimpleString() {
 String input = "hello";
 String expectedOutput = "H2L2O";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testMixedCaseString() {
 String input = "HeLlO wOrLd";
 String expectedOutput = "H2L2OwOr2d";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test(expected = EncoderException.class)
 public void testNullInput() {
 String input = null;
 caverphone.encode(input);
 }

 @Test(expected = EncoderException.class)
 public void testLongInput() {
 String input = "This is a test string that is longer than the maximum length that can be legally encoded using the Caverphone algorithm with Java strings of type String";
 caverphone.encode(input);
 }

 @Test
 public void testStartOptions() {
 String input1 = "cough";
 String expectedOutput1 = "cou2f";
 String output1 = caverphone.encode(input1);
 assertEquals(expectedOutput1, output1);

 String input2 = "rough";
 String expectedOutput2 = "rou2f";
 String output2 = caverphone.encode(input2);
 assertEquals(expectedOutput2, output2);

 String input3 = "tough";
 String expectedOutput3 = "tou2f";
 String output3 = caverphone.encode(input3);
 assertEquals(expectedOutput3, output3);

 String input4 = "enough";
 String expectedOutput4 = "enou2f";
 String output4 = caverphone.encode(input4);
 assertEquals(expectedOutput4, output4);

 String input5 = "trough";
 String expectedOutput5 = "trou2f";
 String output5 = caverphone.encode(input5);
 assertEquals(expectedOutput5, output5);
 }

 @Test
 public void testReplacements() {
 String input1 = "cq";
 String expectedOutput1 = "2q";
 String output1 = caverphone.encode(input1);
 assertEquals(expectedOutput1, output1);

 String input2 = "ci";
 String expectedOutput2 = "si";
 String output2 = caverphone.encode(input2);
 assertEquals(expectedOutput2, output2);

 String input3 = "ce";
 String expectedOutput3 = "se";
 String output3 = caverphone.encode(input3);
 assertEquals(expectedOutput3, output3);

 String input4 = "cy";
 String expectedOutput4 = "sy";
 String output4 = caverphone.encode(input4);
 assertEquals(expectedOutput4, output4);

 String input5 = "tch";
 String expectedOutput5 = "2ch";
 String output5 = caverphone.encode(input5);
 assertEquals(expectedOutput5, output5);

 String input6 = "c";
 String expectedOutput6 = "k";
 String output6 = caverphone.encode(input6);
 assertEquals(expectedOutput6, output6);

 String input7 = "q";
 String expectedOutput7 = "k";
 String output7 = caverphone.encode(input7);
 assertEquals(expectedOutput7, output7);

 String input8 = "x";
 String expectedOutput8 = "k";
 String output8 = caverphone.encode(input8);
 assertEquals(expectedOutput8, output8);

 String input9 = "v";
 String expectedOutput9 = "f";
 String output9 = caverphone.encode(input9);
 assertEquals(expectedOutput9, output9);

 String input10 = "dg";
 String expectedOutput10 = "2g";
 String output10 = caverphone.encode(input10);
 assertEquals(expectedOutput10, output10);

 String input11 = "tio";
 String expectedOutput11 = "sio";
 String output11 = caverphone.encode(input11);
 assertEquals(expectedOutput11, output11);

 String input12 = "tia";
 String expectedOutput12 = "sia";
 String output12 = caverphone.encode(input12);
 assertEquals(expectedOutput12, output12);

 String input13 = "d";
 String expectedOutput13 = "t";
 String output13 = caverphone.encode(input13);
 assertEquals(expectedOutput13, output13);

 String input14 = "ph";
 String expectedOutput14 = "fh";
 String output14 = caverphone.encode(input14);
 assertEquals(expectedOutput14, output14);

 String input15 = "b";
 String expectedOutput15 = "p";
 String output15 = caverphone.encode(input15);
 assertEquals(expectedOutput15, output15);

 String input16 = "sh";
 String expectedOutput16 = "s2";
 String output16 = caverphone.encode(input16);
 assertEquals(expectedOutput16, output16);

 String input17 = "z";
 String expectedOutput17 = "s";
 String output17 = caverphone.encode(input17);
 assertEquals(expectedOutput17, output17);

 String input18 = "y3";
 String expectedOutput18 = "Y3";
 String output18 = caverphone.encode(input18);
 assertEquals(expectedOutput18, output18);

 String input19 = "y";
 String expectedOutput19 = "A";
 String output19 = caverphone.encode(input19);
 assertEquals(expectedOutput19, output19);

 String input20 = "j";
 String expectedOutput20 = "y";
 String output20 = caverphone.encode(input20);
 assertEquals(expectedOutput20, output20);

 String input21 = "w3";
 String expectedOutput21 = "W3";
 String output21 = caverphone.encode(input21);
 assertEquals(expectedOutput21, output21);
 }

 @Test
 public void testHandlesRemovals() {
 String input = "22gh3";
 String expectedOutput = "";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testHandlesRemovalsLastCharacter() {
 String input = "3";
 String expectedOutput = "A";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testHandlesRemovalsAllCharacters() {
 String input = "3";
 String expectedOutput = "";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testHandlesStartAndEndOption() {
 String input = "cough3";
 String expectedOutput = "cou2fA";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testHandlesStartOption() {
 String input = "cough";
 String expectedOutput = "cou2f";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testHandlesEndOption() {
 String input = "cough";
 String expectedOutput = "cou2f";
 String output = caverphone.encode(input);
 String expectedOutputWithEndOption = "cou2f111111";
 String outputWithEndOption = caverphone.encode(input) + "111111";
 assertEquals(expectedOutputWithEndOption, outputWithEndOption);
 }

 @Test
 public void testHandlesStartAndEndOptionTogether() {
 String input = "cough3";
 String expectedOutput = "cou2fA";
 String output = caverphone.encode(input);
 assertEquals(expectedOutput, output);
 String expectedOutputWithEndOption = "cou2fA111111";
 String outputWithEndOption = caverphone.encode(input) + "111111";
 assertEquals(expectedOutputWithEndOption, outputWithEndOption);
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "hello";
 String str2 = "hello";
 boolean expectedOutput = true;
 boolean output = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testIsCaverphoneEqualDifferentStrings() {
 String str1 = "hello";
 String str2 = "world";
 boolean expectedOutput = false;
 boolean output = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testIsCaverphoneEqualNullInput1() {
 String str1 = null;
 String str2 = "hello";
 boolean expectedOutput = false;
 boolean output = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(expectedOutput, output);
 }

 @Test
 public void testIsCaverphoneEqualNullInput2() {
 String str1 = "hello";
 String str2 = null;
 boolean expectedOutput = false;
 boolean output = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(expectedOutput, output);
 }
}