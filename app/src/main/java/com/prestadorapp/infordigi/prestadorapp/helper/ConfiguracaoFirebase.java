package com.prestadorapp.infordigi.prestadorapp.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFirebase {

    private static DatabaseReference referenceFirebase;
    private static FirebaseAuth referenceAuth;
    private static StorageReference referenceStorage;

    //retorna a referencia do database
    public static DatabaseReference getReferenceFirebase(){
        if(referenceFirebase == null){
            referenceFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenceFirebase;
    }

    //retornar a instancia do firebaseAuth
    public static FirebaseAuth getReferenceAuth(){
        if(referenceAuth == null){
           referenceAuth = FirebaseAuth.getInstance();
        }
        return referenceAuth;
    }

    public static StorageReference getReferenceStorage(){
        if(referenceStorage == null){
            referenceStorage = FirebaseStorage.getInstance().getReference();
        }
        return referenceStorage;
    }

}
