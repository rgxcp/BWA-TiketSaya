package com.rgxcp.tiketsaya;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.TicketViewHolder> {
    Context context;
    ArrayList<MyTicket> myTicket;

    public TicketAdapter(Context c, ArrayList<MyTicket> p) {
        context = c;
        myTicket = p;
    }

    @NonNull
    @Override
    public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TicketViewHolder(LayoutInflater.from(context).inflate(R.layout.recview_myticket, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TicketViewHolder holder, int position) {
        holder.x_nama_wisata.setText(myTicket.get(position).getNama_wisata());
        holder.x_lokasi_wisata.setText(myTicket.get(position).getLokasi_wisata());
        holder.x_jumlah_ticket.setText(myTicket.get(position).getJumlah_ticket() + " Tickets");

        final String nama_wisata = myTicket.get(position).getNama_wisata();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goto_MyTicketDetail = new Intent(context, MyTicketDetailActivity.class);
                goto_MyTicketDetail.putExtra("nama_wisata", nama_wisata);
                context.startActivity(goto_MyTicketDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myTicket.size();
    }

    class TicketViewHolder extends RecyclerView.ViewHolder {
        TextView x_nama_wisata, x_lokasi_wisata, x_jumlah_ticket;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);

            x_nama_wisata = itemView.findViewById(R.id.x_nama_wisata);
            x_lokasi_wisata = itemView.findViewById(R.id.x_lokasi_wisata);
            x_jumlah_ticket = itemView.findViewById(R.id.x_jumlah_ticket);
        }
    }
}
