package me.thanel.markdownedit.sample;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import me.thanel.markdownedit.MarkdownEdit;

public class MainActivity extends AppCompatActivity {

    private EditText inputField;

    private interface DialogCallback {
        void onConfirm(CharSequence inputText);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputField = (EditText) findViewById(R.id.input_filed);
    }

    public void makeBold(View view) {
        MarkdownEdit.addBold(inputField);
    }

    public void makeItalic(View view) {
        MarkdownEdit.addItalic(inputField);
    }

    public void makeStrikeThrough(View view) {
        MarkdownEdit.addStrikeThrough(inputField);
    }

    public void makeHeader(View view) {
        MarkdownEdit.addHeader(inputField, 1);
    }

    public void makeImage(View view) {
        MarkdownEdit.addImage(inputField);
    }

    public void makeLink(View view) {
        MarkdownEdit.addLink(inputField);
    }

    public void makeDivider(View view) {
        MarkdownEdit.addDivider(inputField);
    }

    public void makeOrderedList(View view) {
        MarkdownEdit.addList(inputField, MarkdownEdit.LIST_TYPE_NUMBERS);
    }

    public void makeBulletList(View view) {
        MarkdownEdit.addList(inputField, MarkdownEdit.LIST_TYPE_BULLETS);
    }

    public void makeTasksList(View view) {
        MarkdownEdit.addList(inputField, MarkdownEdit.LIST_TYPE_TASKS);
    }

    public void makeCode(View view) {
        MarkdownEdit.addCode(inputField);
    }

    public void makeQuote(View view) {
        displaySingleInputDialog(R.string.add_quote, R.string.quote_hint, new DialogCallback() {
            @Override
            public void onConfirm(CharSequence inputText) {
                if (TextUtils.isEmpty(inputText)) {
                    MarkdownEdit.addQuote(inputField);
                } else {
                    MarkdownEdit.addQuote(inputField, inputText);
                }
            }
        });
    }

    private void displaySingleInputDialog(@StringRes int titleResId, @StringRes int hintResId,
            @NonNull final DialogCallback callback) {
        View dialogView = View.inflate(this, R.layout.dialog_single_input_field, null);
        final TextView dialogInput = (TextView) dialogView.findViewById(R.id.input_filed);
        dialogInput.setHint(hintResId);

        new AlertDialog.Builder(this)
                .setTitle(titleResId)
                .setView(dialogView)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callback.onConfirm(dialogInput.getText());
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
