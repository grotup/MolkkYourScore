package com.molkky.main;

import android.widget.Toast;

/**
 * Classe qui gère la partie de Mölkky.
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
	 * Fonction qui ajoute le score passé en paramètre au joueur actuel
	 */
	public void ajouterScore(String score) {
		listeJoueur.getJoueurActuel().ajouterScore(score);
	}
}
