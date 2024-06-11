package macaw.util;

import static org.junit.Assert.*;
import macaw.businessLayer.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class DisplayableListPanelTest {

 @InjectMocks
 private DisplayableListPanel displayableListPanel;
 private SessionProperties sessionProperties;
 private DisplayableListParentForm parentForm;
 private UserInterfaceFactory userInterfaceFactory;
 private DisplayableList displayableList;
 private JButton selectButton, addButton, editButton, viewButton, deleteButton;

 @Before
 public void setUp() {
 MockitoAnnotations.initMocks(this);
 sessionProperties = Mockito.mock(SessionProperties.class);
 parentForm = Mockito.mock(DisplayableListParentForm.class);
 userInterfaceFactory = Mockito.mock(UserInterfaceFactory.class);
 displayableList = Mockito.mock(DisplayableList.class);
 selectButton = Mockito.mock(JButton.class);
 addButton = Mockito.mock(JButton.class);
 editButton = Mockito.mock(JButton.class);
 viewButton = Mockito.mock(JButton.class);
 deleteButton = Mockito.mock(JButton.class);

 when(userInterfaceFactory.createButton(anyString())).thenReturn(Mockito.mock(JButton.class));
 when(sessionProperties.getLog()).thenReturn(Mockito.mock(Log.class));
 when(sessionProperties.getProperty("currentUser")).thenReturn(new User());

 displayableListPanel = new DisplayableListPanel(sessionProperties, parentForm, "Test Panel", true);
 displayableListPanel.init();
 displayableListPanel.buildUI();
 }

 @Test
 public void testInitialize() {
 assertNotNull(displayableListPanel.getPanel());
 assertNotNull(displayableListPanel.select);
 assertNotNull(displayableListPanel.add);
 assertNotNull(displayableListPanel.edit);
 assertNotNull(displayableListPanel.view);
 assertNotNull(displayableListPanel.delete);
 }

 @Test
 public void testSetDisplayableItems() {
 List<Displayable> masterList = new ArrayList<>();
 masterList.add(Mockito.mock(Displayable.class));
 masterList.add(Mockito.mock(Displayable.class));

 ArrayList<Displayable> currentList = new ArrayList<>();
 currentList.add(Mockito.mock(Displayable.class));
 currentList.add(Mockito.mock(Displayable.class));

 displayableList.setDisplayItems(masterList);
 displayableListPanel.displayableList = displayableList;
 displayableListPanel.setDisplayableItems(masterList);

 assertEquals(masterList, displayableListPanel.displayableList.getAllItems());
 }

 @Test
 public void testSave() {
 ArrayList<Displayable> masterList = new ArrayList<>();
 masterList.add(Mockito.mock(Displayable.class));
 masterList.add(Mockito.mock(Displayable.class));

 ArrayList<Displayable> currentList = new ArrayList<>();
 currentList.add(Mockito.mock(Displayable.class));
 currentList.add(Mockito.mock(Displayable.class));

 displayableList.setDisplayItems(currentList);
 displayableListPanel.displayableList = displayableList;

 displayableListPanel.save();

 assertEquals(currentList, displayableListPanel.masterList);
 }

 @Test
 public void testSetListOwnerName() {
 String listOwnerName = "Test Owner";
 displayableListPanel.setListOwnerName(listOwnerName);
 assertEquals(listOwnerName, displayableListPanel.listOwnerName);
 }

 @Test
 public void testActionPerformed() {
 ActionEvent actionEvent = Mockito.mock(ActionEvent.class);

 doNothing().when(parentForm).commitChanges();

 when(selectButton.doClick()).then(Mockito.doCallRealMethod());
 when(addButton.doClick()).then(Mockito.doCallRealMethod());
 when(editButton.doClick()).then(Mockito.doCallRealMethod());
 when(viewButton.doClick()).then(Mockito.doCallRealMethod());
 when(deleteButton.doClick()).then(Mockito.doCallRealMethod());

 displayableListPanel.select = selectButton;
 displayableListPanel.add = addButton;
 displayableListPanel.edit = editButton;
 displayableListPanel.view = viewButton;
 displayableListPanel.delete = deleteButton;

 displayableListPanel.actionPerformed(actionEvent);

 verify(selectButton).doClick();
 }
}