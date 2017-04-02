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
public class LinkTagsTest {
    @Test
    public void addImage_addsEmptyTag() {
        Editable text = createEditableText("");

        MarkdownEdit.addImage(text);

        assertEqualsWithCursorPosition("![|](url)", text);
    }

    @Test
    public void addImage_addsImageAtCursorPosition() {
        Editable text = createEditableText("Hello |");

        MarkdownEdit.addImage(text);

        assertEqualsWithCursorPosition("Hello ![|](url)", text);
    }

    @Test
    public void addImage_turnsSelectionIntoImageTitle() {
        Editable text = createEditableText("Test |Hell|o text");

        MarkdownEdit.addImage(text);

        assertEqualsWithCursorPosition("Test ![Hell](|url|)o text", text);
    }

    @Test
    public void addImage_turnsNearestWordIntoImageTitle() {
        Editable text = createEditableText("Test Hello| text");

        MarkdownEdit.addImage(text);

        assertEqualsWithCursorPosition("Test ![Hello](|url|) text", text);
    }

    @Test
    public void addImage_trimsSelectedText() {
        Editable text = createEditableText("Test | Hello\n| text");

        MarkdownEdit.addImage(text);

        assertEqualsWithCursorPosition("Test ![Hello](|url|) text", text);
    }

    @Test
    public void addLink_addsEmptyTag() {
        Editable text = createEditableText("");

        MarkdownEdit.addLink(text);

        assertEqualsWithCursorPosition("[|](url)", text);
    }

    @Test
    public void addLink_addsLinkAtCursorPosition() {
        Editable text = createEditableText("Hello |");

        MarkdownEdit.addLink(text);

        assertEqualsWithCursorPosition("Hello [|](url)", text);
    }

    @Test
    public void addLink_turnsSelectionIntoLinkTitle() {
        Editable text = createEditableText("Test |Hell|o text");

        MarkdownEdit.addLink(text);

        assertEqualsWithCursorPosition("Test [Hell](|url|)o text", text);
    }

    @Test
    public void addLink_turnsNearestWordIntoLinkTitle() {
        Editable text = createEditableText("Test Hello| text");

        MarkdownEdit.addLink(text);

        assertEqualsWithCursorPosition("Test [Hello](|url|) text", text);
    }

    @Test
    public void addLink_trimsSelectedText() {
        Editable text = createEditableText("Test | Hello\n| text");

        MarkdownEdit.addLink(text);

        assertEqualsWithCursorPosition("Test [Hello](|url|) text", text);
    }
}
