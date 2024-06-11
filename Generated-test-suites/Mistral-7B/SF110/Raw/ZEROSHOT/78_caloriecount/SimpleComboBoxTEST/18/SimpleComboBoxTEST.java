package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxConstructionWithModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertNotNull(comboBox);
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        ComboBoxModel otherModel = new DefaultComboBoxModel();
        comboBox.setModel(otherModel);
        assertEquals(otherModel, comboBox.getModel());
        comboBox.setModel(model);
        assertEquals(model, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValueIsNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithInteger() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement(new Integer(1));
        model.setSelectedValue(new Integer(1));
        assertEquals(new Integer(1), comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement(new Long(1));
        model.setSelectedValue(new Long(1));
        assertEquals(new Long(1), comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithShort() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement(new Short((short)1));
        model.setSelectedValue(new Short((short)1));
        assertEquals(new Short((short)1), comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithByte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement(new Byte((byte)1));
        model.setSelectedValue(new Byte((byte)1));
        assertEquals(new Byte((byte)1), comboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedValueWithWrongType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement("String");
        model.setSelectedValue("String");
    }

    @Test
    public void testSetSelectedValueWithInteger() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement(new Integer(1));
        comboBox.setSelectedValue(new Integer(1));
        assertEquals(new Integer(1), comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement(new Long(1));
        comboBox.setSelectedValue(new Long(1));
        assertEquals(new Long(1), comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithShort() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement(new Short((short)1));
        comboBox.setSelectedValue(new Short((short)1));
        assertEquals(new Short((short)1), comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithByte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement(new Byte((byte)1));
        comboBox.setSelectedValue(new Byte((byte)1));
        assertEquals(new Byte((byte)1), comboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithWrongType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue("String");
    }

    @Test
    public void testGetSelectedIntIsNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithInteger() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement(new Integer(1));
        model.setSelectedValue(new Integer(1));
        assertEquals(1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement(new Long(1));
        model.setSelectedValue(new Long(1));
        assertEquals(1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithShort() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement(new Short((short)1));
        model.setSelectedValue(new Short((short)1));
        assertEquals(1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithByte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement(new Byte((byte)1));
        model.setSelectedValue(new Byte((byte)1));
        assertEquals(1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLongIsNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertEquals(-1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithInteger() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement(new Integer(1));
        model.setSelectedValue(new Integer(1));
        assertEquals(1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement(new Long(1));
        model.setSelectedValue(new Long(1));
        assertEquals(1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithShort() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement(new Short((short)1));
        model.setSelectedValue(new Short((short)1));
        assertEquals(1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithByte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        model.addElement(new Byte((byte)1));
        model.setSelectedValue(new Byte((byte)1));
        assertEquals(1L, comboBox.getSelectedLong());
    }
}