
import static org.junit.Assert.*;

import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxConstruction() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNotNull(simpleComboBox);
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);

        Object selectedValue = simpleComboBox.getSelectedValue();
        assertEquals(1, selectedValue);
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Apple");
        model.addElement("Banana");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedValue("Apple");
        assertEquals("Apple", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);

        int selectedInt = simpleComboBox.getSelectedInt();
        assertEquals(1, selectedInt);
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1L);
        model.addElement(2L);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);

        long selectedLong = simpleComboBox.getSelectedLong();
        assertEquals(1L, selectedLong);
    }

    @Test(expected = NullPointerException.class)
    public void testGetSelectedValueWithNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
        simpleComboBox.getSelectedValue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithStringValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Apple");
        model.addElement("Banana");
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedIndex(0);

        simpleComboBox.getSelectedInt();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetSelectedIntWithInvalidIndex() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(1);
        model.addElement(2);
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);

        simpleComboBox.setSelectedIndex(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithInvalidModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new JComboBoxModel());
    }

}
