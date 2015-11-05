package it.bsdsoftware.dynamicquestion.library;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import java.util.Arrays;
import java.util.List;
import it.bsdsoftware.dynamicquestion.library.models.BSDChoiceModel;

/**
 * Created by Simone on 02/11/15.
 */
class MultiChoiceSpinner extends Spinner implements DialogInterface.OnMultiChoiceClickListener {

    private int styleTextResponse = -1;
    private List<BSDChoiceModel> _items = null;
    private boolean[] mSelection = null;
    private boolean[] mSelectionAtStart = null;
    private MultiChoiceModel _itemsAtStart = null;
    private MultiChoiceAdapter simple_adapter;
    private OnChangeSelectedItem onChangeSelectedItem;

    public MultiChoiceSpinner(Context context) {
        super(context);
        simple_adapter = new MultiChoiceAdapter(context);
        super.setAdapter(simple_adapter);
        simple_adapter.setStyleText(styleTextResponse);
    }

    public MultiChoiceSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        simple_adapter = new MultiChoiceAdapter(context);
        super.setAdapter(simple_adapter);
        simple_adapter.setStyleText(styleTextResponse);
    }

    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (mSelection != null && which < mSelection.length) {
            mSelection[which] = isChecked;
            simple_adapter.clear();
            simple_adapter.add(buildSelectedItems());
        } else {
            throw new IllegalArgumentException("Argument 'which' is out of bounds.");
        }
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.MyAlertDialog);
        builder.setMultiChoiceItems(getMultiArray(), mSelection, this);
        _itemsAtStart = buildSelectedItems();
        builder.setPositiveButton(R.string.submit_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.arraycopy(mSelection, 0, mSelectionAtStart, 0, mSelection.length);
                if(onChangeSelectedItem!=null) {
                    onChangeSelectedItem.onChange(simple_adapter.getItem(0));
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                simple_adapter.clear();
                simple_adapter.add(_itemsAtStart);
                System.arraycopy(mSelectionAtStart, 0, mSelection, 0, mSelectionAtStart.length);
            }
        });
        AlertDialog alertDialog = builder.create();
        ListView listView = alertDialog.getListView();
        listView.setDivider(null);
        listView.setDividerHeight(-1);
        alertDialog.show();

        return true;
    }

    @Override
    public boolean performLongClick() {
        return performClick();
    }

    @Override
    public boolean performContextClick() {
        return performClick();
    }

    private String[] getMultiArray(){
        String[] array = new String[_items.size()];
        for(int i = 0; i < _items.size(); i++) {
            array[i] = _items.get(i).getLabel();
        }
        return array;
    }

    public void selectItems(List<Integer> selection) {
        mSelection = new boolean[_items.size()];
        mSelectionAtStart = new boolean[_items.size()];
        Arrays.fill(mSelection, false);
        Arrays.fill(mSelectionAtStart, false);
        for(int sel : selection){
            for(int i = 0; i < _items.size(); i++){
                if(sel == _items.get(i).getValue()){
                    mSelection[i] = true;
                    mSelectionAtStart[i] = true;
                    break;
                }
            }
        }
        boolean ok = false;
        for(boolean b : mSelection){
            if(b) {
                ok = true;
                break;
            }
        }
        if(!ok){
            mSelection[0] = true;
            mSelectionAtStart[0] = true;
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItems());
        if(onChangeSelectedItem!=null) {
            onChangeSelectedItem.onChange(simple_adapter.getItem(0));
        }
    }


    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException(
                "setAdapter is not supported by MultiSelectSpinner.");
    }

    public void setItems(List<BSDChoiceModel> items) {
        _items = items;
        mSelection = new boolean[_items.size()];
        mSelectionAtStart = new boolean[_items.size()];
        simple_adapter.clear();
        MultiChoiceModel mcm = new MultiChoiceModel();
        mcm.getLabels().add(_items.get(0).getLabel());
        mcm.getValues().add(_items.get(0).getValue());
        simple_adapter.add(mcm);
        Arrays.fill(mSelection, false);
        mSelection[0] = true;
        mSelectionAtStart[0] = true;
    }

    public boolean isItemsVoid(){
        return _items==null || _items.size()==0;
    }

    public MultiChoiceModel buildSelectedItems() {
        MultiChoiceModel mcm = new MultiChoiceModel();
        for (int i = 0; i < _items.size(); ++i) {
            if (mSelection[i]) {
                mcm.getLabels().add(_items.get(i).getLabel());
                mcm.getValues().add(_items.get(i).getValue());
            }
        }
        return mcm;
    }

    public void setOnChangeSelectedItem(OnChangeSelectedItem onChangeSelectedItem) {
        this.onChangeSelectedItem = onChangeSelectedItem;
    }

    public interface OnChangeSelectedItem{
        void onChange(MultiChoiceModel multiChoiceModel);
    }

    public void setStyleTextResponse(int styleTextResponse) {
        this.styleTextResponse = styleTextResponse;
        if(simple_adapter!=null)
            simple_adapter.setStyleText(styleTextResponse);
    }


}