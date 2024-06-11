package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBox() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        assertEquals(model, combobox.getModel());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel oldModel = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(oldModel);
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        combobox.setModel(newModel);
        assertSame(newModel, combobox.getModel());
        assertNull(combobox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        SimpleComboBox combobox = new SimpleComboBox(model);
        model.setSelectedItem("One");
        assertEquals("One", combobox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithNullModel() {
        SimpleComboBox combobox = new SimpleComboBox(null);
        assertNull(combobox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox combobox = new SimpleComboBox(model);
        model.setSelectedIndex(1);
        assertEquals(2, combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithNullSelectedValue() {
        SimpleComboBox combobox = new SimpleComboBox(null);
        assertSame(-1, combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithNonIntegerSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        SimpleComboBox combobox = new SimpleComboBox(model);
        assertEquals(-1, combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        SimpleComboBox combobox = new SimpleComboBox(model);
        model.setSelectedIndex(1);
        assertEquals(2L, combobox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithNullSelectedValue() {
        SimpleComboBox combobox = new SimpleComboBox(null);
        assertSame(-1L, combobox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithNonLongSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.getSelectedLong();
    }
}