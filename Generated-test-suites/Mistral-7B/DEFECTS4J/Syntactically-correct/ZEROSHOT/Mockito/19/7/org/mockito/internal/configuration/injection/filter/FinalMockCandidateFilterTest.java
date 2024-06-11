package org.mockito.internal.configuration.injection.filter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.MockitoConfiguration;
import org.mockito.internal.configuration.MockitoConfigurationBuilder;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.SimpleReflectionUtil;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private List<Object> mocks;
    private Object fieldInstance;
    private Field field;
    private Reporter reporter;

    @Before
    public void before() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        fieldInstance = mock(Object.class);
        field = SimpleReflectionUtil.getDeclaredField(fieldInstance, "fieldToBeInjected");
        reporter = mock(Reporter.class);
    }

    @Test
    public void testFilterCandidateWhenOneMockAvailable() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        when(filter.filterCandidate(mocks, field, fieldInstance)).thenReturn(createOngoingInjecter(mock));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        Object result = injecter.thenInject();

        verify(mock).equals(fieldInstance);
        verify(reporter).never().cannotInjectDependency(field, mock, any(RuntimeException.class));
    }

    @Test
    public void testFilterCandidateWhenOneMockIsNotAbleToInject() {
        Object mock = mock(Object.class);
        mocks.add(mock);

        when(filter.filterCandidate(mocks, field, fieldInstance)).thenReturn(createOngoingInjecter(mock));
        when(mock.getClass()).thenReturn(Object.class); // mock is not the same class as fieldInstance

        try {
            filter.filterCandidate(mocks, field, fieldInstance);
            fail();
        } catch (RuntimeException e) {
            verify(reporter).cannotInjectDependency(field, mock, e);
        }
    }

    @Test
    public void testFilterCandidateWhenMultipleMocksAvailable() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        verify(injecter).thenInject();
        verify(reporter).never().cannotInjectDependency(field, mock1, any(RuntimeException.class));
        verify(reporter).never().cannotInjectDependency(field, mock2, any(RuntimeException.class));
    }

    private OngoingInjecter createOngoingInjecter(Object mock) {
        return new OngoingInjecter() {
            @Override
            public Object thenInject() {
                BeanPropertySetter propertySetter = new BeanPropertySetter(fieldInstance, field);
                propertySetter.set(mock);
                return mock;
            }
        };
    }
}