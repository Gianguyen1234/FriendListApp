package com.example.friendlistapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    private List<User> mListUser;
    private final IClickItemUser iClickItemUser;
    public interface IClickItemUser{
        void updateUser(User user);

        void deleteUser(User user);

    }

    public UserAdapter(IClickItemUser iClickItemUser) {
        this.iClickItemUser = iClickItemUser;
    }


    public void setData(List<User> list){
        this.mListUser = list;
        notifyDataSetChanged();
    }




    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {

        User user = mListUser.get(position);
        if(user == null){
            return;
        }

        holder.tvUsername.setText(user.getUserName());
        holder.tvPhoneNumber.setText(user.getPhoneNumber());
        holder.tvAddress.setText(user.getAddress());


        holder.btnUpdate.setOnClickListener(v -> iClickItemUser.updateUser(user));

        holder.btnDelete.setOnClickListener(v -> iClickItemUser.deleteUser(user));

    }

    @Override
    public int getItemCount() {
        if(mListUser != null){
            return mListUser.size();
        }
        return 0;
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder{

        private final TextView tvUsername;
        private final TextView tvAddress;
        private final TextView tvPhoneNumber;

        private final Button btnUpdate;

        private final Button btnDelete;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUsername = itemView.findViewById(R.id.tv_username);
            tvPhoneNumber = itemView.findViewById(R.id.tv_number_phone);
            tvAddress = itemView.findViewById(R.id.tv_address);
            btnUpdate = itemView.findViewById(R.id.btn_update);
            btnDelete = itemView.findViewById(R.id.btn_delete);

        }
    }
}
