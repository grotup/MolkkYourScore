package test;

import static org.junit.Assert.*;

import model.Joueur;

import org.junit.Test;

public class TestJoueur {

	@Test
	public void testAjouterScoreNominal(){
		Joueur j1 = new Joueur("Joueur 1", 50, 3, 25);
		int nbCoupsAvantJ1 = j1.listeScore.size();
		j1.ajouterScore(12);		
		
		assertTrue(j1.peutJouer);
		assertFalse(j1.isGagnant);
		assertFalse(j1.isDernierTourSupScore());
		assertEquals(12,j1.nbPoints);
		assertEquals(0, j1.nbLignes);
		assertEquals(nbCoupsAvantJ1+1, j1.listeScore.size());	
	}
	
	@Test
	public void testAjouterScoreDepassement(){
		Joueur j2 = new Joueur("Joueur 2", 50, 3, 25);
		
		j2.nbPoints = 45;
		int nbLignesAvant = j2.nbLignes;
		j2.ajouterScore(6);
		assertEquals("Ajout de score > score max - 1", j2.getScoreDepassement(), j2.nbPoints);
		assertEquals("Ajout de score > score max - 3", nbLignesAvant, j2.nbLignes);
	}
	
	@Test 
	public void testAjouterScoreZero(){
		Joueur j3 = new Joueur("Joueur 3", 50, 3, 25);
		j3.ajouterScore(0);
		assertEquals("Test - Joueur fait ligne -", 1, j3.nbLignes);
	}
	
	@Test
	public void testAjouterScoreZeroMax(){
		Joueur j4 = new Joueur("Joueur 4", 50, 3, 25);		

		j4.nbLignes = j4.getNbLignesMax()-1;
		j4.ajouterScore(0);
		assertEquals("Test - Joueur fait 0, nombre de ligne max", j4.getNbLignesMax(), j4.nbLignes);
	}
	
	@Test
	public void testAjouterScoreJoueurGagnant() {
		// score après => score avant + score
		// score après > scoreMax => scoreDepassement
		// score = 0 => ligne ++
		// score = 0 && ligne = 2 => joueurPerdu
		// score après = score victoire => joueur gagnant
		Joueur j5 = new Joueur("Joueur 5", 50, 3, 25);
		
		j5.nbPoints = 45;
		j5.ajouterScore(5);
		assertEquals("Test - Joueur gagnant ", j5.getNbPointsVictoire(), j5.nbPoints);
	}

	@Test
	public void testAjouterLigne() {
		
	}

	@Test
	public void testAnnulerScore() {
		
	}

	@Test
	public void testIsGagnant() {
		
	}

}
