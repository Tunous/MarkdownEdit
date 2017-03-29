package me.thanel.markdownedit.util;

import static org.junit.Assert.assertEquals;

import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;

import me.thanel.markdownedit.SelectionUtils;

public class TestUtils {
    private TestUtils() { /* cannot be instantiated */ }

    public static Editable createEditableText(String text) {
        String textWithoutSelection = text.replace("|", "");
        if (textWithoutSelection.length() < text.length() - 2) {
            throw new IllegalArgumentException(
                    "text: Can't specify more than 2 selections points (| character).");
        }

        int selectionStart = text.indexOf("|");
        int selectionEnd = Math.max(selectionStart, text.lastIndexOf("|") - 1);

        text = text.replace("|", "");

        Editable editable = new SpannableStringBuilder(text);
        if (selectionStart == -1) {
            Selection.removeSelection(editable);
        } else {
            Selection.setSelection(editable, selectionStart, selectionEnd);
        }
        return editable;
    }

    public static void assertEqualsWithCursorPosition(String expected, Spannable editText) {
        int startPosition = expected.indexOf('|');
        int endPosition = Math.max(startPosition, expected.lastIndexOf('|') - 1);

        expected = expected.replace("|", "");
        assertEquals("Incorrect text", expected, editText.toString());

        assertEquals("Incorrect start position", startPosition,
                SelectionUtils.getSelectionStart(editText));
        assertEquals("Incorrect end position", endPosition,
                SelectionUtils.getSelectionEnd(editText));
    }
}
