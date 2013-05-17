package com.molkky.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnItemLongClickListener{

	private static int CODE_RETOUR = 1;
	private JoueurListe listeJoueur;
	/**
	 * Les variables issues des paramètres
	 */
	private int nbPointsVictoire;
	private int nbLignesMax;
	private int scoreDepassement;
	private MolkkEngine engine;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.listeJoueur = new JoueurListe();
		// On va chercher les prefs
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		this.nbPointsVictoire = Integer.valueOf(sharedPref.getString("nbPoints", "50"));
		this.nbLignesMax = Integer.valueOf(sharedPref.getString("nbLignes", "3"));
		this.scoreDepassement = Integer.valueOf(sharedPref.getString("scoreDepassement", "25"));
		// Si on a des préférences, on ajoute les joueurs.
		initComponents();
		getPreferences();
	}

	/**
	 * Méthode appelée au lancement de l'application. Si une partie était en cours, elle est rechargée.
	 */
	private void getPreferences() {
		SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);
		try{
			String lesJoueurs = prefs.getString("joueurs", "0");
			if(lesJoueurs != "0"){
				String[] joueurs = lesJoueurs.split(";");
				for(int i = 0; i < joueurs.length ; i++){
					String[] objJoueur = joueurs[i].split("__");
					Joueur joueur = listeJoueur.addJoueur(new Joueur(objJoueur[0], nbPointsVictoire, nbLignesMax, scoreDepassement));
					if(objJoueur.length > 1){
						String[] scores = objJoueur[1].split(" - ");
						for(int j = 0 ; j < scores.length ; j++){
							joueur.ajouterScore(scores[j]);
						}
					}
				}
			}
			listeJoueur.setIndexJoueurActuel(prefs.getInt("joueurActuel", 0));
			TextView tvJoueurActuel = (TextView) findViewById(R.id.joueurActuel);
	        tvJoueurActuel.setText(listeJoueur.getJoueurActuel().nomJoueur);
	        updateComponents();
	        Button ajoutScore = (Button) findViewById(R.id.scoreButton);
			ajoutScore.setEnabled(true);
		}catch(Exception e){
			SharedPreferences.Editor editor = prefs.edit();
			editor.remove("joueurs");
			editor.remove("joueurActuel");
			editor.commit();
		}
	}

	protected void onStop(){
		super.onStop();
		// Quand on tue l'application, on enregistre le nom des joueurs déjà créés
		if(listeJoueur.size() != 0){
			SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);
			String joueurs = "";
			for(int i = 0 ; i < listeJoueur.size() ; i++){
				joueurs += listeJoueur.getJoueur(i).nomJoueur + "__";
				joueurs += listeJoueur.getJoueur(i).getListeScore();
				joueurs += ";";
			}
			SharedPreferences.Editor editor = prefs.edit();
			editor.remove("joueurs");
			editor.putString("joueurs", joueurs);
			editor.putInt("joueurActuel", listeJoueur.getJoueurActuelIndex());
			editor.commit();
		}
	}
	
	private void initComponents() {
		Button ajoutScore = (Button) findViewById(R.id.scoreButton);
		ajoutScore.setText("Score");
		ajoutScore.setOnClickListener(this);
		ajoutScore.setEnabled(false);
		
		ImageButton annulerScore = (ImageButton) findViewById(R.id.annulerScore);
		annulerScore.setOnClickListener(this);
		annulerScore.setEnabled(false);
		
		ListView listJoueur = (ListView) findViewById(R.id.listJoueurs);
		listJoueur.setOnItemLongClickListener(this);
		JoueurListeAdapter adapter = new JoueurListeAdapter(this, this.listeJoueur);
		listJoueur.setAdapter(adapter);
		listJoueur.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		      public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
		    	  if(!listeJoueur.partieCommencee()){
		    		  view.animate().setDuration(500).alpha(0).withEndAction(new Runnable() {
		    			  public void run() {		            	  
		    				  supprimerJoueurListe(position);
		    				  view.setAlpha(1);		    				  
		    			  }
		    		  });
		    	  }
		      };

		    });
		TextView tvJoueurActuel = (TextView) findViewById(R.id.joueurActuel);
        tvJoueurActuel.setText("");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main, menu);
	    return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_nouvellePartie:
				resetAll();
				creerPartie();
				break;
				
			case R.id.menu_ajouter_joueur:
				ajouterJoueur();
				break;
				
			case R.id.menu_options:
				Intent i = new Intent(this, SettingsActivity.class);
				startActivityForResult(i, CODE_RETOUR);
		}
		return false;
	}

	private void creerPartie(){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		this.engine = new MolkkEngine(	Integer.valueOf(sharedPref.getString("nbPoints", "50")), 
										Integer.valueOf(sharedPref.getString("nbLignes", "3")), 
										Integer.valueOf(sharedPref.getString("scoreDepassement", "25"))
									);
		
		this.resetPreference();
	}
	
	/**
	 * Fonction qui résete les préférences de l'application (Partie déjà lancée ...)
	 */
	private void resetPreference() {
		SharedPreferences prefs = this.getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.remove("joueurs");
		editor.remove("joueurActuel");
		editor.commit();
	}

	private void resetAll() {
		listeJoueur = new JoueurListe();
		this.resetPreference();
		// On va chercher les prefs
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		this.nbPointsVictoire = Integer.valueOf(sharedPref.getString("nbPoints", "50"));
		this.nbLignesMax = Integer.valueOf(sharedPref.getString("nbLignes", "3"));
		this.scoreDepassement = Integer.valueOf(sharedPref.getString("scoreDepassement", "25"));
		initComponents();
	}

	public void onClick(View arg0) {
		if(arg0 == (Button)findViewById(R.id.scoreButton)){
			// On sélectionne le premier joueur de la liste
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Score de " + listeJoueur.getJoueurActuel().nomJoueur);
			final String[] liste = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
			builder.setItems(liste, new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which) {
			    	ajouterScore(liste[which]);
			    	engine.ajouterScore(liste[which]);
			    }
			});
			builder.show();
		}
		if(arg0 == (ImageButton)findViewById(R.id.annulerScore)){
			annulerScore();
		}
	}
	
	/**
	 * Méthode appelée quand clique sur le bouton pour annuler un score.
	 * Supprime le dernier score et sélectionne le joueur d'avant en tant que joueur actuel
	 */
	public void annulerScore(){
		Joueur dernierJoueur = listeJoueur.getJoueurAvant();
		dernierJoueur.annulerScore();
		updateComponents();
	}
	
	/**
	 * Méthode appelée quand on clique sur le bouton d'ajout de score
	 * Affiche le menu des scores et ajoute le score sélectionné au joueur actuel
	 */
	public void ajouterScore(String score){
		listeJoueur.getJoueurActuel().ajouterScore(score);
		if(score == "0")
			Toast.makeText(this, "Le joueur " + listeJoueur.getJoueurActuel().nomJoueur + " a " + listeJoueur.getJoueurActuel().nbLignes + " ligne(s)", Toast.LENGTH_LONG).show();
		verifsFinDeTour();
	}
	
	/**
	 * Méthode appelée quand on clqiue sur le bouton d'ajout de joueur
	 * Ajoute un joueur à la liste
	 */
	public void ajouterJoueur(){
		// Dans ce cas, on affiche le formulaire du nom de joueur
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Nom du nouveau joueur");

		// Set up the input
		final EditText input = new EditText(this);
		InputFilter[] filterArray = new InputFilter[1];
		filterArray[0] = new InputFilter.LengthFilter(10);
		input.setFilters(filterArray);
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		        listeJoueur.addJoueur(new Joueur(input.getText().toString(), nbPointsVictoire, nbLignesMax, scoreDepassement));
		        TextView tvJoueurActuel = (TextView) findViewById(R.id.joueurActuel);
		        tvJoueurActuel.setText(listeJoueur.getJoueur(0).nomJoueur);
		        updateComponents();
		        Button ajoutScore = (Button) findViewById(R.id.scoreButton);
				ajoutScore.setEnabled(true);
		    }
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});

		builder.setView(input);
		builder.show();
	}

	/**
	 * Dans cette méthode appelée en fin de tour, on vérifie que la partie n'est pas finie.
	 */
	private void verifsFinDeTour(){
		boolean finDePartie = false;
		Joueur joueurActuelOuGagnant = listeJoueur.getJoueurActuel();
		// Si le joueur actuel a 50 points, il a gagné, c'est la fin de partie.
		if(joueurActuelOuGagnant.isGagnant){
			finDePartie = true;
		}
		// Si c'est la troisième ligne pour le joueur actuel, alors il a perdu.
		if(joueurActuelOuGagnant.nbLignes == this.nbLignesMax){
			joueurActuelOuGagnant.peutJouer = false;
			afficherJoueurPerdant(listeJoueur.getJoueurActuel());
		}
		// S'il ne reste plus qu'un joueur qui peut jouer, alors celui ci a gagné.
		if(listeJoueur.size() > 1 && listeJoueur.getNbJoueursQuiPeuventJouer() == 1){
			joueurActuelOuGagnant = listeJoueur.next();
			finDePartie = true;
		}
		if(finDePartie){
			afficherJoueurGagnant(joueurActuelOuGagnant);
		}else{
			listeJoueur.next();
		}
		updateComponents();
	}
	
	/**
	 * Méthode qui fait perdre le joueur passé en paramètre. Il est considéré comme ne pouvant plus jouer.
	 * @param j le joueur qui perd la partie
	 */
	public void afficherJoueurPerdant(Joueur j){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Perdu.");
		builder.setMessage("Fin de partie pour " + j.nomJoueur + " !");
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		    	updateComponents();
		    }
		});
		builder.show();
	}
	
	/**
	 * Méthode qui fait gagner un joueur passé en paramètre. Affichage ensuite d'une popup proposant de recommencer la partie
	 * @param j le joueur gagnant.
	 */
	public void afficherJoueurGagnant(Joueur j){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Fin de partie ! ");
		builder.setMessage(j.nomJoueur + " gagne !");
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		    	listeJoueur.ResetScoreTousJoueurs();
		    	updateComponents();
		    }
		});
		builder.show();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Joueur");
		builder.setMessage("Supprimer " + listeJoueur.getJoueur(arg2).nomJoueur);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				supprimerJoueurListe(which);
			}
		});
		return false;
	}
	
	private void updateComponents(){
		// Si la partie a commencé
		if(listeJoueur.partieCommencee()){
			// On rend disponible le bouton d'annulation de score
			ImageButton bAnnulerScore = (ImageButton) findViewById(R.id.annulerScore);
			bAnnulerScore.setEnabled(true);
		}else{
			// On grise le bouton d'annulation de score
			ImageButton bAnnulerScore = (ImageButton) findViewById(R.id.annulerScore);
			bAnnulerScore.setEnabled(false);
			
		}		
		// Dans tous les cas, on notifie un changement dans la liste
		ListView lv = (ListView) findViewById(R.id.listJoueurs);
        ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
        if(listeJoueur.size()!=0){
    		// On check aussi le joueur actuel
    		TextView tvJoueurActuel = (TextView) findViewById(R.id.joueurActuel);
            tvJoueurActuel.setText(listeJoueur.getJoueurActuel().nomJoueur); 
            // On ajoute le joueur actuel sur le bouton de score
            Button bAjoutScore = (Button) findViewById(R.id.scoreButton);
            bAjoutScore.setText("Score de " + listeJoueur.getJoueurActuel().nomJoueur);
        }else{
        	TextView tvJoueurActuel = (TextView) findViewById(R.id.joueurActuel);
            tvJoueurActuel.setText(""); 
        }
	}

	private void supprimerJoueurListe(int index){
		listeJoueur.remove(index);
		  ListView lv = (ListView) findViewById(R.id.listJoueurs);
		  ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
		  updateComponents();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Toast.makeText(this, "Si des préférences ont été modifiées, elles seront prises en compte à la prochaine réinitialisation.", Toast.LENGTH_SHORT).show();	 
		super.onActivityResult(requestCode, resultCode, data);
	}
}
