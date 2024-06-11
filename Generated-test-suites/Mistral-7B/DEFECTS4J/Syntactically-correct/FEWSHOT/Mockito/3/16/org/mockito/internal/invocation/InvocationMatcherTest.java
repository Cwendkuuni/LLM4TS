package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentMatchers.argThat;
import static org.mockito.internal.matchers.ArgumentsAsList.arguments;
import static org.mockito.internal.matchers.ArgumentsAsList.is;
import static org.mockito.internal.matchers.ArgumentsAsList.nullValue;
import static org.mockito.internal.matchers.ArgumentsMatcher.isInstanceOf;
import static org.mockito.internal.matchers.ArgumentsMatcher.isMethod;
import static org.mockito.internal.matchers.ArgumentsMatcher.isNull;
import static org.mockito.internal.matchers.ArgumentsMatcher.isSame;
import static org.mockito.internal.matchers.ArgumentsMatcher.isVariable;
import static org.mockito.internal.reporting.PrintSettings.print;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.invocation.MockInvocation;
import org.mockito.invocation.VerifiedInvocation;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.TypeSafeMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.Collections2;
import org.mockito.internal.util.ReflectionUtils;
import org.mockito.internal.util.Types;
import org.mockito.internal.verification.VerificationInfo;
import org.mockito.internal.verification.VerificationResult;
import org.mockito.internal.verification.VerificationUtils;
import org.mockito.internal.verification.Verifications;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

public class InvocationMatcherTest {

    private static final Class<?> MOCK_CLASS = Mockito.class;
    private static final Class<?> INVOCATION_MATCHER_CLASS = InvocationMatcher.class;
    private static final Class<?> DESCRIBED_INVOCATION_CLASS = DescribedInvocation.class;
    private static final Class<?> MATCHER_CLASS = Matcher.class;
    private static final Class<?> LIST_CLASS = List.class;
    private static final Class<?> INVOCATION_CLASS = Invocation.class;
    private static final Class<?> ARRAY_CLASS = Array.class;
    private static final Class<?> METHOD_INTERCEPTOR_CLASS = MethodInterceptor.class;
    private static final Class<?> METHOD_CLASS = Method.class;
    private static final Class<?> LOCATION_CLASS = Location.class;
    private static final Class<?> CAPTURES_ARGUMENTS_CLASS = CapturesArgumentsFromInvocation.class;
    private static final Class<?> SERIALIZABLE_CLASS = Serializable.class;
    private static final Class<?> ARGUMENTS_PROCESSOR_CLASS = ArgumentsProcessor.class;

    @Rule
    public MockitoRule mockitoRule = new MockitoRule();

    @Rule
    public TestRule printRule = new TestPrint() {

        @Override
        public void print(String message) {
            System.out.println(message);
        }
    };

    private InvocationMatcher matcher;
    private Invocation invocation;
    private Method method;
    private Object[] arguments;
    private Object mock;
    private Object[] rawArguments;
    private Location location;
    private List<Matcher> matchers;
    private VerificationResult verificationResult;

    @Before
    public void setUp() {
        this.mock = mock(MOCK_CLASS);
        this.method = ReflectionUtils.getMethod(MOCK_CLASS, "someMethod");
        this.matchers = new ArrayList<>();
        this.matcher = new InvocationMatcher(new MockInvocation(this.mock, this.method, this.matchers.toArray(new Matcher[0])));
        this.invocation = new InvocationOnMock(this.mock, this.method, this.matchers.toArray(new Matcher[0]), null, null, null);
        this.location = new Location();
        this.arguments = new Object[]{null, "foo"};
        this.rawArguments = new Object[]{this.arguments[0], this.arguments[1]};
        this.verificationResult = new VerificationResult();
    }

    @Test
    public void testInvocationMatcher() {
        // Test constructor with matchers
        InvocationMatcher matcher1 = new InvocationMatcher(new MockInvocation(MOCK_CLASS, this.method, Collections.<Matcher>emptyList().toArray(new Matcher[0])));
        assertTrue(matcher1 instanceof InvocationMatcher);
        assertTrue(matcher1.getMatchers().isEmpty());
        assertTrue(matcher1.matches(this.invocation));
        
        // Test constructor without matchers
        InvocationMatcher matcher2 = new InvocationMatcher(new MockInvocation(MOCK_CLASS, this.method));
        assertTrue(matcher2 instanceof InvocationMatcher);
        assertTrue(matcher2.getMatchers().isEmpty());
        assertTrue(matcher2.matches(this.invocation));
        
        // Test constructor with matchers
        Matcher matcher3 = argThat(isNull());
        InvocationMatcher matcher3Matcher = new InvocationMatcher(new MockInvocation(MOCK_CLASS, this.method, new Matcher[]{matcher3}));
        assertTrue(matcher3Matcher instanceof InvocationMatcher);
        assertFalse(matcher3Matcher.matches(this.invocation));
        
        // Test getters
        assertTrue(this.matcher.getMethod().equals(this.method));
        assertTrue(this.matcher.getInvocation().equals(this.invocation));
        assertTrue(this.matcher.getMatchers().equals(this.matchers));
        assertTrue(this.matcher.toString().equals(print(this.matchers, this.invocation)));
        
        // Test matches
        assertTrue(this.matcher.matches(this.invocation));
        assertFalse(this.matcher.matches(new MockInvocation(MOCK_CLASS, this.method, this.matchers.toArray(new Matcher[0]))));
        
        // Test isVerified
        assertFalse(this.matcher.isVerified());
        
        // Test hasSameMethod
        InvocationMatcher otherMatcher = new InvocationMatcher(new MockInvocation(MOCK_CLASS, this.method));
        assertTrue(this.matcher.hasSameMethod(otherMatcher));
        
        // Test toString
        InvocationMatcher matcher4 = new InvocationMatcher(new MockInvocation(MOCK_CLASS, this.method), Arrays.asList(is("foo")));
        assertTrue(this.matcher.toString().equals(print(this.matchers, this.invocation)));
        assertTrue(matcher4.toString().equals(print("arguments[0] = 'foo'", this.invocation)));
        
        // Test matches with arguments
        InvocationMatcher matcher5 = new InvocationMatcher(new MockInvocation(MOCK_CLASS, this.method), Arrays.asList(is("foo"), is("bar")));
        assertTrue(matcher5.matches(new MockInvocation(MOCK_CLASS, this.method, new Object[]{"foo", "bar"})));
        
        // Test matches with varargs
        InvocationMatcher matcher6 = new InvocationMatcher(new MockInvocation(MOCK_CLASS, this.method), Arrays.asList(isVariable(0), is("foo")));
        assertTrue(matcher6.matches(new MockInvocation(MOCK_CLASS, this.method, new Object[]{"foo", "bar", "baz"})));
        
        // Test hasSimilarMethod
        InvocationMatcher otherMatcher2 = new InvocationMatcher(new MockInvocation(MOCK_CLASS, ReflectionUtils.getMethod(MOCK_CLASS, "someMethodWithDifferentName")));
        assertFalse(this.matcher.hasSimilarMethod(otherMatcher2));
        
        // Test hasSameMethod
        InvocationMatcher otherMatcher3 = new InvocationMatcher(new MockInvocation(MOCK_CLASS, this.method));
        assertTrue(this.matcher.hasSameMethod(otherMatcher3));
        
        // Test matches with null arguments
        InvocationMatcher matcher7 = new InvocationMatcher(new MockInvocation(MOCK_CLASS, this.method), Arrays.asList(isNull()));
        assertTrue(matcher7.matches(new MockInvocation(MOCK_CLASS, this.method, new Object[]{null})));
        
        // Test matches with non-null arguments
        InvocationMatcher matcher8 = new InvocationMatcher(new MockInvocation(MOCK_CLASS, this.method), Arrays.asList(isInstanceOf(String.class), is("foo")));
        assertTrue(matcher8.matches(new MockInvocation(MOCK_CLASS, this.method, new Object[]{"foo"})));
        
        // Test matches with arguments decorator
        MatcherDecorator decorator = new MatcherDecorator(new TypeSafeMatcher<Object>() {
            @Override
            public boolean matchesSafely(Object item) {
                return item instanceof List;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a list");
            }
        });
        Matcher matcher9 = argThat(decorator.decorate(isInstanceOf(List.class)));
        InvocationMatcher matcher10 = new InvocationMatcher(new MockInvocation(MOCK_CLASS, this.method), Arrays.asList(matcher9));
        assertTrue(matcher10.matches(new MockInvocation(MOCK_CLASS, this.method, new Object[]{Arrays.asList(new ArrayList<>())})));
        
        // Test matches with arguments capture
        ArgumentCaptor<List<String>> captor = ArgumentCaptor.forClass(List.class);
        InvocationMatcher matcher11 = new InvocationMatcher(new MockInvocation(MOCK_CLASS, this.method), Arrays.asList(captor));
        Verifications.verify(this.mock).someMethod((List<String>) Capture.allValues(captor));
        assertTrue(matcher11.matches(this.verificationResult.getInvocations().get(0)));
        
        // Test matches with multiple arguments capture
        ArgumentCaptor<List<String>> captor1 = ArgumentCaptor.forClass(List.class);
        ArgumentCaptor<List<String>> captor2 = ArgumentCaptor.forClass(List.class);
        InvocationMatcher matcher12 = new InvocationMatcher(new MockInvocation(MOCK_CLASS, this.method),
                Arrays.asList(captor1, captor2));
        Verifications.verify(this.mock).someMethod(argThat(new TypeSafeMatcher<List<String>>() {
            @Override
            public boolean matchesSafely(List<String> items1) {
                List<String> items2 = (List<String>) Capture.allValues(captor2);
                return items1.equals(items2);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("the same list as captured by the second argument captor");
            }
        }), Capture.allValues(captor1));
        assertTrue(matcher12.matches(this.verificationResult.getInvocations().get(0)));
        
        // Test createFrom
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(new MockInvocation(MOCK_CLASS, this.method));
        invocations.add(new MockInvocation(MOCK_CLASS, this.method, new Object[]{"foo", "bar"}));
        List<InvocationMatcher> matchersList = InvocationMatcher.createFrom(Collections2.newList(invocations));
        InvocationMatcher matcher13 = matchersList.get(0);
        InvocationMatcher matcher14 = matchersList.get(1);
        assertTrue(matcher13.matches(new MockInvocation(MOCK_CLASS, this.method)));
        assertTrue(matcher14.matches(new MockInvocation(MOCK_CLASS, this.method, new Object[]{"foo", "bar"})));
    }

    private static class TestPrint implements PrintSettings {
        @Override
        public String print(Object value) {
            return value.toString();
        }

        @Override
        public String print(Matcher matcher) {
            return matcher.description().toString();
        }

        @Override
        public String print(Object value, Matcher matcher) {
            return String.format("%s = %s", matcher.description().toString(), value.toString());
        }

        @Override
        public String print(Matcher matcher1, Matcher matcher2) {
            return String.format("%s = %s, %s = %s", matcher1.description().toString(),
                    matcher1.getDescription().toString(), matcher2.description().toString(),
                    matcher2.getDescription().toString());
        }

        @Override
        public String print(Matcher matcher, Object[] args) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            for (int i = 0; i < args.length; i++) {
                sb.append(print(matcher, args[i]));
                if (i < args.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("}");
            return sb.toString();
        }

        @Override
        public String print(Matcher matcher, Object value) {
            return String.format("%s = %s", matcher.description().toString(), value.toString());
        }
    }
}