package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.LookupTranslator;

import org.junit.Test;

/**
 * Tests for {@link LookupTranslator}.
 */
public class LookupTranslatorTest {

    @Test
    public void testEmptyLookupTable() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {});
        StringWriter stringWriter = new StringWriter();
        String input = "";
        int index = 0;

        int length = translator.translate(input, index, stringWriter);
        assertEquals("", stringWriter.toString());
        assertEquals(0, length);
    }

    @Test
    public void testLookupTableWithOneEntry() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] { { "Hello", "Hi" } });
        StringWriter stringWriter = new StringWriter();
        String input = "Hello";
        int index = 0;

        int length = translator.translate(input, index, stringWriter);
        assertEquals("Hi", stringWriter.toString());
        assertEquals(5, length);
    }

    @Test
    public void testLookupTableWithMultipleEntries() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            { "Hello", "Hi" },
            { "World", "Hello" }
        });
        StringWriter stringWriter = new StringWriter();
        String input = "WorldHello";
        int index = 0;

        int length = translator.translate(input, index, stringWriter);
        assertEquals("HelloHi", stringWriter.toString());
        assertEquals(7, length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            { "Hello", "Hi" }
        });
        StringWriter stringWriter = new StringWriter();
        String input = null;
        int index = 0;

        translator.translate(input, index, stringWriter);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInputBeyondBoundaries() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            { "Hello", "Hi" }
        });
        StringWriter stringWriter = new StringWriter();
        String input = "HelloWorld";
        int index = 6;

        translator.translate(input, index, stringWriter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullLookupTable() {
        LookupTranslator translator = new LookupTranslator(null);
    }

    @Test
    public void testLookupTableWithEmptyInputStream() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            { "Hello", "Hi" },
            { "World", "Hello" }
        });
        StringWriter stringWriter = new StringWriter();
        String input = "";
        int index = 0;

        int length = translator.translate(input, index, stringWriter);
        assertEquals("", stringWriter.toString());
        assertEquals(0, length);
    }

    @Test
    public void testLookupTableWithLongInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            { "Hello", "Hi" }
        });
        StringWriter stringWriter = new StringWriter();
        String input = StringUtils.repeat("Hello", 1000);
        int index = 0;

        int length = translator.translate(input, index, stringWriter);
        String expected = StringUtils.repeat("Hi", 1000);
        assertEquals(expected, stringWriter.toString());
        assertEquals(expected.length(), length);
    }

    @Test
    public void testLookupTableWithComplexInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            { "Apple", "Fruit" },
            { "Banana", "Fruit" },
            { "Orange", "Fruit" },
            { "Watermelon", "Fruit" },
            { "Steak", "Meat" },
            { "Chicken", "Meat" },
            { "Fish", "Meat" },
            { "Egg", "Egg" },
            { "Bread", "Bread" },
            { "Butter", "Butter" }
        });
        StringWriter stringWriter = new StringWriter();
        String input = "SteakBananaFishEggBreadAppleOrangeWatermelonChickenButter";
        int index = 0;

        int length = translator.translate(input, index, stringWriter);
        String expected = "MeatFruitMeatEggBreadFruitFruitMeatButter";
        assertEquals(expected, stringWriter.toString());
        assertEquals(expected.length(), length);
    }

    @Test
    public void testLookupTableWithComplexInputAndMultipleMatches() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            { "Apple", "Fruit" },
            { "Banana", "Fruit" },
            { "Orange", "Fruit" },
            { "Watermelon", "Fruit" }
        });
        StringWriter stringWriter = new StringWriter();
        String input = "AppleBananaOrangeAppleWatermelon";
        int index = 0;

        int length = translator.translate(input, index, stringWriter);
        String expected = "FruitFruitFruitFruit";
        assertEquals(expected, stringWriter.toString());
        assertEquals(expected.length(), length);
    }

    @Test
    public void testLookupTableWithComplexInputAndNoMatches() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            { "Apple", "Fruit" },
            { "Banana", "Fruit" },
            { "Orange", "Fruit" },
            { "Watermelon", "Fruit" }
        });
        StringWriter stringWriter = new StringWriter();
        String input = "GrapesPineappleMango";
        int index = 0;

        int length = translator.translate(input, index, stringWriter);
        assertEquals("", stringWriter.toString());
        assertEquals(0, length);
    }

    @Test
    public void testLookupTableWithComplexInputAndPartialMatches() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            { "Apple", "Fruit" },
            { "Banana", "Fruit" },
            { "Orange", "Fruit" },
            { "Watermelon", "Fruit" }
        });
        StringWriter stringWriter = new StringWriter();
        String input = "AppleBananaOrangeWaterWatermelon";
        int index = 0;

        int length = translator.translate(input, index, stringWriter);
        String expected = "FruitFruitFruit";
        assertEquals(expected, stringWriter.toString());
        assertEquals(expected.length(), length);
    }

    @Test
    public void testLookupTableWithComplexInputAndMultiplePartialMatches() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            { "Apple", "Fruit" },
            { "Banana", "Fruit" },
            { "Orange", "Fruit" },
            { "Watermelon", "Fruit" }
        });
        StringWriter stringWriter = new StringWriter();
        String input = "AppleBananaOrangeWaterWatermelonApple";
        int index = 0;

        int length = translator.translate(input, index, stringWriter);
        String expected = "FruitFruitFruitFruit";
        assertEquals(expected, stringWriter.toString());
        assertEquals(expected.length(), length);
    }

    @Test
    public void testLookupTableWithComplexInputAndSinglePartialMatch() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            { "Apple", "Fruit" },
            { "Banana", "Fruit" },
            { "Orange", "Fruit" },
            { "Watermelon", "Fruit" }
        });
        StringWriter stringWriter = new StringWriter();
        String input = "AppleBananaOrangeWaterWatermelonAppleBanana";
        int index = 0;

        int length = translator.translate(input, index, stringWriter);
        String expected = "FruitFruitFruitFruitFruit";
        assertEquals(expected, stringWriter.toString());
        assertEquals(expected.length(), length);
    }

    @Test
    public void testLookupTableWithComplexInputAndEmptySequence() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            { "Apple", "Fruit" },
            { "Banana", "Fruit" },
            { "Orange", "Fruit" },
            { "Watermelon", "Fruit" }
        });
        StringWriter stringWriter = new StringWriter();
        String input = "AppleBananaOrangeWaterWatermelonAppleBanana";
        int index = 11;

        int length = translator.translate(input, index, stringWriter);
        assertEquals("", stringWriter.toString());
        assertEquals(0, length);
    }
}