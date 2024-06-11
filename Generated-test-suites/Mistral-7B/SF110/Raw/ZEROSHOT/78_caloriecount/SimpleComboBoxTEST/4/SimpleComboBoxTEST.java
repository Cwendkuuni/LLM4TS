package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxConstructionWithModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertSame(model, simpleComboBox.getModel());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox();
        simpleComboBox.setModel(model);
        assertSame(model, simpleComboBox.getModel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithInvalidModelType() {
        SimpleComboBox simpleComboBox = new SimpleComboBox();
        simpleComboBox.setModel(new DefaultComboBoxModel());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Option One");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);
        assertEquals("Option One", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWhenNoSelection() {
        SimpleComboBox simpleComboBox = new SimpleComboBox();
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(1);
        assertEquals(2, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWhenNoSelection() {
        SimpleComboBox simpleComboBox = new SimpleComboBox();
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidSelection() {
        SimpleComboBox simpleComboBox = new SimpleComboBox();
        simpleComboBox.setModel(new SimpleComboBoxModel());
        simpleComboBox.addItem("Option One");
        simpleComboBox.setSelectedValue("Invalid Selection");
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(1);
        assertEquals(2L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWhenNoSelection() {
        SimpleComboBox simpleComboBox = new SimpleComboBox();
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidSelection() {
        SimpleComboBox simpleComboBox = new SimpleComboBox();
        simpleComboBox.setModel(new SimpleComboBoxModel());
        simpleComboBox.addItem(1);
        simpleComboBox.setSelectedValue("Invalid Selection");
    }
}