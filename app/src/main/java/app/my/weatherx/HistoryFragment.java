package app.my.weatherx;

import android.os.Bundle;

import android.content.SharedPreferences;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;


public class HistoryFragment extends Fragment {

    private ArrayAdapter<String> adapter;
    private ListView listView;
    private List<String> cityList;
    private CityDatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        listView = rootView.findViewById(R.id.list_item_view);
        databaseHelper = new CityDatabaseHelper(requireContext());

        // Inside HistoryFragment
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("CityHistory", Context.MODE_PRIVATE);
        // Retrieve the city names from SharedPreferences
        Set<String> citySet = sharedPreferences.getStringSet("citySet", new HashSet<>());
        // Convert the set to an array or list if needed
        cityList = new ArrayList<>(citySet);
        // Initialize ArrayAdapter
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
        return rootView;
    }

    public void updateCityList() {
        // Convert the set to an array or list if needed
        List<String> cityList = databaseHelper.getAllCities();

        // Update the adapter dataset
        if (adapter != null) {
            adapter.clear();
            adapter.addAll(cityList);
            adapter.notifyDataSetChanged();
        }
    }

}