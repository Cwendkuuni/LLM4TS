package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import org.junit.*;
import static macaw.util.MacawMessages.*;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private Log log;
 private UserInterfaceFactory userInterfaceFactory;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 parentForm = new DisplayableListParentForm();
 log = new Log();
 userInterfaceFactory = new UserInterfaceFactory();
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 }

 @Test
 public void testDisplayableListPanelConstructionWithAllowWriteAccess() {
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testDisplayableListPanelConstructionWithAllowWriteAccessAndAllowRecordEditing() {
 assertNotNull(displayableListPanel);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 }

 @Test
 public void testInitMethod() {
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel Title", true, true);
 assertNotNull(displayableListPanel.displayableList);
 assertNotNull(displayableListPanel.select);
 assertNotNull(displayableListPanel.add);
 assertNotNull(displayableListPanel.edit);
 assertNotNull(displayableListPanel.view);
 assertNotNull(displayableListPanel.delete);
 }

 @Test
 public void testBuildUI() {
 displayableListPanel.buildUI();
 assertNotNull(displayableListPanel.panel);
 assertTrue(displayableListPanel.panel.getComponentCount() > 0);
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSetNewRecordFalse() {
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(new DisplayableListItemSelector() {
 @Override
 public void selectListItems(String listOwnerName, DisplayableList displayableList) {
 // Implementation here
 }
 });
 assertNotNull(displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(new DisplayableListItemAdder() {
 @Override
 public void addItems(String listOwnerName, DisplayableList displayableList) {
 // Implementation here
 }
 });
 assertNotNull(displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(new DisplayableListItemEditor() {
 @Override
 public void editListItem(String listOwnerName, Displayable listItem, DisplayableList displayableList) {
 // Implementation here
 }
 });
 assertNotNull(displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel.setDisplayableListItemViewer(new DisplayableListItemViewer() {
 @Override
 public void viewListItem(Displayable listItem) {
 // Implementation here
 }
 });
 assertNotNull(displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(new DisplayableListItemDeleter() {
 @Override
 public void deleteListItems(String listOwnerName, DisplayableList displayableList) {
 // Implementation here
 }
 });
 assertNotNull(displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.displayableList.setItems(new ArrayList<>());
 JButton addButton = displayableListPanel.add;
 addButton.doClick();
 // Implement addListItem method here
 assertTrue(displayableListPanel.displayableList.getAllItems().size() > 0);
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable selectedListItem = new Displayable();
 displayableListPanel.displayableList.setItems(new ArrayList<>());
 displayableListPanel.displayableList.add(selectedListItem);
 displayableListPanel.displayableList.setSelectedIndex(0);
 JButton editButton = displayableListPanel.edit;
 editButton.doClick();
 // Implement editSelectedListItem method here
 assertEquals("Old Display Name", selectedListItem.getDisplayName());
 // Implement code to change the display name here
 assertEquals("New Display Name", selectedListItem.getDisplayName());
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable selectedListItem = new Displayable();
 displayableListPanel.displayableList.setItems(new ArrayList<>());
 displayableListPanel.displayableList.add(selectedListItem);
 displayableListPanel.displayableList.setSelectedIndex(0);
 JButton viewButton = displayableListPanel.view;
 viewButton.doClick();
 // Implement viewSelectedListItem method here
 // Implement code to check that the DisplayableListItemViewer was shown here
 }

 @Test
 public void testDeleteSelectedListItems() {
 Displayable selectedListItem1 = new Displayable();
 Displayable selectedListItem2 = new Displayable();
 displayableListPanel.displayableList.setItems(new ArrayList<>());
 displayableListPanel.displayableList.add(selectedListItem1);
 displayableListPanel.displayableList.add(selectedListItem2);
 displayableListPanel.displayableList.setSelectedIndices(new int[]{0, 1});
 JButton deleteButton = displayableListPanel.delete;
 deleteButton.doClick();
 // Implement deleteSelectedListItems method here
 assertTrue(displayableListPanel.displayableList.getAllItems().size() == 0);
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.displayableList.setItems(new ArrayList<>());
 JButton selectButton = displayableListPanel.select;
 selectButton.doClick();
 // Implement selectListItems method here
 assertTrue(displayableListPanel.displayableList.itemsAreSelected());
 }

 @Test
 public void testUpdateButtonStates() {
 DisplayableList items = new DisplayableList();
 items.setItems(new ArrayList<>());
 displayableListPanel.displayableList = items;
 displayableListPanel.setDisplayableListItemSelector(new DisplayableListItemSelector() {
 @Override
 public void selectListItems(String listOwnerName, DisplayableList displayableList) {
 // Implementation here
 }
 });
 displayableListPanel.setDisplayableListItemAdder(new DisplayableListItemAdder() {
 @Override
 public void addItems(String listOwnerName, DisplayableList displayableList) {
 // Implementation here
 }
 });
 displayableListPanel.setDisplayableListItemEditor(new DisplayableListItemEditor() {
 @Override
 public void editListItem(String listOwnerName, Displayable listItem, DisplayableList displayableList) {
 // Implementation here
 }
 });
 displayableListPanel.setDisplayableListItemViewer(new DisplayableListItemViewer() {
 @Override
 public void viewListItem(Displayable listItem) {
 // Implementation here
 }
 });
 displayableListPanel.setDisplayableListItemDeleter(new DisplayableListItemDeleter() {
 @Override
 public void deleteListItems(String listOwnerName, DisplayableList displayableList) {
 // Implementation here
 }
 });
 displayableListPanel.setNewRecord(false);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel.setNewRecord(true);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel.displayableList.add(new Displayable());
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel.displayableList.clearSelection();
 displayableListPanel.displayableList.setSelectedIndex(0);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable());
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable());
 displayableListPanel.displayableList.setItems(masterList);
 displayableListPanel.save();
 assertEquals(masterList, displayableListPanel.masterList);
 }

 @Test
 public void testGetAllListItems() {
 displayableListPanel.displayableList.setItems(new ArrayList<>());
 displayableListPanel.displayableList.add(new Displayable());
 assertEquals(1, displayableListPanel.getAllListItems().size());
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("Test List Owner Name");
 assertEquals("Test List Owner Name", displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformed() {
 JButton addButton = displayableListPanel.add;
 addButton.doClick();
 // Implement addListItem method here
 JButton editButton = displayableListPanel.edit;
 editButton.doClick();
 // Implement editSelectedListItem method here
 JButton viewButton = displayableListPanel.view;
 viewButton.doClick();
 // Implement viewSelectedListItem method here
 JButton deleteButton = displayableListPanel.delete;
 deleteButton.doClick();
 // Implement deleteSelectedListItems method here
 JButton selectButton = displayableListPanel.select;
 selectButton.doClick();
 // Implement selectListItems method here
 }
}