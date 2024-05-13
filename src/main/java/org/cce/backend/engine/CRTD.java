package org.cce.backend.engine;

import java.util.HashMap;

public class CRTD {
    private HashMap<String, Item> crdtMap;
    private Item firstItem;

    public CRTD() {
        crdtMap = new HashMap<>();
    }

    public void insert(String key, Item item) {
        if (item.left == null) {
            if (firstItem != item.right && firstItem.id.split("@")[1].compareTo(item.id.split("@")[1]) > 0) {
                item.left = firstItem;
                System.out.println(item);
            } else {
                item.right = firstItem;
                if (firstItem != null) firstItem.left = item;
                firstItem = item;
                crdtMap.put(item.id, item);
                System.out.println("here");
                return;
            }
        }
        while (item.left.right != item.right && item.left.right.id.split("@")[1].compareTo(item.id.split("@")[1]) > 0) {
            item.left = item.left.right;
        }

        item.right = item.left.right;
        crdtMap.put(item.id, item);
        item.left.right = item;
        if (item.right != null) item.right.left = item;

    }

    public void delete(String key) {
        Item item = crdtMap.get(key);
        item.isDeleted = true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Item current = firstItem;
        while (current != null) {
            if (!current.isDeleted)  sb.append(current.content);
            current = current.right;
        }
        return sb.toString();
    }
}


