package molkky.android.main;

import com.google.ads.*;
import com.molkky.main.R;

import molkky.android.model.Joueur;
import molkky.android.model.JoueurListeAdapter;
import molkky.android.model.Persistence;
import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnItemLongClickListener{

	private static final int CODE_RETOUR_OPTION = 1;
	// La partie
	private Partie engine;
	boolean finPartie = false;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// On crée une partie
		creerPartie();
		// Si on a des préférences, on ajoute les joueurs puis on reset les préférences
		chargerPersistence();
		resetPreference();
		// On init les components.
		initComponents();
		updateComponents();
		
		// Create the adView
		AdView  adView = new AdView(this, AdSize.BANNER, "ca-app-pub-6559258480031885/3966197652");

	    // Lookup your LinearLayout assuming it's been given
	    // the attribute android:id="@+id/mainLayout"
	    LinearLayout layout = (LinearLayout)findViewById(R.id.panelHaut);

	    // Add the adView to it
	    layout.addView(adView);

	    // Initiate a generic request to load it with an ad
	    adView.loadAd(new AdRequest());
	}
	
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		// Quand on tue l'application, on enregistre le nom des joueurs déjà créés
		if(engine.getNbJoueurs() != 0){
			Persistence.savePartie(this.engine, getApplicationContext());
		}
	}
	
	protected void onStop(){
		super.onStop();
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
		JoueurListeAdapter adapter = new JoueurListeAdapter(this, this.engine.getListeJoueur());
		listJoueur.setAdapter(adapter);

		listJoueur.setOnItemLongClickListener(this);
		TextView tvJoueurActuel = (TextView) findViewById(R.id.joueurActuel);
        tvJoueurActuel.setText("");
        
        LinearLayout panelBas = (LinearLayout) findViewById(R.id.panelBas);
		panelBas.setVisibility(View.GONE);
	}
	
	/**
	 * Méthode appelée au lancement de l'application. Si une partie était en cours, elle est rechargée.
	 */
	private void chargerPersistence() {
		// On charge la partie dans la persistence
		Persistence.loadPartie(this.engine,getApplicationContext());
		// On définit les élements d'interface
		TextView tvJoueurActuel = (TextView) findViewById(R.id.joueurActuel);
		if(engine.getNbJoueurs() != 0){
			tvJoueurActuel.setText(engine.getJoueurActuel().nomJoueur);
			Button ajoutScore = (Button) findViewById(R.id.scoreButton);
			ajoutScore.setEnabled(true);
		}
	}

	private void creerPartie(){
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		this.engine = new Partie(	
					Integer.valueOf(sharedPref.getString("nbPoints", "50")), 
					Integer.valueOf(sharedPref.getString("nbLignes", "3")), 
					Integer.valueOf(sharedPref.getString("scoreDepassement", "25"))
									);
	}
	
	/**
	 * Fonction qui résete les préférences de l'application (Partie déjà lancée ...)
	 */
	private void resetPreference() {
		Persistence.reset(getApplicationContext());
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
		    	if(!engine.partieCommencee()){
			    	engine.addJoueur(input.getText().toString());
			        TextView tvJoueurActuel = (TextView) findViewById(R.id.joueurActuel);
			        tvJoueurActuel.setText(engine.getJoueur(0).nomJoueur);
			        updateComponents();
			        Button ajoutScore = (Button) findViewById(R.id.scoreButton);
					ajoutScore.setEnabled(true);
		    	}else{
		    		Toast.makeText(MainActivity.this, "Partie commencée. Impossible d'ajouter un joueur", Toast.LENGTH_SHORT).show();
		    	}
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
		if(engine.finDePartie){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Fin.");
			builder.setMessage("Fin de partie !");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
			    	engine.ResetScoreTousJoueurs();
			    	ListView listJoueur = (ListView) findViewById(R.id.listJoueurs);
			    	JoueurListeAdapter adapter = new JoueurListeAdapter(MainActivity.this, engine.getListeJoueur());
					listJoueur.setAdapter(adapter);
					updateComponents();
			    }
			});
			builder.setNegativeButton("Annuler dernier coup", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					engine.annulerScore();
					updateComponents();
				}
			});
			builder.setNeutralButton("Voir partie", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finPartie = true;
					Button nouvellePartie = (Button) findViewById(R.id.nouvellePartie);
					nouvellePartie.setEnabled(true);
					updateComponents();
				}
			});
			builder.show();
		}
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
		    public void onClick(DialogInterface dialog, int which) {}
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
		    public void onClick(DialogInterface dialog, int which) {}
		});
		builder.show();
	}

	private void updateComponents(){
		// Si la partie a commencé
		if(engine.partieCommencee()){
			// On rend disponible le bouton d'annulation de score
			ImageButton bAnnulerScore = (ImageButton) findViewById(R.id.annulerScore);
			bAnnulerScore.setEnabled(true);
		}else{
			// On grise le bouton d'annulation de score
			ImageButton bAnnulerScore = (ImageButton) findViewById(R.id.annulerScore);
			bAnnulerScore.setEnabled(false);
		}
        if(engine.getNbJoueurs()!=0){
        	// Si on a des joueurs, le panelBas est visible
        	LinearLayout panelBas = (LinearLayout) findViewById(R.id.panelBas);
			panelBas.setVisibility(View.VISIBLE);
    		// On check aussi le joueur actuel
    		TextView tvJoueurActuel = (TextView) findViewById(R.id.joueurActuel);
            tvJoueurActuel.setText(engine.getJoueurActuel().nomJoueur); 
            // On ajoute le joueur actuel sur le bouton de score
            Button bAjoutScore = (Button) findViewById(R.id.scoreButton);
            bAjoutScore.setText("Score de " + engine.getJoueurActuel().nomJoueur);
            bAjoutScore.setEnabled(true);
        }else{
        	// Si on n'a pas de joueurs, le panelBas est invisible
        	LinearLayout panelBas = (LinearLayout) findViewById(R.id.panelBas);
			panelBas.setVisibility(View.GONE);
        	TextView tvJoueurActuel = (TextView) findViewById(R.id.joueurActuel);
            tvJoueurActuel.setText(""); 
            Button bAjoutScore = (Button) findViewById(R.id.scoreButton);
            bAjoutScore.setText("Score");
            bAjoutScore.setEnabled(false);
        }
		// Dans tous les cas, on notifie un changement dans la liste
		ListView lv = (ListView) findViewById(R.id.listJoueurs);
        ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
	}
	
	public void onClick(View arg0) {
		if(arg0 == (Button)findViewById(R.id.scoreButton)){
			// On sélectionne le premier joueur de la liste
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Score de " + engine.getJoueurActuel().nomJoueur);
			final String[] liste = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
			builder.setItems(liste, new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which) {
			    	if(which == 0)
						Toast.makeText(MainActivity.this, "Le joueur " + engine.getJoueurActuel().nomJoueur + " a " + (engine.getJoueurActuel().nbLignes + 1) + " ligne(s)", Toast.LENGTH_SHORT).show();
			    	engine.ajouterScore(which);
					verifsFinDeTour();
					engine.joueurSuivant();
					updateComponents();
			    }
			});
			builder.show();
		}
		if(arg0 == (ImageButton)findViewById(R.id.annulerScore)){
			engine.annulerScore();
			updateComponents();
		}
		if(arg0 == (Button)findViewById(R.id.nouvellePartie)){
			if(finPartie){
				engine.ResetScoreTousJoueurs();
				ListView listJoueur = (ListView) findViewById(R.id.listJoueurs);
		    	JoueurListeAdapter adapter = new JoueurListeAdapter(MainActivity.this, engine.getListeJoueur());
				listJoueur.setAdapter(adapter);
				finPartie = false;
				findViewById(R.id.nouvellePartie).setEnabled(false);
			}
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == CODE_RETOUR_OPTION){
			Toast.makeText(this, "Si des préférences ont été modifiées, elles seront prises en compte à la prochaine réinitialisation.", Toast.LENGTH_SHORT).show();	 
		}
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
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setTitle("Réinitialiser");
				builder.setMessage("Êtes vous sûrs de vouloir réinitialiser ?");
				builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int which) {
				    	creerPartie();
						initComponents();
						ListView lv = (ListView) findViewById(R.id.listJoueurs);
						((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();	
						updateComponents();
				    }
				});
				builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int which) {}
				});
				builder.show();
				break;
								
			case R.id.menu_ajouter_joueur:
				if(this.engine.partieCommencee()){
					AlertDialog.Builder builderErreurPartieCommencee = new AlertDialog.Builder(MainActivity.this);
					builderErreurPartieCommencee.setTitle("Erreur");
					builderErreurPartieCommencee.setMessage("Impossible d'ajouter un joueur, la partie est commencée.");
					builderErreurPartieCommencee.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog, int which) {}
					});
					builderErreurPartieCommencee.show();
				}else{
					ajouterJoueur();
				}
				break;
				
			case R.id.menu_options:
				Intent i2 = new Intent(this, SettingsActivity.class);
				startActivityForResult(i2, CODE_RETOUR_OPTION);
		}
		return false;
	}

	public boolean onItemLongClick(AdapterView<?> arg0, final View arg1, final int arg2, long arg3) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(engine.getJoueur(arg2).getNomJoueur());
		
		builder.setItems(R.array.menuJoueurSelection, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	switch(which){
            		case 0 :
            			
            			final Joueur joueurAModifier = engine.getJoueur(arg2);
            			
            			AlertDialog.Builder builderModifier = new AlertDialog.Builder(MainActivity.this);
            			builderModifier.setTitle("Modifier joueur");

            			// Set up the input
            			final EditText input = new EditText(MainActivity.this);
            			InputFilter[] filterArray = new InputFilter[1];
            			filterArray[0] = new InputFilter.LengthFilter(10);
            			input.setFilters(filterArray);
            			// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            			input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            			input.append(joueurAModifier.getNomJoueur()); 	
            			// Set up the buttons
            			builderModifier.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            			    public void onClick(DialogInterface dialog, int which) {
            			    	joueurAModifier.setNomJoueur(input.getText().toString());
            			    	updateComponents();
            			    	Toast.makeText(MainActivity.this, "Joueur modifié", Toast.LENGTH_SHORT).show();
            			    	ListView lv = (ListView) findViewById(R.id.listJoueurs);
          					  	((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
            			    }
            			});
            			builderModifier.setView(input);
            			builderModifier.show();
            			break;
            		
            		case 1 :
            			AlertDialog.Builder builderSupprimer = new AlertDialog.Builder(MainActivity.this);
            			builderSupprimer.setMessage("Supprimer " + engine.getJoueur(arg2).nomJoueur);
            			builderSupprimer.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            				public void onClick(DialogInterface dialog, int which) {	
            			    	if(!engine.partieCommencee()){
            			    		engine.supprimerJoueur(arg2);
            			    		arg1.setAlpha(1);
            			    		updateComponents();
            			    	}
            			    	ListView lv = (ListView) findViewById(R.id.listJoueurs);
            			    	((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
        				    }	
            			});
            			builderSupprimer.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            			    public void onClick(DialogInterface dialog, int which) {}
            			});
            			builderSupprimer.show();
            			break;
            	}
            }
        });
		builder.show();
		return false;
	}
}
