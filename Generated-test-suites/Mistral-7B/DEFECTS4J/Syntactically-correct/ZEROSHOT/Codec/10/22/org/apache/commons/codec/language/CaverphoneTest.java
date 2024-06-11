import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class CaverphoneTest {

 private Caverphone caverphone = new Caverphone();

 @Test
 public void testCaverphoneNull() {
 try {
 caverphone.caverphone(null);
 } catch (NullPointerException e) {
 // Expected since the method has a null check
 }
 }

 @Test
 public void testCaverphoneEmpty() {
 String result = caverphone.caverphone("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testCaverphoneLowerCase() {
 String result = caverphone.caverphone("hello world");
 assertEquals("helloworl", result);
 }

 @Test
 public void testCaverphoneRemoveNonLetters() {
 String result = caverphone.caverphone("123hello45world67");
 assertEquals("helloworl", result);
 }

 @Test
 public void testCaverphoneRemoveFinalE() {
 String result = caverphone.caverphone("enough");
 assertEquals("enou2f", result);
 }

 @Test
 public void testCaverphoneStartHandlers() {
 String result = caverphone.caverphone("coughenough");
 assertEquals("cou2fenou2f", result);
 String result2 = caverphone.caverphone("tough");
 assertEquals("tou2f", result2);
 String result3 = caverphone.caverphone("trough");
 assertEquals("trou2f", result3);
 String result4 = caverphone.caverphone("gn");
 assertEquals("2n", result4);
 }

 @Test
 public void testCaverphoneReplace() {
 String result = caverphone.caverphone("cq");
 assertEquals("2q", result);
 String result2 = caverphone.caverphone("ci");
 assertEquals("si", result2);
 String result3 = caverphone.caverphone("ce");
 assertEquals("se", result3);
 String result4 = caverphone.caverphone("cy");
 assertEquals("sy", result4);
 String result5 = caverphone.caverphone("tch");
 assertEquals("2ch", result5);
 String result6 = caverphone.caverphone("c");
 assertEquals("k", result6);
 String result7 = caverphone.caverphone("q");
 assertEquals("k", result7);
 String result8 = caverphone.caverphone("x");
 assertEquals("k", result8);
 String result9 = caverphone.caverphone("v");
 assertEquals("f", result9);
 String result10 = caverphone.caverphone("dg");
 assertEquals("2g", result10);
 String result11 = caverphone.caverphone("tio");
 assertEquals("sio", result11);
 String result12 = caverphone.caverphone("tia");
 assertEquals("sia", result12);
 String result13 = caverphone.caverphone("d");
 assertEquals("t", result13);
 String result14 = caverphone.caverphone("ph");
 assertEquals("fh", result14);
 String result15 = caverphone.caverphone("b");
 assertEquals("p", result15);
 String result16 = caverphone.caverphone("sh");
 assertEquals("s2", result16);
 String result17 = caverphone.caverphone("z");
 assertEquals("s", result17);
 String result18 = caverphone.caverphone("y3");
 assertEquals("Y3", result18);
 String result19 = caverphone.caverphone("gh");
 assertEquals("22", result19);
 String result20 = caverphone.caverphone("g");
 assertEquals("k", result20);
 String result21 = caverphone.caverphone("s+");
 assertEquals("S", result21);
 String result22 = caverphone.caverphone("t+");
 assertEquals("T", result22);
 String result23 = caverphone.caverphone("p+");
 assertEquals("P", result23);
 String result24 = caverphone.caverphone("k+");
 assertEquals("K", result24);
 String result25 = caverphone.caverphone("f+");
 assertEquals("F", result25);
 String result26 = caverphone.caverphone("m+");
 assertEquals("M", result26);
 String result27 = caverphone.caverphone("n+");
 assertEquals("N", result27);
 String result28 = caverphone.caverphone("w3");
 assertEquals("W3", result28);
 // String result29 = caverphone.caverphone("wy"); // 1.0 only
 String result30 = caverphone.caverphone("wh3");
 assertEquals("Wh3", result30);
 String result31 = caverphone.caverphone("w$");
 assertEquals("3", result31);
 // String result32 = caverphone.caverphone("why"); // 1.0 only
 String result33 = caverphone.caverphone("w");
 assertEquals("2", result33);
 String result34 = caverphone.caverphone("^h");
 assertEquals("A", result34);
 String result35 = caverphone.caverphone("h");
 assertEquals("2", result35);
 String result36 = caverphone.caverphone("r3");
 assertEquals("R3", result36);
 String result37 = caverphone.caverphone("r$");
 assertEquals("3", result37);
 // String result38 = caverphone.caverphone("ry"); // 1.0 only
 String result39 = caverphone.caverphone("r");
 assertEquals("2", result39);
 String result40 = caverphone.caverphone("l3");
 assertEquals("L3", result40);
 String result41 = caverphone.caverphone("l$");
 assertEquals("3", result41);
 // String result42 = caverphone.caverphone("ly"); // 1.0 only
 String result43 = caverphone.caverphone("l");
 assertEquals("2", result43);
 // String result44 = caverphone.caverphone("j"); // 1.0 only
 // String result45 = caverphone.caverphone("y3"); // 1.0 only
 // String result46 = caverphone.caverphone("y"); // 1.0 only
 String result47 = caverphone.caverphone("y");
 assertEquals("3", result47);
 String result48 = caverphone.caverphone("3gh3");
 assertEquals("3kh3", result48);
 String result49 = caverphone.caverphone("gh");
 assertEquals("22", result49);
 String result50 = caverphone.caverphone("g");
 assertEquals("k", result50);
 }

 @Test
 public void testCaverphoneRemove() {
 String result = caverphone.caverphone("2");
 assertEquals("", result);
 String result2 = caverphone.caverphone("3$");
 assertEquals("A", result2);
 String result3 = caverphone.caverphone("3");
 assertEquals("", result3);
 }

 @Test
 public void testCaverphoneEndingOnes() {
 String result = caverphone.caverphone("111111hello1111");
 assertEquals("helloworl1111111111", result);
 }

 @Test
 public void testCaverphoneFirstSixChars() {
 String result = caverphone.caverphone("111111hello111111");
 assertEquals("helloworl", result.substring(0, 10));
 }

 @Test(expected = DecoderException.class)
 public void testDecodeException() {
 String encoded = caverphone.caverphone("test");
 caverphone.decode(encoded.substring(0, 3) + "X");
 }

}