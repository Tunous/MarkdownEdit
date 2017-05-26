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
public class ListTagsTest {
    @Test
    public void addList_createsEmptyBulletList() {
        Editable text = createEditableText("");

        MarkdownEdit.addList(text, MarkdownEdit.LIST_TYPE_BULLETS);

        assertEqualsWithCursorPosition("- |", text);
    }

    @Test
    public void addList_createsEmptyOrderedList() {
        Editable text = createEditableText("");

        MarkdownEdit.addList(text, MarkdownEdit.LIST_TYPE_NUMBERS);

        assertEqualsWithCursorPosition("1. |", text);
    }

    @Test
    public void addList_createsEmptyTasksList() {
        Editable text = createEditableText("");

        MarkdownEdit.addList(text, MarkdownEdit.LIST_TYPE_TASKS);

        assertEqualsWithCursorPosition("- [ ] |", text);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addList_throwsExceptionOnUnknownType() {
        Editable text = createEditableText("");

        //noinspection WrongConstant
        MarkdownEdit.addList(text, 5);

        assertEqualsWithCursorPosition("- [ ] |", text);
    }

    @Test
    public void addList_turnsWholeLineToList() {
        Editable text = createEditableText("Hel|lo");

        MarkdownEdit.addList(text, MarkdownEdit.LIST_TYPE_BULLETS);

        assertEqualsWithCursorPosition("- Hello|", text);
    }

    @Test
    public void addList_addsEmptyLines() {
        Editable text = createEditableText("Text\nHel|lo\ntest");

        MarkdownEdit.addList(text, MarkdownEdit.LIST_TYPE_BULLETS);

        assertEqualsWithCursorPosition("Text\n\n- Hello\n|\ntest", text);
    }

    @Test
    public void addList_turnsSelectionToList() {
        Editable text = createEditableText("Text\nH|el|lo\ntest");

        MarkdownEdit.addList(text, MarkdownEdit.LIST_TYPE_BULLETS);

        assertEqualsWithCursorPosition("Text\nH\n\n- el\n|\nlo\ntest", text);
    }

    @Test
    public void addList_turnsEachLineToSeparateEntry() {
        Editable text = createEditableText("One\n|Two\nThree\nFour|\nFive");

        MarkdownEdit.addList(text, MarkdownEdit.LIST_TYPE_BULLETS);

        assertEqualsWithCursorPosition("One\n\n- Two\n- Three\n- Four\n|\nFive", text);
    }
}
