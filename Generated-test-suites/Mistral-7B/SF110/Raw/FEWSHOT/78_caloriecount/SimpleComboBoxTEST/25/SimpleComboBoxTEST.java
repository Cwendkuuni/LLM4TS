package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;

import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {

    private ComboBoxModel model;
    private SimpleComboBox comboBox;

    @Before
    public void setUp() {
        model = new DefaultComboBoxModel();
        comboBox = new SimpleComboBox(model);
    }

    @Test
    public void testSimpleComboBoxWithValidModel() {
        SimpleComboBoxModel simpleModel = mock(SimpleComboBoxModel.class);
        when(simpleModel.getSelectedValue()).thenReturn("Test Value");

        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
        assertEquals("Test Value", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetModelWithNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertNull(simpleComboBox.mySimpleModel);
    }

    @Test
    public void testSetModelWithValidModel() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
        assertSame(simpleModel, simpleComboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValueWithNullModel() {
        assertNull(new SimpleComboBox(null).getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithValidModel() {
        Object selectedValue = "Test Value";
        when(model.getSelectedItem()).thenReturn(selectedValue);
        when(model.getSelectedIndex()).thenReturn(0);

        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertEquals(selectedValue, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithNoSelection() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithNullValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertNull(simpleComboBox.getSelectedValue());
        simpleComboBox.setSelectedValue(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithInteger() {
        when(model.getItemAt(0)).thenReturn(new Integer(1));
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue(1);
        assertEquals(new Integer(1), simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithLong() {
        when(model.getItemAt(0)).thenReturn(Long.valueOf(1));
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue(1L);
        assertEquals(1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testSetSelectedValueWithShort() {
        when(model.getItemAt(0)).thenReturn((short) 1);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue((short) 1);
        assertEquals((short) 1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testSetSelectedValueWithByte() {
        when(model.getItemAt(0)).thenReturn((byte) 1);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue((byte) 1);
        assertEquals((byte) 1, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithInvalidType() {
        String value = "Test Value";
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue(value);
    }

    @Test
    public void testGetSelectedIntWithNullSelectedValue() {
        assertNull(new SimpleComboBox(null).getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithIntegerSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        when(model.getSelectedItem()).thenReturn(new Integer(1));
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithLongSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        when(model.getSelectedItem()).thenReturn(Long.valueOf(1));
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithShortSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        when(model.getSelectedItem()).thenReturn((short) 1);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithByteSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        when(model.getSelectedItem()).thenReturn((byte) 1);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLongWithNullSelectedValue() {
        assertNull(new SimpleComboBox(null).getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithIntegerSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        when(model.getSelectedItem()).thenReturn(new Integer(1));
        assertEquals(1, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithLongSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        when(model.getSelectedItem()).thenReturn(Long.valueOf(1));
        assertEquals(1, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithShortSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        when(model.getSelectedItem()).thenReturn((short) 1);
        assertEquals(1, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithByteSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        when(model.getSelectedItem()).thenReturn((byte) 1);
        assertEquals(1, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testModelChangeEvent() {
        SimpleComboBoxModel simpleModel = mock(SimpleComboBoxModel.class);
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
        final int oldSize = simpleModel.getSize();
        final String newItem = "New Item";
        when(simpleModel.getSize()).thenReturn(oldSize + 1);
        when(simpleModel.getItemAt(oldSize)).thenReturn(newItem);

        simpleModel.addListDataListener(e -> {
            assertEquals(ListDataEvent.CONTENTS_CHANGED, e.getID());
            assertEquals(oldSize + 1, simpleModel.getSize());
            assertEquals(newItem, simpleComboBox.getSelectedItem());
        });

        simpleModel.addElement(newItem);
    }
}