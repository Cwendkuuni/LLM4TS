package org.jsoup.parser.test;

import org.junit.Test;
import org.junit.Assert;
import static org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructor() {
        String input = "abcdef";
        CharacterReader reader = new CharacterReader(input);
        assertEquals(5, reader.length());
        assertEquals(0, reader.pos());
        assertEquals(false, reader.isEmpty());
    }

    @Test
    public void testCharacterReaderEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testCharacterReaderNullInput() {
        new CharacterReader(null);
    }

    // Test other methods with basic cases here...

    // Test edge cases and exceptions here...

}