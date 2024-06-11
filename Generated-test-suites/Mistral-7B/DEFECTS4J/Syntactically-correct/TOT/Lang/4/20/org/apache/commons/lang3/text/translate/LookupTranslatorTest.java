package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.CharSequenceUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link LookupTranslator}.
 */
public class LookupTranslatorTest {

    @Test
    public void testEmptyLookup() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        CharSequence input = "";
        CharSequence expected = "";
        try (Writer out = new StringBuilder()) {
            translator.translate(input, 0, out);
            CharSequence result = out.toString();
            assertEquals(expected, result);
        } catch (IOException e) {
            fail("IOException should not be thrown for empty lookup table");
        }
    }

    @Test
    public void testSingleKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}});
        CharSequence input = "hello";
        CharSequence expected = "world";
        try (Writer out = new StringBuilder()) {
            translator.translate(input, 0, out);
            CharSequence result = out.toString();
            assertEquals(expected, result);
        } catch (IOException e) {
            fail("IOException should not be thrown for single key lookup");
        }
    }

    @Test
    public void testMultipleKeys() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}, {"goodbye", "farewell"}});
        CharSequence input = "hello";
        CharSequence expected = "world";
        try (Writer out = new StringBuilder()) {
            translator.translate(input, 0, out);
            CharSequence result = out.toString();
            assertEquals(expected, result);
        } catch (IOException e) {
            fail("IOException should not be thrown for multiple keys lookup");
        }

        CharSequence input2 = "goodbye";
        CharSequence expected2 = "farewell";
        try (Writer out = new StringBuilder()) {
            translator.translate(input2, 0, out);
            CharSequence result = out.toString();
            assertEquals(expected2, result);
        } catch (IOException e) {
            fail("IOException should not be thrown for multiple keys lookup");
        }
    }

    @Test
    public void testKeyLongerThanValue() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hellohellohello", "world"}});
        CharSequence input = "hellohellohello";
        CharSequence expected = "world";
        try (Writer out = new StringBuilder()) {
            translator.translate(input, 0, out);
            CharSequence result = out.toString();
            assertEquals(expected, result);
        } catch (IOException e) {
            fail("IOException should not be thrown for key longer than value lookup");
        }
    }

    @Test
    public void testKeyShorterThanValue() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "helloworld"}});
        CharSequence input = "hello";
        CharSequence expected = "helloworld";
        try (Writer out = new StringBuilder()) {
            translator.translate(input, 0, out);
            CharSequence result = out.toString();
            assertEquals(expected, result);
        } catch (IOException e) {
            fail("IOException should not be thrown for key shorter than value lookup");
        }
    }

    @Test
    public void testKeyPartOfValue() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hellohello", "worldhello"}});
        CharSequence input = "hellohello";
        CharSequence expected = "worldhello";
        try (Writer out = new StringBuilder()) {
            translator.translate(input, 0, out);
            CharSequence result = out.toString();
            assertEquals(expected, result);
        } catch (IOException e) {
            fail("IOException should not be thrown for key part of value lookup");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}});
        CharSequence input = null;
        try (Writer out = new StringBuilder()) {
            translator.translate(input, 0, out);
        } catch (IOException e) {
            fail("IOException should not be thrown for null input");
        }
        translator.translate(input, 0, null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBounds() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}});
        CharSequence input = "helloworld";
        try (Writer out = new StringBuilder()) {
            translator.translate(input, 5, out);
        } catch (IOException e) {
            fail("IOException should not be thrown for index out of bounds lookup");
        }
    }

    @Test(expected = IOException.class)
    public void testWriteError() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}});
        CharSequence input = "hello";
        Writer out = new Writer() {
            @Override
            public void write(int c) throws IOException {
                throw new IOException();
            }

            @Override
            public void write(char[] c, int off, int len) throws IOException {
                throw new IOException();
            }

            @Override
            public void flush() throws IOException {
            }

            @Override
            public void close() throws IOException {
            }
        };
        try {
            translator.translate(input, 0, out);
            fail("IOException should be thrown for write error");
        } catch (IOException e) {
        }
    }

    @Test
    public void testEmptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}});
        CharSequence input = "";
        CharSequence expected = "";
        try (Writer out = new StringBuilder()) {
            translator.translate(input, 0, out);
            CharSequence result = out.toString();
            assertEquals(expected, result);
        } catch (IOException e) {
            fail("IOException should not be thrown for empty input");
        }
    }

    @Test
    public void testInputLongerThanLookup() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hello", "world"}, {"hello", "worldhello"}});
        CharSequence input = "helloworld";
        CharSequence expected = "worldhello";
        try (Writer out = new StringBuilder()) {
            translator.translate(input, 0, out);
            CharSequence result = out.toString();
            assertEquals(expected, result);
        } catch (IOException e) {
            fail("IOException should not be thrown for input longer than lookup");
        }
    }

    @Test
    public void testInputShorterThanShortestKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hellohello", "world"}, {"helloworld", "worldhello"}});
        CharSequence input = "hello";
        CharSequence expected = "";
        try (Writer out = new StringBuilder()) {
            translator.translate(input, 0, out);
            CharSequence result = out.toString();
            assertEquals(expected, result);
        } catch (IOException e) {
            fail("IOException should not be thrown for input shorter than shortest key");
        }
    }

    @Test
    public void testInputPartOfShortestKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hellohello", "world"}, {"helloworld", "worldhello"}});
        CharSequence input = "hello";
        CharSequence expected = "world";
        try (Writer out = new StringBuilder()) {
            translator.translate(input, 0, out);
            CharSequence result = out.toString();
            assertEquals(expected, result);
        } catch (IOException e) {
            fail("IOException should not be thrown for input part of shortest key");
        }
    }

    @Test
    public void testInputPartOfLongestKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hellohello", "world"}, {"helloworld", "worldhello"}});
        CharSequence input = "worldhelloworld";
        CharSequence expected = "worldhello";
        try (Writer out = new StringBuilder()) {
            translator.translate(input, 0, out);
            CharSequence result = out.toString();
            assertEquals(expected, result);
        } catch (IOException e) {
            fail("IOException should not be thrown for input part of longest key");
        }
    }

    @Test
    public void testInputLongerThanLongestKey() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"hellohello", "world"}, {"helloworld", "worldhello"}});
        CharSequence input = "helloworldhelloworld";
        CharSequence expected = "worldhello";
        try (Writer out = new StringBuilder()) {
            translator.translate(input, 0, out);
            CharSequence result = out.toString();
            assertEquals(expected, result);
        } catch (IOException e) {
            fail("IOException should not be thrown for input longer than longest key");
        }
    }
}