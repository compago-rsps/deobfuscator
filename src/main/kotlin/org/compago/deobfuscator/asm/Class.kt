package org.compago.deobfuscator.asm

import org.objectweb.asm.tree.ClassNode

/**
 * Copyright (c) 2020 Travis Welles
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

/**
 * Represents an ASM class.
 */
class Class(val group: ClassGroup, internal val node: ClassNode) {

    /**
     * The name of the class
     */
    var name: String
        get() = node.name
        set(value) {  }

    /**
     * The name of the extended class
     */
    var superName: String
        get() = node.superName
        set(value) {  }

    val interfaces get() = node.interfaces

    /**
     * The access opcode of the class
     */
    val access get() = node.access

    /**
     * The parent class. (Null if not apart of the gamepack)
     */
    var parent: Class? = null

    /**
     * The interfaces [Class]. Only contains gamepack classes.
     */
    val interfaceClasses = mutableListOf<Class>()

    /**
     * The [Class]s which inherit this.
     */
    val subClasses = mutableListOf<Class>()

    /**
     * Builds the class hierarchy.
     */
    internal fun build() {
        /**
         * Calculate the parent group.
         */
        if(this.superName.length <= 2) {
            this.parent = group.find { it.name == this.superName }
        }

        if(node.interfaces.size > 0 && node.interfaces.count { it.length <= 2 } > 0) {
            node.interfaces
                .filter { it.length <= 2 }
                .mapNotNull { i -> group.find { it.name == i }}
                .let { interfaceClasses.addAll(it) }
        }

        group.forEach { c ->
            if(c.superName == name || c.interfaces.contains(name)) {
                subClasses.add(c)
            }
        }
    }

    override fun toString(): String = name
}