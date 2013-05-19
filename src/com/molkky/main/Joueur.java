package com.molkky.main;

import java.util.ArrayList;

public class Joueur {
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
	
	void ajouterLigne(){
		this.nbLignes++;		
		if(nbLignes == nbLignesMax){
			this.peutJouer = false;
		}
	}

	public void annulerScore() {
		if(listeScore.size() != 0){
			int scoreARetirer = listeScore.get(listeScore.size()-1);
			if(dernierTourSupScore){
				this.nbPoints = scoreSup - scoreARetirer;
				dernierTourSupScore = false;
			}else{
				// On récupère la valeur à enlever
				this.nbPoints -= scoreARetirer;				
			}
			// Si le dernier score est une ligne, on l'enlève
			if(scoreARetirer == 0){
				if(nbLignes == nbLignesMax){
					this.peutJouer = true;
				}
				this.nbLignes--;
				
			}
			listeScore.remove(listeScore.size()-1);
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
}
