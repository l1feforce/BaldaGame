package gusev.spbstu.org.baldagame.androidPlayer

class Trie<T>(val root: Node<T> = Node<T>(null, false)) {

    fun add(values: List<T>) {
        var children = root.children

        values.forEachIndexed { i, value ->
            val isLeaf = i == values.size - 1
            // add new node
            if (!children.contains(value)) {
                val node = Node<T>(value, isLeaf)
                children[value] = node
                children = node.children

            } else {
                // exist, so traverse current path + set isLeaf if needed
                val node = children[value]!!
                if (isLeaf) { node.isLeaf = isLeaf }
                children = node.children
            }
        }
    }

    fun contains(values: List<T>): Boolean {
        return search(values) != null
    }

    private fun search(values: List<T>) : Node<T>? {
        var children = root.children
        if (children.isEmpty()) { return null }

        values.forEachIndexed { i, value ->
            val isLeaf = i == values.size - 1
            // add new node
            if (!children.contains(value)) {
                return null

            } else {
                // exist, so traverse current path, ending if is last value, and is leaf node
                val node = children[value]!!
                if (isLeaf) {
                    if (node.isLeaf) { return node }
                    else { return null }
                }
                // not at end, continue traversing
                children = node.children
            }
        }
        throw IllegalStateException("Should not get here")
    }
}