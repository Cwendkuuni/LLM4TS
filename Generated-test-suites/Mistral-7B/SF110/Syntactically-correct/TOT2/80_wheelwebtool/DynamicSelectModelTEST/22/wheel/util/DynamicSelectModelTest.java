import static org.junit.Assert.*;
import wheel.components.Component;
import wheel.util.DynamicSelectModel;

public class DynamicSelectModelTest {

    @Test
    public void testGetOptionCount() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("new ArrayList<String>()"), new ElExpression("toString"), new ElExpression("toString"));
        assertEquals(0, model.getOptionCount());

        model = new DynamicSelectModel(new ElExpression("[\"Option 1\", \"Option 2\", \"Option 3\"]"), new ElExpression("toString"), new ElExpression("toString"));
        assertEquals(3, model.getOptionCount());

        model = new DynamicSelectModel(new ElExpression("null"), new ElExpression("toString"), new ElExpression("toString"));
        assertEquals(0, model.getOptionCount());

        model = new DynamicSelectModel(new ElExpression("[null]"), new ElExpression("toString"), new ElExpression("toString"));
        assertEquals(1, model.getOptionCount());
    }

    @Test
    public void testGetLabel() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("new ArrayList<String>()"), new ElExpression("toString"), new ElExpression("toString"));
        model.setComponent(new Component());

        String label = model.getLabel(0);
        assertEquals("", label);

        model = new DynamicSelectModel(new ElExpression("[\"Label 1\", \"Label 2\", \"Label 3\"]"), new ElExpression("toString"), new ElExpression("toString"));
        model.setComponent(new Component());

        label = model.getLabel(1);
        assertEquals("Label 2", label);

        model = new DynamicSelectModel(new ElExpression("[null, \"Label 1\", \"Label 2\"]"), new ElExpression("toString"), new ElExpression("toString"));
        model.setComponent(new Component());

        label = model.getLabel(1);
        assertNull(label);

        model = new DynamicSelectModel(new ElExpression("[\"Label 1\", null, \"Label 2\"]"), new ElExpression("toString"), new ElExpression("toString"));
        model.setComponent(new Component());

        label = model.getLabel(0);
        assertNull(label);
    }

    @Test
    public void testTranslateValue() {
        DynamicSelectModel model = new DynamicSelectModel(new ElExpression("[1, \"One\", 2, \"Two\", 3, \"Three\"]"), new ElExpression("toString"), new ElExpression("toString"));
        model.setComponent(new Component());
        model.setTranslator(new ElExpression("toString"));

        Object translatedValue = model.translateValue("One");
        assertEquals(1, translatedValue);

        translatedValue = model.translateValue("Two");
        assertEquals(2, translatedValue);

        translatedValue = model.translateValue("Three");
        assertEquals(3, translatedValue);

        translatedValue = model.translateValue("Four");
        assertNull(translatedValue);

        model = new DynamicSelectModel(new ElExpression("[1, \"One\", 2, \"Two\", 3, \"Three\"]"), new ElExpression("toString"), new ElExpression(null));
        model.setComponent(new Component());

        translatedValue = model.translateValue("One");
        assertNull(translatedValue);
    }
}