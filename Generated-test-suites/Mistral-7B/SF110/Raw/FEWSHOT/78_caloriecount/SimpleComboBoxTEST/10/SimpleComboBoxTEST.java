package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBox() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        assertEquals(model, combobox.getModel());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model1 = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model1);
        SimpleComboBoxModel model2 = new SimpleComboBoxModel();
        combobox.setModel(model2);
        assertSame(model2, combobox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Object selectedValue = new Integer(1);
        model.setSelectedValue(selectedValue);
        assertEquals(selectedValue, combobox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue_whenNullModel() {
        SimpleComboBox combobox = new SimpleComboBox(null);
        assertNull(combobox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Integer selectedValue = new Integer(1);
        model.setSelectedValue(selectedValue);
        assertEquals(selectedValue.intValue(), combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_whenNullSelectedValue() {
        SimpleComboBox combobox = new SimpleComboBox(null);
        assertEquals(-1, combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_whenSelectedValueIsLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Long selectedValue = new Long(1);
        model.setSelectedValue(selectedValue);
        assertEquals(selectedValue.intValue(), combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_whenSelectedValueIsShort() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Short selectedValue = new Short("1");
        model.setSelectedValue(selectedValue);
        assertEquals(selectedValue.intValue(), combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_whenSelectedValueIsByte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Byte selectedValue = new Byte("1");
        model.setSelectedValue(selectedValue);
        assertEquals(selectedValue.intValue(), combobox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_whenSelectedValueIsNotANumber() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        String selectedValue = "not a number";
        model.setSelectedValue(selectedValue);
        combobox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Long selectedValue = new Long(1);
        model.setSelectedValue(selectedValue);
        assertEquals(selectedValue, combobox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_whenNullSelectedValue() {
        SimpleComboBox combobox = new SimpleComboBox(null);
        assertEquals(-1L, combobox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_whenSelectedValueIsInteger() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Integer selectedValue = new Integer(1);
        model.setSelectedValue(selectedValue);
        assertEquals(selectedValue.longValue(), combobox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_whenSelectedValueIsShort() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Short selectedValue = new Short("1");
        model.setSelectedValue(selectedValue);
        assertEquals(selectedValue.longValue(), combobox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_whenSelectedValueIsByte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Byte selectedValue = new Byte("1");
        model.setSelectedValue(selectedValue);
        assertEquals(selectedValue.longValue(), combobox.getSelectedLong());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        Object selectedValue = new Integer(1);
        combobox.setSelectedValue(selectedValue);
        assertEquals(selectedValue, model.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValue_whenInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        String selectedValue = "not a number";
        combobox.setSelectedValue(selectedValue);
    }
}