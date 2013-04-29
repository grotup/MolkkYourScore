package com.molkky.main;

import java.util.ArrayList;

public class Joueur {
	public int nbPoints = 0;
	public int nbLignes = 0;
	public String nomJoueur;
	public ArrayList<Integer> listeScore = new ArrayList<Integer>();
	public boolean isGagnant = false;;
	public boolean peutJouer = true;
	
	public Joueur(String iStrNom){
		this.nomJoueur = iStrNom;
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
		if(this.nbPoints>50){
			this.nbPoints = 25;
		}
		if(this.nbPoints==50){
			this.isGagnant = true;
		}
		return this.nbPoints;
	}
	
	private void resetLignes() {
		this.nbLignes = 0;
	}

	public int ajouterScore(int score){
		this.nbPoints += score;
		return this.nbPoints;
	}
	
	private void ajouterLigne(){
		this.nbLignes++;		
	}

	public void annulerScore() {
		if(listeScore.size() != 0){
			// On récupère la valeur à enlever
			int scoreARetirer = listeScore.get(listeScore.size()-1);
			this.nbPoints -= scoreARetirer;
			listeScore.remove(listeScore.size()-1);
		}
	}
}
