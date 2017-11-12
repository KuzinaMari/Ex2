/**
 * Created by 1 on 12.11.2017.
 */
public class BinaryTree {



        public static class TreeNode {
            private int myValue;
            private TreeNode parent;
            private TreeNode left;
            private TreeNode right;

            public TreeNode(int value) {
                myValue = value;
            }

            public TreeNode(int myValue, TreeNode left, TreeNode right) {
                this.myValue = myValue;
                this.left = left;
                this.right = right;
                left.parent = this;
                right.parent = this;

            }

            public TreeNode getFarLeft() {
                if (left == null) {
                    return this;
                }
                return left.getFarLeft();
            }

            public boolean isLeftChild(){
                if( parent == null ) throw new RuntimeException( "root" );
                return parent.left == this;
            }

            public boolean isRightChild(){
                if( parent == null ) throw new RuntimeException( "root" );
                return parent.right == this;
            }

            public TreeNode getClosestLeftParent(){
                if( parent == null ){
                    return this;
                }
                if( isRightChild() ){
                    return parent.getClosestLeftParent();
                }
                return parent;
            }

            public void add( int value ){
                if( myValue == value ){
                    throw new RuntimeException( "value already exist" );
                }else if( value < myValue ){
                    if( left == null ){
                        TreeNode node = new TreeNode( value );
                        node.parent = this;
                        left = node;
                    }else{
                        left.add( value );
                    }
                }else{
                    if( right == null ){
                        TreeNode node = new TreeNode( value );
                        node.parent = this;
                        right = node;
                    }else{
                        right.add( value );
                    }
                }
            }

            public TreeNode getLeft(){
                return left;
            }

            public TreeNode getRight(){
                return right;
            }

            public int getValue(){
                return myValue;
            }

            public TreeNode min(){
                if( left != null ){
                    return left.min();
                }
                return this;
            }

            public TreeNode max(){
                if( right != null ){
                    return right.max();
                }
                return this;
            }

            public void delete(){
                if( isLeftChild() ) {
                    parent.left = null;
                }else{
                    parent.right = null;
                }
                parent = null;
            }

            public void replace( TreeNode node ){
                if( isLeftChild() ){
                    parent.left = node;
                }else{
                    parent.right = node;
                }
                node.parent = parent;
                parent = null;
            }
        }

        public static BinaryTree createTree() {
            TreeNode leaf1 = new TreeNode(1);
            TreeNode leaf2 = new TreeNode(5);
            TreeNode leaf3 = new TreeNode(11);
            TreeNode parent12 = new TreeNode(4, leaf1, leaf2);
            TreeNode root = new TreeNode(8, parent12, leaf3);
            return new BinaryTree(root);
        }
        /*
        обход
        левое -> само -> правое
        если левого нету, то сразу само
        если левое есть
          обход для левого
        само
        если правого нету, то конец
        если правое, то
          обход для правого
         */
        public static class Iterator {
            public static class NodeChain{
                private NodeChain myPrevChain;
                private TreeNode myNode;

                private boolean myLeftProcessed;

                public NodeChain( NodeChain prevChain, TreeNode node ){
                    myPrevChain = prevChain;
                    myNode = node;
                    myLeftProcessed = node.left == null;
                }

                public NodeChain getPrevChain() {
                    return myPrevChain;
                }

                public TreeNode getNode() {
                    return myNode;
                }

                public boolean isLeftProcessed() {
                    return myLeftProcessed;
                }

                public void setLeftProcessed(){
                    myLeftProcessed = true;
                }

            }

            private TreeNode current; //то что вернули в последний вызов next()
            private BinaryTree myTree;

            private NodeChain myChain;

            private boolean hasNext;

            private void printChain(){
                String res = "";
                NodeChain chain = myChain;
                while( chain != null ){
                    res = Integer.toString( chain.myNode.myValue ) + ( res.equals("") ? "" : " - " ) + res;
                    chain = chain.getPrevChain();
                }
                System.out.println( "chain: " + res );
            }

            public void doIteration2(){
                boolean allDone = false;
                NodeChain chain;
                if( myChain == null ){
                    myChain = new NodeChain( null, myTree.root );
                }
                while( !allDone ) {
                    if (!myChain.isLeftProcessed()) {
                        chain = new NodeChain(myChain, myChain.myNode.getLeft());
                        myChain.setLeftProcessed();
                        myChain = chain;
                    } else {
                        System.out.println(myChain.myNode.getValue());
                        if (myChain.myNode.getRight() != null) {
                            chain = new NodeChain(myChain.getPrevChain(), myChain.myNode.getRight());
                            myChain = chain;
                        } else {
                            myChain = myChain.getPrevChain();
                            if (myChain == null) {
                                allDone = true;
                            }
                        }
                    }
                }
            }

            public boolean hasNext(){
                return hasNext;
            }

            public int next(){
                if( !hasNext ){
                    throw new RuntimeException("end of iteration");
                }
                boolean allDone = false;
                NodeChain chain;
                if( myChain == null ){
                    myChain = new NodeChain( null, myTree.root );
                }
                while( !allDone ) {
                    if (!myChain.isLeftProcessed()) {
                        chain = new NodeChain(myChain, myChain.myNode.getLeft());
                        myChain.setLeftProcessed();
                        myChain = chain;
                    } else {
                        int res = myChain.myNode.getValue();
                        current = myChain.myNode;
                        if (myChain.myNode.getRight() != null) {
                            chain = new NodeChain(myChain.getPrevChain(), myChain.myNode.getRight());
                            myChain = chain;
                        } else {
                            myChain = myChain.getPrevChain();
                            if (myChain == null) {
                                allDone = true;
                                hasNext = false;
                            }
                        }
                        return res;
                    }
                }
                return 0;
            }

            private boolean allDone = false;
            public void doIteration(){
                if( allDone ){
                    return;
                }
                NodeChain chain;
                if( myChain == null ){
                    myChain = new NodeChain( null, myTree.root );
                    allDone = false;
                }
                if( !myChain.isLeftProcessed() ){
                    chain = new NodeChain( myChain, myChain.myNode.getLeft() );
                    myChain.setLeftProcessed();
                    myChain = chain;
                }else{
                    System.out.println( myChain.myNode.getValue() );
                    if( myChain.myNode.getRight() != null ){
                        chain = new NodeChain( myChain.getPrevChain(), myChain.myNode.getRight() );
                        myChain = chain;
                    }else{
                        myChain = myChain.getPrevChain();
                        if( myChain == null ){
                            allDone = true;
                        }
                    }
                }
                doIteration();
            }

            public Iterator(BinaryTree tree) {
                myTree = tree;
                current = null; //tree.root.getFarLeft();
                myChain = null;
                hasNext = myTree.root != null;
            }

            public void remove(){
                if( ( current.left == null ) && ( current.right == null ) ){
                    current.delete();
                }else if( current.right == null ){
                    current.replace( current.left );
                }else if( current.left == null ){
                    current.replace( current.right );
                }else{
                    TreeNode newNode = current.parent.min();
                    if( newNode.getRight() != null ){
                        newNode.replace( newNode.getRight() );
                    }
                    newNode.left = current.left;
                    newNode.right = current.right;
                    current.replace( current.right );
                }
            }

            public void remove2() {

                if( current == null ) throw new RuntimeException("can't remove null");
                //if( current.left == null && current.right == null ){
                if( current.parent.left == current ){
                    current.parent.left = null;
                }else{
                    current.parent.right = null;
                }
                //current.parent = null;
//            }else if( current.left != null ){
//
//            }
            }

        }

        TreeNode root;


        public BinaryTree() {
            root = null;
        }

        public BinaryTree(TreeNode root) {
            this.root = root;
        }


        //    int inform;
//    TreeNode left;
//    TreeNode right;
//
//// создание конечного узла
//    public BinaryTree(int inform) {
//        this.inform = inform;
//        left = null;
//        right = null;
//    }
    }


