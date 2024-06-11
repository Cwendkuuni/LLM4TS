package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSetModel() {
        final SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        final SimpleComboBox comboBox = new SimpleComboBox(model);
        assertSame(model, comboBox.mySimpleModel);
        comboBox.setModel(null);
        assertNull(comboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        final SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        final Integer value = 1;
        when(model.getSelectedValue()).thenReturn(value);
        final SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(value, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        final SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        final Integer value = 1;
        when(model.getSelectedValue()).thenReturn(null);
        final SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue(value);
        assertEquals(value, model.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        final SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        final Integer value = 1;
        when(model.getSelectedValue()).thenReturn(value);
        final SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(value.intValue(), comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        final SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        final Long value = 1L;
        when(model.getSelectedValue()).thenReturn(value);
        final SimpleComboBox comboBox = new SimpleComboBox(model);
        assertEquals(value.longValue(), comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithNullModel() {
        final SimpleComboBox comboBox = new SimpleComboBox(null);
        comboBox.getSelectedInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithNullModel() {
        final SimpleComboBox comboBox = new SimpleComboBox(null);
        comboBox.getSelectedLong();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithWrongTypeModel() {
        final SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        final String value = "value";
        when(model.getSelectedValue()).thenReturn(value);
        final SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.getSelectedInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithWrongTypeModel() {
        final SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        final Float value = 1.0F;
        when(model.getSelectedValue()).thenReturn(value);
        final SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.getSelectedLong();
    }

    @Test
    public void testGetSelectedIntWithNullValue() {
        final SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        final SimpleComboBox comboBox = new SimpleComboBox(model);
        assertNull(comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLongWithNullValue() {
        final SimpleComboBoxModel model = mock(SimpleComboBoxModel.class);
        final SimpleComboBox comboBox = new SimpleComboBox(model);
        assertNull(comboBox.getSelectedLong());
    }
}