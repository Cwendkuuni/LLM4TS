package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import java.util.Locale;

public class MetaphoneTest {

    private Metaphone metaphone;

    @Before
    public void setup() {
        metaphone = new Metaphone();
    }

    @Test
    public void testEmptyString() {
        String result = metaphone.metaphone("");
        Assert.assertEquals("", result);
    }

    @Test
    public void testSingleCharacter() {
        String result = metaphone.metaphone("A");
        Assert.assertEquals("A", result);

        result = metaphone.metaphone("Z");
        Assert.assertEquals("S", result);
    }

    @Test
    public void testBasicCases() {
        String result = metaphone.metaphone("APPLE");
        Assert.assertEquals("A1P1L1E0", result);

        result = metaphone.metaphone("BANANA");
        Assert.assertEquals("B2A1N1A1", result);

        result = metaphone.metaphone("CAT");
        Assert.assertEquals("K1T0", result);

        result = metaphone.metaphone("DOG");
        Assert.assertEquals("D2G0", result);

        result = metaphone.metaphone("ELEPHANT");
        Assert.assertEquals("E1L1P1H1A1T0", result);

        result = metaphone.metaphone("FISH");
        Assert.assertEquals("F1S1H0", result);

        result = metaphone.metaphone("GIRAFFE");
        Assert.assertEquals("G1R1A1F1F1E0", result);

        result = metaphone.metaphone("HIJKLMNOPQRSTUVWXYZ");
        Assert.assertEquals("H0I1J1K1L1M1N1P1O1Q1R1S1T1U1V1W1X1Y1Z0", result);
    }

    @Test
    public void testComplexCases() {
        String result = metaphone.metaphone("APPALOOSA");
        Assert.assertEquals("A1P1L1O1S1A0", result);

        result = metaphone.metaphone("BANANAS");
        Assert.assertEquals("B2A1N1A1S0", result);

        result = metaphone.metaphone("CATALOGUE");
        Assert.assertEquals("K1T1A1L1O1G1E0", result);

        result = metaphone.metaphone("DOCTOR");
        Assert.assertEquals("D2T1O1R0", result);

        result = metaphone.metaphone("ELEPHANTINE");
        Assert.assertEquals("E1L1P1H1A1N1T1I0E0", result);

        result = metaphone.metaphone("FISHING");
        Assert.assertEquals("F1S1H1I1N1G0", result);

        result = metaphone.metaphone("GIRaffe");
        Assert.assertEquals("G1R1A1F1F1E0", result);

        result = metaphone.metaphone("HIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Assert.assertEquals("H0I1J1K1L1M1N1P1O1R1S1T1U1V1W1X1Y1Z0A1B1C1D1E1F1G1H1I1J1K1L1M1N1O1P1Q1R1S1T1U1V1W1X1Y1Z0", result);
    }

    @Test(expected = EncoderException.class)
    public void testNullInput() {
        metaphone.metaphone(null);
    }

    @Test
    public void testMaxCodeLen() {
        String result = metaphone.metaphone("APPLE", 1);
        Assert.assertEquals("A1P1L1E", result);
    }

    @Test
    public void testMaxCodeLen2() {
        String result = metaphone.metaphone("APPLE", 5);
        Assert.assertEquals("A1P1L1E0A1", result);
    }
}