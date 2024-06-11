package com.lts.swing.combobox;

import org.junit.*;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {
    SimpleComboBox simpleComboBox;
    SimpleComboBoxModel simpleComboBoxModel;

    @Before
    public void setUp() {
        simpleComboBoxModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
    }

    @Test
    public void testGetSelectedValue() {
        simpleComboBox.setSelectedValue("test");
        assertEquals("test", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        simpleComboBox.setSelectedValue("test");
        assertEquals("test", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueEdgeCases() {
        simpleComboBox.setModel(null);
        assertNull(simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue(null);
        assertNull(simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue("not a number");
        assertNull(simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue(new Object());
        assertNotNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValueEdgeCases() {
        simpleComboBox.setModel(null);
        simpleComboBox.setSelectedValue("test");
        // It should not throw an error as the setModel method checks if the model is an instance of SimpleComboBoxModel

        simpleComboBox.setSelectedValue(null);
        assertEquals(null, simpleComboBox.getSelectedValue());

        simpleComboBox.setSelectedValue("not a number");
        assertEquals(null, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        simpleComboBox.setSelectedValue(10);
        assertEquals(10, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntEdgeCases() {
        simpleComboBox.setModel(null);
        assertNull(simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue(null);
        assertNull(simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue("test");
        assertNull(simpleComboBox.getSelectedInt());

        simpleComboBox.setSelectedValue(new Object());
        assertNull(simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        simpleComboBox.setSelectedValue(10L);
        assertEquals(10L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongEdgeCases() {
        simpleComboBox.setModel(null);
        assertNull(simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedValue(null);
        assertNull(simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedValue("test");
        assertNull(simpleComboBox.getSelectedLong());

        simpleComboBox.setSelectedValue(new Object());
        assertNull(simpleComboBox.getSelectedLong());
    }

    @After
    public void tearDown() {
        // Teardown code if needed
    }
}