package app.my.weatherx;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRCAdapter extends RecyclerView.Adapter<WeatherRCAdapter.ViewHolder> {

    private Context context;
    private ArrayList<WeatherRCModal> weatherRCModalArrayList;

    public WeatherRCAdapter(Context context, ArrayList<WeatherRCModal> weatherRCModalArrayList) {
        this.context = context;
        this.weatherRCModalArrayList = weatherRCModalArrayList;
    }

    @NonNull
    @Override
    public WeatherRCAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRCAdapter.ViewHolder holder, int position) {

        WeatherRCModal weatherRCModal = weatherRCModalArrayList.get(position);
        SimpleDateFormat input_date_format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output_date_format = new SimpleDateFormat("hh:mm aa");

        try {
            Date date_time = input_date_format.parse(weatherRCModal.getTime());
            holder.tv_time.setText(output_date_format.format(date_time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.tv_temp.setText(weatherRCModal.getTemperature() + "°C");
        Picasso.get().load("https:".concat(weatherRCModal.getIcon_img())).into(holder.iv_condition);
        holder.tv_condition.setText(weatherRCModal.getCondition_hour());
        holder.tv_wind.setText(weatherRCModal.getWind_speed() + " km/h");
    }

    @Override
    public int getItemCount() {
        return weatherRCModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_time, tv_temp, tv_wind, tv_condition;
        private ImageView iv_condition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_time = itemView.findViewById(R.id.tv_time);
            tv_temp = itemView.findViewById(R.id.tv_temp);
            tv_wind = itemView.findViewById(R.id.tv_wind);
            tv_condition = itemView.findViewById(R.id.tv_condition_hour);
            iv_condition = itemView.findViewById(R.id.iv_condition);
        }
    }
}
