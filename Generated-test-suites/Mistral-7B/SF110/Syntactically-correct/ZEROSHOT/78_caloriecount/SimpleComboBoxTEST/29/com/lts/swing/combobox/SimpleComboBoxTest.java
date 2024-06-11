package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxConstructionWithModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertNotNull(comboBox);
        assertEquals(model, comboBox.getModel());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();

        comboBox.setModel(newModel);
        assertEquals(newModel, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValueNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(5);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);

        Object selectedValue = comboBox.getSelectedValue();
        assertEquals(Integer.valueOf(5), selectedValue);
    }

    @Test
    public void testGetSelectedValueLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(5L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);

        Object selectedValue = comboBox.getSelectedValue();
        assertEquals(Long.valueOf(5L), selectedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedValueInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("String value");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);

        Object selectedValue = comboBox.getSelectedValue();
        assertNotSame("String value", selectedValue);
        comboBox.getSelectedValue(); // This line throws the exception
    }

    @Test
    public void testSetSelectedValueInteger() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        Integer integerValue = new Integer(5);

        comboBox.setSelectedValue(integerValue);
        assertEquals(integerValue, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        Long longValue = new Long(5);

        comboBox.setSelectedValue(longValue);
        assertEquals(longValue, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueString() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("String value");
        SimpleComboBox comboBox = new SimpleComboBox(model);

        String stringValue = "String value";
        comboBox.setSelectedValue(stringValue);
        assertEquals(stringValue, comboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        Integer integerValue = new Integer(5);

        comboBox.setSelectedValue(new Byte((byte)5));
        // This line throws the exception
        comboBox.setSelectedValue(integerValue);
    }

    @Test
    public void testGetSelectedIntNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(5);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);

        int selectedInt = comboBox.getSelectedInt();
        assertEquals(5, selectedInt);
    }

    @Test
    public void testGetSelectedIntLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(5L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);

        int selectedInt = comboBox.getSelectedInt();
        assertEquals(5, selectedInt);
    }

    @Test
    public void testGetSelectedIntInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("String value");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);

        int selectedInt = comboBox.getSelectedInt();
        assertEquals(-1, selectedInt);
    }

    @Test
    public void testGetSelectedLongNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertEquals(-1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(5);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);

        long selectedLong = comboBox.getSelectedLong();
        assertEquals(5L, selectedLong);
    }

    @Test
    public void testGetSelectedLongLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(5L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);

        long selectedLong = comboBox.getSelectedLong();
        assertEquals(5L, selectedLong);
    }

    @Test
    public void testGetSelectedLongInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("String value");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedIndex(0);

        long selectedLong = comboBox.getSelectedLong();
        assertEquals(-1L, selectedLong);
    }
}