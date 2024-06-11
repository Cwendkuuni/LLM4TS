package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphoneEncodedStringsAreEqual() {
        Metaphone encoder = new Metaphone();
        String string1 = "Metaphone";
        String string2 = "MetaphoneAlgorithm";
        String result1 = encoder.metaphone(string1);
        String result2 = encoder.metaphone(string2);
        assertTrue(encoder.isMetaphoneEqual(string1, string2) ||
                result1.equals(result2));
    }

    @Test
    public void testMetaphoneEncodedEmptyStringIsEmptyString() {
        Metaphone encoder = new Metaphone();
        String string = "";
        String result = encoder.metaphone(string);
        assertEquals("", result);
    }

    @Test
    public void testMetaphoneEncodedNullStringIsEmptyString() {
        Metaphone encoder = new Metaphone();
        String string = null;
        String result = encoder.metaphone(string);
        assertEquals("", result);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneEncodedInvalidInputThrowsEncoderException() {
        Metaphone encoder = new Metaphone();
        String string = "Metaphone123";
        encoder.metaphone(string);
    }

    @Test
    public void testMetaphoneEncodedWordsWithInitialExceptions() {
        Metaphone encoder = new Metaphone();
        String string1 = "Apple";
        String string2 = "Orange";
        String string3 = "Kangaroo";
        String string4 = "Kitten";
        String result1 = encoder.metaphone(string1);
        String result2 = encoder.metaphone(string2);
        String result3 = encoder.metaphone(string3);
        String result4 = encoder.metaphone(string4);
        assertEquals("APE", result1);
        assertEquals("OWG", result2);
        assertEquals("AOOS", result3);
        assertEquals("KAE", result4);
    }

    @Test
    public void testMetaphoneEncodedWordsWithInitialExceptionsAndFollowingVowels() {
        Metaphone encoder = new Metaphone();
        String string1 = "Quack";
        String string2 = "Quagmire";
        String result1 = encoder.metaphone(string1);
        String result2 = encoder.metaphone(string2);
        assertEquals("KWAK", result1);
        assertEquals("KWAG", result2);
    }

    @Test
    public void testMetaphoneEncodedWordsWithInitialExceptionsAndFollowingConsonsants() {
        Metaphone encoder = new Metaphone();
        String string1 = "Splash";
        String string2 = "Bamboozle";
        String result1 = encoder.metaphone(string1);
        String result2 = encoder.metaphone(string2);
        assertEquals("S Pl", result1);
        assertEquals("BAM", result2);
    }

    @Test
    public void testMetaphoneEncodedWordsWithSilentGh() {
        Metaphone encoder = new Metaphone();
        String string1 = "Bachelor";
        String string2 = "Bachelorhood";
        String result1 = encoder.metaphone(string1);
        String result2 = encoder.metaphone(string2);
        assertEquals("BAA[T[R", result1);
        assertEquals("BAA[T[R", result2);
    }

    @Test
    public void testMetaphoneEncodedWordsWithSilentG() {
        Metaphone encoder = new Metaphone();
        String string1 = "Gentle";
        String string2 = "Gentleman";
        String result1 = encoder.metaphone(string1);
        String result2 = encoder.metaphone(string2);
        assertEquals("JENTL", result1);
        assertEquals("JEN[TLM]", result2);
    }

    @Test
    public void testMetaphoneEncodedWordsWithSilentCH() {
        Metaphone encoder = new Metaphone();
        String string1 = "Bach";
        String string2 = "Bachlor";
        String result1 = encoder.metaphone(string1);
        String result2 = encoder.metaphone(string2);
        assertEquals("B[X", result1);
        assertEquals("B[X", result2);
    }

    @Test
    public void testMetaphoneEncodedWordsWithSilentTH() {
        Metaphone encoder = new Metaphone();
        String string1 = "The";
        String string2 = "Theater";
        String result1 = encoder.metaphone(string1);
        String result2 = encoder.metaphone(string2);
        assertEquals("T0", result1);
        assertEquals("T0", result2);
    }

    @Test
    public void testMetaphoneEncodedWordsWithDifferentLength() {
        Metaphone encoder = new Metaphone();
        String string1 = "Apple";
        String string2 = "Application";
        String result1 = encoder.metaphone(string1);
        String result2 = encoder.metaphone(string2);
        assertEquals("APE", result1);
        assertEquals("A[PL[S[N]", result2);
    }

    @Test
    public void testMetaphoneEncodedWordsWithMultipleExceptions() {
        Metaphone encoder = new Metaphone();
        String string1 = "Eggplant";
        String string2 = "Eggplantation";
        String result1 = encoder.metaphone(string1);
        String result2 = encoder.metaphone(string2);
        assertEquals("EGGPLT", result1);
        assertEquals("EGGPLT[S[N]", result2);
    }

    @Test
    public void testMetaphoneEncodedWordsWithMultipleExceptionsAndFollowingVowels() {
        Metaphone encoder = new Metaphone();
        String string1 = "QuackQuack";
        String string2 = "QuagmireQuagmire";
        String result1 = encoder.metaphone(string1);
        String result2 = encoder.metaphone(string2);
        assertEquals("KWAKKWAK", result1);
        assertEquals("KWAGWAG", result2);
    }

    @Test
    public void testMetaphoneEncodedWordsWithMultipleExceptionsAndFollowingConsonsants() {
        Metaphone encoder = new Metaphone();
        String string1 = "SplashSplash";
        String string2 = "BamboozleBamboozle";
        String result1 = encoder.metaphone(string1);
        String result2 = encoder.metaphone(string2);
        assertEquals("S PlPl", result1);
        assertEquals("BAMBAM", result2);
    }

    @Test
    public void testMetaphoneEncodedWordsWithDifferentVowels() {
        Metaphone encoder = new Metaphone();
        String string1 = "Egg";
        String string2 = "Apple";
        String result1 = encoder.metaphone(string1);
        String result2 = encoder.metaphone(string2);
        assertEquals("EGG", result1);
        assertEquals("APE", result2);
    }

    @Test
    public void testMetaphoneEncodedWordsWithDifferentVowelsAndFollowingConsonsants() {
        Metaphone encoder = new Metaphone();
        String string1 = "Eggplant";
        String string2 = "Application";
        String result1 = encoder.metaphone(string1);
        String result2 = encoder.metaphone(string2);
        assertEquals("EGGPLT", result1);
        assertEquals("A[PL[S[N]", result2);
    }

    @Test
    public void testMetaphoneEncodedWordsWithDifferentVowelsAndFollowingVowels() {
        Metaphone encoder = new Metaphone();
        String string1 = "QuackQuack";
        String string2 = "QuagmireQuagmire";
        String result1 = encoder.metaphone(string1);
        String result2 = encoder.metaphone(string2);
        assertEquals("KWAKKWAK", result1);
        assertEquals("KWAGWAG", result2);
    }

    @Test
    public void testMetaphoneEncodedWordsWithDifferentVowelsAndFollowingConsonsantsAndInitialExceptions() {
        Metaphone encoder = new Metaphone();
        String string1 = "QuackQuack";
        String string2 = "QuagmireQuagmire";
        String string3 = "BachBach";
        String string4 = "BamboozleBamboozle";
        String result1 = encoder.metaphone(string1);
        String result2 = encoder.metaphone(string2);
        String result3 = encoder.metaphone(string3);
        String result4 = encoder.metaphone(string4);
        assertEquals("KWAKKWAK", result1);
        assertEquals("KWAGWAG", result2);
        assertEquals("B[XB[X", result3);
        assertEquals("BAMBAM", result4);
    }
}