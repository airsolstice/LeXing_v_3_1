package com.hs.lxv3_1.main;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

/**
 * Created by Holy-Spirit on 2016/5/11.
 */
public interface SearchBarListener extends TextWatcher,View.OnClickListener{

    @Override
    void onClick(View v);

    @Override
    void afterTextChanged(Editable s);

    @Override
    void beforeTextChanged(CharSequence s, int start, int count, int after);

    @Override
    void onTextChanged(CharSequence s, int start, int before, int count);
}
