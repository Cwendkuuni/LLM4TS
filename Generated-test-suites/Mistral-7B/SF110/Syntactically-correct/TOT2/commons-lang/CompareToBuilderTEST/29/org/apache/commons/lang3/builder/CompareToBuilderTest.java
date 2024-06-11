import org.apache.commons.lang3.builder.CompareToBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompareToBuilderTest {
    @Test
    public void testReflectionCompare() {
        // Test case 1
        Object lhs = new Object();
        Object rhs = new Object();
        int comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);

        // Test case 2
        lhs = new Integer(5);
        rhs = new Integer(5);
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);

        // Test case 3
        lhs = new Integer(5);
        rhs = new Integer(6);
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, comparison);

        // Test case 4
        lhs = new Integer(6);
        rhs = new Integer(5);
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(1, comparison);

        // Test case 5
        lhs = new String("hello");
        rhs = new String("hello");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(0, comparison);

        // Test case 6
        lhs = new String("hello");
        rhs = new String("world");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(-1, comparison);

        // Test case 7
        lhs = new String("world");
        rhs = new String("hello");
        comparison = CompareToBuilder.reflectionCompare(lhs, rhs);
        assertEquals(1, comparison);
    }

    @Test
    public void testAppend_Objects() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testAppend_DifferentObjects() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = new Object();
        lhs = new Integer(5);
        rhs = new Integer(6);
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_SameObject() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = lhs;
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testAppend_NullLhs() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = null;
        Object rhs = new Object();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_NullRhs() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new Object();
        Object rhs = null;
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(1, comparison);
    }

    @Test(expected = NullPointerException.class)
    public void testAppend_NullLhsAndRhs() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = null;
        Object rhs = null;
        builder.append(lhs, rhs);
    }

    @Test
    public void testAppend_Arrays() {
        CompareToBuilder builder = new CompareToBuilder();
        Integer[] lhs = {1, 2, 3};
        Integer[] rhs = {1, 2, 3};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testAppend_Arrays_DifferentLengths() {
        CompareToBuilder builder = new CompareToBuilder();
        Integer[] lhs = {1, 2, 3};
        Integer[] rhs = {1, 2};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_Arrays_NullElements() {
        CompareToBuilder builder = new CompareToBuilder();
        Integer[] lhs = {1, null, 3};
        Integer[] rhs = {1, null, 3};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(0, comparison);
    }

    @Test
    public void testAppend_Arrays_NonComparable() {
        CompareToBuilder builder = new CompareToBuilder();
        Integer[] lhs = {1, 2, 3};
        String[] rhs = {"a", "b", "c"};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertNotEquals(0, comparison);
    }

    @Test
    public void testAppend_ArrayToObject() {
        CompareToBuilder builder = new CompareToBuilder();
        Integer[] lhs = {1, 2, 3};
        Object rhs = new Object();
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(-1, comparison);
    }

    @Test
    public void testAppend_ObjectToArray() {
        CompareToBuilder builder = new CompareToBuilder();
        Object lhs = new Object();
        Integer[] rhs = {1, 2, 3};
        builder.append(lhs, rhs);
        int comparison = builder.toComparison();
        assertEquals(1, comparison);
    }
}