package org.cce.backend.engine;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Item {
    public String id;
    public String content;
    public Item right;

    public Item left;
    public boolean isDeleted;

    public Item(String id, String content) {
        this.id = id;
        this.content = content;
        this.right = null;
        this.left = null;
        this.isDeleted = false;
    }

    public Item(String id, String content, Item right, Item left, boolean isDeleted) {
        this.id = id;
        this.content = content;
        this.right = right;
        this.left = left;
        this.isDeleted = isDeleted;
    }
}

