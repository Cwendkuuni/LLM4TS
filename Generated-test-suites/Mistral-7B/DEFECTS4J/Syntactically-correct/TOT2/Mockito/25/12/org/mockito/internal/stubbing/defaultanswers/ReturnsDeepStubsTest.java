import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReturnsDeepStubsTest {

    @Test
    void testAnswerMethodWithValidArguments() {
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setTarget(mock);
        invocation.setMethod(new Method("testMethod", new Class[0]));

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object answer = returnsDeepStubs.answer(invocation);

        assertEquals(mock, answer);
    }

    @ParameterizedTest
    @MethodSource("edgeCasesForAnswerMethod")
    void testAnswerMethodWithEdgeCases(Object mock, Class<?> returnType, InvocationOnMock invocation) {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object answer = returnsDeepStubs.answer(invocation);

        assertEquals(mock, answer);
    }

    @Test
    void testAnswerMethodWithThrowsException() {
        Object mock = Mockito.mock(Object.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setTarget(mock);
        invocation.setMethod(new Method("testMethod", new Class[0]) {
            @Override
            public Class<?>[] getParameterTypes() {
                return new Class[0];
            }

            @Override
            public Class<?> getReturnType() {
                return Integer.class;
            }
        });
        Mockito.doThrow(new RuntimeException("Test Exception"))
                .when(mock).answer(invocation);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Assertions.assertThrows(RuntimeException.class, () -> returnsDeepStubs.answer(invocation));
    }

    static Stream<Arguments> edgeCasesForAnswerMethod() {
        Object mock1 = Mockito.mock(String.class);
        Object mock2 = Mockito.mock(Integer.class);
        Object mock3 = Mockito.mock(double.class);
        Class<?>[] types = {String.class, Integer.class, double.class};

        return Stream.of(
                Arguments.arguments(mock1, String.class, new InvocationOnMock() {/* set up */}),
                Arguments.arguments(mock2, Integer.class, new InvocationOnMock() {/* set up */}),
                Arguments.arguments(mock3, double.class, new InvocationOnMock() {/* set up */})
        );
    }
}