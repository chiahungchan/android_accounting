package tw.chan.billy.accounting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class ShowExpenseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);

        ProgressBar pgb = findViewById(R.id.show_item_pb);
        pgb.setVisibility(View.GONE);
        RecyclerView rootVIew = findViewById(R.id.show_item_rv);
        rootVIew.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<ExpenseItem> a = new ArrayList<>();
        for(int i = 0; i < 10; i++)
            a.add(new ExpenseItem(12, "4/10", "500", null, "dinner", "my dinner"));
        ExpenseAdapter adapter = new ExpenseAdapter(a);
        rootVIew.setAdapter(adapter);
        rootVIew.setVisibility(View.VISIBLE);
    }
}
