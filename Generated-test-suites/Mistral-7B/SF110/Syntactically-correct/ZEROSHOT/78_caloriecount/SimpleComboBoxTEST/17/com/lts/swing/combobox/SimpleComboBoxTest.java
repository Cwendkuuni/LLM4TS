package com.lts.swing.combobox;

import static org.junit.Assert.*;

import org.junit.Test;

import java.awt.event.ItemEvent;
import java.util.Arrays;
import java.util.List;

public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;
    private SimpleComboBoxModel simpleComboBoxModel;

    @Test
    public void testSimpleComboBoxConstruction() {
        simpleComboBoxModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
        assertNotNull(simpleComboBox);
    }

    @Test
    public void testSetModel() {
        simpleComboBoxModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(simpleComboBoxModel);
        assertEquals(simpleComboBoxModel, simpleComboBox.getModel());
    }

    @Test(expected = IllegalStateException.class)
    public void testSetModelWithInvalidModel() {
        simpleComboBox.setModel(new DefaultComboBoxModel());
    }

    @Test
    public void testGetSelectedValue() {
        simpleComboBoxModel.addElement("One");
        simpleComboBoxModel.addElement("Two");
        simpleComboBox.setSelectedIndex(0);
        assertEquals("One", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWhenNothingIsSelected() {
        assertEquals(null, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        simpleComboBoxModel.addElement("One");
        simpleComboBoxModel.addElement("Two");
        simpleComboBox.setSelectedValue("One");
        assertEquals("One", simpleComboBox.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithInvalidValue() {
        simpleComboBoxModel.addElement("One");
        simpleComboBoxModel.addElement("Two");
        simpleComboBox.setSelectedValue("Three");
    }

    @Test
    public void testGetSelectedInt() {
        simpleComboBoxModel.addElement(1);
        simpleComboBoxModel.addElement(2);
        simpleComboBoxModel.addElement(3);
        simpleComboBox.setSelectedIndex(1);
        assertEquals(2, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWhenNothingIsSelected() {
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        simpleComboBoxModel.addElement(1L);
        simpleComboBoxModel.addElement(2L);
        simpleComboBoxModel.addElement(3L);
        simpleComboBox.setSelectedIndex(1);
        assertEquals(2L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWhenNothingIsSelected() {
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testSetSelectedValueByIndex() {
        simpleComboBoxModel.addElement("One");
        simpleComboBoxModel.addElement("Two");
        simpleComboBox.setSelectedIndex(1);
        assertEquals("Two", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueByValue() {
        simpleComboBoxModel.addElement("One");
        simpleComboBoxModel.addElement("Two");
        simpleComboBox.setSelectedValue("Two");
        assertEquals("Two", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueByObject() {
        simpleComboBoxModel.addElement("One");
        simpleComboBoxModel.addElement("Two");
        simpleComboBox.setSelectedValue(2);
        assertEquals("Two", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testFireItemEventWhenValueIsSelected() {
        simpleComboBoxModel.addElement("One");
        simpleComboBoxModel.addElement("Two");
        simpleComboBox.setSelectedValue("One");

        final Object[] expectedArguments = new Object[]{simpleComboBox, "One", 0, new ItemEvent(simpleComboBox, ItemEvent.SELECTED, 0)};
        simpleComboBox.addItemListener(l -> assertEquals(expectedArguments, l.getItemSelectEvent().getSource(), l.getItemSelectEvent().getItem(), l.getItemSelectEvent().getIndex(), l.getItemSelectEvent()));
        simpleComboBox.setSelectedValue("One");
    }

    @Test
    public void testFireItemEventWhenIndexIsSelected() {
        simpleComboBoxModel.addElement("One");
        simpleComboBoxModel.addElement("Two");
        simpleComboBox.setSelectedIndex(0);

        final Object[] expectedArguments = new Object[]{simpleComboBox, "One", 0, new ItemEvent(simpleComboBox, ItemEvent.SELECTED, 0)};
        simpleComboBox.addItemListener(l -> assertEquals(expectedArguments, l.getItemSelectEvent().getSource(), l.getItemSelectEvent().getItem(), l.getItemSelectEvent().getIndex(), l.getItemSelectEvent()));
        simpleComboBox.setSelectedIndex(0);
    }

    @Test
    public void testGetSelectedValueIsCompatibleWithIntegerType() {
        simpleComboBoxModel.addElement(1);
        simpleComboBoxModel.addElement(2);
        simpleComboBox.setSelectedIndex(1);
        assertEquals(2, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedValueIsCompatibleWithLongType() {
        simpleComboBoxModel.addElement(1L);
        simpleComboBoxModel.addElement(2L);
        simpleComboBox.setSelectedIndex(1);
        assertEquals(2L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedValueIsCompatibleWithShortType() {
        simpleComboBoxModel.addElement((short) 1);
        simpleComboBoxModel.addElement((short) 2);
        simpleComboBox.setSelectedIndex(1);
        assertEquals(2, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedValueIsCompatibleWithByteType() {
        simpleComboBoxModel.addElement((byte) 1);
        simpleComboBoxModel.addElement((byte) 2);
        simpleComboBox.setSelectedIndex(1);
        assertEquals(2, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedValueIsCompatibleWithStringType() {
        simpleComboBoxModel.addElement("One");
        simpleComboBoxModel.addElement("Two");
        simpleComboBox.setSelectedValue("Two");
        assertEquals("Two", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testToStringReturnsExpectedString() {
        simpleComboBoxModel.addElement("One");
        simpleComboBoxModel.addElement("Two");
        simpleComboBox.setSelectedValue("One");
        assertEquals("SimpleComboBox[model=SimpleComboBoxModel[selectedValue=One, items=[One, Two]], selectedIndex=0]", simpleComboBox.toString());
    }
}