package molkky.android.main;

import java.util.ArrayList;

import molkky.android.model.Joueur;

/**
 * Classe qui gère la partie de Mölkky.
 * @author Sylvain
 *
 */
public class Partie {
	/**
	 * Options de partie
	 */
	private Integer nbPointsVictoire;
	private Integer nbLignesMax;
	private Integer scoreDepassement;
	/**
	 * Liste des joueurs et joueur actuel
	 */
	private ArrayList<Joueur> listeJoueur;
	private int indexJoueurActuel;
	/**
	 * Etat de la partie
	 */
	public boolean finDePartie = false;
	/**
	 * Liste des coups
	 */
	private ArrayList<Coup> listeCoups;

	/**
	 * Constructeur.
	 * @param nbPointsVictoire Nombre de point pour finir la partie
	 * @param nbLignes Nombre de ligne pour perdre
	 * @param scoreDepassement score obtenu par un joueur ayant plus que le nombre max de points
	 */
	public Partie(Integer nbPointsVictoire, Integer nbLignes, Integer scoreDepassement){
		this.listeJoueur = new ArrayList<Joueur>();
		this.listeCoups = new ArrayList<Coup>();
		this.nbPointsVictoire = nbPointsVictoire;
		this.nbLignesMax = nbLignes;
		this.scoreDepassement = scoreDepassement;
	}

	/**
	 * Fontion qui définit l'index du joueur actuel
	 * @param index l'index du joueur actuel
	 */
	public void setIndexJoueurActuel(int index){
		this.indexJoueurActuel = index;
	}
	
	/**
	 * retourne la liste des joueurs dans la partie
	 * @return Un ArrayList<Joueur>
	 */
	public ArrayList<Joueur> getListeJoueur() {
		return listeJoueur;
	}

	/**
	 * Fonction qui renvoie vrai ou faux si la partie est commencée ou non
	 * @return l'état "commencée" de la partie
	 */
	public boolean partieCommencee(){
		boolean valRet = false;
		for(int i = 0 ; i < listeJoueur.size() ; i++){
			if(listeJoueur.get(i).listeScore.size() != 0){
				valRet = true;
			}
		}
		return valRet;
	}
	
	/**
	 * Fonction qui ajoute le score passé en paramètre au joueur actuel.
	 * Si le score est 0, on ajoute une ligne au joueur.
	 * @param score Score à ajouter au joueur actuel
	 */
	public void ajouterScore(int score) {
		listeJoueur.get(indexJoueurActuel).ajouterScore(score);
		if(Integer.valueOf(score) == 0){
			listeJoueur.get(indexJoueurActuel).ajouterLigne();
		}else{
			listeJoueur.get(indexJoueurActuel).setNbLignes(0);
		}
		this.listeCoups.add(new Coup(this.indexJoueurActuel, score));
		finDeTour();
	}

	/**
	 * Fonction de vérification de fin de tour. 
	 * Vérifie si le joueur est gagnant ou perdant.
	 */
	private void finDeTour() {
		Joueur joueurActuel = listeJoueur.get(indexJoueurActuel);
		// On vérifie les lignes
		if(!joueurActuel.peutJouer && listeJoueur.size() == 1){
			// Si un joueur a 3 lignes et qu'il joue tout seul, fin de partie
			finDePartie = true;
		}
		// On vérifie les points
		if(joueurActuel.isGagnant){
			// Si le joueur a le nombre de points objectif, il est gagnant.
			finDePartie = true;
		}
		// Si on n'a plus qu'un seul joueur, on a un gagnant
		if(getNbJoueursPeuventJoueur() == 1 && listeJoueur.size() > 1){
			finDePartie = true;
		}
	}
	
	public Joueur getJoueurGagnant(){
		Joueur valRet = listeJoueur.get(indexJoueurActuel);
		for(int i = 0 ; i < listeJoueur.size()-1 ; i++){
			if(listeJoueur.get(i).isGagnant){
				valRet = listeJoueur.get(i); 
			}
		}
		return valRet;
	}
	
	/**
	 * Fonction qui retourne le nombre de joueurs qui peuvent jouer dans la partie
	 */
	public int getNbJoueursPeuventJoueur(){
		int valRet = 0;
		for(int i = 0 ; i < this.listeJoueur.size() ; i++){
			if(this.listeJoueur.get(i).peutJouer){
				valRet++;
			}
		}
		return valRet;
	}
	
	public void ResetScoreTousJoueurs() {
		// On réinitialise toutes les valeurs
		for(int i = 0 ; i < listeJoueur.size() ; i++){
			listeJoueur.get(i).nbLignes = 0;
			listeJoueur.get(i).nbPoints = 0;
			listeJoueur.get(i).listeScore = new ArrayList<Integer>();
			listeJoueur.get(i).isGagnant = false;
			listeJoueur.get(i).peutJouer = true;
		}
		// Et on décale l'ordre
		ArrayList<Joueur> tmp = new ArrayList<Joueur>();
		for(int i = 1 ; i < listeJoueur.size(); i++){
			tmp.add(listeJoueur.get(i));
		}
		tmp.add(listeJoueur.get(0));
		this.listeJoueur = tmp;
		indexJoueurActuel = 0;
		this.finDePartie = false;
	}
	
	/**
	 * Fonction qui retourne le joueur suivant qui peut joueur
	 * @return
	 */
	public int next(){
		int nbBoucles = 0;
		if(indexJoueurActuel < listeJoueur.size()-1){
			indexJoueurActuel++;
			// On boucle exactement le nombre de fois qu'on a de joueurs au total dans la liste
			while(listeJoueur.get(indexJoueurActuel).peutJouer == false && nbBoucles < listeJoueur.size()){
				if(listeJoueur.size() -1 == indexJoueurActuel){
					indexJoueurActuel = 0;
				}else{
					indexJoueurActuel++;
				}
			}
			return indexJoueurActuel;
		}else{
			indexJoueurActuel = 0;
			//On boucle exactement le nombre de fois qu'on a de joueurs au total dans la liste
			while(listeJoueur.get(indexJoueurActuel).peutJouer == false && nbBoucles < listeJoueur.size()){
				if(listeJoueur.size() -1 == indexJoueurActuel){
					indexJoueurActuel = 0;
				}else{
					indexJoueurActuel++;
				}
			}
			return indexJoueurActuel;
		}
	}
	
	/**
	 * Fonction qui ajoute un joueur à la partie
	 * @param j	 le Joueur a ajouter.
	 */
	public Joueur addJoueur(Joueur j){
		this.listeJoueur.add(j);
		return this.listeJoueur.get(this.listeJoueur.size()-1);
	}
	
	/**
	 * Fonction qui ajoute un joueur à la partie
	 * @param nomJoueur	Nom du joueur à ajouter
	 */
	public Joueur addJoueur(String nomJoueur){
		Joueur j = new Joueur(nomJoueur, nbPointsVictoire, nbLignesMax, scoreDepassement);
		this.listeJoueur.add(j);
		return this.listeJoueur.get(this.listeJoueur.size()-1);
	}
	
	/**
	 * Fonction qui supprime un joueur de la partie
	 * @param position index du joueur à supprimer
	 */
	public void supprimerJoueur(int position){
		listeJoueur.remove(position);
		if(listeJoueur.size() != 0){
			this.indexJoueurActuel = 0;
		}
	}
	
	/**
	 * fonction qui annule le dernier score ajouté.
	 */
	public void annulerScore(){
		this.indexJoueurActuel = this.listeCoups.get(this.listeCoups.size()-1).getleJoueur();
		Joueur joueurAnnuler = this.listeJoueur.get(indexJoueurActuel);
		joueurAnnuler.annulerScore();
		this.listeCoups.remove(this.listeCoups.size()-1);
	}
	
	/**
	 * Fonction qui retourne l'objet joueur actuel
	 * @return le joueur actuel (objet)
	 */
	public Joueur getJoueurActuel(){
		return this.listeJoueur.get(indexJoueurActuel);
	}

	public int getNbJoueurs() {
		return this.listeJoueur.size();
	}

	public Joueur getJoueur(int i) {
		return listeJoueur.get(i);
	}

	public int getJoueurActuelIndex() {
		return indexJoueurActuel;
	}

	public void joueurSuivant() {
		this.indexJoueurActuel = this.next();
	}
	
	
}
