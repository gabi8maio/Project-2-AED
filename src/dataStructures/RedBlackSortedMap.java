package dataStructures;

import dataStructures.exceptions.EmptyMapException;

public class RedBlackSortedMap <K extends Comparable<K>, V> extends AdvancedBSTree <K,V> {

    public RedBlackSortedMap() {
        super();
    }

    private BTNode<Entry<K, V>> searchNode(K key) {
        BTNode<Entry<K, V>> current = (BTNode<Entry<K, V>>) root;
        while (current != null) {
            int cmp = key.compareTo(current.getElement().key());
            if (cmp == 0) {
                return current;
            } else if (cmp < 0)
                current = (BTNode<Entry<K, V>>) current.getLeftChild();
            else
                current = (BTNode<Entry<K, V>>) current.getRightChild();
        }
        return null;
    }


    @Override
    public V get(K key) {
        BTNode<Entry<K, V>> node = searchNode(key);
        return (node == null) ? null : node.getElement().value();
    }

    @Override
    public V put (K key, V value) {
        if (root == null){
            root = new RBNode<>(new Map.Entry<>(key, value));
            ((RBNode<Entry<K, V>>) root).setBlack();
            currentSize++;
            return null;
        }
        BTNode<Entry<K, V>> parent = null;
        BTNode<Entry<K, V>> current = (BTNode<Entry<K, V>>) root;
        int cmp = 0;
        while (current != null) {
            cmp = key.compareTo(current.getElement().key());
            if (cmp == 0) {
                V old = current.getElement().value();
                current.setElement(new Map.Entry<>(key, value));
                return old;
            }
            parent = current;
            if (cmp<0)
                current = (BTNode<Entry<K, V>>) current.getLeftChild();
            else
                current = (BTNode<Entry<K, V>>) current.getRightChild();

        }
        RBNode<Entry<K, V>> z = new RBNode<>(new Map.Entry<>(key, value));
        z.setParent(parent);
        if (cmp < 0)
            parent.setLeftChild(z);
        else
            parent.setRightChild(z);
        currentSize++;
        insertFixup(z);
        return null;
    }

    @SuppressWarnings("unchecked")
    private void insertFixup(RBNode<Entry<K, V>> z) {
        // Enquanto existir pai e o pai for vermelho -> violação de RB
        while (true) {
            Node<Entry<K, V>> pNode = z.getParent();
            if (pNode == null) break; // z chegou à raiz
            RBNode<Entry<K, V>> parent = (RBNode<Entry<K, V>>) pNode;
            if (parent.isBlack()) break; // sem violação se o pai for preto

            // parent é vermelho -> existe grandparent (porque root é sempre preto)
            RBNode<Entry<K, V>> grand = (RBNode<Entry<K, V>>) parent.getParent();
            if (grand == null) {
                // Improvável: pai vermelho sem avô (apenas se a árvore tiver raiz vermelha, mas root deve ser preta)
                break;
            }

            // Determinar quem é o tio (uncle) de z
            RBNode<Entry<K, V>> uncle;
            boolean parentIsLeft = (grand.getLeftChild() == parent);

            if (parentIsLeft) {
                uncle = (RBNode<Entry<K, V>>) grand.getRightChild();
                // Caso 1: tio é vermelho -> recolorir pai e tio para preto, avô para vermelho
                if (uncle != null && uncle.isRed()) {
                    parent.setBlack();
                    uncle.setBlack();
                    grand.setRed();
                    z = grand; // subir e continuar a verificar violações acima
                    continue;
                }
                // Caso 2/3: tio é preto ou nulo
                // Se z é filho direito, fazemos rotateLeft(parent) para transformar no caso "externo"
                if (z == parent.getRightChild()) {
                    z = parent;
                    // rotateLeft espera um BTNode (AdvancedBSTree fornece a implementação)
                    rotateLeft((BTNode<Entry<K, V>>) z);
                    parent = (RBNode<Entry<K, V>>) z.getParent();
                    grand = (RBNode<Entry<K, V>>) parent.getParent();
                }
                // Agora z é filho esquerdo do seu pai (caso externo). Fazemos rotateRight(grand)
                parent.setBlack();
                grand.setRed();
                rotateRight((BTNode<Entry<K, V>>) grand);
            } else {
                // parent é filho direito do grand
                uncle = (RBNode<Entry<K, V>>) grand.getLeftChild();
                if (uncle != null && uncle.isRed()) {
                    // Caso 1 simétrico
                    parent.setBlack();
                    uncle.setBlack();
                    grand.setRed();
                    z = grand;
                    continue;
                }
                // Caso 2/3 simétrico: se z é filho esquerdo, rotateRight(parent) para transformar
                if (z == parent.getLeftChild()) {
                    z = parent;
                    rotateRight((BTNode<Entry<K, V>>) z);
                    parent = (RBNode<Entry<K, V>>) z.getParent();
                    grand = (RBNode<Entry<K, V>>) parent.getParent();
                }
                // rotateLeft(grand)
                parent.setBlack();
                grand.setRed();
                rotateLeft((BTNode<Entry<K, V>>) grand);
            }
        }
        // Garantir a raiz preta
        if (root != null)
            ((RBNode<Entry<K, V>>) root).setBlack();
    }

    @Override
    public V remove (K key) {
        return null;
    }

    @Override
    public Entry<K, V> minEntry() {
        if (isEmpty())
            throw new EmptyMapException();
        return furtherLeftElement().getElement();
    }

    @Override
    public Entry<K, V> maxEntry() {
        if (isEmpty())
            throw new EmptyMapException();
        return furtherRightElement().getElement();
    }


}

