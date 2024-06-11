import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.exceptions.Reporter;
import org.mockito.internal.configuration.injection.filter.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.filter.OngoingInjecter;
import org.mockito.internal.util.reflection.BeanPropertySetter;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FinalMockCandidateFilterTest {

    private Reporter reporter;
    private Collection<Object> mocks;
    private final Object fieldInstance = new Object();
    private final Field field = Mockito.mock(Field.class);

    @Before
    public void setup() {
        reporter = Mockito.mock(Reporter.class);
        mocks = new ArrayList<>();
    }

    @Test
    public void testFilterCandidate_SingleMock_BeanPropertySetterSuccess() {
        Object mock = mock(Object.class);
        BeanPropertySetter beanPropertySetter = new BeanPropertySetter(fieldInstance, field);
        Answer<Object> answer = Mockito.answer(beanPropertySetter::set);
        Mockito.doAnswer(answer).when(beanPropertySetter);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        verify(mock).equals(fieldInstance);
        assertNotNull(injecter.getMock());
        verify(reporter).noMoreVerifications();
    }

    @Test
    public void testFilterCandidate_SingleMock_BeanPropertySetterFailure() {
        Object mock = mock(Object.class);
        BeanPropertySetter beanPropertySetter = new BeanPropertySetter(fieldInstance, field);
        Answer<Object> answer = Mockito.answer(beanPropertySetter::set).callRealMethod();
        Mockito.doAnswer(answer).when(beanPropertySetter).set(ArgumentMatchers.any(Object.class));
        Mockito.doThrow(new RuntimeException()).when(beanPropertySetter).set(ArgumentMatchers.eq(mock));

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        try {
            filter.filterCandidate(mocks, field, fieldInstance).thenInject();
            assert false : "Test should throw an exception";
        } catch (RuntimeException e) {
            verify(reporter).cannotInjectDependency(ArgumentMatchers.eq(field), ArgumentMatchers.eq(mock), ArgumentMatchers.eq(e));
        }
    }

    @Test
    public void testFilterCandidate_MultipleMocks_SimpleInjecter() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertNotNull(injecter.getMock());
        verify(reporter).noMoreVerifications();
    }

    @Test
    public void testFilterCandidate_MultipleMocks_ComplexInjecter() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        BeanPropertySetter beanPropertySetter = new BeanPropertySetter(fieldInstance, field);
        Answer<Object> answer = Mockito.answer(beanPropertySetter::set);
        Mockito.doAnswer(answer).when(beanPropertySetter);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        Object matchedMock = injecter.getMock();
        verify(matchedMock).equals(fieldInstance);
        verify(reporter).noMoreVerifications();
    }

    @Test
    public void testFilterCandidate_MultipleMocks_NoInjectorReturned() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        mocks.add(mock1);
        mocks.add(mock2);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);
        injecter.thenInject();

        assertNull(injecter.getMock());
        verify(reporter).noMoreVerifications();
    }
}