package app.my.weatherx;

import android.os.Bundle;

import android.content.SharedPreferences;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.ArrayList;


public class HistoryFragment extends Fragment {

    private List<String> dataList;
    private ArrayAdapter<String> adapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        listView = rootView.findViewById(R.id.list_item_view);
        dataList = readFromSharedPreferences();

        // Initialize ArrayAdapter
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, dataList);

        // Set adapter to ListView
        listView.setAdapter(adapter);

        // Set item click listener for ListView
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // Handle item click, e.g., open a fragment
            String selectedItem = dataList.get(position);
        });

        return rootView;
    }

    private List<String> readFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("locations", Context.MODE_PRIVATE);
        List<String> data = new ArrayList<>();

        // Retrieve data from SharedPreferences
        int itemCount = sharedPreferences.getInt("itemCount", 0);

        for (int i = 0; i < itemCount; i++) {
            String key = "item_" + i;
            String value = sharedPreferences.getString(key, "");
            data.add(value);
        }

        return data;
    }
}