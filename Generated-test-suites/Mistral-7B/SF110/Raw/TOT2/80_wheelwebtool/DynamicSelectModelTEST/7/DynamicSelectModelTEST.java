import static org.junit.Assert.*;
import org.junit.Test;
import wheel.util.DynamicSelectModel;
import wheel.components.*;

public class DynamicSelectModelTest {

    @Test
    public void testGetOptionCount() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("new int[5]"), new ElExpression("toString"), new ElExpression("toString"));
        assertEquals(5, model.getOptionCount());

        model = new DynamicSelectModel(new ElExpression("new String[]{}"), new ElExpression("toString"), new ElExpression("toString"));
        assertEquals(0, model.getOptionCount());

        model = new DynamicSelectModel(new ElExpression("new String[]{" + "apple", "banana" + "}"), new ElExpression("toString"), new ElExpression("toString"));
        assertEquals(2, model.getOptionCount());

        model = new DynamicSelectModel(new ElExpression("new String[]{" + "apple", null, "cherry" + "}"), new ElExpression("toString"), new ElExpression("toString"));
        assertEquals(2, model.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("new String[]{" + "Apple", "Banana", "Cherry" + "}"), new ElExpression("toString"), new ElExpression("toString"));

        assertEquals("Apple", model.getLabel(0));
        assertEquals("Cherry", model.getLabel(2));

        model = new DynamicSelectModel(new ElExpression("new String[]{" + null + "}"), new ElExpression("toString"), new ElExpression("toString"));
        try {
            model.getLabel(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected error
        }

        model = new DynamicSelectModel(new ElExpression("new String[]{" + "Apple", "Banana", "Cherry", null + "}"), new ElExpression("toString"), new ElExpression("toString"));
        assertEquals("Cherry", model.getLabel(3));
    }

    @Test
    public void testGetValue() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("new String[]{" + "Apple", "Banana", "Cherry" + "}"), new ElExpression("toString"), new ElExpression("toString"));

        assertEquals("Apple", model.getValue(0));
        assertEquals("Cherry", model.getValue(2));

        model = new DynamicSelectModel(new ElExpression("new String[]{" + "Apple", "Banana", null + "}"), new ElExpression("toString"), new ElExpression("toString"));
        assertEquals("Banana", model.getValue(1));

        model = new DynamicSelectModel(new ElExpression("new String[]{" + "Apple", "Banana", "Cherry" + "}"), new ElExpression("toString"), new ElExpression("toString"));
        try {
            model.getValue(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected error
        }

        model = new DynamicSelectModel(new ElExpression("new String[]{" + "Apple", "Banana", "Cherry" + "}"), new ElExpression("toString"), new ElExpression("toString"));
        try {
            model.getValue("notAnIndex");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected error
        }
    }

    @Test
    public void testTranslateValue() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("new String[]{" + "apple", "banana", "cherry" + "}"), new ElExpression("toString"), new ElExpression("toString"));

        assertEquals("apple", model.translateValue("apple"));
        assertEquals("banana", model.translateValue("banana"));
        assertEquals("cherry", model.translateValue("cherry"));

        model = new DynamicSelectModel(new ElExpression("new String[]{" + "apple", "banana", "cherry" + "}"), new ElExpression("toString"), new ElExpression("toString"));
        assertEquals(null, model.translateValue("orange"));
    }

    @Test(expected = WheelException.class)
    public void testGetObjects_NullCollection() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression(null), new ElExpression("toString"), new ElExpression("toString"));
    }

    @Test
    public void testGetObjects_EmptyCollection() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("{}"), new ElExpression("toString"), new ElExpression("toString"));
        assertEquals(0, model.getOptionCount());
    }

    @Test
    public void testGetObjects_InvalidObjects() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("new Object[]{" + new Object() {
            public String toString() {
                throw new RuntimeException("Test error.");
            }
        }, "banana", "cherry" + "}"), new ElExpression("toString"), new ElExpression("toString"));

        try {
            model.getLabel(0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected error
        }
    }
}