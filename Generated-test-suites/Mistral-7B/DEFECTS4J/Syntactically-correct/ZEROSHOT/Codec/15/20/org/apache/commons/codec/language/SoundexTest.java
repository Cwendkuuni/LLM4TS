package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

import static org.apache.commons.codec.language.Soundex.US_ENGLISH_MAPPING_STRING;

public class SoundexTest {

 private final Soundex soundex = new Soundex();
 private final String input1 = "Apple";
 private final String input2 = "Orange";
 private final String input3 = "SoundexTest";
 private final String input4 = "Eins, Zwei, Drei";
 private final String input5 = "Able was I ere I saw Elba";

 @Test
 public void testUS_ENGLISH_MAPPING_STRING() {
     Assert.assertEquals("01230120022455012623010202", US_ENGLISH_MAPPING_STRING);
 }

 @Test
 public void testSoundex_defaultConstructor() {
     Assert.assertNotNull(soundex);
 }

 @Test
 public void testSoundex_defaultMapping() {
     Assert.assertEquals(soundex.soundex(input1), "A123");
     Assert.assertEquals(soundex.soundex(input2), "O156");
 }

 @Test
 public void testSoundex_customMapping() {
     final Soundex customSoundex = new Soundex(new String("1230124551262310102").toCharArray());
     Assert.assertEquals(customSoundex.soundex(input1), "A123");
     Assert.assertEquals(customSoundex.soundex(input2), "O156");
 }

 @Test
 public void testDifference() throws EncoderException {
     Assert.assertEquals(soundex.difference(input1, input2), 1);
     Assert.assertEquals(soundex.difference(input1, input3), 2);
     Assert.assertEquals(soundex.difference(input1, input4), 3);
     Assert.assertEquals(soundex.difference(input1, input5), 4);
     Assert.assertEquals(soundex.difference(input2, input3), 3);
     Assert.assertEquals(soundex.difference(input2, input4), 2);
     Assert.assertEquals(soundex.difference(input2, input5), 2);
     Assert.assertEquals(soundex.difference(input3, input4), 3);
     Assert.assertEquals(soundex.difference(input3, input5), 1);
     Assert.assertEquals(soundex.difference(input4, input5), 4);
 }

 @Test
 public void testEncode() throws EncoderException {
     Assert.assertEquals(soundex.encode(input1), "A123");
     Assert.assertEquals(soundex.encode(input2), "O156");
     Assert.assertEquals(soundex.encode(input3), "S463");
     Assert.assertEquals(soundex.encode(input4), "I123");
     Assert.assertEquals(soundex.encode(input5), "A151");
 }
}