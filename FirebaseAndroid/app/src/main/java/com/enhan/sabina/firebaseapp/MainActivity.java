package com.enhan.sabina.firebaseapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.enhan.sabina.firebaseapp.model.Article;
import com.enhan.sabina.firebaseapp.model.Author;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int TAG_1 = 0x01;
    private static final int TAG_2 = 0x02;
    private static final int TAG_3 = 0x03;
    private static final int TAG_4 = 0x04;

    private Button mButton1;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
    private Button mQuery1;
    private Button mQuery2;
    private Button mQuery3;
    private Button mQuery4;
    private Button mQueryFriendAll;

    private Button mQueryAll1;
    private Button mQueryAll2;
    private Button mQueryAll3;
    private Button mQueryAll4;

    private Button mExistFriend;
    private Button mNonexistFriend;

    private Button mButtonUser;
    private DatabaseReference mDatabaseReference;
    private String mUserKey;
    private Author mAuthor;


    private List<Article> mArticlesTag1 = new ArrayList<>();
    private List<Article> mArticlesTag2 = new ArrayList<>();
    private List<Article> mArticlesTag3 = new ArrayList<>();
    private List<Article> mArticlesTag4 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton1 = findViewById(R.id.add_article1);
        mButton1.setText("表特article count =" + mArticlesTag1.size());
        mButton2 = findViewById(R.id.add_article2);
        mButton2.setText("八卦 article count =" + mArticlesTag2.size());
        mButton3 = findViewById(R.id.add_article3);
        mButton3.setText("就可 article count =" + mArticlesTag3.size());
        mButton4 = findViewById(R.id.add_article4);
        mButton4.setText("生活 article count =" + mArticlesTag4.size());
        mButtonUser = findViewById(R.id.add_user);

        mQuery1 = findViewById(R.id.querytag1);
        mQuery2 = findViewById(R.id.querytag2);
        mQuery3 = findViewById(R.id.querytag3);
        mQuery4 = findViewById(R.id.querytag4);
        mQueryFriendAll = findViewById(R.id.queryall);

        mQueryAll1 = findViewById(R.id.queryalltag1);
        mQueryAll2 = findViewById(R.id.queryalltag2);
        mQueryAll3 = findViewById(R.id.queryalltag3);
        mQueryAll4 = findViewById(R.id.queryalltag4);

        mExistFriend = findViewById(R.id.findmyfriend);
        mNonexistFriend = findViewById(R.id.findymynonexistfriend);

        mExistFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mExistFriend.getText().toString();
                ifFriendExist(email);
            }
        });

        mNonexistFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mNonexistFriend.getText().toString();
                ifFriendExist(email);
            }
        });

        mQuery1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getFriendEmail();
                queryTagUnderFriend(TAG_1,email);
//                mQuery1.setText("表特中...");
//                mQuery1.setClickable(false);
            }
        });

        mQuery2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getFriendEmail();
                queryTagUnderFriend(TAG_2,email);
//                mQuery2.setText("八卦中...");
//                mQuery2.setClickable(false);
            }
        });

        mQuery3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getFriendEmail();
                queryTagUnderFriend(TAG_3,email);
//                mQuery3.setText("就可中...");
//                mQuery3.setClickable(false);
            }
        });

        mQuery4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = getFriendEmail();
                queryTagUnderFriend(TAG_4,email);
            }
        });

        mQueryFriendAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAllTagsUnderFriend(getFriendEmail());
            }
        });

        mQueryAll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAllTags(TAG_1);
            }
        });

        mQueryAll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAllTags(TAG_2);
            }
        });
        mQueryAll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAllTags(TAG_3);
            }
        });
        mQueryAll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryAllTags(TAG_4);
            }
        });


        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Article article = mArticlesTag1.get(0);
                mArticlesTag1.remove(0);
                String articleKey = mDatabaseReference.child(Constants.FIREBASE_CHILD_ARTICLES).push().getKey();
                article.setArticle_id(articleKey);
                mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_ARTICLES + "/" + articleKey).setValue(article);
                if (mArticlesTag1.isEmpty()) {
                    mButton1.setVisibility(View.INVISIBLE);
                } else {
                    mButton1.setText("表特 article count =" + mArticlesTag1.size());
                }
//                updateUserArticle(articleKey,TAG_1);
//                updateTagArticle(articleKey,TAG_1);
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Article article = mArticlesTag2.get(0);
                mArticlesTag2.remove(0);
                String articleKey = mDatabaseReference.child(Constants.FIREBASE_CHILD_ARTICLES).push().getKey();
                article.setArticle_id(articleKey);
                mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_ARTICLES + "/" + articleKey).setValue(article);
                if (mArticlesTag2.isEmpty()) {
                    mButton2.setVisibility(View.INVISIBLE);
                } else {
                    mButton2.setText("八卦 article count =" + mArticlesTag2.size());
                }
//                updateUserArticle(articleKey,TAG_2);
//                updateTagArticle(articleKey,TAG_2);
            }
        });

        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Article article = mArticlesTag3.get(0);
                mArticlesTag3.remove(0);
                String articleKey = mDatabaseReference.child(Constants.FIREBASE_CHILD_ARTICLES).push().getKey();
                article.setArticle_id(articleKey);
                mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_ARTICLES + "/" + articleKey).setValue(article);
                if (mArticlesTag3.isEmpty()) {
                    mButton3.setVisibility(View.INVISIBLE);
                } else {
                    mButton3.setText("就可 article count =" + mArticlesTag3.size());
                }
//                updateUserArticle(articleKey,TAG_3);
//                updateTagArticle(articleKey,TAG_3);
            }
        });

        mButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Article article = mArticlesTag4.get(0);
                mArticlesTag4.remove(0);
                String articleKey = mDatabaseReference.child(Constants.FIREBASE_CHILD_ARTICLES).push().getKey();
                article.setArticle_id(articleKey);
                mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_ARTICLES + "/" + articleKey).setValue(article);
                if (mArticlesTag4.isEmpty()) {
                    mButton4.setVisibility(View.INVISIBLE);
                } else {
                    mButton4.setText("生活 article count =" + mArticlesTag4.size());
                }
//                updateUserArticle(articleKey,TAG_4);
//                updateTagArticle(articleKey,TAG_4);
            }
        });

        mButtonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addUser();
            }
        });
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

    }

    private String getFriendEmail() {
        return mAuthor.getEmail();
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
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Mainactivity","article count = "+dataSnapshot.getChildrenCount());
                if (dataSnapshot.getChildrenCount() == 0) {
                    Log.d("Mainactivity","no article yet!!");
                } else {
                    for(DataSnapshot d: dataSnapshot.getChildren()) {
                        Article article = d.getValue(Article.class);
                        Log.d("MainActivity",article.getArticle_content());
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
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("MainActivity",""+dataSnapshot.getChildrenCount());
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Article article = d.getValue(Article.class);
                    Log.d("MainActivity",article.getArticle_content());
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
                Log.d("Mainactivity","article count = "+dataSnapshot.getChildrenCount());
                if (dataSnapshot.getChildrenCount() == 0) {
                    Log.d("MainActivity","No article yet!!");
                } else {
                    for(DataSnapshot d: dataSnapshot.getChildren()) {
                        Article article = d.getValue(Article.class);
                        Log.d("MainActivity",article.getArticle_content());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean ifFriendExist(String email) {
        Query query = mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_USERS).orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String friendKey ;
                if(dataSnapshot.getValue()!= null){
                    Map<String,String> map = (Map)dataSnapshot.getValue();
                    for(String key :map.keySet()){
                        friendKey = key;
                        Log.d("MainActivity",friendKey);
////                        sendFriendRequest(friendKey);
//                        deleteFriendRequest();
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return true;
    }

    private void deleteFriendRequest() {
        mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_FRIENDREQUEST).setValue(null);
    }

    private void sendFriendRequest(String friendKey) {
        final String friendRequestKey = mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_FRIENDREQUEST).push().getKey();

        mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_FRIENDREQUEST+"/"+friendRequestKey).child("m1").setValue(mUserKey);
        mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_FRIENDREQUEST+"/"+friendRequestKey).child("m2").setValue(friendKey);
        mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_FRIENDREQUEST+"/"+friendRequestKey).child("invite").setValue("true");
        mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_FRIENDREQUEST+"/"+friendRequestKey).child("result").setValue("false");
        mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_USERS+"/"+mUserKey).child("friend_request").setValue(friendRequestKey);
        mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_USERS+"/"+friendKey).child("friend_request").setValue(friendRequestKey);

        mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_FRIENDREQUEST+"/"+friendRequestKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,String> map = (Map)dataSnapshot.getValue();
                String inviteStatus = map.get("invite");
                String resultStatus = map.get("result");
                String initiator = map.get("m1");
                String recipient = map.get("m2");


                if(resultStatus.equals("true") && inviteStatus.equals("true")){
                    mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_USERS+"/"+initiator).child("friends").child(recipient).setValue(true);
                    mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_USERS+"/"+initiator).child("friend_request").child(friendRequestKey).setValue(null);
                    mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_USERS+"/"+recipient).child("friends").child(initiator).setValue(true);
                    mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_USERS+"/"+recipient).child("friend_request").child(friendRequestKey).setValue(null);
                    mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_FRIENDREQUEST).child(friendRequestKey).setValue(null);

                } else if  (inviteStatus.equals("false") && resultStatus.equals("false")){

                    mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_USERS+"/"+initiator).child("friend_request").child(friendRequestKey).setValue(null);
                    mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_USERS+"/"+recipient).child("friend_request").child(friendRequestKey).setValue(null);
                    mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_FRIENDREQUEST).child(friendRequestKey).setValue(null);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_USERS+"/"+mUserKey).child("friend_request").setValue(friendRequestKey);
//        mDatabaseReference.child("/"+Constants.FIREBASE_CHILD_USERS+"/"+friendKey).child("friend_request").setValue(friendRequestKey);



    }

    private void addUser() {
        String email = "sabchang1@gmail.com";
//        email.replaceAll("\\.","");
        mAuthor = new Author(email,"Sabina");
//        mAuthor = new Author("123happysunday@gmail.com","Meiya");
        mUserKey = mDatabaseReference.child(Constants.FIREBASE_CHILD_USERS).push().getKey();
        mDatabaseReference.child("/"+ Constants.FIREBASE_CHILD_USERS + "/" + mUserKey).setValue(mAuthor);
//        deleteFriendRequest();
        setupArticles();
    }

    private void queryArticle(String articleId) {
        Query query = mDatabaseReference.child("/" + Constants.FIREBASE_CHILD_ARTICLES + "/" + articleId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Article article = dataSnapshot.getValue(Article.class);
                Log.d("Mainactivity","article_content = "+article.getArticle_content());
                Log.d("Mainactivity","article_title = "+article.getArticle_title());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupArticles() {
        mArticlesTag1 = new ArrayList<>();
        mArticlesTag2 = new ArrayList<>();
        mArticlesTag3 = new ArrayList<>();
        mArticlesTag4 = new ArrayList<>();

        Long time = new Date().getTime();
        mArticlesTag1.add(new Article("表特 first article. Coming from a SQL background as I did, it can take a while to grok the freedom of NoSQL data structures and the simplicity of Firebase's dynamic, real-time query environment.","表特1","表特",mAuthor.getEmail(), time.toString(),mAuthor.getEmail()+"_"+"表特"));
        mArticlesTag1.add(new Article("表特 second article. Part 1 of this double-header will will cover some of the common queries we know","表特2","表特",mAuthor.getEmail(),time.toString(),mAuthor.getEmail()+"_"+"表特"));

        mArticlesTag2.add(new Article("八卦 first article. .orderByChild( ) 按路徑下子節點的值做排序","八卦1","八卦",mAuthor.getEmail(),time.toString(),mAuthor.getEmail()+"_"+"八卦"));
        mArticlesTag2.add(new Article("八卦 second article. .orderByKey( ) 按節點的 key 做排序","八卦2","八卦",mAuthor.getEmail(),time.toString(),mAuthor.getEmail()+"_"+"八卦"));

        mArticlesTag3.add(new Article("就可 first article. .Our Firebase database will contain key-value ","就可1","就可",mAuthor.getEmail(),time.toString(),mAuthor.getEmail()+"_"+"就可"));
        mArticlesTag3.add(new Article("就可 second article.Each collection of data, called a node","就可2","就可",mAuthor.getEmail(),time.toString(),mAuthor.getEmail()+"_"+"就可"));

        mArticlesTag4.add(new Article("生活 first article. se data references are uniq","生活1","生活",mAuthor.getEmail(),time.toString(),mAuthor.getEmail()+"_"+"生活"));
        mArticlesTag4.add(new Article("生活 second article.mber for the resta","生活2","生活",mAuthor.getEmail(),time.toString(),mAuthor.getEmail()+"_"+"生活"));


        mButton1.setText("表特article count =" + mArticlesTag1.size());

        mButton2.setText("八卦 article count =" + mArticlesTag2.size());

        mButton3.setText("就可 article count =" + mArticlesTag3.size());

        mButton4.setText("生活 article count =" + mArticlesTag4.size());
    }


}
