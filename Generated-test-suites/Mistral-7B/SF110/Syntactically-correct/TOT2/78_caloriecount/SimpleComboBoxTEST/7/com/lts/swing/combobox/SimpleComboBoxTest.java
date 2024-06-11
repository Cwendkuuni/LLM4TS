import static org.junit.Assert.*;
import com.lts.swing.combobox.SimpleComboBoxModel;
import com.lts.swing.combobox.SimpleComboBox;

public class SimpleComboBoxTest {

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Object selectedValue = "Test Value";
        model.setSelectedValue(selectedValue);

        assertEquals(selectedValue, combobox.getSelectedValue());

        // Edge cases
        model = new SimpleComboBoxModel();
        combobox = new SimpleComboBox(model);
        assertNull(combobox.getSelectedValue());

        model.setSelectedValue(new Object());
        assertNotSame(selectedValue, combobox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Object value = "Test Value";

        combobox.setSelectedValue(value);
        assertEquals(value, model.getSelectedValue());

        // Edge cases
        model = new SimpleComboBoxModel();
        combobox = new SimpleComboBox(model);
        assertNull(combobox.getSelectedValue());
        combobox.setSelectedValue(null);
        assertNull(model.getSelectedValue());

        model = new SimpleComboBoxModel();
        combobox = new SimpleComboBox(model);
        model.setSelectedValue("Test Value");
        combobox.setSelectedValue(value);
        assertEquals(value, model.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Integer selectedValue = 1;
        model.setSelectedValue(selectedValue);

        assertEquals(selectedValue, combobox.getSelectedInt());

        // Edge cases
        model = new SimpleComboBoxModel();
        combobox = new SimpleComboBox(model);
        assertSame(-1, combobox.getSelectedInt());

        model = new SimpleComboBoxModel();
        combobox = new SimpleComboBox(model);
        model.setSelectedValue(new Object());
        assertSame(-1, combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Long selectedValue = 1L;
        model.setSelectedValue(selectedValue);

        assertEquals(selectedValue, combobox.getSelectedLong());

        // Edge cases
        model = new SimpleComboBoxModel();
        combobox = new SimpleComboBox(model);
        assertSame(-1L, combobox.getSelectedLong());

        model = new SimpleComboBoxModel();
        combobox = new SimpleComboBox(model);
        model.setSelectedValue(new Object());
        assertSame(-1L, combobox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithNonNumericValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        String nonNumericValue = "Test String";
        model.setSelectedValue(nonNumericValue);

        combobox.getSelectedInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithNonNumericValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        String nonNumericValue = "Test String";
        model.setSelectedValue(nonNumericValue);

        combobox.getSelectedLong();
    }
}