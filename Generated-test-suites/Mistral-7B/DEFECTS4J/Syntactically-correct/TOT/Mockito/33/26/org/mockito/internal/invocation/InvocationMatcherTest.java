import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Object mock;
    private Stubber stubber;

    @Before
    public void setUp() {
        mock = mock(SomeClass.class);
        stubber = Mockito.stub(mock);
        matcher = new InvocationMatcher(new InvocationOnMock(mock), Collections.emptyList());
    }

    @Test
    public void testMatchesSameInvocation() {
        // Given
        Invocation invocation = new InvocationOnMock(mock);

        // When
        InvocationMatcher testMatcher = new InvocationMatcher(invocation);

        // Then
        assertThat(testMatcher, is(matcher));
    }

    @Test
    public void testMatchesDifferentInvocationWithSameMethodAndSameArguments() {
        // Given
        Invocation invocation1 = new InvocationOnMock(mock);
        when(mock.someMethod(1, "str")).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        Invocation invocation2 = new InvocationOnMock(mock);

        // When
        InvocationMatcher testMatcher = new InvocationMatcher(invocation1);

        // Then
        InvocationMatcher comparisonMatcher = new InvocationMatcher(invocation2);
        assertThat(testMatcher, is(comparisonMatcher));
    }

    @Test
    public void testMatchesDifferentInvocationWithDifferentMethod() {
        // Given
        Invocation invocation1 = new InvocationOnMock(mock).withMethodCall("someMethod", 1, "str");
        Invocation invocation2 = new InvocationOnMock(mock).withMethodCall("anotherMethod", 1, "str");

        // When
        InvocationMatcher testMatcher = new InvocationMatcher(invocation1);

        // Then
        InvocationMatcher comparisonMatcher = new InvocationMatcher(invocation2);
        assertThat(testMatcher, is(not(comparisonMatcher)));
    }

    @Test
    public void testMatchesDifferentInvocationWithSameMethodButDifferentArguments() {
        // Given
        Invocation invocation1 = new InvocationOnMock(mock).withMethodCall("someMethod", 1, "str");
        Invocation invocation2 = new InvocationOnMock(mock).withMethodCall("someMethod", 2, "str");

        // When
        InvocationMatcher testMatcher = new InvocationMatcher(invocation1);

        // Then
        InvocationMatcher comparisonMatcher = new InvocationMatcher(invocation2);
        assertThat(testMatcher, is(not(comparisonMatcher)));
    }

    @Test
    public void testMatchesDifferentInvocationWithSameMethodButDifferentTypes() {
        // Given
        Invocation invocation1 = new InvocationOnMock(mock).withMethodCall("someMethod", "str");
        Invocation invocation2 = new InvocationOnMock(mock).withMethodCall("someMethod", 1);

        // When
        InvocationMatcher testMatcher = new InvocationMatcher(invocation1);

        // Then
        InvocationMatcher comparisonMatcher = new InvocationMatcher(invocation2);
        assertThat(testMatcher, is(not(comparisonMatcher)));
    }

    @Test
    public void testMatchesDifferentInvocationWithSameMethodButDifferentTypesAndArguments() {
        // Given
        Invocation invocation1 = new InvocationOnMock(mock).withMethodCall("someMethod", "str");
        Invocation invocation2 = new InvocationOnMock(mock).withMethodCall("someMethod", 1, "str");

        // When
        InvocationMatcher testMatcher = new InvocationMatcher(invocation1);

        // Then
        InvocationMatcher comparisonMatcher = new InvocationMatcher(invocation2);
        assertThat(testMatcher, is(not(comparisonMatcher)));
    }

    @Test
    public void testMatchesDifferentInvocationWithSameMethodButDifferentInvocationType() {
        // Given
        Invocation invocation1 = spy(mock);
        Invocation invocation2 = new InvocationOnMock(mock);

        // When
        InvocationMatcher testMatcher = new InvocationMatcher(invocation1);

        // Then
        InvocationMatcher comparisonMatcher = new InvocationMatcher(invocation2);
        assertThat(testMatcher, is(not(comparisonMatcher)));
    }

    @Test
    public void testMatchesSameInvocationWithMatchers() {
        // Given
        Invocation invocation = new InvocationOnMock(mock);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        when(mock.someMethod(stringArgumentCaptor.capture())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        // When
        List<Matcher> matchers = Arrays.asList(equalTo("str"));
        InvocationMatcher testMatcher = new InvocationMatcher(invocation, matchers);

        // Then
        String capturedArgument = stringArgumentCaptor.getValue();
        InvocationMatcher comparisonMatcher = new InvocationMatcher(invocation, matchers);
        InvocationMatcher comparisonMatcherWithSameArguments = new InvocationMatcher(invocation);
        assertThat(testMatcher, is(comparisonMatcher));
        assertThat(testMatcher, is(comparisonMatcherWithSameArguments));
    }

    @Test
    public void testMatchesDifferentInvocationWithMatchers() {
        // Given
        Invocation invocation1 = new InvocationOnMock(mock)
                .withMethodCall("someMethod", 1, "str");
        ArgumentCaptor<String> stringArgumentCaptor1 = ArgumentCaptor.forClass(String.class);
        when(mock.someMethod(stringArgumentCaptor1.capture(), 1)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        Invocation invocation2 = new InvocationOnMock(mock)
                .withMethodCall("someMethod", "str", 2);
        ArgumentCaptor<String> stringArgumentCaptor2 = ArgumentCaptor.forClass(String.class);
        when(mock.someMethod(stringArgumentCaptor2.capture(), 2)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        // When
        List<Matcher> matchers1 = Arrays.asList(equalTo("str"), is(1));
        InvocationMatcher testMatcher1 = new InvocationMatcher(invocation1, matchers1);

        List<Matcher> matchers2 = Arrays.asList(equalTo("str"), is(2));
        InvocationMatcher testMatcher2 = new InvocationMatcher(invocation2, matchers2);

        // Then
        String capturedArgument1 = stringArgumentCaptor1.getValue();
        String capturedArgument2 = stringArgumentCaptor2.getValue();
        InvocationMatcher comparisonMatcher1 = new InvocationMatcher(invocation1, matchers1);
        InvocationMatcher comparisonMatcher2 = new InvocationMatcher(invocation2, matchers2);
        InvocationMatcher comparisonMatcherWithSameArguments1 = new InvocationMatcher(invocation1);
        InvocationMatcher comparisonMatcherWithSameArguments2 = new InvocationMatcher(invocation2);
        assertThat(testMatcher1, is(comparisonMatcher1));
        assertThat(testMatcher1, is(comparisonMatcherWithSameArguments1));
        assertThat(testMatcher2, is(not(comparisonMatcher1)));
        assertThat(testMatcher2, is(not(comparisonMatcherWithSameArguments1)));
        assertThat(capturedArgument1, is(equalTo(capturedArgument2)));
    }

    @Test
    public void testToString() {
        // Given
        Invocation invocation = new InvocationOnMock(mock)
                .withMethodCall("someMethod", 1, "str");

        // When
        String expected = "InvocationMatcher[invocation=Invocation[this=org.mockito.internal.invocation.TestClass$MockInvocationSite@6f32532a, method=private java.lang.String org.mockito.internal.test.TestClass.someMethod(int,java.lang.String), arguments=[1, str]], matchers=[]]";
        String actual = matcher.toString();

        // Then
        assertThat(actual, is(expected));
    }
}