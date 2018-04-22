package in.theindianlifter.indianlifter.adapter;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.NinePatchDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shawnlin.numberpicker.NumberPicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import in.theindianlifter.indianlifter.R;
import in.theindianlifter.indianlifter.activity.ChatInfoActivity;
import in.theindianlifter.indianlifter.model.ChatMessage;

import static in.theindianlifter.indianlifter.constants.AppConstants.BOT_AGE_MESSAGE;
import static in.theindianlifter.indianlifter.constants.AppConstants.BOT_BODYTYPE_MESSAGE;
import static in.theindianlifter.indianlifter.constants.AppConstants.BOT_GENDER_MESSAGE;
import static in.theindianlifter.indianlifter.constants.AppConstants.BOT_HEIGHT_MESSAGE;
import static in.theindianlifter.indianlifter.constants.AppConstants.BOT_TEXT_MESSAGE;
import static in.theindianlifter.indianlifter.constants.AppConstants.BOT_WEIGHT_MESSAGE;
import static in.theindianlifter.indianlifter.constants.AppConstants.USER_MESSAGE;

/**
 * Created by rajatdhamija on 22/04/18.
 */

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private ArrayList<ChatMessage> messageList = new ArrayList<>();
    private Activity activity;
    private boolean isGenderChosen = false, isAgeChosen = false, isHeightChosen = false, isWeightChosen = false, isBodyTypeChosen = false;

    public ChatAdapter(ArrayList<ChatMessage> messageList, Activity activity) {
        this.messageList = messageList;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == USER_MESSAGE) {
            View view = LayoutInflater.from(activity).inflate(R.layout.row_message_user, parent, false);
            return new UserMessageViewHolder(view);
        } else if (viewType == BOT_TEXT_MESSAGE) {
            View view = LayoutInflater.from(activity).inflate(R.layout.row_message_bot, parent, false);
            return new BotTextMessageViewHolder(view);
        } else if (viewType == BOT_AGE_MESSAGE) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_age, parent, false);
            return new BotAgeMessageViewHolder(view);
        } else if (viewType == BOT_GENDER_MESSAGE) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_select_gender, parent, false);
            return new BotGenderMessageViewHolder(view);
        } else if (viewType == BOT_BODYTYPE_MESSAGE) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_body_type, parent, false);
            return new BotBodyTypeMessageViewHolder(view);
        } else if (viewType == BOT_HEIGHT_MESSAGE) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_height, parent, false);
            return new BotHeightMessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_weight, parent, false);
            return new BotWeightMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {
        final int position = holder.getAdapterPosition();
        final int itemType = getItemViewType(position);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
        ChatMessage chatMessage = messageList.get(position);
        switch (itemType) {
            case USER_MESSAGE:
                final UserMessageViewHolder userMessageViewHolder = (UserMessageViewHolder) holder;
                userMessageViewHolder.tvMessage.setText(chatMessage.getMessage());
                userMessageViewHolder.tvTime.setText(sdf.format(chatMessage.getDate()));
                break;
            case BOT_TEXT_MESSAGE:
                final BotTextMessageViewHolder botTextMessageViewHolder = (BotTextMessageViewHolder) holder;
                if (messageList.size() > 1 && (position - 1) > 0 && messageList.get(position - 1).getMessageType() == BOT_TEXT_MESSAGE) {
                    botTextMessageViewHolder.tvMessage.setBackgroundResource(R.drawable.chat_bg_normal);
                } else {
                    botTextMessageViewHolder.tvMessage.setBackgroundResource(R.drawable.chat_bg_leftt);
                }
                botTextMessageViewHolder.tvMessage.setText(chatMessage.getMessage());
                if (chatMessage.isShowDate()) {
                    botTextMessageViewHolder.tvTime.setVisibility(View.VISIBLE);
                    botTextMessageViewHolder.tvTime.setText(sdf.format(chatMessage.getDate()));
                } else {
                    botTextMessageViewHolder.tvTime.setVisibility(View.GONE);
                }
                NinePatchDrawable drawable2 = (NinePatchDrawable) botTextMessageViewHolder.tvMessage.getBackground();
                drawable2.setColorFilter(activity.getResources().getColor(R.color.bot_message_bg), PorterDuff.Mode.MULTIPLY);
                break;
            case BOT_AGE_MESSAGE:
                final BotAgeMessageViewHolder botAgeMessageViewHolder = (BotAgeMessageViewHolder) holder;
                botAgeMessageViewHolder.tvTime.setText(sdf.format(chatMessage.getDate()));
                botAgeMessageViewHolder.tvContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAgeChosen) {
                            ((ChatInfoActivity) activity).addMessageToList(botAgeMessageViewHolder.npAge.getValue(), 0, BOT_AGE_MESSAGE, messageList.size());
                            isAgeChosen = true;
                        }
                    }
                });
                break;
            case BOT_GENDER_MESSAGE:
                final BotGenderMessageViewHolder botGenderMessageViewHolder = (BotGenderMessageViewHolder) holder;
                botGenderMessageViewHolder.tvTime.setText(sdf.format(chatMessage.getDate()));
                botGenderMessageViewHolder.tvMale.setOnClickListener(this);
                botGenderMessageViewHolder.tvFemale.setOnClickListener(this);
                break;
            case BOT_HEIGHT_MESSAGE:
                final BotHeightMessageViewHolder botHeightMessageViewHolder = (BotHeightMessageViewHolder) holder;
                botHeightMessageViewHolder.tvTime.setText(sdf.format(chatMessage.getDate()));
                botHeightMessageViewHolder.tvContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isHeightChosen) {
                            ((ChatInfoActivity) activity).addMessageToList(botHeightMessageViewHolder.npFeet.getValue(), botHeightMessageViewHolder.npInches.getValue(), BOT_HEIGHT_MESSAGE, messageList.size());
                            isHeightChosen = true;
                        }
                    }
                });
                break;
            case BOT_WEIGHT_MESSAGE:
                final BotWeightMessageViewHolder botWeightMessageViewHolder = (BotWeightMessageViewHolder) holder;
                botWeightMessageViewHolder.tvTime.setText(sdf.format(chatMessage.getDate()));
                botWeightMessageViewHolder.tvContinue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isWeightChosen) {
                            ((ChatInfoActivity) activity).addMessageToList(botWeightMessageViewHolder.npWeight.getValue(), 0, BOT_WEIGHT_MESSAGE, messageList.size());
                            isWeightChosen = true;
                        }
                    }
                });
                break;
            case BOT_BODYTYPE_MESSAGE:
                final BotBodyTypeMessageViewHolder botBodyTypeMessageViewHolder = (BotBodyTypeMessageViewHolder) holder;
                botBodyTypeMessageViewHolder.tvTime.setText(sdf.format(chatMessage.getDate()));
                botBodyTypeMessageViewHolder.tvEcto.setOnClickListener(this);
                botBodyTypeMessageViewHolder.tvMeso.setOnClickListener(this);
                botBodyTypeMessageViewHolder.tvEndo.setOnClickListener(this);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getMessageType();
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvMale:
                if (!isGenderChosen) {
                    ((ChatInfoActivity) activity).addMessageToList(1, 0, BOT_GENDER_MESSAGE, messageList.size());
                    isGenderChosen = true;
                }
                break;
            case R.id.tvFemale:
                if (!isGenderChosen) {
                    ((ChatInfoActivity) activity).addMessageToList(2, 0, BOT_GENDER_MESSAGE, messageList.size());
                    isGenderChosen = true;
                }
                break;
            case R.id.tvEcto:
                if (!isBodyTypeChosen) {
                    ((ChatInfoActivity) activity).addMessageToList(1, 0, BOT_BODYTYPE_MESSAGE, messageList.size());
                    isBodyTypeChosen = true;
                }
                break;
            case R.id.tvMeso:
                if (!isBodyTypeChosen) {
                    ((ChatInfoActivity) activity).addMessageToList(2, 0, BOT_BODYTYPE_MESSAGE, messageList.size());
                    isBodyTypeChosen = true;
                }
                break;
            case R.id.tvEndo:
                if (!isBodyTypeChosen) {
                    ((ChatInfoActivity) activity).addMessageToList(3, 0, BOT_BODYTYPE_MESSAGE, messageList.size());
                    isBodyTypeChosen = true;
                }
                break;

        }
    }

    class UserMessageViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tvMessage, tvTime;

        UserMessageViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    class BotTextMessageViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tvMessage, tvTime;

        BotTextMessageViewHolder(View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }

    class BotGenderMessageViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tvTime, tvMale, tvFemale;

        BotGenderMessageViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvMale = itemView.findViewById(R.id.tvMale);
            tvFemale = itemView.findViewById(R.id.tvFemale);
        }
    }

    class BotAgeMessageViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tvTime, tvContinue;
        private NumberPicker npAge;

        BotAgeMessageViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvContinue = itemView.findViewById(R.id.tvContinue);
            npAge = itemView.findViewById(R.id.npAge);
        }
    }

    class BotHeightMessageViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tvTime, tvContinue;
        private NumberPicker npFeet, npInches;

        BotHeightMessageViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvContinue = itemView.findViewById(R.id.tvContinue);
            npFeet = itemView.findViewById(R.id.npFeet);
            npInches = itemView.findViewById(R.id.npInches);
        }
    }

    class BotWeightMessageViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tvTime, tvContinue;
        private NumberPicker npWeight;

        BotWeightMessageViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvContinue = itemView.findViewById(R.id.tvContinue);
            npWeight = itemView.findViewById(R.id.npWeight);
        }
    }

    class BotBodyTypeMessageViewHolder extends RecyclerView.ViewHolder {
        private AppCompatTextView tvTime, tvEcto, tvMeso, tvEndo;

        BotBodyTypeMessageViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvEcto = itemView.findViewById(R.id.tvEcto);
            tvMeso = itemView.findViewById(R.id.tvMeso);
            tvEndo = itemView.findViewById(R.id.tvEndo);
        }
    }

    public interface AddMessageToList {
        void addMessageToList(int option, int option2, int messageType, int position);
    }
}
