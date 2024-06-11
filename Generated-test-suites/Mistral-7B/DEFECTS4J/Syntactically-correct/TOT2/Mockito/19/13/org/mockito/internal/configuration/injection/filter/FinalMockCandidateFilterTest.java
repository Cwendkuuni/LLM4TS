import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Exactly;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.configuration.injection.filter.Stubber;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.ReflectUtils;
import org.mockito.internal.util.reflection.Stubber.Throw;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {
    @Test
    public void testFilterCandidate_oneMock() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = ReflectUtils.findField(Object.class, "fieldName");
        Object fieldInstance = spy(new Object() {
            public Object fieldName;
        });
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertEquals(mocks.get(0), result.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList(mock(Object.class), mock(Object.class));
        Field field = ReflectUtils.findField(Object.class, "fieldName");
        Object fieldInstance = spy(new Object() {
            public Object fieldName;
        });

        Object mock1 = mocks.get(0);
        Object mock2 = mocks.get(1);

        ArgumentCaptor<Object> mockCaptor = ArgumentCaptor.forClass(Object.class);

        when(mock1.getClass()).thenReturn(Object.class);
        when(mock1).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                assertEquals(field, args[1]);
                assertEquals(fieldInstance, args[2]);
                return mock1;
            }
        });

        when(mock2.getClass()).thenReturn(Object.class);
        when(mock2).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                assertEquals(field, args[1]);
                assertEquals(fieldInstance, args[2]);
                return null;
            }
        });

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_fieldSetterFailure() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = ReflectUtils.findField(Object.class, "fieldName");
        Object fieldInstance = spy(new Object() {
            public Object fieldName;
        });

        when(mocks.get(0).getClass()).thenReturn(Object.class);
        when(mocks.get(0)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                assertEquals(field, args[1]);
                assertEquals(fieldInstance, args[2]);
                throw new RuntimeException("Failed to set field");
            }
        });

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_fieldAccessFailure() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = ReflectUtils.findField(Object.class, "fieldName");
        Object fieldInstance = spy(new Object() {
            public Object fieldName;
        });

        when(mocks.get(0).getClass()).thenReturn(Object.class);
        when(mocks.get(0)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                assertEquals(field, args[1]);
                assertEquals(fieldInstance, args[2]);
                throw new SecurityException("Access denied");
            }
        });

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        assertNull(result);
    }

    @Test
    public void testFilterCandidate_exceptionDuringInjection() {
        MockCandidateFilter filter = new FinalMockCandidateFilter();
        Collection<Object> mocks = Arrays.asList(mock(Object.class));
        Field field = ReflectUtils.findField(Object.class, "fieldName");
        Object fieldInstance = spy(new Object() {
            public Object fieldName;
        });

        when(mocks.get(0).getClass()).thenReturn(Object.class);
        when(mocks.get(0)).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                assertEquals(field, args[1]);
                assertEquals(fieldInstance, args[2]);
                throw new RuntimeException("An error occurred during injection");
            }
        });

        try {
            filter.filterCandidate(mocks, field, fieldInstance);
        } catch (RuntimeException e) {
            assertEquals("An error occurred during injection", e.getMessage());
        }
    }
}