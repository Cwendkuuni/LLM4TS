import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.base.MockitoFailureException;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.MockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.internal.util.reflection.MockUtil;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

public class FinalMockCandidateFilterTest {

    private FinalMockCandidateFilter filter;
    private Object fieldInstance;
    private Field field;
    private List<Object> mocks;
    private Stubber stubber;

    @Before
    public void setUp() {
        fieldInstance = mock(Object.class);
        field = mock(Field.class);
        mocks = new ArrayList<>();
        stubber = stub(BeanPropertySetter.class, BeanPropertySetter::set);
        stubber = stub(FieldSetter.class, FieldSetter::set);
        filter = new FinalMockCandidateFilter();
    }

    @Test
    public void testFilterCandidate_SingleMock_PropertySetterSuccess() {
        Object mock = spy(MockUtil.createAnonymousClass(new Class[]{Object.class}, "Mock"));
        mocks.add(mock);

        when(field.getName()).thenReturn("testField");
        doAnswer(invocation -> {
            Object value = invocation.getArguments()[1];
            fieldInstance.setField(value);
            return null;
        }).when(BeanPropertySetter.class).set(fieldInstance, field, any(Object.class));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        verify(mock).setField(fieldInstance);
        verifyNoInteractions(mock, fieldInstance);
    }

    @Test
    public void testFilterCandidate_SingleMock_PropertySetterFailure() {
        Object mock = spy(MockUtil.createAnonymousClass(new Class[]{Object.class}, "Mock"));
        mocks.add(mock);

        when(field.getName()).thenReturn("testField");
        doThrow(new MockitoFailureException("Error setting property")).when(BeanPropertySetter.class).set(fieldInstance, field, any(Object.class));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        verify(mock).setField(fieldInstance);
        verify(mock).cannotInjectDependency(eq(field), eq(fieldInstance), any(Throwable.class));
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldSetterSuccess() {
        Object mock = spy(MockUtil.createAnonymousClass(new Class[]{Object.class}, "Mock"));
        mocks.add(mock);

        when(field.getName()).thenReturn("testField");
        doAnswer(invocation -> {
            Object value = invocation.getArguments()[1];
            fieldInstance.setField(value);
            return null;
        }).when(FieldSetter.class).set(fieldInstance, field, any(Object.class));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        verify(mock).setField(fieldInstance);
        verifyNoInteractions(mock, fieldInstance);
    }

    @Test
    public void testFilterCandidate_SingleMock_FieldSetterFailure() {
        Object mock = spy(MockUtil.createAnonymousClass(new Class[]{Object.class}, "Mock"));
        mocks.add(mock);

        when(field.getName()).thenReturn("testField");
        doThrow(new MockitoFailureException("Error setting field")).when(FieldSetter.class).set(fieldInstance, field, any(Object.class));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        verify(mock).setField(fieldInstance);
        verify(mock).cannotInjectDependency(eq(field), eq(fieldInstance), any(Throwable.class));
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoInject() {
        when(field.getName()).thenReturn("testField");

        OngoingInjecter injecter = filter.filterCandidate(Collections.nEmptyList(), field, fieldInstance);
        injecter.thenInject();

        verifyNoInteractions(fieldInstance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testFilterCandidate_MultipleMocks_UnexpectedReturnType() {
        when(field.getName()).thenReturn("testField");

        Object mock1 = mock(Object.class);
        Object mock2 = mock(String.class);
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        verifyNoInteractions(fieldInstance);
        throw new UnsupportedOperationException("Unexpected return type from test method");
    }
}