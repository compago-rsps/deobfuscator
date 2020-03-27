package org.compago.deobfuscator

import org.compago.deobfuscator.asm.ClassGroup
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import org.tinylog.kotlin.Logger.info
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream

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
 * The root deobfuscator object.
 * @constructor [config] The command line configuration
 */
class Deobfuscator(private val config: Configuration) {

    /**
     * The currently loaded class group.
     */
    private val group = ClassGroup()

    /**
     * Starts the deobfuscator.
     */
    fun start() {
        info("Starting deobfuscator...")

        /**
         * Load the class group.
         */
        info("Loading jar file: '${config.inputJar.name}'.")
        this.loadJar(config.inputJar, group)
        info("Loaded ${group.size} classes from jar file.")

        /**
         * Run transformers
         */

        /**
         * Export the class group.
         */
        info("Exporting transformed classes to output jar: '${config.outputJar.name}'.")
        this.exportJar(config.outputJar, group)
        info("Deobfuscator complete! Exported jar path: '${config.outputJar.absolutePath}'.")
    }

    /**
     * Unpacks a Jar file and loads it into a class group.
     *
     * @param file The jar file to unpack
     * @param group The [ClassGroup] to add entries to
     */
    private fun loadJar(file: File, group: ClassGroup) {
        JarFile(file).use { jar ->
            jar.stream().iterator().asSequence()
                .filter { it.name.endsWith(".class") }
                .forEach {
                    val node = ClassNode()
                    val reader = ClassReader(jar.getInputStream(it))
                    reader.accept(node, ClassReader.SKIP_FRAMES or ClassReader.SKIP_DEBUG)
                    group.add(node)
                }

            jar.close()
        }

        group.build()
    }

    /**
     * Exports the classes in a [ClassGroup] into a jar file.
     *
     * @param file The output jar file
     * @param group The [ClassGroup] to export classes from.
     */
    private fun exportJar(file: File, group: ClassGroup) {
        JarOutputStream(FileOutputStream(file)).use { out ->
            group.forEach {
                out.putNextEntry(JarEntry(it.name + ".class"))
                val writer = ClassWriter(ClassWriter.COMPUTE_MAXS)
                it.node.accept(writer)

                out.write(writer.toByteArray())
                out.closeEntry()
            }
            out.close()
        }
    }
}