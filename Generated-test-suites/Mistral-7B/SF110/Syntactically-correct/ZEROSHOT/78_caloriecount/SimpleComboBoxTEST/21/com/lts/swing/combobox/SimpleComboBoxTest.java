package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxWithIntegerValues() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        model.addElement(3);

        SimpleComboBox simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedIndex(1);
        assertEquals(2, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue(3);
        assertEquals(3, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testSimpleComboBoxWithLongValues() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        model.addElement(3L);

        SimpleComboBox simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedIndex(1);
        assertEquals(2, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue(3);
        assertEquals(3, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testSimpleComboBoxWithNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertNull(simpleComboBox.getSelectedValue());

        simpleComboBox.setModel(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleComboBoxWithNonSupportedType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("apple");

        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue("banana");
    }

    @Test
    public void testSimpleComboBoxWithNullSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);

        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSimpleComboBoxWithInvalidGetSelectedValueType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Object());

        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue(new Object());

        try {
            simpleComboBox.getSelectedInt();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testSimpleComboBoxWithInvalidGetSelectedLongType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Object());

        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue(new Object());

        try {
            simpleComboBox.getSelectedLong();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}