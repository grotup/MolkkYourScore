package com.molkky.main;

import com.molkky.main.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class JoueurListeAdapter extends BaseAdapter {

	JoueurListe liste;
	LayoutInflater inflater;
	
	public JoueurListeAdapter(Context c, JoueurListe liste) {
		this.inflater = LayoutInflater.from(c);
		this.liste = liste;
	}

	@Override
	public int getCount() {
		return liste.size();
	}

	@Override
	public Object getItem(int position) {
		return liste.getJoueur(position);
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
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvNomJoueur.setText(liste.getJoueur(position).nomJoueur);
		holder.tvScore.setText(Integer.toString(liste.getJoueur(position).nbPoints));
		return convertView;
	}

	private class ViewHolder {
		TextView tvNomJoueur;
		TextView tvScore;
	}
}
