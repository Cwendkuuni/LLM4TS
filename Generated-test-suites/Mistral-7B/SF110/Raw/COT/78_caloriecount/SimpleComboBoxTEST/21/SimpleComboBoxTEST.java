package com.lts.swing.combobox.test;

import static org.junit.Assert.*;

import java.awt.event.ItemEvent;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

/**
 * SimpleComboBox Test Cases
 *
 */
public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;
    private JFrame frame;

    @Before
    public void setUp() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        frame.add(simpleComboBox);
        frame.pack();
        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    @After
    public void tearDown() {
        SwingUtilities.invokeLater(() -> {
            if (null != simpleComboBox) {
                simpleComboBox.dispose();
            }
            if (null != frame) {
                frame.dispose();
            }
        });
    }

    // Test case for constructor
    @Test
    public void testSimpleComboBoxConstruction() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNotNull(simpleComboBox);
    }

    // Test case for setModel method
    @Test
    public void testSetModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNull(simpleComboBox.mySimpleModel);
        ComboBoxModel newModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(newModel);
        assertNotNull(simpleComboBox.mySimpleModel);
        assertEquals(simpleComboBox.mySimpleModel, newModel);
    }

    // Test case for getSelectedValue method
    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Option1");
        model.addElement("Option2");
        simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);

        Object selectedValue = simpleComboBox.getSelectedValue();
        assertEquals("Option1", selectedValue);

        simpleComboBox.setSelectedIndex(1);
        selectedValue = simpleComboBox.getSelectedValue();
        assertEquals("Option2", selectedValue);

        simpleComboBox.setSelectedIndex(-1);
        selectedValue = simpleComboBox.getSelectedValue();
        assertNull(selectedValue);
    }

    // Test case for setSelectedValue method
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Option1");
        model.addElement("Option2");
        simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedValue("Option1");
        assertEquals("Option1", simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue("Option2");
        assertEquals("Option2", simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue("InvalidOption");
        assertEquals("Option2", simpleComboBox.getSelectedValue()); // No exception is thrown

        simpleComboBox.setSelectedValue(null);
        assertEquals("Option2", simpleComboBox.getSelectedValue()); // No exception is thrown
    }

    // Test case for getSelectedInt method
    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Integer(1));
        model.addElement(new Integer(2));
        simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedIndex(1);
        assertEquals(2, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedIndex(-1);
        assertEquals(-1, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue(new Long(3));
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue(new Short((short) 4));
        simpleComboBox.setSelectedIndex(1);
        assertEquals(2, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue(new Byte((byte) 5));
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue("InvalidOption");
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt()); // No exception is thrown

        simpleComboBox.setSelectedValue(null);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt()); // No exception is thrown
    }

    // Test case for getSelectedLong method
    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Integer(1));
        model.addElement(new Integer(2));
        simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedIndex(1);
        assertEquals(2, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedIndex(-1);
        assertEquals(-1, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedValue(new Long(3));
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedValue(new Short((short) 4));
        simpleComboBox.setSelectedIndex(1);
        assertEquals(2, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedValue(new Byte((byte) 5));
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedValue("InvalidOption");
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedLong()); // No exception is thrown

        simpleComboBox.setSelectedValue(null);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedLong()); // No exception is thrown
    }

    // Test case for setSelectedValue with invalid data types
    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithInvalidDataTypes() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Integer(1));
        model.addElement(new Integer(2));
        simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedValue("InvalidString");
    }

    // Test case for getSelectedInt with null model
    @Test(expected = NullPointerException.class)
    public void testGetSelectedIntWithNullModel() {
        simpleComboBox = new SimpleComboBox(null);
        simpleComboBox.setSelectedIndex(0);
        simpleComboBox.getSelectedInt();
    }

    // Test case for getSelectedLong with null model
    @Test(expected = NullPointerException.class)
    public void testGetSelectedLongWithNullModel() {
        simpleComboBox = new SimpleComboBox(null);
        simpleComboBox.setSelectedIndex(0);
        simpleComboBox.getSelectedLong();
    }

    // Test case for setSelectedValue with null value
    @Test
    public void testSetSelectedValueWithNullValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Integer(1));
        model.addElement(new Integer(2));
        simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedValue(null);
        simpleComboBox.setSelectedIndex(0);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    // Test case for getSelectedInt with unsupported data types
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithUnsupportedDataType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Float(1.5f));
        model.addElement(new Float(2.5f));
        simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedIndex(0);
        simpleComboBox.getSelectedInt();
    }

    // Test case for getSelectedLong with unsupported data types
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithUnsupportedDataType() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Float(1.5f));
        model.addElement(new Float(2.5f));
        simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedIndex(0);
        simpleComboBox.getSelectedLong();
    }

    // Test case for setSelectedInt with invalid index
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetSelectedIntWithInvalidIndex() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Integer(1));
        model.addElement(new Integer(2));
        simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedIndex(3);
    }

    // Test case for setSelectedInt with invalid value
    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedIntWithInvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Integer(1));
        model.addElement(new Integer(2));
        simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedValue(new Float(3.5f));
    }

    // Test case for setSelectedLong with invalid index
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetSelectedLongWithInvalidIndex() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Integer(1));
        model.addElement(new Integer(2));
        simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedIndex(3);
    }

    // Test case for setSelectedLong with invalid value
    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedLongWithInvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Integer(1));
        model.addElement(new Integer(2));
        simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedValue(new Float(3.5f));
    }

    // Test case for setSelectedValue with fireEvent parameter set to false
    @Test
    public void testSetSelectedValueWithFireEventFalse() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Integer(1));
        model.addElement(new Integer(2));
        simpleComboBox = new SimpleComboBox(model);

        int initialSize = simpleComboBox.getItemCount();

        simpleComboBox.setSelectedValue("Option3");
        simpleComboBox.fireEvent(new ItemEvent(simpleComboBox, ItemEvent.ITEM_STATE_CHANGED, 0, 0, "Option3", -1));

        assertEquals(initialSize, simpleComboBox.getItemCount());
    }

    // Test case for setSelectedValue with fireEvent parameter set to true
    @Test
    public void testSetSelectedValueWithFireEventTrue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(new Integer(1));
        model.addElement(new Integer(2));
        simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedValue("Option3");
        simpleComboBox.fireEvent(new ItemEvent(simpleComboBox, ItemEvent.ITEM_STATE_CHANGED, -1, 0, "Option3", -1));

        assertEquals(2, simpleComboBox.getItemCount());
    }
}