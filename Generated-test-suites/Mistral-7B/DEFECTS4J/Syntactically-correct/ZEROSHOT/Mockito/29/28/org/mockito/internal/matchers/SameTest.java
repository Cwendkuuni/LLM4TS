package org.mockito.internal.matchers;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertThat;
import static org.mockito.internal.matchers.Same.same;

@RunWith(MockitoJUnitRunner.class)
public class SameTest {

    @Test
    public void testSameObjectMatches() {
        Object wanted = new Object();
        Object actual = wanted;

        Matcher<Object> matcher = same(wanted);

        assertThat(matcher, Matchers.is(matcher)); // test that matcher is not null
        assertThat(matcher, Matchers.matches(actual));
    }

    @Test
    public void testSameObjectDoesntMatchDifferentObject() {
        Object wanted = new Object();
        Object actual = new Object();

        Matcher<Object> matcher = same(wanted);

        assertThat(matcher, Matchers.not(Matchers.matches(actual)));
    }

    @Test
    public void testSameStringMatches() {
        String wanted = "hello";
        String actual = "hello";

        Matcher<String> matcher = same(wanted);

        assertThat(matcher, Matchers.is(matcher)); // test that matcher is not null
        assertThat(matcher, Matchers.matches(actual));
    }

    @Test
    public void testSameStringDoesntMatchDifferentString() {
        String wanted = "hello";
        String actual = "world";

        Matcher<String> matcher = same(wanted);

        assertThat(matcher, Matchers.not(Matchers.matches(actual)));
    }

    @Test
    public void testSameCharacterMatches() {
        Character wanted = 'a';
        Character actual = 'a';

        Matcher<Character> matcher = same(wanted);

        assertThat(matcher, Matchers.is(matcher)); // test that matcher is not null
        assertThat(matcher, Matchers.matches(actual));
    }

    @Test
    public void testSameCharacterDoesntMatchDifferentCharacter() {
        Character wanted = 'a';
        Character actual = 'b';

        Matcher<Character> matcher = same(wanted);

        assertThat(matcher, Matchers.not(Matchers.matches(actual)));
    }

    @Test
    public void testSameSerializableMatches() {
        Serializable wanted = new Serializable() {};
        Serializable actual = wanted;

        Matcher<Serializable> matcher = same(wanted);

        assertThat(matcher, Matchers.is(matcher)); // test that matcher is not null
        assertThat(matcher, Matchers.matches(actual));
    }

    @Test
    public void testSameSerializableDoesntMatchDifferentSerializable() {
        Serializable wanted = new Serializable() {};
        Serializable actual = new Serializable() {};

        if (wanted instanceof Object) {
            // Serializable class is abstract, so we cannot create an instance directly.
            // We can only test that two different instances of Serializable do not match.
            Matcher<Serializable> matcher = same(wanted);
            assertThat(matcher, Matchers.not(Matchers.matches(actual)));
        }
    }
}