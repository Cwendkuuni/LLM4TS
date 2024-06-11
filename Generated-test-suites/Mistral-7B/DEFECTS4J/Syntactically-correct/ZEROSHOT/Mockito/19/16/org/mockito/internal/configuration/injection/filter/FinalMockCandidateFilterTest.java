package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.internal.util.reflection.InjectionPoint;
import org.mockito.internal.util.reflection.MockFactory;
import org.mockito.internal.util.reflection.TypeToken;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private List<Object> mocks;
    private Object fieldInstance;
    private Field field;
    private Reporter reporter;

    @Rule
    public TestRule testRule = new TestRule() {
        @Override
        public Statement apply(Statement base, Description description) {
            return super.apply(base, description).passing(new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    filter = new FinalMockCandidateFilter();
                    mocks = new ArrayList<>();
                    fieldInstance = mock(Object.class);
                    field = FieldType.getField(fieldInstance, "testField");
                    reporter = mock(Reporter.class);
                }
            });
        }
    };

    @Before
    public void setup() {
        InjectionPoint injectionPoint = new InjectionPoint(TypeToken.of(Object.class), new TypeToken<Field>() {}, new Object[]{fieldInstance}, new Object[]{field}, null, null);
        MockFactory mockFactory = mock(MockFactory.class);
        FilterChain filterChain = spy(filterChain(filter, mocks, fieldInstance, field, reporter, mockFactory));
        when(filter.filterCandidate(eq(mocks), eq(field), eq(fieldInstance))).thenReturn(filterChain);
    }

    private FilterChain filterChain(FinalMockCandidateFilter filter, Collection<Object> mocks, Object fieldInstance, Field field, Reporter reporter, MockFactory mockFactory) {
        return mock(FilterChain.class);
    }

    @org.junit.Test
    public void testFilterCandidate_singleMock_propertySetterSuccess() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();

        verify(mock).equals(fieldInstance);
    }

    @org.junit.Test
    public void testFilterCandidate_singleMock_propertySetterFailure() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        doThrow(new RuntimeException("Test exception")).when(new BeanPropertySetter(fieldInstance, field)).set(mock);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();

        verify(reporter).cannotInjectDependency(eq(field), eq(mock), eq("Test exception"));
    }

    @org.junit.Test
    public void testFilterCandidate_singleMock_fieldSetterSuccess() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        doThrow(new RuntimeException("Test exception")).when(new BeanPropertySetter(fieldInstance, field)).set(mock);
        when(new FieldSetter(fieldInstance, field).canSet(mock)).thenReturn(true);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();

        verify(mock).equals(fieldInstance);
    }

    @org.junit.Test
    public void testFilterCandidate_singleMock_fieldSetterFailure() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        doThrow(new RuntimeException("Test exception")).when(new BeanPropertySetter(fieldInstance, field)).set(mock);
        when(new FieldSetter(fieldInstance, field).canSet(mock)).thenReturn(false);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();

        verify(reporter).cannotInjectDependency(eq(field), eq(mock), eq("Test exception"));
    }

    @org.junit.Test
    public void testFilterCandidate_multipleMocks_noMatchingMock() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();

        verify(reporter).cannotInjectDependency(eq(field), eq(null), anyString());
    }

    @org.junit.Test
    public void testFilterCandidate_multipleMocks_matchingMock() {
        Object mock = mock(Object.class);
        Object anotherMock = mock(Object.class);
        mocks.add(mock);
        mocks.add(anotherMock);

        when(new BeanPropertySetter(fieldInstance, field).set(mock)).thenReturn(mock);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();

        verify(mock).equals(fieldInstance);
    }

    @org.junit.Test
    public void testFilterCandidate_multipleMocks_fieldSetterSuccess() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        doThrow(new RuntimeException("Test exception")).when(new BeanPropertySetter(fieldInstance, field)).set(mock1);
        when(new FieldSetter(fieldInstance, field).canSet(mock2)).thenReturn(true);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();

        verify(mock2).equals(fieldInstance);
    }

    @org.junit.Test
    public void testFilterCandidate_multipleMocks_fieldSetterFailure() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        doThrow(new RuntimeException("Test exception")).when(new BeanPropertySetter(fieldInstance, field)).set(mock1);
        when(new FieldSetter(fieldInstance, field).canSet(mock2)).thenReturn(false);

        OngoingInjecter ongoingInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        ongoingInjecter.thenInject();

        verify(reporter).cannotInjectDependency(eq(field), eq(mock2), eq("Test exception"));
    }

}