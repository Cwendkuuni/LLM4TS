import org.junit.Test;
import static org.junit.Assert.*;
import ipac.WhoIS;

public class WhoISTest {

 private WhoIS whois;

 @Before
 public void setUp() {
 whois = new WhoIS();
 }

 @Test
 public void testQueryPanel() {
 // Test queryTextField creation and focus
 WhoIS.JTextField queryTextField = whois.queryPanel().queryTextField;
 assertNotNull(queryTextField);
 queryTextField.requestFocus();

 // Test queryButton action listener
 WhoIS.JButton queryButton = whois.queryPanel().queryButton;
 queryButton.doClick();
 whois.assertQueryCalled();

 // Test resetButton action listener
 WhoIS.JButton resetButton = whois.queryPanel().resetButton;
 resetButton.doClick();
 whois.assertQueryNotCalled();
 }

 @Test
 public void testAdditionalQueriesPanel() {
 // Test IPAddressLookups creation and selection of radio buttons
 WhoIS.JPanel additionalQueriesPanel = whois.queryPanel().additionalQueriesPanel;
 WhoIS.JRadioButton lowercaseL = additionalQueriesPanel.IPAddressLookups().lowercaseL;
 WhoIS.JRadioButton uppercaseL = additionalQueriesPanel.IPAddressLookups().uppercaseL;
 WhoIS.JRadioButton resetRadioButton = additionalQueriesPanel.IPAddressLookups().resetRadioButton;

 lowercaseL.doClick();
 assertTrue(lowercaseL.isSelected());
 assertFalse(uppercaseL.isSelected());
 assertFalse(resetRadioButton.isSelected());

 uppercaseL.doClick();
 assertFalse(lowercaseL.isSelected());
 assertTrue(uppercaseL.isSelected());
 assertFalse(resetRadioButton.isSelected());

 resetRadioButton.doClick();
 assertFalse(lowercaseL.isSelected());
 assertFalse(uppercaseL.isSelected());
 assertTrue(resetRadioButton.isSelected());
 }

 @Test
 public void testMiscellaneousQueries() {
 // Test inverseQueries creation and selection
 WhoIS.JPanel additionalQueriesPanel = whois.queryPanel().additionalQueriesPanel;
 WhoIS.JComboBox inverseQueries = additionalQueriesPanel.MiscellaneousQueries().inverseQueries;

 inverseQueries.setSelectedIndex(1); // admin-c
 assertEquals("admin-c", inverseQueries.getSelectedItem());

 inverseQueries.setSelectedIndex(3); // tech-c,zone-c
 String[] selectedValues = inverseQueries.getSelectedValues();
 assertEquals(2, selectedValues.length);
 assertEquals("tech-c", selectedValues[0]);
 assertEquals("zone-c", selectedValues[1]);
 }

 @Test
 public void testQueryHintsPanel() {
 // Test queryHintsPanel creation
 WhoIS.JPanel queryHintsPanel = whois.queryPanel().queryHintsPanel;

 // Test queryHintsText1 existence
 assertNotNull(queryHintsPanel.getComponent(0));

 // Test queryHintsText2 existence
 assertNotNull(queryHintsPanel.getComponent(1));

 // Test queryHintsText3 existence
 assertNotNull(queryHintsPanel.getComponent(2));

 // Test queryHintsText4 existence
 assertNotNull(queryHintsPanel.getComponent(3));

 // Test queryHintsText5 existence
 assertNotNull(queryHintsPanel.getComponent(4));
 }

 @Test
 public void testCreatePopupMenu() {
 // Test cut, copy, paste, and selectAll JMenuItem creation
 WhoIS.JPopupMenu popupMenu = whois.queryPanel().queryTextField.getPopupMenu();

 WhoIS.JMenuItem cut = popupMenu.getComponent(0);
 WhoIS.JMenuItem copy = popupMenu.getComponent(1);
 WhoIS.JMenuItem paste = popupMenu.getComponent(2);
 WhoIS.JMenuItem selectAll = popupMenu.getComponent(3);

 // Test action listener for cut, copy, and paste
 WhoIS.JTextField queryTextField = whois.queryPanel().queryTextField;
 String text = "test query text";
 queryTextField.setText(text);

 cut.doClick();
 assertEquals("", queryTextField.getText());

 copy.doClick();
 String copiedText = "";
 copy(queryTextField, null); // Use clipboardUtil to get copied text
 copiedText = clipboardUtil.getText();
 assertEquals(text, copiedText);

 paste.doClick();
 assertEquals(text, queryTextField.getText());

 // Test action listener for selectAll
 selectAll.doClick();
 assertEquals(text, queryTextField.getText());
 }

 @After
 public void tearDown() {
 whois = null;
 }
}