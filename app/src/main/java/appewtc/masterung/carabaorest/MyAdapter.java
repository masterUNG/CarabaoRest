package appewtc.masterung.carabaorest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by masterUNG on 4/29/15 AD.
 */
public class MyAdapter extends BaseAdapter{

    //Explicit
    private Context objContext;
    private String[] strShowFood, strShowPrice;
    private int[] intImageFood;

    public MyAdapter(Context objContext, String[] strShowFood, String[] strShowPrice, int[] intImageFood) {
        this.objContext = objContext;
        this.strShowFood = strShowFood;
        this.strShowPrice = strShowPrice;
        this.intImageFood = intImageFood;
    }   // Constructor


    @Override
    public int getCount() {
        return strShowFood.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater objLayoutInflater = (LayoutInflater) objContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View objView1 = objLayoutInflater.inflate(R.layout.listview_rom, viewGroup, false);

        //Show Name Food
        TextView listFood = (TextView) objView1.findViewById(R.id.txtShowFood);
        listFood.setText(strShowFood[i]);

        //Show Name Price
        TextView listPrice = (TextView) objView1.findViewById(R.id.txtShowPrice);
        listPrice.setText(strShowPrice[i]);

        //Show Image Food
        ImageView imvListFood = (ImageView) objView1.findViewById(R.id.imvFood);
        imvListFood.setBackgroundResource(intImageFood[i]);

        return objView1;
    }
}   // Main Class
