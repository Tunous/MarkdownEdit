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
public class DividerTagTest {
    @Test
    public void addDivider_addsDividerToEmptyText() {
        Editable text = createEditableText("");

        MarkdownEdit.addDivider(text);

        assertEqualsWithCursorPosition("-------\n|", text);
    }

    @Test
    public void addDivider_addsDividerAfterText() {
        Editable text = createEditableText("Hello|");

        MarkdownEdit.addDivider(text);

        assertEqualsWithCursorPosition("Hello\n\n-------\n|", text);
    }

    @Test
    public void addDivider_addsDividerBeforeText() {
        Editable text = createEditableText("|Hello");

        MarkdownEdit.addDivider(text);

        assertEqualsWithCursorPosition("-------\n|\nHello", text);
    }

    @Test
    public void addDivider_addsDividerBetweenText() {
        Editable text = createEditableText("He|llo");

        MarkdownEdit.addDivider(text);

        assertEqualsWithCursorPosition("He\n\n-------\n|\nllo", text);
    }

    @Test
    public void addDivider_replacesSelectionWithDivider() {
        Editable text = createEditableText("He|ll|o");

        MarkdownEdit.addDivider(text);

        assertEqualsWithCursorPosition("He\n\n-------\n|\no", text);
    }
}
