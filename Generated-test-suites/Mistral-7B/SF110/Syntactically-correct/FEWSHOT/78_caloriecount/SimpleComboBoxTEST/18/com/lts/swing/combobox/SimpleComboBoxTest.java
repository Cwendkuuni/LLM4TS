package com.lts.swing.combobox;

import static org.junit.Assert.*;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;
    private SimpleComboBoxModel simpleComboBoxModel;

    @Before
    public void setUp() {
        simpleComboBoxModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
    }

    @Test
    public void testSimpleComboBoxConstructor() {
        assertNotNull(simpleComboBox);
        assertEquals(simpleComboBoxModel, simpleComboBox.getModel());
    }

    @Test
    public void testSetModel() {
        ComboBoxModel anotherModel = new DefaultComboBoxModel();
        simpleComboBox.setModel(anotherModel);
        assertEquals(anotherModel, simpleComboBox.getModel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithInvalidModel() {
        simpleComboBox.setModel(null);
    }

    @Test
    public void testGetSelectedValue() {
        simpleComboBoxModel.addElement("Item1");
        simpleComboBoxModel.addElement("Item2");
        simpleComboBoxModel.setSelectedItem("Item1");

        Object selectedValue = simpleComboBox.getSelectedValue();
        assertEquals("Item1", selectedValue);

        simpleComboBoxModel.setSelectedItem("Item2");
        selectedValue = simpleComboBox.getSelectedValue();
        assertEquals("Item2", selectedValue);

        simpleComboBoxModel.setSelectedIndex(-1);
        selectedValue = simpleComboBox.getSelectedValue();
        assertNull(selectedValue);
    }

    @Test
    public void testSetSelectedValue() {
        simpleComboBoxModel.addElement("Item1");
        simpleComboBoxModel.addElement("Item2");

        simpleComboBox.setSelectedValue("Item1");
        assertEquals("Item1", simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue("Item2");
        assertEquals("Item2", simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue("Item3");
        assertEquals("Item2", simpleComboBox.getSelectedValue()); // since no "Item3" in the model
    }

    @Test
    public void testGetSelectedInt() {
        simpleComboBoxModel.addElement("Item1");
        simpleComboBoxModel.addElement("Item2");
        simpleComboBoxModel.setSelectedItem("Item1");

        int selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(-1, selectedInt);

        simpleComboBoxModel.setSelectedItem("Item1");
        selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(0, selectedInt);

        simpleComboBoxModel.setSelectedItem("Item2");
        selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(1, selectedInt);
    }

    @Test
    public void testGetSelectedLong() {
        simpleComboBoxModel.addElement(1L);
        simpleComboBoxModel.addElement(2L);
        simpleComboBoxModel.setSelectedItem(1L);

        long selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(-1L, selectedLong);

        simpleComboBoxModel.setSelectedItem(1L);
        selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(1L, selectedLong);

        simpleComboBoxModel.setSelectedItem(2L);
        selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(2L, selectedLong);
    }

    @Test
    public void testComboBoxRenderer() {
        simpleComboBoxModel.addElement("Item1");
        simpleComboBoxModel.addElement("Item2");
        simpleComboBox.setRenderer(new DefaultListCellRenderer());

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.add(simpleComboBox, 10, 10);
        frame.setVisible(true);

        SwingUtilities.invokeLater(() -> simpleComboBox.setSelectedIndex(1));

        assertEquals("Item2", simpleComboBox.getItemAt(1).toString());
    }

    @After
    public void tearDown() {
        SwingUtilities.invokeLater(() -> {
            if (simpleComboBox != null) {
                simpleComboBox.dispose();
            }
        });
    }
}