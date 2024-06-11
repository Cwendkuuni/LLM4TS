package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxConstructionWithModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertNotNull(simpleComboBox);
        assertEquals(model, simpleComboBox.getModel());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(newModel);
        assertEquals(newModel, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValueNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertEquals(1, selectedValue);
    }

    @Test
    public void testGetSelectedValueStringValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertEquals("One", selectedValue);
    }

    @Test
    public void testGetSelectedValueLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertEquals(1L, selectedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedValueInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Object());
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.getSelectedValue();
    }

    @Test
    public void testSetSelectedValueInteger() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue(1);
        assertEquals(1, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueString() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue("One");
        assertEquals("One", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue(1L);
        assertEquals(1L, simpleComboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Object());
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue(new Object());
    }

    @Test
    public void testGetSelectedIntNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Object());
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue("One");
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLongNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1L, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("One");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue("One");
        simpleComboBox.getSelectedLong();
    }
}