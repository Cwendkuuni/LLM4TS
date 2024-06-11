import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MethodType;
import org.mockito.mock.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private static final Class<InvocationMatcher> CLASS_UNDER_TEST = InvocationMatcher.class;

    private InvocationMatcher invocationMatcher;
    private InvocationOnMock invocationOnMock;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        invocationOnMock = MockitoAnnotations.mock(InvocationOnMock.class);
    }

    @Test
    public void testConstructorWithInvocationAndEmptyListOfMatchers() {
        // given
        Invocation invocation = new InvocationOnMock(MethodType.METHOD, new Object[]{}, new Object[0], new Object[0], new Location(1, 1), null);
        List<Matcher> emptyList = Collections.emptyList();
        
        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, emptyList);
        
        // then
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testConstructorWithInvocationAndListOfMatchers() {
        // given
        Invocation invocation = new InvocationOnMock(MethodType.METHOD, new Object[]{ArgumentMatchers.anyInt(), ArgumentMatchers.anyString()}, new Object[]{1, "Test"}, new Object[0], new Location(1, 1), null);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("Test"));
        
        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        // then
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testMethodGetMethod() {
        // given
        Invocation invocation = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg2"}, new Object[]{"Test1", "Test2"}, new Object[0], new Location(1, 1), null);

        // when
        Method method = invocationMatcher.getMethod();

        // then
        assertEquals(method.getName(), "method");
        assertEquals(method.getParameterTypes().length, 2);
    }

    @Test
    public void testMethodGetInvocation() {
        // given
        Invocation invocation = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg2"}, new Object[]{"Test1", "arg2"}, new Object[0], new Location(1, 1), null);

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // then
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testListGetMatchers() {
        // given
        Invocation invocation = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg2"}, new Object[]{"Test1", "Test2"}, new Object[0], new Location(1, 1), null);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("Test1"));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        // then
        assertEquals(matchers, actualMatchers);
    }

    @Test
    public void testToString() {
        // given
        Invocation invocation = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg2"}, new Object[]{"Test1", "Test2"}, new Object[0], new Location(1, 1), null);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("Test1"));

        // when
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // then
        String expectedToString = String.format("InvocationMatcher{method=%s, arguments={%s}}%n", invocation.getMethod().toGenericString(), matchers.stream().map(Matcher::toString).collect(Collectors.joining(", ")));
        String actualToString = invocationMatcher.toString();

        // then
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testMatchesWithSameInvocation() {
        // given
        Invocation invocation = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg2"}, new Object[]{"Test1", "Test2"}, new Object[0], new Location(1, 1), null);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("Test1"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        boolean result = invocationMatcher.matches(invocation);

        // then
        assertTrue(result);
    }

    @Test
    public void testMatchesWithDifferentInvocation() {
        // given
        Invocation invocation1 = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg2"}, new Object[]{"Test1", "Test2"}, new Object[0], new Location(1, 1), null);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("Test1")));
        Invocation invocation2 = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg3"}, new Object[]{"Test1", "Test3"}, new Object[0], new Location(1, 1), null);

        // when
        boolean result = invocationMatcher1.matches(invocation2);

        // then
        assertFalse(result);
    }

    @Test
    public void testHasSameMethodWithSameInvocation() {
        // given
        Invocation invocation = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg2"}, new Object[]{"Test1", "Test2"}, new Object[0], new Location(1, 1), null);

        // when
        boolean result = invocationMatcher.hasSameMethod(invocation);

        // then
        assertTrue(result);
    }

    @Test
    public void testHasSameMethodWithDifferentInvocationButSameMethodNameAndMocks() {
        // given
        Invocation invocation1 = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg2"}, new Object[]{"Test1", "Test2"}, new Object[0], new Location(1, 1), null);
        Invocation invocation2 = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg3"}, new Object[]{"Test1", "Test3"}, new Object[0], new Location(1, 1), null);

        // when
        boolean result = invocationMatcher.hasSameMethod(invocation2);

        // then
        assertFalse(result);
    }

    @Test
    public void testHasSameMethodWithDifferentInvocationButSameMethodNameAndArguments() {
        // given
        Invocation invocation1 = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg2"}, new Object[]{"Test1", "Test2"}, new Object[0], new Location(1, 1), null);
        Invocation invocation2 = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg2"}, new Object[]{"Test1", "Test2"}, new Object[0], new Location(1, 1), null);

        // when
        boolean result = invocationMatcher.hasSameMethod(invocation2);

        // then
        assertTrue(result);
    }

    @Test
    public void testHasSameMethodWithDifferentInvocationButDifferentMethodName() {
        // given
        Invocation invocation1 = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg2"}, new Object[]{"Test1", "Test2"}, new Object[0], new Location(1, 1), null);
        Invocation invocation2 = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg3"}, new Object[]{"Test1", "Test3"}, new Object[0], new Location(1, 1), null);

        // when
        boolean result = invocationMatcher.hasSameMethod(invocation2);

        // then
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethodWithSameInvocation() {
        // given
        Invocation invocation = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg2"}, new Object[]{"Test1", "Test2"}, new Object[0], new Location(1, 1), null);

        // when
        boolean result = invocationMatcher.hasSimilarMethod(invocation);

        // then
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethodWithDifferentInvocationButSameMethodName() {
        // given
        Invocation invocation1 = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg2"}, new Object[]{"Test1", "Test2"}, new Object[0], new Location(1, 1), null);
        Invocation invocation2 = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg3"}, new Object[]{"Test1", "Test3"}, new Object[0], new Location(1, 1), null);

        // when
        boolean result = invocationMatcher.hasSimilarMethod(invocation2);

        // then
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethodWithDifferentInvocationButSameMethodNameAndSameArguments() {
        // given
        Invocation invocation1 = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg2"}, new Object[]{"Test1", "Test2"}, new Object[0], new Location(1, 1), null);
        Invocation invocation2 = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg2"}, new Object[]{"Test1", "Test2"}, new Object[0], new Location(1, 1), null);

        // when
        boolean result = invocationMatcher.hasSimilarMethod(invocation2);

        // then
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethodWithDifferentInvocationButSameMethodNameAndDifferentArguments() {
        // given
        Invocation invocation1 = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg2"}, new Object[]{"Test1", "Test2"}, new Object[0], new Location(1, 1), null);
        Invocation invocation2 = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg3"}, new Object[]{"Test1", "Test3"}, new Object[0], new Location(1, 1), null);

        // when
        boolean result = invocationMatcher.hasSimilarMethod(invocation2);

        // then
        assertFalse(result);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // given
        Invocation invocation = new InvocationOnMock(MethodType.METHOD, new Object[]{"arg1", "arg2"}, new Object[]{"Test1", "Test2"}, new Object[0], new Location(1, 1), null);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("Test1"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // when
        invocationMatcher.captureArgumentsFrom(invocationOnMock);

        // then
        for (int i = 0; i < invocationMatcher.getMatchers().size(); i++) {
            Matcher matcher = invocationMatcher.getMatchers().get(i);
            if (matcher instanceof CapturesArguments) {
                Object capturedArg = ((CapturesArguments) matcher).getValue();
                assertEquals(invocationOnMock.getArgumentAt(i, Object.class), capturedArg);
            }
        }
    }
}