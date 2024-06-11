import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Object mock = mock(Object.class);
        when(mock.toString()).thenAnswer(new ReturnsEmptyValues());
        String result = mock.toString();
        assertNotNull(result);
        assertEquals("Mock for [class name], hashCode: [hash code]", result);
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);
        when(mock1.compareTo(mock2)).thenAnswer(new ReturnsEmptyValues());
        int result = mock1.compareTo(mock2);
        assertEquals(1, result);
    }

    @Test
    public void testPrimitiveReturningMethod() {
        Object mock = mock(int.class);
        when(mock.answer(any(InvocationOnMock.class))).thenAnswer(new ReturnsEmptyValues());
        int result = (int) mock.answer(mock(InvocationOnMock.class));
        assertEquals(0, result);
    }

    @Test
    public void testCollectionReturningMethods() {
        Object mock = mock(Collection.class);
        when(mock.answer(any(InvocationOnMock.class))).thenAnswer(new ReturnsEmptyValues());
        Collection<Object> result = (Collection<Object>) mock.answer(mock(InvocationOnMock.class));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSetReturningMethods() {
        Object mock = mock(Set.class);
        when(mock.answer(any(InvocationOnMock.class))).thenAnswer(new ReturnsEmptyValues());
        Set<Object> result = (Set<Object>) mock.answer(mock(InvocationOnMock.class));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMapReturningMethods() {
        Object mock = mock(Map.class);
        when(mock.answer(any(InvocationOnMock.class))).thenAnswer(new ReturnsEmptyValues());
        Map<Object, Object> result = (Map<Object, Object>) mock.answer(mock(InvocationOnMock.class));
        assertTrue(result.isEmpty());
    }

    // Add test cases for other collection types and methods here
}