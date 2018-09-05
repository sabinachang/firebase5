package com.enhan.sabina.firebaseapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.enhan.sabina.firebaseapp.model.Article;
import com.enhan.sabina.firebaseapp.model.Author;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int TAG_1 = 0x01;
    private static final int TAG_2 = 0x02;
    private static final int TAG_3 = 0x03;
    private static final int TAG_4 = 0x04;
    
    private Button mStart;
    private Button mFindFriend;
    private Button mSend;
    private Button mAccept;
    private Button mCancel;
    private Button mDeny;
    private Button mFindTagFriend;
    private Button mFindThisTag;
    private Button mFindThisFriend;

    private EditText mUserEmail;
    private EditText mFriendEmail;
    private EditText mArticleTitle;
    private EditText mArticleContent;
    private EditText mFriendEmailFilter;
    private EditText mArticleTag;
    private EditText mTagFilter;
    private EditText mNewUserName;

    private DatabaseReference mDatabaseReference;
    private String mUserKey;
    private String mMail;
    private String mFriendKey ;
    private String mFriendRequestKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStart = findViewById(R.id.start_session);
        mSend = findViewById(R.id.send);
        mFindFriend = findViewById(R.id.find_friend);
        mAccept = findViewById(R.id.accept_invite);
        mCancel = findViewById(R.id.cancel_invite);
        mDeny = findViewById(R.id.deny_invite);
        mFindTagFriend = findViewById(R.id.search_this_ft);
        mFindThisTag = findViewById(R.id.search_this_tag);
        mFindThisFriend = findViewById(R.id.search_this_friend);

        mUserEmail = findViewById(R.id.user_email);
        mFriendEmail = findViewById(R.id.friend_email);
        mFriendEmailFilter = findViewById(R.id.friend_email_filter);
        mArticleContent = findViewById(R.id.article_content);
        mArticleTitle = findViewById(R.id.article_title);
        mArticleTag = findViewById(R.id.tag_category);
        mTagFilter = findViewById(R.id.tag_filter);
        mNewUserName = findViewById(R.id.new_user_name);
        
        mStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSession();
            }
        });

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewArticle();
            }
        });

        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptFriendRequest();
            }
        });

        mDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                negateFriendRequest();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                negateFriendRequest();
            }
        });

        mFindFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifFriendExist();
            }
        });

        mFindThisFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mFriendEmailFilter.getText().toString();
                queryAllTagsUnderFriend(email);
            }
        });

        mFindThisTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = Integer.parseInt(mTagFilter.getText().toString());
                queryAllTags(tag);
            }
        });

        mFindTagFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tag = Integer.parseInt(mTagFilter.getText().toString());
                String email = mFriendEmailFilter.getText().toString();
                queryTagUnderFriend(tag,email);
            }
        });

        mAccept.setClickable(false);
        mCancel.setClickable(false);
        mDeny.setClickable(false);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void registerListener() {
        mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_USERS + "/"+ mUserKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("Main Activity","In add child event");
                if("sender".equals(dataSnapshot.getValue().toString())){
                    Log.d("Main Activity","sender = "+ dataSnapshot.getKey().toString());
                    mFriendRequestKey = dataSnapshot.getKey();
                    mAccept.setTextColor(getResources().getColor(R.color.colorAccent));
                    mDeny.setTextColor(getResources().getColor(R.color.colorAccent));
                    mAccept.setClickable(true);
                    mDeny.setClickable(true);
                } else if("receiver".equals(dataSnapshot.getValue().toString())){
                    mFriendRequestKey = dataSnapshot.getKey();
                    mCancel.setTextColor(getResources().getColor(R.color.colorAccent));
                    mCancel.setClickable(true);
                    Log.d("Main Activity","waiting for response");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if ("friend".equals(dataSnapshot.getValue())) {
                    Log.d("Main Activity","friend request accepted!");
                } else if ("not friend".equals(dataSnapshot.getValue())) {
                    Log.d("Main Activity","friend request failed!");
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createNewArticle() {
        String key = mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_ARTICLES).push().getKey();
        String articleContent = mArticleContent.getText().toString();
        String articleTitle = mArticleTitle.getText().toString();
        int tag  = Integer.parseInt(mArticleTag.getText().toString());
        String articleTag = null;
        String aatag = null;
        switch (tag){
            case TAG_1:
                articleTag = "表特";
                aatag = mMail + "_" + "表特";
                break;
            case TAG_2:
                articleTag ="八卦";
                aatag = mMail + "_" + "八卦";
                break;
            case TAG_3:
                articleTag ="就可";
                aatag = mMail + "_" + "就可";
                break;
            case TAG_4:
                articleTag ="生活";
                aatag = mMail + "_" + "生活";
                break;
            default:
                articleTag = "表特";
                aatag = mMail + "_" + "表特";
                break;
        }
        String time = Long.toString(System.currentTimeMillis() / 1000L);
        Article article = new Article(articleContent,articleTitle,articleTag,mMail,time,aatag);
        article.setArticle_id(key);
        mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_ARTICLES + "/" + key).setValue(article);
        Log.d("Main Activity", "new article added!");
    }

    private void startSession() {
        mMail = mUserEmail.getText().toString();
        Query query = mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_USERS).orderByChild("email").equalTo(mMail);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) {
                    addNewUser(mMail);
                    registerListener();
                } else {
                    Map<String,String> map = (Map)dataSnapshot.getValue();
                    for(String key :map.keySet()){
                        mUserKey = key;
                        Log.d("Main Activity","Login success: " + mUserKey);
                        registerListener();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    private void addNewUser(String userEmail) {
        String key = mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_USERS).push().getKey();
        String name = mNewUserName.getText().toString();
        mUserKey = key;
        mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_USERS + "/" + mUserKey).child("email").setValue(userEmail);
        mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_USERS + "/" + mUserKey).child("name").setValue(name);
    }

    private void queryAllTags(int tag) {
        String tagPath = null;
        switch (tag){
            case TAG_1:
                tagPath = "表特";
                break;
            case TAG_2:
                tagPath ="八卦";
                break;
            case TAG_3:
                tagPath ="就可";
                break;
            case TAG_4:
                tagPath ="生活";
                break;
        }

        Query query = mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_ARTICLES).orderByChild("article_tag").equalTo(tagPath);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Main Activity","article count = " + dataSnapshot.getChildrenCount());
                if (dataSnapshot.getChildrenCount() == 0) {
                    Log.d("Main Activity","no article yet!!");
                } else {
                    for(DataSnapshot d: dataSnapshot.getChildren()) {
                        Article article = d.getValue(Article.class);
                        Log.d("Main Activity","article content :" + article.getArticle_content());
                        Log.d("Main Activity","article title :" + article.getArticle_title());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void queryAllTagsUnderFriend(String email) {

        Query query = mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_ARTICLES).orderByChild("author").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Main Activity","article count =" + dataSnapshot.getChildrenCount());
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Article article = d.getValue(Article.class);
                    Log.d("Main Activity","article content: " + article.getArticle_content());
                    Log.d("Main Activity","article title: " + article.getArticle_title());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void queryTagUnderFriend(int tag,String email) {
        String tagPath = null;
        switch (tag){
            case TAG_1:
                tagPath = email + "_"+"表特";
                break;
            case TAG_2:
                tagPath = email + "_"+"八卦";
                break;
            case TAG_3:
                tagPath = email + "_"+"就可";
                break;
            case TAG_4:
                tagPath = email + "_"+"生活";
                break;
        }
        Query query = mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_ARTICLES).orderByChild("author_article_tag").equalTo(tagPath);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot){
                Log.d("Main Activity","article count = " + dataSnapshot.getChildrenCount());
                if (dataSnapshot.getChildrenCount() == 0) {
                    Log.d("Main Activity","No article yet!!");
                } else {
                    for(DataSnapshot d: dataSnapshot.getChildren()) {
                        Article article = d.getValue(Article.class);
                        Log.d("Main Activity","article content: " + article.getArticle_content());
                        Log.d("Main Activity","article title: " + article.getArticle_title());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ifFriendExist() {
        String email = mFriendEmail.getText().toString();
        Query query = mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_USERS).orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String friendKey;
                if (dataSnapshot.getValue() != null) {
                    Map<String, String> map = (Map) dataSnapshot.getValue();
                    for (String key : map.keySet()) {
                        mFriendKey = key;
                        Log.d("Main Activity", "Friend exists: " + mFriendKey);
                        sendFriendRequest();
                        break;
                    }
                } else {
                    Log.d("Main Activity", "Friend doesn't exist!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void negateFriendRequest() {
        mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_USERS + "/" + mFriendRequestKey).child(mUserKey).setValue("not friend");
        mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_USERS + "/" + mUserKey).child(mFriendRequestKey).setValue("not friend");
        mAccept.setTextColor(getResources().getColor(android.R.color.black));
        mDeny.setTextColor(getResources().getColor(android.R.color.black));
        mCancel.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void acceptFriendRequest() {
        mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_USERS + "/" + mFriendRequestKey).child(mUserKey).setValue("friend");
        mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_USERS + "/" + mUserKey).child(mFriendRequestKey).setValue("friend");
        mAccept.setTextColor(getResources().getColor(android.R.color.black));
        mDeny.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void sendFriendRequest() {
        mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_USERS + "/" + mUserKey).child(mFriendKey).setValue("receiver");
        mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_USERS + "/" + mFriendKey).child(mUserKey).setValue("sender");
    }
}
