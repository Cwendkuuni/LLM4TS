import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.encoder.Caverphone;
import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

 private final Caverphone encoder = new Caverphone();

 @Test
 public void testEncodeNull() {
 String result = encoder.encode(null);
 assertEquals("1111111111", result);
 }

 @Test
 public void testEncodeEmpty() {
 String result = encoder.encode("");
 assertEquals("1111111111", result);
 }

 @Test
 public void testEncodeLowerCase() {
 String input = "hello";
 String result = encoder.encode(input);
 String expected = "h2h2l2o2o";
 assertEquals(expected, result);
 }

 @Test
 public void testEncodeUpperCase() {
 String input = "HELLO";
 String result = encoder.encode(input);
 String expected = "h2h2l2o2o";
 assertEquals(expected, result);
 }

 @Test
 public void testEncodeSpecialChars() {
 String input = "Caverphone Test@#$%^&*()_+={}|[]:;\"'<>,.?/";
 String result = encoder.encode(input);
 String expected = "c2a2v2e2p2h2o3n3e3t3@2#2$2%2&2*2(2_+2={2}|2[]2:2;2\">2,2.2?2/2";
 assertEquals(expected, result);
 }

 @Test
 public void testEncodeStartOptions() {
 String input1 = "Cough";
 String input2 = "Rough";
 String input3 = "Tough";
 String input4 = "Enough";
 String input5 = "Trough"; // Spec error, should be "enough"
 String result1 = encoder.encode(input1);
 String result2 = encoder.encode(input2);
 String result3 = encoder.encode(input3);
 String result4 = encoder.encode(input4);
 String result5 = encoder.encode(input5);
 String expected1 = "cou2f";
 String expected2 = "rou2f";
 String expected3 = "tou2f";
 String expected4 = "enou2f";
 String expected5 = "enou2f"; // Spec error, should be "trou2f"
 assertEquals(expected1, result1);
 assertEquals(expected2, result2);
 assertEquals(expected3, result3);
 assertEquals(expected4, result4);
 assertEquals(expected5, result5);
 }

 @Test
 public void testEncodeReplacements() {
 String input1 = "Cq";
 String input2 = "Ci";
 String input3 = "Ce";
 String input4 = "Cy";
 String input5 = "Tch";
 String input6 = "C";
 String input7 = "Q";
 String input8 = "X";
 String input9 = "V";
 String input10 = "Dg";
 String input11 = "Tio";
 String input12 = "Tia";
 String input13 = "D";
 String input14 = "Ph";
 String input15 = "B";
 String input16 = "Sh";
 String input17 = "Z";
 String input18 = "Why"; // 1.0 only, not in 2.0
 String input19 = "Wh3"; // 2.0 only
 String input20 = "W3";
 String input21 = "W";
 String input22 = "h";
 String input23 = "H";
 String input24 = "r3";
 String input25 = "r";
 String input26 = "l3";
 String input27 = "l";
 String input28 = "j";
 String input29 = "y"; // 2.0 only
 String input30 = "Y3"; // 2.0 only
 String input31 = "y"; // 2.0 only
 String input32 = "3gh3";
 String input33 = "gh";
 String input34 = "g";
 String input35 = "s+";
 String input36 = "t+";
 String input37 = "p+";
 String input38 = "k+";
 String input39 = "f+";
 String input40 = "m+";
 String input41 = "n+";
 String input42 = "w3";
 String input43 = "w$"; // 2.0 only
 String input44 = "w";
 String input45 = "3";
 String input46 = "A";
 String input47 = "R3";
 String input48 = "r$"; // 2.0 only
 String input49 = "r";
 String input50 = "L3";
 String input51 = "l$"; // 2.0 only
 String input52 = "l";
 String input53 = "y"; // 1.0 only
 String input54 = "Y3"; // 1.0 only
 String input55 = "y"; // 1.0 only
 String input56 = "2";
 String input57 = "F";
 String input58 = "M";
 String input59 = "N";
 String input60 = "S";
 String result1 = encoder.encode(input1);
 String result2 = encoder.encode(input2);
 String result3 = encoder.encode(input3);
 String result4 = encoder.encode(input4);
 String result5 = encoder.encode(input5);
 String result6 = encoder.encode(input6);
 String result7 = encoder.encode(input7);
 String result8 = encoder.encode(input8);
 String result9 = encoder.encode(input9);
 String result10 = encoder.encode(input10);
 String result11 = encoder.encode(input11);
 String result12 = encoder.encode(input12);
 String result13 = encoder.encode(input13);
 String result14 = encoder.encode(input14);
 String result15 = encoder.encode(input15);
 String result16 = encoder.encode(input16);
 String result17 = encoder.encode(input17);
 String result18 = encoder.encode(input18);
 String result19 = encoder.encode(input19);
 String result20 = encoder.encode(input20);
 String result21 = encoder.encode(input21);
 String result22 = encoder.encode(input22);
 String result23 = encoder.encode(input23);
 String result24 = encoder.encode(input24);
 String result25 = encoder.encode(input25);
 String result26 = encoder.encode(input26);
 String result27 = encoder.encode(input27);
 String result28 = encoder.encode(input28);
 String result29 = encoder.encode(input29);
 String result30 = encoder.encode(input30);
 String result31 = encoder.encode(input31);
 String result32 = encoder.encode(input32);
 String result33 = encoder.encode(input33);
 String result34 = encoder.encode(input34);
 String result35 = encoder.encode(input35);
 String result36 = encoder.encode(input36);
 String result37 = encoder.encode(input37);
 String result38 = encoder.encode(input38);
 String result39 = encoder.encode(input39);
 String result40 = encoder.encode(input40);
 String result41 = encoder.encode(input41);
 String result42 = encoder.encode(input42);
 String result43 = encoder.encode(input43);
 String result44 = encoder.encode(input44);
 String result45 = encoder.encode(input45);
 String result46 = encoder.encode(input46);
 String result47 = encoder.encode(input47);
 String result48 = encoder.encode(input48);
 String result49 = encoder.encode(input49);
 String result50 = encoder.encode(input50);
 String result51 = encoder.encode(input51);
 String result52 = encoder.encode(input52);
 String result53 = encoder.encode(input53);
 String result54 = encoder.encode(input54);
 String result55 = encoder.encode(input55);
 String result56 = encoder.encode(input56);
 String result57 = encoder.encode(input57);
 String result58 = encoder.encode(input58);
 String result59 = encoder.encode(input59);
 String result60 = encoder.encode(input60);
 String expected1 = "2q";
 String expected2 = "si";
 String expected3 = "se";
 String expected4 = "sy";
 String expected5 = "2ch";
 String expected6 = "k";
 String expected7 = "k";
 String expected8 = "k";
 String expected9 = "f";
 String expected10 = "2g";
 String expected11 = "sio";
 String expected12 = "sia";
 String expected13 = "t";
 String expected14 = "fh";
 String expected15 = "p";
 String expected16 = "s2";
 String expected17 = "s";
 String expected18 = null; // 1.0 only
 String expected19 = "W3";
 String expected20 = "W";
 String expected21 = "2";
 String expected22 = "A";
 String expected23 = "R3";
 String expected24 = "3";
 String expected25 = "2";
 String expected26 = "L3";
 String expected27 = "3";
 String expected28 = "y";
 String expected29 = "Y3";
 String expected30 = "y";
 String expected31 = null; // 1.0 only
 String expected32 = "3kh3";
 String expected33 = "22";
 String expected34 = "k";
 String expected35 = "S";
 String expected36 = "T";
 String expected37 = "P";
 String expected38 = "K";
 String expected39 = "F";
 String expected40 = "M";
 String expected41 = "N";
 String expected42 = "W3";
 String expected43 = null; // 2.0 only
 String expected44 = "3";
 String expected45 = "A";
 String expected46 = null; // 2.0 only
 String expected47 = "R3";
 String expected48 = "3";
 String expected49 = "2";
 String expected50 = "L3";
 String expected51 = null; // 2.0 only
 String expected52 = "3";
 String expected53 = "y"; // 1.0 only
 String expected54 = "Y3"; // 1.0 only
 String expected55 = "y"; // 1.0 only
 String expected56 = null;
 String expected57 = "F";
 String expected58 = "M";
 String expected59 = "N";
 String expected60 = "S";
 String expected61 = "C";
 String expected62 = "C";
 String expected63 = "s+";
 String expected64 = "t+";
 String expected65 = "p+";
 String expected66 = "k+";
 String expected67 = "f+";
 String expected68 = "m+";
 String expected69 = "n+";
 String expected70 = "W3";
 String expected71 = null; // 2.0 only
 String expected72 = "3";
 String expected73 = null; // 1.0 only
 String expected74 = "3";
 String expected75 = null; // 1.0 only
 String expected76 = null;
 String expected77 = "F";
 String expected78 = "M";
 String expected79 = "N";
 String expected80 = "S";

 for (int i = 0; i < 60; i++) {
 assertEquals(expected[(int) i], result[(int) i]);
 }
 }

 @Test
 public void testEncodeTen1s() {
 String input = "111111111111111111";
 String result = encoder.encode(input);
 String expected = input;
 assertEquals(expected, result);
 }

 @Test
 public void testEncodeFirstSixCharacters() {
 String input = "111111111112222222";
 String result = encoder.encode(input);
 String expected = input.substring(0, 10);
 assertEquals(expected, result);
 }

 @Test(expected = EncoderException.class)
 public void testEncodeNonString() {
 Object nonStringObject = new Object();
 encoder.encode(nonStringObject);
 }

}