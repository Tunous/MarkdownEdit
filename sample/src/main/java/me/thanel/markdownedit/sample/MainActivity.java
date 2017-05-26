package me.thanel.markdownedit.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import me.thanel.markdownedit.MarkdownEdit;

public class MainActivity extends AppCompatActivity {

    private EditText inputField;

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
        MarkdownEdit.addQuote(inputField);
    }
}
