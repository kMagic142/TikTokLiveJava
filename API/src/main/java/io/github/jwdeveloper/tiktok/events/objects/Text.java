/*
 * Copyright (c) 2023-2023 jwdeveloper jacekwoln@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.github.jwdeveloper.tiktok.events.objects;

import io.github.jwdeveloper.tiktok.exceptions.TikTokLiveException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
public class Text {
    String key;
    String pattern;
    List<TextPiece> textPieces;

    public Text(String key, String pattern, List<TextPiece> textPieces) {
        this.key = key;
        this.pattern = pattern;
        this.textPieces = textPieces;
    }

    public static Text map(io.github.jwdeveloper.tiktok.messages.data.Text input) {
        var pieces = input.getPiecesListList().stream().map(Text::mapTextPiece).toList();
        return new Text(input.getKey(), input.getDefaultPattern(), pieces);
    }


    public static TextPiece mapTextPiece(io.github.jwdeveloper.tiktok.messages.data.Text.TextPiece input)
    {
        return switch (input.getType())
                {
            case 0 -> {
                var user = User.mapOrEmpty(input.getUserValue().getUser());
                yield new UserTextPiece(input.getStringValue(), user);
            }
            case 1 -> new GiftTextPiece(input.getStringValue());
            default -> throw new TikTokLiveException("Unknown text piece");
        };
    }

    @Getter
    @AllArgsConstructor
    public static class TextPiece {
        String value;
    }

    public static class UserTextPiece extends TextPiece {
        User user;

        public UserTextPiece(String value, User user) {
            super(value);
            this.user = user;
        }
    }

    public static class GiftTextPiece extends TextPiece {
        Gift gift;

        public GiftTextPiece(String value) {
            super(value);
        }
    }
}
