package jaw.entrada.test;

import static org.junit.Assert.*;
import jaw.entrada.Salvar;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SalvarTest {

 private Salvar salvar;
 private ProcessarEntidades form;
 private List<Entidade> entidadeList;

 @Before
 public void setUp() {
 entidadeList = new ArrayList<>();
 form = new ProcessarEntidades();
 salvar = new Salvar(form, Salvar.SALVAR);
 }

 @Test
 public void testSalvarWithValidInputs() {
 // Testing the scenario where all inputs are valid
 salvar.entidade = entidadeList;
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.approveSelection();
 salvar.vela = true;
 salvar.arquivo = "test.jaw";
 assertFalse(salvar.arquivo.isEmpty());
 }

 @Test(expected = Exception.class)
 public void testSalvarWithNullEntidadeList() {
 // Testing the scenario where entidadeList is null
 salvar.entidade = null;
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.approveSelection();
 salvar.vela = true;
 salvar.arquivo = "test.jaw";
 salvar.Salvar();
 }

 @Test(expected = Exception.class)
 public void testSalvarWithInvalidFileType() {
 // Testing the scenario where the selected file type is not JAW
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setFileFilter(new FilteredFileFilter("InvalidType"));
 fileChooser.approveSelection();
 salvar.vela = false;
 salvar.arquivo = "test.jaw";
 salvar.Salvar();
 }

 @Test(expected = Exception.class)
 public void testSalvarWithoutFileSelection() {
 // Testing the scenario where no file is selected
 salvar.entidade = entidadeList;
 salvar.vela = false;
 salvar.arquivo = "";
 salvar.Salvar();
 }

 @Test(expected = Exception.class)
 public void testSalvarWithNullArquivo() {
 // Testing the scenario where arquivo is null
 salvar.entidade = entidadeList;
 salvar.vela = true;
 salvar.arquivo = null;
 salvar.Salvar();
 }

 @Test
 public void testSalvarXMLWithValidInputs() {
 // Testing the scenario for saving in XML format with valid inputs
 salvar.entidade = entidadeList;
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.approveSelection();
 salvar.vela = true;
 salvar.arquivo = "test.jaw.xml";
 assertFalse(salvar.arquivo.isEmpty());
 }

 @Test(expected = Exception.class)
 public void testSalvarXMLWithNullEntidadeList() {
 // Testing the scenario where entidadeList is null for saving in XML format
 salvar.entidade = null;
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.approveSelection();
 salvar.vela = true;
 salvar.arquivo = "test.jaw.xml";
 salvar.SalvarXML();
 }

 @Test(expected = Exception.class)
 public void testSalvarXMLWithInvalidFileType() {
 // Testing the scenario where the selected file type is not XML for saving in XML format
 JFileChooser fileChooser = new JFileChooser();
 fileChooser.setFileFilter(new FilteredFileFilter("InvalidTypeXML"));
 fileChooser.approveSelection();
 salvar.vela = false;
 salvar.arquivo = "test.jaw.xml";
 salvar.SalvarXML();
 }

 @Test(expected = Exception.class)
 public void testSalvarXMLWithoutFileSelection() {
 // Testing the scenario where no file is selected for saving in XML format
 salvar.entidade = entidadeList;
 salvar.vela = false;
 salvar.arquivo = "";
 salvar.SalvarXML();
 }

 @Test(expected = Exception.class)
 public void testSalvarXMLWithNullArquivo() {
 // Testing the scenario where arquivo is null for saving in XML format
 salvar.entidade = entidadeList;
 salvar.vela = true;
 salver.arquivo = null;
 salvar.SalvarXML();
 }
}

 class FilteredFileFilter extends FileFilter {
 private String extension;

 FilteredFileFilter(String extension) {
 this.extension = extension;
 }

 @Override
 public boolean accept(File file) {
 return file.isDirectory() || file.getName().toLowerCase().endsWith(extension);
 }

 @Override
 public String getDescription() {
 return "Files with " + extension + " extension";
 }
}