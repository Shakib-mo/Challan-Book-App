package shakibk.app.challanbook;
import static com.itextpdf.text.Element.ALIGN_CENTER;
import static com.itextpdf.text.Element.ALIGN_RIGHT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import shakibk.app.challanbook.adapter.View_Adapter;
import shakibk.app.challanbook.object.DtoShopInfo;
public class View_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Invoice> arrayList;
    View_Adapter adapter;
    Invoice invoice;
    DtoShopInfo dtoShopInfo;
    private static final int STORAGE_CODE = 1000;

    @SuppressLint({"MissingInflatedId", "NotifyDataSetChanged"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        recyclerView = findViewById(R.id.recyclerView);
        dtoShopInfo = MyApplication.getIntence().getShopInfo();


        View_Adapter.Clicklistner clicklistner = (position, v) -> {
            invoice = arrayList.get(position);
            try {
                invoice.dtoProducts.get(position);

                checkPermissionGenratePdf();

            }catch (IndexOutOfBoundsException e){
                Toast.makeText(View_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            openPdf();
        };
        arrayList = MyApplication.getIntence().getInvoiceList();
        if (arrayList == null){
            arrayList = new ArrayList<>();
        }else {
            arrayList = MyApplication.getIntence().getInvoiceList();
            adapter = new View_Adapter(View_Activity.this,arrayList,clicklistner);
            recyclerView.setLayoutManager(new LinearLayoutManager(View_Activity.this));
            recyclerView.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }

    public void genretPdf() throws IOException, DocumentException {
        Document document = new Document(PageSize.A4);

        String pdfPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + "InvoicePDF";
        File directory = new File(pdfPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Toast.makeText(this, "Failed to create directory", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        File file = new File(directory, "invoice-" + invoice.getBillNo() + ".pdf");

        try {
            if (file.exists()) {
                if (!file.delete()) {
                    Toast.makeText(this, "Failed to delete existing file", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (file.createNewFile()) {
                FileOutputStream fos = new FileOutputStream(file);
                PdfWriter.getInstance(document, fos);

                document.open();

                Paragraph userShop = new Paragraph();
                userShop.add(dtoShopInfo.getShopName()+"\n");
                userShop.add(dtoShopInfo.getShopAddress()+", "+dtoShopInfo.getShopStreet()+"\n");
                userShop.add(dtoShopInfo.getShopState()+", "+dtoShopInfo.getShopCity()+", "+dtoShopInfo.getShopPostCode());
                userShop.setAlignment(ALIGN_RIGHT);

                Font font = new Font(Font.FontFamily.TIMES_ROMAN, 14.0f, Font.NORMAL, BaseColor.BLUE.brighter());
                Paragraph p=new Paragraph("",font);
                p.add(dtoShopInfo.getShopEmail()+", "+dtoShopInfo.getShopMobileNo()+"\n");
                p.setAlignment(ALIGN_RIGHT);

                PdfPTable line = new PdfPTable(1);
                line.setWidthPercentage(100);
                PdfPCell cell = new PdfPCell(new Phrase("\n"));
                cell.setBorder(PdfPCell.BOTTOM);
                cell.setBorderColor(BaseColor.RED);
                line.addCell(cell);

                PdfPTable pdfPTable = new PdfPTable(2);
                Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL, BaseColor.BLUE);
                pdfPTable.setWidthPercentage(98);
                PdfPCell cInvoice = new PdfPCell(new Phrase("\n"+"Invoice",tableHeaderFont));
                cInvoice.setBorder(PdfPCell.NO_BORDER);

                pdfPTable.addCell(cInvoice);
                PdfPCell cBill = new PdfPCell(new Phrase("\n"+"Bill_No. 0"+invoice.getBillNo(),tableHeaderFont));
                cBill.setBorder(PdfPCell.NO_BORDER);
                cBill.setHorizontalAlignment(ALIGN_RIGHT);
                pdfPTable.addCell(cBill);

                PdfPTable tableName = new PdfPTable(2);
                tableName.setWidthPercentage(98);
                PdfPCell cName = new PdfPCell(new Phrase(invoice.dtoCustomer.getName()+"\n"+invoice.dtoCustomer.getAddress()+"\n"+"\n"));
                PdfPCell cDate = new PdfPCell(new Phrase(invoice.getBillData()));
                cName.setBorder(PdfPCell.NO_BORDER);
                cDate.setBorder(PdfPCell.NO_BORDER);
                cDate.setHorizontalAlignment(ALIGN_RIGHT);
                tableName.addCell(cName);
                tableName.addCell(cDate);

                float[] columwith = {2,7,3,3,5};
                PdfPTable tableProduct = new PdfPTable(columwith);
                tableProduct.setWidthPercentage(98);
                tableProduct.getDefaultCell().setPadding(4);
                tableProduct.getDefaultCell().setBorderWidth(1);
                PdfPCell sr = new PdfPCell(Phrase.getInstance("S.N"));
                PdfPCell de = new PdfPCell(Phrase.getInstance("DESCRIPTON"));
                PdfPCell un = new PdfPCell(Phrase.getInstance("UNIT PRICE"));
                PdfPCell qt = new PdfPCell(Phrase.getInstance("QUANTITY"));
                PdfPCell to = new PdfPCell(Phrase.getInstance("TOTAL"));
                sr.setHorizontalAlignment(ALIGN_CENTER);
                de.setHorizontalAlignment(ALIGN_CENTER);
                un.setHorizontalAlignment(ALIGN_CENTER);
                qt.setHorizontalAlignment(ALIGN_CENTER);
                to.setHorizontalAlignment(ALIGN_CENTER);

                tableProduct.addCell(sr);
                tableProduct.addCell(de);
                tableProduct.addCell(un);
                tableProduct.addCell(qt);
                tableProduct.addCell(to);


                float[] columwith1 = {2,7,3,3,5};
                PdfPTable tableProduct1 = new PdfPTable(columwith1);
                tableProduct1.setWidthPercentage(98);
                for (int i = 0;i<invoice.dtoProducts.size();i++){
                    PdfPCell p1 = new PdfPCell(Phrase.getInstance(String.valueOf(tableProduct1.size()+1)));
                    PdfPCell p2 = new PdfPCell(Phrase.getInstance(invoice.dtoProducts.get(i).getProductNama()));
                    PdfPCell p3 = new PdfPCell(Phrase.getInstance(String.valueOf(invoice.dtoProducts.get(i).getPrice())));
                    PdfPCell p4 = new PdfPCell(Phrase.getInstance(String.valueOf(invoice.dtoProducts.get(i).getQuantity())));
                    PdfPCell p5 = new PdfPCell(Phrase.getInstance(String.valueOf(invoice.dtoProducts.get(i).getTotalItemPrice())));
                    p1.setHorizontalAlignment(ALIGN_CENTER);
                    p2.setHorizontalAlignment(ALIGN_CENTER);
                    p3.setHorizontalAlignment(ALIGN_CENTER);
                    p4.setHorizontalAlignment(ALIGN_CENTER);
                    p5.setHorizontalAlignment(ALIGN_CENTER);

                    tableProduct1.addCell(p1);
                    tableProduct1.addCell(p2);
                    tableProduct1.addCell(p3);
                    tableProduct1.addCell(p4);
                    tableProduct1.addCell(p5);
                }

                PdfPTable tableProduct2 = new PdfPTable(1);
                tableProduct2.setWidthPercentage(98);
                tableProduct2.getDefaultCell().setPadding(4);
                PdfPCell pCell1 = new PdfPCell(Phrase.getInstance("TOTAL PRICE    "+invoice.totalQtyPrice.getTotalPrice()));

                pCell1.setHorizontalAlignment(ALIGN_RIGHT);
                tableProduct2.addCell(pCell1);

                PdfPTable tableSign = new PdfPTable(2);
                tableSign.setWidthPercentage(98);
                PdfPCell s1 = new PdfPCell(new Phrase("\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"Signature"));
                s1.setBorder(PdfPCell.NO_BORDER);

                tableSign.addCell(s1);
                PdfPCell s2 = new PdfPCell(new Phrase("\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"\n"+"Signature Marks"));
                s2.setBorder(PdfPCell.NO_BORDER);
                s2.setHorizontalAlignment(ALIGN_RIGHT);
                tableSign.addCell(s2);


                document.add(userShop);
                document.add(p);
                document.add(line);
                document.add(pdfPTable);
                document.add(tableName);
                document.add(tableProduct);
                document.add(tableProduct1);
                document.add(tableProduct2);
                document.add(tableSign);



                document.close();
                fos.close();

            } else {
                Toast.makeText(this, "Failed to create new file", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void openPdf() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String pdfPath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + File.separator + "InvoicePDF";

        File file = new File(pdfPath, "invoice-" + invoice.getBillNo() + ".pdf");

        Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", file);
        intent.setDataAndType(uri, "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_GRANT_READ_URI_PERMISSION);

        try {
            startActivity(intent);
        } catch (Throwable t) {
            Toast.makeText(this, "Error opening PDF: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("ObsoleteSdkInt")
    public void checkPermissionGenratePdf(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, STORAGE_CODE);
            }else {
                try {
                    genretPdf();
                } catch (IOException | DocumentException d) {
                    Toast.makeText(View_Activity.this, "Error generating PDF: "+d.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }else {
            try {
                genretPdf();
            }catch (IOException | DocumentException doc){
                Toast.makeText(View_Activity.this, "Error generating PDF: "+doc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    genretPdf();
                } catch (IOException | DocumentException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Permission Denide", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
