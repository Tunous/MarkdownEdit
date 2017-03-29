package me.thanel.markdownedit;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;

public class SelectionUtils {
    private SelectionUtils() { /* cannot be instantiated */ }

    public static int getSelectionStart(CharSequence text) {
        return Math.max(0, Selection.getSelectionStart(text));
    }

    public static int getSelectionEnd(CharSequence text) {
        return Math.max(0, Selection.getSelectionEnd(text));
    }

    public static void selectWordAroundCursor(@NonNull Spannable text) {
        int selectionStart = SelectionUtils.getSelectionStart(text);
        int selectionEnd = SelectionUtils.getSelectionEnd(text);
        if (selectionStart != selectionEnd) {
            return;
        }

        while (selectionStart > 0 && !Character.isWhitespace(text.charAt(selectionStart - 1))) {
            selectionStart -= 1;
        }
        while (selectionEnd < text.length() &&
                !Character.isWhitespace(text.charAt(selectionEnd))) {
            selectionEnd += 1;
        }

        Selection.setSelection(text, selectionStart, selectionEnd);
    }

    public static CharSequence getSelectedText(CharSequence text) {
        int selectionStart = SelectionUtils.getSelectionStart(text);
        int selectionEnd = SelectionUtils.getSelectionEnd(text);

        int min = Math.max(0, Math.min(selectionStart, selectionEnd));
        int max = Math.max(0, Math.max(selectionStart, selectionEnd));

        return text.subSequence(min, max);
    }

    public static void replaceSelectedText(Editable text, CharSequence replacementText) {
        int selectionStart = SelectionUtils.getSelectionStart(text);
        int selectionEnd = SelectionUtils.getSelectionEnd(text);

        int min = Math.max(0, Math.min(selectionStart, selectionEnd));
        int max = Math.max(0, Math.max(selectionStart, selectionEnd));

        text.replace(min, max, replacementText);
        Selection.setSelection(text, min + replacementText.length());
    }

    public static boolean hasSelection(CharSequence text) {
        int selectionStart = SelectionUtils.getSelectionStart(text);
        int selectionEnd = SelectionUtils.getSelectionEnd(text);

        int min = Math.max(0, Math.min(selectionStart, selectionEnd));
        int max = Math.max(0, Math.max(selectionStart, selectionEnd));

        return min < max;
    }
}
