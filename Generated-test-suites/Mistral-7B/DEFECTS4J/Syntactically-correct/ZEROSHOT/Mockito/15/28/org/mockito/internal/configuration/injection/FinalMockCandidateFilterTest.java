import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FinalMockCandidateFilterTest {

    @Rule
    public TestRule mockitoRule = MockitoJUnit.rule();

    private FinalMockCandidateFilter filter;
    private Collection<Object> mocks;
    private Field field;
    private Object fieldInstance;

    @Before
    public void setup() {
        filter = new FinalMockCandidateFilter();
        mocks = new ArrayList<>();
        field = new Field("fieldName", String.class);
        fieldInstance = new Object() {};
    }

    @Test
    public void testFilterCandidate_singleMock_injectViaProperty() {
        Object mock = mock(String.class);
        mocks.add(mock);

        Stubber stubber = Mockito.mock(Stubber.class);
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                new FieldSetter(fieldInstance, field).set(mock, "testValue");
                return null;
            }
        }).when(stubber).doAnswer(any());

        when(mock.getClass()).thenReturn(String.class);
        when(mock.getDeclaredField("fieldName")).thenReturn(field);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(injecter.thenInject());
        verify(mockitoRule.getMockConstructionNotifications()).newMockCreated(String.class);

        injecter = spy(injecter);
        stubber.doAnswer(invocation -> injecter.thenInject());

        assertEquals(true, injecter.thenInject());
        verify(mock).setField("fieldName", fieldInstance);
        assertEquals("testValue", fieldInstance.getClass().getDeclaredField("fieldName").get(mock));
    }

    @Test
    public void testFilterCandidate_singleMock_failToInjectViaProperty() {
        Object mock = mock(String.class);
        mocks.add(mock);

        when(mock.getClass()).thenReturn(String.class);
        when(mock.getDeclaredField("fieldName")).thenReturn(field);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(injecter.thenInject());
        verify(mockitoRule.getMockConstructionNotifications()).newMockCreated(String.class);

        assertThrows(MockitoException.class, () -> {
            injecter.thenInject();
        });
    }

    @Test
    public void testFilterCandidate_multipleMocks_returnFailInjecter() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        mocks.addAll(Arrays.asList(mock1, mock2));

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(injecter.thenInject());
    }

    @Test
    public void testFilterCandidate_multipleMocks_injectViaProperty_firstMockSucceeds() {
        Object mock1 = mock(String.class);
        Object mock2 = mock(String.class);
        mocks.addAll(Arrays.asList(mock1, mock2));

        Stubber stubber1 = Mockito.mock(Stubber.class);
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                new FieldSetter(fieldInstance, field).set(mock1, "testValue1");
                return null;
            }
        }).when(stubber1).doAnswer(any());

        when(mock1.getClass()).thenReturn(String.class);
        when(mock1.getDeclaredField("fieldName")).thenReturn(field);

        Stubber stubber2 = Mockito.mock(Stubber.class);
        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                new FieldSetter(fieldInstance, field).set(mock2, "testValue2");
                return null;
            }
        }).when(stubber2).doAnswer(any());

        when(mock2.getClass()).thenReturn(String.class);
        when(mock2.getDeclaredField("fieldName")).thenReturn(field);

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(injecter.thenInject());
        verify(mockitoRule.getMockConstructionNotifications()).newMockCreated(String.class).times(2);

        injecter = spy(injecter);
        stubber1.doAnswer(invocation -> injecter.thenInject());
        stubber2.doAnswer(invocation -> injecter.thenInject());

        assertEquals(true, injecter.thenInject());
        assertEquals("testValue1", fieldInstance.getClass().getDeclaredField("fieldName").get(mock1));
        assertEquals("testValue2", fieldInstance.getClass().getDeclaredField("fieldName").get(mock2));
    }
}