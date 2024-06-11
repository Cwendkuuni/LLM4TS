package com.allenstudio.ir.ui;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.util.*;

public class MainMenuTest {

    @Test
    public void testMainMenuConstruction() {
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu mainMenu = new MainMenu(mediator);
        assertNotNull(mainMenu);
    }

    @Test
    public void testCreateMenuWithLabel() {
        String menuLabel = "File";
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu mainMenu = new MainMenu(mediator);
        JMenu fileMenu = mainMenu.createMenu(menuLabel);
        String expectedLabel = "File";
        assertEquals(expectedLabel, fileMenu.getText());
    }

    @Test
    public void testCreateMenuWithLabelAndMnemonic() {
        String menuLabel = "Edit*";
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu mainMenu = new MainMenu(mediator);
        JMenu editMenu = mainMenu.createMenu(menuLabel);
        String expectedLabel = "Edit";
        assertEquals(expectedLabel, editMenu.getText());
        assertEquals('E', editMenu.getMnemonic());
    }

    @Test
    public void testCreateMenuWithLabelAndSubmenu() {
        String menuLabel = "Help";
        String subMenuLabel = "Submenu";
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu mainMenu = new MainMenu(mediator);
        JMenu helpMenu = mainMenu.createMenu(menuLabel);
        JMenu submenu = mainMenu.createMenu(subMenuLabel);
        helpMenu.add(submenu);

        String expectedLabel = "Help";
        assertEquals(expectedLabel, helpMenu.getText());

        String subExpectedLabel = "Submenu";
        assertEquals(subExpectedLabel, submenu.getText());
    }

    @Test
    public void testCreateMenuWithLabelAndCheckboxMenuItem() {
        String menuLabel = "Options";
        String itemLabel = "Show Grid*";
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu mainMenu = new MainMenu(mediator);
        JMenu optionsMenu = mainMenu.createMenu(menuLabel);
        JCheckBoxMenuItem checkboxMenuItem = mainMenu.createCheckboxMenuItem(itemLabel);
        optionsMenu.add(checkboxMenuItem);

        String expectedLabel = "Options";
        assertEquals(expectedLabel, optionsMenu.getText());

        String expectedCheckboxLabel = "Show Grid";
        assertEquals(expectedCheckboxLabel, checkboxMenuItem.getText());
        assertTrue(checkboxMenuItem.isSelected());
    }

    @Test
    public void testCreateMenuWithLabelAndJMenuItem() {
        String menuLabel = "Help";
        String itemLabel = "About";
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu mainMenu = new MainMenu(mediator);
        JMenu helpMenu = mainMenu.createMenu(menuLabel);
        JMenuItem aboutMenuItem = mainMenu.createMenuItem(itemLabel);
        helpMenu.add(aboutMenuItem);

        String expectedLabel = "Help";
        assertEquals(expectedLabel, helpMenu.getText());

        String expectedItemLabel = "About";
        assertEquals(expectedItemLabel, aboutMenuItem.getText());
    }

    @Test
    public void testAddShortcutAndIcon() {
        String key = "FileNew";
        InspirentoMediator mediator = new InspirentoMediator();
        MainMenu mainMenu = new MainMenu(mediator);
        JMenuItem newMenuItem = mainMenu.createMenuItem(key);

        String expectedShortcut = "Ctrl+N";
        assertEquals(expectedShortcut, newMenuItem.getAccelerator().toString());

        String expectedIconPath = "images/new.png";
        UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
        for (UIManager.LookAndFeelInfo lookAndFeelInfo : info) {
            try {
                UIManager.setLookAndFeel(lookAndFeelInfo.getClassName());
            } catch (Exception e) {
                // ignore
            }
            BufferedImage icon = (BufferedImage) mainMenu.getIcon(newMenuItem);
            if (icon != null) {
                String imagePath = new File(new File(".").getAbsolutePath(), expectedIconPath).getAbsolutePath();
                File imageFile = new File(imagePath);
                ImageIO.checkIOException(new FileInputStream(imageFile), "file not found");
                BufferedImage expectedIcon = ImageIO.read(imageFile);
                assertEquals(expectedIcon.getWidth(), icon.getWidth());
                assertEquals(expectedIcon.getHeight(), icon.getHeight());
                assertEquals(expectedIcon.getType(), icon.getType());
                break;
            }
        }
    }
}