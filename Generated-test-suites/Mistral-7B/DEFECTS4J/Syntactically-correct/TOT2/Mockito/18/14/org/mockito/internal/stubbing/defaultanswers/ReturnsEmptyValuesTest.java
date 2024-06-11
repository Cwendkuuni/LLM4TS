import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;
import java.util.Collections;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        Object mock = mock(Object.class);
        Answer answer = new ReturnsEmptyValues();

        when(mock.toString()).then(answer);

        assertEquals("Mock for [class java.lang.Object], hashCode: " + mock.hashCode(), mock.toString());
    }

    @Test
    public void testAnswerToStringMethodWithNull() {
        Object mock = mock(Object.class);
        Answer answer = new ReturnsEmptyValues();

        when(mock.toString()).then(answer);

        assertNull(mock.toString());
    }

    @Test
    public void testAnswerToStringMethodWithCustomMockName() {
        Object mock = mock(Object.class, "CustomMockName");
        Answer answer = new ReturnsEmptyValues();

        when(mock.toString()).then(answer);

        assertEquals("CustomMockName", mock.toString());
    }

    @Test
    public void testAnswerCompareToMethod() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Answer answer = new ReturnsEmptyValues();

        when(mock1.compareTo(mock2)).then(answer);

        assertEquals(1, mock1.compareTo(mock2));
    }

    @Test
    public void testAnswerCompareToMethodWithNull() {
        Object mock1 = mock(Object.class);
        Object mock2 = null;
        Answer answer = new ReturnsEmptyValues();

        when(mock1.compareTo(mock2)).then(answer);

        assertEquals(1, mock1.compareTo(mock2));
    }

    @Test
    public void testAnswerToStringAndCompareToMethods() {
        Object mock = mock(Object.class);
        Answer answer = new ReturnsEmptyValues();

        when(mock.toString()).then(answer);
        when(mock.compareTo(mock)).then(answer);

        assertEquals("Mock for [class java.lang.Object], hashCode: " + mock.hashCode(), mock.toString());
        assertEquals(0, mock.compareTo(mock));
    }

    @Test
    public void testReturnValueForPrimitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        assertEquals(0, returnsEmptyValues.returnValueFor(int.class).intValue());
        assertEquals(false, returnsEmptyValues.returnValueFor(boolean.class).booleanValue());
    }

    @Test
    public void testReturnValueForCollectionTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        assertEquals(Collections.emptyList(), returnsEmptyValues.returnValueFor(List.class));
        assertEquals(Collections.emptySet(), returnsEmptyValues.returnValueFor(Set.class));
        assertEquals(Collections.emptyHashSet(), returnsEmptyValues.returnValueFor(HashSet.class));
        assertEquals(Collections.emptySortedSet(), returnsEmptyValues.returnValueFor(SortedSet.class));
        assertEquals(Collections.emptyTreeSet(), returnsEmptyValues.returnValueFor(TreeSet.class));
        assertEquals(Collections.emptyLinkedHashSet(), returnsEmptyValues.returnValueFor(LinkedHashSet.class));
    }

    @Test
    public void testReturnValueForCustomClass() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        CustomClass customClass = new CustomClass();

        when(customClass.someMethod()).thenReturn(customClass);

        assertEquals(customClass, returnsEmptyValues.returnValueFor(CustomClass.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReturnValueForException() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        returnsEmptyValues.returnValueFor(Exception.class);
    }
}