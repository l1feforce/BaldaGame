package gusev.spbstu.org.baldagame.androidPlayer

data class Node<T>(val value: T?,
                   var isLeaf: Boolean,
                   val children: MutableMap<T, Node<T>> = mutableMapOf())