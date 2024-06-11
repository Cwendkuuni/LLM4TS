import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphoneInstance = new Caverphone();

 @Test
 public void testEmptyString() {
 String expected = "1111111111";
 String actual = caverphoneInstance.caverphone("");
 assertEquals(expected, actual);
 }

 @Test
 public void testNullString() {
 try {
 caverphoneInstance.caverphone(null);
 fail("Expected EncoderException when trying to encode a null String");
 } catch (EncoderException e) {
 // pass
 }
 }

 @Test
 public void testLowerCaseString() {
 String input = "hello world!";
 String expected = "h2h2l2l2o2o2w2w2d2d2";
 String actual = caverphoneInstance.caverphone(input.toLowerCase());
 assertEquals(expected, actual);
 }

 @Test
 public void testRemoveNonAlphabets() {
 String input = "123hello45world!67";
 String expected = "helloworld";
 String actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testRemoveFinalE() {
 String input = "enough";
 String expected = "enou2f";
 String actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testHandlesStartOptions() {
 String input = "cough";
 String expected = "cou2f";
 String actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "rough";
 expected = "rou2f";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "tough";
 expected = "tou2f";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "enough";
 expected = "enou2f";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "trough";
 expected = "trou2f";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "gn";
 expected = "2n";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testHandlesReplacements() {
 String input = "cq";
 String expected = "2q";
 String actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "ci";
 expected = "si";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "ce";
 expected = "se";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "cy";
 expected = "sy";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "tch";
 expected = "2ch";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "c";
 expected = "k";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "q";
 expected = "k";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "x";
 expected = "k";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "v";
 expected = "f";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "dg";
 expected = "2g";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "tio";
 expected = "sio";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "tia";
 expected = "sia";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "d";
 expected = "t";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "ph";
 expected = "fh";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "b";
 expected = "p";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "sh";
 expected = "s2";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "z";
 expected = "s";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "^[aeiou]";
 expected = "A";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "[aeiou]";
 expected = "3";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "j";
 expected = "y";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "^y3";
 expected = "Y3";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "^y";
 expected = "A";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "y";
 expected = "3";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "3gh3";
 expected = "3kh3";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "gh";
 expected = "22";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "g";
 expected = "k";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "s+";
 expected = "S";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "t+";
 expected = "T";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "p+";
 expected = "P";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "k+";
 expected = "K";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "f+";
 expected = "F";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "m+";
 expected = "M";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "n+";
 expected = "N";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "w3";
 expected = "W3";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "wy";
 // Uncomment this test case if you want to support caverphone version 1.0
 // expected = "Wy";
 // actual = caverphoneInstance.caverphone(input);
 // assertEquals(expected, actual);

 input = "wh3";
 expected = "Wh3";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "w$";
 // Uncomment this test case if you want to support caverphone version 2.0
 // expected = "3";
 // actual = caverphoneInstance.caverphone(input);
 // assertEquals(expected, actual);

 input = "why";
 // Uncomment this test case if you want to support caverphone version 1.0
 // expected = "Why";
 // actual = caverphoneInstance.caverphone(input);
 // assertEquals(expected, actual);

 input = "w";
 expected = "2";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "^h";
 expected = "A";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "h";
 expected = "2";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "r3";
 expected = "R3";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "r$";
 // Uncomment this test case if you want to support caverphone version 2.0
 // expected = "3";
 // actual = caverphoneInstance.caverphone(input);
 // assertEquals(expected, actual);

 input = "ry";
 // Uncomment this test case if you want to support caverphone version 1.0
 // expected = "Ry";
 // actual = caverphoneInstance.caverphone(input);
 // assertEquals(expected, actual);

 input = "r";
 expected = "2";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "l3";
 expected = "L3";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "l$";
 // Uncomment this test case if you want to support caverphone version 2.0
 // expected = "3";
 // actual = caverphoneInstance.caverphone(input);
 // assertEquals(expected, actual);

 input = "ly";
 // Uncomment this test case if you want to support caverphone version 1.0
 // expected = "Ly";
 // actual = caverphoneInstance.caverphone(input);
 // assertEquals(expected, actual);

 input = "l";
 expected = "2";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "j";
 expected = "y";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "y3";
 expected = "Y3";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "y";
 expected = "2";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testHandlesRemovals() {
 String input = "2";
 String expected = "";
 String actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);

 input = "3$";
 // Uncomment this test case if you want to support caverphone version 2.0
 // expected = "A";
 // actual = caverphoneInstance.caverphone(input);
 // assertEquals(expected, actual);

 input = "3";
 expected = "";
 actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual);
 }

 @Test
 public void testHandlesTen1sOnTheEnd() {
 String input = "abcdefghijklmnopqrstuvwxyz";
 String expected = "h2h2l2l2o2o2w2w2d2d2";
 String actual = caverphoneInstance.caverphone(input);
 assertEquals(expected, actual.substring(0, 10));
 }
}