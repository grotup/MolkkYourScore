package molkky.android.main;

import java.util.ArrayList;

import molkky.android.model.Joueur;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class ListeJoueursActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		@SuppressWarnings("unchecked")
		ArrayList<Joueur> joueurs = (ArrayList<Joueur>) getIntent().getExtras().getSerializable("listeJoueurs");
		if(joueurs.size() > 0){
			Toast.makeText(getApplicationContext(), "Coucou ! ",Toast.LENGTH_SHORT).show();
		}
		if(joueurs.size() == 0){
			Toast.makeText(getApplicationContext(), "Coucou 22! ",Toast.LENGTH_SHORT).show();
		}
	}

}
