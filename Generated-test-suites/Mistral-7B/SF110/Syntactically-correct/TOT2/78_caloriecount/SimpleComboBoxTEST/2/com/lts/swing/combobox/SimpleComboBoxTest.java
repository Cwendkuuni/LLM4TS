import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testGetSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel<String>() {
            @Override
            public Object getElementAt(int index) {
                return String.valueOf(index);
            }

            @Override
            public int getSize() {
                return 5;
            }

            @Override
            public Object getSelectedItem() {
                return String.valueOf(2);
            }
        });

        simpleComboBox.setSelectedIndex(2);
        Object selectedValue = simpleComboBox.getSelectedValue();
        assertEquals("2", selectedValue.toString());

        simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel<String>() {
            @Override
            public Object getElementAt(int index) {
                return String.valueOf(index);
            }

            @Override
            public int getSize() {
                return 5;
            }
        });

        assertNull(simpleComboBox.getSelectedValue());
    }
}