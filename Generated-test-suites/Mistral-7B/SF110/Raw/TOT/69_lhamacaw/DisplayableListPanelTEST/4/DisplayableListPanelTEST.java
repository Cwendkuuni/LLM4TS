package macaw.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import macaw.businessLayer.*;
import macaw.system.Log;
import macaw.system.SessionProperties;
import macaw.util.DisplayableListPanel;
import macaw.util.DisplayableListParentForm;
import macaw.util.DisplayableListItemSelector;
import macaw.util.DisplayableListItemAdder;
import macaw.util.DisplayableListItemEditor;
import macaw.util.DisplayableListItemViewer;
import macaw.util.DisplayableListItemDeleter;
import macaw.util.UserInterfaceFactory;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private Log log;
 private DisplayableList displayableList;
 private DisplayableListItemSelector listItemSelector;
 private DisplayableListItemAdder listItemAdder;
 private DisplayableListItemEditor listItemEditor;
 private DisplayableListItemViewer listItemViewer;
 private DisplayableListItemDeleter listItemDeleter;

 @Before
 public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);
 log = mock(Log.class);
 displayableList = mock(DisplayableList.class);

 listItemSelector = mock(DisplayableListItemSelector.class);
 listItemAdder = mock(DisplayableListItemAdder.class);
 listItemEditor = mock(DisplayableListItemEditor.class);
 listItemViewer = mock(DisplayableListItemViewer.class);
 listItemDeleter = mock(DisplayableListItemDeleter.class);

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 displayableListPanel.userInterfaceFactory = userInterfaceFactory;
 displayableListPanel.log = log;
 displayableListPanel.displayableList = displayableList;
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 }

 @Test
 public void testInit() {
 // Test initialization with allowWriteAccess and allowRecordEditing true
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
 verify(parentForm, times(1)).commitChanges();

 // Test initialization with allowWriteAccess and allowRecordEditing false
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 verify(parentForm, times(1)).commitChanges();
 }

 @Test
 public void testBuildUI() {
 // Test UI component creation
 verify(userInterfaceFactory, times(1)).createLabel("Test Panel");
 verify(userInterfaceFactory, times(1)).createButton("Select");
 // Other UI component verifications omitted for brevity

 // Test UI component layout
 verify(panel, times(1)).add(any(JLabel.class), any(GridBagConstraints.class));
 // Other UI component layout verifications omitted for brevity
 }

 @Test
 public void testSetNewRecordAndUpdateButtonStates() {
 displayableListPanel.setNewRecord(true);
 assertTrue(displayableListPanel.isNewRecord);
 verify(add, times(1)).setEnabled(false);
 verify(delete, times(1)).setEnabled(false);
 verify(edit, times(1)).setEnabled(false);
 verify(view, times(1)).setEnabled(false);
 verify(select, times(1)).setEnabled(false);

 displayableListPanel.setNewRecord(false);
 assertFalse(displayableListPanel.isNewRecord);
 verify(add, times(1)).setEnabled(true);
 verify(select, times(1)).setEnabled(true);
 verify(delete, times(1)).setEnabled(displayableList.isEmpty());
 if (!displayableList.isEmpty()) {
 verify(edit, times(1)).setEnabled(true);
 verify(view, times(1)).setEnabled(true);
 }
 }

 @Test
 public void testSetDisplayableItems() {
 final List<Displayable> masterList = new ArrayList<>();
 masterList.add(mock(Displayable.class));
 masterList.add(mock(Displayable.class));
 displayableListPanel.setDisplayableItems(masterList);
 verify(displayableList, times(1)).setDisplayItems(masterList);
 }

 @Test
 public void testSelectListItems() {
 // Test selecting all items
 displayableListPanel.selectListItems();
 verify(listItemSelector, times(1)).selectListItems("Test List Owner", displayableList);

 // Test selecting no items
 displayableList.clear();
 displayableListPanel.selectListItems();
 verify(listItemSelector, times(0)).selectListItems("Test List Owner", displayableList);
 }

 @Test
 public void testAddListItem() {
 // Test adding an item with a non-null listItemAdder
 displayableList.clear();
 when(listItemAdder.addItems("Test List Owner", displayableList)).thenReturn(new ArrayList<>());
 displayableListPanel.addListItem();
 verify(displayableList, times(1)).add(any(Displayable.class));
 verify(parentForm, times(1)).commitChanges();

 // Test adding an item with a null listItemAdder
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, false);
 displayableListPanel.addListItem();
 verify(log, times(1)).displayErrorDialog(containsString("missingAdder"));
 }

 // Test other methods (editSelectedListItem, viewSelectedListItem, deleteSelectedListItems) and error scenarios omitted for brevity
}