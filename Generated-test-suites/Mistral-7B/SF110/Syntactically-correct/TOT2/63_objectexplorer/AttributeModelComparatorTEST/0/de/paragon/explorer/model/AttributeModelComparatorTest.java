import org.junit.Test;
import static org.junit.Assert.*;
import de.paragon.explorer.model.AttributeModel;
import de.paragon.explorer.model.AttributeModelComparator;

public class AttributeModelComparatorTest {

    @Test
    public static void testCompare_sameName_returnsZero() {
        AttributeModel attr1 = new AttributeModel("name1");
        AttributeModel attr2 = new AttributeModel("name1");
        int result = new AttributeModelComparator().compare(attr1, attr2);
        assertNotNull(result);
        assertEquals(0, result);
    }

    @Test
    public static void testCompare_differentNames_returnsNegativeOrPositive() {
        AttributeModel attr1 = new AttributeModel("name1");
        AttributeModel attr2 = new AttributeModel("name2");
        int result = new AttributeModelComparator().compare(attr1, attr2);
        assertNotNull(result);
        assertTrue(result < 0 || result > 0);
    }

    @Test(expected = NullPointerException.class)
    public static void testCompare_null_throwsNullPointerException() {
        AttributeModel attr1 = null;
        AttributeModel attr2 = new AttributeModel("name1");
        new AttributeModelComparator().compare(attr1, attr2);
    }

    @Test
    public static void testCompare_null_o1_returnsZero() {
        AttributeModel attr1 = null;
        AttributeModel attr2 = new AttributeModel("name1");
        int result = new AttributeModelComparator().compare(attr1, attr2);
        assertEquals(0, result);
    }

    @Test
    public static void testCompare_o1_is_longer_than_o2_returnsNegative() {
        AttributeModel attr1 = new AttributeModel("name1");
        AttributeModel attr2 = new AttributeModel("name");
        int result = new AttributeModelComparator().compare(attr1, attr2);
        assertNegative(result);
    }

    @Test
    public static void testCompare_o1_is_shorter_than_o2_returnsPositive() {
        AttributeModel attr1 = new AttributeModel("name");
        AttributeModel attr2 = new AttributeModel("name1");
        int result = new AttributeModelComparator().compare(attr1, attr2);
        assertTrue(result > 0);
    }

    @Test
    public static void testCompare_both_names_are_equal_to_a_returns_o2_before_o1() {
        AttributeModel attr1 = new AttributeModel("a");
        AttributeModel attr2 = new AttributeModel("a");
        int result = new AttributeModelComparator().compare(attr1, attr2);
        assertEquals(-1, result);
    }

    @Test
    public static void testCompare_both_names_are_equal_to_a_returns_o1_before_o2() {
        AttributeModel attr1 = new AttributeModel("a");
        AttributeModel attr2 = new AttributeModel("a");
        int result = new AttributeModelComparator().compare(attr2, attr1);
        assertEquals(1, result);
    }

    @Test
    public static void testCompare_o1_name_is_empty_string_and_o2_name_is_not_empty() {
        AttributeModel attr1 = new AttributeModel("");
        AttributeModel attr2 = new AttributeModel("name");
        int result = new AttributeModelComparator().compare(attr1, attr2);
        assertTrue(result < 0);
    }

    @Test
    public static void testCompare_o1_name_is_not_empty_string_and_o2_name_is_empty() {
        AttributeModel attr1 = new AttributeModel("name");
        AttributeModel attr2 = new AttributeModel("");
        int result = new AttributeModelComparator().compare(attr1, attr2);
        assertTrue(result > 0);
    }

    @Test
    public static void testCompare_o1_name_is_null_and_o2_name_is_not_null() {
        AttributeModel attr1 = null;
        AttributeModel attr2 = new AttributeModel("name");
        int result = new AttributeModelComparator().compare(attr1, attr2);
        assertTrue(result < 0);
    }

    @Test
    public static void testCompare_o1_name_is_not_null_and_o2_name_is_null() {
        AttributeModel attr1 = new AttributeModel("name");
        AttributeModel attr2 = null;
        int result = new AttributeModelComparator().compare(attr1, attr2);
        assertTrue(result > 0);
    }
}