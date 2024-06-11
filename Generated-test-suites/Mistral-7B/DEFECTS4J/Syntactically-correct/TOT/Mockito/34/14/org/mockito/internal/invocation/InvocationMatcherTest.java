import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private Object mock;
    private InvocationMatcher matcher;
    private Invocation invitation;

    @Before
    public void setup() {
        mock = mock(SomeClass.class);
        invitation = new InvocationOnMock(mock, getClass().getMethod("someMethod", int.class, String.class), new Object[]{1, "test"});
        matcher = new InvocationMatcher(invitation);
    }

    @Test
    public void testMatcherWithEmptyList() {
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher emptyMatcher = new InvocationMatcher(invitation, matchers);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invitation);

        assertThat(emptyMatcher, is(expectedMatcher));
    }

    @Test
    public void testMatcherWithSpecifiedMatchers() {
        Matcher<Integer> integerMatcher = Mockito.argThat(is(1));
        Matcher<String> stringMatcher = Mockito.argThat(equalTo("test"));
        List<Matcher> matchers = Arrays.asList(integerMatcher, stringMatcher);

        InvocationMatcher matcher = new InvocationMatcher(invitation, matchers);

        ArgumentCaptor<Integer> integerCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        Answer<Object> answer = Mockito.answer(new Object());
        mock.someMethod(Mockito.capture(integerCaptor), Mockito.capture(stringCaptor));

        matcher.captureArgumentsFrom(invitation);

        verify(mock).someMethod(integerCaptor.getValue(), stringCaptor.getValue());
        assertThat(integerCaptor.getValue(), is(1));
        assertThat(stringCaptor.getValue(), is("test"));
    }

    @Test(expected = AssertionError.class)
    public void testMatcherWithMismatchingArguments() {
        Matcher<Integer> integerMatcher = Mockito.argThat(is(2));
        List<Matcher> matchers = Arrays.asList(integerMatcher);

        InvocationMatcher matcher = new InvocationMatcher(invitation, matchers);

        matcher.captureArgumentsFrom(invitation);
    }

    @Test
    public void testToString() {
        String expectedToString = "InvocationMatcher [invocation=InvocationOnMock [mock=org.mockito.internal.InvocationMatcherTest$Mock$MockMockitoJUnitRunner$3@27812282, location=InvocationLocation [line=1, method=InvocationMatcherTest.testMatcherWithEmptyList()], method=public void org.mockito.SomeClass.someMethod(int, java.lang.String), argumentsToMatch=[], mocked=true], matchers=[]]";

        String actualToString = matcher.toString();

        assertThat(actualToString, is(expectedToString));
    }

    @Test
    public void testMatches() {
        Matcher<Integer> integerMatcher = Mockito.argThat(is(1));
        Matcher<String> stringMatcher = Mockito.argThat(equalTo("test"));
        List<Matcher> matchers = Arrays.asList(integerMatcher, stringMatcher);

        InvocationMatcher matcher = new InvocationMatcher(invitation, matchers);
        InvocationMatcher otherMatcher = new InvocationMatcher(new InvocationOnMock(mock, getClass().getMethod("someMethod", int.class, String.class), new Object[]{2, "test"}));

        InvocationMatcher expectedMatcher = new InvocationMatcher(invitation);

        assertThat(matcher, is(expectedMatcher));
        assertFalse(matcher.matches(otherMatcher));
    }

    @Test
    public void testHasSameMethod() {
        InvocationMatcher matcher1 = new InvocationMatcher(invitation);
        InvocationMatcher matcher2 = new InvocationMatcher(invitation);

        assertTrue(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testHasDifferentMethods() {
        InvocationMatcher matcher1 = new InvocationMatcher(invitation);
        InvocationMatcher matcher2 = new InvocationMatcher(new InvocationOnMock(mock, getClass().getMethod("anotherMethod", int.class), new Object[]{1}));

        assertFalse(matcher1.hasSameMethod(matcher2));
    }
}