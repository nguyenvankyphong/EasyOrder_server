package gameloft.com.easyorder_server;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Thong_Tin_Ban extends AppCompatActivity {
    Button btn_set_true, btn_set_false;
    TextView name, table, tsum;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;
    ListView lv;
    ArrayList<MonAn> list = new ArrayList<MonAn>() ;
    ThongTinBan_apt apt;
    ProgressDialog pro;
    int sum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong__tin__ban);
        //------------------------------------------------
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference=mFirebaseDatabase.getReference();
        btn_set_true=(Button)findViewById(R.id.btn_set_true);
        btn_set_false=(Button)findViewById(R.id.btn_set_false);
        name=(TextView) findViewById(R.id.user_name);
        table=(TextView) findViewById(R.id.user_table);
        tsum = (TextView)findViewById(R.id.user_sum);
        pro=new ProgressDialog(this);
        lv = (ListView)findViewById(R.id.lv_mon_an);
        apt= new ThongTinBan_apt(this,R.layout.item_thong_tin_ban,list);
        lv.setAdapter(apt);
        //------------------------------------------------
        table.setText(String.valueOf(quan_li_ban.table_number));
        mDatabaseReference.child("danhSachBanAn").child("ban" + String.valueOf(quan_li_ban.table_number))
                .child("khachHang").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        name.setText(dataSnapshot.child("tenKhachHang").getValue(String.class));
                        for(DataSnapshot data:dataSnapshot.child("danhSachMonAn").getChildren()) {

                            MonAn ma = new MonAn();
                            ma.setName(data.child("name").getValue(String.class));
                            ma.setDang_chon(data.child("dang_chon").getValue(int.class));
                            ma.setGia(data.child("gia").getValue(int.class));
                            ma.setTt(Integer.parseInt(data.getKey()));
                            list.add(ma);
                            sum+=ma.getDang_chon()*ma.getGia();
                            apt.notifyDataSetChanged();
                        }
                        tsum.setText(String.valueOf(sum));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        btn_set_true.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pro.setMessage("Đang thay đổi trạng thái");
                pro.setCancelable(false);
                mDatabaseReference.child("danhSachBanAn").child("ban" + String.valueOf(quan_li_ban.table_number)).child("state").setValue(true);
                pro.dismiss();
                onBackPressed();
            }
        });
        btn_set_false.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pro.setMessage("Đang thay đổi trạng thái");
                pro.setCancelable(false);
                mDatabaseReference.child("danhSachBanAn").child("ban" + String.valueOf(quan_li_ban.table_number)).child("state").setValue(false);
                mDatabaseReference.child("danhSachBanAn").child("ban" + String.valueOf(quan_li_ban.table_number))
                        .child("khachHang").removeValue();
                pro.dismiss();
                onBackPressed();
            }
        });
        //------------------------------------------------

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,quan_li_ban.class);
        startActivity(intent);
    }
}
