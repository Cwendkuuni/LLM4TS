package jaw.entrada.test;

import static org.junit.Assert.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import jaw.entrada.Entidade;
import jaw.entrada.ProcessarEntidades;
import jaw.entrada.Salvar;
import jaw.metamodelo.Atributo;
import jaw.metamodelo.Classe;
import jaw.metamodelo.Relacao;
import jaw.metamodelo.Tipo;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 form = new ProcessarEntidades();
 salvar = new Salvar(form, 20);

 Classe classe1 = new Classe("Classe1", new ArrayList<Atributo>(), new ArrayList<Relacao>(), new ArrayList<Tipo>());
 Atributo atributo1 = new Atributo("atributo1", new Tipo("int"), "Descricao1");
 classe1.getListaAtributos().add(atributo1);

 Classe classe2 = new Classe("Classe2", new ArrayList<Atributo>(), new ArrayList<Relacao>(), new ArrayList<Tipo>());
 Atributo atributo2 = new Atributo("atributo2", new Tipo("String"), "Descricao2");
 classe2.getListaAtributos().add(atributo2);

 entidades = new ArrayList<Entidade>();
 entidades.add(new Entidade("Entidade1.jaw", classe1));
 entidades.add(new Entidade("Entidade2.jaw", classe2));
 form.setEntidades(entidades);
 }

 @Test
 public void testSalvar() throws IOException {
 File directory = new File(Main.configuracao.getCaminhoDosProjetos());
 File file = new File(directory.getPath() + File.separator + "test.jaw");
 salvar.salvar(form.getEntidades(), file.getAbsolutePath());
 assertTrue(file.exists());
 File tempFile = new File(file.getAbsolutePath() + ".bak");
 assertFalse(tempFile.exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileChooserNull() {
 salvar.salvar(form, 30);
 }

 @Test(expected = NullPointerException.class)
 public void testFileChooserNullFile() {
 salvar = new Salvar(form, 20);
 salvar.setVela(true);
 salvar.setArquivo(null);
 salvar.salvar(form.getEntidades(), new File(""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFileChooserInvalidFileType() {
 salvar = new Salvar(form, 20);
 salvar.setVela(true);
 salvar.setArquivo(new File("test.txt"));
 salvar.salvar(form.getEntidades(), salvar.getArquivo());
 }

 @Test
 public void testSalvarXML() throws IOException {
 File directory = new File(Main.configuracao.getCaminhoDosProjetos());
 File file = new File(directory.getPath() + File.separator + "test.jaw.xml");
 salvar.salvarXML(form.getEntidades(), file.getAbsolutePath());
 assertTrue(file.exists());
 File tempFile = new File(file.getAbsolutePath() + ".bak");
 assertFalse(tempFile.exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testFileChooserNullForXML() {
 salvar.salvarXML(form.getEntidades(), "");
 }

 @Test(expected = NullPointerException.class)
 public void testFileChooserNullFileForXML() {
 salvar = new Salvar(form, 10);
 salvar.salvarXML(form.getEntidades(), null);
 }
}