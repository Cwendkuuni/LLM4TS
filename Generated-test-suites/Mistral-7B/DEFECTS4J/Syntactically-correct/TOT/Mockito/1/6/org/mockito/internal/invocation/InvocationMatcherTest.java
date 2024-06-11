package org.mockito.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testEquals() {
        Object mock = mock(String.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        when(invocation1.getMethod()).thenReturn(new MethodStub("method1") {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[] { String.class, int.class };
            }
        });
        when(invocation2.getMethod()).thenReturn(new MethodStub("method1") {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[] { String.class, int.class };
            }
        });

        Matcher expectedMatcher = ArgumentMatchers.argThat(new ArgumentMatcher<Invocation>() {
            @Override
            public boolean matches(Invocation argument) {
                return argument.getMock().equals(mock)
                        && argument.getMethod().getName().equals("method1")
                        && Arrays.equals(argument.getArguments(), new Object[] { "test", 1 });
            }
        });

        InvocationMatcher matcher3 = new InvocationMatcher(invocation1, Arrays.asList(expectedMatcher));
        InvocationMatcher matcher4 = new InvocationMatcher(invocation2, Arrays.asList(expectedMatcher));

        assertTrue(matcher1.matches(matcher3));
        assertTrue(matcher3.matches(matcher1));
        assertTrue(matcher1.matches(matcher4));
        assertTrue(matcher4.matches(matcher1));

        assertTrue(matcher1 != matcher2);
        assertFalse(matcher1.matches(matcher2));
        assertFalse(matcher2.matches(matcher1));
    }

    @Test
    public void testHasSameMethod() {
        Object mock = mock(String.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        when(invocation1.getMethod()).thenReturn(new MethodStub("method1") {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[] { String.class, int.class };
            }
        });
        when(invocation2.getMethod()).thenReturn(new MethodStub("method2") {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[] { String.class, int.class };
            }
        });

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.hasSameMethod(matcher2));

        when(invocation2.getMethod()).thenReturn(new MethodStub("method1") {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[] { String.class, int.class };
            }
        });

        assertTrue(matcher1.hasSameMethod(matcher2));
    }

    @Test
    public void testToString() {
        Object mock = mock(String.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.anyInt());
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String toString = matcher.toString();
        assertTrue(toString.contains("method"));
        assertTrue(toString.contains("test"));
        assertTrue(toString.contains("any int"));
    }

    @Test
    public void testCaptureArguments() {
        Object mock = mock(String.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(
                ArgumentMatchers.argThat(new ArgumentMatcher<String>() {
                    @Override
                    public boolean matches(String argument) {
                        return "test".equals(argument);
                    }
                }),
                ArgumentMatchers.argThat(ArgumentMatchers.instanceOf(String.class))
        );
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        when(invocation.getMethod()).thenReturn(new MethodStub("method") {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[] { String.class };
            }
        });
        Object test = "test";
        Object argument = "test2";

        matcher.captureArgumentsFrom(Mockito.mock(InvocationOnMock.class));

        verify(mock).method(test);

        assertTrue(matcher.getMatchers().get(0).matches("test"));
        assertTrue(matcher.getMatchers().get(1).matches(argument));
    }

    @Test
    public void testCaptureArgumentsVarargs() {
        Object mock = mock(String[].class);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(
                ArgumentMatchers.argThat(new ArgumentMatcher<Object[]>() {
                    @Override
                    public boolean matches(Object[] argument) {
                        return Arrays.equals(new String[]{"test1", "test2"}, argument);
                    }
                }),
                ArgumentMatchers.argThat(ArgumentMatchers.instanceOf(String[].class))
        );
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        when(invocation.getMethod()).thenReturn(new MethodStub("method") {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[] { String[].class };
            }
        });
        String[] testArgs = new String[]{"test1", "test2"};
        String[] arguments = new String[]{"test1", "test3", "test4"};

        matcher.captureArgumentsFrom(Mockito.mock(InvocationOnMock.class));

        verify(mock).method(testArgs);

        assertTrue(matcher.getMatchers().get(0).matches(testArgs));
        assertTrue(matcher.getMatchers().get(1).matches(arguments));
    }

    @Test
    public void testCaptureArgumentsInvalidType() {
        Object mock = mock(String.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(
                ArgumentMatchers.argThat(new ArgumentMatcher<String>() {
                    @Override
                    public boolean matches(String argument) {
                        return "test".equals(argument);
                    }
                }),
                ArgumentMatchers.argThat(ArgumentMatchers.instanceOf(Integer.class))
        );
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        when(invocation.getMethod()).thenReturn(new MethodStub("method") {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[] { String.class, int.class };
            }
        });

        try {
            matcher.captureArgumentsFrom(Mockito.mock(InvocationOnMock.class));
            fail();
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testHasSimilarMethod() {
        Object mock = mock(String.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);

        when(invocation1.getMethod()).thenReturn(new MethodStub("method1") {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[] { String.class, int.class };
            }
        });
        when(invocation2.getMethod()).thenReturn(new MethodStub("method2") {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[] { String.class, int.class };
            }
        });

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.hasSimilarMethod(matcher2));

        when(invocation1.getMethod()).thenReturn(new MethodStub("method1") {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[] { String.class, int[].class };
            }
        });
        when(invocation2.getMethod()).thenReturn(new MethodStub("method1") {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[] { String.class, int.class };
            }
        });

        assertTrue(matcher1.hasSimilarMethod(matcher2));

        when(invocation2.getMethod()).thenReturn(new MethodStub("method3") {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[] { String.class, int.class };
            }
        });

        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testCreateFrom() {
        Object mock = mock(String.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        when(invocation1.getMethod()).thenReturn(new MethodStub("method1") {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[] { String.class, int.class };
            }
        });
        when(invocation2.getMethod()).thenReturn(new MethodStub("method1") {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[] { String.class, int.class };
            }
        });

        Matcher expectedMatcher = ArgumentMatchers.argThat(new ArgumentMatcher<Invocation>() {
            @Override
            public boolean matches(Invocation argument) {
                return argument.getMock().equals(mock)
                        && argument.getMethod().getName().equals("method1")
                        && Arrays.equals(argument.getArguments(), new Object[] { "test", 1 });
            }
        });

        InvocationMatcher matcher3 = new InvocationMatcher(invocation1, Arrays.asList(expectedMatcher));
        InvocationMatcher matcher4 = new InvocationMatcher(invocation2, Arrays.asList(expectedMatcher));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Arrays.asList(matcher1, matcher2));
        InvocationMatcher matcher5 = invocationMatchers.get(0);
        InvocationMatcher matcher6 = invocationMatchers.get(1);

        assertTrue(matcher5.matches(matcher3));
        assertTrue(matcher3.matches(matcher5));
        assertTrue(matcher5.matches(matcher4));
        assertTrue(matcher4.matches(matcher5));
    }
}

class MethodStub implements Method {
    private String name;
    private Class<?>[] parameterTypes;

    public MethodStub(String name, Class<?>... parameterTypes) {
        this.name = name;
        this.parameterTypes = parameterTypes;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public int getParameterCount() {
        return parameterTypes.length;
    }

    @Override
    public Class<?> getReturnType() {
        return Object.class;
    }

    @Override
    public Class<?>[] getExceptionTypes() {
        return new Class[0];
    }

    @Override
    public int getModifiers() {
        return 0;
    }

    @Override
    public Answer<Object> invoke(Object obj, Object... args) throws Throwable {
        return null;
    }
}