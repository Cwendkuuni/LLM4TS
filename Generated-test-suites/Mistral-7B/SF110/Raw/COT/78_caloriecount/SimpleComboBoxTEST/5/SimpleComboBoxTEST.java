package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxWithNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSimpleComboBoxWithValidModel() {
        SimpleComboBoxModel mySimpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(mySimpleModel);
        mySimpleModel.addElement("Test1");
        mySimpleModel.setSelectedIndex(0);
        assertEquals("Test1", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetModelWithNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel());
        assertNull(simpleComboBox.getSelectedValue());
        try {
            simpleComboBox.setModel(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSetModelWithValidModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel());
        JComboBoxModel newModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(newModel);
        assertEquals(newModel, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValueWithNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithStringValue() {
        SimpleComboBoxModel mySimpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(mySimpleModel);
        mySimpleModel.addElement("Test1");
        mySimpleModel.setSelectedIndex(0);
        assertEquals("Test1", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithIntegerValue() {
        SimpleComboBoxModel mySimpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(mySimpleModel);
        mySimpleModel.addElement(new Integer(1));
        mySimpleModel.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedValueWithLongValue() {
        SimpleComboBoxModel mySimpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(mySimpleModel);
        mySimpleModel.addElement(new Long(1L));
        mySimpleModel.setSelectedIndex(0);
        assertEquals(1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedValueWithShortValue() {
        SimpleComboBoxModel mySimpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(mySimpleModel);
        mySimpleModel.addElement(Short.valueOf((short)1));
        mySimpleModel.setSelectedIndex(0);
        assertEquals((short)1, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithByteValue() {
        SimpleComboBoxModel mySimpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(mySimpleModel);
        mySimpleModel.addElement(Byte.valueOf((byte)1));
        mySimpleModel.setSelectedIndex(0);
        assertEquals((byte)1, simpleComboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedValueWithInvalidValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.getSelectedValue();
    }
}