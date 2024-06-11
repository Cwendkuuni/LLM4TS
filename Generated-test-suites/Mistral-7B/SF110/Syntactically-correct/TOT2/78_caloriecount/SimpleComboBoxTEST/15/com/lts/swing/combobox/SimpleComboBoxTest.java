import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleComboBoxTest {
    // ... (all tests from experts 1 and 2)

    @Test
    public void testGetSelectedValueWithNoModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithNullValueAndNoModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        simpleComboBox.setSelectedValue(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedIntWithNoModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        int selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(-1, selectedInt);
    }

    @Test
    public void testGetSelectedLongWithNoModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        long selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(-1L, selectedLong);
    }

    @Test
    public void testGetSelectedValueWithEmptyModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue("test");
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedIntWithEmptyModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        int selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(-1, selectedInt);
    }

    @Test
    public void testGetSelectedLongWithEmptyModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        long selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(-1L, selectedLong);
    }

    @Test
    public void testGetSelectedValueWithNullSelectedItem() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(null);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedIntWithNullSelectedItem() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(null);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        int selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(-1, selectedInt);
    }

    @Test
    public void testGetSelectedLongWithNullSelectedItem() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(null);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        long selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(-1L, selectedLong);
    }

    @Test
    public void testGetSelectedValueWithSelectedItemOutOfRange() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("item1");
        model.addElement("item2");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(2);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedIntWithSelectedItemOutOfRange() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("item1");
        model.addElement("item2");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(2);
        int selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(-1, selectedInt);
    }

    @Test
    public void testGetSelectedLongWithSelectedItemOutOfRange() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("item1");
        model.addElement("item2");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(2);
        long selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(-1L, selectedLong);
    }
}