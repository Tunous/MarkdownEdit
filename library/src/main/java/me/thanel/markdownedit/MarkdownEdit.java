package me.thanel.markdownedit;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;

public class MarkdownEdit {
    public static final int LIST_TYPE_BULLETS = 0;
    public static final int LIST_TYPE_NUMBERS = 1;
    public static final int LIST_TYPE_TASKS = 2;

    private MarkdownEdit() { /* cannot be instantiated */ }

    /**
     * Surrounds the selected text with Markdown bold tag "**text**", or if no text is selected
     * inserts empty bold tag at the current cursor position.
     *
     * @param text The {@link Editable} text to which to add Markdown bold tag.
     */
    public static void addBold(@NonNull Editable text) {
        surroundSelectionWith(text, "**");
    }

    /**
     * Surrounds the selected text with Markdown italic tag "_text_", or if no text is selected
     * inserts empty italic tag at the current cursor position.
     *
     * @param text The Editable text to which to add Markdown italic tag.
     */
    public static void addItalic(@NonNull Editable text) {
        surroundSelectionWith(text, "_");
    }

    /**
     * Surrounds the selected text with Markdown strike-through tag "~~text~~", or if no text is
     * selected inserts empty strike-through tag at the current cursor position.
     *
     * @param text The {@link Editable} text to which to add Markdown strike-through tag.
     */
    public static void addStrikeThrough(@NonNull Editable text) {
        surroundSelectionWith(text, "~~");
    }

    /**
     * Turns the selected text to Markdown image tag "![title](url)" treating selection as a title.
     * <p>
     * If no text is selected the cursor will be positioned inside of the title tag, otherwise the
     * url marker will be selected and previously selected text will be inserted as a image title.
     *
     * @param text The {@link Editable} text to which to add Markdown image tag.
     */
    public static void addImage(@NonNull Editable text) {
        addLink(text, true);
    }

    /**
     * Turns the selected text to Markdown link tag "[title](url)" treating selection as a title.
     * <p>
     * If no text is selected the cursor will be positioned inside of the title tag, otherwise the
     * url marker will be selected and previously selected text will be inserted as a link title.
     *
     * @param text The {@link Editable} text to which to add Markdown link tag.
     */
    public static void addLink(@NonNull Editable text) {
        addLink(text, false);
    }

    /**
     * Inserts a Markdown divider at the cursor position.
     * <p>
     * If text is selected it'll be removed and divider will be added at its position instead.
     *
     * @param text The {@link Editable} text to which to add Markdown divider.
     */
    public static void addDivider(@NonNull Editable text) {
        int selectionStart = SelectionUtils.getSelectionStart(text);

        StringBuilder stringBuilder = new StringBuilder();
        requireEmptyLineAbove(text, stringBuilder, selectionStart);
        stringBuilder.append("-------");

        if (SelectionUtils.getSelectionEnd(text) == text.length()) {
            stringBuilder.append("\n");
        } else {
            requireEmptyLineBelow(text, stringBuilder, SelectionUtils.getSelectionEnd(text));
        }

        SelectionUtils.replaceSelectedText(text, stringBuilder);
        updateCursorPosition(text, true);
    }

    /**
     * Inserts a markdown list to the specified EditText at the currently selected position.
     *
     * @param text     The EditText to which to add markdown list.
     * @param listType The type of the list.
     */
    public static void addList(@NonNull Editable text, @ListType int listType) {
        int tagCount = 1;
        String tag;
        if (listType == LIST_TYPE_NUMBERS) {
            tag = "1. ";
        } else if (listType == LIST_TYPE_TASKS) {
            tag = "- [ ] ";
        } else {
            tag = "- ";
        }

        if (!SelectionUtils.hasSelection(text)) {
            moveSelectionStartToStartOfLine(text);
        }

        CharSequence selectedText = SelectionUtils.getSelectedText(text);
        int selectionStart = SelectionUtils.getSelectionStart(text);

        StringBuilder stringBuilder = new StringBuilder();

        String[] lines = selectedText.toString().split("\n");
        if (lines.length > 0) {
            for (String line : lines) {
                if (line.length() == 0 && stringBuilder.length() != 0) {
                    stringBuilder.append("\n");
                    continue;
                }

                if (stringBuilder.length() > 0) {
                    stringBuilder.append("\n");
                }
                if (!line.trim().startsWith(tag)) {
                    stringBuilder.append(tag).append(line);
                } else {
                    stringBuilder.append(line);
                }

                if (listType == LIST_TYPE_NUMBERS) {
                    tagCount += 1;
                    tag = tagCount + ". ";
                }
            }
        }

        if (stringBuilder.length() == 0) {
            stringBuilder.append(tag);
        }

        int selectionEnd = SelectionUtils.getSelectionEnd(text);
        requireEmptyLineAbove(text, stringBuilder, selectionStart);
        requireEmptyLineBelow(text, stringBuilder, selectionEnd);

        text.replace(selectionStart, selectionEnd, stringBuilder);
        Selection.setSelection(text, selectionStart + stringBuilder.length());
        updateCursorPosition(text, selectedText.length() > 0);
    }

    /**
     * Inserts a markdown header tag to the specified EditText at the currently selected position.
     *
     * @param text  The EditText to which to add markdown header tag.
     * @param level The level of the header tag.
     */
    public static void addHeader(@NonNull Editable text, int level) {
        if (!SelectionUtils.hasSelection(text)) {
            moveSelectionStartToStartOfLine(text);
            moveSelectionEndToEndOfLine(text);
        }

        CharSequence selectedText = SelectionUtils.getSelectedText(text);
        int selectionStart = SelectionUtils.getSelectionStart(text);

        StringBuilder result = new StringBuilder();
        requireEmptyLineAbove(text, result, selectionStart);

        for (int i = 0; i < level; i++) {
            result.append("#");
        }
        result.append(" ").append(selectedText);

        requireEmptyLineBelow(text, result, SelectionUtils.getSelectionEnd(text));

        SelectionUtils.replaceSelectedText(text, result);
        updateCursorPosition(text, selectedText.length() > 0);
    }

    /**
     * Inserts a markdown code block to the specified EditText at the currently selected position.
     *
     * @param text The EditText to which to add markdown code block.
     */
    public static void addCode(@NonNull Editable text) {
        if (!SelectionUtils.hasSelection(text)) {
            SelectionUtils.selectWordAroundCursor(text);
        }
        CharSequence selectedText = SelectionUtils.getSelectedText(text);
        int selectionStart = SelectionUtils.getSelectionStart(text);
        String string = selectedText.toString();
        boolean isCodeBlock = string.contains("\n");

        StringBuilder stringBuilder = new StringBuilder();
        if (isCodeBlock) {
            requireEmptyLineAbove(text, stringBuilder, selectionStart);
            stringBuilder.append("```\n").append(selectedText).append("\n```");
            requireEmptyLineBelow(text, stringBuilder, SelectionUtils.getSelectionEnd(text));
        } else {
            stringBuilder.append("`").append(string.trim()).append("`");
        }

        SelectionUtils.replaceSelectedText(text, stringBuilder);
        if (isCodeBlock) {
            updateCursorPosition(text, true);
        } else {
            Selection.setSelection(text, SelectionUtils.getSelectionEnd(text) - 1);
        }
    }

    /**
     * Inserts a markdown quote block to the specified EditText at the currently selected position.
     *
     * @param text The EditText to which to add quote block.
     */
    public static void addQuote(@NonNull Editable text) {
        if (!SelectionUtils.hasSelection(text)) {
            moveSelectionStartToStartOfLine(text);
            moveSelectionEndToEndOfLine(text);
        }
        CharSequence selectedText = SelectionUtils.getSelectedText(text);

        int selectionStart = SelectionUtils.getSelectionStart(text);

        StringBuilder stringBuilder = new StringBuilder();
        requireEmptyLineAbove(text, stringBuilder, selectionStart);

        stringBuilder.append("> ");
        if (selectedText.length() > 0) {
            stringBuilder.append(selectedText.toString().replace("\n", "\n> "));
        }

        requireEmptyLineBelow(text, stringBuilder, SelectionUtils.getSelectionEnd(text));

        SelectionUtils.replaceSelectedText(text, stringBuilder);
        updateCursorPosition(text, selectedText.length() > 0);
    }

    @VisibleForTesting
    static void surroundSelectionWith(@NonNull Editable text, @NonNull String surroundText) {
        if (!SelectionUtils.hasSelection(text)) {
            SelectionUtils.selectWordAroundCursor(text);
        }
        CharSequence selectedText = SelectionUtils.getSelectedText(text);
        int selectionStart = SelectionUtils.getSelectionStart(text);

        selectedText = selectedText.toString().trim();

        int charactersToGoBack = 0;
        if (selectedText.length() == 0) {
            charactersToGoBack = surroundText.length();
        }

        StringBuilder result = new StringBuilder();
        result.append(surroundText).append(selectedText).append(surroundText);

        SelectionUtils.replaceSelectedText(text, result);
        Selection.setSelection(text, selectionStart + result.length() - charactersToGoBack);
    }

    private static void addLink(@NonNull Editable text, boolean isImageLink) {
        if (!SelectionUtils.hasSelection(text)) {
            SelectionUtils.selectWordAroundCursor(text);
        }
        String selectedText = SelectionUtils.getSelectedText(text).toString().trim();

        int selectionStart = SelectionUtils.getSelectionStart(text);

        String imageMarker = isImageLink ? "!" : "";
        String result = imageMarker + "[" + selectedText + "](url)";
        SelectionUtils.replaceSelectedText(text, result);

        if (selectedText.length() == 0) {
            Selection.setSelection(text, selectionStart + (isImageLink ? 2 : 1));
        } else {
            selectionStart = selectionStart + result.length() - 4;
            Selection.setSelection(text, selectionStart, selectionStart + 3);
        }
    }

    private static void requireEmptyLineAbove(@NonNull Spannable text, StringBuilder stringBuilder,
            int position) {
        if (position <= 0) {
            return;
        }

        if (text.charAt(position - 1) != '\n') {
            stringBuilder.insert(0, "\n\n");
        } else if (position > 1 && text.charAt(position - 2) != '\n') {
            stringBuilder.insert(0, "\n");
        }
    }

    private static void requireEmptyLineBelow(@NonNull Spannable text, StringBuilder stringBuilder,
            int position) {
        if (position > text.length() - 1) {
            return;
        }

        if (text.charAt(position) != '\n') {
            stringBuilder.append("\n\n");
        } else if (position < text.length() - 2 && text.charAt(position + 1) != '\n') {
            stringBuilder.append("\n");
        }
    }

    private static void moveSelectionStartToStartOfLine(@NonNull Spannable text) {
        int position = SelectionUtils.getSelectionStart(text);
        String substring = text.subSequence(0, position).toString();

        int selectionStart = substring.lastIndexOf('\n') + 1;
        Selection.setSelection(text, selectionStart, SelectionUtils.getSelectionEnd(text));
    }

    private static void moveSelectionEndToEndOfLine(@NonNull Spannable text) {
        int position = SelectionUtils.getSelectionEnd(text);
        String substring = text.subSequence(0, position).toString();

        int selectionEnd = substring.indexOf('\n');
        if (selectionEnd == -1) {
            selectionEnd = text.length();
        } else {
            selectionEnd += position;
        }
        Selection.setSelection(text, SelectionUtils.getSelectionStart(text), selectionEnd);
    }

    private static void updateCursorPosition(@NonNull Spannable text, boolean goToNewLine) {
        int selectionEnd = SelectionUtils.getSelectionEnd(text);
        if (selectionEnd > text.length()) {
            return;
        }

        while (selectionEnd > 0 && text.charAt(selectionEnd - 1) == '\n') {
            selectionEnd -= 1;
        }
        if (goToNewLine && selectionEnd < text.length()) {
            selectionEnd += 1;
        }
        Selection.setSelection(text, selectionEnd);
    }

    @IntDef({LIST_TYPE_BULLETS, LIST_TYPE_NUMBERS, LIST_TYPE_TASKS})
    public @interface ListType {
    }
}
