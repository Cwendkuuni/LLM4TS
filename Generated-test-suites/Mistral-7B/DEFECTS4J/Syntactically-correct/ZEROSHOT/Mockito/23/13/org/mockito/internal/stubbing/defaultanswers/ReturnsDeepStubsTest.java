package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.mockito.internal.stubbing.answers.ReturnsDeepStubsAnswer.deepStubsAnswer;
import static org.mockito.internal.stubbing.answers.ReturnsDeepStubsAnswer.returnsDeepStubsAnswer;

import static org.mockito.mockito3.Mockito.mock;
import static org.mockito.mockito3.Mockito.whenAnswer;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

    @Mock
    private GenericsNest<String> genericsNestMock;

    @Mock
    private GenericsNest<Number> genericsNestNumberMock;

    @Test
    public void testReturnsDeepStubs() {
        final GenericsNest<String> genericsNestString = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
        final GenericsNest<Number> genericsNestNumber = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);

        when(genericsNestString.entrySet()).thenReturn(Collections.singletonMap("key", genericsNestNumberMock));
        when(genericsNestNumber.iterator()).thenReturn(Collections.singletonList(1L).iterator());

        GenericsNest<Number> deepGeneratedNestNumber = genericsNestString.entrySet().iterator().next().getValue().iterator().next();
        Number deepGeneratedNumber = deepGeneratedNestNumber.iterator().next();

        //Test deep stubs
        GenericsNest<Number> deepStubGeneratedNestNumber = (GenericsNest<Number>) genericsNestString;
        GenericsNest<Number> deepStubGeneratedNestNumber2 = (GenericsNest<Number>) deepStubGeneratedNestNumber.entrySet().iterator().next().getValue();
        Number deepStubGeneratedNumber = deepStubGeneratedNestNumber2.iterator().next();

        verify(genericsNestNumber).iterator();
        verify(deepGeneratedNestNumber).iterator();
        verify(deepStubGeneratedNestNumber).iterator();
        verify(deepStubGeneratedNestNumber2).iterator();
        verify(deepStubGeneratedNumber).equals(1L);
        verify(deepStubGeneratedNumber).hashCode();

        //Test deep stubs with nested generic
        GenericsNest<Map<String, Set<Number>>> genericsNestMap = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
        Map<String, Set<Number>> deepGeneratedMap = genericsNestMap.entrySet().iterator().next().getValue();
        Set<Number> deepGeneratedSet = deepGeneratedMap.values().iterator().next();
        Number deepGeneratedNumber = deepGeneratedSet.iterator().next();

        verify(deepGeneratedMap.values()).iterator().next().iterator().next().equals(1L);

        //Test deep stubs with different types
        GenericsNest<String> genericsNestString2 = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
        GenericsNest<Number> genericsNestNumber3 = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
        whenAnswer(Answers.returnsDeepStubsAnswer(GenericsNest.class, String.class, Number.class))
                .givenAnswer(returnsDeepStubsAnswer(GenericsNest.class));

        when(genericsNestString2.entrySet()).thenReturn(Collections.singletonMap("key", genericsNestNumber3));
        when(genericsNestNumber3.iterator()).thenReturn(Collections.singletonList(1L).iterator());

        GenericsNest<Number> deepGeneratedNestNumber3 = (GenericsNest<Number>) genericsNestString2.entrySet().iterator().next().getValue();
        Number deepGeneratedNumber3 = deepGeneratedNestNumber3.iterator().next();

        verify(genericsNestNumber3).iterator();
        verify(deepGeneratedNestNumber3).iterator();
        verify(deepGeneratedNumber3).equals(1L);
        verify(deepGeneratedNumber3).hashCode();
    }

    @Test(expected = RuntimeException.class)
    public void testNonMockableTypeReturnsEmptyValues() {
        final Object nonMockableObject = new Object() {};
        when(mockitoCore.isTypeMockable(nonMockableObject.getClass())).thenReturn(false);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.answer(Mockito.mock(Object.class).invoke(ArgumentMatchers.any()));

        throw new RuntimeException("Expected a mock but got empty values");
    }
}

class ReturnsDeepStubsAnswer implements Answer<Object>, Serializable {
    public static final ReturnsDeepStubsAnswer RETURNS_DEEP_STUBS = new ReturnsDeepStubsAnswer();

    @Override
    public Object answer(InvocationOnMock invocation) throws Throwable {
        return new ReturnsDeepStubs().answer(invocation);
    }

    @Override
    public void answer(InvocationOnMock invocation, Object answer) throws Throwable {
        throw new UnsupportedOperationException("Method answer(InvocationOnMock, Object) is not supported.");
    }

    public static ReturnsDeepStubsAnswer deepStubsAnswer(Class<?> rawType, Class<?>... rawExtraInterfaces) {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.setDelegate(new ReturnsEmptyValues());
        returnsDeepStubs.mockitoCore = new MockitoCore();
        returnsDeepStubs = new ReturnsDeepStubs() {
            @Override
            protected GenericMetadataSupport actualParameterizedType(Object mock) {
                return GenericMetadataSupport.forType(rawType, rawExtraInterfaces);
            }
        };

        return returnsDeepStubs;
    }
}