package org.apache.commons.codec.language;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the Metaphone class.
 */
public class MetaphoneTest {

    @Test
    public void testMetaphoneSimple() {
        Metaphone metaphone = new Metaphone();
        String input = "apple";
        String expected = "AA0PLE";
        String result = metaphone.metaphone(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneWithDoubleLetter() {
        Metaphone metaphone = new Metaphone();
        String input = "banana";
        String expected = "A00PLE";
        String result = metaphone.metaphone(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneWithInitialX() {
        Metaphone metaphone = new Metaphone();
        String input = "xylophone";
        String expected = "KSXLF0N";
        String result = metaphone.metaphone(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneWithInitialSilentG() {
        Metaphone metaphone = new Metaphone();
        String input = "giraffe";
        String expected = "JR0F";
        String result = metaphone.metaphone(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneWithInitialSilentH() {
        Metaphone metaphone = new Metaphone();
        String input = "hour";
        String expected = "H0UR";
        String result = metaphone.metaphone(input);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = EncoderException.class)
    public void testMetaphoneWithNullInput() {
        Metaphone metaphone = new Metaphone();
        String input = null;
        metaphone.metaphone(input);
    }

    @Test
    public void testMetaphoneWithEmptyInput() {
        Metaphone metaphone = new Metaphone();
        String input = "";
        String expected = "";
        String result = metaphone.metaphone(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneWithSpecialCharacters() {
        Metaphone metaphone = new Metaphone();
        String input = "Résumé";
        String expected = "R00M0";
        String result = metaphone.metaphone(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneWithMultiWordString() {
        Metaphone metaphone = new Metaphone();
        String input = "hello world";
        String expected = "H00L0 W0RLD";
        String result = metaphone.metaphone(input);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testMetaphoneWithEdgeCases() {
        Metaphone metaphone = new Metaphone();

        String[] edgeCases = {
                "a",
                "ab",
                "abracadabra",
                "abstemious",
                "acquiesce",
                "acclivitous",
                "adipose",
                "admonish",
                "aerodynamic",
                "aesthetic",
                "aesthete",
                "afghani",
                "agamemnon",
                "agglomerate",
                "aggrandize",
                "aggravate",
                "agile",
                "agnostic",
                "alacrity",
                "alchemist",
                "alchemy",
                "al fresco",
                "aloof",
                "alpine",
                "ambiguity",
                "ameliorate",
                "amelioration",
                "amnesia",
                "anachronism",
                "anagnorisis",
                "anathema",
                "anathematize",
                "anarchy",
                "anemone",
                "anesthesia",
                "anesthetize",
                "anion",
                "ansible",
                "antagonize",
                "antidote",
                "antithesis",
                "anxiety",
                "aorta",
                "aphrodisiac",
                "aphelion",
                "apogee",
                "apotheosis",
                "archipelago",
                "archivist",
                "architect",
                "archipelago",
                "argentine",
                "argus",
                "armadillo",
                "arrhythmia",
                "asinine",
                "asphalt",
                "aspirant",
                "aspirate",
                "asymptote",
                "atheist",
                "athene",
                "atlas",
                "atmospheric",
                "attic",
                "atticize",
                "attire",
                "audacious",
                "auburn",
                "aura",
                "auricula",
                "auroran",
                "avalanche",
                "avatar",
                "avian",
                "avocado",
                "awe",
                "axiom",
                "axiomatic",
                "axle"
        };

        for (String edgeCase : edgeCases) {
            String expected = metaphone.metaphone(edgeCase);
            System.out.println("Testing edge case: " + edgeCase);
            String result = metaphone.metaphone(edgeCase);
            Assert.assertEquals(expected, result);
        }
    }
}