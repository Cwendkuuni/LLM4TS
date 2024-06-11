import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter;
import org.mockito.internal.configuration.injection.filter.TypeBasedCandidateFilter.OngoingInjecter;
import org.mockito.mock.MockComponentImplementation;
import org.mockito.mock.MockConstructor;
import org.mockito.reflect.MockitoClass;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

public class TypeBasedCandidateFilterTest {

    private TypeBasedCandidateFilter filter;
    private MockCandidateFilter nextMock;
    private Collection<Object> mocks;
    private Object fieldInstance;
    private Object mock;
    private Field field;

    @Before
    public void setUp() {
        nextMock = MockitoAnnotations.mock(MockCandidateFilter.class);
        filter = new TypeBasedCandidateFilter(nextMock);
        mocks = new ArrayList<>();
        field = Mockito3Annotations.getFields(this, Field.class)[0];
        fieldInstance = new Object();
        mock = MockitoAnnotations.mock(MockitoClass.get(this.getClass()).getDeclaredComponentType());
        Mockito.doReturn(mock).when(nextMock).filterCandidate(Mockito.anyCollection(), Mockito.any(Field.class), Mockito.any());
    }

    @Test
    public void testFilterCandidate_EmptyCollection() {
        OngoingInjecter result = filter.filterCandidate(Collections.emptyList(), field, fieldInstance);
        Assert.assertNull(result);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullMocks() {
        filter.filterCandidate(null, field, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullField() {
        filter.filterCandidate(mocks, null, fieldInstance);
    }

    @Test(expected = NullPointerException.class)
    public void testFilterCandidate_NullFieldInstance() {
        filter.filterCandidate(mocks, field, null);
    }

    @Test
    public void testFilterCandidate_DifferentTypes() {
        Object differentTypeMock = new Object() {};
        mocks.add(mock);
        mocks.add(differentTypeMock);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);
        Assert.assertNull(result);
    }

    @Test
    public void testFilterCandidate() {
        Object mock1 = new MockObject1();
        Object mock2 = new MockObject2();
        mocks.add(mock1);
        mocks.add(mock2);

        OngoingInjecter result = filter.filterCandidate(mocks, field, fieldInstance);

        ArgumentCaptor<Collection<Object>> mocksArgumentCaptor = ArgumentCaptor.forClass(Collection.class);
        Mockito.verify(nextMock).filterCandidate(mocksArgumentCaptor.capture(), field, fieldInstance);
        Assert.assertSame(mock1, result.getCandidate());
        Assert.assertEquals(mock1.getClass(), field.getType());
    }
}

class MockObject1 {
    // Empty class
}

class MockObject2 {
    // Empty class
}