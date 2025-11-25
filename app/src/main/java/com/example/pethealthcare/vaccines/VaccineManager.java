package com.example.pethealthcare.vaccines;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class VaccineManager {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void fetchVaccines(FetchVaccinesCallback callback) {
        db.collection("vaccinations").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Vaccine> vaccineList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Vaccine vaccine = document.toObject(Vaccine.class);
                        vaccineList.add(vaccine);
                    }
                    callback.onVaccinesFetched(vaccineList);
                });
    }

    public void deleteVaccine(String id) {
        db.collection("vaccinations").document(id).delete();
    }

    public interface FetchVaccinesCallback {
        void onVaccinesFetched(List<Vaccine> vaccineList);
    }
}
