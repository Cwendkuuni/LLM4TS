import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import org.junit.Assert.*;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testNullInput() {
  String expected = "1111111111";
  String actual = caverphone.caverphone(null);
  assertEquals(expected, actual);
 }

 @Test
 public void testEmptyInput() {
  String expected = "1111111111";
  String actual = caverphone.caverphone("");
  assertEquals(expected, actual);
 }

 @Test
 public void testLowerCase() {
  String txt = "this is a test";
  String expected = "T2h2i2s2i2s2a2t2e2s2t2";
  String actual = caverphone.caverphone(txt);
  assertEquals(expected, actual);
 }

 @Test
 public void testRemoveNonAZ() {
  String txt = "1234abc567defghijkl";
  String expected = "akjl";
  String actual = caverphone.caverphone(txt);
  assertEquals(expected, actual);
 }

 @Test
 public void testRemoveFinalE() {
  String txt = "enough";
  String expected = "enou2f";
  String actual = caverphone.caverphone(txt);
  assertEquals(expected, actual);
 }

 @Test
 public void testHandleStartOptions() {
  String txt = "cough";
  String expected = "cou2f";
  String actual = caverphone.caverphone(txt);
  assertEquals(expected, actual);

  String txt2 = "rough";
  String expected2 = "rou2f";
  String actual2 = caverphone.caverphone(txt2);
  assertEquals(expected2, actual2);

  String txt3 = "tough";
  String expected3 = "tou2f";
  String actual3 = caverphone.caverphone(txt3);
  assertEquals(expected3, actual3);

  String txt4 = "enough";
  String expected4 = "enou2f";
  String actual4 = caverphone.caverphone(txt4);
  assertEquals(expected4, actual4);

  String txt5 = "trough";
  String expected5 = "trou2f";
  String actual5 = caverphone.caverphone(txt5);
  assertEquals(expected5, actual5);

  String txt6 = "gn";
  String expected6 = "2n";
  String actual6 = caverphone.caverphone(txt6);
  assertEquals(expected6, actual6);
 }

 @Test
 public void testHandleReplacements() {
  String txt = "caquachid";
  String expected = "2qsi2d";
  String actual = caverphone.caverphone(txt);
  assertEquals(expected, actual);

  String txt2 = "citizen";
  String expected2 = "si2zen";
  String actual2 = caverphone.caverphone(txt2);
  assertEquals(expected2, actual2);

  String txt3 = "ceizo";
  String expected3 = "seizo";
  String actual3 = caverphone.caverphone(txt3);
  assertEquals(expected3, actual3);

  String txt4 = "cyclone";
  String expected4 = "syclone";
  String actual4 = caverphone.caverphone(txt4);
  assertEquals(expected4, actual4);

  String txt5 = "tchisel";
  String expected5 = "2chisel";
  String actual5 = caverphone.caverphone(txt5);
  assertEquals(expected5, actual5);

  String txt6 = "cat";
  String expected6 = "k";
  String actual6 = caverphone.caverphone(txt6);
  assertEquals(expected6, actual6);

  String txt7 = "quiet";
  String expected7 = "kiet";
  String actual7 = caverphone.caverphone(txt7);
  assertEquals(expected7, actual7);

  String txt8 = "expert";
  String expected8 = "f";
  String actual8 = caverphone.caverphone(txt8);
  assertEquals(expected8, actual8);

  String txt9 = "victory";
  String expected9 = "fh";
  String actual9 = caverphone.caverphone(txt9);
  assertEquals(expected9, actual9);

  String txt10 = "bubble";
  String expected10 = "p";
  String actual10 = caverphone.caverphone(txt10);
  assertEquals(expected10, actual10);

  String txt11 = "shoes";
  String expected11 = "s2";
  String actual11 = caverphone.caverphone(txt11);
  assertEquals(expected11, actual11);

  String txt12 = "zebra";
  String expected12 = "s";
  String actual12 = caverphone.caverphone(txt12);
  assertEquals(expected12, actual12);

  String txt13 = "apple";
  String expected13 = "A";
  String actual13 = caverphone.caverphone(txt13);
  assertEquals(expected13, actual13);

  String txt14 = "elephant";
  String expected14 = "E3";
  String actual14 = caverphone.caverphone(txt14);
  assertEquals(expected14, actual14);

  String txt15 = "indigo";
  String expected15 = "i3";
  String actual15 = caverphone.caverphone(txt15);
  assertEquals(expected15, actual15);

  String txt16 = "jungle";
  String expected16 = "y";
  String actual16 = caverphone.caverphone(txt16);
  assertEquals(expected16, actual16);

  String txt17 = "why";
  String expected17 = "Why";
  String actual17 = caverphone.caverphone(txt17);
  assertEquals(expected17, actual17);

  String txt18 = "why3";
  String expected18 = "Why3";
  String actual18 = caverphone.caverphone(txt18);
  assertEquals(expected18, actual18);

  String txt19 = "whyjungle";
  String expected19 = "Whyjungle";
  String actual19 = caverphone.caverphone(txt19);
  assertEquals(expected19, actual19);

  String txt20 = "whyjungleWhy";
  String expected20 = "WhyjungleWhy";
  String actual20 = caverphone.caverphone(txt20);
  assertEquals(expected20, actual20);
 }

 @Test
 public void testReplaceFinalR() {
  String txt = "rough";
  String expected = "rou2f";
  String actual = caverphone.caverphone(txt);
  assertEquals(expected, actual);
 }

 @Test
 public void testReplaceFinalRw() {
  String txt = "w3";
  String expected = "W3";
  String actual = caverphone.caverphone(txt);
  assertEquals(expected, actual);
 }

 @Test
 public void testHandleRemovals() {
  String txt = "22";
  String expected = "";
  String actual = caverphone.caverphone(txt);
  assertEquals(expected, actual);

  String txt2 = "A";
  String expected2 = "";
  String actual2 = caverphone.caverphone(txt2);
  assertEquals(expected2, actual2);

  String txt3 = "3";
  String expected3 = "";
  String actual3 = caverphone.caverphone(txt3);
  assertEquals(expected3, actual3);
 }

 @Test
 public void testAddOnes() {
  String txt = "test";
  String expected = "T2e2s2t2";
  String actual = caverphone.caverphone(txt);
  assertEquals(expected, actual);
 }

 @Test
 public void testTakeFirstSix() {
  String txt = "this iss a test";
  String expected = "T2h2i2s2i2s2a2t2";
  String actual = caverphone.caverphone(txt).substring(0, 10);
  assertEquals(expected, actual);
 }

 @Test
 public void testEncodeString() throws EncoderException {
  String str = "this is a test";
  String expected = "T2h2i2s2i2s2a2t2";
  String actual = (String) caverphone.encode(str);
  assertEquals(expected, actual);
 }

 @Test
 public void testEncodeObject() throws EncoderException {
  Object obj = "this is a test";
  String expected = "T2h2i2s2i2s2a2t2";
  String actual = (String) caverphone.encode(obj);
  assertEquals(expected, actual);
 }

 @Test
 public void testIsCaverphoneEqual() {
  String str1 = "this is a test";
  String str2 = "this is a test";
  boolean expected = true;
  boolean actual = caverphone.isCaverphoneEqual(str1, str2);
  assertEquals(expected, actual);
 }

 @Test
 public void testIsCaverphoneNotEqual() {
  String str1 = "this is a test";
  String str2 = "this is a different test";
  boolean expected = false;
  boolean actual = caverphone.isCaverphoneEqual(str1, str2);
  assertEquals(expected, actual);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeInvalidInput() throws EncoderException {
  int num = 10;
  String actual = (String) caverphone.encode(num);
  fail("Should throw EncoderException for invalid input");
 }
}