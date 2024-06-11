package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

/**
 * Test class for SimpleComboBox
 */
public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxConstructor() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNotNull(comboBox);
    }

    @Test
    public void testSetModel() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        comboBox.setModel(new SimpleComboBoxModel());
        assertSame(comboBox.mySimpleModel, comboBox.getModel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithInvalidModel() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        comboBox.setModel(new Object());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        comboBox.setModel(model);
        comboBox.setSelectedIndex(0);

        Object selectedValue = comboBox.getSelectedValue();
        assertEquals(1, selectedValue);
    }

    @Test
    public void testGetSelectedValueWhenNoSelection() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        Object selectedValue = comboBox.getSelectedValue();
        assertNull(selectedValue);
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        comboBox.setModel(model);

        comboBox.setSelectedValue(1);
        assertSame(1, comboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithInvalidType() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        comboBox.setModel(model);

        comboBox.setSelectedValue("abc");
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        comboBox.setModel(model);
        comboBox.setSelectedIndex(0);

        int selectedInt = comboBox.getSelectedInt();
        assertEquals(1, selectedInt);
    }

    @Test
    public void testGetSelectedIntWhenNoSelection() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        int selectedInt = comboBox.getSelectedInt();
        assertEquals(-1, selectedInt);
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        comboBox.setModel(model);
        comboBox.setSelectedIndex(0);

        long selectedLong = comboBox.getSelectedLong();
        assertEquals(1, selectedLong);
    }

    @Test
    public void testGetSelectedLongWhenNoSelection() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        long selectedLong = comboBox.getSelectedLong();
        assertEquals(-1, selectedLong);
    }
}