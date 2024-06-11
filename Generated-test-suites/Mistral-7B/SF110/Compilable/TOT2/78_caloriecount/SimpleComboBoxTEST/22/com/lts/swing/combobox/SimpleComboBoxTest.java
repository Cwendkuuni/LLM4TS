package com.lts.swing.combobox;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SimpleComboBoxTest {
    // Test case for getSelectedValue()
    @Test
    public void testGetSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedItem("Test Value");
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertEquals("Test Value", selectedValue);
    }
}