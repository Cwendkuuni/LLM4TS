package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jaw.metamodelo.Entidade;
import jaw.metamodelo.MetamodeloFactory;
import jaw.gui.ProcessarEntidades;
import jaw.salvar.Salvar;

public class SalvarTest {

 private File tempFile;
 private ProcessarEntidades form;
 private Salvar salvar;

 @Before
 public void setUp() {
 MetamodeloFactory factory = new MetamodeloFactory();
 Entidade entidade1 = factory.createEntidade();
 Entidade entidade2 = factory.createEntidade();
 List<Entidade> entidades = new ArrayList<>();
 entidades.add(entidade1);
 entidades.add(entidade2);
 form = new ProcessarEntidades(entidades);
 }

 @After
 public void tearDown() {
 if (tempFile != null && tempFile.exists()) {
 tempFile.delete();
 }
 }

 @Test
 public void testSalvarComTipoXml() {
 Salvar salvarXML = new Salvar(form, Salvar.SALVAR_XML);
 salvarXML.salvar();
 File file = new File(salvarXML.getArquivo());
 assertTrue(file.exists());
 }

 @Test
 public void testSalvarComTipoJaw() {
 Salvar salvarJaw = new Salvar(form, Salvar.SALVAR);
 salvarJaw.salvar();
 File file = new File(salvarJaw.getArquivo());
 assertTrue(file.exists());
 }

 @Test
 public void testSalvarComTipoJawComNomeArquivo() {
 Salvar salvarJaw = new Salvar(form, Salvar.SALVAR);
 salvarJaw.setArquivo("teste.jaw");
 salvarJaw.salvar();
 File file = new File("teste.jaw");
 assertTrue(file.exists());
 }

 @Test
 public void testSalvarComTipoXmlComNomeArquivo() {
 Salvar salvarXML = new Salvar(form, Salvar.SALVAR_XML);
 salvarXML.setArquivo("teste.jaw.xml");
 salvarXML.salvar();
 File file = new File("teste.jaw.xml");
 assertTrue(file.exists());
 }

 @Test
 public void testSalvarComTipoJawComDialogoNaoAberto() {
 Salvar salvarJaw = new Salvar(form, Salvar.SALVAR);
 salvarJaw.salvar();
 assertNull(salvarJaw.getArquivo());
 }

 @Test
 public void testSalvarComTipoJawSemEntidades() {
 Salvar salvarJaw = new Salvar(new ProcessarEntidades(new ArrayList<Entidade>()), Salvar.SALVAR);
 salvarJaw.salvar();
 assertFalse(new File(salvarJaw.getArquivo()).exists());
 }

 @Test
 public void testSalvarComTipoXmlComDialogoNaoAberto() {
 Salvar salvarXML = new Salvar(form, Salvar.SALVAR_XML);
 salvarXML.salvar();
 assertNull(salvarXML.getArquivo());
 }

 @Test(expected = NullPointerException.class)
 public void testSalvarComTipoXmlSemEntidades() {
 Salvar salvarXML = new Salvar(new ProcessarEntidades(new ArrayList<Entidade>()), Salvar.SALVAR_XML);
 salvarXML.salvar();
 }

 @Test
 public void testSalvarComNomeArquivoNulo() {
 Salvar salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setArquivo(null);
 salvar.salvar();
 assertNull(salvar.getArquivo());
 }

 @Test
 public void testSalvarComTipoJawComDialogoAbertoECancelado() {
 tempFile = new File("tempFile.jaw");
 Salvar salvarJaw = new Salvar(form, Salvar.SALVAR);
 JFileChooser fileChooser = salvarJaw.getFileChooser();
 fileChooser.setSelectedFile(new File("tempFile.jaw"));
 fileChooser.setDialogType(JFileChooser.SaveDialog);
 fileChooser.setApproveButtonText("Cancel");
 salvarJaw.salvar();
 assertFalse(new File(salvarJaw.getArquivo()).exists());
 }

 @Test
 public void testSalvarComTipoXmlComDialogoAbertoECancelado() {
 tempFile = new File("tempFile.jaw.xml");
 Salvar salvarXML = new Salvar(form, Salvar.SALVAR_XML);
 JFileChooser fileChooser = salvarXML.getFileChooser();
 fileChooser.setSelectedFile(new File("tempFile.jaw.xml"));
 fileChooser.setDialogType(JFileChooser.SaveDialog);
 fileChooser.setApproveButtonText("Cancel");
 salvarXML.salvar();
 assertFalse(new File(salvarXML.getArquivo()).exists());
 }

 @Test
 public void testSalvarComTipoJawComDialogoAbertoESalvado() {
 tempFile = new File("tempFile.jaw");
 Salvar salvarJaw = new Salvar(form, Salvar.SALVAR);
 JFileChooser fileChooser = salvarJaw.getFileChooser();
 fileChooser.setSelectedFile(new File("tempFile.jaw"));
 fileChooser.setDialogType(JFileChooser.SaveDialog);
 fileChooser.setApproveButtonText("Save");
 salvarJaw.salvar();
 File file = new File(salvarJaw.getArquivo());
 assertTrue(file.exists());
 }

 @Test
 public void testSalvarComTipoXmlComDialogoAbertoESalvado() {
 tempFile = new File("tempFile.jaw.xml");
 Salvar salvarXML = new Salvar(form, Salvar.SALVAR_XML);
 JFileChooser fileChooser = salvarXML.getFileChooser();
 fileChooser.setSelectedFile(new File("tempFile.jaw.xml"));
 fileChooser.setDialogType(JFileChooser.SaveDialog);
 fileChooser.setApproveButtonText("Save");
 salvarXML.salvar();
 File file = new File(salvarXML.getArquivo());
 assertTrue(file.exists());
 }
}