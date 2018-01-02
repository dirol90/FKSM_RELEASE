package com.tsimbalyukstudio.fksm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;


public class MyLoansAdapter extends RecyclerView.Adapter<MyLoansAdapter.ViewHolder> {

    static List <Refound> refo;
        static List <Loan> loans;
    static DatabaseReference myRef;
    static FirebaseUser currentUser;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvDate;
            TextView tvSum;
            TextView tvPayed;
            TextView tvTerm;
            TextView tvStatus;
            Button bt;

            public ViewHolder(View v) {
                super(v);
                tvDate = (TextView) v.findViewById(R.id.loan_date);
                tvSum = (TextView) v.findViewById(R.id.loan_full);
                tvPayed = (TextView) v.findViewById(R.id.loan_payed);
                tvTerm = (TextView) v.findViewById(R.id.loan_terms);
                tvStatus = (TextView) v.findViewById(R.id.loan_status);
                bt = (Button) v.findViewById(R.id.loan_del);

            }
        }

        public MyLoansAdapter(List<Loan> loans, List<Refound> refo, DatabaseReference myRef, FirebaseUser currentUser) {
            this.refo = refo;
            this.currentUser = currentUser;
            this.myRef = myRef;
            this.loans = new ArrayList<>();
            this.loans = loans;
        }

    @Override
        public MyLoansAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.loan_sample, parent, false);
            ViewHolder vh = new ViewHolder(v);

            return vh;
        }

        int sum;
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Loan loan = loans.get(position);
                    loan.setStatus(0);
                    myRef.child(currentUser.getUid()).child("loan").child(loans.get(position).getLoanID()).setValue(loan);
                }
            });

            char [] c = loans.get(position).getDate().toCharArray();
            String s = "";
            int x = 0;
            while (c[x] != '_'){
                if(x == 4 || x==6){
                    s+="-";
                }
                s += c[x++];
            }
            holder.tvDate.setText(s);
            holder.tvSum.setText(loans.get(position).getSum()+"");

            sum = 0;
            for (int i = 0; i < refo.size(); i++){
                Refound tempR = refo.get(i);
                Loan tempL = loans.get(position);
                if (tempR.getLoanID().equals(tempL.getLoanID()) && tempR.getStatus()==1){
                    sum+=tempR.getSum();
                }
            }
            holder.tvPayed.setText(sum+"");
            holder.tvTerm.setText(loans.get(position).getTime()+"");
            switch (loans.get(position).getStatus()) {
                case (1):
                    holder.tvStatus.setText("Статус: ПРОВЕРЕН");
                    holder.tvStatus.setTextColor(GREEN);
                    holder.bt.setVisibility(View.INVISIBLE);
                    break;
                case (2):
                    holder.tvStatus.setText("Статус: ОТКЛОНЕН");
                    holder.tvStatus.setTextColor(RED);
                    holder.bt.setVisibility(View.INVISIBLE);
                    break;
                case (3):
                    holder.tvStatus.setText("Статус: НА ПРОВЕРКЕ");
                    holder.tvStatus.setTextColor(BLUE);
                    break;
                case (4):
                    holder.tvStatus.setText("Статус: ПОГАШЕН");
                    holder.tvStatus.setTextColor(BLACK);
                    holder.bt.setVisibility(View.INVISIBLE);
                    break;
                case (0):
                    holder.tvStatus.setText("Статус: ОШИБКА");
                    holder.tvStatus.setTextColor(BLACK);
                    holder.bt.setVisibility(View.INVISIBLE);
                    break;
                default:
                    holder.tvStatus.setText("Статус: ОШИБКА");
                    holder.tvStatus.setTextColor(BLACK);
                    holder.bt.setVisibility(View.INVISIBLE);
                    break;
            }

        }

        @Override
        public int getItemCount() {
            return loans.size();
        }


}
