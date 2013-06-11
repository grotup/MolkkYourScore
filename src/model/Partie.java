package model;

import java.util.ArrayList;

public class Partie {
	private ArrayList<JoueurBis> listeJoueurs;
	private int joueurActuel;
	private boolean terminee;
	
	public boolean isTerminee() {
		return terminee;
	}
	public void setTerminee(boolean terminee) {
		this.terminee = terminee;
	}
	public ArrayList<JoueurBis> getListeJoueurs() {
		return listeJoueurs;
	}
	public void setListeJoueurs(ArrayList<JoueurBis> listeJoueurs) {
		this.listeJoueurs = listeJoueurs;
	}
	public int getJoueurActuel() {
		return joueurActuel;
	}
	public void setJoueurActuel(int joueurActuel) {
		this.joueurActuel = joueurActuel;
	}
}
