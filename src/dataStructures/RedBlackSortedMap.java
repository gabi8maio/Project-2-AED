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
        BTNode<Entry<K, V>> z = searchNode(key);
        if (z == null) return null;

        V removedValue = z.getElement().value();

        RBNode<Entry<K, V>> y = (RBNode<Entry<K, V>>) z;
        boolean yOriginalRed = y.isRed();
        BTNode<Entry<K, V>> x;
        BTNode<Entry<K, V>> xParent;

        if (z.getLeftChild() == null) {
            x = (BTNode<Entry<K, V>>) z.getRightChild();
            xParent = (BTNode<Entry<K, V>>) z.getParent();
            transplant((BTNode<Entry<K, V>>) z, (BTNode<Entry<K, V>>) z.getRightChild());
        }else if (z.getRightChild() == null) {
            x = (BTNode<Entry<K, V>>) z.getLeftChild();
            xParent = (BTNode<Entry<K, V>>) z.getParent();
            transplant((BTNode<Entry<K, V>>) z, (BTNode <Entry<K, V>>) z.getLeftChild());
        } else {
            BTNode<Entry<K, V>> zRight = (BTNode<Entry<K, V>>) z.getRightChild();
            BTNode <Entry<K, V>> successor = zRight.furtherLeftElement();
            y = (RBNode<Entry<K, V>>) successor;
            yOriginalRed = !y.isRed() ? false : false;

            boolean yWasRed = y.isRed();

            x = (BTNode<Entry<K, V>>) y.getRightChild();

            if (y.getParent() == z){
                xParent = y;
                if (x!=null) x.setParent(y);
            } else {
                xParent = (BTNode<Entry<K, V>>) y.getParent();
                transplant ((BTNode<Entry<K, V>>) y, (BTNode<Entry<K, V>>) y.getRightChild());
                y.setRightChild(z.getRightChild());
                if (y.getRightChild() != null) ((BTNode<Entry<K,V>>) y.getRightChild()).setParent(y);
            }
            transplant((BTNode<Entry<K, V>>) z, (BTNode<Entry<K, V>>) y);
            y.setLeftChild(z.getLeftChild());
            if (y.getLeftChild() != null) ((BTNode<Entry<K, V>> )y.getLeftChild()).setParent(y);

            if (z instanceof RBNode){
                if (((RBNode<Entry<K, V>>)z).isRed())
                    y.setRed();
                else
                    y.setBlack();
            }else {
                y.setBlack();
            }
            yOriginalRed = yWasRed;

            if (x == null){
                if (y.getParent() != null)
                    xParent = (BTNode<Entry<K, V>>) y.getParent();
            } else {
                xParent = (BTNode<Entry<K, V>>) x.getParent();
            }

            if (!yWasRed){
                deleteFixup (x, xParent);
            }
            currentSize--;
            return removedValue;
        }

        boolean removedNodeWasRed = true;
        if (z instanceof RBNode){
            removedNodeWasRed = ((RBNode<Entry<K, V>>) z).isRed();
        }
        if (!removedNodeWasRed){
            deleteFixup(x, xParent);
        }
        currentSize--;
        return removedValue;
    }

    @SuppressWarnings("unchecked")
    private void deleteFixup(BTNode<Entry<K, V>> x, BTNode<Entry<K, V>> xParent) {
        // Enquanto x não é a raiz e x é preto (null é considerado preto)
        while ((x != root) && (x == null || !((RBNode<Entry<K, V>>) x).isRed())) {
            // Determinar se x é filho esquerdo ou direito do seu pai
            if (xParent == null) break; // segurança
            if (x == xParent.getLeftChild()) {
                // w é sibling (irmão) de x
                RBNode<Entry<K, V>> w = (RBNode<Entry<K, V>>) xParent.getRightChild();
                // Caso 1: w é vermelho -> recolorir e rotacionar
                if (w != null && w.isRed()) {
                    w.setBlack();
                    ((RBNode<Entry<K, V>>) xParent).setRed();
                    rotateLeft(xParent);
                    // atualizar w após rotação
                    w = (RBNode<Entry<K, V>>) xParent.getRightChild();
                }
                // Agora w é preto
                boolean wLeftBlack = !nodeIsRed(w != null ? w.getLeftChild() : null);
                boolean wRightBlack = !nodeIsRed(w != null ? w.getRightChild() : null);

                if (w == null) {
                    // sibling nulo -> tratar como todos os seus filhos pretos: propaga "black" para cima
                    x = xParent;
                    xParent = (BTNode<Entry<K, V>>) x.getParent();
                } else if (wLeftBlack && wRightBlack) {
                    // Caso 2: w e seus filhos são pretos -> pintar w de vermelho e subir
                    w.setRed();
                    x = xParent;
                    xParent = (BTNode<Entry<K, V>>) x.getParent();
                } else {
                    // Caso 3 e 4: pelo menos um filho de w é vermelho
                    if (!nodeIsRed(w.getRightChild())) {
                        // Caso 3: w.right é preto e w.left é vermelho -> rotacionar em w
                        if (w.getLeftChild() != null) setBlack(w.getLeftChild());
                        w.setRed();
                        rotateRight(w);
                        w = (RBNode<Entry<K, V>>) xParent.getRightChild();
                    }
                    // Caso 4: w.right é vermelho -> recolorir e rotacionar em xParent
                    // copiar cor de xParent para w
                    if (w != null) {
                        if (xParent instanceof RBNode) {
                            if (((RBNode<Entry<K, V>>) xParent).isRed()) setRed(w); else setBlack(w);
                        }
                        ((RBNode<Entry<K, V>>) xParent).setBlack();
                        if (w.getRightChild() != null) setBlack(w.getRightChild());
                    }
                    rotateLeft(xParent);
                    x = (BTNode<Entry<K, V>>) root;
                    xParent = null;
                }
            } else {
                // Simétrico: x é filho direito
                RBNode<Entry<K, V>> w = (RBNode<Entry<K, V>>) xParent.getLeftChild();
                if (w != null && w.isRed()) {
                    w.setBlack();
                    ((RBNode<Entry<K, V>>) xParent).setRed();
                    rotateRight(xParent);
                    w = (RBNode<Entry<K, V>>) xParent.getLeftChild();
                }

                boolean wLeftBlack = !nodeIsRed(w != null ? w.getLeftChild() : null);
                boolean wRightBlack = !nodeIsRed(w != null ? w.getRightChild() : null);

                if (w == null) {
                    x = xParent;
                    xParent = (BTNode<Entry<K, V>>) x.getParent();
                } else if (wLeftBlack && wRightBlack) {
                    w.setRed();
                    x = xParent;
                    xParent = (BTNode<Entry<K, V>>) x.getParent();
                } else {
                    if (!nodeIsRed(w.getLeftChild())) {
                        if (w.getRightChild() != null) setBlack(w.getRightChild());
                        w.setRed();
                        rotateLeft(w);
                        w = (RBNode<Entry<K, V>>) xParent.getLeftChild();
                    }
                    if (w != null) {
                        if (xParent instanceof RBNode) {
                            if (((RBNode<Entry<K, V>>) xParent).isRed()) setRed(w); else setBlack(w);
                        }
                        ((RBNode<Entry<K, V>>) xParent).setBlack();
                        if (w.getLeftChild() != null) setBlack(w.getLeftChild());
                    }
                    rotateRight(xParent);
                    x = (BTNode<Entry<K, V>>) root;
                    xParent = null;
                }
            }
        }
        // Garantir que x (se não nulo) fica preto ao fim
        if (x != null) setBlack(x);
    }

    private void transplant(BTNode<Entry<K, V>> u, BTNode<Entry<K, V>> v) {
        BTNode<Entry<K, V>> parent = (BTNode<Entry<K, V>>) u.getParent();
        if (parent == null) {
            root = v;
        } else if (parent.getLeftChild() == u) {
            parent.setLeftChild(v);
        } else {
            parent.setRightChild(v);
        }
        if (v != null) {
            v.setParent(parent);
        }
    }

    private boolean nodeIsRed(Node<Entry<K, V>> n){
        if ( n == null) return false;
        return ((RBNode<Entry<K, V>>) n).isRed();
    }

    private void setBlack (Node<Entry<K, V>> n){
        if (n == null) return;
        ((RBNode<Entry<K, V>>) n).setBlack();
    }

    private void setRed (Node<Entry<K, V>> n){
        if (n == null) return;
        ((RBNode<Entry<K, V>>) n).setRed();
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

