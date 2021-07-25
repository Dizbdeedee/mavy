package com.dizzydefiler.mavy;

import com.dizzydefiler.mavy.actions.IMavyContainer;
import com.dizzydefiler.mavy.util.Logger;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.dizzydefiler.mavy.actions.Dummy;

import java.util.*;

public class MavyTree {

    ArrayList<Character> masterKeys;

    CharTree<IMavyContainer> finalTree;

    CharTree.Node<IMavyContainer> traverseNode;

    public MavyTree(ArrayList<Character> keys) {
        masterKeys = keys;
    }

    public void make(ArrayList<IMavyContainer> lst) {
        if (finalTree != null) {
            throw new RuntimeException("Tree already made");
        }
        finalTree = new CharTree<IMavyContainer>(null);
        recurse(lst,finalTree.root);
    }


    CharTree.Node<IMavyContainer> recurse(ArrayList<IMavyContainer> _lst, CharTree.Node<IMavyContainer> currentNode) {
        ArrayList<Character> ks = (ArrayList<Character>) masterKeys.clone();
        ArrayList<IMavyContainer> lst = (ArrayList<IMavyContainer>) _lst.clone();
        if (lst.size() < masterKeys.size()) {
            while (ks.size() > 0 && lst.size() > 0) {
                currentNode.link(ks.remove(0),lst.remove(0));
            }
            return currentNode;
        }
        for (Integer s : divide(lst.size(),masterKeys.size())) {
            if (s == 1) {
                currentNode.link(ks.remove(0),lst.remove(0));
            } else {
                Character k = ks.remove(0);
                currentNode.link(k,recurse(popAll(lst,s),new CharTree.Node<IMavyContainer>(null,currentNode,k)));
            }
        }
        return currentNode;
    }

    static ArrayList<IMavyContainer> popAll(ArrayList<IMavyContainer> lst, Integer n) {
//        ArrayList<Integer> lst = (ArrayList<Integer>) _lst.clone();
        ArrayList<IMavyContainer> popped = new ArrayList<IMavyContainer>();
        if (lst.size() < n) {
            return popped;
        }
        for (int i = 0; i < n; i++) {
            popped.add(lst.remove(0));
        }
        return popped;
    }

    static ArrayList<Integer> divide(int n, int b) {

        int p = (int) Math.floor(1.0E-6 + (Math.log(n) / Math.log(b))) - 1;
        int x1 = (int) Math.pow(b, p);
        int x2 = b * x1;
        int delta = n - x2;
        int n2 = delta / (x2 - x1);
        int n1 = b - n2 - 1;
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < n1; i++) {
            list.add(x1);
        }
        list.add(n - (x1 * n1) - (n2 * x2));
        for (int i = 0; i < n2; i++) {
            list.add(x2);
        }
        return list;
    }

    /**
     * Set traverseNode based on current input
     * @param chars Input
     * @return Input valid, traverse node set
     */
    public boolean findCharacters(ArrayList<Character> chars) {
        CharTree.Node<IMavyContainer> curNode = finalTree.root;
        for (Character c : chars) {
            curNode = curNode.children.get(c);
            if (curNode == null) {
                return false;
            }
        }
        traverseNode = curNode;
        return true;
    }

    /**
     * Traverse the tree based on the current traverseNode. If no traverse node, start at the root
     * @return Selected mavycontainers, with characters filled
     */
    public ArrayList<IMavyContainer> traverse() {
        ArrayList<IMavyContainer> mc = new ArrayList<IMavyContainer>();
        CharTree.Node<IMavyContainer> curNode = traverseNode;
        if (traverseNode == null) {
            curNode = finalTree.root;
        }
        if (curNode.children.size() == 0 && curNode.data != null) {
            mc.add(curNode.data);
            return mc;
        }
        ArrayList<CharTree.Node<IMavyContainer>> queue = new ArrayList<CharTree.Node<IMavyContainer>>(curNode.children.values());

        while (queue.size() > 0) {
            CharTree.Node<IMavyContainer> node = queue.remove(0);
            if (node.data != null) {
                mc.add(node.data);
                node.data.setCharacters(node.cs.substring(curNode.cs.length()));
//                System.out.println("currentNode cs " + curNode.cs);
            } else {
                queue.addAll(node.children.values());
            }
        }
        return mc;
    }

    public void remove(IMavyContainer remove) {
        ArrayList<CharTree.Node<IMavyContainer>> queue = new ArrayList<CharTree.Node<IMavyContainer>>(finalTree.root.children.values());

        while (queue.size() > 0) {
            CharTree.Node<IMavyContainer> node = queue.remove(0);
            if (node.data != null && node.data == remove) {
                Logger.info("remove()","Removed " + node.data);
                node.parent.children.values().remove(node);
                return;
            } else {
                queue.addAll(node.children.values());
            }
        }
    }


    /**
     * Generates all possible key combos, with given keys up to 5 characters.
     * @param keys Individual characters
     * @return Set of possible key combinations
     */
    public static Set<String> generateAllSamples(ArrayList<Character> keys) {

        ArrayList<StringBuilder> sb = new ArrayList<StringBuilder>();
        for (Character c : keys) {
            StringBuilder b = new StringBuilder();
            b.append(c);
            sb.add(b);
        }

        Set<StringBuilder> initial = ImmutableSet.copyOf(sb);
        Set<StringBuilder> total = new HashSet<StringBuilder>(initial);
        Set<StringBuilder> current = new HashSet<StringBuilder>(initial);
        for (int i = 0; i < 4; i++) {
            Set<List<StringBuilder>> cart = Sets.cartesianProduct(current,initial);
            Set<StringBuilder> condense = new HashSet<StringBuilder>();
            for (List<StringBuilder> list : cart) {
//                System.out.println(list);
                StringBuilder master = new StringBuilder();
                for (StringBuilder s : list) {
                    master.append(s);
                }
                condense.add(master);
            }
            total.addAll(condense);
            current = condense;
        }
        HashSet<String> transform = new HashSet<String>();
        for (StringBuilder uh : total) {
            transform.add(uh.toString());
        }
        return transform;
    }

    public static void main(String[] args) {
        generateAllSamples(Mavy.keys);
        MavyTree mv = new MavyTree(Mavy.keys);
        ArrayList<IMavyContainer> md = new ArrayList<IMavyContainer>();
        for (int i = 0; i < 1000; i++) {
            md.add(new Dummy());
        }
        mv.make(md);
        System.out.println(mv.finalTree);
        mv.findCharacters(new ArrayList<Character>(Arrays.asList('a')));
        System.out.println(md);
        System.out.println(mv.traverse());

    }
}
