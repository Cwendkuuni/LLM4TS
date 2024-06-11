import static org.junit.Assert.*;
import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

public class SimpleComboBoxTest {
    @Test
    public void testSimpleComboBoxConstructor() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertNotNull(simpleComboBox);
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        model.setSelectedItem("Test");
        assertEquals("Test", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        simpleComboBox.setSelectedValue("Test");
        assertEquals("Test", model.getSelectedItem());
    }

    @Test
    public void testGetSelectedInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        model.setSelectedItem(1);
        assertEquals(1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        model.setSelectedItem(1L);
        assertEquals(1L, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetModelWithNullModel() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setModel(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithNullValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSelectedValueWithInvalidDataType() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        simpleComboBox.setSelectedValue(new Object()); //Invalid data type
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWhenNoSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNull(simpleComboBox.getModel());
        assertNull(simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWhenNoSelectedValue() {
        SimpleComboBox simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
        assertNull(simpleComboBox.getModel());
        assertNull(simpleComboBox.getSelectedLong());
    }

    @Test
    public void testEmptySimpleComboBoxModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox simpleComboBox = new SimpleComboBox(model);
        assertNull(simpleComboBox.getSelectedValue());
        assertNull(simpleComboBox.getSelectedInt());
        assertNull(simpleComboBox.getSelectedLong());
    }
}