package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import org.junit.*;
import macaw.system.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private Log log;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableList displayableList;
 private JButton select, add, edit, view, delete;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 log = new Log();
 userInterfaceFactory = new UserInterfaceFactory();
 parentForm = new DisplayableListParentForm();
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableList = new DisplayableList(userInterfaceFactory);
 select = displayableListPanel.select;
 add = displayableListPanel.add;
 edit = displayableListPanel.edit;
 view = displayableListPanel.view;
 delete = displayableListPanel.delete;
 }

 @Test
 public void testConstructorWithAllowRecordEditingFalse() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 assertNull(displayableListPanel.listItemEditor);
 assertNull(displayableListPanel.listItemViewer);
 }

 @Test
 public void testConstructorWithAllowRecordEditingTrue() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertNotNull(displayableListPanel.listItemEditor);
 assertNotNull(displayableListPanel.listItemViewer);
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
 public void testGetPanel() {
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector mockSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(mockSelector);
 assertEquals(mockSelector, displayableListPanel.listItemSelector);
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithMissingAdder() {
 displayableListPanel.addListItem();
 fail("AddListItem should thrown MacawException when listItemAdder is null");
 }

 @Test
 public void testEditSelectedListItem() {
 displayableList.add(new Displayable("Test1", "Test1 DisplayName"));
 displayableList.add(new Displayable("Test2", "Test2 DisplayName"));
 displayableListPanel.setDisplayableListItemEditor(mock(DisplayableListItemEditor.class));
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.editSelectedListItem();
 assertEquals("Test2 Updated DisplayName", displayableList.get(1).getDisplayName());
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithMissingEditor() {
 displayableList.add(new Displayable("Test1", "Test1 DisplayName"));
 displayableList.add(new Displayable("Test2", "Test2 DisplayName"));
 displayableListPanel.editSelectedListItem();
 fail("EditSelectedListItem should thrown MacawException when listItemEditor is null");
 }

 @Test
 public void testViewSelectedListItem() {
 displayableList.add(new Displayable("Test1", "Test1 DisplayName"));
 displayableList.add(new Displayable("Test2", "Test2 DisplayName"));
 displayableListPanel.setDisplayableListItemViewer(mock(DisplayableListItemViewer.class));
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.viewSelectedListItem();
 verify(displayableListItemViewer).viewListItem(displayableList.get(0));
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithMissingViewer() {
 displayableList.add(new Displayable("Test1", "Test1 DisplayName"));
 displayableList.add(new Displayable("Test2", "Test2 DisplayName"));
 displayableListPanel.viewSelectedListItem();
 fail("ViewSelectedListItem should thrown MacawException when listItemViewer is null");
 }

 @Test
 public void testDeleteSelectedListItems() {
 displayableList.add(new Displayable("Test1", "Test1 DisplayName"));
 displayableList.add(new Displayable("Test2", "Test2 DisplayName"));
 displayableListPanel.setDisplayableListItemDeleter(mock(DisplayableListItemDeleter.class));
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.deleteSelectedListItems();
 verify(displayableListItemDeleter).deleteListItems(displayableList.getName(), displayableList);
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithEmptyList() {
 displayableListPanel.deleteSelectedListItems();
 fail("DeleteSelectedListItems should thrown MacawException when displayableList is empty");
 }

 @Test
 public void testSelectListItems() {
 displayableList.add(new Displayable("Test1", "Test1 DisplayName"));
 displayableList.add(new Displayable("Test2", "Test2 DisplayName"));
 displayableListPanel.setDisplayableListItemSelector(mock(DisplayableListItemSelector.class));
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.selectListItems();
 verify(displayableListItemSelector).selectListItems(displayableList.getName(), displayableList);
 }

 @Test(expected = MacawException.class)
 public void testSelectListItemsWithEmptyList() {
 displayableListPanel.selectListItems();
 fail("SelectListItems should thrown MacawException when displayableList is empty");
 }

 @Test
 public void testUpdateButtonStates() {
 JFrame frame = new JFrame();
 JPanel panel = new JPanel();
 frame.getContentPane().add(panel);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.updateButtonStates();
 assertFalse(select.isEnabled());
 assertFalse(add.isEnabled());
 assertFalse(edit.isEnabled());
 assertFalse(view.isEnabled());
 assertFalse(delete.isEnabled());
 displayableList.add(new Displayable("Test1", "Test1 DisplayName"));
 displayableListPanel.updateButtonStates();
 assertTrue(select.isEnabled());
 assertTrue(add.isEnabled());
 assertTrue(edit.isEnabled());
 assertTrue(view.isEnabled());
 assertTrue(delete.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable("Test1", "Test1 DisplayName"));
 masterList.add(new Displayable("Test2", "Test2 DisplayName"));
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable("Test1", "Test1 DisplayName"));
 masterList.add(new Displayable("Test2", "Test2 DisplayName"));
 displayableListPanel.setDisplayableListItemSelector(mock(DisplayableListItemSelector.class));
 displayableListPanel.setDisplayableList(displayableList);
 displayableListPanel.save();
 assertEquals(masterList, displayableListPanel.masterList);
 }

 @Test
 public void testGetAllListItems() {
 ArrayList<Displayable> allItems = new ArrayList<>();
 allItems.add(new Displayable("Test1", "Test1 DisplayName"));
 allItems.add(new Displayable("Test2", "Test2 DisplayName"));
 displayableListPanel.displayableList.setDisplayItems(allItems);
 assertEquals(allItems, displayableListPanel.getAllListItems());
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("Test Owner Name");
 assertEquals("Test Owner Name", displayableListPanel.listOwnerName);
 }

 @Test(expected = MacawException.class)
 public void testActionPerformedWithMissingButton() {
 ActionEvent event = new ActionEvent(new Object(), 0, "Test Event");
 displayableListPanel.actionPerformed(event);
 fail("ActionPerformed should thrown MacawException when button is null");
 }

 @Test
 public void testActionPerformedWithSelectButton() {
 ActionEvent event = new ActionEvent(select, 0, "Test Event");
 displayableListPanel.actionPerformed(event);
 displayableListPanel.selectListItems();
 }

 @Test
 public void testActionPerformedWithAddButton() {
 ActionEvent event = new ActionEvent(add, 0, "Test Event");
 displayableListPanel.actionPerformed(event);
 displayableListPanel.addListItem();
 }

 @Test
 public void testActionPerformedWithEditButton() {
 ActionEvent event = new ActionEvent(edit, 0, "Test Event");
 displayableListPanel.actionPerformed(event);
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testActionPerformedWithViewButton() {
 ActionEvent event = new ActionEvent(view, 0, "Test Event");
 displayableListPanel.actionPerformed(event);
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testActionPerformedWithDeleteButton() {
 ActionEvent event = new ActionEvent(delete, 0, "Test Event");
 displayableListPanel.actionPerformed(event);
 displayableListPanel.deleteSelectedListItems();
 }
}