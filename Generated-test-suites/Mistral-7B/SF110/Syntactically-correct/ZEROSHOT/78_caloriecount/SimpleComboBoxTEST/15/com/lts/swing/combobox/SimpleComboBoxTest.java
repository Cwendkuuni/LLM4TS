package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxCreationWithModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);
        assertEquals(model, combobox.getModel());
    }

    @Test
    public void testDefaultConstructor() {
        SimpleComboBox combobox = new SimpleComboBox();
        assertNull(combobox.getModel());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox();
        combobox.setModel(model);
        assertEquals(model, combobox.getModel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithInvalidModel() {
        SimpleComboBox combobox = new SimpleComboBox();
        combobox.setModel(new Object());
    }

    @Test
    public void testGetSelectedValueNullModel() {
        SimpleComboBox combobox = new SimpleComboBox();
        assertNull(combobox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWithModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Test1");
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedIndex(0);
        assertEquals("Test1", combobox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueNull() {
        SimpleComboBox combobox = new SimpleComboBox();
        assertNull(combobox.getSelectedValue());
        combobox.setSelectedValue(null);
        assertNull(combobox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueSimpleInteger() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Test1");
        model.addElement("Test2");
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedIndex(1);
        assertEquals("Test2", combobox.getSelectedValue());
        combobox.setSelectedValue(1);
        assertEquals(1, combobox.getSelectedInt());
    }

    @Test
    public void testSetSelectedValueSimpleLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Test1");
        model.addElement("Test2");
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedIndex(1);
        assertEquals("Test2", combobox.getSelectedValue());
        combobox.setSelectedValue(2L);
        assertEquals(2, combobox.getSelectedInt());
    }

    @Test
    public void testSetSelectedValueSimpleShort() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Test1");
        model.addElement("Test2");
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedIndex(1);
        assertEquals("Test2", combobox.getSelectedValue());
        combobox.setSelectedValue((short) 3);
        assertEquals(3, combobox.getSelectedInt());
    }

    @Test
    public void testSetSelectedValueSimpleByte() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Test1");
        model.addElement("Test2");
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedIndex(1);
        assertEquals("Test2", combobox.getSelectedValue());
        combobox.setSelectedValue((byte) 4);
        assertEquals(4, combobox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueInvalidType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Test1");
        model.addElement("Test2");
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedIndex(1);
        assertEquals("Test2", combobox.getSelectedValue());
        combobox.setSelectedValue("InvalidType");
    }

    @Test
    public void testGetSelectedLongNullModel() {
        SimpleComboBox combobox = new SimpleComboBox();
        assertEquals(-1L, combobox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Test1");
        model.addElement("Test2");
        SimpleComboBox combobox = new SimpleComboBox(model);
        combobox.setSelectedIndex(1);
        assertEquals(2L, combobox.getSelectedLong());
    }

    @Test
    public void testItemStateChanged() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(model);

        List<Integer> indices = Arrays.asList(0, 1);
        for (Integer index : indices) {
            combobox.setSelectedIndex(index);
            assertEquals(ItemEvent.SELECTED, combobox.getItemStateChange(0));
        }
    }

    @Test
    public void testItemStateChangedNullModel() {
        SimpleComboBox combobox = new SimpleComboBox();
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        combobox.setModel(model);

        assertEquals(ItemEvent.DESELECTED, combobox.getItemStateChange(0));
        combobox.setModel(null);
        assertEquals(ItemEvent.DESELECTED, combobox.getItemStateChange(0));
    }

    @Test
    public void testItemStateChangedEmptyModel() {
        SimpleComboBox combobox = new SimpleComboBox();
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        combobox.setModel(model);

        model.addElement("Test1");
        assertEquals(ItemEvent.SELECTED, combobox.getItemStateChange(0));
        model.removeAllElements();
        assertEquals(ItemEvent.DESELECTED, combobox.getItemStateChange(0));
    }

    @Test
    public void testItemStateChangedWithMultipleItems() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Test1");
        model.addElement("Test2");
        model.addElement("Test3");
        SimpleComboBox combobox = new SimpleComboBox(model);

        for (int index = 0; index < 3; index++) {
            combobox.setSelectedIndex(index);
            assertEquals(ItemEvent.SELECTED, combobox.getItemStateChange(index));
            assertEquals(index, combobox.getSelectedIndex());
        }

        for (int index = 0; index < 3; index++) {
            combobox.setSelectedIndex(~(1 << index));
            assertEquals(ItemEvent.DESELECTED, combobox.getItemStateChange(index));
            assertEquals(-1, combobox.getSelectedIndex());
        }
    }
}