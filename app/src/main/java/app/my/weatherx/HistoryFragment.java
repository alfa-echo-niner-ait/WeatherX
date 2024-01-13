package app.my.weatherx;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;


public class HistoryFragment extends Fragment {

    private ArrayAdapter<String> adapter;
    private ListView listView;
    private Button clearButton;
    private List<String> cityList;
    private CityDatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        listView = rootView.findViewById(R.id.list_item_view);
        clearButton = rootView.findViewById(R.id.clear_btn);
        databaseHelper = new CityDatabaseHelper(requireContext());

        // Initialize ArrayAdapter
        cityList = databaseHelper.getAllCities();
        adapter = new ArrayAdapter<>(requireContext(), R.layout.list_item, cityList);
        // Set adapter to ListView
        listView.setAdapter(adapter);

        // Set item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Handle item click, for example, show details of the selected city
                String selectedCity = cityList.get(position);
                Toast.makeText(requireContext(), "City: " + selectedCity, Toast.LENGTH_SHORT).show();
            }
        });
        adapter.notifyDataSetChanged();

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelper.removeAll();
                updateCityList();
            }
        });

        return rootView;
    }


    public void updateCityList() {
        // Convert the set to an array or list if needed
        cityList = databaseHelper.getAllCities();

        // Update the adapter dataset
        if (adapter != null) {
            adapter.clear();
            adapter.addAll(cityList);
            adapter.notifyDataSetChanged();
        }
    }

}