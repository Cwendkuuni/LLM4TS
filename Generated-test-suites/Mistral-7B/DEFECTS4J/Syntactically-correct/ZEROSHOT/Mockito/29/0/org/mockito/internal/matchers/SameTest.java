package org.mockito.internal.matchers;

import org.junit.Test;
import org.junit.Assert;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Object actual = wanted;

        Same sameMatcher = new Same(wanted);

        Assert.assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentInstances() {
        Object wanted = new Object();
        Object actual = new Object();

        Same sameMatcher = new Same(wanted);

        Assert.assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testNullAndSameInstance() {
        Object wanted = new Object();

        Same sameMatcher = new Same(wanted);

        Assert.assertFalse(sameMatcher.matches(null));
        Assert.assertTrue(sameMatcher.matches(wanted));
    }

    @Test
    public void testSameString() {
        String wanted = "test";
        String actual = "test";

        Same sameMatcher = new Same(wanted);

        assertThat(sameMatcher, is(sameInstance(wanted)));
        assertThat(sameMatcher, is(sameInstance(actual)));
    }

    @Test
    public void testDifferentStrings() {
        String wanted = "test";
        String actual = "different";

        Same sameMatcher = new Same(wanted);

        Assert.assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testSameCharacter() {
        Character wanted = 'a';
        Character actual = 'a';

        Same sameMatcher = new Same(wanted);

        Assert.assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentCharacters() {
        Character wanted = 'a';
        Character actual = 'b';

        Same sameMatcher = new Same(wanted);

        Assert.assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testMockitoMock() {
        Object wanted = mock(Object.class);
        Object actual = mock(Object.class);

        Same sameMatcher = new Same(wanted);

        Assert.assertTrue(sameMatcher.matches(actual));
    }

    @Test(expected = AssertionError.class)
    public void testPropertyAccessor() {
        final Object wanted = new Object() {
            @Override
            public String toString() {
                return "wanted";
            }
        };

        Same sameMatcher = new Same(wanted);
        Object actual = new Object() {
            @Override
            public String toString() {
                return "actual";
            }
        };

        Assert.assertThat(sameMatcher, is(not(sameInstance(actual))));
    }

    private Matcher<Object> sameInstance(Object expected) {
        return org.hamcrest.CoreMatchers.sameInstance(expected);
    }
}