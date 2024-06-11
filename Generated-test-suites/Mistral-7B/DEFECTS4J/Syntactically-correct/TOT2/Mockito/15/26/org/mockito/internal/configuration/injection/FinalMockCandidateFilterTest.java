import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.configuration.injection.OngoingInjecter;
import org.mockito.mockitoSession;

public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateWithOneMock() {
        MockitoSession session = new MockitoSession();
        Object mock = session.spy(MockitoTestClass.class);
        Collection<Object> mocks = new ArrayList<>();
        mocks.add(mock);

        Field field = MockitoTestClass.class.getDeclaredField("testField");
        Object fieldInstance = new Object();

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();
        OngoingInjecter injecter = filter.filterCandidate(mocks, field, fieldInstance);

        assertTrue(injecter.thenInject());
        assertEquals(mock, fieldInstance);
    }
}