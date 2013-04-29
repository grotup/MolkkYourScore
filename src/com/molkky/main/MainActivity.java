package com.molkky.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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

	private JoueurListe listeJoueur;
	private String nomJoueur = "";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.listeJoueur = new JoueurListe();
		initComponents();
	}

	private void initComponents() {
		Button ajoutScore = (Button) findViewById(R.id.scoreButton);
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
		case R.id.menu_nouveauJoueur:
			if(!listeJoueur.partieCommencee()){
				ajouterJoueur();
			}
			break;
			
		case R.id.menu_nouvellePartie:
			resetAll();
			break;
		}
		return false;
	}

	private void resetAll() {
		listeJoueur = new JoueurListe();
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
		// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
		input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {
		        listeJoueur.addJoueur(new Joueur(input.getText().toString()));
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
		if(joueurActuelOuGagnant.nbLignes == 3){
			joueurActuelOuGagnant.peutJouer = false;
			afficherJoueurPerdant(listeJoueur.getJoueurActuel());
		}
		// S'il ne reste plus qu'un joueur qui peut jouer, alors celui ci a gagné.
		if(listeJoueur.size() != 1 && listeJoueur.getNbJoueursQuiPeuventJouer() == 1){
			joueurActuelOuGagnant = listeJoueur.next();
			finDePartie = true;
		}
		if(finDePartie){
			afficherJoueurGagnant(joueurActuelOuGagnant);
		}
		listeJoueur.next();
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
		// TODO Auto-generated method stub
		return false;
	}
	
	private void updateComponents(){
		// Si la partie a commencé
		if(listeJoueur.partieCommencee()){
			// On rend disponible le bouton d'annulation de score
			ImageButton bAnnulerScore = (ImageButton) findViewById(R.id.annulerScore);
			bAnnulerScore.setEnabled(true);
		}else{
			// On rend disponible le bouton d'ajout de joueur
			//Button bNouvJoueur = (Button) findViewById(R.id.ajoutJoueur);
			//bNouvJoueur.setEnabled(true);
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
}
