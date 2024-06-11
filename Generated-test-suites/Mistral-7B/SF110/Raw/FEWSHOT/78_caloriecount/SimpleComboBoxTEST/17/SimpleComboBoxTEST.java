package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxCreation() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNotNull(simpleComboBox);
        assertSame(SimpleComboBoxModel.class, simpleComboBox.getModel().getClass());
    }

    @Test
    public void testSetModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        ComboBoxModel anotherModel = new DefaultComboBoxModel<>();
        simpleComboBox.setModel(anotherModel);
        assertSame(anotherModel, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        Object selectedValue = new Integer(1);
        simpleComboBox.setSelectedValue(selectedValue);
        assertEquals(selectedValue, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueWhenNoSelection() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        Object selectedValue = new Integer(1);
        simpleComboBox.setSelectedValue(selectedValue);
        assertEquals(selectedValue, simpleComboBox.getSelectedModel().getSelectedItem());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedItem("Two");
        simpleComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"One", "Two"}));
        assertEquals(-1, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedItem("One");
        assertEquals(0, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedItem("Two");
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedItem("ThreeL");
        simpleComboBox.setModel(new DefaultComboBoxModel<>(new Object[]{"One", "Three"}));
        assertEquals(-1L, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedItem("One");
        assertEquals(0L, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedItem("Three");
        assertEquals(1L, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedItem("ThreeL");
        assertEquals(1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedIntWhenNoSelection() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLongWhenNoSelection() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testIllegalArgumentExceptionWhenWrongTypeIsSelected() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"One", "Two"}));
        simpleComboBox.setSelectedItem("ThreeF");
        assertThrows(IllegalArgumentException.class, () -> simpleComboBox.getSelectedInt());
    }

    @Test
    public void testIllegalArgumentExceptionWhenWrongTypeIsSelectedLong() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setModel(new DefaultComboBoxModel<>(new String[]{"One", "Two"}));
        simpleComboBox.setSelectedItem("ThreeF");
        assertThrows(IllegalArgumentException.class, () -> simpleComboBox.getSelectedLong());
    }

    @Test
    public void testComboBoxDisplay() {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String[] items = {"One", "Two", "Three"};
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel(items));
        simpleComboBox.setBounds(10, 10, 100, 30);

        frame.add(simpleComboBox);
        frame.pack();
        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> simpleComboBox.setSelectedItem("Two"));

        assertEquals("Two", simpleComboBox.getSelectedItem());
        assertEquals("Two", simpleComboBox.getModel().getSelectedItem());

        frame.dispose();
    }
}