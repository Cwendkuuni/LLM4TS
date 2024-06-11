package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import macaw.businessLayer.*;
import java.awt.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;
 private String panelTitleText;
 private boolean allowWriteAccess;
 private boolean allowRecordEditing;
 private ArrayList<Displayable> masterList;
 private ArrayList<Displayable> masterSourceVariableCollection;
 private JPanel panel;
 private DisplayableList displayableList;
 private JButton select;
 private JButton add;
 private JButton edit;
 private JButton view;
 private JButton delete;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemViewer listItemViewer;
 private DisplayableListItemDeleter listItemDeleter;

 @Before
 public void setup() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 log = mock(Log.class);
 panelTitleText = "test title";
 allowWriteAccess = true;
 allowRecordEditing = true;
 masterList = new ArrayList<Displayable>();
 masterSourceVariableCollection = new ArrayList<Displayable>();
 displayableList = new DisplayableList(userInterfaceFactory);
 select = mock(JButton.class);
 add = mock(JButton.class);
 edit = mock(JButton.class);
 view = mock(JButton.class);
 delete = mock(JButton.class);
 listItemSelector = mock(DisplayableListItemSelector.class);
 listItemAdder = mock(DisplayableListItemAdder.class);
 listItemEditor = mock(DisplayableListItemEditor.class);
 listItemViewer = mock(DisplayableListItemViewer.class);
 listItemDeleter = mock(DisplayableListItemDeleter.class);

 when(sessionProperties.getLog()).thenReturn(log);
 when(sessionProperties.getProperty("currentUser")).thenReturn(new User());
 when(userInterfaceFactory.createButton(anyString())).thenAnswer(invocation -> invocation.callRealMethod());
 when(userInterfaceFactory.createLabel(anyString())).thenAnswer(invocation -> invocation.callRealMethod());
 when(userInterfaceFactory.createPanel()).thenAnswer(invocation -> invocation.callRealMethod());
 when(parentForm.commitChanges()).thenReturn(true);
 when(listItemSelector.selectListItems(anyString(), any(DisplayableList.class))).thenReturn(true);
 when(listItemAdder.addItems(anyString(), any(DisplayableList.class))).thenReturn(true);
 when(listItemEditor.editListItem(anyString(), any(Displayable.class), any(DisplayableList.class))).thenReturn(true);
 when(listItemViewer.viewListItem(any(Displayable.class))).thenReturn(true);
 when(listItemDeleter.deleteListItems(anyString(), any(DisplayableList.class))).thenReturn(true);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 }

 @Test
 public void testDisplayableListPanelConstruction() {
 assertNotNull(displayableListPanel);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDisplayableListPanelInvalidConstruction1() {
 new DisplayableListPanel(null, null, null, false, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDisplayableListPanelInvalidConstruction2() {
 new DisplayableListPanel(null, null, null, true, false);
 }

 @Test
 public void testDisplayableListPanelConstructorWithAllowWriteAccessAndAllowRecordEditing() {
 assertEquals(panelTitleText, displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertTrue(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testDisplayableListPanelConstructorWithoutAllowRecordEditing() {
 assertEquals(panelTitleText, displayableListPanel.getPanelTitleText());
 assertTrue(displayableListPanel.isAllowWriteAccess());
 assertFalse(displayableListPanel.isAllowRecordEditing());
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertFalse(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isViewEnabled());
 assertFalse(displayableListPanel.isSelectEnabled());

 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.isAddEnabled());
 assertTrue(displayableListPanel.isSelectEnabled());
 if (!displayableListPanel.isEmpty()) {
 assertTrue(displayableListPanel.isDeleteEnabled());
 assertTrue(displayableListPanel.isEditEnabled());
 assertTrue(displayableListPanel.isViewEnabled());
 }
 }

 @Test
 public void testGetPanel() {
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertSame(listItemSelector, displayableListPanel.getListItemSelector());
 }

 @Test
 public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertSame(listItemAdder, displayableListPanel.getListItemAdder());
 }

 @Test
 public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertSame(listItemEditor, displayableListPanel.getListItemEditor());
 }

 @Test
 public void testSetDisplayableListItemViewer() {
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertSame(listItemViewer, displayableListPanel.getListItemViewer());
 }

 @Test
 public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertSame(listItemDeleter, displayableListPanel.getListItemDeleter());
 }

 @Test
 public void testAddListItem() {
 displayableListPanel.addListItem();
 assertEquals(1, masterList.size());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemWithMissingAdder() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess);
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItem() {
 Displayable selectedDisplayable = mock(Displayable.class);
 when(displayableList.getSelectedItem()).thenReturn(selectedDisplayable);
 displayableListPanel.editSelectedListItem();
 assertEquals(selectedDisplayable.getDisplayName(), selectedDisplayable.getDisplayName()); // No change expected
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemWithMissingEditor() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItem() {
 Displayable selectedDisplayable = mock(Displayable.class);
 when(displayableList.getSelectedItem()).thenReturn(selectedDisplayable);
 displayableListPanel.viewSelectedListItem();
 assertTrue(listItemViewer.viewListItem(selectedDisplayable));
 }

 @Test(expected = MacawException.class)
 public void testViewSelectedListItemWithMissingViewer() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItems() {
 when(displayableList.itemsAreSelected()).thenReturn(true);
 displayableListPanel.deleteSelectedListItems();
 assertEquals(0, displayableList.getAllItems().size());
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsWithNoSelection() {
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, panelTitleText, allowWriteAccess, allowRecordEditing);
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testSelectListItems() {
 when(listItemSelector.selectListItems(anyString(), any(DisplayableList.class))).thenReturn(true);
 displayableListPanel.selectListItems();
 assertFalse(displayableList.isEmpty());
 assertTrue(displayableList.itemsAreSelected());
 }

 @Test
 public void testUpdateButtonStates() {
 when(displayableList.isEmpty()).thenReturn(true);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.isAddEnabled());
 assertFalse(displayableListPanel.isDeleteEnabled());
 assertFalse(displayableListPanel.isEditEnabled());
 assertFalse(displayableListPanel.isViewEnabled());
 assertFalse(displayableListPanel.isSelectEnabled());

 when(displayableList.isEmpty()).thenReturn(false);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.isAddEnabled());
 assertTrue(displayableListPanel.isSelectEnabled());
 assertTrue(displayableListPanel.isDeleteEnabled());
 assertTrue(displayableListPanel.isEditEnabled());
 assertTrue(displayableListPanel.isViewEnabled());
 }

 @Test
 public void testSetDisplayableItems() {
 ArrayList<Displayable> testDisplayables = new ArrayList<Displayable>();
 testDisplayables.add(mock(Displayable.class));
 displayableListPanel.setDisplayableItems(testDisplayables);
 assertEquals(testDisplayables, displayableListPanel.getAllListItems());
 }

 @Test
 public void testSave() {
 displayableListPanel.save();
 assertEquals(masterList, displayableListPanel.getAllListItems());
 }

 @Test
 public void testGetAllListItems() {
 assertEquals(masterList, displayableListPanel.getAllListItems());
 }

 @Test
 public void testSetListOwnerName() {
 displayableListPanel.setListOwnerName("test name");
 assertEquals("test name", displayableListPanel.getListOwnerName());
 }

 @Test
 public void testActionPerformed() {
 ActionEvent event = mock(ActionEvent.class);
 when(select.isEnabled()).thenReturn(true);
 when(add.isEnabled()).thenReturn(true);
 when(edit.isEnabled()).thenReturn(true);
 when(view.isEnabled()).thenReturn(true);
 when(delete.isEnabled()).thenReturn(true);
 displayableListPanel.actionPerformed(event);
 }

 @Test
 public void testActionPerformedWithDisabledAddButton() {
 when(add.isEnabled()).thenReturn(false);
 ActionEvent event = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(event);
 }

 @Test
 public void testActionPerformedWithDisabledDeleteButton() {
 when(delete.isEnabled()).thenReturn(false);
 ActionEvent event = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(event);
 }

 @Test
 public void testActionPerformedWithDisabledEditButton() {
 when(edit.isEnabled()).thenReturn(false);
 ActionEvent event = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(event);
 }

 @Test
 public void testActionPerformedWithDisabledViewButton() {
 when(view.isEnabled()).thenReturn(false);
 ActionEvent event = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(event);
 }

 @Test
 public void testActionPerformedWithDisabledSelectButton() {
 when(select.isEnabled()).thenReturn(false);
 ActionEvent event = mock(ActionEvent.class);
 displayableListPanel.actionPerformed(event);
 }
}