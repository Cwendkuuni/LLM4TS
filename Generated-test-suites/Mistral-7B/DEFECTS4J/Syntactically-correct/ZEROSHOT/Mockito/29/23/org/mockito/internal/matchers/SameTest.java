package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.Stubber;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

@RunWith(MockitoJUnitRunner.class)
public class SameTest {

    @Test
    public void testSameInstance() {
        Object want = new Object();
        Object actual = want;

        Stubber stubber = mock(Stubber.class);
        ArgumentMatcher<Object> sameMatcher = new Same(want);

        stub(stubber.matches(same(want)))
                .thenReturn(true);

        Matcher<Object> sameInstanceMatcher = is(same(want));

        assertThat(sameInstanceMatcher.matches(actual), is(true));
        stubber.verify(stubber).matches(same(want));
    }

    @Test
    public void testSameNullInstance() {
        Object want = null;
        Object actual = want;

        Stubber stubber = mock(Stubber.class);
        ArgumentMatcher<Object> sameMatcher = new Same(want);

        stub(stubber.matches(same(want)))
                .thenReturn(false);

        Matcher<Object> sameInstanceMatcher = is(same(want));

        assertThat(sameInstanceMatcher.matches(actual), is(false));
        stubber.verify(stubber, never()).matches(same(null));
    }

    @Test
    public void testSameDifferentClassInstance() {
        Object want = new Object();
        String actual = "different object";

        Stubber stubber = mock(Stubber.class);
        ArgumentMatcher<Object> sameMatcher = new Same(want);

        stub(stubber.matches(same(want)))
                .thenReturn(false);

        Matcher<Object> sameInstanceMatcher = is(same(want));

        assertThat(sameInstanceMatcher.matches(actual), is(false));
        stubber.verify(stubber, never()).matches(same(want));
    }

    @Test
    public void testSameStringInstance() {
        String want = "same string";
        String actual = want;

        Stubber stubber = mock(Stubber.class);
        ArgumentMatcher<Object> sameMatcher = new Same(want);

        stub(stubber.matches(same(want)))
                .thenReturn(true);

        Matcher<Object> sameInstanceMatcher = is(same(want));

        assertThat(sameInstanceMatcher.matches(actual), is(true));
        stubber.verify(stubber).matches(same(want));
    }

    @Test
    public void testSameCharacterInstance() {
        Character want = 'c';
        Character actual = want;

        Stubber stubber = mock(Stubber.class);
        ArgumentMatcher<Object> sameMatcher = new Same(want);

        stub(stubber.matches(same(want)))
                .thenReturn(true);

        Matcher<Object> sameInstanceMatcher = is(same(want));

        assertThat(sameInstanceMatcher.matches(actual), is(true));
        stubber.verify(stubber).matches(same(want));
    }

    @Test
    public void testSameMatcherDescription() {
        Object want = new Object();

        Stubber stubber = mock(Stubber.class);
        ArgumentMatcher<Object> sameMatcher = new Same(want);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same(" + want.toString() + ")";
        String actualDescription = description.toString();

        assertThat(actualDescription, is(expectedDescription));
    }
}