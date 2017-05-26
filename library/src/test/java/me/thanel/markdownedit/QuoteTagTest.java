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
public class QuoteTagTest {

    @Test
    public void addQuote_createsEmptyQuote() {
        Editable text = createEditableText("");

        MarkdownEdit.addQuote(text);

        assertEqualsWithCursorPosition("> |", text);
    }

    @Test
    public void addQuote_turnsSelectionToQuote() {
        Editable text = createEditableText("|Hello|");

        MarkdownEdit.addQuote(text);

        assertEqualsWithCursorPosition("> Hello|", text);
    }

    @Test
    public void addQuote_addsEmptyLines() {
        Editable text = createEditableText("He|ll|o");

        MarkdownEdit.addQuote(text);

        assertEqualsWithCursorPosition("He\n\n> ll\n|\no", text);
    }

    @Test
    public void addQuote_turnsNearestWordToQuote() {
        Editable text = createEditableText("Hel|lo");

        MarkdownEdit.addQuote(text);

        assertEqualsWithCursorPosition("> Hello|", text);
    }

    @Test
    public void addQuote_trimsWhitespace() {
        Editable text = createEditableText("| Hello\t|");

        MarkdownEdit.addQuote(text);

        assertEqualsWithCursorPosition("> Hello|", text);
    }

    @Test
    public void addQuote_doesNotTouchWhitespaceOutsideSelection() {
        Editable text = createEditableText(" |Hello|\n");

        MarkdownEdit.addQuote(text);

        assertEqualsWithCursorPosition(" \n\n> Hello\n|", text);
    }

    @Test
    public void addQuote_createsBlockQuote() {
        Editable text = createEditableText("|One\nTwo|");

        MarkdownEdit.addQuote(text);

        assertEqualsWithCursorPosition("> One\n> Two|", text);
    }
}
