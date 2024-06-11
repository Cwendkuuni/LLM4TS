package macaw.util.test;

import static org.junit.Assert.*;

import macaw.businessLayer.*;
import macaw.system.Log;
import macaw.util.DisplayableListPanel;
import macaw.util.DisplayableListParentForm;
import macaw.util.UserInterfaceFactory;
import macaw.util.macawMessages.MacawMessages;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

import org.junit.*;
import org.junit.Test;
import org.mockito.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;
 private MacawMessages macawMessages;
 private SessionProperties sessionProperties;

 @Before
 public void setUp() {
 sessionProperties = Mockito.mock(SessionProperties.class);
 log = Mockito.mock(Log.class);
 macawMessages = Mockito.mock(MacawMessages.class);
 userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 }

 @Test
 public void testDisplayableListPanelConstructor() {
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertFalse(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowRecordEditing() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 assertFalse(displayableListPanel.isAllowWriteAccess());
 assertTrue(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testInit() {
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, false);
 List<Displayable> masterList = new ArrayList<Displayable>();
 assertEquals(masterList, displayableListPanel.getMasterList());

 DisplayableList displayableList = Mockito.mock(DisplayableList.class);
 DisplayableListItemAdder listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
 DisplayableListItemEditor listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
 DisplayableListItemViewer listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
 DisplayableListItemDeleter listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);

 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, false);
 Mockito.verify(displayableListPanel, Mockito.times(1)).displayableList = Mockito.argThat(arg -> arg instanceof DisplayableList);
 Mockito.verify(displayableListPanel, Mockito.times(1)).listItemSelector = Mockito.argThat(arg -> arg instanceof DisplayableListItemSelector);
 Mockito.verify(displayableListPanel, Mockito.times(1)).listItemAdder = listItemAdder;
 Mockito.verify(displayableListPanel, Mockito.times(1)).listItemEditor = listItemEditor;
 Mockito.verify(displayableListPanel, Mockito.times(1)).listItemViewer = listItemViewer;
 Mockito.verify(displayableListPanel, Mockito.times(1)).listItemDeleter = listItemDeleter;

 Mockito.verifyNoMoreInteractions(displayableListPanel);
 }

 @Test
 public void testBuildUI() {
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector listItemSelector = Mockito.mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertSame(listItemSelector, displayableListPanel.getListItemSelector());
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 DisplayableListItemAdder listItemAdder = Mockito.mock(DisplayableListItemAdder.class);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertSame(listItemAdder, displayableListPanel.getListItemAdder());
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 DisplayableListItemEditor listItemEditor = Mockito.mock(DisplayableListItemEditor.class);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertSame(listItemEditor, displayableListPanel.getListItemEditor());
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 DisplayableListItemViewer listItemViewer = Mockito.mock(DisplayableListItemViewer.class);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertSame(listItemViewer, displayableListPanel.getListItemViewer());
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 DisplayableListItemDeleter listItemDeleter = Mockito.mock(DisplayableListItemDeleter.class);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertSame(listItemDeleter, displayableListPanel.getListItemDeleter());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithMissingAdder() {
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItemWithMissingEditor() {
 displayableListPanel.editSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithMissingViewer() {
 displayableListPanel.viewSelectedListItem();
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithMissingDeleter() {
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItems() {
 displayableListPanel.selectListItems();
 }

 @Test
 public void testUpdateButtonStates() {
 displayableListPanel.setMasterList(new ArrayList<>());
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertFalse(displayableListPanel.isViewEnabled());
 assertFalse(displayableListPanel.isSelectEnabled());

 displayableListPanel.setMasterList(Arrays.asList(new Displayable[] { new Displayable() }));
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertTrue(displayableListPanel.isViewEnabled());
 assertTrue(displayableListPanel.isSelectEnabled());

 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertFalse(displayableListPanel.isViewEnabled());
 assertFalse(displayableListPanel.isSelectEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 List<Displayable> masterList = new ArrayList<>();
 masterList.add(new Displayable());
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.getMasterList());
 }

 @Test
 public void testSave() {
 displayableListPanel.save();
 List<Displayable> masterList = displayableListPanel.getMasterList();
 List<Displayable> currentList = displayableListPanel.displayableList.getAllItems();
 assertEquals(masterList, currentList);
 }

 @Test
 public void testGetAllListItems() {
 List<Displayable> allListItems = displayableListPanel.getAllListItems();
 assertEquals(displayableListPanel.displayableList.getAllItems(), allListItems);
 }

 @Test
 public void testSetListOwnerName() {
 String listOwnerName = "Test List Owner";
 displayableListPanel.setListOwnerName(listOwnerName);
 assertEquals(listOwnerName, displayableListPanel.getListOwnerName());
 }

 @Test
 public void testActionPerformedSelectListItems() {
 displayableListPanel.actionPerformed(Mockito.mock(ActionEvent.class));
 displayableListPanel.selectListItems();
 }

 @Test
 public void testActionPerformedAddListItem() {
 displayableListPanel.actionPerformed(Mockito.mock(ActionEvent.class));
 displayableListPanel.addListItem();
 }

 @Test
 public void testActionPerformedEditSelectedListItem() {
 Displayable selectedListItem = new Displayable();
 displayableListPanel.displayableList.add(selectedListItem);
 displayableListPanel.actionPerformed(Mockito.mock(ActionEvent.class));
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testActionPerformedViewSelectedListItem() {
 Displayable selectedListItem = new Displayable();
 displayableListPanel.displayableList.add(selectedListItem);
 displayableListPanel.actionPerformed(Mockito.mock(ActionEvent.class));
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testActionPerformedDeleteSelectedListItems() {
 Displayable selectedListItem1 = new Displayable();
 Displayable selectedListItem2 = new Displayable();
 displayableListPanel.displayableList.add(selectedListItem1);
 displayableListPanel.displayableList.add(selectedListItem2);
 displayableListPanel.actionPerformed(Mockito.mock(ActionEvent.class));
 displayableListPanel.deleteSelectedListItems();
 }
}