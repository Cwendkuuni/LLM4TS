package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBox() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertNull(simpleComboBox.getSelectedValue());
        assertSame(model, simpleComboBox.getModel());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        ComboBoxModel newModel = new DefaultComboBoxModel();
        simpleComboBox.setModel(newModel);
        assertNotSame(model, simpleComboBox.getModel());
        assertSame(newModel, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValueWhenNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWhenIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());
        assertEquals(1, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWhenLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());
        assertEquals(1L, simpleComboBox.getSelectedLong());
        assertEquals(1L, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWhenShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());
        assertEquals((short) 1, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWhenByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());
        assertEquals((byte) 1, simpleComboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedValueWhenInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("String");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
    }

    @Test
    public void testSetSelectedValueWhenIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue(1);
        assertEquals(1, simpleComboBox.getSelectedInt());
        assertEquals(1, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWhenLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue(1L);
        assertEquals(1, simpleComboBox.getSelectedInt());
        assertEquals(1L, simpleComboBox.getSelectedLong());
        assertEquals(1L, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWhenShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue((short) 1);
        assertEquals(1, simpleComboBox.getSelectedInt());
        assertEquals((short) 1, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWhenByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue((byte) 1);
        assertEquals(1, simpleComboBox.getSelectedInt());
        assertEquals((byte) 1, simpleComboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWhenInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue("String");
    }
}