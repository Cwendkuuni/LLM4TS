package com.example.MockitoTest; // replace with your test package name

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testReturnsEmptyList() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        when(returnsEmptyValues.answer(Mockito.any(InvocationOnMock.class)))
                .then(Mockito.answers(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        if (invocation.getMethod().getReturnType() == List.class) {
                            return new LinkedList<>();
                        }
                        return null;
                    }
                }));

        Mockito.<ReturnsEmptyValues>verify(returnsEmptyValues).answer(Mockito.any(InvocationOnMock.class));
        List list = (List) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == List.class));
        verify(returnsEmptyValues).answer(Mockito.eq(invocation));

        assert list.isEmpty();
    }

    @Test
    public void testReturnsEmptySet() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        when(returnsEmptyValues.answer(Mockito.any(InvocationOnMock.class)))
                .then(Mockito.answers(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        if (invocation.getMethod().getReturnType() == Set.class) {
                            return new HashSet<>();
                        }
                        return null;
                    }
                }));

        Mockito.<ReturnsEmptyValues>verify(returnsEmptyValues).answer(Mockito.any(InvocationOnMock.class));
        Set set = (Set) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == Set.class));
        verify(returnsEmptyValues).answer(Mockito.eq(invocation));

        assert set.isEmpty();
    }

    @Test
    public void testReturnsEmptyMap() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        when(returnsEmptyValues.answer(Mockito.any(InvocationOnMock.class)))
                .then(Mockito.answers(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        if (invocation.getMethod().getReturnType() == Map.class) {
                            return new HashMap<>();
                        }
                        return null;
                    }
                }));

        Mockito.<ReturnsEmptyValues>verify(returnsEmptyValues).answer(Mockito.any(InvocationOnMock.class));
        Map map = (Map) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == Map.class));
        verify(returnsEmptyValues).answer(Mockito.eq(invocation));

        assert map.isEmpty();
    }

    @Test
    public void testToStringMethod() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

        Mockito.<ReturnsEmptyValues>verify(returnsEmptyValues).answer(Mockito.any(InvocationOnMock.class));
        Mockito.<String>verify(returnsEmptyValues).answer(Mockito.any(InvocationOnMock.class));

        String string = (String) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == String.class));
        verify(returnsEmptyValues).answer(Mockito.eq(invocation));

        assert string.isEmpty();
    }

    @Test
    public void testCompareToMethod() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

        Mockito.<ReturnsEmptyValues>verify(returnsEmptyValues).answer(Mockito.any(InvocationOnMock.class));
        Mockito.<Integer>verify(returnsEmptyValues).answer(Mockito.any(InvocationOnMock.class));

        when(returnsEmptyValues.answer(Mockito.any(InvocationOnMock.class)))
                .then(Mockito.answers(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        if (invocation.getMethod().getReturnType() == Integer.class && invocation.getArguments()[0] == returnsEmptyValues) {
                            return 0;
                        }
                        return 1;
                    }
                }));

        Integer integer = (Integer) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == Integer.class && invocation.getArguments()[0] == returnsEmptyValues));
        verify(returnsEmptyValues).answer(Mockito.eq(invocation));

        assert integer == 0;

        when(returnsEmptyValues.answer(Mockito.any(InvocationOnMock.class)))
                .then(Mockito.answers(new Answer<Object>() {
                    @Override
                    public Object answer(InvocationOnMock invocation) throws Throwable {
                        if (invocation.getMethod().getReturnType() == Integer.class && invocation.getArguments()[0] != returnsEmptyValues) {
                            return 1;
                        }
                        return null;
                    }
                }));

        Mockito.<ReturnsEmptyValues>verify(returnsEmptyValues).answer(Mockito.any(InvocationOnMock.class));
        Integer nonZeroInteger = (Integer) returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() == Integer.class && invocation.getArguments()[0] != returnsEmptyValues));
        verify(returnsEmptyValues).answer(Mockito.eq(invocation));

        assert nonZeroInteger != null;
    }

    @Test(expected = AssertionError.class)
    public void testUnknownType() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

        Object unknown = returnsEmptyValues.answer(Mockito.argThat(invocation -> invocation.getMethod().getReturnType() != String.class && invocation.getMethod().getReturnType() != Integer.class &&
                invocation.getMethod().getReturnType() != List.class && invocation.getMethod().getReturnType() != Set.class &&
                invocation.getMethod().getReturnType() != Map.class));

        Mockito.<ReturnsEmptyValues>verify(returnsEmptyValues).answer(Mockito.any(InvocationOnMock.class));
        verify(returnsEmptyValues).answer(Mockito.eq(invocation));

        assert unknown == null;
    }
}