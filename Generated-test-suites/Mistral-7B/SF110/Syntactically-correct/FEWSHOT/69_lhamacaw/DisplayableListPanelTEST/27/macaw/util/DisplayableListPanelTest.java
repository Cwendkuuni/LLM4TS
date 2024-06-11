package macaw.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import macaw.businessLayer.*;
import java.awt.*;
import macaw.system.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.*;

public class DisplayableListPanelTest {

DisplayableListPanel displayableListPanel;
SessionProperties sessionProperties;
DisplayableListParentForm parentForm;
DisplayableList displayableList;
JPanel panel;
DisplayableListItemSelector listItemSelector;
DisplayableListItemAdder listItemAdder;
DisplayableListItemEditor listItemEditor;
DisplayableListItemViewer listItemViewer;
DisplayableListItemDeleter listItemDeleter;

@Before
public void setUp() {
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 displayableList = mock(DisplayableList.class);
 listItemSelector = mock(DisplayableListItemSelector.class);
 listItemAdder = mock(DisplayableListItemAdder.class);
 listItemEditor = mock(DisplayableListItemEditor.class);
 listItemViewer = mock(DisplayableListItemViewer.class);
 listItemDeleter = mock(DisplayableListItemDeleter.class);
 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true, true);
}

@Test
public void testDisplayableListPanelCreation() {
 assertEquals("Test Panel", displayableListPanel.getPanelTitleText());
 assertFalse(displayableListPanel.isNewRecord());
 assertNull(displayableListPanel.getListItemSelector());
 assertNull(displayableListPanel.getListItemAdder());
 assertNull(displayableListPanel.getListItemEditor());
 assertNull(displayableListPanel.getListItemViewer());
 assertNull(displayableListPanel.getListItemDeleter());
 assertNull(displayableListPanel.getPanel());
}

@Test
public void testDisplayableListPanelInit() {
 displayableListPanel.init(sessionProperties, parentForm, "Test Panel", true, true);
 assertNotNull(displayableListPanel.getSessionProperties());
 assertNotNull(displayableListPanel.getLog());
 assertNotNull(displayableListPanel.getParentForm());
 assertNotNull(displayableListPanel.getDisplayableList());
 assertNotNull(displayableListPanel.getSelect());
 assertNotNull(displayableListPanel.getAdd());
 assertNotNull(displayableListPanel.getEdit());
 assertNotNull(displayableListPanel.getView());
 assertNotNull(displayableListPanel.getDelete());
}

@Test
public void testBuildUI() {
 displayableListPanel.buildUI();
 assertNotNull(displayableListPanel.getPanel());
}

@Test
public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertTrue(displayableListPanel.isNewRecord());
}

@Test
public void testSetDisplayableListItemSelector() {
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertNotNull(displayableListPanel.getListItemSelector());
}

@Test
public void testSetDisplayableListItemAdder() {
 displayableListPanel.setDisplayableListItemAdder(listItemAdder);
 assertNotNull(displayableListPanel.getListItemAdder());
}

@Test
public void testSetDisplayableListItemEditor() {
 displayableListPanel.setDisplayableListItemEditor(listItemEditor);
 assertNotNull(displayableListPanel.getListItemEditor());
}

@Test
public void testSetDisplayableListItemViewer() {
 displayableListPanel.setDisplayableListItemViewer(listItemViewer);
 assertNotNull(displayableListPanel.getListItemViewer());
}

@Test
public void testSetDisplayableListItemDeleter() {
 displayableListPanel.setDisplayableListItemDeleter(listItemDeleter);
 assertNotNull(displayableListPanel.getListItemDeleter());
}

@Test
public void testAddListItem() {
 when(listItemAdder.addItems(displayableListPanel.getListOwnerName(), displayableList)).thenReturn(new ArrayList<>());
 displayableListPanel.addListItem();
 assertTrue(displayableListPanel.isNewRecord());
 assertFalse(displayableList.itemsAreSelected());
}

@Test
public void testEditSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 when(listItemEditor.editListItem(displayableListPanel.getListOwnerName(), selectedListItem, displayableList)).thenReturn(selectedListItem);
 displayableListPanel.editSelectedListItem();
 assertFalse(displayableList.itemsAreSelected());
}

@Test
public void testViewSelectedListItem() {
 Displayable selectedListItem = mock(Displayable.class);
 when(displayableList.getSelectedItem()).thenReturn(selectedListItem);
 when(listItemViewer.viewListItem(selectedListItem)).thenReturn(new Object());
 displayableListPanel.viewSelectedListItem();
 assertFalse(displayableList.itemsAreSelected());
}

@Test
public void testDeleteSelectedListItems() {
 when(displayableList.itemsAreSelected()).thenReturn(true);
 when(listItemDeleter.deleteListItems(displayableListPanel.getListOwnerName(), displayableList)).thenReturn(new ArrayList<>());
 displayableListPanel.deleteSelectedListItems();
 assertFalse(displayableList.itemsAreSelected());
}

@Test
public void testSelectListItems() {
 when(listItemSelector.selectListItems(displayableListPanel.getListOwnerName(), displayableList)).thenReturn(new ArrayList<>());
 displayableListPanel.selectListItems();
 assertTrue(displayableList.itemsAreSelected());
}

@Test
public void testUpdateButtonStates() {
 displayableListPanel.addListItem();
 displayableListPanel.setDisplayableList(displayableList);
 displayableList.addItem(mock(Displayable.class));
 displayableList.setSelectedIndex(0);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.getAdd().isEnabled());
 assertTrue(displayableListPanel.getSelect().isEnabled());
 assertFalse(displayableListPanel.getEdit().isEnabled());
 assertFalse(displayableListPanel.getView().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 displayableList.clearSelectedIndex();
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.getAdd().isEnabled());
 assertFalse(displayableListPanel.getSelect().isEnabled());
 assertTrue(displayableListPanel.getEdit().isEnabled());
 assertFalse(displayableListPanel.getView().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 displayableList.setSelectedIndex(0);
 displayableListPanel.setAllowRecordEditing(false);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.getEdit().isEnabled());
 assertFalse(displayableListPanel.getView().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 displayableListPanel.setAllowRecordEditing(true);
 displayableListPanel.updateButtonStates();
 assertTrue(displayableListPanel.getEdit().isEnabled());
 assertFalse(displayableListPanel.getView().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 displayableListPanel.setAllowWriteAccess(false);
 displayableListPanel.updateButtonStates();
 assertFalse(displayableListPanel.getAdd().isEnabled());
 assertFalse(displayableListPanel.getDelete().isEnabled());
 assertFalse(displayableListPanel.getEdit().isEnabled());
 assertTrue(displayableListPanel.getView().isEnabled());
}

@Test
public void testSetDisplayableItems() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(mock(Displayable.class));
 displayableListPanel.setDisplayableItems(masterList);
 assertEquals(masterList, displayableListPanel.getAllListItems());
}

@Test
public void testSave() {
 ArrayList<Displayable> items = new ArrayList<>();
 items.add(mock(Displayable.class));
 displayableListPanel.setDisplayableList(displayableList);
 displayableList.setAllItems(items);
 displayableListPanel.save();
 assertEquals(items, displayableListPanel.getMasterList());
}
}