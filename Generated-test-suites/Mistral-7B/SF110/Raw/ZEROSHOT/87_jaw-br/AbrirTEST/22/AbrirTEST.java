package jaw.entrada.test;

import static org.junit.Assert.*;

import java.awt.FileDialog;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Abrir;
import jaw.entrada.ProcessarEntidades;
import jaw.gui.Main;
import jaw.serializacao.Serializacao;
import jaw.serializacao.SerializacaoXML;

public class AbrirTest {

 private ProcessarEntidades form;
 private Abrir abrir;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 }

 @Test
 public void testAbrirXMLWithValidFile() throws IOException {
 String xmlFile = "teste.jaw.xml";
 abrir = new Abrir(form, xmlFile);
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithInvalidFile() {
 String invalidFile = "teste.txt";
 abrir = new Abrir(form, invalidFile);
 fail();
 }

 @Test
 public void testAbrirSerializadoWithValidFile() {
 String jawFile = "teste.jaw";
 abrir = new Abrir(form, 20, new File(jawFile));
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirSerializadoWithInvalidFile() {
 String invalidFile = "teste.txt";
 abrir = new Abrir(form, 20, new File(invalidFile));
 fail();
 }

 @Test
 public void testOpenFileChooserWithXMLFilter() {
 JFileChooser fileChooser = new JFileChooser();
 FileFilter xmlFilter = new Filtro2();
 fileChooser.setFileFilter(xmlFilter);
 File selectedFile = new File("teste.jaw.xml");
 fileChooser.setSelectedFile(selectedFile);
 int returnValue = fileChooser.showOpenDialog(null);
 assertEquals(JFileChooser.APPROVE_OPTION, returnValue);
 }

 @Test
 public void testOpenFileChooserWithJawFilter() {
 JFileChooser fileChooser = new JFileChooser();
 FileFilter jawFilter = new Filtro();
 fileChooser.setFileFilter(jawFilter);
 File selectedFile = new File("teste.jaw");
 fileChooser.setSelectedFile(selectedFile);
 int returnValue = fileChooser.showOpenDialog(null);
 assertEquals(JFileChooser.APPROVE_OPTION, returnValue);
 }

 @Test
 public void testAbrirXML() throws IOException {
 String xmlFile = "teste.jaw.xml";
 abrir = new Abrir(form, xmlFile);
 assertTrue(form.getEntidades() != null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAbrirXMLWithInvalidXMLFile() throws IOException {
 String invalidXMLFile = "teste.txt";
 abrir = new Abrir(form, invalidXMLFile);
 fail();
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirSerializadoWithInvalidFile() throws IOException {
 String invalidJawFile = "teste.jawx";
 abrir = new Abrir(form, 20, new File(invalidJawFile));
 fail();
 }

 @Test
 public void testAbrirSerializado() throws IOException {
 String jawFile = "teste.jaw";
 abrir = new Abrir(form, 20, new File(jawFile));
 assertTrue(form.getEntidades() != null);
 }
}

class Filtro2 extends FileFilter {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw.xml");
 }

 @Override
 public String getDescription() {
 return "XML JAW files";
 }
}

class Filtro extends FileFilter {
 @Override
 public boolean accept(File f) {
 return f.isDirectory() || f.getName().toLowerCase().endsWith(".jaw");
 }

 @Override
 public String getDescription() {
 return "JAW files";
 }
}