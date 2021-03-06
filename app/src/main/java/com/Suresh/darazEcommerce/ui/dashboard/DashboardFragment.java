package com.Suresh.darazEcommerce.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Suresh.darazEcommerce.AdapterClass.FlashAdapter;
import com.Suresh.darazEcommerce.AdapterClass.ProductAdapter;
import com.Suresh.darazEcommerce.InterfaceClasses.BaseAPI;
import com.Suresh.darazEcommerce.InterfaceClasses.RetrofitInterface;
import com.Suresh.darazEcommerce.ModalClass.FlashModalClass;
import com.Suresh.darazEcommerce.ModalClass.ProductCollectionModal;
import com.Suresh.darazEcommerce.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    RecyclerView recyclerView_flash, recyclerView_product;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // rcyclerview code for flash sale
        recyclerView_flash = root.findViewById(R.id.flashRecycler);
        recyclerView_product = root.findViewById(R.id.productRecycler);
        // Data listing through Arraylist
        List<FlashModalClass> contactsList = new ArrayList<>();
        contactsList.add(new FlashModalClass(R.drawable.sample_image_one_flash, "1,200", "12 Sold"));
        contactsList.add(new FlashModalClass(R.drawable.sample_image_two_flash, "300", "18 Sold"));
        contactsList.add(new FlashModalClass(R.drawable.sample_image_three_flash, "2,000", "20 Sold"));

        // Flash Sale Set Adapter
        FlashAdapter FlashAdapter = new FlashAdapter(getActivity(), contactsList);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_flash.setLayoutManager(layoutManager);
        recyclerView_flash.setHasFixedSize(true);
        recyclerView_flash.setAdapter(FlashAdapter);

        // call function in order to load product
        product_function();

        return root;
    }

    public void product_function(){
        RetrofitInterface retrofitApiInterface = BaseAPI.getRetrofit().create(RetrofitInterface.class);
        Call<ProductCollectionModal> modalCollectionCall = retrofitApiInterface.parseProduct();
        // modal bata hunna kei ni
        modalCollectionCall.enqueue(new Callback<ProductCollectionModal>() {
            @Override
            public void onResponse(Call<ProductCollectionModal> call, Response<ProductCollectionModal> response) {
                System.out.println("Collection list " + response.body());

                ProductAdapter recyclerviewAdapter = new ProductAdapter(getActivity(),response.body().darazProductModalClasses());
                RecyclerView.LayoutManager mlayoutManager = new GridLayoutManager(getContext(), 2);
                recyclerView_product.setLayoutManager(mlayoutManager);
                recyclerView_product.setHasFixedSize(true);
                recyclerView_product.setAdapter(recyclerviewAdapter);
                recyclerviewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ProductCollectionModal> call, Throwable t) {

            }
        });

    }
}