package model;

public class JoueurBis {
	private int id;
	private String nom;
	private int nbLignes;
	private int score;
	private boolean peutJouer;
	private boolean dernierTourSupScore = false;
	private int scoreSup = -1;
	
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public int getNbLignes() {
		return nbLignes;
	}
	public void setNbLignes(int nbLignes) {
		this.nbLignes = nbLignes;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public boolean isPeutJouer() {
		return peutJouer;
	}
	public void setPeutJouer(boolean peutJouer) {
		this.peutJouer = peutJouer;
	}
}
