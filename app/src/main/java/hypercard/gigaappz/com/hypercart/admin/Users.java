package hypercard.gigaappz.com.hypercart.admin;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hypercard.gigaappz.com.hypercart.R;
import hypercard.gigaappz.com.hypercart.User;
import hypercard.gigaappz.com.hypercart.model_class.Shop;

/**
 * A simple {@link Fragment} subclass.
 */
public class Users extends Fragment {
    RecyclerView recyclerView;
    List<User> pendinglist = new ArrayList<>();
    ;
    UsersListAdapter userpendingadapter;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    TextView nocontent;

    public Users() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        nocontent = (TextView) view.findViewById(R.id.nocontent);
        recyclerView.setVisibility(View.GONE);
        nocontent.setVisibility(View.VISIBLE);
        getpendinglist();
        return view;
    }

    private void getpendinglist() {
        //  TODO pending shop request
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        mFirebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pendinglist.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    User user = dataSnapshot1.getValue(User.class);
                    User userlist = new User();
                    userlist.setMobile(user.getMobile());
                    userlist.setName(user.getName());
                    userlist.setPlace(user.getPlace());
                    userlist.setAcctype(user.getAcctype());
                    userlist.setPassword(user.getPassword());
                    pendinglist.add(userlist);
                }
                if (pendinglist.size() > 0) {
                    recyclerView.setVisibility(View.VISIBLE);
                    nocontent.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    nocontent.setVisibility(View.VISIBLE);
                }
                userpendingadapter = new UsersListAdapter(pendinglist);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(userpendingadapter);
                userpendingadapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
