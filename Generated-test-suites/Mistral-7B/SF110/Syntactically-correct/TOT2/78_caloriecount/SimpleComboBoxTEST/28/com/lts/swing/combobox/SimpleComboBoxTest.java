import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import org.junit.*;
import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxConstruction() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNotNull(simpleComboBox);
    }

    @Test
    public void testSetModel() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        ComboBoxModel newModel = new DefaultComboBoxModel();
        simpleComboBox.setModel(newModel);
        assertNotNull(simpleComboBox.getModel());
        assertEquals(newModel, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue("Test Value");
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertNotNull(selectedValue);
        assertEquals("Test Value", selectedValue);
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue("Test Value");
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertNotNull(selectedValue);
        assertEquals("Test Value", selectedValue);
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(1);
        int selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(1, selectedInt);
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(1L);
        long selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(1, selectedLong);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueNull() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueNonSupportedType() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue("Test Value");
        simpleComboBox.setSelectedValue(new Object());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntNull() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNull(simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongNull() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNull(simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedIntNonInteger() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(new Double(1.0));
        int selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(-1, selectedInt);
    }

    @Test
    public void testGetSelectedLongNonLong() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(new Float(1.0));
        long selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(-1, selectedLong);
    }
}