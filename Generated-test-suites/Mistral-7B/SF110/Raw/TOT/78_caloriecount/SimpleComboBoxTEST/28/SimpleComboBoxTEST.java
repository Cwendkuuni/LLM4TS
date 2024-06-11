
import static org.junit.Assert.*;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;
import com.lts.swing.combobox.TestCases;

@RunWith(JUnitPlatform.class)
@Category(TestCases.class)
public class SimpleComboBoxTest {

    @Test
    public void testGetSelectedValue() {
        Expert1.testGetSelectedValue();
    }

    @Test
    public void testSetSelectedValueObject() {
        Expert2.testSetSelectedValueObject();
        Expert2.testSetSelectedValueIllegalArgumentException();
    }

    @Test
    public void testGetSelectedInt() {
        Expert3.testGetSelectedInt();
        Expert3.testGetSelectedIntLongValue();
        Expert3.testGetSelectedIntNullValue();
    }

    @Test
    public void testGetSelectedLong() {
        // Test case for getSelectedLong() method can be added here
    }

    @Test
    public void testSetModel() throws IllegalAccessException, InvocationTargetException {
        SimpleComboBoxModel model = new DefaultComboBoxModel<>();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);

        Component originalComponent = simpleComboBox.getEditorComponent();
        SimpleComboBoxModel newModel = new SimpleComboBoxModel(){{
            setSelectedItem("New Item");
        }};
        ReflectionHelper.setFieldValue(simpleComboBox, "mySimpleModel", newModel);

        assertEquals("New Item", simpleComboBox.getSelectedItem());
        assertEquals("New Item", simpleComboBox.getItemAt(simpleComboBox.getSelectedIndex()));
        assertEquals("New Item", simpleComboBox.getEditor().getEditorComponent().getValue());
    }

    @Test
    public void testSetModelNull() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);

        assertNull(simpleComboBox.getModel());
    }

    // Add other test cases as necessary

    static class ReflectionHelper {
        @SuppressWarnings("unchecked")
        public static <T> T getFieldValue(Object object, String fieldName) throws IllegalAccessException {
            Class<?> clazz = object.getClass();
            java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(object);
        }

        @SuppressWarnings("unchecked")
        public static <T> void setFieldValue(Object object, String fieldName, T value) throws IllegalAccessException {
            Class<?> clazz = object.getClass();
            java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        }
    }
}
