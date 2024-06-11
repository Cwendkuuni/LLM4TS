import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class FinalMockCandidateFilterTest {

    @Test
    public void testFilterCandidateSuccess() {
        // Given
        Collection<Object> mocks = Mockito.mock(Collection.class);
        Mockito.when(mocks.size()).thenReturn(1);
        Mockito.when(mocks.iterator().next()).thenReturn(new Object() {});
        Object fieldInstance = new Object();
        Field field = Mockito.spy(Field.class);

        FinalMockCandidateFilter filter = new FinalMockCandidateFilter();

        // When
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertTrue(actual.thenInject());
    }
}