package org.junit.jupiter.api;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.ASSume.assumeFalse;
import static org.junit.jupiter.api.ASSume.assumeTrue;
import static org.jsoup.parser.CharacterReader.EOF;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CharacterReaderTest {
    @Test
    void testisEmpty_emptyString() {
        CharacterReader reader = new CharacterReader("");
        assertTrue(reader.isEmpty());
    }

    @Test
    void testisEmpty_nonEmptyString() {
        CharacterReader reader = new CharacterReader("abc");
        assertFalse(reader.isEmpty());
    }

    @Test
    void testpos_emptyString() {
        CharacterReader reader = new CharacterReader("");
        assertEquals(0, reader.pos());
    }

    @Test
    void testpos_nonEmptyString() {
        CharacterReader reader = new CharacterReader("abc");
        assertEquals(3, reader.pos());
    }

    // ... Test methods for other public methods ...

}