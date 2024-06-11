package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBox() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        assertNull(combobox.getSelectedValue());
        assertEquals(0, combobox.getSelectedInt());
        assertEquals(-1L, combobox.getSelectedLong());

        model.addElement("Option 1");
        model.addElement("Option 2");
        model.setSelectedIndex(1);

        assertEquals("Option 2", combobox.getSelectedValue());
        assertEquals(1, combobox.getSelectedInt());
        assertEquals(1L, combobox.getSelectedLong());

        combobox.setSelectedValue("Option 1");
        assertEquals("Option 1", combobox.getSelectedValue());
        assertEquals(0, combobox.getSelectedInt());
        assertEquals(0L, combobox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithInvalidType() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue("invalidType");
    }

    @Test
    public void testSetSelectedValueWithInteger() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue(1);
        assertEquals(1, combobox.getSelectedInt());
        assertEquals(1L, combobox.getSelectedLong());
    }

    @Test
    public void testSetSelectedValueWithLong() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue(1L);
        assertEquals(1, combobox.getSelectedInt());
        assertEquals(1L, combobox.getSelectedLong());
    }

    @Test
    public void testSetSelectedValueWithShort() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue((short) 1);
        assertEquals(1, combobox.getSelectedInt());
        assertEquals(1L, combobox.getSelectedLong());
    }

    @Test
    public void testSetSelectedValueWithByte() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue((byte) 1);
        assertEquals(1, combobox.getSelectedInt());
        assertEquals(1L, combobox.getSelectedLong());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);

        model.addElement("Option 1");
        model.addElement("Option 2");
        model.setSelectedIndex(1);

        assertEquals("Option 2", combobox.getSelectedValue());
        assertEquals(1, combobox.getSelectedInt());
        assertEquals(1L, combobox.getSelectedLong());

        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        combobox.setModel(newModel);

        assertEquals(0, combobox.getSelectedInt());
        assertEquals(-1L, combobox.getSelectedLong());
        assertNull(combobox.getSelectedValue());
    }
}