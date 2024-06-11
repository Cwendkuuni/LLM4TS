import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import macaw.businessLayer.*; // For BusinessLayerException
import macaw.system.*; // For MacawException, MacawErrorType
import macaw.util.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class DisplayableListPanelTest {

 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private JButton selectButton;
 private JButton addButton;
 private JButton editButton;
 private JButton viewButton;
 private JButton deleteButton;

 @Before
 public void setUp() {
 displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel", true, true);
 sessionProperties = mock(SessionProperties.class);
 parentForm = mock(DisplayableListParentForm.class);
 userInterfaceFactory = mock(UserInterfaceFactory.class);

 selectButton = mock(JButton.class);
 addButton = mock(JButton.class);
 editButton = mock(JButton.class);
 viewButton = mock(JButton.class);
 deleteButton = mock(JButton.class);

 displayableListPanel.setUserInterfaceFactory(userInterfaceFactory);
 displayableListPanel.setSessionProperties(sessionProperties);
 displayableListPanel.setParentForm(parentForm);

 when(userInterfaceFactory.createButton(anyString())).thenReturn(selectButton);
 when(userInterfaceFactory.createButton(anyString())).thenReturn(addButton);
 when(userInterfaceFactory.createButton(anyString())).thenReturn(editButton);
 when(userInterfaceFactory.createButton(anyString())).thenReturn(viewButton);
 when(userInterfaceFactory.createButton(anyString())).thenReturn(deleteButton);
 }

 @Test
 public void testConstructor() {
 DisplayableListPanel displayableListPanel = new DisplayableListPanel(new SessionProperties(), new DisplayableListParentForm(), "Test Panel", true);
 assertNotNull(displayableListPanel);
 }

 @Test
 public void testSetNewRecord() {
 displayableListPanel.setNewRecord(true);
 assertFalse(displayableListPanel.isNewRecord());
 displayableListPanel.setNewRecord(false);
 assertTrue(displayableListPanel.isNewRecord());
 }

 @Test
 public void testGetPanel() {
 assertNotNull(displayableListPanel.getPanel());
 }

 @Test
 public void testSetDisplayableListItemSelector() {
 DisplayableListItemSelector listItemSelector = mock(DisplayableListItemSelector.class);
 displayableListPanel.setDisplayableListItemSelector(listItemSelector);
 assertEquals(listItemSelector, displayableListPanel.getDisplayableListItemSelector());
 }

 @Test(expected = MacawException.class)
 public void testAddListItemMissingAdder() {
 displayableListPanel.addListItem();
 }

 @Test
 public void testEditSelectedListItemMissingEditor() throws MacawException {
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testViewSelectedListItemMissingViewer() throws MacawException {
 displayableListPanel.viewSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItemsMissingDeleter() throws MacawException {
 displayableListPanel.deleteSelectedListItems();
 }

 @Test(expected = MacawException.class)
 public void testEditSelectedListItemUnselectedItems() throws MacawException {
 displayableListPanel.editSelectedListItem();
 }

 @Test
 public void testDeleteSelectedListItems() throws MacawException {
 List<Displayable> itemsBeforeDeletion = displayableListPanel.getDisplayableList().getAllItems();
 displayableListPanel.deleteSelectedListItems();
 List<Displayable> itemsAfterDeletion = displayableListPanel.getDisplayableList().getAllItems();
 assertEquals(itemsBeforeDeletion.size() - 1, itemsAfterDeletion.size());
 }

 @Test
 public void testSelectListItemsEmptyList() throws MacawException {
 displayableListPanel.selectListItems();
 assertFalse(displayableListPanel.getDisplayableList().itemsAreSelected());
 }

 @Test
 public void testSelectListItemsSelectedItems() throws MacawException {
 List<Displayable> itemsBeforeSelection = displayableListPanel.getDisplayableList().getAllItems();
 displayableListPanel.selectListItems();
 List<Displayable> itemsAfterSelection = displayableListPanel.getDisplayableList().getAllItems();
 assertEquals(itemsBeforeSelection.size(), itemsAfterSelection.size());
 for (Displayable item : itemsAfterSelection) {
 assertTrue(item.isSelected());
 }
 }

 @Test(expected = MacawException.class)
 public void testDeleteSelectedListItemsNoConfirmation() throws MacawException {
 displayableListPanel.deleteSelectedListItems();
 }

 @Test
 public void testDeleteSelectedListItemsConfirmation() throws MacawException {
 List<Displayable> itemsBeforeDeletion = displayableListPanel.getDisplayableList().getAllItems();
 when(JOptionPane.showConfirmDialog(anyObject(), anyString(), anyString(), anyInt())).thenReturn(JOptionPane.YES_OPTION);
 displayableListPanel.deleteSelectedListItems();
 List<Displayable> itemsAfterDeletion = displayableListPanel.getDisplayableList().getAllItems();
 assertEquals(itemsBeforeDeletion.size() - 1, itemsAfterDeletion.size());
 }
}