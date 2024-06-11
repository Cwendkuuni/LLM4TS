package com.lts.swing.combobox;

import static org.junit.Assert.*;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBox() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNotNull(simpleComboBox);
    }

    @Test
    public void testSetModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        ComboBoxModel model = new DefaultComboBoxModel();
        simpleComboBox.setModel(model);
        assertFalse(simpleComboBox.mySimpleModel instanceof SimpleComboBoxModel);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithWrongType() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setModel(new Object());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedIndex(0);
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertNotNull(selectedValue);
    }

    @Test
    public void testGetSelectedValueWhenEmpty() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertNull(selectedValue);
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedIndex(0);
        simpleComboBox.setModel(new DefaultComboBoxModel(new String[]{"One", "Two", "Three"}));
        int selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(0, selectedInt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithWrongType() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setModel(new DefaultComboBoxModel(new String[]{"One", "Two", "Three"}));
        simpleComboBox.setSelectedIndex(0);
        simpleComboBox.setSelectedValue("Three");
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedIndex(0);
        simpleComboBox.setModel(new DefaultComboBoxModel(new Object[] {1, 2, 3}));
        long selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(1, selectedLong);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithWrongType() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setModel(new DefaultComboBoxModel(new Object[] {1, 2, 3}));
        simpleComboBox.setSelectedIndex(0);
        simpleComboBox.setSelectedValue("Three");
        simpleComboBox.getSelectedLong();
    }
}