package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.FieldType;
import org.mockito.internal.util.reflection.SimpleField;
import org.mockito.internal.util.reflection.SimpleType;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.OngoingStubbing;

import java.lang.reflect.Collection;
import java.lang.reflect.Field;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    @Rule
    public TestRule mockitoRules = mockitoRules();

    private Reporter reporter;
    private SimpleField field;
    private Object fieldInstance;
    private Collection<Object> mocks;
    private BeanPropertySetter propertySetter;
    private FieldSetter fieldSetter;
    private FinalMockCandidateFilter filter;

    @Before
    public void setUp() {
        reporter = mock(Reporter.class);
        field = new SimpleField(new SimpleType("String"), "field", new FieldType("public", "field", "java.lang.String"));
        fieldInstance = mock(Object.class);
        mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        propertySetter = spy(new BeanPropertySetter(fieldInstance, field));
        fieldSetter = spy(new FieldSetter(fieldInstance, field));
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void shouldReturnOngoingInjecterWithSingleMock() {
        // given
        OngoingInjecter expectedInjecter = new OngoingInjecter() {
            @Override
            public Object thenInject() {
                propertySetter.set(mock(Object.class), fieldInstance);
                return mock(Object.class);
            }
        };

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        verify(propertySetter).set(mock(Object.class), fieldInstance);
        verify(fieldSetter).set(eq(mock(Object.class)), eq(fieldInstance));
        verifyZeroInteractions(reporter);
        verify(mocks.iterator()).next();
        verify(mocks).size() == 1;
        verify(actualInjecter).thenInject();
        verify(actualInjecter).getClass().isAssignableFrom(expectedInjecter.getClass());
    }

    @Test
    public void shouldReturnOngoingInjecterWithMultipleMocks() {
        // given

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        verifyZeroInteractions(reporter);
        verifyZeroInteractions(propertySetter);
        verifyZeroInteractions(fieldSetter);
        verify(mocks.iterator()).next();
        verify(mocks).size() > 1;
        verify(actualInjecter).thenInject();
    }

    @Test
    public void shouldReportErrorWhenCannotInjectWithSingleMock() {
        // given
        when(propertySetter.set(mock(Object.class), fieldInstance)).thenThrow(new RuntimeException("Cannot set property"));

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        verify(reporter).cannotInjectDependency(eq(field), eq(mock(Object.class)), instanceOf(RuntimeException.class));
        verifyZeroInteractions(propertySetter);
        verifyZeroInteractions(fieldSetter);
        verify(mocks.iterator()).next();
        verify(mocks).size() == 1;
    }

    @Test
    public void shouldReportErrorWhenCannotInjectWithMultipleMocks() {
        // given

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        verifyZeroInteractions(reporter);
        verifyZeroInteractions(propertySetter);
        verifyZeroInteractions(fieldSetter);
        verify(mocks.iterator()).next();
        verify(mocks).size() > 1;
    }

    @Test
    public void shouldNotInjectWithNullMockCollection() {
        // given
        Collection<Object> nullMocks = null;

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(nullMocks, field, fieldInstance);

        // then
        verifyZeroInteractions(reporter);
        verifyZeroInteractions(propertySetter);
        verifyZeroInteractions(fieldSetter);
        verify(mocks).isNull();
    }

    @Test
    public void shouldNotInjectWithNullField() {
        // given
        Field nullField = null;

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, nullField, fieldInstance);

        // then
        verifyZeroInteractions(reporter);
        verifyZeroInteractions(propertySetter);
        verifyZeroInteractions(fieldSetter);
        verify(field).isNull();
    }

    @Test
    public void shouldNotInjectWithNullFieldInstance() {
        // given
        Object nullFieldInstance = null;

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, nullFieldInstance);

        // then
        verifyZeroInteractions(reporter);
        verifyZeroInteractions(propertySetter);
        verifyZeroInteractions(fieldSetter);
        verify(fieldInstance).isNull();
    }

    @Test
    public void shouldNotInjectWithEmptyMockCollection() {
        // given
        Collection<Object> emptyMocks = Arrays.asList();

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(emptyMocks, field, fieldInstance);

        // then
        verifyZeroInteractions(reporter);
        verifyZeroInteractions(propertySetter);
        verifyZeroInteractions(fieldSetter);
        verify(mocks).isEmpty();
    }

    @Test
    public void shouldDelegateToOngoingInjecterWithNullResult() {
        // given

        // when
        OngoingInjecter actualInjecter = filter.filterCandidate(mocks, field, fieldInstance);

        // then
        verifyZeroInteractions(reporter);
        verifyZeroInteractions(propertySetter);
        verifyZeroInteractions(fieldSetter);
        verify(actualInjecter).thenInject();
    }
}