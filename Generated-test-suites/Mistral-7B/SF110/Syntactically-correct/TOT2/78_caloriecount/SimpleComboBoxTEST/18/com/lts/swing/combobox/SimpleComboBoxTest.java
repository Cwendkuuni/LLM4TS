import static org.junit.Assert.*;
import com.lts.swing.combobox.*;
import javax.swing.*;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxConstruction() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        assertNotNull(combobox);
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setModel(model);
        assertEquals(model, combobox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        model.setSelectedItem("Test Value");
        Object selectedValue = combobox.getSelectedValue();
        assertEquals("Test Value", selectedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullModel() {
        SimpleComboBoxModel model = null;
        SimpleComboBox combobox = new SimpleComboBox(model);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithNullModel() {
        SimpleComboBoxModel model = null;
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setModel(model);
    }

    @Test
    public void testGetSelectedValueWhenNoValueSelected() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Object selectedValue = combobox.getSelectedValue();
        assertNull(selectedValue);
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Integer testValue = 5;
        combobox.setSelectedValue(testValue);
        Object selectedValue = combobox.getSelectedValue();
        assertEquals(testValue, selectedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithNullValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedValue(null);
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Integer testValue = 5;
        combobox.setSelectedValue(testValue);
        int selectedInt = combobox.getSelectedInt();
        assertEquals(testValue.intValue(), selectedInt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithNullSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithNonIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        String nonIntegerValue = "Test Value";
        combobox.setSelectedValue(nonIntegerValue);
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Long testValue = 10L;
        combobox.setSelectedValue(testValue);
        long selectedLong = combobox.getSelectedLong();
        assertEquals(testValue.longValue(), selectedLong);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithNullSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithNonLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        String nonLongValue = "Test Value";
        combobox.setSelectedValue(nonLongValue);
    }

}