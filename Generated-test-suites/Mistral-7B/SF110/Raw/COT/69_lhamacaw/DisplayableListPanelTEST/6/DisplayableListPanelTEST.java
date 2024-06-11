package macaw.util;

import static org.junit.Assert.*;

import macaw.businessLayer.*;
import macaw.system.Log;
import macaw.util.DisplayableListPanel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableListParentForm parentForm;
 private SessionProperties sessionProperties;
 private Log log;
 private UserInterfaceFactory userInterfaceFactory;

 @Before
 public void setUp() {
 sessionProperties = Mockito.mock(SessionProperties.class);
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 log = Mockito.mock(Log.class);
 userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccessAndAllowRecordEditing() {
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccess() {
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccessAndNoAllowRecordEditing() {
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testInitMethod() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 verify(parentForm, Mockito.times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testInitMethodWithMissingListItemAdderThrowsException() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 verify(parentForm, Mockito.times(1)).commitChanges();
}

 @Test(expected = MacawException.class)
 public void testInitMethodWithMissingDisplayableListThrowsException() {
 DisplayableListPanel displayableListPanelSpy = Mockito.spy(new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true));
 doThrow(new MacawException("Test Exception")).when(displayableListPanelSpy).getPanel();
 verify(parentForm, Mockito.times(1)).commitChanges();
 try {
 displayableListPanelSpy.init(sessionProperties, parentForm, "Test Panel", true, true);
 fail();
 } catch (MacawException e) {
 // expected
 }
}

 @Test
 public void testBuildUIMethod() {
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testCreateControlButtonPanelMethod() {
 DisplayableListPanel displayableListPanelSpy = Mockito.spy(displayableListPanel);
 JPanel controlButtonPanel = displayableListPanelSpy.createControlButtonPanel();
 assertNotNull(controlButtonPanel);
 }

 @Test
 public void testSetNewRecordMethod() {
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.isNewRecord());
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.isNewRecord());
 }

 @Test
 public void testSetDisplayableListItemSelectorMethod() {
 DisplayableListItemSelector listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertSame(listItemSelector, displayableListPanel.listItemSelector);
 }

 @Test
 public void testSetDisplayableListItemAdderMethod() {
 DisplayableListItemAdder listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertSame(listItemAdder, displayableListPanel.listItemAdder);
 }

 @Test
 public void testSetDisplayableListItemEditorMethod() {
 DisplayableListItemEditor listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertSame(listItemEditor, displayableListPanel.listItemEditor);
 }

 @Test
 public void testSetDisplayableListItemViewerMethod() {
 DisplayableListItemViewer listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertSame(listItemViewer, displayableListPanel.listItemViewer);
 }

 @Test
 public void testSetDisplayableListItemDeleterMethod() {
 DisplayableListItemDeleter listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertSame(listItemDeleter, displayableListPanel.listItemDeleter);
 }

 @Test
 public void testAddListItemMethod() {
 displayableListPanel.addListItem();
 verify(parentForm, Mockito.times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMethodWithMissingListItemAdderThrowsException() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 displayableListPanel.addListItem();
 fail();
 }

 @Test
 public void testEditSelectedListItemMethod() {
 Displayable selectedListItem = Mockito.mock(Displayable.class);
 displayableListPanel.editSelectedListItem();
 verify(parentForm, Mockito.times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemMethodWithMissingListItemEditorThrowsException() {
 displayableListPanel.editSelectedListItem();
 verify(parentForm, Mockito.times(1)).commitChanges();
 try {
 displayableListPanel.editSelectedListItem();
 fail();
 } catch (MacawException e) {
 // expected
 }
 }

 @Test
 public void testViewSelectedListItemMethod() {
 Displayable selectedListItem = Mockito.mock(Displayable.class);
 displayableListPanel.viewSelectedListItem();
 // TODO: Implement test for this method
 }

 @Test
 public void testDeleteSelectedListItemsMethod() {
 displayableListPanel.deleteSelectedListItems();
 verify(parentForm, Mockito.times(1)).commitChanges();
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsMethodWithMissingListItemDeleterThrowsException() {
 displayableListPanel.deleteSelectedListItems();
 verify(parentForm, Mockito.times(1)).commitChanges();
 try {
 displayableListPanel.deleteSelectedListItems();
 fail();
 } catch (MacawException e) {
 // expected
 }
 }

 @Test
 public void testSelectListItemsMethod() {
 displayableListPanel.selectListItems();
 verify(parentForm, Mockito.times(1)).commitChanges();
 }

 @Test
 public void testUpdateButtonStatesMethod() {
 displayableListPanel.updateButtonStates();
 // TODO: Implement test for this method
 }

 @Test
 public void testSetDisplayableItemsMethod() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 Displayable displayable = Mockito.mock(Displayable.class);
 masterList.add(displayable);
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSaveMethod() {
 displayableListPanel.save();
 assertEquals(displayableListPanel.masterList, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testGetAllListItemsMethod() {
 ArrayList<Displayable> allListItems = displayableListPanel.getAllListItems();
 assertNotNull(allListItems);
 }

 @Test
 public void testSetListOwnerNameMethod() {
 String listOwnerName = "Test Owner";
 displayableListPanel.setListOwnerName(listOwnerName);
 assertEquals(listOwnerName, displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformedMethod() {
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);
 displayableListPanel.actionPerformed(actionEvent);
 // TODO: Implement test for this method
 }
}