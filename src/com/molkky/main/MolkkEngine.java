package com.molkky.main;

import android.widget.Toast;

/**
 * Classe qui g�re la partie de M�lkky.
 * @author Sylvain
 *
 */
public class MolkkEngine {

	private Integer nbPointsVictoire;
	private Integer nbLignesMax;
	private Integer scoreDepassement;
	private JoueurListe listeJoueur;

	public MolkkEngine(Integer nbPointsVictoire, Integer nbLignes, Integer scoreDepassement){
		this.listeJoueur = new JoueurListe();
		this.nbPointsVictoire = nbPointsVictoire;
		this.nbLignesMax = nbLignes;
		this.scoreDepassement = scoreDepassement;
	}

	/**
	 * Fonction qui ajoute le score pass� en param�tre au joueur actuel
	 */
	public void ajouterScore(String score) {
		listeJoueur.getJoueurActuel().ajouterScore(score);
	}
}
