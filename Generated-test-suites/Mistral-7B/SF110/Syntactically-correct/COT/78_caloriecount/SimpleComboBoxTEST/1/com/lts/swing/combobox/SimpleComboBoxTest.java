package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBox() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNotNull(combobox);
    }

    @Test
    public void testSetModel() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setModel(new DefaultComboBoxModel());
        assertNull(combobox.mySimpleModel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithInvalidModel() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setModel(new JButton());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        model.addElement("Two");
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedIndex(0);

        Object value = combobox.getSelectedValue();
        assertEquals("One", value);
    }

    @Test
    public void testGetSelectedValueWhenNoSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);

        Object value = combobox.getSelectedValue();
        assertNull(value);
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        model.addElement("Two");
        SimpleComboBox combobox = new SimpleComboBox(model);

        combobox.setSelectedValue("One");
        Object selectedValue = combobox.getSelectedValue();
        assertEquals("One", selectedValue);

        combobox.setSelectedValue("Three");
        selectedValue = combobox.getSelectedValue();
        assertNull(selectedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithInvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        SimpleComboBox combobox = new SimpleComboBox(model);

        combobox.setSelectedValue("Three");
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        model.addElement("Two");
        SimpleComboBox combobox = new SimpleComboBox(model);
        model.setSelectedIndex(0);

        int value = combobox.getSelectedInt();
        assertEquals(0, value);
    }

    @Test
    public void testGetSelectedIntWhenNoSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);

        int value = combobox.getSelectedInt();
        assertEquals(-1, value);
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        model.addElement("Two");
        SimpleComboBox combobox = new SimpleComboBox(model);
        model.setSelectedIndex(0);

        long value = combobox.getSelectedLong();
        assertEquals(0L, value);
    }

    @Test
    public void testGetSelectedLongWhenNoSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);

        long value = combobox.getSelectedLong();
        assertEquals(-1L, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);

        combobox.getSelectedLong();
    }

    @Test
    public void testSetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        model.addElement("Two");
        SimpleComboBox combobox = new SimpleComboBox(model);

        combobox.setSelectedIndex(0);
        int selectedIndex = combobox.getSelectedInt();
        assertEquals(0, selectedIndex);

        combobox.setSelectedIndex(1);
        selectedIndex = combobox.getSelectedInt();
        assertEquals(-1, selectedIndex);

        combobox.setSelectedIndex(0);
        combobox.setSelectedInt(1);
        selectedIndex = combobox.getSelectedInt();
        assertEquals(-1, selectedIndex);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedIntWithInvalidIndex() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);

        combobox.setSelectedInt(1);
    }

    @Test
    public void testSetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        model.addElement("Two");
        SimpleComboBox combobox = new SimpleComboBox(model);

        combobox.setSelectedIndex(0);
        long selectedIndex = combobox.getSelectedLong();
        assertEquals(0L, selectedIndex);

        combobox.setSelectedIndex(1);
        selectedIndex = combobox.getSelectedLong();
        assertEquals(-1L, selectedIndex);

        combobox.setSelectedIndex(0);
        combobox.setSelectedLong(1L);
        selectedIndex = combobox.getSelectedLong();
        assertEquals(-1L, selectedIndex);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedLongWithInvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);

        combobox.setSelectedLong(1L);
    }
}