package company.velo.velo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jyp08 on 2017-07-09.
 */

public class VelowListAdapter extends RecyclerView.Adapter<VelowListAdapter.VelowListAdapterViewHolder> {

    private String[][] mVelowListData;

    private final VelowListAdapterOnClickHandler mClickHandler;

    private String user_id;

    public interface VelowListAdapterOnClickHandler {
        void onClick(String[] velowData, Button button);
    }

    public VelowListAdapter(VelowListAdapterOnClickHandler clickHandler, String user_id) {
        mClickHandler = clickHandler;
        this.user_id = user_id;
    }

    public class VelowListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mVelowNameTextView;
        public final Button mVelowButton;
        public final ImageView mBikeTypeImageView;

        public VelowListAdapterViewHolder(View view) {
            super(view);
            mVelowNameTextView = (TextView) view.findViewById(R.id.velow_name);
            mVelowButton = (Button) view.findViewById(R.id.button3);
            mVelowButton.setOnClickListener(this);
//            view.setOnClickListener(this);
            mBikeTypeImageView = (ImageView) view.findViewById(R.id.profile_pic);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String[] velowData = mVelowListData[adapterPosition];
            mClickHandler.onClick(velowData, mVelowButton);
        }
    }

    @Override
    public VelowListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.velow_list_item, parent, false);
        return new VelowListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VelowListAdapterViewHolder holder, int position) {
        String[] velowData = mVelowListData[position];
        holder.mVelowNameTextView.setText(velowData[1]);

        int[] biketype_array = {R.drawable.biketype_1, R.drawable.biketype_2, R.drawable.biketype_3, R.drawable.biketype_4, R.drawable.user};
        int biketype_index = 0;

        if (velowData[2] == null){
            biketype_index = 4;
        } else {
            biketype_index = Integer.parseInt(velowData[2]);
        }

        holder.mBikeTypeImageView.setImageResource(biketype_array[biketype_index]);

        //getSharedPreferences("appData", MODE_PRIVATE).getString("user_id", "error");
        //appData.getString("user_id", "error")
        fetchButtonText(holder, velowData);
    }

    @Override
    public int getItemCount() {
        if (mVelowListData == null) return 0;
        return mVelowListData.length;
    }

    public void fetchButtonText(VelowListAdapterViewHolder holder, String[] velowData) {
        CheckVelow task = new CheckVelow(holder);
        task.setContext((VelowingListPage) mClickHandler);
        task.setString("CheckVelow.php", "phptest_CheckVelow");
        task.execute(new MyTaskParams(null,null,null,null,velowData[0],null,null,null));
    }

    public void setVelowListData(String[][] velowListData) {
        mVelowListData = velowListData;
        notifyDataSetChanged();
    }

    class CheckVelow extends AbstractInsertData {
        private VelowListAdapterViewHolder holder;

        public CheckVelow(VelowListAdapterViewHolder holder) {
            super();
            this.holder = holder;
        }

        @Override
        String doSomething(MyTaskParams... params) {
            String uid = user_id;
            return "uid=" + uid+"&vid="+params[0].string;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            Log.d(TAG, "POST response  - " + result);

            if(result.equals("[]"))
                holder.mVelowButton.setText("Velow");
            else
                holder.mVelowButton.setText("Velowing");

            Log.d(TAG, "ASDF");
        }
    }

}
