package company.velo.velo;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by jyp08 on 2017-07-08.
 */

public class BikeListAdapter extends RecyclerView.Adapter<BikeListAdapter.BikeListAdapterViewHolder> {

    private String[][] mBikeListData;

    private final BikeListAdapterOnClickHandler mClikcHandler;
    Context mContext;
    Activity mActivity;

    public interface BikeListAdapterOnClickHandler {
        void onClick(String[] bikeData);
    }

    public BikeListAdapter(/*String[][] bikeListData,*/ BikeListAdapterOnClickHandler clickHandler) {
        mClikcHandler = clickHandler;
        //mBikeListData = bikeListData;
    }

    public class BikeListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView mBikeOwnerTextView;
        public final TextView mBikeLocationTextView;
        public final ImageView mBikeTypeImageView;

        public BikeListAdapterViewHolder(View view) {
            super(view);
            mBikeOwnerTextView = (TextView) view.findViewById(R.id.bike_owner);
            mBikeLocationTextView = (TextView) view.findViewById(R.id.bike_location);
            mBikeTypeImageView = (ImageView) view.findViewById(R.id.bike_type);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String[] bikeData = mBikeListData[adapterPosition];
            mClikcHandler.onClick(bikeData);
        }
    }

    @Override
    public BikeListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        mContext = parent.getContext();

        View view = inflater.inflate(R.layout.bike_list_item, parent, false);
        return new BikeListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BikeListAdapterViewHolder holder, int position) {
        String[] bikeData = mBikeListData[position];

        holder.mBikeOwnerTextView.setText(bikeData[0]);
        holder.mBikeLocationTextView.setText(bikeData[1]);

        // 각 view에 user와 bicycle간 거리 나타나게

        double distance, latitude = 0.0, longitude = 0.0;

        Location locationA = new Location("pointBike");

        locationA.setLatitude(Double.parseDouble(bikeData[3]));
        locationA.setLongitude(Double.parseDouble(bikeData[4]));

        Location locationB = new Location("pointUser");

        GpsInfo gps = new GpsInfo(mContext, mActivity);

        if (gps.isGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }

        locationB.setLatitude(latitude);
        locationB.setLongitude(longitude);

        distance = locationA.distanceTo(locationB);
        String dis = String.valueOf((int)distance)+" m";
        holder.mBikeLocationTextView.setText(dis);
        mBikeListData[position][5] = dis;

        int[] biketype_array = {R.drawable.biketype_1, R.drawable.biketype_2, R.drawable.biketype_3, R.drawable.biketype_4};
        int biketype_index = Integer.parseInt(bikeData[2]);
        holder.mBikeTypeImageView.setImageResource(biketype_array[biketype_index]);
    }

    @Override
    public int getItemCount() {
        if (mBikeListData == null) return 0;
        return mBikeListData.length;
    }

    public void setBikeListData(String[][] bikeListData) {
        mBikeListData = bikeListData;
        notifyDataSetChanged();
    }
}




