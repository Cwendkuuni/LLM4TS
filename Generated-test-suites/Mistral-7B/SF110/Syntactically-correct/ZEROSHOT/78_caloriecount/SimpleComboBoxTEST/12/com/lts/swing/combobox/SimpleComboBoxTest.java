package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxWithIntegerValues() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        model.addElement(3);

        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals("Unexpected initial value", 0, comboBox.getSelectedInt());

        comboBox.setSelectedIndex(1);
        assertEquals("Unexpected selected value after setting index", 2, comboBox.getSelectedInt());

        comboBox.setSelectedValue(2);
        assertEquals("Unexpected selected value after setting value directly", 2, comboBox.getSelectedInt());
    }

    @Test
    public void testSimpleComboBoxWithLongValues() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        model.addElement(3L);

        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals("Unexpected initial value", 0, comboBox.getSelectedLong());

        comboBox.setSelectedIndex(1);
        assertEquals("Unexpected selected value after setting index", 2, comboBox.getSelectedLong());

        comboBox.setSelectedValue(2);
        assertEquals("Unexpected selected value after setting value directly", 2, comboBox.getSelectedLong());
    }

    @Test
    public void testSimpleComboBoxWithShortValues() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 1);
        model.addElement((short) 2);
        model.addElement((short) 3);

        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals("Unexpected initial value", 0, comboBox.getSelectedInt());

        comboBox.setSelectedIndex(1);
        assertEquals("Unexpected selected value after setting index", 2, comboBox.getSelectedInt());

        comboBox.setSelectedValue((short) 2);
        assertEquals("Unexpected selected value after setting value directly", 2, comboBox.getSelectedInt());
    }

    @Test
    public void testSimpleComboBoxWithByteValues() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 1);
        model.addElement((byte) 2);
        model.addElement((byte) 3);

        SimpleComboBox comboBox = new SimpleComboBox(model);

        assertEquals("Unexpected initial value", 0, comboBox.getSelectedInt());

        comboBox.setSelectedIndex(1);
        assertEquals("Unexpected selected value after setting index", 2, comboBox.getSelectedInt());

        comboBox.setSelectedValue((byte) 2);
        assertEquals("Unexpected selected value after setting value directly", 2, comboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleComboBoxWithNonSupportedType() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("not a number");

        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue("not a number");
    }

    @Test
    public void testSetModel() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        final JComboBox<String> jComboBox = new JComboBox<>();
        jComboBox.setModel(model);

        SimpleComboBox comboBox = new SimpleComboBox(model);
        assertNull("Unexpected mySimpleModel after initializing with null model", comboBox.mySimpleModel);

        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        comboBox.setModel(newModel);
        assertSame("Unexpected mySimpleModel after setting new model", newModel, comboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        model.addElement(3);

        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue(2);

        assertEquals("Unexpected selected value", 2, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        final SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        model.addElement(3);

        SimpleComboBox comboBox = new SimpleComboBox(model);
        comboBox.setSelectedValue(2);

        assertEquals("Unexpected selected value after setting", 2, comboBox.getSelectedInt());
    }
}