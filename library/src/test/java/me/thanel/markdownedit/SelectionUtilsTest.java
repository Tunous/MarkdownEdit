package me.thanel.markdownedit;

import static org.junit.Assert.assertEquals;

import static me.thanel.markdownedit.util.TestUtils.assertEqualsWithCursorPosition;
import static me.thanel.markdownedit.util.TestUtils.createEditableText;

import android.text.Editable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SelectionUtilsTest {
    @Test
    public void getSelectedText_returnsSelectedText_whenSelectedEverything() {
        Editable text = createEditableText("|Hello|");

        CharSequence selectedText = SelectionUtils.getSelectedText(text);

        assertEquals("Hello", selectedText.toString());
    }

    @Test
    public void getSelectedText_returnsSelectedText_whenSelectedPart() {
        Editable text = createEditableText("H|el|lo");

        CharSequence selectedText = SelectionUtils.getSelectedText(text);

        assertEquals("el", selectedText.toString());
    }

    @Test
    public void getSelectedText_returnsEmptyText_whenNotSelected() {
        Editable text = createEditableText("Hello");

        CharSequence selectedText = SelectionUtils.getSelectedText(text);

        assertEquals("", selectedText.toString());
    }

    @Test
    public void getSelectedText_returnsEmptyText_whenNotSelected_andCursorIsPresent() {
        Editable text = createEditableText("Hel|lo");

        CharSequence selectedText = SelectionUtils.getSelectedText(text);

        assertEquals("", selectedText.toString());
    }

    @Test
    public void selectWordAroundCursor_selectsWholeWord() {
        Editable text = createEditableText("Hel|lo");

        SelectionUtils.selectWordAroundCursor(text);

        assertEqualsWithCursorPosition("|Hello|", text);
    }

    @Test
    public void selectWordAroundCursor_doesNotSelectWhitespace() {
        Editable text = createEditableText("Hel|lo World");

        SelectionUtils.selectWordAroundCursor(text);

        assertEqualsWithCursorPosition("|Hello| World", text);
    }

    @Test
    public void selectWordAroundCursor_doesNotSelectNewLine() {
        Editable text = createEditableText("Hello\nWo|rld");

        SelectionUtils.selectWordAroundCursor(text);

        assertEqualsWithCursorPosition("Hello\n|World|", text);
    }

    @Test
    public void selectWordAroundCursor_selectsSpecialCharacters() {
        Editable text = createEditableText("~|~Hello~~");

        SelectionUtils.selectWordAroundCursor(text);

        assertEqualsWithCursorPosition("|~~Hello~~|", text);
    }

    @Test
    public void selectWordAroundCursor_doesNotChangeExistingSelection() {
        Editable text = createEditableText("He|llo|");

        SelectionUtils.selectWordAroundCursor(text);

        assertEqualsWithCursorPosition("He|llo|", text);
    }

    @Test
    public void replaceSelectedText_replacesSelection() {
        Editable text = createEditableText("He|ll|o");

        SelectionUtils.replaceSelectedText(text, "y");

        assertEqualsWithCursorPosition("Hey|o", text);
    }

    @Test
    public void replaceSelectedText_addsTextAtCursorPosition() {
        Editable text = createEditableText("He|llo");

        SelectionUtils.replaceSelectedText(text, "y he");

        assertEqualsWithCursorPosition("Hey he|llo", text);
    }

    @Test
    public void replaceSelectedText_ifNoSelectionInsertsTextAtBeginning() {
        Editable text = createEditableText("Hello");

        SelectionUtils.replaceSelectedText(text, "Hi, ");

        assertEqualsWithCursorPosition("Hi, |Hello", text);
    }

    @Test
    public void replaceSelectedText_ifCursorAtEndAppendsText() {
        Editable text = createEditableText("Hello|");

        SelectionUtils.replaceSelectedText(text, " World");

        assertEqualsWithCursorPosition("Hello World|", text);
    }
}
