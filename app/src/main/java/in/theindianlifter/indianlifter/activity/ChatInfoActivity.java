package in.theindianlifter.indianlifter.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

import in.theindianlifter.indianlifter.R;
import in.theindianlifter.indianlifter.adapter.ChatAdapter;
import in.theindianlifter.indianlifter.model.ChatMessage;

import static in.theindianlifter.indianlifter.constants.AppConstants.AGE;
import static in.theindianlifter.indianlifter.constants.AppConstants.BODY_TYPE;
import static in.theindianlifter.indianlifter.constants.AppConstants.BOT_AGE_MESSAGE;
import static in.theindianlifter.indianlifter.constants.AppConstants.BOT_BODYTYPE_MESSAGE;
import static in.theindianlifter.indianlifter.constants.AppConstants.BOT_GENDER_MESSAGE;
import static in.theindianlifter.indianlifter.constants.AppConstants.BOT_HEIGHT_MESSAGE;
import static in.theindianlifter.indianlifter.constants.AppConstants.BOT_TEXT_MESSAGE;
import static in.theindianlifter.indianlifter.constants.AppConstants.BOT_WEIGHT_MESSAGE;
import static in.theindianlifter.indianlifter.constants.AppConstants.GENDER;
import static in.theindianlifter.indianlifter.constants.AppConstants.HEIGHT;
import static in.theindianlifter.indianlifter.constants.AppConstants.USERS;
import static in.theindianlifter.indianlifter.constants.AppConstants.USER_MESSAGE;
import static in.theindianlifter.indianlifter.constants.AppConstants.WEIGHT;

public class ChatInfoActivity extends BaseActivity implements View.OnClickListener, ChatAdapter.AddMessageToList {
    private RecyclerView rvChat;
    private AppCompatEditText etMessage;
    private AppCompatImageView ivSend;
    private ArrayList<String> defaultMessages = new ArrayList<>();
    private int position = 0;
    private ArrayList<ChatMessage> messageList = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private String age, weight, height, bodyType, gender;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase myDb;
    DatabaseReference mainRef, userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_info);
        setUpFirebase();
        initializeViews();
        addListeners();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addIntroMessageToList(position);
            }
        }, 700);
    }

    private void addIntroMessageToList(int pos) {
        int DELAY;
        if (pos < 2) {
            DELAY = (pos + 1) * 700;
        } else {
            DELAY = 1500;
        }
        if (pos == 3) {
            messageList.add(new ChatMessage(defaultMessages.get(pos), BOT_GENDER_MESSAGE, new Date(), true));
        } else if (pos == 2) {
            messageList.add(new ChatMessage(defaultMessages.get(pos), BOT_TEXT_MESSAGE, new Date(), true));
        } else {
            messageList.add(new ChatMessage(defaultMessages.get(pos), BOT_TEXT_MESSAGE, new Date(), false));
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (position < 3) {
                    position = position + 1;
                    addIntroMessageToList(position);
                }
            }
        }, DELAY);
        chatAdapter.notifyItemInserted(pos);
    }

    private void initializeViews() {
        rvChat = findViewById(R.id.rvChat);
        etMessage = findViewById(R.id.etMessage);
        ivSend = findViewById(R.id.ivSend);
        chatAdapter = new ChatAdapter(messageList, this);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(chatAdapter);
        defaultMessages.add("Hello ");
        defaultMessages.add("Welcome to The Indian Lifter");
        defaultMessages.add("Please answer a few questions to help us know what is best for you.");
        defaultMessages.add("");
    }

    private void addListeners() {
        ivSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivSend:
                break;
            default:
                break;
        }
    }


    @Override
    public void addMessageToList(int option, int option2, int messageType, int position) {
        String message = "";
        switch (messageType) {
            case BOT_GENDER_MESSAGE:
                switch (option) {
                    case 1:
                        message = "I am a Male";
                        gender = "Male";
                        break;
                    case 2:
                        message = "I am a Female";
                        gender = "Female";
                        break;
                }
                break;
            case BOT_AGE_MESSAGE:
                message = "I am " + option + " years young";
                age = String.valueOf(option);
                break;
            case BOT_HEIGHT_MESSAGE:
                message = "I am " + option + "' " + option2 + "\"" + " tall";
                height = String.valueOf((option * 12) + option2);
                break;
            case BOT_WEIGHT_MESSAGE:
                message = "I weigh " + option + " kgs";
                weight = String.valueOf(option);
                break;
            case BOT_BODYTYPE_MESSAGE:
                switch (option) {
                    case 1:
                        message = "I am an Ectomorph";
                        bodyType = "Ectomorph";
                        break;
                    case 2:
                        message = "I am a Mesomorph";
                        bodyType = "Mesomorph";
                        break;
                    case 3:
                        message = "I am an Endomorph";
                        bodyType = "Endomorph";
                        break;
                }
                break;
        }
        addUserMessageToList(message, messageType, position);
    }

    public void addUserMessageToList(String message, final int messageType, final int position) {
        messageList.add(new ChatMessage(message, USER_MESSAGE, new Date(), true));
        chatAdapter.notifyItemInserted(position);
        rvChat.scrollToPosition(position);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (messageType) {
                    case BOT_GENDER_MESSAGE:
                        messageList.add(new ChatMessage("", BOT_AGE_MESSAGE, new Date(), true));
                        break;
                    case BOT_AGE_MESSAGE:
                        messageList.add(new ChatMessage("", BOT_HEIGHT_MESSAGE, new Date(), true));
                        break;
                    case BOT_HEIGHT_MESSAGE:
                        messageList.add(new ChatMessage("", BOT_WEIGHT_MESSAGE, new Date(), true));
                        break;
                    case BOT_WEIGHT_MESSAGE:
                        messageList.add(new ChatMessage("", BOT_BODYTYPE_MESSAGE, new Date(), true));
                        break;
                    case BOT_BODYTYPE_MESSAGE:
                        messageList.add(new ChatMessage("Thank you, for the information.", BOT_TEXT_MESSAGE, new Date(), true));
                        user = mAuth.getCurrentUser();
                        if (user != null) {
                            userRef = mainRef.child(user.getUid());
                            userRef.child(AGE).setValue(age);
                            userRef.child(HEIGHT).setValue(height);
                            userRef.child(WEIGHT).setValue(weight);
                            userRef.child(GENDER).setValue(gender);
                            userRef.child(BODY_TYPE).setValue(bodyType);
                        }
                        break;
                }
                chatAdapter.notifyItemInserted(position + 1);
                rvChat.scrollToPosition(position + 1);
            }
        }, 1000);
    }

    private void setUpFirebase() {
        mAuth = FirebaseAuth.getInstance();
        myDb = FirebaseDatabase.getInstance();
        mainRef = myDb.getReference(USERS);
    }
}
