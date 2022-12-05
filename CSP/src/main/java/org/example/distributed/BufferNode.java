package org.example.distributed;

import java.util.ArrayList;
import java.util.List;

public class BufferNode {
    List<BufferNode> children;
    BufferNode parent;
    Long id;

    BufferNode(Long id) {
        this.id = id;
        this.children = new ArrayList<>();
    }

    public void setParent(BufferNode parent) {
        this.parent = parent;
    }

    public void addChild(BufferNode childNode) {
        children.add(childNode);
    }

}
