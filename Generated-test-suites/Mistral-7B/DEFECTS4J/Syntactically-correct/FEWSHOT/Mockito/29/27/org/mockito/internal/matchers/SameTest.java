package org.mockito.internal.matchers;

import static org.junit.Assert.assertThat;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

public class SameTest {

    @Test
    public void testSameObject() {
        Object wanted = new Object();

        Matcher<Object> sameObjectMatcher = new Same(wanted);
        Object actual = wanted;

        assertThat(sameObjectMatcher, is(sameInstance(actual)));
    }

    @Test
    public void testSameString() {
        String wanted = "testString";

        Matcher<String> sameStringMatcher = new Same(wanted);
        String actual = wanted;

        assertThat(sameStringMatcher, is(equalTo(actual)));
    }

    @Test
    public void testSameCharacter() {
        char wanted = 'a';

        Matcher<Character> sameCharacterMatcher = new Same(wanted);
        Character actual = wanted;

        assertThat(sameCharacterMatcher, is(equalTo(actual)));
    }

    @Test
    public void testDifferentObjects() {
        Object wanted = new Object();
        Object actual = new Object();

        Matcher<Object> sameObjectMatcher = new Same(wanted);

        assertFalse(sameObjectMatcher.matches(actual));
    }

    @Test
    public void testDifferentStrings() {
        String wanted = "testString1";
        String actual = "testString2";

        Matcher<String> sameStringMatcher = new Same(wanted);

        assertFalse(sameStringMatcher.matches(actual));
    }

    @Test
    public void testDifferentCharacters() {
        char wanted = 'a';
        char actual = 'b';

        Matcher<Character> sameCharacterMatcher = new Same(wanted);

        assertFalse(sameCharacterMatcher.matches(actual));
    }

    @Test
    public void testNull() {
        Object wanted = null;

        Matcher<Object> sameObjectMatcher = new Same(wanted);

        assertNull(sameObjectMatcher.matches(null));
        assertNull(sameObjectMatcher.matches(new Object()));
    }

    @Test
    public void testDescribeTo() {
        Object wanted = new Object();

        Matcher<Object> sameObjectMatcher = new Same(wanted);

        Description description = new Description();
        sameObjectMatcher.describeTo(description);

        String expectedDescription = "same(" + getQuotedString(wanted.toString()) + ")";
        String actualDescription = description.toString();

        assertEquals(expectedDescription, actualDescription);
    }

    private void appendQuoting(Description description, Object obj) {
        if (obj instanceof String) {
            description.appendText("\"" + obj.toString() + "\"");
        } else if (obj instanceof Character) {
            description.appendText("'" + obj.toString() + "'");
        } else {
            description.appendText(obj.toString());
        }
    }

    private String getQuotedString(String stringToQuote) {
        if (stringToQuote == null) {
            return "";
        }
        return "\"" + stringToQuote + "\"";
    }
}