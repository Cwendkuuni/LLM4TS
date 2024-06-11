package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxWithIntegerModel() {
        SimpleComboBoxModel integerModel = new SimpleComboBoxModel();
        integerModel.addElement(1);
        integerModel.addElement(2);

        SimpleComboBox combobox = new SimpleComboBox(integerModel);

        assertEquals(1, combobox.getSelectedInt());
        combobox.setSelectedIndex(1);
        assertEquals(2, combobox.getSelectedInt());
    }

    @Test
    public void testSimpleComboBoxWithLongModel() {
        SimpleComboBoxModel longModel = new SimpleComboBoxModel();
        longModel.addElement(1L);
        longModel.addElement(2L);

        SimpleComboBox combobox = new SimpleComboBox(longModel);

        assertEquals(1, combobox.getSelectedLong());
        combobox.setSelectedIndex(1);
        assertEquals(2, combobox.getSelectedLong());
    }

    @Test
    public void testSimpleComboBoxWithShortModel() {
        SimpleComboBoxModel shortModel = new SimpleComboBoxModel();
        shortModel.addElement((short)1);
        shortModel.addElement((short)2);

        SimpleComboBox combobox = new SimpleComboBox(shortModel);

        assertEquals(1, combobox.getSelectedInt());
        combobox.setSelectedIndex(1);
        assertEquals(2, combobox.getSelectedInt());
    }

    @Test
    public void testSimpleComboBoxWithByteModel() {
        SimpleComboBoxModel byteModel = new SimpleComboBoxModel();
        byteModel.addElement((byte)1);
        byteModel.addElement((byte)2);

        SimpleComboBox combobox = new SimpleComboBox(byteModel);

        assertEquals(1, combobox.getSelectedInt());
        combobox.setSelectedIndex(1);
        assertEquals(2, combobox.getSelectedInt());
    }

    @Test
    public void testSimpleComboBoxWithNullModel() {
        SimpleComboBox combobox = new SimpleComboBox(null);
        assertNull(combobox.getSelectedValue());
    }

    @Test
    public void testSimpleComboBoxWithWrongModelType() {
        ObjectModel objectModel = new ObjectModel();
        objectModel.addElement("one");
        objectModel.addElement("two");

        SimpleComboBox combobox = new SimpleComboBox(objectModel);

        assertThrows(IllegalArgumentException.class, () -> combobox.getSelectedInt());
    }

    @Test
    public void testSetSelectedValueWithNullValue() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(simpleModel);

        assertNull(combobox.getSelectedValue());
        combobox.setSelectedValue(null);
        assertNull(combobox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueWithValidValue() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        simpleModel.addElement(1);
        simpleModel.addElement(2);

        SimpleComboBox combobox = new SimpleComboBox(simpleModel);

        assertEquals(1, combobox.getSelectedInt());
        combobox.setSelectedValue(2);
        assertEquals(2, combobox.getSelectedInt());
    }

    @Test
    public void testSetSelectedValueWithInt() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        simpleModel.addElement(1);
        simpleModel.addElement(2);

        SimpleComboBox combobox = new SimpleComboBox(simpleModel);

        assertEquals(1, combobox.getSelectedInt());
        combobox.setSelectedValue(2);
        assertEquals(2, combobox.getSelectedInt());

        combobox.setSelectedValue(1);
        assertEquals(1, combobox.getSelectedInt());
    }

    @Test
    public void testSetSelectedValueWithLong() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        simpleModel.addElement(1L);
        simpleModel.addElement(2L);

        SimpleComboBox combobox = new SimpleComboBox(simpleModel);

        assertEquals(1, combobox.getSelectedLong());
        combobox.setSelectedValue(2L);
        assertEquals(2, combobox.getSelectedLong());

        combobox.setSelectedValue(1L);
        assertEquals(1, combobox.getSelectedLong());
    }

    @Test
    public void testSetSelectedValueWithShort() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        simpleModel.addElement((short)1);
        simpleModel.addElement((short)2);

        SimpleComboBox combobox = new SimpleComboBox(simpleModel);

        assertEquals(1, combobox.getSelectedInt());
        combobox.setSelectedValue((short)2);
        assertEquals(2, combobox.getSelectedInt());

        combobox.setSelectedValue((short)1);
        assertEquals(1, combobox.getSelectedInt());
    }

    @Test
    public void testSetSelectedValueWithByte() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        simpleModel.addElement((byte)1);
        simpleModel.addElement((byte)2);

        SimpleComboBox combobox = new SimpleComboBox(simpleModel);

        assertEquals(1, combobox.getSelectedInt());
        combobox.setSelectedValue((byte)2);
        assertEquals(2, combobox.getSelectedInt());

        combobox.setSelectedValue((byte)1);
        assertEquals(1, combobox.getSelectedInt());
    }

    @Test
    public void testSetSelectedValueWithObject() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        simpleModel.addElement("one");
        simpleModel.addElement("two");

        SimpleComboBox combobox = new SimpleComboBox(simpleModel);

        assertEquals("one", combobox.getSelectedItem());
        combobox.setSelectedValue("two");
        assertEquals("two", combobox.getSelectedItem());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox combobox = new SimpleComboBox(simpleModel);

        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        combobox.setModel(newModel);

        assertSame(newModel, combobox.getModel());
    }

    @Test
    public void testSetModelWithNullModel() {
        SimpleComboBox combobox = new SimpleComboBox();

        assertNull(combobox.getModel());
        combobox.setModel(null);
        assertNull(combobox.getModel());
    }

    @Test
    public void testSetModelWithWrongModelType() {
        SimpleComboBox combobox = new SimpleComboBox();
        DefaultComboBoxModel defaultModel = new DefaultComboBoxModel();

        combobox.setModel(defaultModel);
        assertThrows(IllegalArgumentException.class, () -> combobox.getSelectedInt());
    }
}