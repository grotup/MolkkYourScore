package test;

import org.junit.Test;

import model.Joueur;
import junit.framework.TestCase;

public class JoueurTest extends TestCase {

	@Test
	public void test1(){
		
		Joueur j = new Joueur("JoueurTest", 50, 3, 25);
		
		assertEquals("Premier test unitaire", "JoueurTest", j.getNomJoueur());
		assertEquals("Second test unitaire", 50, j.getNbPointsVictoire());
		assertEquals("Troisième test unitaire", 25, j.getScoreDepassement());
		assertEquals("Quatrième test unitaire", 3, j.getNbLignesMax());
	}
	
	@Test
	public void testAjouterScore(){
		
	}
}
