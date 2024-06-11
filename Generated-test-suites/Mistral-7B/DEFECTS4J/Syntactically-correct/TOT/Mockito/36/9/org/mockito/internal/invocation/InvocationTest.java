package org.mockito.internal.invocation;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ExactInvocationMatcher;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.stub.Stub;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.isNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link Invocation}.
 */
public class InvocationTest {

    @Test
    public void testEmptyInvocation() {
        // Given
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(String.class, "toString", new Class[0]), new Object[0], 1, new RealMethod() {
            public Object invoke(Object obj, Object[] objects) {
                return "Mockito";
            }
        });

        // When
        String result = invocation.callRealMethod();

        // Then
        assertThat(result, isInstanceOf(String.class));
        assertThat(result, is(Matchers.equalTo("Mockito")));
    }

    @Test
    public void testSimpleInvocation() {
        // Given
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(String.class, "toString", new Class[]{Integer.TYPE}), new Object[]{1}, 1, new RealMethod() {
            public Object invoke(Object obj, Object[] objects) {
                return "Mockito - " + ((Integer) objects[0]).intValue();
            }
        });

        // When
        String result = invocation.callRealMethod();

        // Then
        assertThat(result, isInstanceOf(String.class));
        assertThat(result, is(Matchers.equalTo("Mockito - 1")));
    }

    @Test
    public void testVarArgsInvocation() {
        // Given
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(String.class, "format", new Class[]{String.class, Integer.TYPE, Integer.TYPE}), new Object[]{"%s %d %d", 1, 2, 3}, 1, new RealMethod() {
            public Object invoke(Object obj, Object[] objects) {
                return String.format("%s %d %d", objects[0], objects[1], objects[2]);
            }
        });

        // When
        String result = invocation.callRealMethod();

        // Then
        assertThat(result, isInstanceOf(String.class));
        assertThat(result, is(Matchers.equalTo("%s 1 2 %d 3".formatted("Mockito", 1, 2, 3))));
    }

    @Test
    public void testExactInvocationWithArgs() {
        // Given
        Object mock = mock(String.class);
        Invocation expectedInvocation = new Invocation(mock, new MockitoMethod(String.class, "format", new Class[]{String.class, Integer.TYPE, Integer.TYPE}), new Object[]{"%s %d %d"}, 1, new RealMethod() {
            public Object invoke(Object obj, Object[] objects) {
                return String.format("%s %d %d", objects[0], objects[1], objects[2]);
            }
        });

        Stub stub = Mockito.stub(mock.getClass()).toReturn(mock);
        ArgumentCaptor<Invocation> invocationCaptor = ArgumentCaptor.forClass(Invocation.class);

        // When
        String result = stub.callRealMethod("{} %d %d", 1, 2);

        // Then
        verify(mock).format("%s %d %d", Mockito.anyString(), Mockito.eq(1), Mockito.eq(2));
        Invocation capturedInvocation = invocationCaptor.getValue();
        assertThat(capturedInvocation, isA(Invocation.class));
        assertThat(capturedInvocation, is(equalTo(expectedInvocation)));
    }

    @Test
    public void testExactInvocationWithException() {
        // Given
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(String.class, "format", new Class[]{String.class, Integer.TYPE, Integer.TYPE}), new Object[]{"%s %d %d"}, 1, new RealMethod() {
            public Object invoke(Object obj, Object[] objects) throws RuntimeException {
                if (objects[1].equals(3)) {
                    throw new RuntimeException();
                }
                return String.format("%s %d %d", objects[0], objects[1], objects[2]);
            }
        });

        // When
        try {
            invocation.callRealMethod();
        } catch (RuntimeException e) {
            // Then
            assertThat(e, isA(RuntimeException.class));
            return;
        }

        fail("Expected 'RuntimeException' to be thrown.");
    }

    @Test
    public void testVoidInvocation() {
        // Given
        Object mock = mock(VoidConsumer.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(Void.TYPE, "accept", new Class[]{Integer.TYPE}), new Object[]{1}, 1, new RealMethod() {
            public Object invoke(Object obj, Object[] objects) {
                ((VoidConsumer) obj).accept((Integer) objects[0]);
                return null;
            }
        });

        // When
        invocation.callRealMethod();

        // Then
        verify(mock).accept(Mockito.eq(1));
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testToString() {
        // Given
        Object mock = mock(String.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(String.class, "toString", new Class[0]), new Object[0], 1, new RealMethod() {
            public Object invoke(Object obj, Object[] objects) {
                return "Mockito";
            }
        });

        // When
        String result = invocation.toString();

        // Then
        String expected = "Invocation [sequenceNumber=1, mock=Mockito, method=toString(), arguments=[], rawArguments=[], location=Invocation@7f9433b7, verified=false, verifiedInOrder=false, realMethod=RealMethod@5563c296]";
        assertThat(result, is(Matchers.equalTo(expected)));
    }

    private interface VoidConsumer {
        void accept(int value);
    }
}