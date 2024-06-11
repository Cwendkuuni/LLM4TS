package com.lts.swing.combobox;
import static org.junit.Assert.*;

import javax.swing.ComboBoxModel;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testGetSelectedValue() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel() {
            @Override
            public Object getSelectedItem() {
                return "Item 1";
            }
        });
        assertEquals("Item 1", combobox.getSelectedValue());
    }

    @Test
    public void testConstructorNullModel() {
        assertThrows(IllegalArgumentException.class, () -> new SimpleComboBox(null));
    }

    @Test
    public void testSetModelNullModel() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        assertThrows(IllegalArgumentException.class, () -> combobox.setModel(null));
    }

    @Test
    public void testGetSelectedValueWhenNoSelection() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNull(combobox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntNullValue() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue(null);
        combobox.getSelectedInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongNullValue() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue(null);
        combobox.getSelectedLong();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWrongDataType() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue(new Object());
        combobox.getSelectedInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWrongDataType() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue(new Object());
        combobox.getSelectedLong();
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue("Item 1");
        assertEquals("Item 1", combobox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue(1);
        assertEquals(1, combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue(1L);
        assertEquals(1L, combobox.getSelectedLong());
    }

    @Test
    public void testGetSelectedIntString() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue("String Value");
        assertEquals(-1, combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLongString() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue("String Value");
        assertEquals(-1L, combobox.getSelectedLong());
    }

    @Test
    public void testGetSelectedIntByte() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue((byte) 1);
        assertEquals(1, combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLongByte() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue((byte) 1);
        assertEquals(1L, combobox.getSelectedLong());
    }

    @Test
    public void testGetSelectedIntShort() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue((short) 1);
        assertEquals(1, combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLongShort() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue((short) 1);
        assertEquals(1L, combobox.getSelectedLong());
    }

    @Test
    public void testGetSelectedIntInteger() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue(1);
        assertEquals(1, combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLongInteger() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue(1);
        assertEquals(1L, combobox.getSelectedLong());
    }

    @Test
    public void testGetSelectedIntLong() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue(1L);
        assertEquals(1, combobox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLongLong() {
        SimpleComboBox combobox = new SimpleComboBox(new SimpleComboBoxModel());
        combobox.setSelectedValue(1L);
        assertEquals(1L, combobox.getSelectedLong());
    }
}