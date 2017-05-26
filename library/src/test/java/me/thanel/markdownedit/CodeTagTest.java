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
public class CodeTagTest {

    @Test
    public void addCode_createsEmptyCodeTag() {
        Editable text = createEditableText("");

        MarkdownEdit.addCode(text);

        assertEqualsWithCursorPosition("`|`", text);
    }

    @Test
    public void addCode_surroundsSelectedTextWithInlineTag() {
        Editable text = createEditableText("|Hello|");

        MarkdownEdit.addCode(text);

        assertEqualsWithCursorPosition("`Hello`|", text);
    }

    @Test
    public void addCode_surroundsSelectedText_whenItIsInsideOfWord() {
        Editable text = createEditableText("He|ll|o");

        MarkdownEdit.addCode(text);

        assertEqualsWithCursorPosition("He`ll`|o", text);
    }

    @Test
    public void addCode_surroundsNearestWord() {
        Editable text = createEditableText("Hel|lo");

        MarkdownEdit.addCode(text);

        assertEqualsWithCursorPosition("`Hello`|", text);
    }

    @Test
    public void addCode_trimsWhitespace() {
        Editable text = createEditableText("| Hello\t|");

        MarkdownEdit.addCode(text);

        assertEqualsWithCursorPosition("`Hello`|", text);
    }

    @Test
    public void addCode_doesNotTouchWhitespaceOutsideSelection() {
        Editable text = createEditableText(" |Hello|\n");

        MarkdownEdit.addCode(text);

        assertEqualsWithCursorPosition(" `Hello`|\n", text);
    }

    @Test
    public void addCode_createsCodeBlock() {
        Editable text = createEditableText("|One\nTwo|");

        MarkdownEdit.addCode(text);

        assertEqualsWithCursorPosition("```\nOne\nTwo\n```|", text);
    }
}
