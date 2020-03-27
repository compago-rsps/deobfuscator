package org.compago.deobfuscator

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.file

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

object Main {

    /**
     * Command line interface sub-class.
     */
    private class Cli : CliktCommand(
        name = "Deobfuscator",
        help = "Deobfuscates an OSRS client.",
        printHelpOnEmptyArgs = true
    ) {

        private val inputJar by option("--input", "-i", help = "Input Jar file").file(mustExist = true, canBeDir = false).required()
        private val outputJar by option("--output", "-o", help = "Output Jar file").file(mustExist = false, canBeDir = false).required()

        override fun run() {
            val config = Configuration(this.inputJar, this.outputJar)
            Deobfuscator(config).start()
        }
    }

    @JvmStatic
    fun main(args: Array<String>) = Cli().main(args)
    
}