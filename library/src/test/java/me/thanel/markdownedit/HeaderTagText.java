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
public class HeaderTagText {
    @Test
    public void addHeader_addsFirstLevelHeader() {
        Editable text = createEditableText("");

        MarkdownEdit.addHeader(text, 1);

        assertEqualsWithCursorPosition("# |", text);
    }

    @Test
    public void addHeader_addsSecondLevelHeader() {
        Editable text = createEditableText("");

        MarkdownEdit.addHeader(text, 2);

        assertEqualsWithCursorPosition("## |", text);
    }

    @Test
    public void addHeader_addsThirdLevelHeader() {
        Editable text = createEditableText("");

        MarkdownEdit.addHeader(text, 3);

        assertEqualsWithCursorPosition("### |", text);
    }

    @Test
    public void addHeader_addsFourthLevelHeader() {
        Editable text = createEditableText("");

        MarkdownEdit.addHeader(text, 4);

        assertEqualsWithCursorPosition("#### |", text);
    }

    @Test
    public void addHeader_addsFifthLevelHeader() {
        Editable text = createEditableText("");

        MarkdownEdit.addHeader(text, 5);

        assertEqualsWithCursorPosition("##### |", text);
    }

    @Test
    public void addHeader_addsSixthLevelHeader() {
        Editable text = createEditableText("");

        MarkdownEdit.addHeader(text, 6);

        assertEqualsWithCursorPosition("###### |", text);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addHeader_doesNotAllowMoreThanSixHeadingLevels() {
        Editable text = createEditableText("");

        //noinspection Range
        MarkdownEdit.addHeader(text, 7);
    }

    @Test
    public void addHeader_turnsWholeLineToHeader() {
        Editable text = createEditableText("Hel|lo");

        MarkdownEdit.addHeader(text, 1);

        assertEqualsWithCursorPosition("# Hello|", text);
    }

    @Test
    public void addHeader_doesNotAddEmptyLines() {
        Editable text = createEditableText("Text\nHel|lo\ntest");

        MarkdownEdit.addHeader(text, 1);

        assertEqualsWithCursorPosition("Text\n# Hello\n|test", text);
    }

    @Test
    public void addHeader_turnsSelectionToHeader() {
        Editable text = createEditableText("Text\nH|el|lo\ntest");

        MarkdownEdit.addHeader(text, 1);

        assertEqualsWithCursorPosition("Text\nH\n# el\n|lo\ntest", text);
    }
}
