import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.configuration.injection.FinalMockCandidateFilter;
import org.mockito.internal.util.reflection.FieldSetter;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class FinalMockCandidateFilterTest {

    FinalMockCandidateFilter filter;
    Field field;
    Object fieldInstance;
    List<Object> mocks;

    @Before
    public void setUp() {
        filter = new FinalMockCandidateFilter();
        field = Mockito.spy(Field.class);
        fieldInstance = Mockito.spy(Object.class);
        mocks = Collections.singletonList(Mockito.mock(Object.class));
    }

    @Test
    public void testFilterCandidateWithSingleMock() {
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        assertNotNull(actual);
    }

    @Test
    public void testFilterCandidateWithEmptyMockCollection() {
        mocks = Collections.emptyList();
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);
        assertFalse(actual.thenInject());
    }

    @Test(expected = MockitoException.class)
    public void testFilterCandidateWithFailingFieldSetter() {
        // Arrange
        RuntimeException exception = new RuntimeException("Exception when setting field");
        FieldSetter fieldSetter = Mockito.spy(new FieldSetter(fieldInstance, field));
        Mockito.doThrow(exception).when(fieldSetter).set(Mockito.any(), Mockito.any());

        // Act
        OngoingInjecter actual = filter.filterCandidate(mocks, field, fieldInstance);

        // Assert the exception is thrown
        assertFalse(actual.thenInject());
    }
}