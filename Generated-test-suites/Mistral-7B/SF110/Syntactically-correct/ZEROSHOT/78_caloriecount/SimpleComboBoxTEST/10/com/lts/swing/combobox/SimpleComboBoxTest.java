package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxConstructionWithModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel<>();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertEquals(model, simpleComboBox.getModel());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel<>();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);

        ComboBoxModel differentModel = new DefaultComboBoxModel<>();
        simpleComboBox.setModel(differentModel);
        assertNotSame(model, simpleComboBox.getModel());

        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValueNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithSimpleComboBoxModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel<>();
        model.addElement("Element1");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedIndex(0);
        assertEquals("Element1", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithNullValue() {
        SimpleComboBox model = new SimpleComboBox(new SimpleComboBoxModel<>());
        assertNull(model.getSelectedValue());

        model.setSelectedValue(null);
        assertNull(model.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithSimpleComboBoxModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel<>();
        model.addElement("Element1");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedValue("Element1");
        assertEquals("Element1", simpleComboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithNonSupportedType() {
        SimpleComboBox model = new SimpleComboBox(new SimpleComboBoxModel<>());
        model.setSelectedValue('A');
    }

    @Test
    public void testGetSelectedIntNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithSimpleComboBoxModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel<>();
        model.addElement(1);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLongNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithSimpleComboBoxModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel<>();
        model.addElement(1L);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedIndex(0);
        assertEquals(1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedIntWithStringValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel<>();
        model.addElement("1");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel<>();
        model.addElement(1L);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel<>();
        model.addElement((short)1);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel<>();
        model.addElement((byte)1);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }
}