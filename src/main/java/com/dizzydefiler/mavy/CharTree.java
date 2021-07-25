package com.dizzydefiler.mavy;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CharTree<T> {
    public Node<T> root;

    public CharTree(T rootData) {
        root = new Node<T>(null);
        root.data = null;
        root.children = new HashMap<Character,Node<T>>();
    }

    @Override
    public String toString() {
        return "Tree{" +
                "root=" + root +
                '}';
    }

    public static class Node<T> {
        public T data = null;
        public Node<T> parent = null;
        public String cs = "";
        public HashMap<Character,Node<T>> children;

        public Node (T _data) {
            this.data = _data;
            this.children = new HashMap<Character, Node<T>>();
        }

        public Node(T _data, Node<T> parent, Character key) {
            this.data = _data;
            this.children = new HashMap<Character, Node<T>>();
            this.parent = parent;
            cs = parent.cs + key;
        }

        public void link(Character key, T data) {
            children.put(key,new Node<T>(data,this,key));
        }

        public void link(Character key, Node<T> node) {
            children.put(key,node);
            node.cs = cs + key;

        }

        @Override
        public String toString() {

            return "Node{" +
                    "data=" + data +
                    ", cs=" + cs +
                    ", children=[" + children.toString() + "]" +
                    '}';
        }
    }
}