package com.hs.lxv3_1.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hs.lxv3_1.R;
import com.hs.lxv3_1.utils.BitmapDecoder;

/**
 * Created by Holy-Spirit on 2016/2/25.
 */
public class GraphicView extends LinearLayout {


    private ImageView mImag = null;
    private EditText mEditTxt = null;


    public GraphicView(Context context) {
        super(context);
    }

    public GraphicView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.layout_graphic, this);
        mImag = (ImageView) this.findViewById(R.id.image_view);
        ViewGroup.LayoutParams params = mImag.getLayoutParams();
        mEditTxt = (EditText) this.findViewById(R.id.edit_text);
        mEditTxt.addTextChangedListener(new MyTextWatcher());

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.GraphicView);
        int index = typedArray.getIndexCount();
        System.out.println("-->>index;"+index);
        int resId;
        for (int i = 0; i < index; i++) {

            int attr = typedArray.getIndex(i);

            switch (attr) {
                case R.styleable.GraphicView_inputType:

                    resId = typedArray.getInt(R.styleable.GraphicView_inputType, 0);
                    switch (resId) {
                        case 1:
                            mEditTxt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                            break;
                        case 2:
                            System.out.println("-->>input type:" + resId);
                            mEditTxt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            mEditTxt.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());
                            break;
                        default:
                            mEditTxt.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);
                            break;
                    }
                    break;


                case R.styleable.GraphicView_text:

                    resId = typedArray.getResourceId(R.styleable.GraphicView_text, 0);
                    mEditTxt.setText(resId > 0 ? typedArray.getResources().getText(resId) :
                            typedArray.getString(R.styleable.GraphicView_text));
                    mEditTxt.requestFocus();
                    mEditTxt.setSelection(mEditTxt.getText().length());

                    break;


                case R.styleable.GraphicView_textSize:

                    resId = typedArray.getResourceId(R.styleable.GraphicView_textSize, 0);
                    mEditTxt.setTextSize(resId > 0 ? typedArray.getResources().getDimension(resId) :
                            typedArray.getDimension(
                                    R.styleable.GraphicView_textSize, 0));

                    break;

                case R.styleable.GraphicView_textColor:

                    resId = typedArray.getResourceId(R.styleable.GraphicView_textColor, 0);
                    mEditTxt.setTextColor(
                            resId > 0 ? typedArray.getResources().getColor(resId) :
                                    typedArray.getColor(resId, 0));

                    break;


                case R.styleable.GraphicView_isSingleLine:

                    boolean isSingleLine =  typedArray.getBoolean(R.styleable.GraphicView_isSingleLine, false);
                    mEditTxt.setSingleLine(isSingleLine);

                    break;


                case R.styleable.GraphicView_src:

                    resId = typedArray.getResourceId(R.styleable.GraphicView_src, 0);
                    mImag.setImageBitmap(BitmapDecoder.decodeBitmapByResource(getResources(),resId,20,20));
                   // mImag.setImageResource(resId > 0 ? resId : R.drawable.ic_launcher);

                    break;

                case R.styleable.GraphicView_imgWidth:
                    resId = typedArray.getDimensionPixelSize(R.styleable.GraphicView_imgWidth,0);
                    params.width = resId;

                    break;

                case R.styleable.GraphicView_imgHeight:

                    resId = typedArray.getDimensionPixelSize(R.styleable.GraphicView_imgHeight, 0);
                    params.height = resId;
                    break;
            }


        }

        typedArray.recycle();
    }


    public void setText(String content){
        mEditTxt.setText(content);
    }


    public String getText(){
        return mEditTxt.getText().toString().trim();
    }


    private class MyTextWatcher implements TextWatcher{


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


}
