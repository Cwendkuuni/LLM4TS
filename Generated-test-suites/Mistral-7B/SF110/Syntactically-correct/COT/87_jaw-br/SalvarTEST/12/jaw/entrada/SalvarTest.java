package jaw.entrada.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import jaw.entrada.*;
import jaw.metamodelo.*;
import jaw.gui.*;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private List<Entidade> entidades;
 private Entidade entidade;

 @Before
 public void setUp() {
 entidade = new Entidade();
 entidades = new ArrayList<>();
 entidades.add(entidade);
 form = new ProcessarEntidades();
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 }

 @Test
 public void testSalvarWithValidData() {
 // Testing the constructor with valid data
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 assertEquals("", salvar.arquivo);
 assertNull(salvar.xml);
 assertNull(salvar.entidade);

 // Testing the method when file is selected and XML format is chosen
 JFileChooser fileChooser = new JFileChooser();
 File file = new File("test.jaw.xml");
 fileChooser.setSelectedFile(file);
 salvar.vela = true;
 salvar.arquivo = file.getAbsolutePath();
 salvar.save();
 assertTrue(salvar.xml != null);
 try {
 File fileXML = new File(salvar.arquivo);
 FileReader reader = new FileReader(fileXML);
 StringBuilder xmlContent = new StringBuilder();
 String line;
 while ((line = reader.readLine()) != null) {
 xmlContent.append(line);
 }
 reader.close();
 assertEquals(entidades.toString(), xmlContent.toString().replaceAll("\\[", "").replaceAll("]", ""));
 } catch (IOException e) {
 e.printStackTrace();
 }

 // Testing the method when no file is selected and name is empty
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.save();
 assertNull(salvar.xml);
 assertNull(salvar.arquivo);
 }

 @Test(expected = Exception.class)
 public void testSaveWithNullEntidades() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.entidade = null;
 salvar.save();
 }

 @Test
 public void testSaveWithInvalidFileFilter() {
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
 @Override
 public boolean accept(File f) {
 return f.isDirectory();
 }

 @Override
 public String getDescription() {
 return "Invalid Filter";
 }
 });
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.vela = true;
 salvar.save();
 assertFalse(salvar.vela);
 }

 @Test
 public void testSaveWithEmptyFileName() {
 JFileChooser fileChooser = new JFileChooser();
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.vela = false;
 salvar.arquivo = "";
 salvar.save();
 assertNull(salvar.xml);
 assertNull(salvar.arquivo);
 }

 @Test(expected = IOException.class)
 public void testSaveWithXMLFormatAndInvalidFile() {
 JFileChooser fileChooser = new JFileChooser();
 File file = new File("test.txt");
 fileChooser.setSelectedFile(file);
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.vela = true;
 salvar.arquivo = file.getAbsolutePath();
 salvar.save();
 }

 @Test
 public void testSaveWithJawFormatAndInvalidFile() {
 JFileChooser fileChooser = new JFileChooser();
 File file = new File("test.txt");
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.vela = true;
 salvar.arquivo = file.getAbsolutePath();
 salvar.save();
 assertFalse(salvar.vela);
 }

 @Test(expected = IOException.class)
 public void testSaveWithInvalidFormatAndFile() {
 JFileChooser fileChooser = new JFileChooser();
 File file = new File("test.jaw");
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.vela = true;
 salvar.arquivo = file.getAbsolutePath();
 salvar.save();
 }

 @Test
 public void testSaveWithValidFormatAndInvalidFile() {
 JFileChooser fileChooser = new JFileChooser();
 File file = new File("test.txt");
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.vela = true;
 salvar.arquivo = file.getAbsolutePath();
 salvar.save();
 assertFalse(salvar.vela);
 }

 @Test
 public void testSaveWithValidFormatAndValidFile() {
 JFileChooser fileChooser = new JFileChooser();
 File file = new File("test.jaw");
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.vela = true;
 salvar.arquivo = file.getAbsolutePath();
 salvar.save();
 assertTrue(salvar.vela);
 }

 @Test(expected = IOException.class)
 public void testSaveWithInvalidFormatAndValidFile() {
 JFileChooser fileChooser = new JFileChooser();
 File file = new File("test.jaw.xml");
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.vela = true;
 salvar.arquivo = file.getAbsolutePath();
 salvar.save();
 }

 @Test
 public void testSaveWithValidFormatAndValidFile() {
 JFileChooser fileChooser = new JFileChooser();
 File file = new File("test.jaw.xml");
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.vela = true;
 salvar.arquivo = file.getAbsolutePath();
 salvar.save();
 assertTrue(salvar.vela);
 }
}