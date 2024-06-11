import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleComboBoxTest {
    @Test
    public void testSimpleComboBox() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue("Test Value");
        assertEquals("Test Value", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model1 = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model1);
        ComboBoxModel model2 = new DefaultComboBoxModel();
        simpleComboBox.setModel(model2);
        assertNotSame(model1, simpleComboBox.getModel());
        assertSame(model2, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue("Test Value");
        assertEquals("Test Value", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue("Test Value");
        simpleComboBox.setSelectedValue(42);
        assertEquals(42, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue(42);
        assertEquals(42, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue(42L);
        assertEquals(42L, simpleComboBox.getSelectedLong());
    }
}