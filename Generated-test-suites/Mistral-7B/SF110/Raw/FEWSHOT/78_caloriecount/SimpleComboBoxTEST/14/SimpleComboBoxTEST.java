package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBox() {
        SimpleComboBox model = new SimpleComboBox(new SimpleComboBoxModel());
        assertNull(model.getSelectedValue());
        assertNull(model.mySimpleModel);
    }

    @Test
    public void testSetModel() {
        SimpleComboBox combobox = new SimpleComboBox(null);
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        combobox.setModel(model);
        assertSame(model, combobox.mySimpleModel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWrongType() {
        SimpleComboBox combobox = new SimpleComboBox(null);
        Object wrongModel = new Object();
        combobox.setModel(wrongModel);
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        model.setSelectedItem("foo");
        assertEquals("foo", combobox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueNullModel() {
        SimpleComboBox combobox = new SimpleComboBox(null);
        assertNull(combobox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedValue("foo");
        assertEquals("foo", model.getSelectedItem());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWrongType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedValue(123L);
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        model.addElement("One");
        model.addElement("Two");
        model.setSelectedItem("Two");
        assertEquals(1, combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntNullModel() {
        SimpleComboBox combobox = new SimpleComboBox(null);
        assertEquals(-1, combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntNonNumber() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        model.addElement("One");
        model.setSelectedItem("Two");
        assertEquals(-1, combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        model.addElement(1);
        model.addElement(2L);
        model.setSelectedItem(2L);
        assertEquals(2L, combobox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongNullModel() {
        SimpleComboBox combobox = new SimpleComboBox(null);
        assertEquals(-1L, combobox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongNonNumber() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        model.addElement(1);
        model.addElement("Two");
        model.setSelectedItem("Two");
        assertEquals(-1L, combobox.getSelectedLong());
    }
}