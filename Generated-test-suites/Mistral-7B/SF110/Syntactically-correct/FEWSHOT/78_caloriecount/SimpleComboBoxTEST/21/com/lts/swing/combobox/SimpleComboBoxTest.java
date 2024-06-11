package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSetAndGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        String value1 = "One";
        comboBox.setSelectedValue(value1);
        assertEquals(value1, comboBox.getSelectedValue());

        String value2 = "Two";
        comboBox.setSelectedValue(value2);
        assertEquals(value2, comboBox.getSelectedValue());

        Object value3 = new Integer(1);
        comboBox.setSelectedValue(value3);
        assertEquals(value3, comboBox.getSelectedValue());

        Object value4 = new Long(2);
        comboBox.setSelectedValue(value4);
        assertEquals(value4, comboBox.getSelectedValue());

        Object value5 = new Short((short)3);
        comboBox.setSelectedValue(value5);
        assertEquals(value5, comboBox.getSelectedValue());

        Object value6 = new Byte((byte)4);
        comboBox.setSelectedValue(value6);
        assertEquals(value6, comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        Integer intValue1 = new Integer(1);
        comboBox.setSelectedValue(intValue1);
        assertEquals(intValue1.intValue(), comboBox.getSelectedInt());

        Long longValue1 = new Long(2);
        comboBox.setSelectedValue(longValue1);
        assertEquals(-1, comboBox.getSelectedInt());

        Short shortValue1 = new Short((short)3);
        comboBox.setSelectedValue(shortValue1);
        assertEquals(shortValue1.shortValue(), comboBox.getSelectedInt());

        Byte byteValue1 = new Byte((byte)4);
        comboBox.setSelectedValue(byteValue1);
        assertEquals(byteValue1.byteValue(), comboBox.getSelectedInt());

        comboBox.setSelectedValue(null);
        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        Integer intValue1 = new Integer(1);
        comboBox.setSelectedValue(intValue1);
        assertEquals(intValue1.longValue(), comboBox.getSelectedLong());

        Long longValue1 = new Long(2);
        comboBox.setSelectedValue(longValue1);
        assertEquals(longValue1.longValue(), comboBox.getSelectedLong());

        Short shortValue1 = new Short((short)3);
        comboBox.setSelectedValue(shortValue1);
        assertEquals(shortValue1.longValue(), comboBox.getSelectedLong());

        Byte byteValue1 = new Byte((byte)4);
        comboBox.setSelectedValue(byteValue1);
        assertEquals(byteValue1.longValue(), comboBox.getSelectedLong());

        comboBox.setSelectedValue(null);
        assertEquals(-1L, comboBox.getSelectedLong());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model1 = new SimpleComboBoxModel();
        SimpleComboBox comboBox1 = new SimpleComboBox(model1);

        SimpleComboBoxModel model2 = new SimpleComboBoxModel();
        SimpleComboBox comboBox2 = new SimpleComboBox(model2);

        comboBox1.setModel(model2);
        assertNull(comboBox1.getModel());
        assertEquals(model2, comboBox2.getModel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);

        String value = new String("Invalid");
        comboBox.setSelectedValue(value);
    }
}