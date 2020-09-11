package com.example.profile.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.profile.Book;
import com.example.profile.R;
import com.example.profile.VehicleData;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {
  //private  Context  context;
    private LayoutInflater layoutInflater;
    private List<VehicleData> data;
    private List<VehicleData> SearchData;

    public Adapter(Context context, List<VehicleData> data){

        this.layoutInflater=LayoutInflater.from(context);
        this.data=data;
        SearchData=new  ArrayList<>(data);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view=layoutInflater.inflate(R.layout.custom,parent, false );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            holder.texttitle.setText("Name :- "+data.get(position).getName());
            holder.textDesc.setText("Number :- "+data.get(position).getNumber());
            holder.textmodel.setText("Model :- "+data.get(position).getModel());


            String imgdata = data.get(position).getImg();
            if(!imgdata.isEmpty()) {
                byte[] data = Base64.decode(imgdata, Base64.DEFAULT);
                Bitmap decodedata = BitmapFactory.decodeByteArray(data, 0, data.length);
                holder.imageView.setImageBitmap(decodedata);
            }

        }catch (Exception e){
           // Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView texttitle,textDesc,textmodel,textyear;
        ImageView imageView;
        ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(v.getContext(), Book.class);
                    intent.putExtra("name",data.get(getAdapterPosition()).getName());
                    intent.putExtra("number",data.get(getAdapterPosition()).getNumber());
                    intent.putExtra("mobile",data.get(getAdapterPosition()).getMobile());
                    intent.putExtra("mail",data.get(getAdapterPosition()).getEmail());
                    intent.putExtra("img",data.get(getAdapterPosition()).getImg());

                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(intent);

                }
            });
            texttitle=itemView.findViewById(R.id.textTitle);
            textDesc=itemView.findViewById(R.id.textDesc);
            textmodel=itemView.findViewById(R.id.textmodel);
            imageView=itemView.findViewById(R.id.imageView3);
        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<VehicleData> filterlist=new ArrayList<>();

            if(constraint==null || constraint.length()==0){
                filterlist.addAll(SearchData);
            }else {
                String filterpattern=constraint.toString().toLowerCase().trim();

                for(VehicleData item:SearchData)
                {
                    if(item.getName().toLowerCase().contains(filterpattern)){
                        filterlist.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filterlist;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            data.clear();
            data.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };
}
