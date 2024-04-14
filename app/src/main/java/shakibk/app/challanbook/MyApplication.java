package shakibk.app.challanbook;
import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Source;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shakibk.app.challanbook.object.DtoCustomer;
import shakibk.app.challanbook.object.DtoShopInfo;

public class MyApplication extends Application {

    private Context context;
    @SuppressLint("StaticFieldLeak")
    private static MyApplication myApplication;
    public DtoShopInfo dtoShopInfo;
    public FirebaseAuth firebaseAuth;
    public FirebaseUser currentUser;;
    public FirebaseFirestore firestore;
    private DocumentReference  myDocShopRef;
    private Map<String,DtoCustomer> mapCustomers = new HashMap<>();
    public Map<String,Invoice> mapIncoice = new HashMap<>();
    private CollectionReference myCollCustomersRef;
    private CollectionReference myCollInvoiceRef;

    public MyApplication(){
    }

    public static MyApplication getIntence(){
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        myApplication = this;
        try {

            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            firebaseAuth = FirebaseAuth.getInstance();
            currentUser = firebaseAuth.getCurrentUser();
            firestore = FirebaseFirestore.getInstance();

            firebaseAuth.getCurrentUser().getUid();
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                    .build();
            firestore.setFirestoreSettings(settings);
            initializeShopInfo();
            initializeCustomer();
            initializeInvoice();
        }catch (NullPointerException e){
        }
    }

    public DtoShopInfo getShopInfo(){
        if (dtoShopInfo == null){
            initializeShopInfo();
        }
        return dtoShopInfo;
    }
    public void initializeShopInfo(){
        myDocShopRef = firestore.collection("Users").document(currentUser.getUid());
        myDocShopRef.get(Source.CACHE).addOnSuccessListener(documentSnapshot -> {
            Log.d(TAG, "DB_SHOP_CACHE");
            dtoShopInfo = documentSnapshot.toObject(DtoShopInfo.class);
        });

        myDocShopRef.addSnapshotListener((documentSnapshot, e) -> {
            Log.d(TAG, "DB_SHOP_SNAPSHOT");
            dtoShopInfo = documentSnapshot.toObject(DtoShopInfo.class);
        });
    }
    public void setShopInfo(DtoShopInfo dto) {
        dtoShopInfo = dto;
        myDocShopRef.set(dto);
    }
    public List<DtoCustomer> getCustomerList(){
        List<DtoCustomer> customerList = new ArrayList<>();

        for (String key: mapCustomers.keySet()){
            customerList.add(mapCustomers.get(key));
        }
        return customerList;
    }
    public void addCustomer(DtoCustomer dtoCustomer){
        myCollCustomersRef.add(dtoCustomer);
    }
    public void initializeCustomer(){
        myCollCustomersRef =firestore.collection("Users").document(currentUser.getUid()).collection("Customers");
        myCollCustomersRef.get(Source.CACHE).addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentChange> document = queryDocumentSnapshots.getDocumentChanges();
            Log.d(TAG, "DB_RECIPIENTS_CACHE Rows:" + document.size());
            for(int i=0; i<document.size(); i++){
                mapCustomers.put(document.get(i).getDocument().getId(), document.get(i).getDocument().toObject(DtoCustomer.class));
            }
        });
        myCollCustomersRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            List<DocumentChange> document = queryDocumentSnapshots.getDocumentChanges();
            Log.d(TAG, "DB_RECIPIENTS_SNAPSHOT Rows:" + document.size());
            for(int i=0; i<document.size(); i++){
                DocumentChange.Type state = document.get(i).getType();
                if(state==DocumentChange.Type.ADDED || state==DocumentChange.Type.MODIFIED){
                    mapCustomers.put(document.get(i).getDocument().getId(), document.get(i).getDocument().toObject(DtoCustomer.class));
                }else if(state==DocumentChange.Type.REMOVED){
                    mapCustomers.remove(document.get(i).getDocument().getId());
                }
            }
        });
    }
    public void saveInvoice(Invoice invoice){
        myCollInvoiceRef.add(invoice);
    }
    public List<Invoice> getInvoiceList(){
        List<Invoice> invoiceList = new ArrayList<>();
        for (String key : mapIncoice.keySet()){
            invoiceList.add(mapIncoice.get(key));
        }
        return invoiceList;
    }
    public void initializeInvoice(){
        myCollInvoiceRef = firestore.collection("Users").document(currentUser.getUid()).collection("Invoice");
        myCollInvoiceRef.get(Source.CACHE).addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentChange> document = queryDocumentSnapshots.getDocumentChanges();
            Log.d(TAG, "DB_INVOICES_CACHE Rows:" + document.size());

            for(int i=0; i<document.size(); i++){
                Invoice inv = document.get(i).getDocument().toObject(Invoice.class);
                inv.setDocID(document.get(i).getDocument().getId());
                mapIncoice.put(document.get(i).getDocument().getId(),inv);

            }
        });
        myCollInvoiceRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            List<DocumentChange> document = queryDocumentSnapshots.getDocumentChanges();
            Log.d(TAG, "DB_INVOICES_SNAPSHOT Rows:" + document.size());
            for(int i=0; i<document.size(); i++){
                Invoice inv = document.get(i).getDocument().toObject(Invoice.class);
                inv.setDocID(document.get(i).getDocument().getId());
                DocumentChange.Type state = document.get(i).getType();
                if(state==DocumentChange.Type.ADDED || state==DocumentChange.Type.MODIFIED){
                    mapIncoice.put(document.get(i).getDocument().getId(),inv);
                }else if(state==DocumentChange.Type.REMOVED){
                    mapIncoice.remove(document.get(i).getDocument().getId());
                }
            }
        });
    }
//    public void updateInvoiceData(Invoice dtInvoice){
//        myCollInvoiceRef.document(dtInvoice.docID).set(dtInvoice);
//    }
    public void deleteInvoiceData(String id){
        myCollInvoiceRef.document (id).delete ();
    }
//    public void savePdfLabel(String str) {
//        try {
//            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString(PDF_LABELS, str);
//            Log.d(TAG, "PDF_LABELS : " + str);
//            editor.apply();
//        }
//        catch (Exception e){
//            Log.e(TAG, "ERROR PDF_LABELS " + e.getMessage());
//        }
//    }
//    public String getPdfLabel(String name) {
//        try {
//            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
//            String strJson =  sharedPreferences.getString(PDF_LABELS, "{\"INVOICE\":\"INVOICE\", \"INVOICE_NO\":\"Invoice No\", \"REMARK\"" +
//                    ":\"Shipping Marks\", \"SIGNATURE\":\"Signature\", \"CURRENCY\":\"Rs.\", \"GROSS_WGT\":\"GROSS WGT\", \"TARE_WGT\":\"TARE WGT\"," +
//                    " \"NET_WGT\":\"NET WGT\", \"COPS\":\"COPS\" }");
//            JSONObject obj = new JSONObject(strJson);
//            return obj.getString(name);
//        }
//        catch (Exception e){
//            Log.e(TAG, "Error getpdf label for " + name + " " + e.getMessage());
//            return "";
//        }
//    }

}