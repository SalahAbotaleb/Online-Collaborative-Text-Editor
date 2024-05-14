package org.cce.backend.engine;

import com.mongodb.lang.Nullable;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Item {
    public String id;
    public String content;
    public Item right;
    @Nullable
    public Item left;
    public boolean isDeleted;
    public boolean isBold;
    public boolean isItalic;

    public Item(String id, String content) {
        this.id = id;
        this.content = content;
        this.right = null;
        this.left = null;
        this.isDeleted = false;
    }

    public Item(String id, String content, Item right, Item left, boolean isDeleted, boolean isBold, boolean isItalic) {
        this.id = id;
        this.content = content;
        this.right = right;
        this.left = left;
        this.isDeleted = isDeleted;
        this.isBold = isBold;
        this.isItalic = isItalic;
    }
}

