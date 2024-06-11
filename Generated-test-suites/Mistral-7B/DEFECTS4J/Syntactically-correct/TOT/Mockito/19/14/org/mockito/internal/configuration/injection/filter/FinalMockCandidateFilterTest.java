package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object fieldInstance;
    private Field field;
    private Collection<Object> mocks;
    private Reporter reporter;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        fieldInstance = mock(Object.class);
        field = Mockito.spy(new Field("testField", String.class, fieldInstance) {
            @Override
            public Object get(Object obj) {
                return "defaultValue";
            }
        });
        mocks = new ArrayList<>();
        reporter = Mockito.spy(new Reporter() {});
    }

    @Test
    public void testSingleMock() {
        Object mock = mock(String.class);
        mocks.add(mock);

        FinalMockCandidateFilter filterSpy = spy(filter);
        when(filterSpy.filterCandidate(mocks, field, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                Object result = invocation.callRealMethod();
                verify(reporter).cannotInjectDependency(ArgumentMatchers.notNull(), ArgumentMatchers.notNull(Object.class), ArgumentMatchers.notNull(Throwable.class));
                return mock;
            }
        });

        filterSpy.filterCandidate(mocks, field, fieldInstance);
        verify(mock).then(ArgumentMatchers.isA(String.class));
    }

    @Test
    public void testMultipleMocks() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        mocks.add(mock1);
        mocks.add(mock2);

        filter.filterCandidate(mocks, field, fieldInstance);
        // No verification needed as the method returns an OngoingInjecter that does nothing in this case
    }

    @Test(expected = RuntimeException.class)
    public void testFailedPropertySetter() {
        Object mock = mock(String.class);
        mocks.add(mock);

        when(mock.getClass()).thenReturn(String.class);
        when(mock.hasProperty("testProperty")).thenReturn(false);
        when(fieldInstance.getClass()).thenReturn(String.class);

        FinalMockCandidateFilter filterSpy = spy(filter);
        when(filterSpy.filterCandidate(mocks, field, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                BeanPropertySetter propertySetter = new BeanPropertySetter(fieldInstance, field);
                propertySetter.set(mock);
                throw new RuntimeException("Test exception");
            }
        });

        filterSpy.filterCandidate(mocks, field, fieldInstance);
        verify(reporter).cannotInjectDependency(ArgumentMatchers.notNull(), ArgumentMatchers.notNull(Object.class), ArgumentMatchers.notNull(Throwable.class));
    }

    @Test(expected = RuntimeException.class)
    public void testFailedFieldSetter() {
        Object mock = mock(String.class);
        mocks.add(mock);

        when(mock.getClass()).thenReturn(String.class);
        when(fieldInstance.getClass()).thenReturn(String.class);

        FinalMockCandidateFilter filterSpy = spy(filter);
        when(filterSpy.filterCandidate(mocks, field, fieldInstance)).thenAnswer(new Answer<OngoingInjecter>() {
            @Override
            public OngoingInjecter answer(InvocationOnMock invocation) throws Throwable {
                FieldSetter fieldSetter = new FieldSetter(fieldInstance, field);
                fieldSetter.set(mock);
                throw new RuntimeException("Test exception");
            }
        });

        filterSpy.filterCandidate(mocks, field, fieldInstance);
        verify(reporter).cannotInjectDependency(ArgumentMatchers.notNull(), ArgumentMatchers.notNull(Object.class), ArgumentMatchers.notNull(Throwable.class));
    }
}