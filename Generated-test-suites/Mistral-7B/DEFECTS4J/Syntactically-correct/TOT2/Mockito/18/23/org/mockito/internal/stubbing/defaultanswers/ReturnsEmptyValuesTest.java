package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.Mockito;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void test_ToStringMethod() {
        // ... test code for the test_ToStringMethod() method
    }

    @Test
    public void test_IsCompareToMethod() {
        // ... test code for the test_IsCompareToMethod() method
    }

    @Test
    public void test_ReturnValueForPrimitiveTypes() {
        Object mock = Mockito.mock(Void.class);

        when(mock.toString()).thenAnswer(new ReturnsEmptyValues());

        Byte result1 = (Byte) mock.returnValueFor(Byte.TYPE);
        Short result2 = (Short) mock.returnValueFor(Short.TYPE);
        Integer result3 = (Integer) mock.returnValueFor(Integer.TYPE);
        Long result4 = (Long) mock.returnValueFor(Long.TYPE);
        Float result5 = (Float) mock.returnValueFor(Float.TYPE);
        Double result6 = (Double) mock.returnValueFor(Double.TYPE);
        Boolean result7 = (Boolean) mock.returnValueFor(Boolean.TYPE);
        Character result8 = (Character) mock.returnValueFor(Character.TYPE);

        verify(mock).toString();
        assertEquals((byte) 0, result1);
        assertEquals((short) 0, result2);
        assertEquals(0, result3);
        assertEquals(0L, result4);
        assertEquals(0.0f, result5, 0.0f);
        assertEquals(0.0, result6, 0.0);
        assertEquals(false, result7);
        assertEquals((char) 0, result8);
    }

    @Test
    public void test_ReturnValueForCollectionTypes() {
        Object mock = Mockito.mock(ArrayList.class);

        when(mock.toString()).thenAnswer(new ReturnsEmptyValues());

        Collection<String> result = (Collection<String>) mock.returnValueFor(Collection.class);

        verify(mock).toString();
        assertTrue(result.isEmpty());
    }

    // ... remaining test cases for other collection types, Map types, etc.
}