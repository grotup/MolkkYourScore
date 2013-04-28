package com.molkky.main;

public class PartieOptions {
	private int nbJoueur = 2;
	private int nbPointMax = 50;
	private int nbLignesMax = 3;
	private boolean finDePartiePremierJoueur = true;
	
	public int getNbJoueur() {
		return nbJoueur;
	}
	public void setNbJoueur(int nbJoueur) {
		this.nbJoueur = nbJoueur;
	}
	public int getNbPointMax() {
		return nbPointMax;
	}
	public void setNbPointMax(int nbPointMax) {
		this.nbPointMax = nbPointMax;
	}
	public int getNbLignesMax() {
		return nbLignesMax;
	}
	public void setNbLignesMax(int nbLignesMax) {
		this.nbLignesMax = nbLignesMax;
	}
	public boolean isFinDePartiePremierJoueur() {
		return finDePartiePremierJoueur;
	}
	public void setFinDePartiePremierJoueur(boolean finDePartiePremierJoueur) {
		this.finDePartiePremierJoueur = finDePartiePremierJoueur;
	}
}
