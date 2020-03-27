package org.compago.deobfuscator.asm

import org.objectweb.asm.tree.ClassNode
import org.tinylog.kotlin.Logger.info

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
 * Represents a group of ASM loaded classes.
 */
class ClassGroup {

    /**
     * The private store of classes.
     */
    private val classes = hashSetOf<Class>()

    val size: Int get() = classes.size

    fun toList(): List<Class> = classes.toList()

    fun add(node: ClassNode): Boolean {
        return classes.add(Class(this, node))
    }

    fun forEach(action: (Class) -> Unit) = classes.forEach(action)

    fun find(predicate: (Class) -> Boolean) = classes.firstOrNull(predicate)

    fun any(predicate: (Class) -> Boolean) = classes.any(predicate)

    /**
     * Builds the group structure.
     * Responsible for calculating the class hierarchy
     */
    internal fun build() {
        info("Building class group hierarchy.")
        forEach { c ->
            c.build()
        }
    }
}