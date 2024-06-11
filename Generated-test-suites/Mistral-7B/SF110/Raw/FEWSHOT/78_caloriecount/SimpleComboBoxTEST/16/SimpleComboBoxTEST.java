package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSetAndGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        String value = "test";
        comboBox.setSelectedValue(value);
        assertEquals(value, comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWhenNullModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        Integer value = 123;
        comboBox.setSelectedValue(value);
        assertEquals(value.intValue(), comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWhenNullSelectedValue() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWhenStringSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        String value = "test";
        comboBox.setSelectedValue(value);
        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWhenIntegerSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        Integer value = 123;
        comboBox.setSelectedValue(value);
        assertEquals(value.intValue(), comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWhenLongSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        Long value = 123L;
        comboBox.setSelectedValue(value);
        assertEquals(value.intValue(), comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWhenShortSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        Short value = (short)123;
        comboBox.setSelectedValue(value);
        assertEquals(value, comboBox.getSelectedValue());
        assertEquals(value, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWhenByteSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        Byte value = (byte)123;
        comboBox.setSelectedValue(value);
        assertEquals(value, comboBox.getSelectedValue());
        assertEquals(value.intValue(), comboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWhenInvalidSelectedValue() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        comboBox.setSelectedValue("test");
    }

    @Test
    public void testSetAndGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        Long value = 123L;
        comboBox.setSelectedValue(value);
        assertEquals(value, comboBox.getSelectedValue());
        assertEquals(value.longValue(), comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWhenNullSelectedValue() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertEquals(-1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWhenStringSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        String value = "test";
        comboBox.setSelectedValue(value);
        assertEquals(-1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWhenIntegerSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        Integer value = 123;
        comboBox.setSelectedValue(value);
        assertEquals(value.longValue(), comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWhenLongSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        Long value = 123L;
        comboBox.setSelectedValue(value);
        assertEquals(value, comboBox.getSelectedValue());
        assertEquals(value.longValue(), comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWhenShortSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        Short value = (short)123;
        comboBox.setSelectedValue(value);
        assertEquals(value.longValue(), comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWhenByteSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        Byte value = (byte)123;
        comboBox.setSelectedValue(value);
        assertEquals(value.longValue(), comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWhenInvalidSelectedValue() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        comboBox.setSelectedValue("test");
    }

    @Test
    public void testSetModelAndGetMySimpleModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(model, comboBox.getModel());
        assertEquals(model, comboBox.mySimpleModel);
    }

    @Test
    public void testSetModelWithInvalidModel() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        assertNull(comboBox.getModel());
        assertNull(comboBox.mySimpleModel);
    }
}