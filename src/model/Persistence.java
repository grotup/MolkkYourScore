package model;

import com.molkky.main.Partie;

import android.content.Context;
import android.content.SharedPreferences;

public class Persistence {

	/**
	 * Fonction qui enregistre une partie en persistence
	 * @param p
	 */
	public static void savePartie(Partie engine, Context ctx){
		SharedPreferences sp = ctx.getSharedPreferences("FILE", 0);
		String joueurs = "";
		for(int i = 0 ; i < engine.getNbJoueurs() ; i++){
			joueurs += engine.getJoueur(i).nomJoueur + "__";
			joueurs += engine.getJoueur(i).getListeScore();
			joueurs += ";";
		}
		SharedPreferences.Editor editor = sp.edit();
		editor.remove("joueurs");
		editor.putString("joueurs", joueurs);
		editor.putInt("joueurActuel", engine.getJoueurActuelIndex());
		editor.commit();
	}
	
	public static void loadPartie(Partie engine, Context ctx){
		SharedPreferences sp = ctx.getSharedPreferences("FILE", 0);
		try{
			String lesJoueurs = sp.getString("joueurs", "0");
			if(lesJoueurs != "0"){
				String[] joueurs = lesJoueurs.split(";");
				for(int i = 0; i < joueurs.length ; i++){
					String[] objJoueur = joueurs[i].split("__");
					Joueur joueur = engine.addJoueur(objJoueur[0]);
					if(objJoueur.length > 1){
						String[] scores = objJoueur[1].split(" - ");
						for(int j = 0 ; j < scores.length ; j++){
							joueur.ajouterScore(scores[j]);
						}
					}
				}
				engine.setIndexJoueurActuel(sp.getInt("joueurActuel", 0));
			}
			
		}catch(Exception e){
			SharedPreferences.Editor editor = sp.edit();
			editor.remove("joueurs");
			editor.remove("joueurActuel");
			editor.commit();
		}
	}

	public static void reset(Context ctx) {
		SharedPreferences sp = ctx.getSharedPreferences("FILE", 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove("joueurs");
		editor.remove("joueurActuel");
		editor.commit();
	}
	
}
