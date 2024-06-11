
package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleComboBoxTest_Complete {

    @Test
    public void testSimpleComboBoxCreation() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNotNull(comboBox);
    }

    @Test
    public void testSetModel() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        ComboBoxModel model = new DefaultComboBoxModel();
        comboBox.setModel(model);
        assertEquals(model, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValueNull() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueInteger() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        Object selectedValue = comboBox.getSelectedValue();
        assertEquals(1, selectedValue);
    }

    @Test
    public void testSetSelectedValueAndGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue(1);
        Object selectedValue = comboBox.getSelectedValue();
        assertEquals(1, selectedValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntOrLongWithNonSupportedType() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        comboBox.setSelectedValue("test");
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        int selectedInt = comboBox.getSelectedInt();
        assertEquals(1, selectedInt);
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        long selectedLong = comboBox.getSelectedLong();
        assertEquals(1, selectedLong);
    }

    @Test
    public void testEmptyModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertFalse(comboBox.getItemCount() > 0);
    }

    @Test
    public void testMultipleSelectedItems() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        Object[] selectedValues = new Object[2];
        selectedValues[0] = 1;
        selectedValues[1] = 2;
        comboBox.setSelectedItems(selectedValues);
        Object[] actualValues = new Object[2];
        actualValues[0] = comboBox.getSelectedValue();
        actualValues[1] = comboBox.getSelectedValue();
        assertArrayEquals(selectedValues, actualValues);
    }

    @Test(expected = NullPointerException.class)
    public void testGetSelectedIntOrLongWithNullSelectedValue() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        comboBox.setSelectedValue(null);
        try {
            comboBox.getSelectedInt();
        } catch (IllegalArgumentException e) {
            fail("Expected NullPointerException");
        }
    }

    @Test(expected = NumberFormatException.class)
    public void testGetSelectedIntOrLongWithNonNumericValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("test");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue("test");
        try {
            comboBox.getSelectedInt();
        } catch (IllegalArgumentException e) {
            fail("Expected NumberFormatException");
        }
    }

    @Test
    public void testOutOfBoundsIndex() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        try {
            comboBox.getSelectedInt(2);
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testGetSelectedIntCyclicCalls() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        int currentSelectedInt = comboBox.getSelectedInt();
        comboBox.setSelectedInt(currentSelectedInt);
        int newSelectedInt = comboBox.getSelectedInt();
        assertEquals(currentSelectedInt, newSelectedInt);
    }

    @Test
    public void testGetSelectedLongCyclicCalls() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        long currentSelectedLong = comboBox.getSelectedLong();
        comboBox.setSelectedLong(currentSelectedLong);
        long newSelectedLong = comboBox.getSelectedLong();
        assertEquals(currentSelectedLong, newSelectedLong);
    }

    @Test
    public void testSetSelectedValueToCustomObject() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new SimpleObject());
        SimpleComboBox comboBox = new SimpleComboBox(model);
        SimpleObject selectedValue = new SimpleObject();
        comboBox.setSelectedValue(selectedValue);
        Object actualValue = comboBox.getSelectedValue();
        assertSame(selectedValue, actualValue);
    }
}

class SimpleObject {
    @Override
    public String toString() {
        return "SimpleObject";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleObject)) return false;
        SimpleObject that = (SimpleObject) o;
        return true;
    }
}
