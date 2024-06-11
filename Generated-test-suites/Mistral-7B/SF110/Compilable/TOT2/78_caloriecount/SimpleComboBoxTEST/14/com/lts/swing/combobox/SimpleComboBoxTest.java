package com.lts.swing.combobox;
import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBox() {
        // Create a SimpleComboBox instance
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        // Test constructor
        assertNotNull(simpleComboBox);
    }

    @Test
    public void testSetModel() {
        // Create a SimpleComboBox instance
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        // Test setting model with valid SimpleComboBoxModel
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.getModel());
        // Test setting model with null
        simpleComboBox.setModel(null);
        assertNull(simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        // Create a SimpleComboBox instance and set selected value
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(new Integer(1));
        // Test getting selected value
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertEquals(new Integer(1), selectedValue);
        // Test getting selected value when no value is selected
        simpleComboBox.setSelectedIndex(-1);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        // Create a SimpleComboBox instance and set selected value
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(new Integer(1));
        // Test setting selected value with valid Object
        simpleComboBox.setSelectedValue(new Long(2));
        assertEquals(new Long(2), simpleComboBox.getSelectedValue());
        // Test setting selected value with null
        simpleComboBox.setSelectedValue(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        // Create a SimpleComboBox instance and set selected value
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(new Integer(1));
        // Test getting selected int
        int selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(1, selectedInt);
        // Test getting selected int when no integer value is selected
        simpleComboBox.setSelectedValue(new Double(2.5));
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        // Create a SimpleComboBox instance and set selected value
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(new Long(1));
        // Test getting selected long
        long selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(1, selectedLong);
        // Test getting selected long when no long value is selected
        simpleComboBox.setSelectedValue(new Float(2.5));
        assertEquals(-1, simpleComboBox.getSelectedLong());
    }
}