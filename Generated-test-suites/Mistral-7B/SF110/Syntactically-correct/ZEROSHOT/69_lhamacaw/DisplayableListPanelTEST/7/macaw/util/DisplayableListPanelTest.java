package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.system.*;
import org.junit.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private Log log;

 @Before
 public void setUp() {
 sessionProperties = new SessionProperties();
 parentForm = mock(DisplayableListParentForm.class);
 log = mock(Log.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 }

 @After
 public void tearDown() {
 displayableListPanel = null;
 sessionProperties = null;
 parentForm = null;
 log = null;
 }

 @Test
 public void testDisplayableListPanel() {
 // Test constructor with allowWriteAccess and allowRecordEditing set to true
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 assertNotNull(displayableListPanel.getPanel());

 // Test constructor with allowWriteAccess set to true and allowRecordEditing set to false
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, false);
 assertNotNull(displayableListPanel.getPanel());

 // Test constructor with allowWriteAccess set to false
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", false, false);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testInit() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 assertNotNull(displayableListPanel.sessionProperties);
 assertNotNull(displayableListPanel.log);
 assertNotNull(displayableListPanel.parentForm);
 assertNotNull(displayableListPanel.panelTitleText);
 assertNotNull(displayableListPanel.currentUserID);
 assertNull(displayableListPanel.masterList);
 assertNotNull(displayableListPanel.displayableList);
 assertNotNull(displayableListPanel.select);
 assertNotNull(displayableListPanel.add);
 assertNotNull(displayableListPanel.edit);
 assertNotNull(displayableListPanel.view);
 assertNotNull(displayableListPanel.delete);
 }

 @Test
 public void testBuildUI() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 assertNotNull(displayableListPanel.panel);
 }

 @Test
 public void testCreateControlButtonPanel() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 JPanel controlButtonPanel = displayableListPanel.createControlButtonPanel();
 assertNotNull(controlButtonPanel);
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.isNewRecord);
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.isNewRecord);
 }

 @Test
 public void testGetPanel() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 JPanel panel = displayableListPanel.getPanel();
 assertNotNull(panel);
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertSame(displayableListPanel.listItemSelector, listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 DisplayableListItemAdder listItemAdder = mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertSame(displayableListPanel.listItemAdder, listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 DisplayableListItemEditor listItemEditor = mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertSame(displayableListPanel.listItemEditor, listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 DisplayableListItemViewer listItemViewer = mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertSame(displayableListPanel.listItemViewer, listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 DisplayableListItemDeleter listItemDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertSame(displayableListPanel.listItemDeleter, listItemDeleter);
 }

 @Test
 public void testAddListItem() {
 displayableListPanel = spy(new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true));
 doNothing().when(displayableListPanel).parentForm.commitChanges();
 doThrow(new MacawException("Test Exception")).when(displayableListPanel).listItemAdder.addItems("Test Owner Name", displayableListPanel.displayableList);

 displayableListPanel.addListItem();
 verify(displayableListPanel.log).displayErrorDialog(argThat(isA(MacawException.class)));
 }

 @Test
 public void testEditSelectedListItem() {
 displayableListPanel = spy(new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true));
 doNothing().when(displayableListPanel).parentForm.commitChanges();
 doThrow(new MacawException("Test Exception")).when(displayableListPanel.listItemEditor).editListItem("Test Owner Name", mock(Displayable.class), displayableListPanel.displayableList);

 displayableListPanel.editSelectedListItem();
 verify(displayableListPanel.log).displayErrorDialog(argThat(isA(MacawException.class)));
 }

 @Test
 public void testViewSelectedListItem() {
 displayableListPanel = spy(new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true));
 doNothing().when(displayableListPanel).parentForm.commitChanges();
 doThrow(new MacawException("Test Exception")).when(displayableListPanel.listItemViewer).viewListItem(mock(Displayable.class));

 displayableListPanel.viewSelectedListItem();
 verify(displayableListPanel.log).displayErrorDialog(argThat(isA(MacawException.class)));
 }

 @Test
 public void testDeleteSelectedListItems() {
 displayableListPanel = spy(new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true));
 doNothing().when(displayableListPanel).parentForm.commitChanges();
 doThrow(new MacawException("Test Exception")).when(displayableListPanel.listItemDeleter).deleteListItems("Test Owner Name", displayableListPanel.displayableList);

 displayableListPanel.deleteSelectedListItems();
 verify(displayableListPanel.log).displayErrorDialog(argThat(isA(MacawException.class)));
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel = spy(new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true));
 doNothing().when(displayableListPanel.listItemSelector).selectListItems("Test Owner Name", displayableListPanel.displayableList);

 displayableListPanel.selectListItems();
 verify(displayableListPanel.updateButtonStates()).once();
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 assertFalse(displayableListPanel.isNewRecord);
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

 displayableListPanel.displayableList.clear();
 displayableListPanel.displayableList.add(new Displayable("Test Display Name", "Test Display Value"));
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertFalse(displayableListPanel.select.isEnabled());

 displayableListPanel.allowWriteAccess = false;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());

 displayableListPanel.displayableList.clear();
 displayableListPanel.displayableList.add(new Displayable("Test Display Name", "Test Display Value"));
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertFalse(displayableListPanel.edit.isEnabled());
 assertFalse(displayableListPanel.view.isEnabled());
 assertTrue(displayableListPanel.select.isEnabled());

 displayableListPanel.allowRecordEditing = false;
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());

 displayableListPanel.displayableList.clear();
 displayableListPanel.displayableList.add(new Displayable("Test Display Name", "Test Display Value"));
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.add.isEnabled());
 assertFalse(displayableListPanel.delete.isEnabled());
 assertTrue(displayableListPanel.edit.isEnabled());
 assertTrue(displayableListPanel.view.isEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 List<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable("Test Display Name 1", "Test Display Value 1"));
 masterList.add(new Displayable("Test Display Name 2", "Test Display Value 2"));
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList.size(), displayableListPanel.displayableList.getAllItems().size());
 }

 @Test
 public void testSave() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 List<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable("Test Display Name 1", "Test Display Value 1"));
 masterList.add(new Displayable("Test Display Name 2", "Test Display Value 2"));
 displayableListPanel.displayableList.setDisplayItems(masterList);
 displayableListPanel.save();
 assertEquals(masterList.size(), displayableListPanel.masterList.size());
 }

 @Test
 public void testGetAllListItems() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 List<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable("Test Display Name 1", "Test Display Value 1"));
 masterList.add(new Displayable("Test Display Name 2", "Test Display Value 2"));
 displayableListPanel.displayableList.setDisplayItems(masterList);
 assertEquals(masterList.size(), displayableListPanel.getAllListItems().size());
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 displayableListPanel.setListOwnerName("Test Owner Name");
 assertEquals("Test Owner Name", displayableListPanel.listOwnerName);
 }
}