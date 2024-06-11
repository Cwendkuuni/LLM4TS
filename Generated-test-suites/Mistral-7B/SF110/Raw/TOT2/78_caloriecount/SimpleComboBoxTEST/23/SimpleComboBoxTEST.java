import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleComboBoxTest {
    // ...
    
    @Test
    public void testGetSelectedValue() {
        SimpleComboBox comboBox = new SimpleComboBox(new SimpleComboBoxModel());
        Object selectedValue = comboBox.getSelectedValue();
        assertNull(selectedValue);

        comboBox.setModel(new SimpleComboBoxModel("Option 1", new Object[]{"Option 1", "Option 2"}));
        selectedValue = comboBox.getSelectedValue();
        assertEquals("Option 1", selectedValue);
    }
}