package genyus.com.whichmovie.view;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CurrencyTextView extends TextView {

    String rawText;

    public CurrencyTextView(Context context) {
        super(context);

    }

    public CurrencyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CurrencyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        rawText = text.toString();
        String price = text.toString();
        try {

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###,###", symbols);
            price = decimalFormat.format(Integer.parseInt(text.toString()));
        }catch (Exception e){
            e.printStackTrace();
        }

        super.setText(Html.fromHtml("<b>$</b> "+price.replaceAll(",", " ")), type);
    }

    @Override
    public CharSequence getText() {
        return rawText;
    }
}