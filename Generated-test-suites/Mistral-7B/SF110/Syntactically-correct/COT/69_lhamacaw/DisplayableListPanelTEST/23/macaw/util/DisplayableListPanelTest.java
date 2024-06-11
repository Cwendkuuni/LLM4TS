package macaw.util.test;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.system.*;
import macaw.util.*;
import org.junit.Before;
import org.junit.Test;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;

 @Before
 public void setup() {
 sessionProperties = new SessionProperties();
 log = MockSessionLogger.getLogger();
 userInterfaceFactory = MockUserInterfaceFactory.getFactory();
 parentForm = MockDisplayableListParentForm.create();
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true);
 }

 @Test
 public void testDisplayableListPanelConstructor() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowRecordEditing() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true, true);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testInitMethod() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true, true);
 assertEquals("Displayable List Panel", displayableListPanel.getPanelTitleText());
 assertNotNull(displayableListPanel.getPanel());
 assertNotNull(displayableListPanel.select);
 assertNotNull(displayableListPanel.add);
 assertNotNull(displayableListPanel.edit);
 assertNotNull(displayableListPanel.view);
 assertNotNull(displayableListPanel.delete);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithoutListItemAdder() {
 displayableListPanel.addListItem();
 fail("Exception should be thrown when listItemAdder is null");
 }

 @Test
 public void testEditSelectedListItemWithoutListItemEditor() {
 displayableListPanel.editSelectedListItem();
 fail("Exception should be thrown when listItemEditor is null");
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithoutListItemViewer() {
 displayableListPanel.viewSelectedListItem();
 fail("Exception should be thrown when listItemViewer is null");
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithoutListItemDeleter() {
 displayableListPanel.deleteSelectedListItems();
 fail("Exception should be thrown when listItemDeleter is null");
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true, true);
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 displayableListPanel.displayableList.add(new Displayable());
 assertTrue(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 displayableListPanel.allowRecordEditing = true;
 assertTrue(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true, true);
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable());
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSaveMethod() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Displayable List Panel", true, true);
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable());
 displayableListPanel.setDisplayableItems(masterList);
 displayableListPanel.save();
 assertEquals(masterList, displayableListPanel.masterList);
 }
}