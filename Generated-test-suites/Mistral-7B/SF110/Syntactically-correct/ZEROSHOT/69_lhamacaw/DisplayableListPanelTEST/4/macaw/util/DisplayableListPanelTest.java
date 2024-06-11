package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;
 private boolean allowWriteAccess;
 private boolean allowRecordEditing;

 @Before
 public void setUp() {
 sessionProperties = Mockito.mock(SessionProperties.class);
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 log = Mockito.mock(Log.class);
 allowWriteAccess = true;
 allowRecordEditing = true;
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", allowWriteAccess, allowRecordEditing);
 }

 @Test
 public void testDisplayableListPanel() {
 assertNotNull(displayableListPanel);
 assertNotNull(displayableListPanel.sessionProperties);
 assertNotNull(displayableListPanel.parentForm);
 assertNotNull(displayableListPanel.userInterfaceFactory);
 assertNotNull(displayableListPanel.log);
 assertNotNull(displayableListPanel.panelTitleText);
 assertNull(displayableListPanel.currentUserID);
 assertNull(displayableListPanel.masterList);
 assertNull(displayableListPanel.displayableList);
 assertNull(displayableListPanel.select);
 assertNull(displayableListPanel.add);
 assertNull(displayableListPanel.edit);
 assertNull(displayableListPanel.view);
 assertNull(displayableListPanel.delete);
 assertNull(displayableListPanel.listItemSelector);
 assertNull(displayableListPanel.listItemAdder);
 assertNull(displayableListPanel.listItemEditor);
 assertNull(displayableListPanel.listItemViewer);
 assertNull(displayableListPanel.listItemDeleter);
 assertNull(displayableListPanel.listOwnerName);
 assertFalse(displayableListPanel.allowWriteAccess);
 assertFalse(displayableListPanel.allowRecordEditing);
 assertNull(displayableListPanel.masterSourceVariableCollection);
 assertNull(displayableListPanel.panel);
 }

 @Test
 public void testInit() {
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", allowWriteAccess, allowRecordEditing);
 assertSame(sessionProperties, displayableListPanel.sessionProperties);
 assertSame(parentForm, displayableListPanel.parentForm);
 assertSame("Test Panel", displayableListPanel.panelTitleText);
 assertTrue(allowWriteAccess);
 assertTrue(allowRecordEditing);
 assertNull(displayableListPanel.currentUserID);
 assertNotNull(displayableListPanel.masterList);
 assertNotNull(displayableListPanel.displayableList);
 assertNull(displayableListPanel.select);
 assertNull(displayableListPanel.add);
 assertNull(displayableListPanel.edit);
 assertNull(displayableListPanel.view);
 assertNull(displayableListPanel.delete);
 assertNull(displayableListPanel.listItemSelector);
 assertNull(displayableListPanel.listItemAdder);
 assertNull(displayableListPanel.listItemEditor);
 assertNull(displayableListPanel.listItemViewer);
 assertNull(displayableListPanel.listItemDeleter);
 assertNull(displayableListPanel.listOwnerName);
 assertNull(displayableListPanel.masterSourceVariableCollection);
 assertNull(displayableListPanel.panel);
 }

 @Test
 public void testBuildUI() {
 displayableListPanel.buildUI();
 assertNotNull(displayableListPanel.panel);
 }

 @Test
 public void testCreateControlButtonPanel() {
 displayableListPanel.createControlButtonPanel();
 assertNotNull(displayableListPanel.panel);
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertTrue(displayableListPanel.isNewRecord);
 }

 @Test
 public void testGetPanel() {
 displayableListPanel.buildUI();
 assertNotNull(displayableListPanel.panel);
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertSame(listItemSelector, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListItemAdder listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertSame(listItemAdder, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListItemEditor listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertSame(listItemEditor, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListItemViewer listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertSame(listItemViewer, displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListItemDeleter listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertSame(listItemDeleter, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 try {
 displayableListPanel.addListItem();
 fail("Expected MacawException to be thrown");
 } catch (MacawException e) {
 // Expected
 }
 }

 @Test
 public void testEditSelectedListItem() {
 try {
 displayableListPanel.editSelectedListItem();
 fail("Expected MacawException to be thrown");
 } catch (MacawException e) {
 // Expected
 }
 }

 @Test
 public void testViewSelectedListItem() {
 try {
 displayableListPanel.viewSelectedListItem();
 fail("Expected MacawException to be thrown");
 } catch (MacawException e) {
 // Expected
 }
 }

 @Test
 public void testDeleteSelectedListItems() {
 try {
 displayableListPanel.deleteSelectedListItems();
 fail("Expected MacawException to be thrown");
 } catch (MacawException e) {
 // Expected
 }
 }

 @Test
 public void testSelectListItems() {
 try {
 displayableListPanel.selectListItems();
 fail("Expected MacawException to be thrown");
 } catch (MacawException e) {
 // Expected
 }
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.add = Mockito.mock(JButton.class);
 displayableListPanel.delete = Mockito.mock(JButton.class);
 displayableListPanel.edit = Mockito.mock(JButton.class);
 displayableListPanel.view = Mockito.mock(JButton.class);
 displayableListPanel.select = Mockito.mock(JButton.class);
 displayableListPanel.isNewRecord = true;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel.isNewRecord = false;
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.add.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());

 displayableListPanel.allowWriteAccess = false;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 Displayable displayable = Mockito.mock(Displayable.class);
 masterList.add(displayable);
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 Displayable displayable = Mockito.mock(Displayable.class);
 masterList.add(displayable);
 displayableListPanel.displayableList.setDisplayItems(masterList);
 displayableListPanel.save();
 assertEquals(masterList, displayableListPanel.masterList);
 }
}