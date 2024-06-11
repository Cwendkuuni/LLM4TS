package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxCreation() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel());
        assertSame(DefaultComboBoxModel.class, simpleComboBox.getModel().getClass());
    }

    @Test
    public void testGetSelectedValueNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueIntegerValue() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
        simpleModel.addElement(1);
        simpleModel.addElement(2);
        simpleModel.addElement(3);
        simpleComboBox.setSelectedIndex(1);

        assertEquals(Integer.valueOf(1), simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueLongValue() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
        simpleModel.addElement(1L);
        simpleModel.addElement(2L);
        simpleModel.addElement(3L);
        simpleComboBox.setSelectedIndex(1);

        assertEquals(Long.valueOf(1), simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueShortValue() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
        simpleModel.addElement((short) 1);
        simpleModel.addElement((short) 2);
        simpleModel.addElement((short) 3);
        simpleComboBox.setSelectedIndex(1);

        assertEquals(Short.valueOf((short) 1), simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueByteValue() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
        simpleModel.addElement((byte) 1);
        simpleModel.addElement((byte) 2);
        simpleModel.addElement((byte) 3);
        simpleComboBox.setSelectedIndex(1);

        assertEquals(Byte.valueOf((byte) 1), simpleComboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedValueUnsupportedType() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
        simpleModel.addElement("foo");
        simpleModel.addElement("bar");
        simpleModel.addElement("baz");
        simpleComboBox.setSelectedIndex(1);

        simpleComboBox.getSelectedValue();
    }

    @Test
    public void testGetSelectedIntNullValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel());
        assertSame(Integer.class, simpleComboBox.getClass());
        assertSame(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntIntegerValue() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
        simpleModel.addElement(1);
        simpleModel.addElement(2);
        simpleModel.addElement(3);
        simpleComboBox.setSelectedIndex(1);

        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntLongValue() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
        simpleModel.addElement(1L);
        simpleModel.addElement(2L);
        simpleModel.addElement(3L);
        simpleComboBox.setSelectedIndex(1);

        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntShortValue() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
        simpleModel.addElement((short) 1);
        simpleModel.addElement((short) 2);
        simpleModel.addElement((short) 3);
        simpleComboBox.setSelectedIndex(1);

        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntByteValue() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
        simpleModel.addElement((byte) 1);
        simpleModel.addElement((byte) 2);
        simpleModel.addElement((byte) 3);
        simpleComboBox.setSelectedIndex(1);

        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntUnsupportedType() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel());
        simpleComboBox.setSelectedValue("foo");
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testSetSelectedValueIntegerValue() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
        simpleModel.addElement(1);
        simpleModel.addElement(2);
        simpleModel.addElement(3);

        simpleComboBox.setSelectedValue(Integer.valueOf(1));
        assertEquals(1, simpleComboBox.getSelectedIndex());
    }

    @Test
    public void testSetSelectedValueLongValue() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
        simpleModel.addElement(1L);
        simpleModel.addElement(2L);
        simpleModel.addElement(3L);

        simpleComboBox.setSelectedValue(Long.valueOf(1));
        assertEquals(1, simpleComboBox.getSelectedIndex());
    }

    @Test
    public void testSetSelectedValueShortValue() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
        simpleModel.addElement((short) 1);
        simpleModel.addElement((short) 2);
        simpleModel.addElement((short) 3);

        simpleComboBox.setSelectedValue(Short.valueOf((short) 1));
        assertEquals(1, simpleComboBox.getSelectedIndex());
    }

    @Test
    public void testSetSelectedValueByteValue() {
        SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(simpleModel);
        simpleModel.addElement((byte) 1);
        simpleModel.addElement((byte) 2);
        simpleModel.addElement((byte) 3);

        simpleComboBox.setSelectedValue(Byte.valueOf((byte) 1));
        assertEquals(1, simpleComboBox.getSelectedIndex());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueUnsupportedType() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new DefaultComboBoxModel());
        simpleComboBox.setSelectedValue("foo");
    }
}