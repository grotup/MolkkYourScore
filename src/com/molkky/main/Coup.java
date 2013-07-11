package com.molkky.main;

/**
 * Classe représentant un lancer de Molkky
 * Contient un index de joueur (index dans l'array de joueurs pour l'instant) et une valeur
 * @author Sylvain
 *
 */
public class Coup {
	private int leJoueur;
	private int valeurCoup;
	
	public Coup(int nomJoueur, int valeurCoup){
		this.leJoueur = nomJoueur;
		this.valeurCoup = valeurCoup;
	}
	
	public int getleJoueur() {
		return leJoueur;
	}
	public void setleJoueur(int leJoueur) {
		this.leJoueur = leJoueur;
	}
	public int getValeurCoup() {
		return valeurCoup;
	}
	public void setValeurCoup(int valeurCoup) {
		this.valeurCoup = valeurCoup;
	}
	
}
