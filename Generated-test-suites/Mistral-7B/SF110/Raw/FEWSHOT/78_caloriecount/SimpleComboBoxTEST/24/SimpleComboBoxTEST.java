package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxWithSimpleComboBoxModel() {
        final SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        final Object selectedValue = new Integer(1);
        when(model.getSelectedValue()).thenReturn(selectedValue);

        final SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(model, comboBox.getModel());
        assertEquals(selectedValue, comboBox.getSelectedValue());

        comboBox.setSelectedValue("test");
        verify(model).setSelectedValue("test");
    }

    @Test
    public void testSetModel() {
        final SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        final SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setModel(new DefaultComboBoxModel());
        assertNull(comboBox.mySimpleModel);

        comboBox.setModel(model);
        assertSame(model, comboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        final SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        final Object selectedValue = new Integer(1);
        when(model.getSelectedValue()).thenReturn(selectedValue);

        final SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(selectedValue, comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWhenNoModel() {
        final SimpleComboBox comboBox = new SimpleComboBox(null);
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        final SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        final SimpleComboBox comboBox = new SimpleComboBox(model);
        final Object value = new Integer(1);

        comboBox.setSelectedValue(value);
        verify(model).setSelectedValue(value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithInvalidType() {
        final SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        final SimpleComboBox comboBox = new SimpleComboBox(model);

        comboBox.setSelectedValue("test");
    }

    @Test
    public void testGetSelectedInt() {
        final SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        final Object selectedValue = new Integer(1);
        when(model.getSelectedValue()).thenReturn(selectedValue);

        final SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWhenNoModel() {
        final SimpleComboBox comboBox = new SimpleComboBox(null);
        assertEquals(-1, comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        final SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        final Object selectedValue = new Long(1);
        when(model.getSelectedValue()).thenReturn(selectedValue);

        final SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(1L, comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWhenNoModel() {
        final SimpleComboBox comboBox = new SimpleComboBox(null);
        assertEquals(-1L, comboBox.getSelectedLong());
    }
}