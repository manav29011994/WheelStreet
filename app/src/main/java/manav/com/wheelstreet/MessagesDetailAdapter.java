package manav.com.wheelstreet;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by manav on 13/5/18.
 */

public class MessagesDetailAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int LEFT_MESSAGE_VIEW = 0;
    private static final int RIGHT_MESSAGE_VIEW = 1;
    Activity activity;
    private List<QuestionModel> items;

    public MessagesDetailAdapter(Activity activity, List<QuestionModel> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {

        if (items != null && items.size() > 0) {
                if (items.get(position).getId()!=null) {
                    return LEFT_MESSAGE_VIEW;
                } else {
                    return RIGHT_MESSAGE_VIEW;
                }
            } else {
                return -1;
            }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LEFT_MESSAGE_VIEW) {
            View messagesView = LayoutInflater.from(activity).inflate(R.layout.item_left, null, false);
            return new MessagesAdapterView(messagesView);
        } else if (viewType == RIGHT_MESSAGE_VIEW) {
            View messagesView = LayoutInflater.from(activity).inflate(R.layout.item_right, null, false);
            return new MessagesAdapterView(messagesView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MessagesAdapterView) {
            ((MessagesAdapterView) holder).setMessagesData(items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }


    public class MessagesAdapterView extends RecyclerView.ViewHolder {
        public TextView question;
        QuestionModel localAnswer;

        public MessagesAdapterView(View view) {
            super(view);
            question = (TextView) view.findViewById(R.id.txtMsgFrom);
        }


        public void setMessagesData(QuestionModel localMessageObject) {
            if (localMessageObject == null) {
                return;
            }
            if (!TextUtils.isEmpty(localMessageObject.getQuestion())) {
                question.setText(localMessageObject.getQuestion());
            }

        }

    }

}
