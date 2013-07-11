package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Joueur implements Serializable{
	private static final long serialVersionUID = 1L;
	public int nbPoints = 0;
	public int nbLignes = 0;
	public String nomJoueur;
	public ArrayList<Integer> listeScore = new ArrayList<Integer>();
	public boolean isGagnant = false;;
	public boolean peutJouer = true;
	private boolean dernierTourSupScore = false;
	private int scoreSup = -1;
	/**
	 * Les variables issues des paramètres
	 */
	private int nbPointsVictoire;
	private int nbLignesMax;
	private int scoreDepassement;
	
	public Joueur(String iStrNom, int nbPointsV, int nbLignesM, int scoreDepassement){
		this.nomJoueur = iStrNom;
		this.nbPointsVictoire=nbPointsV;
		this.nbLignesMax=nbLignesM;
		this.scoreDepassement=scoreDepassement;
	}
	
	public int ajouterScore(String score){
		int leScore = Integer.valueOf(score);
		if(leScore == 0){
			this.ajouterLigne();
		}else{
			this.resetLignes();
		}
		this.nbPoints += leScore;
		this.listeScore.add(Integer.valueOf(score));
		if(this.nbPoints>this.nbPointsVictoire){
			dernierTourSupScore = true;
			scoreSup = nbPoints;
			this.nbPoints = scoreDepassement;
		}
		if(this.nbPoints==this.nbPointsVictoire){
			this.isGagnant = true;
		}
		return this.nbPoints;
	}
	
	private void resetLignes() {
		this.nbLignes = 0;
	}

	public int ajouterScore(int score){
		this.nbPoints += score;
		this.listeScore.add(Integer.valueOf(score));
		if(this.nbPoints>this.nbPointsVictoire){
			dernierTourSupScore = true;
			scoreSup = nbPoints;
			this.nbPoints = scoreDepassement;
		}
		if(this.nbPoints==this.nbPointsVictoire){
			this.isGagnant = true;
		}
		return this.nbPoints;
	}
	
	public void ajouterLigne(){
		this.nbLignes++;		
		if(nbLignes == nbLignesMax){
			this.peutJouer = false;
		}
	}

	public void annulerScore() {
		if(listeScore.size() != 0){
			int scoreARetirer = listeScore.get(listeScore.size()-1);
			if(dernierTourSupScore){
				// Si le dernier coup était au dessus de 50, on retire du score au dessus de 50 le dernier coup
				this.nbPoints = scoreSup - scoreARetirer;
				dernierTourSupScore = false;
			}else{
				// On récupère la valeur à enlever
				this.nbPoints -= scoreARetirer;				
			}
			// Si le dernier score est une ligne, on l'enlève
			if(scoreARetirer == 0){
				if(nbLignes != nbLignesMax){
					this.peutJouer = true;
				}
				this.nbLignes--;
			}
			listeScore.remove(listeScore.size()-1);
			//Enfin, on recompte les lignes
			int tmpNbLignes = 0;
			for(int i = 0; i < this.listeScore.size(); i++){
				if(this.listeScore.get(i) == 0){
					tmpNbLignes++;
				}else{
					tmpNbLignes=0;
				}
			}
			this.nbLignes=tmpNbLignes;
			System.out.println(this.nbLignes);
		}
	}

	public CharSequence getListeScore() {
		String valRet = "";
		String tmp;
		for(int i = 0 ; i < listeScore.size() ; i++){
			if(listeScore.get(i) == 0){
				tmp = "X";
			}else{
				tmp = listeScore.get(i).toString();
			}
			valRet += tmp + " - ";
		}
		return valRet;
	}
	
	/*
	 * Méthodes de serializationB
	 */
	public int getNbPoints() {
		return nbPoints;
	}

	public void setNbPoints(int nbPoints) {
		this.nbPoints = nbPoints;
	}

	public int getNbLignes() {
		return nbLignes;
	}

	public void setNbLignes(int nbLignes) {
		this.nbLignes = nbLignes;
	}

	public String getNomJoueur() {
		return nomJoueur;
	}

	public void setNomJoueur(String nomJoueur) {
		this.nomJoueur = nomJoueur;
	}

	public boolean isGagnant() {
		return isGagnant;
	}

	public void setGagnant(boolean isGagnant) {
		this.isGagnant = isGagnant;
	}

	public boolean isPeutJouer() {
		return peutJouer;
	}

	public void setPeutJouer(boolean peutJouer) {
		this.peutJouer = peutJouer;
	}

	public boolean isDernierTourSupScore() {
		return dernierTourSupScore;
	}

	public void setDernierTourSupScore(boolean dernierTourSupScore) {
		this.dernierTourSupScore = dernierTourSupScore;
	}

	public int getScoreSup() {
		return scoreSup;
	}

	public void setScoreSup(int scoreSup) {
		this.scoreSup = scoreSup;
	}

	public int getNbPointsVictoire() {
		return nbPointsVictoire;
	}

	public void setNbPointsVictoire(int nbPointsVictoire) {
		this.nbPointsVictoire = nbPointsVictoire;
	}

	public int getNbLignesMax() {
		return nbLignesMax;
	}

	public void setNbLignesMax(int nbLignesMax) {
		this.nbLignesMax = nbLignesMax;
	}

	public int getScoreDepassement() {
		return scoreDepassement;
	}

	public void setScoreDepassement(int scoreDepassement) {
		this.scoreDepassement = scoreDepassement;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setListeScore(ArrayList<Integer> listeScore) {
		this.listeScore = listeScore;
	}
}
