package me.thanel.markdownedit;

import static me.thanel.markdownedit.util.TestUtils.assertEqualsWithCursorPosition;
import static me.thanel.markdownedit.util.TestUtils.createEditableText;

import android.text.Editable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SurroundTest {
    private final String SURROUND_TEXT = "**";

    @Test
    public void surroundSelectionWith_addsTagToEmptyText() {
        Editable text = createEditableText("");

        MarkdownEdit.surroundSelectionWith(text, SURROUND_TEXT);

        assertEqualsWithCursorPosition("**|**", text);
    }

    @Test
    public void surroundSelectionWith_surroundsSelectedText() {
        Editable text = createEditableText("|Hello|");

        MarkdownEdit.surroundSelectionWith(text, SURROUND_TEXT);

        assertEqualsWithCursorPosition("**Hello**|", text);
    }

    @Test
    public void surroundSelectionWith_surroundsSelectedText_whenItIsInsideOfWord() {
        Editable text = createEditableText("He|ll|o");

        MarkdownEdit.surroundSelectionWith(text, SURROUND_TEXT);

        assertEqualsWithCursorPosition("He**ll**|o", text);
    }

    @Test
    public void surroundSelectionWith_surroundsNearestWord() {
        Editable text = createEditableText("Hel|lo");

        MarkdownEdit.surroundSelectionWith(text, SURROUND_TEXT);

        assertEqualsWithCursorPosition("**Hello**|", text);
    }

    @Test
    public void surroundSelectionWith_trimsWhitespace() {
        Editable text = createEditableText("| Hello\n|");

        MarkdownEdit.surroundSelectionWith(text, SURROUND_TEXT);

        assertEqualsWithCursorPosition("**Hello**|", text);
    }

    @Test
    public void surroundSelectionWith_doesNotTouchWhitespaceOutsideSelection() {
        Editable text = createEditableText(" |Hello|\n");

        MarkdownEdit.surroundSelectionWith(text, SURROUND_TEXT);

        assertEqualsWithCursorPosition(" **Hello**|\n", text);
    }
}
