package org.cce.backend.engine;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class Item implements Serializable {
    private String id;
    private String content;
    private Item right;
    private Item left;
    private String operation;
    private boolean isdeleted;
    private boolean isbold;
    private boolean isitalic;

    public Item(String id, String content) {
        this.id = id;
        this.content = content;
        this.right = null;
        this.left = null;
        this.isdeleted = false;
    }

    public Item(String id, String content, Item right, Item left, String operation, boolean isDeleted, boolean isBold, boolean isItalic) {
        this.id = id;
        this.content = content;
        this.right = right;
        this.left = left;
        this.isdeleted = isDeleted;
        this.isbold = isBold;
        this.isitalic = isItalic;
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", right=" + (right != null ? right.getId() : "null") +
                ", left=" + (left != null ? left.getId() : "null") +
                ", operation='" + operation + '\'' +
                ", isdeleted=" + isdeleted +
                ", isbold=" + isbold +
                ", isitalic=" + isitalic +
                '}';
    }
}

