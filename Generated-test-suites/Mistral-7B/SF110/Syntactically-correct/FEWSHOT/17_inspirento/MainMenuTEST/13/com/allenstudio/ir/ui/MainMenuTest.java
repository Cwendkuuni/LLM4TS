package com.allenstudio.ir.ui;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;
import java.util.*;

public class MainMenuTest {
    @Test
    public void testMainMenuCreation() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);
        MainMenu mainMenu = new MainMenu(mediator);
        assertNotNull(mainMenu);
    }

    @Test(expected = MissingResourceException.class)
    public void testCannotLoadMenuLabel() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);
        new MainMenu(mediator);
    }

    @Test
    public void testCreateMenu() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);

        String[] menus = {"File", "Edit", "Help"};
        when(UIResources.getString("menu")).thenReturn(String.join(" ", menus));

        MainMenu mainMenu = new MainMenu(mediator);

        JMenu fileMenu = mainMenu.createMenu("File");
        assertEquals("File", fileMenu.getText());

        String[] fileItems = {"New", "Open", "Save", "Save As", "Export", "Import", "Exit"};
        String[] fileLabels = {"NewLabel", "OpenLabel", "SaveLabel", "SaveAsLabel", "ExportLabel", "ImportLabel", "ExitLabel"};

        for (int i = 0; i < fileItems.length; i++) {
            String item = fileItems[i];
            JMenuItem jMenuItem = null;

            if (item.equals("-")) {
                jMenuItem = fileMenu.addSeparator();
                continue;
            }

            if (item.startsWith("#")) {
                String key = item.substring(1);
                String label = UIResources.getString(key + "Label");
                JCheckBoxMenuItem jMenuItem2 = (JCheckBoxMenuItem) fileMenu.add(new JCheckBoxMenuItem(label));
                assertEquals(label.substring(0, label.length() - 2), jMenuItem2.getText());
                assertEquals(key, jMenuItem2.getName());
            } else {
                String label = UIResources.getString(item + "Label");
                jMenuItem = (JMenuItem) fileMenu.add(new JMenuItem(label));
                assertEquals(label.substring(0, label.length() - 2), jMenuItem.getText());
            }

            assertNull(jMenuItem.getIcon());
            assertNull(jMenuItem.getAccelerator());
        }
    }

    @Test
    public void testAddShortcutAndIcon() {
        InspirentoMediator mediator = mock(InspirentoMediator.class);

        String[] menus = {"File", "Edit", "Help"};
        when(UIResources.getString("menu")).thenReturn(String.join(" ", menus));

        Map<String, String> resources = new HashMap<>();
        resources.put("NewShortcut", "Ctrl+N");
        resources.put("NewLabel", "New");
        resources.put("OpenShortcut", "Ctrl+O");
        resources.put("OpenLabel", "Open");
        resources.put("SaveShortcut", "Ctrl+S");
        resources.put("SaveAsLabel", "Save As");
        resources.put("ExportShortcut", "Ctrl+E");
        resources.put("ExportLabel", "Export");
        resources.put("ImportShortcut", "Ctrl+I");
        resources.put("ImportLabel", "Import");
        resources.put("ExitShortcut", "Alt+F4");
        resources.put("ExitLabel", "Exit");
        resources.put("FileIcon", "file.png");
        resources.put("EditIcon", "edit.png");
        resources.put("HelpIcon", "help.png");

        when(UIResources.getString(eq("menu"))).thenReturn(String.join(" ", menus));
        when(UIResources.getString(eq(anyString()))).thenAnswer((Answer<String>)(invocation) -> resources.get(invocation.getArgument(0)));

        MainMenu mainMenu = new MainMenu(mediator);
        JMenu fileMenu = mainMenu.createMenu("File");

        for (String key : resources.keySet()) {
            if (key.startsWith("File")) {
                JMenuItem item = (JMenuItem) fileMenu.findComponentAt(0, 0);
                assertEquals(KeyEvent.VK_N, item.getAccelerator().getKeyCode());
                assertEquals("file.png", item.getIcon().toString());
                break;
            }
        }
    }
}