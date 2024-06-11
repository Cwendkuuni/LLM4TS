import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private InvocationOnMock invocation;
    private Object mock;

    @Before
    public void setup() {
        mock = mock(Object.class);
        invocation = Mockito.onCall(Mockito.mock(Answer.class)).invocation();
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        // Given

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Then
        assertThat(invocationMatcher, is(notNullValue()));
    }

    @Test
    public void testConstructorWithInvocationAndMatchersList() {
        // Given
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.anyInt());

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Then
        assertThat(invocationMatcher, is(notNullValue()));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testGetMethod() {
        // Given

        // When
        Method method = invocationMatcher.getMethod();

        // Then
        assertThat(method, is(notNullValue()));
    }

    @Test
    public void testGetInvocation() {
        // Given

        // When
        Invocation invocation = invocationMatcher.getInvocation();

        // Then
        assertThat(invocation, is(invocation));
    }

    @Test
    public void testGetMatchers() {
        // Given

        // When
        List<Matcher> matchers = invocationMatcher.getMatchers();

        // Then
        assertThat(matchers, is(Collections.emptyList()));
    }

    @Test
    public void testToString() {
        // Given
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.anyInt());

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Then
        String actual = invocationMatcher.toString();
        String expected = invocation.toString(matchers, new PrintSettings());

        // When (assert)
        assertThat(actual, is(expected));
    }

    @Test
    public void testMatches() {
        // Given
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation);

        // When
        // Both invocationMatchers should match because they have the same invocation
        boolean result = invocationMatcher1.matches(invocationMatcher2);

        // Then
        assertThat(result, is(true));
    }

    @Test
    public void testMatchesWithDifferentInvocations() {
        // Given
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(Mockito.onCall(Mockito.mock(Answer.class)).invocation());

        // When
        // Different invocations should not match
        boolean result = invocationMatcher1.matches(invocationMatcher2);

        // Then
        assertThat(result, is(false));
    }

    @Test
    public void testMatchesWithDifferentMethods() {
        // Given
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(Mockito.onCall(Mockito.mock(Answer.class)).invocation(), Arrays.asList(ArgumentMatchers.eq("test2")));

        // When
        // Different methods should not match
        boolean result = invocationMatcher1.matches(invocationMatcher2);

        // Then
        assertThat(result, is(false));
    }

    @Test
    public void testMatchesWithSameMethodAndDifferentArguments() {
        // Given
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(Mockito.onCall(Mockito.mock(Answer.class)).invocation(), Arrays.asList(ArgumentMatchers.eq("test2"), ArgumentMatchers.anyInt()));

        // When
        // Different arguments should not match
        boolean result = invocationMatcher1.matches(invocationMatcher2);

        // Then
        assertThat(result, is(false));
    }

    @Test
    public void testMatchesWithSameMethodAndSameArguments() {
        // Given
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.anyInt());

        // When
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation, matchers);

        // When
        // Same invocation and same arguments should match
        boolean result = invocationMatcher1.matches(invocationMatcher2);

        // Then
        assertThat(result, is(true));
    }

    @Test
    public void testHasSimilarMethod() {
        // Given
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(Mockito.onCall(Mockito.mock(Answer.class)).invocation());

        // When
        boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        // Then
        assertThat(result, is(false));
    }

    @Test
    public void testHasSameMethod() {
        // Given
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation);

        // When
        boolean result = invocationMatcher1.hasSameMethod(invocationMatcher2);

        // Then
        assertThat(result, is(true));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        // Given
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.arg(0), ArgumentMatchers.arg(1));

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Object[] mockArguments = new Object[]{1, 2};

        // Then
        invocationMatcher.captureArgumentsFrom(Mockito.onCall(Mockito.mock(Answer.class)).invocation(mock, mockArguments));

        // Verify that the matchers have been called with the captured arguments
        for (int i = 0; i < matchers.size(); i++) {
            Matcher matcher = matchers.get(i);
            Object capturedArgument = ((CapturesArguments) matcher).getCapturedValue();
            assertThat(capturedArgument, is(mockArguments[i]));
        }
    }
}