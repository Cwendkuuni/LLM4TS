package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxWithValidModel() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        // Check if the combobox is initialized with the provided model
        assertEquals("SimpleComboBox should have the provided model", new SimpleComboBoxModel(), combobox.getModel());
    }

    @Test
    public void testSetModelWithValidSimpleComboBoxModel() {
        SimpleComboBox combobox = new SimpleComboBox(new JComboBoxModel());
        SimpleComboBoxModel validModel = new SimpleComboBoxModel();
        combobox.setModel(validModel);
        // Check if the combobox model is correctly set
        assertEquals("SimpleComboBox model should be set to the provided one", validModel, combobox.getModel());
    }

    @Test
    public void testGetSelectedValueWithNullModel() {
        SimpleComboBox combobox = new SimpleComboBox(null);
        assertNull("getSelectedValue() should return null when the model is null", combobox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithValidModelAndSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Test Value");
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedIndex(0);
        // Check if the getSelectedValue() method returns the selected value
        assertEquals("getSelectedValue() should return the selected value", "Test Value", combobox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithNullValue() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue(null);
    }

    @Test
    public void testSetSelectedValueWithValidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Test Value");
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedValue("Test Value");
        // Check if the selected value is correctly set
        assertEquals("setSelectedValue() should set the selected value", "Test Value", combobox.getSelectedValue());
    }

    @Test
    public void testGetSelectedIntWithNullSelectedValue() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        int result = combobox.getSelectedInt();
        // Check if getSelectedInt() returns -1 when the selected value is null
        assertEquals("getSelectedInt() should return -1 when the selected value is null", -1, result);
    }

    @Test
    public void testGetSelectedIntWithValidIntegerSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Integer(1));
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedIndex(0);
        int result = combobox.getSelectedInt();
        // Check if getSelectedInt() returns the correct integer value
        assertEquals("getSelectedInt() should return the correct integer value", 1, result);
    }

    @Test
    public void testGetSelectedIntWithValidShortSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedIndex(0);
        int result = combobox.getSelectedInt();
        // Check if getSelectedInt() correctly converts short to int
        assertEquals("getSelectedInt() should correctly convert short to int", 1, result);
    }

    @Test
    public void testGetSelectedIntWithValidByteSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedIndex(0);
        int result = combobox.getSelectedInt();
        // Check if getSelectedInt() correctly converts byte to int
        assertEquals("getSelectedInt() should correctly convert byte to int", 1, result);
    }

    @Test
    public void testGetSelectedLongWithNullSelectedValue() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        long result = combobox.getSelectedLong();
        // Check if getSelectedLong() returns -1L when the selected value is null
        assertEquals("getSelectedLong() should return -1L when the selected value is null", -1L, result);
    }

    @Test
    public void testGetSelectedLongWithValidIntegerSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Integer(1));
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedIndex(0);
        long result = combobox.getSelectedLong();
        // Check if getSelectedLong() returns the correct long value
        assertEquals("getSelectedLong() should return the correct long value", 1L, result);
    }

    @Test
    public void testGetSelectedLongWithValidLongSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Long(1));
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedIndex(0);
        long result = combobox.getSelectedLong();
        // Check if getSelectedLong() returns the correct long value
        assertEquals("getSelectedLong() should return the correct long value", 1L, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidValue() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue("Invalid Value");
        combobox.getSelectedLong();
    }
}