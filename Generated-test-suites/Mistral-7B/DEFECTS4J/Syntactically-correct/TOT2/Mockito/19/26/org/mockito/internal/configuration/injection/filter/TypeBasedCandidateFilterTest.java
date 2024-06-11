import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.OngoingInjecter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class TypeBasedCandidateFilterTest {

    @Test
    public void testFilterCandidate() {
        // Given
        Collection<Object> mocks = new ArrayList<>();
        Object fieldInstance = new Object();
        Field field = new Field() {
            @Override
            public Class<?> getType() {
                return Object.class;
            }
        };
        Object mock = mock(Object.class);
        mocks.add(mock);

        TypeBasedCandidateFilter filter = new TypeBasedCandidateFilter(null);

        // When
        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        // Then
        assertSame(mock, result.getCandidate());
    }
}