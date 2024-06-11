package jaw.entrada;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jaw.gui.*; // Importing for the Filtro and Filtro2 classes
import jaw.metamodelo.*;
import jaw.*;

public class SalvarTest {

 private ProcessarEntidades form;
 private Salvar salvar;
 private List<Entidade> entidades;

 @Before
 public void setUp() {
 entidades = new ArrayList<>();
 form = new ProcessarEntidades();
 }

 @Test
 public void testSalvarComEntidades() {
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1"));
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.setEntidades(entidades);
 // Test the save logic for XML files
 salvar.salvar();
 File file = new File(salvar.getArquivo());
 assertTrue(file.exists());
 }

 @Test(expected = Exception.class)
 public void testSalvarSemEntidades() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvar();
 }

 @Test
 public void testSalvarSemArquivo() {
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1"));
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.setEntidades(entidades);
 salvar.setArquivo("");
 salvar.salvar();
 }

 @Test
 public void testSalvarComNomeInvalido() {
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1"));
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.setEntidades(entidades);
 salvar.setArquivo("Teste.txt");
 salvar.salvar();
 }

 @Test
 public void testSalvarComErroSerializacao() {
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1"));
 salvar = new Salvar(form, Salvar.SALVAR);
 salvar.setEntidades(entidades);
 salvar.setArquivo("Teste.jaw");
 salvar.salvar();
 }

 @Test
 public void testSalvarComEntidadesVazias() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.salvar();
 }

 @Test
 public void testSalvarComArquivoExistente() {
 File file = new File("Teste.jaw.xml");
 file.createNewFile();
 entidades.add(new Entidade("Teste1", "Teste1", "Teste1"));
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.setEntidades(entidades);
 salvar.setArquivo("Teste.jaw.xml");
 salvar.salvar();
 File newFile = new File(salvar.getArquivo());
 assertFalse(file.exists());
 assertTrue(newFile.exists());
 }

 @Test(expected = FileNotFoundException.class)
 public void testAbrirArquivoInvalido() {
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.setArquivo("Teste.txt");
 salvar.abrirArquivo();
 }

 @Test
 public void testAbrirArquivoValido() {
 File file = new File("Teste.jaw.xml");
 file.createNewFile();
 salvar = new Salvar(form, Salvar.SALVAR_XML);
 salvar.setArquivo("Teste.jaw.xml");
 salvar.abrirArquivo();
 assertNotNull(salvar.getEntidades());
 }

}