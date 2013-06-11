package model;

import java.util.ArrayList;


import com.molkky.main.R;
import com.molkky.main.R.id;
import com.molkky.main.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JoueurListeAdapter extends BaseAdapter {

	ArrayList<Joueur> liste;
	LayoutInflater inflater;
	
	public JoueurListeAdapter(Context c, ArrayList<Joueur> liste) {
		this.inflater = LayoutInflater.from(c);
		this.liste = liste;
	}

	public void setListe(ArrayList<Joueur> liste) {
		this.liste = liste;
	}

	@Override
	public int getCount() {
		return liste.size();
	}

	@Override
	public Object getItem(int position) {
		return liste.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.itemjoueur, null);
			holder.tvNomJoueur = (TextView)convertView.findViewById(R.id.tvNomJoueur);
			holder.tvScore = (TextView)convertView.findViewById(R.id.tvScore);
			holder.listeScore = (TextView)convertView.findViewById(R.id.listeScore);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvNomJoueur.setText(liste.get(position).nomJoueur);
		holder.tvScore.setText(Integer.toString(liste.get(position).nbPoints));
		holder.listeScore.setText(liste.get(position).getListeScore());
		
		return convertView;
	}

	private class ViewHolder {
		TextView tvNomJoueur;
		TextView tvScore;
		TextView listeScore;
	}
}
