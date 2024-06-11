import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testEmptyString() {
 String expected = "1111111111";
 String actual = caverphone.caverphone("");
 assertEquals(expected, actual);
 }

 @Test
 public void testNullString() {
 String expected = "1111111111";
 String actual = caverphone.caverphone(null);
 assertEquals(expected, actual);
 }

 @Test
 public void testLowerCase() {
 String input = "hello world";
 String expected = "H2L2O2W2D2";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testRemoveNonAlpha() {
 String input = "123hello world456";
 String expected = "H2L2O2W2D2";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testRemoveFinalE() {
 String input = "hello worlde";
 String expected = "H2L2O2W2D";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testHandleStartOptions() {
 String input = "cough";
 String expected = "cou2f";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "rough";
 expected = "rou2f";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "tough";
 expected = "tou2f";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "enough";
 expected = "enou2f";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "trough";
 expected = "trou2f";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "gn";
 expected = "2n";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "mb";
 expected = "m2";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testHandleReplacements() {
 String input = "cq";
 String expected = "2q";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "ci";
 expected = "si";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "ce";
 expected = "se";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "cy";
 expected = "sy";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "tch";
 expected = "2ch";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "c";
 expected = "k";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "q";
 expected = "k";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "x";
 expected = "k";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "v";
 expected = "f";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "dg";
 expected = "2g";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "tio";
 expected = "sio";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "tia";
 expected = "sia";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "d";
 expected = "t";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "ph";
 expected = "fh";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "b";
 expected = "p";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "sh";
 expected = "s2";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "z";
 expected = "s";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "w3";
 expected = "W3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "why";
 //expected = "Why"; // 1.0 only
 //actual = caverphone.caverphone(input);
 //assertEquals(expected, actual);

 input = "wh3";
 expected = "Wh3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "w";
 expected = "2";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "^h";
 expected = "A";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "h";
 expected = "2";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "r3";
 expected = "R3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "r$";
 expected = "3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "ry";
 //expected = "Ry"; // 1.0 only
 //actual = caverphone.caverphone(input);
 //assertEquals(expected, actual);

 input = "r";
 expected = "2";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "l3";
 expected = "L3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "l$";
 expected = "3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "ly";
 //expected = "Ly"; // 1.0 only
 //actual = caverphone.caverphone(input);
 //assertEquals(expected, actual);

 input = "l";
 expected = "2";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "j";
 expected = "y";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "y3";
 expected = "Y3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "y";
 expected = "3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "3gh3";
 expected = "3kh3";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "gh";
 expected = "22";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "g";
 expected = "k";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "s+";
 expected = "S";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "t+";
 expected = "T";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "p+";
 expected = "P";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "k+";
 expected = "K";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "f+";
 expected = "F";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "m+";
 expected = "M";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "n+";
 expected = "N";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testHandleRemovals() {
 String input = "123";
 String expected = "";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "3$";
 expected = "A";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);

 input = "3";
 expected = "";
 actual = caverphone.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testPutTenOnEnd() {
 String input = "abcdefghijk";
 String expected = "abcdefghijk1111111111";
 String actual = caverphone.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test(expected = EncoderException.class)
 public void testNonStringInput() {
 Integer input = 123;
 caverphone.encode(input);
 }

 @Test
 public void testEncodeString() {
 String input = "hello world";
 String expected = "H2L2O2W2D2";
 String actual = caverphone.encode(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testIsCaverphoneEqual() {
 String str1 = "hello world";
 String str2 = "hello world";
 boolean expected = true;
 boolean actual = caverphone.isCaverphoneEqual(str1, str2);
 assertEquals(expected, actual);

 str2 = "hello worlde";
 actual = caverphone.isCaverphoneEqual(str1, str2);
 expected = false;
 assertEquals(expected, actual);
 }
}