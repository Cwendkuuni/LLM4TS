package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import macaw.system.Log;
import macaw.system.SessionProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private Log log;

 @Before
 public void setup() {
 sessionProperties = Mockito.mock(SessionProperties.class);
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 log = Mockito.mock(Log.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true);
 }

 @Test
 public void testDisplayableListPanelConstruction() {
 assertNotNull(displayableListPanel);
 assertSame(sessionProperties, displayableListPanel.getSessionProperties());
 assertSame(parentForm, displayableListPanel.getParentForm());
 assertNull(displayableListPanel.getPanel());
 assertNull(displayableListPanel.getSelect());
 assertNull(displayableListPanel.getAdd());
 assertNull(displayableListPanel.getEdit());
 assertNull(displayableListPanel.getView());
 assertNull(displayableListPanel.getDelete());
 assertFalse(displayableListPanel.isNewRecord());
 }

 @Test
 public void testDisplayableListPanelConstructionWithAllowRecordEditing() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel Title", true, true);
 assertFalse(displayableListPanel.isNewRecord());
 }

 @Test
 public void testInit() {
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel Title", true, true);
 assertSame(sessionProperties, displayableListPanel.getSessionProperties());
 assertSame(parentForm, displayableListPanel.getParentForm());
 assertNotNull(displayableListPanel.getPanel());
 assertNotNull(displayableListPanel.getSelect());
 assertNotNull(displayableListPanel.getAdd());
 assertNotNull(displayableListPanel.getEdit());
 assertNotNull(displayableListPanel.getView());
 assertNotNull(displayableListPanel.getDelete());
 assertFalse(displayableListPanel.isNewRecord());
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertTrue(displayableListPanel.isNewRecord());
 }

 @Test
 public void testGetPanel() {
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel Title", true, true);
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector mockSelector = Mockito.mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(mockSelector);
 assertSame(mockSelector, displayableListPanel.getListItemSelector());
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListItemAdder mockAdder = Mockito.mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(mockAdder);
 assertSame(mockAdder, displayableListPanel.getListItemAdder());
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListItemEditor mockEditor = Mockito.mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(mockEditor);
 assertSame(mockEditor, displayableListPanel.getListItemEditor());
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListItemViewer mockViewer = Mockito.mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(mockViewer);
 assertSame(mockViewer, displayableListPanel.getListItemViewer());
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListItemDeleter mockDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(mockDeleter);
 assertSame(mockDeleter, displayableListPanel.getListItemDeleter());
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel Title", true, true);
 DisplayableListItemAdder mockAdder = Mockito.mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(mockAdder);
 List<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable("Test Item 1"));
 displayableListPanel.setDisplayableItems(masterList);
 displayableListPanel.addListItem();
 assertEquals(2, displayableListPanel.getDisplayableList().getAllItems().size());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithoutAdder() {
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel Title", true, true);
 List<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable("Test Item 1"));
 displayableListPanel.setDisplayableItems(masterList);
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem() {
 // TODO: Implement test for editSelectedListItem
 }

 @Test
 public void testViewSelectedListItem() {
 // TODO: Implement test for viewSelectedListItem
 }

 @Test
 public void testDeleteSelectedListItems() {
 // TODO: Implement test for deleteSelectedListItems
 }

 @Test
 public void testSelectListItems() {
 // TODO: Implement test for selectListItems
 }

 @Test
 public void testUpdateButtonStates() {
 // TODO: Implement test for updateButtonStates
 }

 @Test
 public void testSetDisplayableItems() {
 // TODO: Implement test for setDisplayableItems
 }

 @Test
 public void testSave() {
 // TODO: Implement test for save
 }

 @Test
 public void testGetAllListItems() {
 // TODO: Implement test for getAllListItems
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("Test Owner Name");
 assertEquals("Test Owner Name", displayableListPanel.getListOwnerName());
 }

 @Test
 public void testActionPerformedSelect() {
 ActionEvent mockSelectEvent = Mockito.mock(ActionEvent.class);
 displayableListPanel.select(mockSelectEvent);
 displayableListPanel.selectListItems();
 }

 @Test
 public void testActionPerformedAdd() {
 ActionEvent mockAddEvent = Mockito.mock(ActionEvent.class);
 displayableListPanel.add(mockAddEvent);
 displayableListPanel.addListItem();
 }

 @Test
 public void testActionPerformedEdit() {
 ActionEvent mockEditEvent = Mockito.mock(ActionEvent.class);
 displayableListPanel.edit(mockEditEvent);
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testActionPerformedView() {
 ActionEvent mockViewEvent = Mockito.mock(ActionEvent.class);
 displayableListPanel.view(mockViewEvent);
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testActionPerformedDelete() {
 ActionEvent mockDeleteEvent = Mockito.mock(ActionEvent.class);
 displayableListPanel.delete(mockDeleteEvent);
 displayableListPanel.deleteSelectedListItems();
 }
}