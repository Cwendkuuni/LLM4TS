package com.lts.swing.combobox;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testGetSelectedValue_NullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue_ValidSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        model.addElement(3);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(1);
        assertEquals(2, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue_NullSelection() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue_InvalidSelection() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertThrows(IllegalArgumentException.class, () -> simpleComboBox.getSelectedValue("Invalid Value"));
    }

    @Test
    public void testGetSelectedInt_ValidSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        model.addElement(3);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(1);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_NullSelection() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_InvalidSelection() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue("Invalid Value");
        assertThrows(IllegalArgumentException.class, () -> simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_EdgeCases() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList(Byte.MIN_VALUE, Byte.MAX_VALUE, Short.MIN_VALUE, Short.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Long.MIN_VALUE, Long.MAX_VALUE).toArray());
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        for (Object value : model.toArray()) {
            simpleComboBox.setSelectedValue(value);
            assertEquals(value.hashCode(), simpleComboBox.getSelectedInt());
        }
    }

    @Test
    public void testGetSelectedLong_ValidSelection() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        model.addElement(3);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(1);
        assertEquals(1, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_NullSelection() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertEquals(-1, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_InvalidSelection() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue("Invalid Value");
        assertThrows(IllegalArgumentException.class, () -> simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_EdgeCases() {
        SimpleComboBoxModel model = new SimpleComboBoxModel(Arrays.asList(Byte.MIN_VALUE, Byte.MAX_VALUE, Short.MIN_VALUE, Short.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Long.MIN_VALUE, Long.MAX_VALUE).toArray());
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        for (Object value : model.toArray()) {
            simpleComboBox.setSelectedValue(value);
            assertEquals(value.hashCode() >>> 32, simpleComboBox.getSelectedLong() >>> 32);
            assertEquals(value.hashCode() & 0xFFFFFFFFL, simpleComboBox.getSelectedLong() & 0xFFFFFFFFL);
        }
    }

}