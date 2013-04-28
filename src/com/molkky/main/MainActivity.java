package com.molkky.main;

import java.util.ArrayList;
import java.util.Hashtable;

import com.example.test21.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnMenuItemClickListener, OnItemLongClickListener{

	private PartieOptions options;
	private JoueurListe listeJoueur;
	private String nomJoueur = "";
	private boolean partieCommencee = false;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.options = new PartieOptions();
		this.listeJoueur = new JoueurListe();
		initComponents();
	}

	private void initComponents() {
		Button ajoutJoueur = (Button) findViewById(R.id.ajoutJoueur);
		ajoutJoueur.setOnClickListener(this);
		Button ajoutScore = (Button) findViewById(R.id.scoreButton);
		ajoutScore.setOnClickListener(this);
		ImageButton annulerScore = (ImageButton) findViewById(R.id.annulerScore);
		annulerScore.setOnClickListener(this);
		annulerScore.setEnabled(false);
		ListView listJoueur = (ListView) findViewById(R.id.listJoueurs);
		listJoueur.setOnItemLongClickListener(this);
		JoueurListeAdapter adapter = new JoueurListeAdapter(this, this.listeJoueur);
		listJoueur.setAdapter(adapter);
	}

	public boolean onMenuItemClick(MenuItem arg0) {
		
		return false;
	}

	public void onClick(View arg0) {
		if(arg0 == (Button)findViewById(R.id.ajoutJoueur)){
			// Dans ce cas, on affiche le formulaire du nom de joueur
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Nom du nouveau joueur");

			// Set up the input
			final EditText input = new EditText(this);
			// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
			input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);			
			builder.setView(input);
			// Set up the buttons
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
			        nomJoueur = input.getText().toString();
			        listeJoueur.addJoueur(new Joueur(nomJoueur));
			        TextView tvJoueurActuel = (TextView) findViewById(R.id.joueurActuel);
			        tvJoueurActuel.setText(listeJoueur.getJoueur(0).nomJoueur);
			        updateComponents();
			    }
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
			        dialog.cancel();
			        updateComponents();
			    }
			});
			builder.show();
		}
		if(arg0 == (Button)findViewById(R.id.scoreButton)){
			// On sélectionne le premier joueur de la liste
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Score de " + listeJoueur.getJoueurActuel().nomJoueur);
			final String[] liste = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
			builder.setItems(liste, new DialogInterface.OnClickListener(){
			    public void onClick(DialogInterface dialog, int which) {
			    	listeJoueur.getJoueurActuel().ajouterScore(liste[which]);
					verifsFinDeTour();
					listeJoueur.next();
					updateComponents();
			    }
			});
			builder.show();
		}
		if(arg0 == (ImageButton)findViewById(R.id.annulerScore)){
			Joueur dernierJoueur = listeJoueur.getJoueurAvant();
			dernierJoueur.annulerScore();
			updateComponents();
		}
		
		
	}
	
	/**
	 * Dans cette méthode appelée en fin de tour, on vérifie que la partie n'est pas finie.
	 */
	private void verifsFinDeTour(){
		if(listeJoueur.getJoueurActuel().isGagnant){
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Fin de partie ! " + listeJoueur.getJoueurActuel().nomJoueur + " gagne !");
			builder.setMessage("Fin de partie. Recommencer ?");
			builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
			    	listeJoueur.ResetScoreTousJoueurs();
			    	updateComponents();
			    }
			});
			builder.setNegativeButton("Non", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int which) {
			        listeJoueur.ResetJoueurs();
			        updateComponents();
			    }
			});
			builder.show();
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void updateComponents(){
		// Si la partie a commencé
		if(listeJoueur.partieCommencee()){
			// On grise le bouton d'ajout de joueur
			Button bNouvJoueur = (Button) findViewById(R.id.ajoutJoueur);
			bNouvJoueur.setEnabled(false);
			// On rend disponible le bouton d'annulation de score
			ImageButton bAnnulerScore = (ImageButton) findViewById(R.id.annulerScore);
			bAnnulerScore.setEnabled(true);
		}else{
			// On rend disponible le bouton d'ajout de joueur
			Button bNouvJoueur = (Button) findViewById(R.id.ajoutJoueur);
			bNouvJoueur.setEnabled(true);
			// On grise le bouton d'annulation de score
			ImageButton bAnnulerScore = (ImageButton) findViewById(R.id.annulerScore);
			bAnnulerScore.setEnabled(false);
		}
		
		// On check aussi le joueur actuel
		TextView tvJoueurActuel = (TextView) findViewById(R.id.joueurActuel);
        tvJoueurActuel.setText(listeJoueur.getJoueurActuel().nomJoueur);
		
		// Dans tous les cas, on notifie un changement dans la liste
		ListView lv = (ListView) findViewById(R.id.listJoueurs);
        ((BaseAdapter) lv.getAdapter()).notifyDataSetChanged();
	}
}
