package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBox() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertNull(comboBox.getSelectedValue());
        assertEquals(0, comboBox.getSelectedInt());
        assertEquals(-1L, comboBox.getSelectedLong());
        model.addElement("Option 1");
        model.addElement("Option 2");
        comboBox.setModel(model);
        assertEquals("Option 1", comboBox.getSelectedValue().toString());
        assertEquals(0, comboBox.getSelectedInt());
        assertEquals(0L, comboBox.getSelectedLong());
        comboBox.setSelectedIndex(1);
        assertEquals("Option 2", comboBox.getSelectedValue().toString());
        assertEquals(1, comboBox.getSelectedInt());
        assertEquals(1L, comboBox.getSelectedLong());
    }

    @Test
    public void testSetSelectedValueInteger() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement("Option 1");
        model.addElement("Option 2");
        comboBox.setModel(model);
        comboBox.setSelectedValue(1);
        assertEquals("Option 1", comboBox.getSelectedValue().toString());
        assertEquals(0, comboBox.getSelectedInt());
        assertEquals(0L, comboBox.getSelectedLong());
        comboBox.setSelectedIndex(1);
        comboBox.setSelectedValue(2);
        assertEquals("Option 2", comboBox.getSelectedValue().toString());
        assertEquals(1, comboBox.getSelectedInt());
        assertEquals(1L, comboBox.getSelectedLong());
    }

    @Test
    public void testSetSelectedValueLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement("Option 1");
        model.addElement("Option 2");
        comboBox.setModel(model);
        comboBox.setSelectedValue(1);
        assertEquals("Option 1", comboBox.getSelectedValue().toString());
        assertEquals(0, comboBox.getSelectedInt());
        assertEquals(0L, comboBox.getSelectedLong());
        comboBox.setSelectedIndex(1);
        comboBox.setSelectedValue(2L);
        assertEquals("Option 2", comboBox.getSelectedValue().toString());
        assertEquals(1, comboBox.getSelectedInt());
        assertEquals(1L, comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement("Option 1");
        model.addElement("Option 2");
        comboBox.setModel(model);
        comboBox.setSelectedValue('A');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueNull() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setModel(model);
        comboBox.setSelectedValue(null);
    }

    @Test
    public void testGetSelectedValueNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertNull(comboBox.getSelectedValue());
    }
}