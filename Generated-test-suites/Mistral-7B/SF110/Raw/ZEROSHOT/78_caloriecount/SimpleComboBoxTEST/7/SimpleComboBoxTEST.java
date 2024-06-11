package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

/**
 * SimpleComboBoxTest class file.
 *
 * @author [Your Name]
 */
public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxWithValidSimpleComboBoxModel() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);

        // Test constructor
        assertNotNull(simpleComboBox);
        assertEquals(model, simpleComboBox.getModel());

        // Test setModel
        SimpleComboBox newSimpleComboBox = new SimpleComboBox(new DefaultComboBoxModel());
        newSimpleComboBox.setModel(model);
        assertEquals(model, newSimpleComboBox.getModel());

        // Test getSelectedValue
        model.addElement("Element 1");
        model.addElement("Element 2");
        model.setSelectedIndex(0);
        assertEquals("Element 1", simpleComboBox.getSelectedValue());

        // Test setSelectedValue
        simpleComboBox.setSelectedValue("Element 2");
        assertEquals("Element 2", simpleComboBox.getSelectedValue());

        // Test getSelectedInt
        model.setSelectedIndex(1);
        assertEquals(1, simpleComboBox.getSelectedInt());

        // Test getSelectedLong
        model.setSelectedIndex(2);
        Long selectedLongValue = new Long(3);
        model.setSelectedItem(selectedLongValue);
        assertEquals(3L, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithInvalidType() {
        final SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue('A');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidType() {
        final SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setModel(new DefaultComboBoxModel());
        simpleComboBox.setSelectedItem("Invalid Type");
        simpleComboBox.getSelectedInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidType() {
        final SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setModel(new DefaultComboBoxModel());
        simpleComboBox.setSelectedItem("Invalid Type");
        simpleComboBox.getSelectedLong();
    }
}