package com.molkky.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class JoueurListe{
	
	private ArrayList<Joueur> lesJoueurs;
	private ListIterator<Joueur> iterator;
	private int indexJoueurActuel;
	
	public JoueurListe(){
		this.lesJoueurs = new ArrayList<Joueur>();
		this.iterator = this.lesJoueurs.listIterator();
		this.indexJoueurActuel = 0;
	}
	
	public Joueur getJoueurActuel(){
		return this.lesJoueurs.get(indexJoueurActuel);
	}
	
	public Joueur next(){
		if(indexJoueurActuel < lesJoueurs.size()-1){
			indexJoueurActuel++;
			return lesJoueurs.get(indexJoueurActuel);
		}else{
			indexJoueurActuel = 0;
			return lesJoueurs.get(0);
		}
	}
	
	public void addJoueur(Joueur j){
		this.lesJoueurs.add(j);
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
		for(int i = 0 ; i < lesJoueurs.size() ; i++){
			lesJoueurs.get(i).nbLignes = 0;
			lesJoueurs.get(i).nbPoints = 0;
			lesJoueurs.get(i).listeScore = new ArrayList<Integer>();
			lesJoueurs.get(i).isGagnant = false;
		}
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
}
