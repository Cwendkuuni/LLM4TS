import static org.junit.Assert.*;

import org.junit.Test;

public class SimpleComboBoxTest {

    @Test
    public void testSimpleComboBoxConstruction() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNotNull(simpleComboBox);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSimpleComboBoxConstructionWithNullModel() {
        new SimpleComboBox(null);
    }

    @Test
    public void testSetModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setModel(new SimpleComboBoxModel());
        assertNotNull(simpleComboBox.getModel());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithWrongModelType() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new JComboBoxModel());
        simpleComboBox.setModel(new JComboBoxModel());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue("Test Value");
        assertEquals("Test Value", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(5);
        assertEquals(5, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWhenNoValueSelected() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithWrongValueType() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue("Test Value");
        simpleComboBox.setSelectedInt(10); // Should fail
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(10L);
        assertEquals(10L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWhenNoValueSelected() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithWrongValueType() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue("Test Value");
        simpleComboBox.setSelectedLong(10); // Should fail
    }

    @Test
    public void testNullArgumentConstructors() {
        new SimpleComboBox(null); // Should fail
        new SimpleComboBoxModel(); // Should not fail
    }

    @Test
    public void testNullArgumentSetModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setModel(null); // Should fail
    }
}