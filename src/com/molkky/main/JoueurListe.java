package com.molkky.main;

import java.util.ArrayList;

public class JoueurListe{
	
	private ArrayList<Joueur> lesJoueurs;
	private int indexJoueurActuel;
	/**
	 * Les variables issues des paramètres
	 */
	private int nbPointsVictoire;
	private int nbLignesMax;
	private int scoreDepassement;
	
	public JoueurListe(){
		this.lesJoueurs = new ArrayList<Joueur>();
		this.indexJoueurActuel = 0;
	}
	
	public Joueur getJoueurActuel(){
		return this.lesJoueurs.get(indexJoueurActuel);
	}
	
	public int getJoueurActuelIndex(){
		return this.indexJoueurActuel;
	}
	
	public void setIndexJoueurActuel(int index){
		this.indexJoueurActuel = index;
	}
	
	public Joueur next(){
		if(indexJoueurActuel < lesJoueurs.size()-1){
			indexJoueurActuel++;
			while(lesJoueurs.get(indexJoueurActuel).peutJouer == false){
				if(lesJoueurs.size() -1 == indexJoueurActuel){
					indexJoueurActuel = 0;
				}else{
					indexJoueurActuel++;
				}
			}
			return lesJoueurs.get(indexJoueurActuel);
		}else{
			indexJoueurActuel = 0;
			while(lesJoueurs.get(indexJoueurActuel).peutJouer == false){
				if(lesJoueurs.size() -1 == indexJoueurActuel){
					indexJoueurActuel = 0;
				}else{
					indexJoueurActuel++;
				}
			}
			return lesJoueurs.get(0);
		}
	}
	
	public Joueur addJoueur(Joueur j){
		this.lesJoueurs.add(j);
		return this.lesJoueurs.get(this.lesJoueurs.size()-1);
	}
	
	// Fonction qui retourne le joueur qui a joué avant.
	// Si l'iterator est à 0, on renvoie le dernier de la liste, et on fait boucler un nouvel iterator jusqu'au dernier
	public Joueur getJoueurAvant(){
		if(indexJoueurActuel != 0){
			indexJoueurActuel--;
			return lesJoueurs.get(indexJoueurActuel);
		}else{
			indexJoueurActuel = lesJoueurs.size() -1;
			return lesJoueurs.get(indexJoueurActuel);
		}
	}
	
	public int size(){
		return lesJoueurs.size();
	}
	
	public Joueur getJoueur(int index){
		return lesJoueurs.get(index);
	}

	public void ResetScoreTousJoueurs() {
		// On réinitialise toutes les valeurs
		for(int i = 0 ; i < lesJoueurs.size() ; i++){
			lesJoueurs.get(i).nbLignes = 0;
			lesJoueurs.get(i).nbPoints = 0;
			lesJoueurs.get(i).listeScore = new ArrayList<Integer>();
			lesJoueurs.get(i).isGagnant = false;
			lesJoueurs.get(i).peutJouer = true;
		}
		// Et on décale l'ordre
		ArrayList<Joueur> tmp = new ArrayList<Joueur>();
		for(int i = 1 ; i < lesJoueurs.size(); i++){
			tmp.add(lesJoueurs.get(i));
		}
		tmp.add(lesJoueurs.get(0));
		this.lesJoueurs = tmp;
		indexJoueurActuel = 0;
	}

	public void ResetJoueurs() {
		this.lesJoueurs = new ArrayList<Joueur>();
	}
	
	public boolean partieCommencee(){
		boolean valRet = false;
		for(int i = 0 ; i < lesJoueurs.size() ; i++){
			if(lesJoueurs.get(i).listeScore.size() != 0){
				valRet = true;
			}
		}
		return valRet;
	}

	public int getNbJoueursQuiPeuventJouer() {
		int valRet = 0;
		for(int i = 0 ; i < this.lesJoueurs.size() ; i++){
			if(this.lesJoueurs.get(i).peutJouer){
				valRet++;
			}
		}
		return valRet;
	}

	public void remove(int position) {
		lesJoueurs.remove(position);
		if(this.size() != 0){
			this.indexJoueurActuel = 0;
		}
	}
}
