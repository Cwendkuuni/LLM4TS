package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        ReturnsEmptyValues answer = Mockito.mock(Answer.class);

        Object mock = mock(String.class);
        InvocationOnMock invocation = Mockito.mockMethod(mock, "toString");

        String result = (String) answer.answer(invocation);

        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareToMethod() {
        ReturnsEmptyValues answer = Mockito.mock(Answer.class);

        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        InvocationOnMock invocation = Mockito.mockMethod(mock1, "compareTo", mock2);

        int result = (Integer) answer.answer(invocation);

        verify(mock1).equals(mock2); // mocks should be equals, so compareTo() should return 0
        assertEquals(0, result);
    }

    @Test
    public void testPrimitiveTypes() {
        ReturnsEmptyValues answer = Mockito.mock(Answer.class);

        int expectedInteger = 1;
        int resultInteger;

        byte expectedByte = 1;
        byte resultByte;

        short expectedShort = 1;
        short resultShort;

        char expectedChar = 'A';
        char resultChar;

        boolean expectedBoolean = true;
        boolean resultBoolean;

        InvocationOnMock integerInvocation = Mockito.mockMethod(answer, "answer", ArgumentMatchers.method(Integer.TYPE, null));
        InvocationOnMock byteInvocation = Mockito.mockMethod(answer, "answer", ArgumentMatchers.method(Byte.TYPE, null));
        InvocationOnMock shortInvocation = Mockito.mockMethod(answer, "answer", ArgumentMatchers.method(Short.TYPE, null));
        InvocationOnMock charInvocation = Mockito.mockMethod(answer, "answer", ArgumentMatchers.method(CharType.TYPE, null));
        InvocationOnMock booleanInvocation = Mockito.mockMethod(answer, "answer", ArgumentMatchers.method(Boolean.TYPE, null));

        resultInteger = (int) answer.answer(integerInvocation);
        resultByte = (byte) answer.answer(byteInvocation);
        resultShort = (short) answer.answer(shortInvocation);
        resultChar = (char) answer.answer(charInvocation);
        resultBoolean = (boolean) answer.answer(booleanInvocation);

        assertEquals(expectedInteger, expectedInteger);
        assertEquals(expectedByte, resultByte);
        assertEquals(expectedShort, resultShort);
        assertEquals(expectedChar, resultChar);
        assertEquals(expectedBoolean, resultBoolean);

        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(Integer.TYPE), resultInteger);
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(Byte.TYPE), resultByte);
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(Short.TYPE), resultShort);
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(CharType.TYPE), resultChar);
        assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(Boolean.TYPE), resultBoolean);
    }

    @Test
    public void testCollectionTypes() {
        ReturnsEmptyValues answer = Mockito.mock(Answer.class);

        Collection<String> expectedCollection = new ArrayList<>();
        Collection<String> resultCollection;

        InvocationOnMock collectionInvocation = Mockito.mockMethod(answer, "answer", ArgumentMatchers.method(Collection.class, null));

        resultCollection = (Collection<String>) answer.answer(collectionInvocation);

        assertEquals(expectedCollection, resultCollection);
        assertEquals(0, resultCollection.size());
    }
}